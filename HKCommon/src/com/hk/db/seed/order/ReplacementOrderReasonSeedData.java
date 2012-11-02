package com.hk.db.seed.order;

import com.hk.constants.shippingOrder.EnumReplacementOrderReason;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.order.ReplacementOrderReason;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplacementOrderReasonSeedData extends BaseSeedData {

    public void insert(String name, Long id) {
        ReplacementOrderReason replacementOrderReason = new ReplacementOrderReason();
        replacementOrderReason.setName(name);
        replacementOrderReason.setId(id);
        save(replacementOrderReason);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumReplacementOrderReason enumReplacementOrderReason : EnumReplacementOrderReason.values()) {

            if (pkList.contains(enumReplacementOrderReason.getId()))
                throw new RuntimeException("Duplicate key " + enumReplacementOrderReason.getId());
            else
                pkList.add(enumReplacementOrderReason.getId());

            insert(enumReplacementOrderReason.getName(), enumReplacementOrderReason.getId());
        }
    }

}