package web.action.admin.shippingOrder;

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
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.service.SkuService;
import com.hk.service.WarehouseService;
import com.hk.service.shippingOrder.ShippingOrderService;
import com.hk.web.HealthkartResponse;

@Component
public class ShippingOrderAction extends BaseAction {

    private ShippingOrder             shippingOrder;

    WarehouseService                  warehouseService;

    @Autowired
    private ShippingOrderService      shippingOrderService;
    @Autowired
    private AdminShippingOrderService adminShippingOrderService;

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

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }
}
