package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumGrnStatus;
import mhc.domain.GrnStatus;
import mhc.service.dao.GrnStatusDao;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class GrnStatusSeedData {

  @Inject
  GrnStatusDao grnStatusDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    GrnStatus grnStatus = new GrnStatus();
    grnStatus.setName(name);
    grnStatus.setId(id);
    grnStatusDao.save(grnStatus);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumGrnStatus enumGrnStatus : EnumGrnStatus.values()) {

      if (pkList.contains(enumGrnStatus.getId())) throw new RuntimeException("Duplicate key " + enumGrnStatus.getId());
      else pkList.add(enumGrnStatus.getId());

      insert(enumGrnStatus.getName(), enumGrnStatus.getId());
    }
  }

}
