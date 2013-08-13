package com.hk.impl.service.shippingOrder;

import com.akube.framework.dao.Page;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.analytics.Reason;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.order.*;
import com.hk.domain.shippingOrder.LifecycleReason;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.service.queue.BucketService;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.ReconciliationStatusDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ReplacementOrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.HKDateUtil;
import com.hk.util.OrderUtil;
import com.hk.util.TokenUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author vaibhav.adlakha
 */
@Service
public class ShippingOrderServiceImpl implements ShippingOrderService {

	private Logger						logger = LoggerFactory.getLogger(ShippingOrderServiceImpl.class);
    @Autowired
    private UserService                userService;
    @Autowired
    private InventoryService           inventoryService;
    @Autowired
    private ShippingOrderDao           shippingOrderDao;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    private ReconciliationStatusDao    reconciliationStatusDao;
    @Autowired
    private LineItemDao                lineItemDao;
    @Autowired
    private ReplacementOrderDao        replacementOrderDao;
    @Autowired
	private EmailManager        emailManager;
    @Autowired
    BucketService bucketService;
    private OrderService               orderService;
	private ShipmentService 				shipmentService;
    @Autowired
    private RewardPointService rewardPointService;
    @Autowired
    SkuItemDao skuItemDao;
    @Autowired
    BaseDao baseDao;
    @Autowired
    SkuItemLineItemService skuItemLineItemService;


	public ShippingOrder findByGatewayOrderId(String gatewayOrderId) {
        return getShippingOrderDao().findByGatewayOrderId(gatewayOrderId);
    }

    public ShippingOrder find(Long shippingOrderId) {
        return getShippingOrderDao().findById(shippingOrderId);
    }

    @Transactional
    public ShippingOrder save(ShippingOrder shippingOrder) {
        return (ShippingOrder) getShippingOrderDao().save(shippingOrder);
    }

//    public ShippingOrder saveStaus(ShippingOrder shippingOrder,Integer newStatus){
//      return getShippingOrderDao().saveStatus(shippingOrder,newStatus);
//    }

