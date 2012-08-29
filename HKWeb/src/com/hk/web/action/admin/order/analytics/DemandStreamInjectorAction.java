package com.hk.web.action.admin.order.analytics;

import java.util.Date;
import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.order.analytics.DemandHistoryDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.OrderSplitterService;
import com.hk.pojo.DummyOrder;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 7/24/12
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class DemandStreamInjectorAction extends BaseAction {

    Date streamStartDate;
    Date streamEndDate;

    @Autowired
    DemandHistoryDao demandHistoryDao;

    @Autowired
    OrderStatusService orderStatusService;

    @Autowired
    OrderSplitterService orderSplitterService;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    OrderService orderService;

    public Resolution saveHistoricalDemand() {
        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        orderSearchCriteria.setOrderStatusList(orderStatusService.getOrderStatuses(EnumOrderStatus.getStatusForReporting()));
        orderSearchCriteria.setPaymentStartDate(streamStartDate);
        orderSearchCriteria.setPaymentEndDate(streamEndDate);

        List<Order> orderList = orderService.searchOrders(orderSearchCriteria);

        Warehouse ggnWarehouse = warehouseService.getDefaultWarehouse();
        Warehouse mumWarehouse = warehouseService.getMumbaiWarehouse();

        if (orderList != null) {
            for (Order order : orderList) {
                List<DummyOrder> dummyOrders = orderSplitterService.listBestDummyOrdersIdeally(order, ggnWarehouse, mumWarehouse);
                if (dummyOrders != null) {
                    for (DummyOrder dummyOrder : dummyOrders) {
                        for (CartLineItem cartLineItem : dummyOrder.getCartLineItemList()) {
                            demandHistoryDao.createOrUpdateEntry(cartLineItem, dummyOrder.getWarehouse(), order);
                        }
                    }
                }
            }
        }
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }


    public Date getStreamStartDate() {
        return streamStartDate;
    }

    public void setStreamStartDate(Date streamStartDate) {
        this.streamStartDate = streamStartDate;
    }

    public Date getStreamEndDate() {
        return streamEndDate;
    }

    public void setStreamEndDate(Date streamEndDate) {
        this.streamEndDate = streamEndDate;
    }
}
