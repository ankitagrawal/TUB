package com.hk.db.seed.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.inventory.EnumDebitNoteStatus;
import com.hk.constants.inventory.EnumCreditNoteStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.inventory.creditNote.CreditNoteStatus;

@Component
public class CreditNoteStatusSeedData extends BaseSeedData {

    public void insert(String name, Long id) {
        CreditNoteStatus creditNoteStatus = new CreditNoteStatus();
        creditNoteStatus.setName(name);
        creditNoteStatus.setId(id);
        save(creditNoteStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumCreditNoteStatus enumCreditNoteStatus : EnumCreditNoteStatus.values()) {

            if (pkList.contains(enumCreditNoteStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumCreditNoteStatus.getId());
            else
                pkList.add(enumCreditNoteStatus.getId());

            insert(enumCreditNoteStatus.getName(), enumCreditNoteStatus.getId());
        }
    }

}