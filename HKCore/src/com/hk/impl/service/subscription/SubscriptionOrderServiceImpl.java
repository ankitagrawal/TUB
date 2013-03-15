package com.hk.impl.service.subscription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.subscription.EnumSubscriptionLifecycleActivity;
import com.hk.constants.subscription.EnumSubscriptionOrderStatus;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.domain.builder.CartLineItemBuilder;
import com.hk.domain.builder.SubscriptionOrderBuilder;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionOrder;
import com.hk.domain.subscription.SubscriptionOrderStatus;
import com.hk.domain.user.User;
import com.hk.pact.dao.subscription.SubscriptionOrderDao;
import com.hk.pact.service.order.AutomatedOrderService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.store.StoreService;
import com.hk.pact.service.subscription.SubscriptionLoggingService;
import com.hk.pact.service.subscription.SubscriptionOrderService;
import com.hk.pact.service.subscription.SubscriptionService;

/**
 * Created with IntelliJ IDEA. User: Pradeep Date: 7/13/12 Time: 6:56 PM
 */
@Service
public class SubscriptionOrderServiceImpl implements SubscriptionOrderService {

    @Autowired
    private AutomatedOrderService      automatedOrderService;
    @Autowired
    private StoreService               storeService;
    @Autowired
    private SubscriptionOrderDao       subscriptionOrderDao;
    @Autowired
    private SubscriptionService        subscriptionService;
    @Autowired
    private SubscriptionLoggingService subscriptionLoggingService;
    @Autowired
    private OrderService               orderService;

    public SubscriptionOrder save(SubscriptionOrder subscriptionOrder) {
        return subscriptionOrderDao.save(subscriptionOrder);
    }

    public SubscriptionOrder findSubscriptionOrderByBaseOrder(Order order) {
        return subscriptionOrderDao.findSubscriptionOrderByBaseOrder(order);
    }

    public List<SubscriptionOrder> findSubscriptionOrdersForSubscription(Subscription subscription) {
        return subscriptionOrderDao.findSubscriptionOrdersForSubscription(subscription);
    }

    public List<SubscriptionOrder> findSubscriptionOrdersForSubscription(Subscription subscription, SubscriptionOrderStatus subscriptionOrderStatus) {
        return subscriptionOrderDao.findSubscriptionOrdersForSubscription(subscription, subscriptionOrderStatus);
    }

    /**
     * create base order for subscription and an entry in subscription_order
     * 
     * @param subscription
     * @return
     */
    public Order createOrderForSubscription(Subscription subscription) {
        User user = subscription.getUser();
        Order order = automatedOrderService.createNewOrder(user);
        Set<CartLineItem> cartLineItemSet = createSubscriptionOrderCartLineItems(subscription, order);
        Payment payment = createSubscriptionPayment(order, cartLineItemSet);

        order = automatedOrderService.placeOrder(order, cartLineItemSet, subscription.getAddress(), payment, storeService.getDefaultStore(), true);

        subscription.setSubscriptionStatus(EnumSubscriptionStatus.InProcess.asSubscriptionStatus());
        subscriptionService.save(subscription);
        subscriptionLoggingService.logSubscriptionActivityByAdmin(subscription, EnumSubscriptionLifecycleActivity.SubscriptionOrderPlaced, "automated order generation");
        // create an entry in subscription_order table
        createSubscriptionOrder(subscription, order);
        updateParentBOStatus(subscription);

        return order;
    }

