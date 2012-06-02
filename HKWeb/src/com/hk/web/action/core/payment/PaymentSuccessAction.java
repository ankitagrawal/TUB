package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.dto.pricing.PricingDto;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class PaymentSuccessAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(PaymentSuccessAction.class);

    @Validate(required = true, encrypted = true)
    private String gatewayOrderId;

    private Payment payment;
    private PricingDto pricingDto;

    @Autowired
    private PaymentDao paymentDao;
    @Autowired
    UserDao userDao;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private OrderLoggingService orderLoggingService;
    @Autowired
    private OrderStatusService orderStatusService;
    @Autowired
    private LineItemDao lineItemDao;


    public Resolution pre() {
        payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        if (payment != null) {

            logger.debug("payment success page payment status " + payment.getPaymentStatus().getId());

            Order order = payment.getOrder();
            pricingDto = new PricingDto(order.getCartLineItems(), payment.getOrder().getAddress());

            Set<CartLineItem> productCartLineItems = new CartLineItemFilter(payment.getOrder().getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();

            boolean shippingOrderExists = false;

            //Check Inventory health of order lineitems
            for (CartLineItem cartLineItem : productCartLineItems) {
                if (lineItemDao.getLineItem(cartLineItem) != null) {
                    shippingOrderExists = true;
                }
            }

            Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();

            if (!shippingOrderExists) {
                shippingOrders = getOrderService().createShippingOrders(order);
            }

            if (shippingOrders != null && shippingOrders.size() > 0) {
                // save order with InProcess status since shipping orders have been created
                order.setOrderStatus(getOrderStatusService().find(EnumOrderStatus.InProcess));
                order.setShippingOrders(shippingOrders);
                order = getOrderService().save(order);

                /**
                 * Order lifecycle activity logging - Order split to shipping orders
                 */
                getOrderLoggingService().logOrderActivity(order, getUserService().getAdminUser(), getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderSplit), null);

                // auto escalate shipping orders if possible
                if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(order.getPayment().getPaymentStatus().getId())) {
                    for (ShippingOrder shippingOrder : shippingOrders) {
                        getShippingOrderService().autoEscalateShippingOrder(shippingOrder);
                    }
                }

            }

            //Check Inventory health of order lineitems
            for (CartLineItem cartLineItem : productCartLineItems) {
                inventoryService.checkInventoryHealth(cartLineItem.getProductVariant());
            }

        }
        return new ForwardResolution("/pages/payment/paymentSuccess.jsp");
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public Payment getPayment() {
        return payment;
    }

    public PricingDto getPricingDto() {
        return pricingDto;
    }


    public void setPaymentDao(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public OrderLoggingService getOrderLoggingService() {
        return orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }

    public OrderStatusService getOrderStatusService() {
        return orderStatusService;
    }

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }
}
