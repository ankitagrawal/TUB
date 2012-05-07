package db.seed.master;

import com.google.inject.Inject;
import mhc.common.constants.EnumReconciliationStatus;
import mhc.common.constants.EnumTicketStatus;
import mhc.domain.ReconciliationStatus;
import mhc.domain.TicketStatus;
import mhc.service.dao.ReconciliationStatusDao;
import mhc.service.dao.TicketStatusDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: Jul 5, 2011
 * Time: 2:00:18 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings({"InjectOfNonPublicMember"})
public class ReconciliationStatusSeedData {

  @Inject
  ReconciliationStatusDao reconciliationStatusDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    ReconciliationStatus reconciliationStatus = new ReconciliationStatus();
      reconciliationStatus.setName(name);
      reconciliationStatus.setId(id);
    reconciliationStatusDao.save(reconciliationStatus);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumReconciliationStatus enumReconciliationStatus : EnumReconciliationStatus.values()) {

      if (pkList.contains(enumReconciliationStatus.getId())) throw new RuntimeException("Duplicate key "+enumReconciliationStatus.getId());
      else pkList.add(enumReconciliationStatus.getId());

      insert(enumReconciliationStatus.getName(), enumReconciliationStatus.getId());
    }
  }

}
