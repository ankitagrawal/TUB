package com.hk.db.seed.marketing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.marketing.EnumAdNetworks;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.marketing.AdNetworks;

@Component
public class AdNetworksSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        AdNetworks adNetworks = new AdNetworks();
        adNetworks.setName(name);
        adNetworks.setId(id);
        save(adNetworks);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();
        for (EnumAdNetworks enumAdNetworks : EnumAdNetworks.values()) {
            if (pkList.contains(enumAdNetworks.getId()))
                throw new RuntimeException("Duplicate key " + enumAdNetworks.getId());
            else
                pkList.add(enumAdNetworks.getId());
            insert(enumAdNetworks.getName(), enumAdNetworks.getId());
        }
    }

}
