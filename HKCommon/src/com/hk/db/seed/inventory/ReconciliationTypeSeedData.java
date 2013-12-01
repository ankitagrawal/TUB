package com.hk.db.seed.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.inventory.rv.ReconciliationType;

@Component
public class ReconciliationTypeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        ReconciliationType reconciliationType = new ReconciliationType();
        reconciliationType.setName(name);
        reconciliationType.setId(id);
        save(reconciliationType);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumReconciliationType enumReconciliationType : EnumReconciliationType.values()) {

            if (pkList.contains(enumReconciliationType.getId()))
                throw new RuntimeException("Duplicate key " + enumReconciliationType.getId());
            else
                pkList.add(enumReconciliationType.getId());

            insert(enumReconciliationType.getName(), enumReconciliationType.getId());
        }
    }

}
