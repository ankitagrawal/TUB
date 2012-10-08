package com.hk.pact.service.shippingOrder;

import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;

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
}
