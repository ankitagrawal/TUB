package com.hk.db.seed.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.order.ShippingOrderStatus;

@Component
public class ShippingOrderStatusSeedData extends BaseSeedData {

    public void insert(String name, Long id) {
        ShippingOrderStatus shippingOrderStatus = new ShippingOrderStatus();
        shippingOrderStatus.setName(name);
        shippingOrderStatus.setId(id);
        save(shippingOrderStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumShippingOrderStatus enumShippingOrderStatus : EnumShippingOrderStatus.values()) {

            if (pkList.contains(enumShippingOrderStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumShippingOrderStatus.getId());
            else
                pkList.add(enumShippingOrderStatus.getId());

            insert(enumShippingOrderStatus.getName(), enumShippingOrderStatus.getId());
        }
    }

}