package com.hk.impl.service.shippingOrder;

import java.util.Date;
import java.util.List;

import com.hk.pact.dao.shippingOrder.LineItemDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.ReconciliationStatusDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.service.ServiceLocatorFactory;



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
	private LineItemDao lineItemDao;

	private OrderService orderService;
    
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

    /**
     * Auto-escalation logic for all successful transactions This method will check inventory availability and escalate
     * orders from action queue to processing queue accordingly.
     * 
     * @param shippingOrder
     * @description shipping order
     * @return true if it passes all the use cases i.e jit or availableUnbookedInventory Ajeet - 15-Feb-2012
     */
    public boolean isShippingOrderAutoEscalable(ShippingOrder shippingOrder) {
        logger.debug("Trying to autoescalate order#" + shippingOrder.getId());

        for (LineItem lineItem : shippingOrder.getLineItems()) {
            Long availableUnbookedInv = getInventoryService().getAvailableUnbookedInventory(lineItem.getSku()); // This
            // is after including placed order qty
            logger.debug("availableUnbookedInv of[" + lineItem.getSku().getId() + "] = " + availableUnbookedInv);
            ProductVariant productVariant = lineItem.getSku().getProductVariant();
            logger.debug("jit: " + productVariant.getProduct().isJit());
            if (productVariant.getProduct().isJit() != null && productVariant.getProduct().isJit()) {
                String comments = "Because " + lineItem.getSku().getProductVariant().getProduct().getName() + " is JIT";
                logShippingOrderActivity(shippingOrder, getUserService().getAdminUser(),
                        getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), comments);
                return false;
            } else if (availableUnbookedInv < 0) {
                String comments = "Because availableUnbookedInv of " + lineItem.getSku().getProductVariant().getProduct().getName() + " at this instant was = "
                        + availableUnbookedInv;
                logger.info("Could not auto escalate order as availableUnbookedInv of sku[" + lineItem.getSku().getId() + "] = " + availableUnbookedInv + " for shipping order id "
                        + shippingOrder.getId());
                logShippingOrderActivity(shippingOrder, getUserService().getAdminUser(),
                        getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue), comments);
                return false;
            }
        }
        return true;
    }

    @Transactional
    public ShippingOrder autoEscalateShippingOrder(ShippingOrder shippingOrder) {
        if (isShippingOrderAutoEscalable(shippingOrder)) {
            shippingOrder = escalateShippingOrderFromActionQueue(shippingOrder, true);
        }
        /*
         * else { shippingOrder.setOrderStatus(orderStatusService.find(EnumOrderStatus.ActionAwaiting));
         * getShippingOrderDao().save(shippingOrder); }
         */
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder escalateShippingOrderFromActionQueue(ShippingOrder shippingOrder, boolean isAutoEsc) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForProcess));
        shippingOrder = (ShippingOrder) getShippingOrderDao().save(shippingOrder);
        if (isAutoEsc) {
            logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToProcessingQueue);
        } else {
            logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_EscalatedToProcessingQueue);
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
        shippingOrder.setUpdateDate(new Date());
        shippingOrder.setWarehouse(warehouse);
        shippingOrder.setAmount(0D);
        shippingOrder.setReconciliationStatus(getReconciliationStatusDao().getReconciliationStatusById(EnumReconciliationStatus.PENDING));

        return shippingOrder;
    }

	@Override
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
        if(loggedOnUser == null){
            loggedOnUser = shippingOrder.getBaseOrder().getUser();
        }

        ShippingOrderLifeCycleActivity orderLifecycleActivity = getShippingOrderLifeCycleActivity(enumShippingOrderLifecycleActivity);
        logShippingOrderActivity(shippingOrder, loggedOnUser, orderLifecycleActivity, null);
    }

    public void logShippingOrderActivity(ShippingOrder shippingOrder, EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity, String comments) {
        User loggedOnUser = getUserService().getLoggedInUser();
        if(loggedOnUser == null){
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
        if(orderService == null){
            orderService = ServiceLocatorFactory.getService(OrderService.class);
        }
        return orderService;
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

}
