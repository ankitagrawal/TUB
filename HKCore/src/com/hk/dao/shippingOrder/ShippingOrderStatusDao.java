package com.hk.dao.shippingOrder;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.order.ShippingOrderStatus;

@Repository
public class ShippingOrderStatusDao extends BaseDaoImpl {

    @SuppressWarnings("unchecked")
    public List<ShippingOrderStatus> getOrderStatuses(List<EnumShippingOrderStatus> enumShippingOrderStatuses) {
        List<Long> orderStatusIds = EnumShippingOrderStatus.getShippingOrderStatusIDs(enumShippingOrderStatuses);
        Criteria criteria = getSession().createCriteria(ShippingOrderStatus.class);
        criteria.add(Restrictions.in("id", orderStatusIds));

        return criteria.list();
    }

}
