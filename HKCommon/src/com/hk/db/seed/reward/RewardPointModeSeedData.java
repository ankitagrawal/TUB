package com.hk.db.seed.reward;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.offer.rewardPoint.RewardPointMode;

@Component
public class RewardPointModeSeedData extends BaseSeedData {


    public void insert(String name, Long id) {
        RewardPointMode rewardPointMode = new RewardPointMode();
        rewardPointMode.setName(name);
        rewardPointMode.setId(id);
        save(rewardPointMode);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumRewardPointMode enumRewardPointMode : EnumRewardPointMode.values()) {

            if (pkList.contains(enumRewardPointMode.getId()))
                throw new RuntimeException("Duplicate key " + enumRewardPointMode.getId());
            else
                pkList.add(enumRewardPointMode.getId());

            insert(enumRewardPointMode.getName(), enumRewardPointMode.getId());
        }
    }

}