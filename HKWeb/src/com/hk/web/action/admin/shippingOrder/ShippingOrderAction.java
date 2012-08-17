package com.hk.web.action.admin.shippingOrder;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.web.HealthkartResponse;

@Component
public class ShippingOrderAction extends BaseAction {

    private ShippingOrder             shippingOrder;
    @Autowired
    WarehouseService                  warehouseService;

    /*@Autowired
    private ShippingOrderService      shippingOrderService;*/
    @Autowired
    private AdminShippingOrderService adminShippingOrderService;
    @Autowired
    SkuService                        skuService;

    public Resolution flipWarehouse() {
        Warehouse warehouseToUpdate = warehouseService.getWarehoueForFlipping(shippingOrder.getWarehouse());

        boolean isWarehouseUpdated = adminShippingOrderService.updateWarehouseForShippingOrder(shippingOrder, warehouseToUpdate);

        String responseMsg = "";
        if (isWarehouseUpdated) {
            responseMsg = "warehouse flipped";
        } else {
            responseMsg = "All products not avaialable in other warehouse. so cannot update.";
        }

        Map<String, Object> data = new HashMap<String, Object>(1);
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, responseMsg, data);
        return new JsonResolution(healthkartResponse);
    }

    @JsonHandler
    public Resolution initiateRTO() {
        adminShippingOrderService.initiateRTOForShippingOrder(shippingOrder);

        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", data);
        return new JsonResolution(healthkartResponse);
    }
    @JsonHandler
    public Resolution markRTO() {
        adminShippingOrderService.markShippingOrderAsRTO(shippingOrder);

        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success", data);
        return new JsonResolution(healthkartResponse);
    }

    @JsonHandler
    public Resolution cancelShippingOrder() {
        adminShippingOrderService.cancelShippingOrder(shippingOrder);

        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "shipping order canceled", data);
        return new JsonResolution(healthkartResponse);
    }

    @JsonHandler
    public Resolution delieverDropShippingOrder() {
        String message = "";
        boolean orderHasOnlyDropShipProduct = true;

        for (LineItem lineItem : shippingOrder.getLineItems()) {
            CartLineItem cartLineItem = lineItem.getCartLineItem();

            if (EnumCartLineItemType.Product.getId().equals(cartLineItem.getLineItemType().getId())) {
                orderHasOnlyDropShipProduct &= cartLineItem.getProductVariant().getProduct().isDropShipping();
            }
            if(!orderHasOnlyDropShipProduct){
                break;
            }
        }

        if (orderHasOnlyDropShipProduct) {
            adminShippingOrderService.markShippingOrderAsDelivered(shippingOrder);
            message = "shipping order marked as delieverd";
        } else {
            message = "shipping order cannot be marked delievered, since it has non drop ship products";
        }

        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("orderStatus", JsonUtils.hydrateHibernateObject(shippingOrder.getOrderStatus()));
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, message, data);
        return new JsonResolution(healthkartResponse);
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }
}
