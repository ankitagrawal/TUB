package com.hk.pact.service.user;

import java.util.List;
import java.util.Set;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;

public interface UserProfileService {

    public List<Order> getOrdersForUserSortedByDate(User user);

    public Set<ProductVariant> getRecentlyOrderedProductVariantsForUser(User user);

}
