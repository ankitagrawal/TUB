package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumRewardPointTxnType;
import mhc.domain.RewardPointTxnType;
import mhc.service.dao.RewardPointTxnTypeDao;
import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class RewardPointTxnTypeSeedData {

  @Inject
  RewardPointTxnTypeDao rewardPointTxnTypeDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    RewardPointTxnType rewardPointTxnType = new RewardPointTxnType();
      rewardPointTxnType.setName(name);
      rewardPointTxnType.setId(id);
    rewardPointTxnTypeDao.save(rewardPointTxnType);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumRewardPointTxnType enumRewardPointTxnType : EnumRewardPointTxnType.values()) {

      if (pkList.contains(enumRewardPointTxnType.getId())) throw new RuntimeException("Duplicate key "+enumRewardPointTxnType.getId());
      else pkList.add(enumRewardPointTxnType.getId());

      insert(enumRewardPointTxnType.getName(), enumRewardPointTxnType.getId());
    }
  }

}