    private Order updateParentBOStatus(Subscription subscription) {
        Order bo = subscription.getBaseOrder();
        boolean parentBOHasProducts = false;
        if (bo.getOrderStatus().getId().equals(EnumOrderStatus.Placed.getId())) {
            for (CartLineItem cartLineItem : bo.getCartLineItems()) {
                if (cartLineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId())) {
                    parentBOHasProducts = true;
                    break;
                }
            }
            if (!parentBOHasProducts) {
                bo.setOrderStatus(EnumOrderStatus.InProcess.asOrderStatus());
                orderService.save(bo);
            }
        }
        return bo;
    }

    /**
     * create base orders for a list of base orders
     * 
     * @param subscriptions
     * @return
     */
    public List<Order> createOrdersForSubscriptions(List<Subscription> subscriptions) {
        List<Order> orderList = new ArrayList<Order>();
        for (Subscription subscription : subscriptions) {
            orderList.add(this.createOrderForSubscription(subscription));
        }
        return orderList;
    }

    /**
     * used to create an entry in subscription_order table
     * 
     * @param subscription
     * @param order
     * @return
     */
    public SubscriptionOrder createSubscriptionOrder(Subscription subscription, Order order) {
        SubscriptionOrderBuilder subscriptionOrderBuilder = new SubscriptionOrderBuilder();
        subscriptionOrderBuilder.forSubscription(subscription).withStatus(EnumSubscriptionOrderStatus.Placed).setBaseOrder(order);
        SubscriptionOrder subscriptionOrder = subscriptionOrderBuilder.build();
        return this.save(subscriptionOrder);
    }

    /**
     * creates cartLineItems for subscription based on what is best for customer - current price or price at
     * subscription
     * 
     * @param subscription
     * @return
     */
    private Set<CartLineItem> createSubscriptionOrderCartLineItems(Subscription subscription, Order order) {

        Set<CartLineItem> cartLineItemSet = new HashSet<CartLineItem>();
        ProductVariant productVariant = subscription.getProductVariant();
        /*
         * Double subscriptionPrice = subscription.getSubscriptionPrice(); Double currentPrice =
         * productVariant.getHkPrice();
         */

        // Double subscriptionOrderPrice = (currentPrice > subscriptionPrice)? subscriptionPrice : currentPrice;

        CartLineItemBuilder cartLineItemBuilder = new CartLineItemBuilder();
        cartLineItemBuilder.ofType(EnumCartLineItemType.Product);
        cartLineItemBuilder.forVariantQty(productVariant, subscription.getQtyPerDelivery()).hkPrice(subscription.getHkPriceAtSubscription()).markedPrice(
                productVariant.getMarkedPrice()).discountOnHkPrice((subscription.getHkPriceAtSubscription() - subscription.getSubscriptionPrice())*subscription.getQtyPerDelivery());
        CartLineItem cartLineItem = cartLineItemBuilder.build();
        cartLineItem.setOrder(order);

        cartLineItemSet.add(cartLineItem);

        return cartLineItemSet;

    }

    /**
     * @param order
     * @param cartLineItems
     * @return
     */
    private Payment createSubscriptionPayment(Order order, Set<CartLineItem> cartLineItems) {

        Double amount = 0.0D;
        for (CartLineItem cartLineItem : cartLineItems) {
            amount += cartLineItem.getHkPrice()*cartLineItem.getQty()-cartLineItem.getDiscountOnHkPrice();
        }

        return automatedOrderService.createNewPayment(order, amount, EnumPaymentMode.SUBSCRIPTION_PAYMENT.asPaymenMode());

    }

    public void markSubscriptionOrderAsDelivered(Order order) {
        if (order.isSubscriptionOrder()) {
            SubscriptionOrder subscriptionOrder = this.findSubscriptionOrderByBaseOrder(order);
            if (!(subscriptionOrder.getSubscriptionOrderStatus().getId().longValue() == EnumSubscriptionOrderStatus.Delivered.getId().longValue())) {
                subscriptionOrder.setSubscriptionOrderStatus(EnumSubscriptionOrderStatus.Delivered.asSubscriptionOrderStatus());
                subscriptionOrder = this.save(subscriptionOrder);

                Subscription subscription = subscriptionOrder.getSubscription();
                List<SubscriptionOrder> subscriptionOrders = this.findSubscriptionOrdersForSubscription(subscription,
                        EnumSubscriptionOrderStatus.Delivered.asSubscriptionOrderStatus());
                subscription.setQtyDelivered(subscription.getQtyPerDelivery()*(new Long(subscriptionOrders.size())));
                subscriptionService.updateSubscriptionAfterOrderDelivery(subscription);
                if (subscription.getQty() <= subscription.getQtyDelivered()) {

                }
            }
        }
    }

    public void markSubscriptionOrderAsShipped(Order order) {
        if (order.isSubscriptionOrder()) {

            SubscriptionOrder subscriptionOrder = this.findSubscriptionOrderByBaseOrder(order);
            if (!(subscriptionOrder.getSubscriptionOrderStatus().getId().longValue() == EnumSubscriptionOrderStatus.Shipped.getId().longValue())) {
                subscriptionOrder.setSubscriptionOrderStatus(EnumSubscriptionOrderStatus.Shipped.asSubscriptionOrderStatus());
                subscriptionOrder = this.save(subscriptionOrder);

                Subscription subscription = subscriptionOrder.getSubscription();
                List<SubscriptionOrder> subscriptionOrders = this.findSubscriptionOrdersForSubscription(subscription,
                        EnumSubscriptionOrderStatus.Delivered.asSubscriptionOrderStatus());
                subscription.setQtyDelivered(subscription.getQtyPerDelivery()*(new Long(subscriptionOrders.size())));
                subscriptionService.save(subscription);
            }
        }
    }

    public AutomatedOrderService getAutomatedOrderService() {
        return automatedOrderService;
    }

    public void setAutomatedOrderService(AutomatedOrderService automatedOrderService) {
        this.automatedOrderService = automatedOrderService;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public SubscriptionOrderDao getSubscriptionOrderDao() {
        return subscriptionOrderDao;
    }

    public void setSubscriptionOrderDao(SubscriptionOrderDao subscriptionOrderDao) {
        this.subscriptionOrderDao = subscriptionOrderDao;
    }

    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public SubscriptionLoggingService getSubscriptionLoggingService() {
        return subscriptionLoggingService;
    }

    public void setSubscriptionLoggingService(SubscriptionLoggingService subscriptionLoggingService) {
        this.subscriptionLoggingService = subscriptionLoggingService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
