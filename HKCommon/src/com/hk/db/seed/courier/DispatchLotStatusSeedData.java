package com.hk.db.seed.courier;

import com.hk.constants.courier.EnumDispatchLotStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.courier.DispatchLotStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/14/12
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DispatchLotStatusSeedData extends BaseSeedData {

	public void insert(java.lang.String name, java.lang.Long id) {
		DispatchLotStatus dispatchLotStatus = new DispatchLotStatus();
		dispatchLotStatus.setName(name);
		dispatchLotStatus.setId(id);
		save(dispatchLotStatus);
	}

	public void invokeInsert() {
		List<Long> pkList = new ArrayList<Long>();

		for (EnumDispatchLotStatus enumDispatchLotStatus : EnumDispatchLotStatus.values()) {

			if (pkList.contains(enumDispatchLotStatus.getId()))
				throw new RuntimeException("Duplicate key " + enumDispatchLotStatus.getId());
			else
				pkList.add(enumDispatchLotStatus.getId());

			insert(enumDispatchLotStatus.getName(), enumDispatchLotStatus.getId());
		}
	}

}
