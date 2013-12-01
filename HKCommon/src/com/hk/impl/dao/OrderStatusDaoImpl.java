package com.hk.impl.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.core.OrderStatus;
import com.hk.pact.dao.OrderStatusDao;

@Repository
public class OrderStatusDaoImpl extends BaseDaoImpl implements OrderStatusDao {
    public OrderStatus getOrderStatusById(Long orderStatusId) {
        return get(OrderStatus.class, orderStatusId);
    }

    @SuppressWarnings("unchecked")
    public List<OrderStatus> getOrderStatuses(List<EnumOrderStatus> enumOrderStatus) {
        List<Long> orderStatusIds = EnumOrderStatus.getOrderStatusIDs(enumOrderStatus);
        Criteria criteria = getSession().createCriteria(OrderStatus.class);
        criteria.add(Restrictions.in("id", orderStatusIds));

        return criteria.list();
    }

    public List<OrderStatus> listOrderStatusForReporting() {
        List<Long> orderStatusIds = new ArrayList<Long>();
        orderStatusIds.add(EnumOrderStatus.Shipped.getId());
        orderStatusIds.add(EnumOrderStatus.Delivered.getId());
        orderStatusIds.add(EnumOrderStatus.RTO.getId());
        orderStatusIds.add(EnumOrderStatus.Cancelled.getId());

        DetachedCriteria criteria = DetachedCriteria.forClass(OrderStatus.class);
        criteria.add(Restrictions.in("id", orderStatusIds));
        return findByCriteria(criteria);
    }

}
