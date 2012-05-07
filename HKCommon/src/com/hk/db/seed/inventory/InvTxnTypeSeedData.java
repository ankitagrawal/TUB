package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumInvTxnType;
import mhc.domain.InvTxnType;
import mhc.service.dao.InvTxnTypeDao;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class InvTxnTypeSeedData {

  @Inject
  InvTxnTypeDao invTxnTypeDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    InvTxnType invTxnType = new InvTxnType();
    invTxnType.setName(name);
    invTxnType.setId(id);
    invTxnTypeDao.save(invTxnType);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumInvTxnType enumInvTxnType : EnumInvTxnType.values()) {

      if (pkList.contains(enumInvTxnType.getId()))
        throw new RuntimeException("Duplicate key " + enumInvTxnType.getId());
      else pkList.add(enumInvTxnType.getId());

      insert(enumInvTxnType.getName(), enumInvTxnType.getId());
    }
  }

}
