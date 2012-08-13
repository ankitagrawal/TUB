package com.hk.pact.service.order;

import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.Store;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/13/12
 * Time: 6:44 PM
 */
public interface AutomatedOrderService {

    public Order placeOrder(Order order, Set<CartLineItem> cartLineItems, Address address, Payment payment, Store store, boolean isSubscriptionOrder);

    public Order createNewOrder(User user);

    public Payment createNewPayment(Order order, Double amount, PaymentMode paymentMode);

}
