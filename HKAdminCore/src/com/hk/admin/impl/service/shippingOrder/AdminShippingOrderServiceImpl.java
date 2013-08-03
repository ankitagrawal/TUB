package com.hk.admin.impl.service.shippingOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.discount.EnumRewardPointTxnType;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointTxn;
import com.hk.domain.user.UserAccountInfo;
import com.hk.pact.dao.reward.RewardPointDao;
import com.hk.pact.dao.reward.RewardPointTxnDao;
import com.hk.pact.dao.user.UserAccountInfoDao;
import com.hk.pact.service.order.RewardPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.EnumJitShippingOrderMailToCategoryReason;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Shipment;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.shippingOrder.ShippingOrderCategory;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.impl.service.queue.BucketService;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.InventoryHealthService.FetchType;
import com.hk.pact.service.inventory.InventoryHealthService.SkuFilter;
import com.hk.pact.service.inventory.InventoryHealthService.SkuInfo;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.service.ServiceLocatorFactory;

@Service
public class AdminShippingOrderServiceImpl implements AdminShippingOrderService {

    private static Logger logger = LoggerFactory.getLogger(AdminShippingOrderServiceImpl.class);
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private AdminInventoryService adminInventoryService;
    @Autowired
    private BucketService bucketService;
    @Autowired
    private PincodeCourierService pincodeCourierService;
    private String cancellationRemark;

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private WarehouseService warehouseService;
    private AdminOrderService adminOrderService;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private AdminShippingOrderDao adminShippingOrderDao;
    @Autowired
    AwbService awbService;
    @Autowired
    UserService userService;
    @Autowired
	AdminEmailManager adminEmailManager;

    @Autowired
    private RewardPointTxnDao rewardPointTxnDao;
    @Autowired
    RewardPointService rewardPointService;

    @Autowired
    private LoyaltyProgramService loyaltyProgramService;
    
    @Autowired InventoryHealthService inventoryHealthService;
    
    @Autowired BaseDao baseDao;
    @Autowired
	PurchaseOrderService purchaseOrderService;
    @Autowired
    private UserAccountInfoDao userAccountInfoDao;
    @Autowired
    private RewardPointDao rewardPointDao;

    @Autowired LineItemDao lineItemDao;
    @Autowired ShippingOrderDao shippingOrderDao;

    @Autowired LineItemDao lineItemDao;
    @Autowired ShippingOrderDao shippingOrderDao;
    
