package com.hk.db.seed.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.inventory.EnumPurchaseOrderType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.inventory.po.PurchaseOrderType;

@Component
public class PurchaseOrderTypeSeedData extends BaseSeedData{
	
	public void insert(java.lang.String name, java.lang.Long id){
		PurchaseOrderType purchaseOrderType = new PurchaseOrderType();
		purchaseOrderType.setId(id);
		purchaseOrderType.setName(name);
		getBaseDao().save(purchaseOrderType);
	}
	
	public void invokeInsert() {
        List<Long> poTypeList = new ArrayList<Long>();

        for (EnumPurchaseOrderType enumPurchaseOrderType : EnumPurchaseOrderType.values()) {

            if (poTypeList.contains(enumPurchaseOrderType.getId()))
                throw new RuntimeException("Duplicate key " + enumPurchaseOrderType.getId());
            else
            	poTypeList.add(enumPurchaseOrderType.getId());

            insert(enumPurchaseOrderType.getName(), enumPurchaseOrderType.getId());
        }
    }

}
