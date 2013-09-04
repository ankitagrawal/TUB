package com.hk.api.edge.ext.impl.service.cart;

import com.hk.api.edge.ext.pact.service.cart.HKCatalogCartService;
import com.hk.api.edge.ext.response.cart.CartSummaryApiResponse;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.store.EnumStore;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.OrderService;
import com.shiro.PrincipalImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Rimal
 */
@Service
public class HKCatalogCartServiceImpl implements HKCatalogCartService {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;


    @Override
    public CartSummaryApiResponse getCartSummary() {
        int itemsInCart = 0;
        User user = getPrincipalUser();

        if (user != null) {
            Order order = getOrderService().findCart(user, EnumStore.HEALTHKART.asStore());
            if (order != null) {
                Set<CartLineItem> cartLineItems = order.getCartLineItems();
                if (cartLineItems != null && !cartLineItems.isEmpty()) {
                    Set<CartLineItem> productCartLineItems = new CartLineItemFilter(cartLineItems).addCartLineItemType(EnumCartLineItemType.Product).filter();
                    if (productCartLineItems != null) {
                        itemsInCart = order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size();
//                        itemsInCart = Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size());
                    }
                }
                int inCartSubscriptions = new CartLineItemFilter(cartLineItems).addCartLineItemType(EnumCartLineItemType.Subscription).filter().size();
                itemsInCart += inCartSubscriptions;
            }
        }

        CartSummaryApiResponse cartSummaryApiResponse = new CartSummaryApiResponse();
        cartSummaryApiResponse.setItemsInCart(itemsInCart);
        return cartSummaryApiResponse;
    }


    private User getPrincipalUser() {
        if (getPrincipal() == null) {
            return null;
        }
        return getUserService().getUserById(getPrincipal().getId());
    }

    private PrincipalImpl getPrincipal() {
        return (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
    }


    public UserService getUserService() {
        return userService;
    }

    public OrderService getOrderService() {
        return orderService;
    }
}