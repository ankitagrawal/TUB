package com.hk.impl.service.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.pact.dao.user.UserProfileDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.subscription.SubscriptionStatusService;
import com.hk.pact.service.user.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private OrderStatusService orderStatusService;
    @Autowired
    private UserProfileDao     userProfileDao;
    @Autowired
    private SubscriptionStatusService subscriptionStatusService;

    public Set<ProductVariant> getRecentlyOrderedProductVariantsForUser(User user) {
        Map<String, ProductVariant> recentlyOrderedProductVariantsMap = new HashMap<String, ProductVariant>();
        List<Order> ordersByRecentDate = new ArrayList<Order>(getOrdersForUserSortedByDate(user));
        Product product;
        ProductVariant productVariant;
        if (!ordersByRecentDate.isEmpty()) {
            for (Order order : ordersByRecentDate) {
                CartLineItemFilter cartLineItemFilter = new CartLineItemFilter(new HashSet<CartLineItem>(order.getCartLineItems()));
                Set<CartLineItem> productCartLineItems = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
                for (CartLineItem cartLineItem : productCartLineItems) {
                    productVariant = cartLineItem.getProductVariant();
                    product = cartLineItem.getProductVariant().getProduct();
                    if (!recentlyOrderedProductVariantsMap.containsKey(product.getId())) {
                        if ((productVariant != null) && (productVariant.getOutOfStock() == Boolean.FALSE) && (productVariant.getDeleted() == Boolean.FALSE)) {
                            recentlyOrderedProductVariantsMap.put(product.getId(), productVariant);
                            if (recentlyOrderedProductVariantsMap.size() == 3) {
                                break;
                            }
                        }
                    }
                }
                if (recentlyOrderedProductVariantsMap.size() == 3) {
                    break;
                }
            }
        }
        return new HashSet<ProductVariant>(recentlyOrderedProductVariantsMap.values());
    }

    public List<Order> getOrdersForUserSortedByDate(User user) {
        List<OrderStatus> orderStatusList = getOrderStatusService().getOrderStatuses(EnumOrderStatus.getStatusForCustomers());
        return getUserProfileDao().getOrdersForUserSortedByDate(orderStatusList, user);
    }

    public OrderStatusService getOrderStatusService() {
        return orderStatusService;
    }

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public UserProfileDao getUserProfileDao() {
        return userProfileDao;
    }

    public void setUserProfileDao(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    public SubscriptionStatusService getSubscriptionStatusService() {
        return subscriptionStatusService;
    }

    public void setSubscriptionStatusService(SubscriptionStatusService subscriptionStatusService) {
        this.subscriptionStatusService = subscriptionStatusService;
    }
}
