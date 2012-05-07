package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumReconciliationType;
import mhc.domain.ReconciliationType;
import mhc.service.dao.ReconciliationTypeDao;
import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class ReconciliationTypeSeedData {

  @Inject
  ReconciliationTypeDao reconciliationTypeDao;

  public void insert(java.lang.String name,java.lang.Long id) {
    ReconciliationType reconciliationType = new ReconciliationType();
      reconciliationType.setName(name);
      reconciliationType.setId(id);
    reconciliationTypeDao.save(reconciliationType);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumReconciliationType enumReconciliationType : EnumReconciliationType.values()) {

      if (pkList.contains(enumReconciliationType.getId())) throw new RuntimeException("Duplicate key "+enumReconciliationType.getId());
      else pkList.add(enumReconciliationType.getId());

      insert(enumReconciliationType.getName(),enumReconciliationType.getId());
    }
  }

}
