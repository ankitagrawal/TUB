package com.hk.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.core.OrderStatus;


@SuppressWarnings("unchecked")
@Repository
public class OrderStatusDao extends BaseDaoImpl {

    public OrderStatus getOrderStatusById(Long orderStatusId){
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

    public static List<EnumOrderStatus> getStatusForReporting() {
        return Arrays.asList(EnumOrderStatus.InProcess, EnumOrderStatus.Placed, EnumOrderStatus.Shipped, EnumOrderStatus.Delivered);
    }

}
