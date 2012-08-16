package com.hk.db.seed.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.inventory.EnumGrnStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.inventory.GrnStatus;

@Component
public class GrnStatusSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        GrnStatus grnStatus = new GrnStatus();
        grnStatus.setName(name);
        grnStatus.setId(id);
        save(grnStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumGrnStatus enumGrnStatus : EnumGrnStatus.values()) {

            if (pkList.contains(enumGrnStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumGrnStatus.getId());
            else
                pkList.add(enumGrnStatus.getId());

            insert(enumGrnStatus.getName(), enumGrnStatus.getId());
        }
    }

}
