package db.seed.master;

import mhc.domain.AffiliateTxnType;
import mhc.service.dao.AffiliateTxnTypeDao;
import mhc.common.constants.EnumAffiliateTxnType;
import com.google.inject.Inject;

public class AffiliateTxnTypeSeedData {
  @Inject
  AffiliateTxnTypeDao affiliateTxnTypeDao;
  public void insert(Long id,java.lang.String name) {
    AffiliateTxnType affiliateTxnType = new AffiliateTxnType();
    affiliateTxnType.setId(id);
    affiliateTxnType.setName(name);
    affiliateTxnTypeDao.save(affiliateTxnType);
  }

  public void invokeInsert() {
    for (EnumAffiliateTxnType enumAffiliateTxnType : EnumAffiliateTxnType.values()) {
      AffiliateTxnType affiliateTxnType = affiliateTxnTypeDao.find(enumAffiliateTxnType.getId());
      if (affiliateTxnType == null) {
        insert(enumAffiliateTxnType.getId(),enumAffiliateTxnType.getName());
      } else {
        affiliateTxnType.setName(enumAffiliateTxnType.getName());
        affiliateTxnTypeDao.save(affiliateTxnType);
      }
    }
  }
}
