package com.hk.db.seed.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.inventory.EnumDebitNoteType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.accounting.DebitNoteType;

@Component
public class DebitNoteTypeSeedData extends BaseSeedData{
	
	public void insert(java.lang.String name, java.lang.Long id){
		DebitNoteType debitNoteType = new DebitNoteType();
		debitNoteType.setId(id);
		debitNoteType.setName(name);
		getBaseDao().save(debitNoteType);
	}
	
	public void invokeInsert() {
        List<Long> eiliTypeList = new ArrayList<Long>();

        for (EnumDebitNoteType enumDebitNoteType : EnumDebitNoteType.values()) {

            if (eiliTypeList.contains(enumDebitNoteType.getId()))
                throw new RuntimeException("Duplicate key " + enumDebitNoteType.getId());
            else
            	eiliTypeList.add(enumDebitNoteType.getId());

            insert(enumDebitNoteType.getName(), enumDebitNoteType.getId());
        }
    }

}
