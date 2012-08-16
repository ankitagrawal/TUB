package com.hk.db.seed.reward;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.discount.EnumRewardPointTxnType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.offer.rewardPoint.RewardPointTxnType;

@Component
public class RewardPointTxnTypeSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        RewardPointTxnType rewardPointTxnType = new RewardPointTxnType();
        rewardPointTxnType.setName(name);
        rewardPointTxnType.setId(id);
        save(rewardPointTxnType);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumRewardPointTxnType enumRewardPointTxnType : EnumRewardPointTxnType.values()) {

            if (pkList.contains(enumRewardPointTxnType.getId()))
                throw new RuntimeException("Duplicate key " + enumRewardPointTxnType.getId());
            else
                pkList.add(enumRewardPointTxnType.getId());

            insert(enumRewardPointTxnType.getName(), enumRewardPointTxnType.getId());
        }
    }

}
