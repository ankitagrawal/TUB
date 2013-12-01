package com.hk.impl.dao.order;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderLifecycle;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.order.OrderLifecycleDao;

@Repository
public class OrderLifecycleDaoImpl extends BaseDaoImpl implements OrderLifecycleDao {

    public Long containsOrderLifeCycleActivity(Order order, OrderLifecycleActivity orderLifecycleActivity) {
        return (Long) getSession().createQuery("select id from OrderLifecycle olc where olc.order = :order and olc.orderLifecycleActivity = :orderLifecycleActivity").setParameter(
                "order", order).setParameter("orderLifecycleActivity", orderLifecycleActivity).uniqueResult();
    }

    public OrderLifecycle getLatestOrderActivityByActvityType(Order order, List<Long> activityIds) {
        String query = "select olc from OrderLifecycle olc where olc.order=:order and olc.orderLifecycleActivity.id in (:orderLifeCycleActivityIds) order by olc.id desc";

        return (OrderLifecycle) getSession().createQuery(query).setParameter("order", order).setParameterList("orderLifeCycleActivityIds", activityIds).setMaxResults(1).uniqueResult();
    }

}
