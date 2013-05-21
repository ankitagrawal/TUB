package com.hk.admin.impl.service.queue;

import com.hk.constants.queue.EnumActionTask;
import com.hk.constants.queue.EnumBucket;
import com.hk.constants.queue.EnumTrafficState;
import com.hk.domain.analytics.Reason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.Param;
import com.hk.impl.service.queue.BucketService;
import com.hk.pact.dao.queue.ActionItemDao;
import com.hk.pact.service.UserService;
import com.hk.util.BucketAllocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * User: Pratham
 * Date: 10/04/13  Time: 16:04
*/
@Service
public class BucketServiceImpl implements BucketService {

    @Autowired
    UserService userService;

    @Autowired
    ActionItemDao actionItemDao;

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
    public ActionItem existsActionItem(ShippingOrder shippingOrder) {
        return actionItemDao.searchActionItem(shippingOrder);
    }

    public ActionItem autoCreateUpdateActionItem(ShippingOrder shippingOrder) {
        ActionItem actionItem = existsActionItem(shippingOrder);
        if (actionItem == null) {
            actionItem = autoCreateActionItem(shippingOrder);
        } else {
            actionItem = autoUpdateActionItem(actionItem); //normally would be called afterCodConfirmation/payment-authorization/manual-split
        }
        actionItem.setReporter(userService.getAdminUser());
        return saveActionItem(actionItem);
    }

    protected ActionItem autoUpdateActionItem(ActionItem actionItem) {
        List<Bucket> actionableBuckets = getBuckets(autoCreateDefaultBuckets(actionItem.getShippingOrder()));
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
        return BucketAllocator.allocateBuckets(shippingOrder);
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
        actionItem.setCurrentActionTask(find(assignActionTask(actionItem.getBuckets())));
        actionItem.setLastPushDate(new Date());
        actionItem.setFlagged(true);
        actionItem.setTrafficState(EnumTrafficState.RED.asTrafficState());
        return saveActionItem(actionItem);
    }

    @Override
    public void popFromActionQueue(ShippingOrder shippingOrder) {
        ActionItem actionItem = existsActionItem(shippingOrder);
        if (actionItem != null) {
            //release all dependencies
           actionItem.getBuckets().clear();
           actionItem.getWatchers().clear();
           actionItem = saveActionItem(actionItem);
           actionItemDao.delete(actionItem);
        }
    }

    @Override
    public Bucket getBucketById(Long bucketId){
        return actionItemDao.get(Bucket.class,bucketId);
    }

}
