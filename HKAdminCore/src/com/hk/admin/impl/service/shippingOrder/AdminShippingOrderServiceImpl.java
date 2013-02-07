package com.hk.admin.impl.service.shippingOrder;

import java.util.*;

import com.hk.domain.order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Shipment;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.service.ServiceLocatorFactory;

@Service
public class AdminShippingOrderServiceImpl implements AdminShippingOrderService {

		private static Logger logger = LoggerFactory.getLogger(AdminShippingOrderServiceImpl.class);
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private AdminInventoryService adminInventoryService;

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
//	@Autowired
//	SMSManager smsManager;

    public void cancelShippingOrder(ShippingOrder shippingOrder) {
        // Check if Order is in Action Queue before cancelling it.
        if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
	          logger.warn("Cancelling Shipping order gateway id:::"+ shippingOrder.getGatewayOrderId());
            shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Cancelled));
            //shippingOrder = getShippingOrderService().save(shippingOrder);
            getAdminInventoryService().reCheckInInventory(shippingOrder);
            // TODO : Write a generic ROLLBACK util which will essentially release all attached laibilities i.e.
            // inventory, reward points, shipment, discount
            getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Cancelled);

            orderService.updateOrderStatusFromShippingOrders(shippingOrder.getBaseOrder(), EnumShippingOrderStatus.SO_Cancelled, EnumOrderStatus.Cancelled);
            if(shippingOrder.getShipment()!= null){
                Awb awbToRemove = shippingOrder.getShipment().getAwb();
                awbService.preserveAwb(awbToRemove);
                Shipment shipmentToDelete = shippingOrder.getShipment();
                shippingOrder.setShipment(null);
	            shipmentService.delete(shipmentToDelete);
	            //shippingOrderService.save(shippingOrder);
            }
			getShippingOrderService().save(shippingOrder);
        }
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            getInventoryService().checkInventoryHealth(lineItem.getSku().getProductVariant());
        }
    }

    public boolean updateWarehouseForShippingOrder(ShippingOrder shippingOrder, Warehouse warehouse) {
        Map<Long, Sku> lineItemToSkuUpdate = new HashMap<Long, Sku>();

        boolean shouldUpdate = true;
        Set<LineItem> lineItems = shippingOrder.getLineItems();
        try {
        for (LineItem lineItem : lineItems) {
            Sku skuInOtherWarehouse = getSkuService().getSKU(lineItem.getSku().getProductVariant(), warehouse);
                lineItemToSkuUpdate.put(lineItem.getId(), skuInOtherWarehouse);
        }
        }catch(NoSkuException noSku){
        shouldUpdate = false;
        }
        if (shouldUpdate) {
            for (LineItem lineItem : lineItems) {
                lineItem.setSku(lineItemToSkuUpdate.get(lineItem.getId()));
            }
            shippingOrder.setWarehouse(warehouse);
	        shipmentService.recreateShipment(shippingOrder);
            shippingOrder = getShippingOrderService().save(shippingOrder);
            getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_WarehouseChanged);

            // Re-checkin checkedout inventory in case of flipping.
            getAdminInventoryService().reCheckInInventory(shippingOrder);
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

            shippingOrder.setBasketCategory(getOrderService().getBasketCategory(shippingOrder).getName());
            ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, baseOrder);
            shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
            shippingOrder = getShippingOrderService().save(shippingOrder);
            /**
             * this additional call to save is done so that we have shipping order id to generate shipping order gateway
             * id
             */
            shippingOrder = getShippingOrderService().setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
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
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_OnHold));
        getAdminInventoryService().reCheckInInventory(shippingOrder);
        shippingOrder = getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_PutOnHold);
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
    public ShippingOrder markShippingOrderAsDelivered(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Delivered));
        getShippingOrderService().save(shippingOrder);
        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Delivered);
        Order order = shippingOrder.getBaseOrder();
        getAdminOrderService().markOrderAsDelivered(order);
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
            getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RTO_Initiated, rtoReason.getName());
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
        getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Shipped);
        getAdminOrderService().markOrderAsShipped(shippingOrder.getBaseOrder());
//	    smsManager.sendOrderShippedSMS(shippingOrder);

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
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ActionAwaiting));
        getAdminInventoryService().reCheckInInventory(shippingOrder);
        shippingOrder = (ShippingOrder) getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_EscalatedBackToActionQueue);

        getAdminOrderService().moveOrderBackToActionQueue(shippingOrder.getBaseOrder(), shippingOrder.getGatewayOrderId());
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
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

	public UserService getUserService() {
		return userService;
	}
}
