package com.hk.impl.dao.user;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.UserProfileDao;

@Repository
public class UserProfileDaoImpl extends BaseDaoImpl implements UserProfileDao {

    @Override
    public List<Order> getOrdersForUserSortedByDate(List<OrderStatus> orderStatusList, User user) {
        DetachedCriteria orderCriteria = DetachedCriteria.forClass(Order.class);
        DetachedCriteria userCriteria = orderCriteria.createCriteria("user");
        userCriteria.add(Restrictions.eq("id", user.getId()));

        orderCriteria.add(Restrictions.in("orderStatus", orderStatusList));
        orderCriteria.addOrder(org.hibernate.criterion.Order.desc("createDate"));
       /* Boolean[] subscriptionOrders={false};
        orderCriteria.add(Restrictions.in("subscriptionOrder",subscriptionOrders));*/
        return findByCriteria(orderCriteria);
    }

}
