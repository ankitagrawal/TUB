package com.hk.pact.service.shippingOrder;

import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.analytics.Reason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.shippingOrder.LifecycleReason;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Sep 26, 2012
 * Time: 10:58:31 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ShippingOrderLifecycleService {

    public List<ShippingOrderLifeCycleActivity> getOrderActivities(List<EnumShippingOrderLifecycleActivity> enumShippingOrderActivities);

    public List<ShippingOrderLifecycle> getShippingOrderLifecycleBySOAndActivity(Long shippingOrderId, Long shippingOrderLifeCycleActivityId);

    public List<ShippingOrderLifecycle> getShippingOrderLifecycleBySOAndActivities(Long shippingOrderId, List<Long> shippingOrderLifeCycleActivityId);

    public List<Reason> getReasonByType(String type);

    public String getAwbByShippingOrderLifeCycle(ShippingOrder shippingOrder);
}
