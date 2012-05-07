package com.hk.impl.dao.shippingOrder;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.shippingOrder.ShippingOrderStatusDao;

@Repository
public class ShippingOrderStatusDaoImpl extends BaseDaoImpl implements ShippingOrderStatusDao {

    @SuppressWarnings("unchecked")
    public List<ShippingOrderStatus> getOrderStatuses(List<EnumShippingOrderStatus> enumShippingOrderStatuses) {
        List<Long> orderStatusIds = EnumShippingOrderStatus.getShippingOrderStatusIDs(enumShippingOrderStatuses);
        Criteria criteria = getSession().createCriteria(ShippingOrderStatus.class);
        criteria.add(Restrictions.in("id", orderStatusIds));

        return criteria.list();
    }

}
