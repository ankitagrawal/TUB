package com.hk.db.seed.reward;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.offer.rewardPoint.RewardPointStatus;

@Component
public class RewardPointStatusSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        RewardPointStatus rewardPointStatus = new RewardPointStatus();
        rewardPointStatus.setName(name);
        rewardPointStatus.setId(id);
        save(rewardPointStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumRewardPointStatus enumRewardPointStatus : EnumRewardPointStatus.values()) {

            if (pkList.contains(enumRewardPointStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumRewardPointStatus.getId());
            else
                pkList.add(enumRewardPointStatus.getId());

            insert(enumRewardPointStatus.getName(), enumRewardPointStatus.getId());
        }
    }

}
