package com.hk.dao.shippingOrder;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.order.ShippingOrder;

@Repository
public class ShippingOrderLifecycleDao extends BaseDaoImpl {

    public Date getActivityDateForShippingOrder(ShippingOrder shippingOrder, List<EnumShippingOrderLifecycleActivity> shippingOrderLifecycleActivites) {
        List<Long> lifecycleActivityIds = EnumShippingOrderLifecycleActivity.getSOLifecycleActivityIDs(shippingOrderLifecycleActivites);
        return (Date) getSession().createQuery(
                "select max(sol.activityDate) from ShippingOrderLifecycle sol where sol.shippingOrder.id = :shippingOrderId "
                        + " and sol.shippingOrderLifeCycleActivity.id in ( :lifecycleActivityIds)").setLong("shippingOrderId", shippingOrder.getId()).setParameterList(
                "lifecycleActivityIds", lifecycleActivityIds).uniqueResult();
    }
}
