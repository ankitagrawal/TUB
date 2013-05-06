package com.hk.db.seed.inventory;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.hk.constants.inventory.EnumPILineItemType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.inventory.po.PiLineItemType;

@Component
public class PiLineItemTypeSeedData extends BaseSeedData {

	public void insert(java.lang.String name, java.lang.Long id) {
		PiLineItemType piLineItemType = new PiLineItemType();
		piLineItemType.setName(name);
		piLineItemType.setId(id);
		save(piLineItemType);
	}

	public void invokeInsert() {
		List<Long> pkList = new ArrayList<Long>();

		for (EnumPILineItemType enumPILineItemType : EnumPILineItemType.values()) {

			if (pkList.contains(enumPILineItemType.getId()))
				throw new RuntimeException("Duplicate key " + enumPILineItemType.getId());
			else
				pkList.add(enumPILineItemType.getId());

			insert(enumPILineItemType.getName(), enumPILineItemType.getId());
		}
	}
}