    public void cancelShippingOrder(ShippingOrder shippingOrder,String cancellationRemark) {
        // Check if Order is in Action Queue before cancelling it.
        if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
	          logger.warn("Cancelling Shipping order gateway id:::"+ shippingOrder.getGatewayOrderId());
            shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Cancelled));
            //shippingOrder = getShippingOrderService().save(shippingOrder);
            getAdminInventoryService().reCheckInInventory(shippingOrder);
            // TODO : Write a generic ROLLBACK util which will essentially release all attached laibilities i.e.
            // inventory, reward points, shipment, discount
            getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Cancelled,shippingOrder.getReason(),cancellationRemark);

            orderService.updateOrderStatusFromShippingOrders(shippingOrder.getBaseOrder(), EnumShippingOrderStatus.SO_Cancelled, EnumOrderStatus.Cancelled);
            if(shippingOrder.getShipment()!= null){
                Awb awbToRemove = shippingOrder.getShipment().getAwb();
                awbService.preserveAwb(awbToRemove);
                Shipment shipmentToDelete = shippingOrder.getShipment();
                shippingOrder.setShipment(null);
	            shipmentService.delete(shipmentToDelete);
	            //shippingOrderService.save(shippingOrder);
            }
            shippingOrder = getShippingOrderService().save(shippingOrder);
            if(shippingOrder.getPurchaseOrders()!=null && shippingOrder.getPurchaseOrders().size()>0){
            	adminEmailManager.sendJitShippingCancellationMail(shippingOrder,null, EnumJitShippingOrderMailToCategoryReason.SO_CANCELLED);
            }

            reconcileRPLiabilities(shippingOrder,shippingOrder.getBaseOrder());

            getBucketService().popFromActionQueue(shippingOrder);
        }
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            getInventoryService().checkInventoryHealth(lineItem.getSku().getProductVariant());
        }
    }


    public void reconcileRPLiabilities(ShippingOrder shippingOrder, Order order) {
        Double refundValue = 0D;
        if (shippingOrder == null) {
            refundValue = order.getPayment().getAmount();
        } else {
            refundValue = shippingOrder.getAmount(); //TODO: handle for free cases
        }
        Double percentageAmount = refundValue / order.getPayment().getAmount();

        // first redeem redeem points used by this SO/BO
        List<RewardPointTxn> rewardPointTxnList = rewardPointTxnDao.findByTxnTypeAndOrder(EnumRewardPointTxnType.REDEEM, order);
        for (RewardPointTxn rewardPointTxn : rewardPointTxnList) {
            rewardPointTxnDao.createRefundTxn(rewardPointTxn, rewardPointTxn.getValue()*percentageAmount);
        }


        /*List<RewardPoint> currentRewardPoints = rewardPointService.findRewardPoints(order, EnumRewardPointMode.getCashBackModes());
        for (RewardPoint currentRewardPoint : currentRewardPoints) {
            List<RewardPointTxn> currentRewardPointTxnList = rewardPointTxnDao.findByRewardPoint(currentRewardPoint);
            for (RewardPointTxn rewardPointTxn : currentRewardPointTxnList) {
                if (rewardPointTxn.isType(EnumRewardPointTxnType.ADD)) {
                    RewardPoint cashBackRewardPoints = rewardPointService.addRewardPoints(order.getUser(), userService.getAdminUser(),
                            order, percentageAmount * rewardPointTxn.getValue(), "", EnumRewardPointStatus.APPROVED,
                            EnumRewardPointMode.HK_ADJUSTMENTS.asRewardPointMode());
                    rewardPointService.approveRewardPoints(Arrays.asList(cashBackRewardPoints), rewardPointTxn.getExpiryDate());
                }
            }
        }*/

        // cancel reward points in partial manner
        List<RewardPoint> currentRewardPoints = rewardPointService.findByReferredOrder(order);

        for (RewardPoint rewardPoint : currentRewardPoints) {

            List<RewardPointTxn> txnList = rewardPointTxnDao.findByRewardPoint(rewardPoint);
            Double totalAddedRewardPoints = 0D;
            Double totalUsedRewardPoints = 0D;
            for (RewardPointTxn rewardPointTxn : txnList) {
                if (!rewardPointTxn.isType(EnumRewardPointTxnType.REFERRED_ORDER_CANCELLED)) {
                    totalUsedRewardPoints += rewardPointTxn.getValue();
                    if (rewardPointTxn.isType(EnumRewardPointTxnType.ADD)) {
                        totalAddedRewardPoints = rewardPointTxn.getValue();
                    }
                }
            }

            // if total used is zero then, reward point has not been used yet, simple cancel totalRewardPointAdded
            if(totalUsedRewardPoints == 0 && totalAddedRewardPoints > 0) {
                rewardPointTxnDao.createRewardPointCancelTxn(rewardPoint, percentageAmount*totalAddedRewardPoints,txnList.get(0).getExpiryDate());
            } else {

                Double remainingRewardPoints = totalAddedRewardPoints - totalUsedRewardPoints;
                // now first cancel remaining reward points
                if (remainingRewardPoints > 0) {
                    rewardPointTxnDao.createRewardPointCancelTxn(rewardPoint, percentageAmount*totalAddedRewardPoints,txnList.get(0).getExpiryDate());
                }

                // cancel reward points from user total redeemable reward points in lieu of reward points used
                Double redeemablePoints = rewardPointService.getTotalRedeemablePoints(rewardPoint.getUser());
                if (redeemablePoints >= totalUsedRewardPoints) {
                    rewardPointService.cancelRewardPoints(rewardPoint.getUser(), percentageAmount*totalUsedRewardPoints);
                } else {

                    if(redeemablePoints > 0) {
                        rewardPointService.cancelRewardPoints(rewardPoint.getUser(), percentageAmount*redeemablePoints);
                    }

                    // intimate user that redeemable reward points are being canceled from his account at last SO cancel
                    UserAccountInfo userAccountInfo = getUserAccountInfoDao().getOrCreateUserAccountInfo(rewardPoint.getUser());
                    userAccountInfo.setOverusedRewardPoints(userAccountInfo.getOverusedRewardPoints() + percentageAmount*totalUsedRewardPoints);
                    getUserAccountInfoDao().save(userAccountInfo);
                }
            }

            // now cancel reward Point when its amount has been zero
            if(isZeroRewardPointBalance(rewardPoint)) {
               rewardPointDao.cancelRewardPoint(rewardPoint);
            }
        }



        /*List<RewardPointTxn> redeemedRewardPointTxnList = rewardPointTxnDao.findByTxnTypeAndOrder(EnumRewardPointTxnType.REDEEM, order);
        for (RewardPointTxn rewardPointTxn : redeemedRewardPointTxnList) {
            Order concernedOrder = rewardPointTxn.getRewardPoint().getReferredOrder();
            RewardPoint refundRewardPoints = rewardPointService.addRewardPoints(order.getUser(), userService.getAdminUser(),
                    concernedOrder, percentageAmount * rewardPointTxn.getValue(), "", EnumRewardPointStatus.APPROVED,
                    EnumRewardPointMode.HK_ADJUSTMENTS.asRewardPointMode());
            rewardPointService.approveRewardPoints(Arrays.asList(refundRewardPoints), rewardPointTxn.getExpiryDate());
        }*/

    }

    private boolean isZeroRewardPointBalance(RewardPoint rewardPoint) {
        Double totalBalance = 0D;
        List<RewardPointTxn> txnList = rewardPointTxnDao.findByRewardPoint(rewardPoint);
        for (RewardPointTxn rewardPointTxn : txnList) {
            totalBalance += rewardPointTxn.getValue();
        }
        return (totalBalance == 0);
    }

	public boolean updateWarehouseForShippingOrder(ShippingOrder shippingOrder, Warehouse warehouse) {
		Set<LineItem> lineItems = shippingOrder.getLineItems();
		boolean shouldUpdate = true;
		try {
			for (LineItem lineItem : lineItems) {
				SkuFilter filter = new SkuFilter();
				filter.setFetchType(FetchType.ALL);
				filter.setWarehouseId(warehouse.getId());
				filter.setMinQty(lineItem.getQty());
				filter.setMrp(lineItem.getMarkedPrice());
				Collection<SkuInfo> skus = inventoryHealthService.getAvailableSkus(lineItem.getCartLineItem().getProductVariant(), filter);

				if(skus != null && skus.size() > 0) {
					Sku sku = baseDao.get(Sku.class, skus.iterator().next().getSkuId());
					lineItem.setSku(sku);
				}
			}
			
			for (LineItem lineItem : lineItems) {
				if (!lineItem.getSku().getWarehouse().getId().equals(warehouse.getId())) {
					shouldUpdate = false;
				}
			}
			
			if (shouldUpdate) {
				shippingOrder.setWarehouse(warehouse);
				shipmentService.recreateShipment(shippingOrder);
				shippingOrder = getShippingOrderService().save(shippingOrder);
				if(shippingOrder.getShippingOrderStatus().equals(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus()) && shippingOrder.getPurchaseOrders()!=null && shippingOrder.getPurchaseOrders().size()>0){
				adminEmailManager.sendJitShippingCancellationMail(shippingOrder,null, EnumJitShippingOrderMailToCategoryReason.SO_WAREHOUSE_FLIPPED);
				}
				getShippingOrderService().logShippingOrderActivity(shippingOrder,
						EnumShippingOrderLifecycleActivity.SO_WarehouseChanged);

				// Re-checkin checkedout inventory in case of flipping.
				getAdminInventoryService().reCheckInInventory(shippingOrder);

			}

		} catch (NoSkuException noSku) {
			shouldUpdate = false;
		}
		return shouldUpdate;
	}
	
    public ShippingOrder createSOforManualSplit(Set<CartLineItem> cartLineItems, Warehouse warehouse) {

        if (cartLineItems != null && !cartLineItems.isEmpty() && warehouse != null) {
            Order baseOrder = cartLineItems.iterator().next().getOrder();
            ShippingOrder shippingOrder = getShippingOrderService().createSOWithBasicDetails(baseOrder, warehouse);
            shippingOrder.setBaseOrder(baseOrder);

            for (CartLineItem cartLineItem : cartLineItems) {
                ProductVariant productVariant = cartLineItem.getProductVariant();
                Sku sku = getSkuService().getSKU(productVariant, warehouse);
                if (sku != null) {
                    LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
                    shippingOrder.getLineItems().add(shippingOrderLineItem);
                } else {
                    throw new NoSkuException(productVariant, warehouse);
                }
            }

            ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, baseOrder);
            shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
            shippingOrder = getShippingOrderService().save(shippingOrder);
            /**
             * this additional call to save is done so that we have shipping order id to generate shipping order gateway
             * id
             */
            shippingOrder = getShippingOrderService().setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
            shippingOrder = getShippingOrderService().save(shippingOrder);
            Set<ShippingOrderCategory> categories = getOrderService().getCategoriesForShippingOrder(shippingOrder);
            shippingOrder.setShippingOrderCategories(categories);
            shippingOrder.setBasketCategory(getOrderService().getBasketCategory(categories).getName());
            shippingOrder = getShippingOrderService().save(shippingOrder);

			//shipmentService.createShipment(shippingOrder);
	        // auto escalate shipping orders if possible
	        //getShippingOrderService().autoEscalateShippingOrder(shippingOrder);

			orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(baseOrder);
            return shippingOrder;
        }
        return null;
    }

    @Transactional
    public ShippingOrder putShippingOrderOnHold(ShippingOrder shippingOrder) {
        if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
            shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_OnHold));
            getAdminInventoryService().reCheckInInventory(shippingOrder);
            shippingOrder = getShippingOrderService().save(shippingOrder);
            getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_PutOnHold);
        }
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder unHoldShippingOrder(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ActionAwaiting));
        shippingOrder = getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_RemovedOnHold);
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder
    markShippingOrderAsDelivered(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Delivered));
        getShippingOrderService().save(shippingOrder);
        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Delivered);
        Order order = shippingOrder.getBaseOrder();
        getAdminOrderService().markOrderAsDelivered(order);
        loyaltyProgramService.approveKarmaPoints(shippingOrder.getBaseOrder());
