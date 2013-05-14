package com.hk.db.seed.inventory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.rtv.EnumExtraInventoryLineItemType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItemType;

/**
 * 
 * @author Nihal
 *
 */
@Component
public class ExtraInventoryLineItemTypeSeedData extends BaseSeedData{
	
	public void insert(java.lang.String name, java.lang.Long id){
		ExtraInventoryLineItemType extraInventoryLineItemType = new ExtraInventoryLineItemType();
		extraInventoryLineItemType.setId(id);
		extraInventoryLineItemType.setName(name);
	}
	
	public void invokeInsert() {
        List<Long> eiliTypeList = new ArrayList<Long>();

        for (EnumExtraInventoryLineItemType enumExtraInventoryLineItemType : EnumExtraInventoryLineItemType.values()) {

            if (eiliTypeList.contains(enumExtraInventoryLineItemType.getId()))
                throw new RuntimeException("Duplicate key " + enumExtraInventoryLineItemType.getId());
            else
            	eiliTypeList.add(enumExtraInventoryLineItemType.getId());

            insert(enumExtraInventoryLineItemType.getName(), enumExtraInventoryLineItemType.getId());
        }
    }

}
