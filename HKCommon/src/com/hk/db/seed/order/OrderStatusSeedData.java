package com.hk.db.seed.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.OrderStatus;

@Component
public class OrderStatusSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setName(name);
        orderStatus.setId(id);
        save(orderStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumOrderStatus enumOrderStatus : EnumOrderStatus.values()) {

            if (pkList.contains(enumOrderStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumOrderStatus.getId());
            else
                pkList.add(enumOrderStatus.getId());

            insert(enumOrderStatus.getName(), enumOrderStatus.getId());
        }
    }

}
