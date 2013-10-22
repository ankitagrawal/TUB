package com.hk.api.edge.integration.impl.service.cart;

import com.hk.api.edge.integration.pact.service.cart.HybridCartService;
import com.hk.api.edge.integration.response.cart.CartSummaryFromHKR;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.store.EnumStore;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Rimal
 */
@Service
public class HybridCartServiceImpl implements HybridCartService {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @SuppressWarnings("deprecation")
    @Override
    public CartSummaryFromHKR getUserCartSummaryFromHKR(Long userId) {
        int itemsInCart = 0;
//        User user = getUserService().getLoggedInUser();

        if (userId != null) {
            User user = getUserService().getUserById(userId);
            if (user != null) {
                Order order = getOrderService().findCart(user, EnumStore.HEALTHKART.asStore());
                if (order != null) {
                    Set<CartLineItem> cartLineItems = order.getCartLineItems();
                    if (cartLineItems != null && !cartLineItems.isEmpty()) {
                        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(cartLineItems).addCartLineItemType(EnumCartLineItemType.Product).filter();
                        if (productCartLineItems != null) {
                            itemsInCart = order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size();
                            // itemsInCart = Long.valueOf(order.getExclusivelyProductCartLineItems().size() +
                            // order.getExclusivelyComboCartLineItems().size());
                        }
                    }
                    /*int inCartSubscriptions = new CartLineItemFilter(cartLineItems).addCartLineItemType(EnumCartLineItemType.Subscription).filter().size();
                  itemsInCart += inCartSubscriptions;*/
                }
            }
        }

        CartSummaryFromHKR cartSummaryFromHKR = new CartSummaryFromHKR();
        cartSummaryFromHKR.setItemsInCart(itemsInCart);
        return cartSummaryFromHKR;
    }

    public UserService getUserService() {
        return userService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

}