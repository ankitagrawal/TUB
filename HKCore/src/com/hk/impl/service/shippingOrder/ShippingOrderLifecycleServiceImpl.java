package com.hk.impl.service.shippingOrder;

import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;
import com.hk.pact.service.shippingOrder.ShippingOrderLifecycleService;

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


}
