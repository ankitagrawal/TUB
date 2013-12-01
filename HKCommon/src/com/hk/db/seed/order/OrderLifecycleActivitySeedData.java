package com.hk.db.seed.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.OrderLifecycleActivity;

@Component
public class OrderLifecycleActivitySeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        OrderLifecycleActivity orderLifecycleActivity = new OrderLifecycleActivity();
        orderLifecycleActivity.setName(name);
        orderLifecycleActivity.setId(id);
        save(orderLifecycleActivity);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumOrderLifecycleActivity enumOrderLifecycleActivity : EnumOrderLifecycleActivity.values()) {

            if (pkList.contains(enumOrderLifecycleActivity.getId()))
                throw new RuntimeException("Duplicate key " + enumOrderLifecycleActivity.getId());
            else
                pkList.add(enumOrderLifecycleActivity.getId());

            insert(enumOrderLifecycleActivity.getName(), enumOrderLifecycleActivity.getId());
        }
    }

}