//	    smsManager.sendOrderDeliveredSMS(shippingOrder);
	    return shippingOrder;
    }

    @Transactional
       public ShippingOrder markShippingOrderAsInstalled(ShippingOrder shippingOrder) {
           shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Installed));
           getShippingOrderService().save(shippingOrder);
           getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Installed);
           Order order = shippingOrder.getBaseOrder();
           getAdminOrderService().markOrderAsCompletedWithInstallation(order);
//	    smsManager.sendOrderDeliveredSMS(shippingOrder);
           return shippingOrder;
       }



    @Transactional
    public ShippingOrder markShippingOrderAsRTO(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_RTO));
        shippingOrder.getShipment().setReturnDate(new Date());
        getShippingOrderService().save(shippingOrder);
        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Returned);
        Order order = shippingOrder.getBaseOrder();
        getAdminOrderService().markOrderAsRTO(order);
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder markShippingOrderAsLost(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Lost));
        getShippingOrderService().save(shippingOrder);
        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Lost);
        Order order = shippingOrder.getBaseOrder();
        getAdminOrderService().markOrderAsLost(order);
        return shippingOrder;
    }

    public ShippingOrder initiateRTOForShippingOrder(ShippingOrder shippingOrder, ReplacementOrderReason rtoReason) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.RTO_Initiated));
        getShippingOrderService().save(shippingOrder);
	    if(rtoReason != null){
            getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RTO_Initiated, null, rtoReason.getName());
	    }
	    else{
		    getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RTO_Initiated);
	    }
        return shippingOrder;
    }

    public List<ShippingOrder> getShippingOrderListByCouriers(Date startDate, Date endDate, List<Long> courierId) {
        return getAdminShippingOrderDao().getShippingOrderListByCouriers(startDate, endDate, courierId);
    }


    @Transactional
    public ShippingOrder markShippingOrderAsShipped(ShippingOrder shippingOrder) {
        Shipment shipment = shippingOrder.getShipment();
        if (shipment != null) {
            shipment.getAwb().setAwbStatus(EnumAwbStatus.Used.getAsAwbStatus());
            shipment.setShipDate(new Date());
            getShipmentService().save(shipment);
        }
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Shipped));
        getPincodeCourierService().setTargetDeliveryDate(shippingOrder);
        getShippingOrderService().save(shippingOrder);
        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Shipped);
        getBucketService().popFromActionQueue(shippingOrder);
        getAdminOrderService().markOrderAsShipped(shippingOrder.getBaseOrder());
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder markShippingOrderAsPrinted(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_MarkedForPrinting));
        getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ChosenForPrinting);

        return shippingOrder;
    }

    @Transactional
    public ShippingOrder moveShippingOrderToPickingQueue(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Picking));
        getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_InPicking);

        return shippingOrder;
    }

    @Transactional
    public ShippingOrder moveShippingOrderBackToActionQueue(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_OnHold));
        getAdminInventoryService().reCheckInInventory(shippingOrder);
        shippingOrder = getShippingOrderService().save(shippingOrder);
        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_EscalatedBackToActionQueue, shippingOrder.getReason(), null);

        getBucketService().escalateBackToActionQueue(shippingOrder);
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder moveShippingOrderBackToPackingQueue(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForProcess));
        getAdminInventoryService().reCheckInInventory(shippingOrder);
        getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_BackToPackingQueue);

        return shippingOrder;
    }

    @Transactional
       public ShippingOrder moveShippingOrderBackToDropShippingQueue(ShippingOrder shippingOrder) {
           shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForDropShipping));
