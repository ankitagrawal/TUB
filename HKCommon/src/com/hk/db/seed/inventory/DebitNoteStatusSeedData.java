package com.hk.db.seed.inventory;

import java.util.ArrayList;
import java.util.List;

import com.hk.constants.inventory.EnumDebitNoteStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.accounting.DebitNoteStatus;

public class DebitNoteStatusSeedData extends BaseSeedData {

    public void insert(String name, Long id) {
        DebitNoteStatus debitNoteStatus = new DebitNoteStatus();
        debitNoteStatus.setName(name);
        debitNoteStatus.setId(id);
        save(debitNoteStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumDebitNoteStatus enumDebitNoteStatus : EnumDebitNoteStatus.values()) {

            if (pkList.contains(enumDebitNoteStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumDebitNoteStatus.getId());
            else
                pkList.add(enumDebitNoteStatus.getId());

            insert(enumDebitNoteStatus.getName(), enumDebitNoteStatus.getId());
        }
    }

}