package com.hk.web.action.admin.order.split;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.OrderSplitterService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 7/24/12
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class BulkOrderSplitterAction extends BaseAction {

    @Autowired
    OrderStatusService orderStatusService;

    @Autowired
    OrderSplitterService orderSplitterService;

    @Autowired
    OrderService orderService;

    @Autowired
    LineItemDao lineItemDao;

    @Autowired
    OrderLoggingService orderLoggingService;

    @Autowired
    ShippingOrderService shippingOrderService;

    @Autowired
    ShipmentService shipmentService;

    @DefaultHandler
    public Resolution bulkSplitOrders() {
        OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
        orderSearchCriteria.setOrderStatusList(orderStatusService.getOrderStatuses(Arrays.asList(EnumOrderStatus.Placed)));

        List<Order> orderList = orderService.searchOrders(orderSearchCriteria);


        if (orderList != null) {
            for (Order order : orderList) {
                boolean shippingOrderExists = false;

                Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();

                for (CartLineItem cartLineItem : productCartLineItems) {
                    if (lineItemDao.getLineItem(cartLineItem) != null) {
                        shippingOrderExists = true;
                    }
                }

                Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();

                if (!shippingOrderExists) {
                    shippingOrders = orderService.createShippingOrders(order);
                }

                if (shippingOrders != null && shippingOrders.size() > 0) {
                    // save order with InProcess status since shipping orders have been created
                    order.setOrderStatus(orderStatusService.find(EnumOrderStatus.InProcess));
                    order.setShippingOrders(shippingOrders);
                    order = orderService.save(order);

                    /**
                     * Order lifecycle activity logging - Order split to shipping orders
                     */
                    orderLoggingService.logOrderActivity(order, getUserService().getAdminUser(), orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderSplit), null);

                    // auto escalate shipping orders if possible
                    if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(order.getPayment().getPaymentStatus().getId())) {
                        for (ShippingOrder shippingOrder : shippingOrders) {
                            shippingOrderService.autoEscalateShippingOrder(shippingOrder);
                        }
                    }

                    for (ShippingOrder shippingOrder : shippingOrders) {
                        shipmentService.createShipment(shippingOrder);
                    }
                }
            }
        }
        return new ForwardResolution("/pages/admin/shipment/shipmentCostCalculator.jsp");
    }

}

