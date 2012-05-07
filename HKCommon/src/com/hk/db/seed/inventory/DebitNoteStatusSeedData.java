package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumDebitNoteStatus;
import mhc.domain.DebitNoteStatus;
import mhc.service.dao.DebitNoteStatusDao;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class DebitNoteStatusSeedData {

  @Inject
  DebitNoteStatusDao debitNoteStatusDao;

  public void insert(String name, Long id) {
    DebitNoteStatus debitNoteStatus = new DebitNoteStatus();
    debitNoteStatus.setName(name);
    debitNoteStatus.setId(id);
    debitNoteStatusDao.save(debitNoteStatus);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumDebitNoteStatus enumDebitNoteStatus : EnumDebitNoteStatus.values()) {

      if (pkList.contains(enumDebitNoteStatus.getId())) throw new RuntimeException("Duplicate key " + enumDebitNoteStatus.getId());
      else pkList.add(enumDebitNoteStatus.getId());

      insert(enumDebitNoteStatus.getName(), enumDebitNoteStatus.getId());
    }
  }

}