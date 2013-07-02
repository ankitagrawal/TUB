package com.hk.impl.service.shippingOrder;

import java.util.*;

import com.hk.constants.analytics.EnumReason;
import com.hk.constants.queue.EnumBucket;
import com.hk.domain.analytics.Reason;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.*;
import com.hk.domain.payment.Payment;
import com.hk.domain.shippingOrder.LifecycleReason;
import com.hk.impl.service.queue.BucketService;
import com.hk.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.ReconciliationStatusDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ReplacementOrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.manager.EmailManager;

/**
 * @author vaibhav.adlakha
 */
@Service
public class ShippingOrderServiceImpl implements ShippingOrderService {

    private Logger                     logger = LoggerFactory.getLogger(ShippingOrderService.class);

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


    private boolean isShippingOrderAutoEscalable(ShippingOrder shippingOrder, boolean firewall) {
        Payment payment = shippingOrder.getBaseOrder().getPayment();
        List<Reason> reasons = new ArrayList<Reason>();
        if (payment != null && EnumPaymentStatus.getEscalablePaymentStatusIds().contains(payment.getPaymentStatus().getId())) {
            if(firewall && SOFirewall.isAmountMismatch(payment.getOrder())){
              reasons.add(EnumReason.DiscrepancyInPaymentAmount.asReason());
            }
            if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
                if(shippingOrder.isServiceOrder()){
                    return true;
                }
                if (shippingOrder.isDropShipping()) {
                    reasons.add(EnumReason.DROP_SHIPPED_ORDER.asReason());
                }
                List<EnumBucket> enumBuckets = bucketService.getCategoryDefaultersBuckets(shippingOrder);
                if (!enumBuckets.isEmpty()) {
                    reasons.add(EnumReason.InsufficientUnbookedInventory.asReason());
                }
                if (shippingOrder.getShipment() == null) {
                    reasons.add(EnumReason.ShipmentNotCreated.asReason());
                } else {
                    //putting checks for shipping cost
                    Double estimatedShippingCharges = shippingOrder.getShipment().getEstmShipmentCharge();
                    if (firewall && estimatedShippingCharges != null && estimatedShippingCharges > SOFirewall.minAllowedShippingCharges && estimatedShippingCharges >= SOFirewall.calculateCutoffAmount(shippingOrder)) {
                        reasons.add(EnumReason.HighShippingCost.asReason());
                    }
                }
                if (!reasons.isEmpty()) {
                    for (Reason reason : reasons) {
                        logShippingOrderActivityByAdmin(shippingOrder, EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue, reason);
                    }
                    return false;
                }
                return true;
            }
        } else {
            logShippingOrderActivityByAdmin(shippingOrder, EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue,  EnumReason.InvalidPaymentStatus.asReason());
            return false;
        }
        return false;
    }

    private boolean isShippingOrderManuallyEscalable(ShippingOrder shippingOrder) {
        if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(shippingOrder.getBaseOrder().getPayment().getPaymentStatus().getId())) {
            if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
                if(!(shippingOrder.isServiceOrder())){
                User adminUser = getUserService().getAdminUser();
                for (LineItem lineItem : shippingOrder.getLineItems()) {
                    Long availableUnbookedInv = getInventoryService().getUnbookedInventoryInProcessingQueue(lineItem); // This

                    if (availableUnbookedInv < 0 && !shippingOrder.isDropShipping()){ // It cannot be = as for last order/unit unbooked will always be ZERO 
                        String comments = lineItem.getSku().getProductVariant().getProduct().getName() + " at this instant was = " + availableUnbookedInv;
                        logShippingOrderActivity(shippingOrder, adminUser,
                                getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeManuallyEscalatedToProcessingQueue), EnumReason.InsufficientUnbookedInventoryManual.asReason(), comments);
                        return false;
                    }
				}
					if(shippingOrder.getShipment() == null && !shippingOrder.isDropShipping()){
						Shipment newShipment = getShipmentService().createShipment(shippingOrder, true);
						if (newShipment == null) {
                            logShippingOrderActivityByAdmin(shippingOrder, EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue, EnumReason.ShipmentNotCreatedManual.asReason());
							return false;
						}
					}
            }
                return true;
            }
        } else {
            logShippingOrderActivityByAdmin(shippingOrder, EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue, EnumReason.InvalidPaymentStatusManual.asReason());
            return false;
        }
        return false;
    }

    private boolean isShippingOrderAutomaticallyManuallyEscalable(ShippingOrder shippingOrder) {
        logger.debug("Trying to autoEscalate order#" + shippingOrder.getId());
        Payment payment = shippingOrder.getBaseOrder().getPayment();
        User adminUser = getUserService().getAdminUser();
        if (payment != null && EnumPaymentStatus.getEscalablePaymentStatusIds().contains(payment.getPaymentStatus().getId())) {
            if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
                if (shippingOrder.isDropShipping()) {
                    logShippingOrderActivity(shippingOrder, adminUser,
                            getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), EnumReason.DROP_SHIPPED_ORDER.asReason(), null);
                    return false;
                }
                if (shippingOrder.isServiceOrder()) {
                    return true;
                }
                if (shippingOrder.getShipment() == null && !shippingOrder.isDropShipping()) {
                    Shipment newShipment = getShipmentService().createShipment(shippingOrder, true);
                    if (newShipment == null) {
                        logShippingOrderActivity(shippingOrder, adminUser,
                                getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), EnumReason.ShipmentNotCreated.asReason(), null);
                        return false;
                    }
                }
                for (LineItem lineItem : shippingOrder.getLineItems()) {
                    CartLineItem cartLineItem = lineItem.getCartLineItem();
                    if (cartLineItem.getCartLineItemConfig() != null) {
                        logShippingOrderActivity(shippingOrder, adminUser,
                                getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), EnumReason.Contains_Prescription_Glasses.asReason(), null);
                        return false;
                    }
                    if(cartLineItem.getProductVariant().getProductExtraOptions() != null && !cartLineItem.getProductVariant().getProductExtraOptions().isEmpty()){
                        logShippingOrderActivity(shippingOrder, adminUser,
                                getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), EnumReason.Contains_Prescription_Glasses.asReason(), null);
                        return false;
                    }
                    Long availableUnbookedInv = getInventoryService().getUnbookedInventoryInProcessingQueue(lineItem); // This
                    
                    if (availableUnbookedInv < 0) {
                        String comments = lineItem.getSku().getProductVariant().getProduct().getName() + " at this instant was = " + availableUnbookedInv;
                        logger.debug(comments);
                        logShippingOrderActivity(shippingOrder, adminUser,
                                getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), EnumReason.InsufficientUnbookedInventory.asReason(), comments);
                        return false;
                    }
                }
                return true;
            }
        } else {
            logShippingOrderActivity
                    (shippingOrder, adminUser, getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue),
                            EnumReason.InvalidPaymentStatus.asReason(), null);
            return false;
        }
        return false;
    }

    @Transactional
    public ShippingOrder autoEscalateShippingOrder(ShippingOrder shippingOrder, boolean firewall) {
        if(isShippingOrderAutoEscalable(shippingOrder, firewall)){
            User activityUser = getUserService().getAdminUser();
            logShippingOrderActivity(shippingOrder, activityUser, EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToProcessingQueue.asShippingOrderLifecycleActivity(), null, null);
            shippingOrder = escalateShippingOrderFromActionQueue(shippingOrder);
        }
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder manualEscalateShippingOrder(ShippingOrder shippingOrder) {
        if(isShippingOrderManuallyEscalable(shippingOrder)){
            User activityUser = userService.getLoggedInUser();
            logShippingOrderActivity(shippingOrder, activityUser, EnumShippingOrderLifecycleActivity.SO_EscalatedToProcessingQueue.asShippingOrderLifecycleActivity(), null, null);
            bucketService.escalateOrderFromActionQueue(shippingOrder);
            shippingOrder = escalateShippingOrderFromActionQueue(shippingOrder);
        }
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder automateManualEscalation(ShippingOrder shippingOrder) {
        if(isShippingOrderAutomaticallyManuallyEscalable(shippingOrder)){
            User activityUser = getUserService().getAdminUser();
            logShippingOrderActivity(shippingOrder, activityUser, EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToProcessingQueue.asShippingOrderLifecycleActivity(), null, null);
            bucketService.escalateOrderFromActionQueue(shippingOrder);
            shippingOrder = escalateShippingOrderFromActionQueue(shippingOrder);
        }
        return shippingOrder;
    }

    @Transactional
    private ShippingOrder escalateShippingOrderFromActionQueue(ShippingOrder shippingOrder) {
        EnumShippingOrderStatus applicableStatus = shippingOrder.isDropShipping() ? EnumShippingOrderStatus.SO_ReadyForDropShipping : EnumShippingOrderStatus.SO_ReadyForProcess;
        shippingOrder.setLastEscDate(HKDateUtil.getNow());
        shippingOrder.setOrderStatus(applicableStatus.asShippingOrderStatus());
        shippingOrder.setLastEscDate(HKDateUtil.getNow());
        if (shippingOrder.isDropShipping()) {
            emailManager.sendEscalationToDropShipEmail(shippingOrder);
        }
        shippingOrder = (ShippingOrder) getShippingOrderDao().save(shippingOrder);
        getOrderService().escalateOrderFromActionQueue(shippingOrder.getBaseOrder(), shippingOrder.getGatewayOrderId());
        return shippingOrder;
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
}
