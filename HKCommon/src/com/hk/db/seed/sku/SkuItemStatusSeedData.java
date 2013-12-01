package com.hk.db.seed.sku;

import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.sku.SkuItemStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/27/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SkuItemStatusSeedData extends BaseSeedData {

	public void insert(String name, Long id){
		SkuItemStatus skuItemStatus = new SkuItemStatus();
		skuItemStatus.setId(id);
		skuItemStatus.setName(name);
		save(skuItemStatus);
	}

	public void invokeInsert() {
		List<Long> pkList = new ArrayList<Long>();

		for(EnumSkuItemStatus enumSkuItemStatus : EnumSkuItemStatus.values()) {
			if(pkList.contains(enumSkuItemStatus.getId())) {
				throw new RuntimeException("Duplicate key " + enumSkuItemStatus.getId());
			} else {
				pkList.add(enumSkuItemStatus.getId());
			}

			insert(enumSkuItemStatus.getName(), enumSkuItemStatus.getId());
		}
	}
}
