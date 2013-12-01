package com.hk.impl.service.shippingOrder;

import com.hk.domain.analytics.Reason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;
import com.hk.pact.service.shippingOrder.ShippingOrderLifecycleService;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Sep 26, 2012
 * Time: 10:57:35 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ShippingOrderLifecycleServiceImpl implements ShippingOrderLifecycleService {

    @Autowired
    private ShippingOrderLifecycleDao shippingOrderLifecycleDao;

    public List<ShippingOrderLifeCycleActivity> getOrderActivities(List<EnumShippingOrderLifecycleActivity> enumShippingOrderActivities){
        return shippingOrderLifecycleDao.getOrderActivities(enumShippingOrderActivities);
    }

	public List<ShippingOrderLifecycle> getShippingOrderLifecycleBySOAndActivity(Long shippingOrderId, Long shippingOrderLifeCycleActivityId) {
		return shippingOrderLifecycleDao.getShippingOrderLifecycleBySOAndActivities(shippingOrderId, Arrays.asList(shippingOrderLifeCycleActivityId));
	}

    @Override
    public List<ShippingOrderLifecycle> getShippingOrderLifecycleBySOAndActivities(Long shippingOrderId, List<Long> shippingOrderLifeCycleActivityIds) {
        return shippingOrderLifecycleDao.getShippingOrderLifecycleBySOAndActivities(shippingOrderId, shippingOrderLifeCycleActivityIds);
    }

    @Override
    public List<Reason> getReasonByType(String type) {
        return shippingOrderLifecycleDao.getReasonsByType(type);
    }

    public String getAwbByShippingOrderLifeCycle(ShippingOrder shippingOrder){
        return shippingOrderLifecycleDao.getAwbByShippingOrderLifeCycle(shippingOrder);
    }


}
