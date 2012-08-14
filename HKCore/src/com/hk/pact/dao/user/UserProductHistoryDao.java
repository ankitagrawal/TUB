package com.hk.pact.dao.user;

import java.util.List;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.domain.user.UserProductHistory;
import com.hk.pact.dao.BaseDao;


public interface UserProductHistoryDao extends BaseDao {

    public void addToUserProductHistory(Product product, User user);

    public UserProductHistory findByProductAndUser(Product product, User user);

    public void updateIsAddedToCart(Product product, User user, Order order);

    public List<UserProductHistory> findByUser(User user);
}
