package com.hk.pact.dao.shippingOrder;

import java.util.Date;
import java.util.List;

import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.pact.dao.BaseDao;

public interface ShippingOrderLifecycleDao extends BaseDao {

    public Date getActivityDateForShippingOrder(ShippingOrder shippingOrder, List<EnumShippingOrderLifecycleActivity> shippingOrderLifecycleActivites) ;

    public List<ShippingOrderLifeCycleActivity> getOrderActivities(List<EnumShippingOrderLifecycleActivity> enumShippingOrderActivities);
}
