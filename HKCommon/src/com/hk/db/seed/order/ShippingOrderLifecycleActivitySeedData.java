package com.hk.db.seed.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;

@Component
public class ShippingOrderLifecycleActivitySeedData extends BaseSeedData {

    public void insert(String name, Long id) {
        ShippingOrderLifeCycleActivity shippingOrderLifeCycleActivity = new ShippingOrderLifeCycleActivity();
        shippingOrderLifeCycleActivity.setName(name);
        shippingOrderLifeCycleActivity.setId(id);
        save(shippingOrderLifeCycleActivity);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumShippingOrderLifecycleActivity enumShippingOrderLifecycleActivity : EnumShippingOrderLifecycleActivity.values()) {

            if (pkList.contains(enumShippingOrderLifecycleActivity.getId()))
                throw new RuntimeException("Duplicate key " + enumShippingOrderLifecycleActivity.getId());
            else
                pkList.add(enumShippingOrderLifecycleActivity.getId());

            insert(enumShippingOrderLifecycleActivity.getName(), enumShippingOrderLifecycleActivity.getId());
        }
    }

}