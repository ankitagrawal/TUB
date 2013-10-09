package com.hk.admin.impl.service.queue;

import com.akube.framework.dao.Page;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.queue.EnumActionTask;
import com.hk.constants.queue.EnumBucket;
import com.hk.constants.queue.EnumTrafficState;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ActionItemSearchCriteria;
import com.hk.domain.analytics.Reason;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.Param;
import com.hk.domain.queue.TrafficState;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.ForeignSkuItemCLI;
import com.hk.impl.service.queue.BucketService;
import com.hk.pact.dao.queue.ActionItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.util.BucketAllocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * User: Pratham
 * Date: 10/04/13  Time: 16:04
*/
@Service
public class BucketServiceImpl implements BucketService {

  private static Logger logger = LoggerFactory.getLogger(BucketServiceImpl.class);
    @Autowired
    UserService userService;

    @Autowired
    ActionItemDao actionItemDao;

    @Autowired
    InventoryService inventoryService;

    @Override
    public List<Param> getParamsForBucket(List<Bucket> bucketList) {
        List<Param> params = new ArrayList();
        for (Bucket bucket : bucketList) {
            params.addAll(bucket.getParams());
        }
        return params;
    }

    @Override
    public Map<String, Object> getParamMap(List<Bucket> bucketList) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        List<Param> params = getParamsForBucket(bucketList);
        for (Param param : params) {
            parameters.put(param.getParamName(), param.getParamValues());
        }
        return parameters;
    }

    @Override
    public ActionItem saveActionItem(ActionItem actionItem) {
        return actionItemDao.save(actionItem);
    }

    @Override
    public Bucket find(EnumBucket enumBucket) {
        return actionItemDao.get(Bucket.class, enumBucket.getId());
    }

    @Override
    public List<Bucket> findByName(List<String> bucketNames) {
        return actionItemDao.findByName(bucketNames);
    }

    @Override
    public ActionTask find(EnumActionTask enumActionTask) {
        return actionItemDao.get(ActionTask.class, enumActionTask.getId());
    }

    @Override
    public List<Bucket> getBuckets(List<EnumBucket> enumBuckets) {
        return actionItemDao.getBuckets(enumBuckets);
    }

    @Override
    public List<Bucket> listAll() {
        return actionItemDao.getAll(Bucket.class);
    }

    @Override
    public Page searchActionItems(ActionItemSearchCriteria actionItemSearchCriteria, int pageNo, int perPage) {
        return actionItemDao.searchActionItems(actionItemSearchCriteria, pageNo, perPage);
    }

    @Override
    public ActionItem existsActionItem(ShippingOrder shippingOrder) {
        return actionItemDao.searchActionItem(shippingOrder);
    }

    public ActionItem autoCreateUpdateActionItem(ShippingOrder shippingOrder) {
        ActionItem actionItem = null;
        if (EnumShippingOrderStatus.getStatusIdsForActionQueue().contains(shippingOrder.getOrderStatus().getId())) {
            actionItem = existsActionItem(shippingOrder);
            if (actionItem == null) {
                actionItem = autoCreateActionItem(shippingOrder);
            } else {
                actionItem = autoUpdateActionItem(actionItem, true); //normally would be called afterCodConfirmation/payment-authorization/manual-split
            }
            actionItem.setReporter(userService.getAdminUser());
            return saveActionItem(actionItem);
        } else {
            popFromActionQueue(shippingOrder);
        }
        return actionItem;
    }

    @Override
    public void updateCODBucket(Order order) {
        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
            autoCreateUpdateActionItem(shippingOrder);
        }
    }

    public ActionItem autoUpdateActionItem(ActionItem actionItem, boolean autoUpdate) {
        List<Bucket> actionableBuckets;
        if (autoUpdate) {
            actionableBuckets = getBuckets(autoCreateDefaultBuckets(actionItem.getShippingOrder()));
        } else {
            actionableBuckets = actionItem.getBuckets();
        }
        actionItem.setBuckets(actionableBuckets);
        actionItem.setPreviousActionTask(actionItem.getCurrentActionTask());
        actionItem.setCurrentActionTask(find(assignActionTask(actionableBuckets)));
        return actionItem;
    }

    protected ActionItem autoCreateActionItem(ShippingOrder shippingOrder) {
        ActionItem actionItem = new ActionItem();
        actionItem.setShippingOrder(shippingOrder);
        actionItem.setFirstPushDate(new Date());
        actionItem.setTrafficState(EnumTrafficState.NORMAL.asTrafficState());
        List<Bucket> actionableBuckets = getBuckets(autoCreateDefaultBuckets(shippingOrder));
        actionItem.setBuckets(actionableBuckets);
        actionItem.setPreviousActionTask(actionItem.getCurrentActionTask());
        actionItem.setCurrentActionTask(find(assignActionTask(actionableBuckets)));
        return actionItem;
    }

    protected List<EnumBucket> autoCreateDefaultBuckets(ShippingOrder shippingOrder) {
        List<EnumBucket> actionableBuckets = BucketAllocator.allocateBuckets(shippingOrder);
        Payment payment = shippingOrder.getBaseOrder().getPayment();
        if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(payment.getPaymentStatus().getId())) {
            actionableBuckets.addAll(getCategoryDefaultersBuckets(shippingOrder));
        }
        return actionableBuckets;
    }

    protected List<Bucket> computeEscalateBackBuckets(ShippingOrder shippingOrder) {
        Reason reason = shippingOrder.getReason();
        List<Bucket> actionableBuckets = new ArrayList<Bucket>();
        if (reason != null) {
            actionableBuckets.addAll(reason.getBuckets());
        } else {
            actionableBuckets.add(find(EnumBucket.AD_HOC));
        }
        if (actionableBuckets.contains(find(EnumBucket.CM))) {
            actionableBuckets.addAll(getBuckets(BucketAllocator.getBucketsFromSOC(shippingOrder)));
            actionableBuckets.remove(find(EnumBucket.CM));
        }
        return actionableBuckets;
    }

    protected List<Bucket> computeEscalateForwardBuckets(ShippingOrder shippingOrder) {
        List<Bucket> actionableBuckets = new ArrayList<Bucket>();
        Bucket decidedBucket = shippingOrder.isDropShipping() ? find(EnumBucket.Vendor) : shippingOrder.isServiceOrder() ? find(EnumBucket.ServiceOrder) : find(EnumBucket.Warehouse);
        actionableBuckets.add(decidedBucket);
        return actionableBuckets;
    }

    protected EnumActionTask assignActionTask(List<Bucket> buckets) {
        return BucketAllocator.listCurrentActionTask(buckets);
    }

    //called just after SO is escalated
    public ActionItem escalateOrderFromActionQueue(ShippingOrder shippingOrder) {
        ActionItem actionItem = existsActionItem(shippingOrder);
        //in long term, this shouldn't be the case as all SO will already have their actionItem
        if (actionItem == null) {
            actionItem = autoCreateActionItem(shippingOrder);
        }
        //pretty much all is preDecided here, since its no longer a hot action task
        actionItem.setBuckets(computeEscalateForwardBuckets(shippingOrder));
        actionItem.setPopDate(new Date());
        actionItem.setFlagged(false);
        actionItem.setReporter(userService.getLoggedInUser());
        actionItem.setTrafficState(EnumTrafficState.NORMAL.asTrafficState());
        actionItem.setPreviousActionTask(actionItem.getCurrentActionTask());
        actionItem.setCurrentActionTask(find(EnumActionTask.WH_Processing));
        return saveActionItem(actionItem);
    }

    public ActionItem escalateBackToActionQueue(ShippingOrder shippingOrder) {
        ActionItem actionItem = existsActionItem(shippingOrder);
        if (actionItem == null) {
            actionItem = autoCreateActionItem(shippingOrder);
        }
        actionItem.setReporter(userService.getLoggedInUser());
        actionItem.setBuckets(computeEscalateBackBuckets(shippingOrder));
        actionItem.setPreviousActionTask(actionItem.getCurrentActionTask());
        actionItem.setCurrentActionTask(computeEscalateBackActionTask(shippingOrder));
        actionItem.setLastPushDate(new Date());
        actionItem.setFlagged(true);
        actionItem.setTrafficState(EnumTrafficState.RED.asTrafficState());
        return saveActionItem(actionItem);
    }

    private ActionTask computeEscalateBackActionTask(ShippingOrder shippingOrder) {
        Reason reason = shippingOrder.getReason();
        if (reason != null) {
            return reason.getActionTask() != null ? reason.getActionTask() : EnumActionTask.AD_HOC.asActionTask();
        }
        return EnumActionTask.AD_HOC.asActionTask();
    }

    @Override
    public void popFromActionQueue(ShippingOrder shippingOrder) {
        ActionItem actionItem = existsActionItem(shippingOrder);
        if (actionItem != null) {
            shippingOrder.setActionItem(null);
            actionItemDao.save(shippingOrder);
            //release all dependencies
            Iterator<Bucket> bucketIterator = actionItem.getBuckets().iterator();
            while ( bucketIterator.hasNext() ){
            	Bucket bucket = bucketIterator.next();
            	bucket.getActionItems().remove(actionItem);
            	actionItemDao.save(bucket);
            }
            actionItem.getBuckets().clear();
            actionItem.getWatchers().clear();
            saveActionItem(actionItem);
            actionItemDao.refresh(actionItem);
            actionItemDao.delete(actionItem);
        }
    }

    @Override
    public Bucket getBucketById(Long bucketId) {
        return actionItemDao.get(Bucket.class, bucketId);
    }

    @Override
    public ActionItem getActionItemById(Long actionItemId) {
        return actionItemDao.get(ActionItem.class, actionItemId);
    }

    @Override
    public List<ActionTask> listNextActionTasks(ActionItem actionItem) {
        return actionItemDao.listNextActionTasks(actionItem.getCurrentActionTask(), actionItem.getBuckets());
    }

    @Override
    public List<EnumBucket> getCategoryDefaultersBuckets(ShippingOrder shippingOrder) {
      List<EnumBucket> actionableBuckets = new ArrayList<EnumBucket>();
      Set<String> categoryNames = new HashSet<String>();
      for (LineItem lineItem : shippingOrder.getLineItems()) {
        Long availableUnbookedInv = inventoryService.getAvailableUnbookedInventory(lineItem.getSku(), lineItem.getMarkedPrice());
        ProductVariant productVariant = lineItem.getSku().getProductVariant();

        Long availableNetPhysicalInventory = inventoryService.getAvailableUnbookedInventory(Arrays.asList(lineItem.getSku()), false);
        Long bookedQty = 0L;
        Long orderedQty = lineItem.getQty();
        if (lineItem.getSkuItemLineItems() != null) {
          bookedQty = (long) lineItem.getSkuItemLineItems().size();
        }
        logger.debug("Available unbooked Inventory of -- variant " + lineItem.getSku().getProductVariant().getId() + "--" + availableUnbookedInv + " --Net inventory --"  + availableNetPhysicalInventory
            + "--Booked qty -- " + bookedQty + "--ordered qty --" + orderedQty);
        List<ForeignSkuItemCLI> fsiclis  = lineItem.getCartLineItem().getForeignSkuItemCLIs();
        if (fsiclis != null && fsiclis.size() > 0) {
          categoryNames.add(productVariant.getProduct().getPrimaryCategory().getName());
        }
        if (!(bookedQty >= orderedQty)) {
          if (availableNetPhysicalInventory < 0 || availableUnbookedInv < 0) {
            categoryNames.add(productVariant.getProduct().getPrimaryCategory().getName());
          }
        }
        if (lineItem.getCartLineItem().getCartLineItemConfig() != null || !productVariant.getProductExtraOptions().isEmpty()) {
          categoryNames.add(productVariant.getProduct().getPrimaryCategory().getName());
        }
      }
      actionableBuckets.addAll(EnumBucket.findByName(categoryNames));
      return actionableBuckets;
    }


    public List<ActionItem> getActionItemsOfActionQueue() {

        return actionItemDao.getActionItemsOfActionQueue();
    }
}