//           getAdminInventoryService().reCheckInInventory(shippingOrder);
           getShippingOrderService().save(shippingOrder);
           getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_BackToDropShippingQueue);
           return shippingOrder;
       }

	public ReplacementOrderReason getRTOReasonForShippingOrder(ShippingOrder shippingOrder) {
		String rtoReason = null;
		ReplacementOrderReason replacementOrderReason = null;
		for (ShippingOrderLifecycle shippingOrderLifecycle : shippingOrder.getShippingOrderLifecycles()){
			if(shippingOrderLifecycle.getShippingOrderLifeCycleActivity().getId().equals(EnumShippingOrderLifecycleActivity.RTO_Initiated.getId())){
				if(shippingOrderLifecycle.getComments() != null){
					replacementOrderReason = getReplacementOrderReasonByName(shippingOrderLifecycle.getComments());
				}
			}
		}
		return replacementOrderReason;
	}

	public ReplacementOrderReason getReplacementOrderReasonByName(String replacementOrderReasonString) {
		List<ReplacementOrderReason> replacementOrderReasonList = getAdminShippingOrderDao().getAll(ReplacementOrderReason.class);
		for(ReplacementOrderReason replacementOrderReason : replacementOrderReasonList){
			if(replacementOrderReasonString.contains(replacementOrderReason.getName())){
				return replacementOrderReason;
			}
		}
		return null;
	}

	public void adjustPurchaseOrderForSplittedShippingOrder(ShippingOrder shippingOrder, ShippingOrder newShippingOrder){
		List<PurchaseOrder> poList = shippingOrder.getPurchaseOrders();
        Set<PurchaseOrder> newShippingOrderPoSet = new HashSet<PurchaseOrder>();
        Set<PurchaseOrder> parentShippingOrderPoSet = new HashSet<PurchaseOrder>();
        List<ProductVariant> variantListFromSO = new ArrayList<ProductVariant>();
        for(LineItem item: shippingOrder.getLineItems()){
        	variantListFromSO.add(item.getSku().getProductVariant());
        }
        
        if(poList!=null && poList.size()>0){
        	for(PurchaseOrder order:poList){
        		boolean flag = false;
        		List<ProductVariant> productVariants = purchaseOrderService.getAllProductVariantFromPO(order);
        		if(productVariants!=null && productVariants.size()>0){
        			for(ProductVariant pv : productVariants){
        				if(variantListFromSO.contains(pv)){
        					flag = true;
        				}
        				boolean soHasPv = shippingOrderService.shippingOrderContainsProductVariant(newShippingOrder, pv, pv.getMarkedPrice());
        				if(soHasPv){
        					newShippingOrderPoSet.add(order);
        				}
        			}
        		}
        		if(flag ==true){
        			parentShippingOrderPoSet.add(order);
        		}
        	}
        }
        
        
        //shippingOrder = shippingOrderService.save(shippingOrder);
        newShippingOrder.setPurchaseOrders(new ArrayList<PurchaseOrder>(newShippingOrderPoSet));
        shippingOrder.setPurchaseOrders(new ArrayList<PurchaseOrder>(parentShippingOrderPoSet));
        
        newShippingOrder = shippingOrderService.save(newShippingOrder);
        shippingOrder = shippingOrderService.save(shippingOrder);
        
        for(PurchaseOrder po : newShippingOrderPoSet){
        	
        	List<ShippingOrder> soList = po.getShippingOrders();
        	soList.add(newShippingOrder);
        	if(!parentShippingOrderPoSet.contains(po)){
        	soList.remove(shippingOrder);
        	}
        	po.setShippingOrders(soList);
        	baseDao.save(po);
        }
        adminEmailManager.sendJitShippingCancellationMail(shippingOrder,newShippingOrder, EnumJitShippingOrderMailToCategoryReason.SO_SPLITTED);
        
	}

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public ShippingOrderStatusService getShippingOrderStatusService() {
        return shippingOrderStatusService;
    }

    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public WarehouseService getWarehouseService() {
        return warehouseService;
    }

    public void setWarehouseService(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    public AdminOrderService getAdminOrderService() {
        if (adminOrderService == null) {
            adminOrderService = ServiceLocatorFactory.getService(AdminOrderService.class);
        }
        return adminOrderService;
    }

    public void setAdminOrderService(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    public ShipmentService getShipmentService() {
        return shipmentService;
    }

    public void setShipmentService(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    public AdminInventoryService getAdminInventoryService() {
        return adminInventoryService;
    }

    public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
        this.adminInventoryService = adminInventoryService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public AdminShippingOrderDao getAdminShippingOrderDao() {
        return adminShippingOrderDao;
    }

    public void setAdminShippingOrderDao(AdminShippingOrderDao adminShippingOrderDao) {
        this.adminShippingOrderDao = adminShippingOrderDao;
    }

    public OrderService getOrderService() {
        if (orderService == null) {
            this.orderService = ServiceLocatorFactory.getService(OrderService.class);
        }
        return orderService;
    }

	public UserService getUserService() {
		return userService;
	}

    public BucketService getBucketService() {
        return bucketService;
    }

    public void setBucketService(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    public PincodeCourierService getPincodeCourierService() {
        return pincodeCourierService;
    }

    public void setPincodeCourierService(PincodeCourierService pincodeCourierService) {
        this.pincodeCourierService = pincodeCourierService;
    }

    public String getCancellationRemark() {
        return cancellationRemark;
    }

    public void setCancellationRemark(String cancellationRemark) {
        this.cancellationRemark = cancellationRemark;
    }

    public UserAccountInfoDao getUserAccountInfoDao() {
        return userAccountInfoDao;
    }

    public void setUserAccountInfoDao(UserAccountInfoDao userAccountInfoDao) {
        this.userAccountInfoDao = userAccountInfoDao;
    }
}
