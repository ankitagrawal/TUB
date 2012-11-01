package com.hk.db.seed.order;

import com.hk.constants.shippingOrder.EnumReplacementOrderStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.order.ReplacementOrderStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplacementOrderStatusSeedData extends BaseSeedData {

    public void insert(String name, Long id) {
        ReplacementOrderStatus replacementOrderStatus = new ReplacementOrderStatus();
        replacementOrderStatus.setName(name);
        replacementOrderStatus.setId(id);
        save(replacementOrderStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumReplacementOrderStatus enumReplacementOrderStatus : EnumReplacementOrderStatus.values()) {

            if (pkList.contains(enumReplacementOrderStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumReplacementOrderStatus.getId());
            else
                pkList.add(enumReplacementOrderStatus.getId());

            insert(enumReplacementOrderStatus.getName(), enumReplacementOrderStatus.getId());
        }
    }

}