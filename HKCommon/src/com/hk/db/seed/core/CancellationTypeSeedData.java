package com.hk.db.seed.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.core.EnumCancellationType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.CancellationType;

@Component
public class CancellationTypeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        CancellationType cancellationType = new CancellationType();
        cancellationType.setName(name);
        cancellationType.setId(id);
        save(cancellationType);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumCancellationType enumCancellationType : EnumCancellationType.values()) {

            if (pkList.contains(enumCancellationType.getId()))
                throw new RuntimeException("Duplicate key " + enumCancellationType.getId());
            else
                pkList.add(enumCancellationType.getId());

            insert(enumCancellationType.getName(), enumCancellationType.getId());
        }
    }

}
