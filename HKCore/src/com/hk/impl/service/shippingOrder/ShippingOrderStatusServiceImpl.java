package com.hk.impl.service.shippingOrder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.pact.dao.shippingOrder.ShippingOrderStatusDao;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;

@Service
public class ShippingOrderStatusServiceImpl implements ShippingOrderStatusService {

    @Autowired
    private ShippingOrderStatusDao shippingOrderStatusDao;

    public ShippingOrderStatus find(EnumShippingOrderStatus enumShippingOrderStatus) {
        return this.find(enumShippingOrderStatus.getId());
    }

    public List<ShippingOrderStatus> getOrderStatuses(List<EnumShippingOrderStatus> enumShippingOrderStatuses) {
        return getShippingOrderStatusDao().getOrderStatuses(enumShippingOrderStatuses);
    }

    public ShippingOrderStatus find(Long shippingOrderStatusId) {
        return getShippingOrderStatusDao().get(ShippingOrderStatus.class, shippingOrderStatusId);
    }

    public ShippingOrderStatusDao getShippingOrderStatusDao() {
        return shippingOrderStatusDao;
    }

    public void setShippingOrderStatusDao(ShippingOrderStatusDao shippingOrderStatusDao) {
        this.shippingOrderStatusDao = shippingOrderStatusDao;
    }

}
