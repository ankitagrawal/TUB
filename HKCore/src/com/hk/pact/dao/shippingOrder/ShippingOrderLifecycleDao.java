package com.hk.pact.dao.shippingOrder;

import java.util.Date;
import java.util.List;

import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.analytics.Reason;
import com.hk.domain.courier.Awb;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.pact.dao.BaseDao;

public interface ShippingOrderLifecycleDao extends BaseDao {

    public Date getActivityDateForShippingOrder(ShippingOrder shippingOrder, List<EnumShippingOrderLifecycleActivity> shippingOrderLifecycleActivites) ;

    public List<ShippingOrderLifeCycleActivity> getOrderActivities(List<EnumShippingOrderLifecycleActivity> enumShippingOrderActivities);

    List<ShippingOrderLifecycle> getShippingOrderLifecycleBySOAndActivities(Long shippingOrderId, List<Long> shippingOrderLifeCycleActivityIds);

    List<Reason> getReasonsByType(String type);

    public String getAwbByShippingOrderLifeCycle(ShippingOrder shippingOrder);
}
