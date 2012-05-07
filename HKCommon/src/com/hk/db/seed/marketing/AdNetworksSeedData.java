package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumAdNetworks;
import mhc.domain.AdNetworks;
import mhc.service.dao.AdNetworksDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class AdNetworksSeedData {

	@Inject
	AdNetworksDao adNetworksDataDao;

	public void insert(java.lang.String name, java.lang.Long id) {
		AdNetworks adNetworks = new AdNetworks();
		adNetworks.setName(name);
		adNetworks.setId(id);
		adNetworksDataDao.save(adNetworks);
	}

	public void invokeInsert() {
		List<Long> pkList = new ArrayList<Long>();
		for (EnumAdNetworks enumAdNetworks : EnumAdNetworks.values()) {
			if (pkList.contains(enumAdNetworks.getId()))
				throw new RuntimeException("Duplicate key " + enumAdNetworks.getId());
			else pkList.add(enumAdNetworks.getId());
			insert(enumAdNetworks.getName(), enumAdNetworks.getId());
		}
	}

}
