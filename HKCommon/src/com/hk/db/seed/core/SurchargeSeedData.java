package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumSurcharge;
import mhc.domain.Surcharge;
import mhc.service.dao.SurchargeDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class SurchargeSeedData {

	@Inject
	SurchargeDao surchargeDao;

	public void insert(Long id, String name, Double value) {
		Surcharge surcharge = new Surcharge();
		surcharge.setName(name);
		surcharge.setId(id);
		surcharge.setValue(value);
		surchargeDao.save(surcharge);
	}

	public void invokeInsert() {
		List<Long> pkList = new ArrayList<Long>();

		for (EnumSurcharge enumSurcharge : EnumSurcharge.values()) {

			if (pkList.contains(enumSurcharge.getId())) throw new RuntimeException("Duplicate key " + enumSurcharge.getId());
			else pkList.add(enumSurcharge.getId());

			insert(enumSurcharge.getId(), enumSurcharge.getName(), enumSurcharge.getValue());
		}
	}

}
