package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumBoxSize;
import mhc.domain.BoxSize;
import mhc.service.dao.BoxSizeDao;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class BoxSizeSeedData {

  @Inject BoxSizeDao boxSizeDao;

  public void insert(String name, Long id) {
    BoxSize boxSize = new BoxSize();
    boxSize.setName(name);
    boxSize.setId(id);
    boxSizeDao.save(boxSize);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumBoxSize enumBoxSize : EnumBoxSize.values()) {

      if (pkList.contains(enumBoxSize.getId())) throw new RuntimeException("Duplicate key " + enumBoxSize.getId());
      else pkList.add(enumBoxSize.getId());

      insert(enumBoxSize.getName(), enumBoxSize.getId());
    }
  }

}