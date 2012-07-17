package com.hk.admin.impl.service.shippingOrder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
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
import com.hk.service.ServiceLocatorFactory;

@Service
public class AdminShippingOrderServiceImpl implements AdminShippingOrderService {

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

    // public List<Long> getShippingOrderListByCourier(Date startDate, Date endDate, Long courierId) {
    // List<Long> shippingOrderList = getAdminShippingOrderDao().getShippingOrderListByCourier(startDate, endDate,
    // courierId);
    // return shippingOrderList;
    // }


    public void cancelShippingOrder(ShippingOrder shippingOrder) {
        // Check if Order is in Action Queue before cancelling it.
        if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
            shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Cancelled));
            shippingOrder = getShippingOrderService().save(shippingOrder);
            getAdminInventoryService().reCheckInInventory(shippingOrder);
            // TODO : Write a generic ROLLBACK util which will essentially release all attached laibilities i.e.
            // inventory, reward points, shipment, discount
            for (LineItem lineItem : shippingOrder.getLineItems()) {
                getInventoryService().checkInventoryHealth(lineItem.getSku().getProductVariant());
            }
            getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Cancelled);

            orderService.updateOrderStatusFromShippingOrders(shippingOrder.getBaseOrder(), EnumShippingOrderStatus.SO_Cancelled, EnumOrderStatus.Cancelled);
        }
    }

    public boolean updateWarehouseForShippingOrder(ShippingOrder shippingOrder, Warehouse warehouse) {
        Map<Long, Sku> lineItemToSkuUpdate = new HashMap<Long, Sku>();

        boolean shouldUpdate = true;
        Set<LineItem> lineItems = shippingOrder.getLineItems();
        for (LineItem lineItem : lineItems) {
            Sku skuInOtherWarehouse = getSkuService().getSKU(lineItem.getSku().getProductVariant(), warehouse);
            if (skuInOtherWarehouse == null) {
                shouldUpdate = false;
                break;
            } else {
                lineItemToSkuUpdate.put(lineItem.getId(), skuInOtherWarehouse);
            }
        }

        if (shouldUpdate) {
            for (LineItem lineItem : lineItems) {
                lineItem.setSku(lineItemToSkuUpdate.get(lineItem.getId()));
            }
            shippingOrder.setWarehouse(warehouse);
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
            shippingOrder = ShippingOrderHelper.setGatewayIdOnShippingOrder(shippingOrder);
            shippingOrder = getShippingOrderService().save(shippingOrder);

            return shippingOrder;
        }
        return null;
    }

    public ShippingOrder createSOForService(CartLineItem serviceCartLineItem) {
        Order baseOrder = serviceCartLineItem.getOrder();
        Warehouse corporateOffice = getWarehouseService().getCorporateOffice();
        ShippingOrder shippingOrder = getShippingOrderService().createSOWithBasicDetails(baseOrder, corporateOffice);
        shippingOrder.setBaseOrder(baseOrder);

        ProductVariant productVariant = serviceCartLineItem.getProductVariant();
        Sku sku = getSkuService().getSKU(productVariant, corporateOffice);
        if (sku != null) {
            LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, serviceCartLineItem);
            shippingOrder.getLineItems().add(shippingOrderLineItem);
        } else {
            throw new NoSkuException(productVariant, corporateOffice);
        }

        shippingOrder.setBasketCategory("services");
        shippingOrder.setServiceOrder(true);
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForProcess));
        ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, baseOrder);
        shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
        shippingOrder = getShippingOrderService().save(shippingOrder);
        /**
         * this additional call to save is done so that we have shipping order id to generate shipping order gateway id
         */
        shippingOrder = ShippingOrderHelper.setGatewayIdOnShippingOrder(shippingOrder);
        shippingOrder = getShippingOrderService().save(shippingOrder);

        return shippingOrder;

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
        return shippingOrder;
    }

    @Transactional
    public ShippingOrder markShippingOrderAsRTO(ShippingOrder shippingOrder) {
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Returned));
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

    public List<ShippingOrder> getShippingOrderListByCouriers(Date startDate, Date endDate, List<Long> courierId) {
        return getAdminShippingOrderDao().getShippingOrderListByCouriers(startDate, endDate, courierId);
    }


    @Transactional
    public ShippingOrder markShippingOrderAsShipped(ShippingOrder shippingOrder) {

        Shipment shipment = shippingOrder.getShipment();

        if (shipment != null) {
            getShipmentService().saveShipmentDate(shipment);
        }

        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Shipped));
        getShippingOrderService().save(shippingOrder);

        getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Shipped);
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

}
