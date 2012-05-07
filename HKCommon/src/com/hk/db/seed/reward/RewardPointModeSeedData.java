package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumRewardPointTxnType;
import mhc.common.constants.EnumRewardPointMode;
import mhc.domain.RewardPointTxnType;
import mhc.domain.RewardPointMode;
import mhc.service.dao.RewardPointTxnTypeDao;
import mhc.service.dao.RewardPointModeDao;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class RewardPointModeSeedData {

  @Inject RewardPointModeDao rewardPointModeDao;

  public void insert(String name, Long id) {
    RewardPointMode rewardPointMode = new RewardPointMode();
    rewardPointMode.setName(name);
    rewardPointMode.setId(id);
    rewardPointModeDao.save(rewardPointMode);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumRewardPointMode enumRewardPointMode : EnumRewardPointMode.values()) {

      if (pkList.contains(enumRewardPointMode.getId()))
        throw new RuntimeException("Duplicate key " + enumRewardPointMode.getId());
      else pkList.add(enumRewardPointMode.getId());

      insert(enumRewardPointMode.getName(), enumRewardPointMode.getId());
    }
  }

}