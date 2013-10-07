package com.hk.web.action.admin.order.split;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.hk.splitter.LineItemClassification;
import com.hk.splitter.impl.OrderSplitterImpl;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.service.order.OrderSplitterServiceImpl;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.order.OrderService;
import com.hk.pojo.DummyOrder;
import com.hk.web.action.error.AdminPermissionAction;

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
    OrderSplitterServiceImpl orderSplitterServiceImpl;

    @Autowired
    OrderSplitterImpl orderSplitter;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    OrderService orderService;

    String gatewayOrderId;

    List<DummyOrder> dummyOrderList;

    Map<List<DummyOrder>, Long> sortedDummyOrderMaps;

    Order order;

    Collection<LineItemClassification> lineItemClassifications;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/order/pseudoOrderSplitter.jsp");
    }

    public Resolution splitOrderPractically() {
        order = orderService.findByGatewayOrderId(gatewayOrderId);
        if (order != null) {
            sortedDummyOrderMaps = orderSplitterServiceImpl.listSortedDummyOrderMapPractically(order);
        } else {
            addRedirectAlertMessage(new SimpleMessage("No Order found for corresponding gateway Order ID"));
        }
        return new ForwardResolution("/pages/admin/order/pseudoOrderSplitter.jsp");
    }

    public Resolution splitViaNewSplitter() {
        order = orderService.findByGatewayOrderId(gatewayOrderId);
        if (order != null) {
            lineItemClassifications = orderSplitter.feedData(order);
            sortedDummyOrderMaps = orderSplitter.createCombinations(null,order,lineItemClassifications);
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
            sortedDummyOrderMaps = orderSplitterServiceImpl.splitBOIdeally(order, ggnWarehouse, mumWarehouse);
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