    public List<ShippingOrder> searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria) {
        return searchShippingOrders(shippingOrderSearchCriteria, true);
    }

    public List<ShippingOrder> searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, boolean isSearchForWarehouse) {
        if (isSearchForWarehouse) {
            setWarehouseOnShippingOrderSearchCriteria(shippingOrderSearchCriteria);
        }
        return getShippingOrderDao().searchShippingOrders(shippingOrderSearchCriteria);
    }

    public Page searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, boolean isSearchForWarehouse, int pageNo, int perPage) {
        if (isSearchForWarehouse) {
            setWarehouseOnShippingOrderSearchCriteria(shippingOrderSearchCriteria);
        }
        /*
         * else{ shippingOrderSearchCriteria.setWarehouseId(warehouseService.getCorporateOffice().getId()); }
         */
        return getShippingOrderDao().searchShippingOrders(shippingOrderSearchCriteria, pageNo, perPage);
    }

    private void setWarehouseOnShippingOrderSearchCriteria(ShippingOrderSearchCriteria shippingOrderSearchCriteria) {
        Warehouse warehouse = getUserService().getWarehouseForLoggedInUser();
        if (warehouse != null && warehouse.getId() != null) {
            shippingOrderSearchCriteria.setWarehouseId(warehouse.getId());
        }
    }

    public List<ShippingOrder> getShippingOrdersToSendShipmentEmail() {
        return getShippingOrderDao().getShippingOrdersToSendShipmentEmail();
    }

    public ShippingOrderLifeCycleActivity getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity) {
        return getShippingOrderDao().getShippingOrderLifeCycleActivity(enumShippingOrderLifecycleActivity);
    }

    @Override
    @Transactional
    public ShippingOrder setGatewayIdAndTargetDateOnShippingOrder(ShippingOrder shippingOrder) {
        String shippingOrderGatewayId = TokenUtils.generateShippingOrderGatewayOrderId(shippingOrder);
        shippingOrder.setGatewayOrderId(shippingOrderGatewayId);
        if (shippingOrder instanceof ReplacementOrder) {
            setTargetDispatchDelDatesOnSO(new Date(), shippingOrder);
        } else {
            setTargetDispatchDelDatesOnSO(shippingOrder.getBaseOrder().getPayment().getPaymentDate(), shippingOrder);
        }
        return shippingOrder;
    }

    @Transactional
    @Override
    public void setTargetDispatchDelDatesOnSO(Date refDate, ShippingOrder shippingOrder) {
        Long[] dispatchDays = OrderUtil.getDispatchDaysForSO(shippingOrder);
        Date targetDispatchDate = HKDateUtil.addToDate(refDate, Calendar.DAY_OF_MONTH, dispatchDays[1].intValue());
        shippingOrder.setTargetDispatchDate(targetDispatchDate);

        //todo need to write correct logic for targetDeliveryDate, based on historical TAT
        shippingOrder.setTargetDelDate(targetDispatchDate);
        getShippingOrderDao().save(shippingOrder);
    }

    /**
     * Creates a shipping order with basic details
     * 
     * @param baseOrder
     * @param warehouse
     * @return
     */
    public ShippingOrder createSOWithBasicDetails(Order baseOrder, Warehouse warehouse) {
        ShippingOrder shippingOrder = new ShippingOrder();
        shippingOrder.setBaseOrder(baseOrder);
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ActionAwaiting));
        shippingOrder.setCreateDate(new Date());
        // shippingOrder.setUpdateDate(new Date());
        shippingOrder.setWarehouse(warehouse);
        shippingOrder.setAmount(0D);
        shippingOrder.setReconciliationStatus(getReconciliationStatusDao().getReconciliationStatusById(EnumReconciliationStatus.PENDING));

        return shippingOrder;
    }

    @Override
    @Transactional
    public void nullifyCodCharges(ShippingOrder shippingOrder) {
        Double codChargesApplied = 0D;
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            codChargesApplied += lineItem.getCodCharges();
            lineItem.setCodCharges(0D);
            lineItemDao.save(lineItem);
        }
        shippingOrder.setAmount(shippingOrder.getAmount() - codChargesApplied);
        save(shippingOrder);
    }

    public ShippingOrderLifecycle logShippingOrderActivity(ShippingOrder shippingOrder, EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity) {
        User loggedOnUser = getUserService().getLoggedInUser();
        // User loggedOnUser = UserCache.getInstance().getLoggedInUser();
        if (loggedOnUser == null) {
            loggedOnUser = userService.getAdminUser();
        }

        ShippingOrderLifeCycleActivity orderLifecycleActivity = getShippingOrderLifeCycleActivity(enumShippingOrderLifecycleActivity);
        return logShippingOrderActivity(shippingOrder, loggedOnUser, orderLifecycleActivity, null, null);
    }

    public ShippingOrderLifecycle logShippingOrderActivity(ShippingOrder shippingOrder, EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity, Reason reason, String comments) {
        User loggedOnUser = getUserService().getLoggedInUser();
        // User loggedOnUser = UserCache.getInstance().getLoggedInUser();
        if (loggedOnUser == null) {
            loggedOnUser = shippingOrder.getBaseOrder().getUser();
        }
        ShippingOrderLifeCycleActivity orderLifecycleActivity = getShippingOrderLifeCycleActivity(enumShippingOrderLifecycleActivity);
        return logShippingOrderActivity(shippingOrder, loggedOnUser, orderLifecycleActivity, reason, comments);
    }

    public ShippingOrderLifecycle logShippingOrderActivity(ShippingOrder shippingOrder, User user, ShippingOrderLifeCycleActivity shippingOrderLifeCycleActivity, Reason reason, String comments) {
        ShippingOrderLifecycle shippingOrderLifecycle = new ShippingOrderLifecycle();
        shippingOrderLifecycle.setShippingOrder(shippingOrder);
        shippingOrderLifecycle.setShippingOrderLifeCycleActivity(shippingOrderLifeCycleActivity);
        shippingOrderLifecycle.setUser(user);
        shippingOrderLifecycle.setComments(comments);
        shippingOrderLifecycle.setActivityDate(new Date());
        shippingOrderLifecycle = (ShippingOrderLifecycle) getShippingOrderDao().save(shippingOrderLifecycle);
        logReason(shippingOrderLifecycle, reason);
        return shippingOrderLifecycle;
    }

    public ShippingOrderLifecycle logShippingOrderActivityByAdmin(ShippingOrder shippingOrder, EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity, Reason reason) {
        ShippingOrderLifeCycleActivity orderLifecycleActivity = getShippingOrderLifeCycleActivity(enumShippingOrderLifecycleActivity);
        return logShippingOrderActivity(shippingOrder, userService.getAdminUser(), orderLifecycleActivity, reason, null);
    }

    private void logReason(ShippingOrderLifecycle shippingOrderLifecycle, Reason reason){
        if(reason != null){
            LifecycleReason lifecycleReason = new LifecycleReason();
            lifecycleReason.setShippingOrderLifecycle(shippingOrderLifecycle);
            lifecycleReason.setReason(reason);
            getShippingOrderDao().save(lifecycleReason);
        }
    }

    @Override
    public boolean shippingOrderHasReplacementOrder(ShippingOrder shippingOrder) {
        return getReplacementOrderDao().getReplacementOrderFromShippingOrder(shippingOrder.getId()) != null
                && getReplacementOrderDao().getReplacementOrderFromShippingOrder(shippingOrder.getId()).size() > 0;
    }

	public Page searchShippingOrders(ShippingOrderSearchCriteria shippingOrderSearchCriteria, int pageNo, int perPage) {
        return searchShippingOrders(shippingOrderSearchCriteria, true, pageNo, perPage);
    }
	
	public boolean shippingOrderContainsProductVariant(ShippingOrder shippingOrder, ProductVariant productVariant, Double mrp) {
		Set<LineItem> items = shippingOrder.getLineItems();
		if(items!=null && items.size()>0){
			for(LineItem item : items){
				if(item.getSku().getProductVariant().equals(productVariant)&&item.getMarkedPrice().equals(mrp)){
					return true;
				}
			}
		}
		return false;
		
	}

    
	public void validateShippingOrder(ShippingOrder shippingOrder) {
		Set<LineItem> lineItems = shippingOrder.getLineItems();
		for (LineItem lineItem : lineItems) {
			List<Sku> skuList = new ArrayList<Sku>();
			List<Long> skuStatusIdList = new ArrayList<Long>();
			List<Long> skuItemOwnerList = new ArrayList<Long>();
			skuStatusIdList.add(EnumSkuItemStatus.Checked_IN.getId());
			skuList.add(lineItem.getSku());
			skuItemOwnerList.add(EnumSkuItemOwner.SELF.getId());

			// When CLI and LI are empty
			if (lineItem.getCartLineItem().getSkuItemCLIs() == null
					|| (lineItem.getCartLineItem().getSkuItemCLIs() != null && lineItem.getCartLineItem().getSkuItemCLIs().size() == 0)) {
				Long qty = lineItem.getQty();
				List<SkuItemLineItem> skuItemLineItems = new ArrayList<SkuItemLineItem>();
				List<SkuItemCLI> skuItemCLIs = new ArrayList<SkuItemCLI>();
				List<SkuItem> checkAvailableUnbookedSkuItems = skuItemDao.getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, lineItem.getMarkedPrice());
				logger.debug("Available Unbooked Inventory For Sku - " + lineItem.getSku() + " at MRP - " + lineItem.getMarkedPrice() + " is "
						+ checkAvailableUnbookedSkuItems.size());
				if (checkAvailableUnbookedSkuItems.size() >= qty) {
					int i = 1;
					while (i <= qty) {
						SkuItemLineItem skuItemLineItem = new SkuItemLineItem();
						SkuItemCLI skuItemCLI = new SkuItemCLI();
						// get available skuitems warehouse at given mrp
						List<SkuItem> availableUnbookedSkuItems = skuItemDao.getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, lineItem.getMarkedPrice());
						if (availableUnbookedSkuItems != null && availableUnbookedSkuItems.size() > 0) {
							SkuItem skuItem = availableUnbookedSkuItems.get(0);
							// Book the sku item first
							skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
							skuItem = (SkuItem) baseDao.save(skuItem);
							// create skuItemCLI entry
							skuItemCLI.setSkuItem(skuItem);
							skuItemCLI.setCartLineItem(lineItem.getCartLineItem());
							skuItemCLI.setUnitNum((long) i);
							skuItemCLI.setCreateDate(new Date());
							skuItemCLI.setProductVariant(lineItem.getSku().getProductVariant());
							// create skuItemLineItem entry
							skuItemLineItem.setSkuItem(skuItem);
							skuItemLineItem.setLineItem(lineItem);
							skuItemLineItem.setUnitNum((long) i);
							skuItemLineItem.setSkuItemCLI(skuItemCLI);
							skuItemLineItem.setCreateDate(new Date());
							skuItemLineItem.setProductVariant(lineItem.getSku().getProductVariant());
							skuItemLineItems.add(skuItemLineItem);
							skuItemCLIs.add(skuItemCLI);
						}
						++i;
					}
					baseDao.saveOrUpdate(skuItemCLIs);
					baseDao.saveOrUpdate(skuItemLineItems);
					lineItem.getCartLineItem().setSkuItemCLIs(skuItemCLIs);
					baseDao.save(lineItem.getCartLineItem());
					lineItem.setSkuItemLineItems(skuItemLineItems);
					lineItem = (LineItem) baseDao.save(lineItem);
					logger.debug("Populated Table SkuItemLineItem for Line Item - " + lineItem.getId() + " for Cart Line Item - " + lineItem.getCartLineItem().getId()
							+ " of Shipping Order - " + lineItem.getShippingOrder().getId());
					logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_LoggedComment, null,
							"Inventory booked for variant:- " + lineItem.getSku().getProductVariant());
					inventoryService.checkInventoryHealth(lineItem.getSku().getProductVariant());
				} else {
					logger.debug("Could Not Populate Tables for Line Item - " + lineItem.getId() + " for Cart Line Item - " + lineItem.getCartLineItem().getId()
							+ " of Shipping Order - " + lineItem.getShippingOrder().getId());
					logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_LoggedComment, null,
							"Inventory could not be booked for variant:- " + lineItem.getSku().getProductVariant());
				}
			}
			// When there are entries in SILI and SICLI; but MRP related fixes
			// are needed
			else if (lineItem.getCartLineItem().getSkuItemCLIs() != null && lineItem.getCartLineItem().getSkuItemCLIs().size() > 0
					&& lineItem.getSkuItemLineItems() != null && lineItem.getSkuItemLineItems().size() > 0) {
				int entries = lineItem.getSkuItemLineItems().size();
				int j = 1;
				while (j <= entries) {
					SkuItemLineItem skuItemLineItem = lineItem.getSkuItemLineItems().get(j - 1);
					List<SkuItemStatus> skuItemStatus = new ArrayList<SkuItemStatus>();
					skuItemStatus.add(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
					skuItemStatus.add(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());

					if (!skuItemLineItem.getSkuItem().getSkuGroup().getMrp().equals(lineItem.getMarkedPrice())
							|| !skuItemStatus.contains(skuItemLineItem.getSkuItem().getSkuItemStatus())) {
						// get sku items of the given warehouse at mrp
						List<SkuItem> availableUnbookedSkuItems = skuItemDao.getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, lineItem.getMarkedPrice());
						if (availableUnbookedSkuItems != null && availableUnbookedSkuItems.size() > 0) {
							SkuItem skuItem = skuItemLineItem.getSkuItem();
							skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
							skuItem = (SkuItem) baseDao.save(skuItem);

							SkuItem skuItemToBeSet = availableUnbookedSkuItems.get(0);
							skuItemToBeSet.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
							skuItemToBeSet = (SkuItem) baseDao.save(skuItemToBeSet);

							skuItemLineItem.getSkuItemCLI().setSkuItem(skuItemToBeSet);
							baseDao.save(skuItemLineItem.getSkuItemCLI());
							skuItemLineItem.setSkuItem(skuItemToBeSet);
							skuItemLineItem = (SkuItemLineItem) baseDao.save(skuItemLineItem);
							logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_LoggedComment, null,
									"Handled SkuItem mismatch and booked the correct inventory here For Variant:- " + lineItem.getSku().getProductVariant()
											+ " of this Shipping Order." + " The new SkuItem is - " + skuItemToBeSet.getId());
						}
					}
					++j;
				}
				inventoryService.checkInventoryHealth(lineItem.getSku().getProductVariant());
			} else if (lineItem.getCartLineItem().getSkuItemCLIs() != null && lineItem.getCartLineItem().getSkuItemCLIs().size() > 0
					&& (lineItem.getSkuItemLineItems() == null || (lineItem.getSkuItemLineItems() != null && lineItem.getSkuItemLineItems().size() == 0))) {
				Boolean skuItemLineItemStatus = skuItemLineItemService.createNewSkuItemLineItem(lineItem);
				if (!skuItemLineItemStatus) {
					logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_LoggedComment, null,
							"Inventory could not be booked for variant:- " + lineItem.getSku().getProductVariant());
				} else {
					logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_LoggedComment, null,
							"Inventory booked for variant:- " + lineItem.getSku().getProductVariant());
					inventoryService.checkInventoryHealth(lineItem.getSku().getProductVariant());
				}

			}
		}
	}
	
    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public OrderService getOrderService() {
        if (orderService == null) {
            orderService = ServiceLocatorFactory.getService(OrderService.class);
        }
        return orderService;
    }

	 public ShipmentService getShipmentService() {
        if (shipmentService == null) {
            shipmentService = ServiceLocatorFactory.getService(ShipmentService.class);
        }
        return shipmentService;
    }


    public ShippingOrderDao getShippingOrderDao() {
        return shippingOrderDao;
    }

    public void setShippingOrderDao(ShippingOrderDao shippingOrderDao) {
        this.shippingOrderDao = shippingOrderDao;
    }

    public ReconciliationStatusDao getReconciliationStatusDao() {
        return reconciliationStatusDao;
    }

    public void setReconciliationStatusDao(ReconciliationStatusDao reconciliationStatusDao) {
        this.reconciliationStatusDao = reconciliationStatusDao;
    }

    public ShippingOrderStatusService getShippingOrderStatusService() {
        return shippingOrderStatusService;
    }

    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }

    public ReplacementOrderDao getReplacementOrderDao() {
        return replacementOrderDao;
    }

    public List<Reason> getReasonForReversePickup(List<Long> listOfReasonIds){
        return  shippingOrderDao.getReasonForReversePickup(listOfReasonIds);
    }
}
