package com.hk.impl.service.order;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.Store;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.payment.PaymentStatusDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.AutomatedOrderService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/13/12
 * Time: 6:46 PM
 */
@Service
public class AutomatedOrderServiceImpl implements AutomatedOrderService{

    @Autowired
    OrderManager orderManager;
    @Autowired
    OrderService orderService;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    OrderStatusService orderStatusService;
    @Autowired
    OrderLoggingService orderLoggingService;
    @Autowired
    UserService userService;
    @Autowired
    ShippingOrderService shippingOrderService;
    @Autowired
    LineItemDao lineItemDao;
    @Autowired
    PaymentStatusDao paymentStatusDao;
    @Autowired
    PaymentService paymentService;

    /**
     * creates base orders from within the code rather than through the usual UI flow.
     * This can be used for subscriptions and orders that are placed by other stores like MIH
     * @param order
     * @param cartLineItems
     * @param address
     * @param payment
     * @param store
     * @param isSubscriptionOrder
     * @return
     */
    public Order placeOrder(Order order, Set<CartLineItem> cartLineItems, Address address, Payment payment, Store store, boolean isSubscriptionOrder){
        //first of all save the cartLine items
        order.setCartLineItems(cartLineItems);
        //set subscriptionOrder if true
        order.setSubscriptionOrder(isSubscriptionOrder);
        //set address in the base_order
        order.setAddress(address);
        //set store in base order
        order.setStore(store);
        order = orderService.save(order);

        //update amount to be paid for the order... sequence is important here address need to be created priorhand!!
        orderManager.recalAndUpdateAmount(order);

        //update order payment status and order status in general
        orderManager.orderPaymentReceieved(payment);

        //finalize order -- create shipping order and update inventory
        finalizeOrder(order);
        return  order;
    }

    public Order createNewOrder(User user){
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(orderStatusService.find(EnumOrderStatus.InCart));
        order.setAmount(0D);
        order = orderService.save(order);
        return order;
    }

    public Payment createNewPayment(Order order, Double amount, PaymentMode paymentMode){
        Payment payment=new Payment();
        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setPaymentMode(paymentMode);
        PaymentStatus paymentStatus = paymentStatusDao.getPaymentStatusById(EnumPaymentStatus.AUTHORIZATION_PENDING.getId());;

        if (EnumPaymentMode.getPrePaidPaymentModes().contains(paymentMode.getId())) {
            paymentStatus = paymentStatusDao.getPaymentStatusById(EnumPaymentStatus.SUCCESS.getId());
        }
        payment.setPaymentStatus(paymentStatus);
        payment.setCreateDate(BaseUtils.getCurrentTimestamp());
        payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
        return paymentService.save(payment);
    }

    /**
     * takes necessary steps and updates inventory after an order is placed
     * @param order
     */
    public void finalizeOrder(Order order){

        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();

        boolean shippingOrderExists = false;

        //Check Inventory health of order lineitems
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
            orderLoggingService.logOrderActivity(order, userService.getAdminUser(), orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderSplit), null);

            // auto escalate shipping orders if possible
            if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(order.getPayment().getPaymentStatus().getId())) {
                for (ShippingOrder shippingOrder : shippingOrders) {
                    shippingOrderService.autoEscalateShippingOrder(shippingOrder);
                }
            }

        }

        //Check Inventory health of order lineitems
        for (CartLineItem cartLineItem : productCartLineItems) {
            inventoryService.checkInventoryHealth(cartLineItem.getProductVariant());
        }
    }
}
