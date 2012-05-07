package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumRewardPointStatus;
import mhc.domain.RewardPointStatus;
import mhc.service.dao.RewardPointStatusDao;
import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class RewardPointStatusSeedData {

  @Inject
  RewardPointStatusDao rewardPointStatusDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    RewardPointStatus rewardPointStatus = new RewardPointStatus();
      rewardPointStatus.setName(name);
      rewardPointStatus.setId(id);
    rewardPointStatusDao.save(rewardPointStatus);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumRewardPointStatus enumRewardPointStatus : EnumRewardPointStatus.values()) {

      if (pkList.contains(enumRewardPointStatus.getId())) throw new RuntimeException("Duplicate key "+enumRewardPointStatus.getId());
      else pkList.add(enumRewardPointStatus.getId());

      insert(enumRewardPointStatus.getName(), enumRewardPointStatus.getId());
    }
  }

}
