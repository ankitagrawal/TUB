package com.hk.web.action.admin.order.split;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.dto.order.DummyOrder;
import com.hk.admin.impl.service.order.OrderSplitterService;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.order.OrderService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/26/12
 * Time: 12:59 AM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS}, authActionBean = AdminPermissionAction.class)
@Component
public class PseudoOrderSplitAction extends BaseAction {
    @Autowired
    OrderSplitterService orderSplitterService;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    OrderService orderService;

    String gatewayOrderId;

    List<DummyOrder> dummyOrderList;

    Map<List<DummyOrder>, Long> sortedDummyOrderMaps;

    Order order;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/order/pseudoOrderSplitter.jsp");
    }

    public Resolution splitOrderPractically() {
        order = orderService.findByGatewayOrderId(gatewayOrderId);
        if (order != null) {
            sortedDummyOrderMaps = orderSplitterService.splitBOPractically(order);
        } else {
            addRedirectAlertMessage(new SimpleMessage("No Order found for corresponding gateway Order ID"));
        }
        return new ForwardResolution("/pages/admin/order/pseudoOrderSplitter.jsp");
    }

    public Resolution splitOrderIdeally() {
        Warehouse ggnWarehouse = warehouseService.getDefaultWarehouse();
        Warehouse mumWarehouse = warehouseService.getMumbaiWarehouse();

        order = orderService.findByGatewayOrderId(gatewayOrderId);
        if (order != null) {
            sortedDummyOrderMaps = orderSplitterService.splitBOIdeally(order, ggnWarehouse, mumWarehouse);
        } else {
            addRedirectAlertMessage(new SimpleMessage("No Order found for corresponding gateway Order ID"));
        }
        return new ForwardResolution("/pages/admin/order/pseudoOrderSplitter.jsp");
    }


    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public List<DummyOrder> getDummyOrderList() {
        return dummyOrderList;
    }

    public void setDummyOrderList(List<DummyOrder> dummyOrderList) {
        this.dummyOrderList = dummyOrderList;
    }

    public Map<List<DummyOrder>, Long> getSortedDummyOrderMaps() {
        return sortedDummyOrderMaps;
    }

    public void setSortedDummyOrderMaps(Map<List<DummyOrder>, Long> sortedDummyOrderMaps) {
        this.sortedDummyOrderMaps = sortedDummyOrderMaps;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
