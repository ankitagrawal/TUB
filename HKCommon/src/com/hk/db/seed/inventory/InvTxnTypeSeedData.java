package com.hk.db.seed.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.core.InvTxnType;

@Component
public class InvTxnTypeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        InvTxnType invTxnType = new InvTxnType();
        invTxnType.setName(name);
        invTxnType.setId(id);
        save(invTxnType);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumInvTxnType enumInvTxnType : EnumInvTxnType.values()) {

            if (pkList.contains(enumInvTxnType.getId()))
                throw new RuntimeException("Duplicate key " + enumInvTxnType.getId());
            else
                pkList.add(enumInvTxnType.getId());

            insert(enumInvTxnType.getName(), enumInvTxnType.getId());
        }
    }

}
