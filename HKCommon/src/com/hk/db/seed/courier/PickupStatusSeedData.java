package com.hk.db.seed.courier;

import org.springframework.stereotype.Component;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.courier.PickupStatus;
import com.hk.constants.courier.EnumPickupStatus;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Dec 6, 2012
 * Time: 11:45:52 AM
 * To change this template use File | Settings | File Templates.
 */

@Component
public class PickupStatusSeedData extends BaseSeedData {

	public void insert(java.lang.String status, java.lang.Long id) {
		PickupStatus pickupStatus = new PickupStatus();
		pickupStatus.setName(status);
		pickupStatus.setId(id);
		save(pickupStatus);
	}

	public void invokeInsert() {
		List<Long> pickupList = new ArrayList<Long>();

		for (EnumPickupStatus enumPickupStatus : EnumPickupStatus.values()) {

			if (pickupList.contains(enumPickupStatus.getId()))
				throw new RuntimeException("Duplicate key " + enumPickupStatus.getId());
			else
				pickupList.add(enumPickupStatus.getId());

			insert(enumPickupStatus.getName(), enumPickupStatus.getId());
		}
	}
}


