package com.hk.db.seed.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.inventory.rv.ReconciliationStatus;

/**
 * Created by IntelliJ IDEA. User: Pratham Date: Jul 5, 2011 Time: 2:00:18 PM To change this template use File |
 * Settings | File Templates.
 */
@Component
public class ReconciliationStatusSeedData extends BaseSeedData {


    public void insert(java.lang.String name, java.lang.Long id) {
        ReconciliationStatus reconciliationStatus = new ReconciliationStatus();
        reconciliationStatus.setName(name);
        reconciliationStatus.setId(id);
        save(reconciliationStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumReconciliationStatus enumReconciliationStatus : EnumReconciliationStatus.values()) {

            if (pkList.contains(enumReconciliationStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumReconciliationStatus.getId());
            else
                pkList.add(enumReconciliationStatus.getId());

            insert(enumReconciliationStatus.getName(), enumReconciliationStatus.getId());
        }
    }

}
