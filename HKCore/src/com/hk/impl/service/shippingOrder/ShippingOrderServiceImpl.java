package com.hk.impl.service.shippingOrder;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Zone;
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
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.helper.OrderDateUtil;
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
import com.hk.util.HKDateUtil;
import com.hk.util.OrderUtil;
import com.hk.util.TokenUtils;
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

        Date targetDispatchDate = OrderDateUtil.getTargetDispatchDateForWH(refDate, dispatchDays[0]);
        shippingOrder.setTargetDispatchDate(targetDispatchDate);

        Long diffInPromisedTimes = (dispatchDays[1] - dispatchDays[0]);
        int daysTakenForDelievery = Integer.valueOf(diffInPromisedTimes.toString());
        Date targetDelDate = HKDateUtil.addToDate(targetDispatchDate, Calendar.DAY_OF_MONTH, daysTakenForDelievery);
        shippingOrder.setTargetDelDate(targetDelDate);

        getShippingOrderDao().save(shippingOrder);
    }

    /**
     * Auto-escalation logic for all successful transactions This method will check inventory availability and escalate
     * orders from action queue to processing queue accordingly.
     * 
     * @param shippingOrder
     * @return true if it passes all the use cases i.e jit or availableUnbookedInventory Ajeet - 15-Feb-2012
     * @description shipping order
     */
    public boolean isShippingOrderAutoEscalable(ShippingOrder shippingOrder) {
        logger.debug("Trying to autoescalate order#" + shippingOrder.getId());
        if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(shippingOrder.getBaseOrder().getPayment().getPaymentStatus().getId())) {
            if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
                User adminUser = getUserService().getAdminUser();
                Order order = shippingOrder.getBaseOrder();
                if (order.isReferredOrder() && order.getPayment().getAmount() < 1000) {
                    String comments = "BO is a referred Order, Please do a manual approval";
                    logShippingOrderActivity(shippingOrder, adminUser,
                            getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), comments);
                    return false;
                }
                if (shippingOrder.isDropShipping()) {
                    String comments = "Because It is a Drop Shipped Order";
                    logShippingOrderActivity(shippingOrder, adminUser,
                            getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), comments);
                    return false;
                }
                for (LineItem lineItem : shippingOrder.getLineItems()) {
                    Long availableUnbookedInv = getInventoryService().getAvailableUnbookedInventory(lineItem.getSku()); // This
                    // is after including placed order qty

                    logger.debug("availableUnbookedInv of[" + lineItem.getSku().getId() + "] = " + availableUnbookedInv);
                    ProductVariant productVariant = lineItem.getSku().getProductVariant();
                    logger.debug("jit: " + productVariant.getProduct().isJit());
                    if (productVariant.getProduct().isService() != null && productVariant.getProduct().isService()) {
                        continue;
                    }

                    if (productVariant.getProduct().isJit() != null && productVariant.getProduct().isJit()) {
                        String comments = "Because " + lineItem.getSku().getProductVariant().getProduct().getName() + " is JIT";
                        logShippingOrderActivity(shippingOrder, adminUser,
                                getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), comments);
                        return false;
                    } else if (lineItem.getCartLineItem().getCartLineItemConfig() != null) {
                        String comments = "Order contains prescription glasses, Can't escalate";
                        logShippingOrderActivity(shippingOrder, adminUser,
                                getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), comments);
                        return false;
                    } else if (availableUnbookedInv < 0) {
                        String comments = "Because availableUnbookedInv of " + lineItem.getSku().getProductVariant().getProduct().getName() + " at this instant was = "
                                + availableUnbookedInv;
                        logger.info("Could not auto escalate order as availableUnbookedInv of sku[" + lineItem.getSku().getId() + "] = " + availableUnbookedInv
                                + " for shipping order id " + shippingOrder.getId());
                        logShippingOrderActivity(shippingOrder, adminUser,
                                getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), comments);
                        return false;
                    }
				}
					if (shippingOrder.getShipment() == null) {
						String comments = "Because shipment has not been created";
						logShippingOrderActivity(shippingOrder, adminUser,
							getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), comments);
						return false;
					}

                return true;
            }
        } else {
            String comments = "Because payment status is auth pending";
            User adminUser = getUserService().getAdminUser();
            logShippingOrderActivity(shippingOrder, adminUser, getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue),
                    comments);
            return false;
        }

        return false;
    }

    public boolean isShippingOrderManuallyEscalable(ShippingOrder shippingOrder) {
        logger.debug("Trying to manually escalate order#" + shippingOrder.getId());
        if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(shippingOrder.getBaseOrder().getPayment().getPaymentStatus().getId())) {
            if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
                User adminUser = getUserService().getAdminUser();
                for (LineItem lineItem : shippingOrder.getLineItems()) {
                    Long availableUnbookedInv = getInventoryService().getUnbookedInventoryInProcessingQueue(Arrays.asList(lineItem.getSku())); // This
                    // is after including placed order qty

                    logger.debug("availableUnbookedInv of[" + lineItem.getSku().getId() + "] = " + availableUnbookedInv);
                    ProductVariant productVariant = lineItem.getSku().getProductVariant();
                    logger.debug("jit: " + productVariant.getProduct().isJit());

                    if (availableUnbookedInv <= 0) {
                        String comments = "Because availableUnbookedInv of " + lineItem.getSku().getProductVariant().getProduct().getName() + " at this instant was = "
                                + availableUnbookedInv;
                        logger.info("Could not manually escalate order as availableUnbookedInv of sku[" + lineItem.getSku().getId() + "] = " + availableUnbookedInv
                                + " for shipping order id " + shippingOrder.getId());
                        logShippingOrderActivity(shippingOrder, adminUser,
                                getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeManuallyEscalatedToProcessingQueue), comments);
                        return false;
                    }
				}
					if(shippingOrder.getShipment() == null && !shippingOrder.isDropShipping()){
						Shipment newShipment = getShipmentService().createShipment(shippingOrder, true);
						if (newShipment == null) {
							String comments = "Because shipment has not been created";
							logShippingOrderActivity(shippingOrder, adminUser,
								getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeManuallyEscalatedToProcessingQueue), comments);
							return false;
						}
					}

                return true;
            }
        } else {
            String comments = "Because payment status is auth pending";
            User adminUser = getUserService().getAdminUser();
            logShippingOrderActivity(shippingOrder, adminUser,
                    getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeManuallyEscalatedToProcessingQueue), comments);
            return false;
        }
        return false;
    }

    @Transactional
    public ShippingOrder autoEscalateShippingOrder(ShippingOrder shippingOrder) {
        if (isShippingOrderAutoEscalable(shippingOrder)) {
            shippingOrder = escalateShippingOrderFromActionQueue(shippingOrder, true);
        }
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder escalateShippingOrderFromActionQueue(ShippingOrder shippingOrder, boolean isAutoEsc) {
		shippingOrder.setLastEscDate(HKDateUtil.getNow());
		if(shippingOrder.isDropShipping()){
			shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForDropShipping));
		} else{
        	shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForProcess));
		}
        shippingOrder = (ShippingOrder) getShippingOrderDao().save(shippingOrder);

        if (isAutoEsc) {
            logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToProcessingQueue);
        } else {
			if(shippingOrder.isDropShipping()){
				logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_EscalatedToDropShippingQueue);
				emailManager.sendEscalationToDropShipEmail(shippingOrder);
			} else{
            	logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_EscalatedToProcessingQueue);
			}
        }
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

    public void logShippingOrderActivity(ShippingOrder shippingOrder, EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity) {
        User loggedOnUser = getUserService().getLoggedInUser();
        // User loggedOnUser = UserCache.getInstance().getLoggedInUser();
        if (loggedOnUser == null) {
            loggedOnUser = shippingOrder.getBaseOrder().getUser();
        }

        ShippingOrderLifeCycleActivity orderLifecycleActivity = getShippingOrderLifeCycleActivity(enumShippingOrderLifecycleActivity);
        logShippingOrderActivity(shippingOrder, loggedOnUser, orderLifecycleActivity, null);
    }

    public void logShippingOrderActivity(ShippingOrder shippingOrder, EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity, String comments) {
        User loggedOnUser = getUserService().getLoggedInUser();
        // User loggedOnUser = UserCache.getInstance().getLoggedInUser();
        if (loggedOnUser == null) {
            loggedOnUser = shippingOrder.getBaseOrder().getUser();
        }
        ShippingOrderLifeCycleActivity orderLifecycleActivity = getShippingOrderLifeCycleActivity(enumShippingOrderLifecycleActivity);
        logShippingOrderActivity(shippingOrder, loggedOnUser, orderLifecycleActivity, comments);
    }

    public void logShippingOrderActivity(ShippingOrder shippingOrder, User user, ShippingOrderLifeCycleActivity shippingOrderLifeCycleActivity, String comments) {

        ShippingOrderLifecycle shippingOrderLifecycle = new ShippingOrderLifecycle();
        shippingOrderLifecycle.setShippingOrder(shippingOrder);
        shippingOrderLifecycle.setShippingOrderLifeCycleActivity(shippingOrderLifeCycleActivity);
        shippingOrderLifecycle.setUser(user);
        shippingOrderLifecycle.setComments(comments);
        shippingOrderLifecycle.setActivityDate(new Date());
        getShippingOrderDao().save(shippingOrderLifecycle);
    }

    @Override
    public boolean shippingOrderHasReplacementOrder(ShippingOrder shippingOrder) {
        if (getReplacementOrderDao().getReplacementOrderFromShippingOrder(shippingOrder.getId()) != null
                && getReplacementOrderDao().getReplacementOrderFromShippingOrder(shippingOrder.getId()).size() > 0) {
            return true;
        }
        ;
        return false; // To change body of implemented methods use File | Settings | File Templates.
    }

    //todo courier refactor
/*
	@Override
	public boolean printZoneOnSOInvoice(ShippingOrder shippingOrder) {
		Zone zone=null;
		Shipment shipment=shippingOrder.getShipment();
		if(shipment != null && shipment.getZone() !=null){
			Long courierId = shipment.getAwb().getCourier().getId();
			zone=shipment.getZone();
			if(zone != null){
				if(EnumCourier.getDispatchLotCouriers().contains(courierId)){
					return true;
				}
			}
		}
		return false;
	}
*/

	@Override
	public Zone getZoneForShippingOrder(ShippingOrder shippingOrder) {
		return shippingOrder.getShipment().getZone();

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
