package com.hk.pact.dao.user;

import java.util.List;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.domain.user.UserCart;
import com.hk.pact.dao.BaseDao;

public interface UserCartDao extends BaseDao {

    public void addToCartHistory(Product product, User user);

    public List<UserCart> findByUser(User user);

    public void updateIsProductBought(Order order);
}
