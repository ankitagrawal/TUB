package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumCancellationType;
import mhc.domain.CancellationType;
import mhc.service.dao.CancellationTypeDao;
import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class CancellationTypeSeedData {

  @Inject
  CancellationTypeDao cancellationTypeDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    CancellationType cancellationType = new CancellationType();
      cancellationType.setName(name);
      cancellationType.setId(id);
    cancellationTypeDao.save(cancellationType);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumCancellationType enumCancellationType : EnumCancellationType.values()) {

      if (pkList.contains(enumCancellationType.getId())) throw new RuntimeException("Duplicate key "+enumCancellationType.getId());
      else pkList.add(enumCancellationType.getId());

      insert(enumCancellationType.getName(), enumCancellationType.getId());
    }
  }

}
