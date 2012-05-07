package com.hk.impl.dao.shippingOrder;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.order.ShippingOrder;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;

@Repository
public class ShippingOrderLifecycleDaoImpl extends BaseDaoImpl implements ShippingOrderLifecycleDao {

    public Date getActivityDateForShippingOrder(ShippingOrder shippingOrder, List<EnumShippingOrderLifecycleActivity> shippingOrderLifecycleActivites) {
        List<Long> lifecycleActivityIds = EnumShippingOrderLifecycleActivity.getSOLifecycleActivityIDs(shippingOrderLifecycleActivites);
        return (Date) getSession().createQuery(
                "select max(sol.activityDate) from ShippingOrderLifecycle sol where sol.shippingOrder.id = :shippingOrderId "
                        + " and sol.shippingOrderLifeCycleActivity.id in ( :lifecycleActivityIds)").setLong("shippingOrderId", shippingOrder.getId()).setParameterList(
                "lifecycleActivityIds", lifecycleActivityIds).uniqueResult();
    }
}
