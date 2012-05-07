package db.seed.master;

import com.google.inject.Inject;
import java.util.List;
import java.util.ArrayList;

import mhc.service.dao.TicketStatusDao;
import mhc.domain.TicketStatus;
import mhc.common.constants.EnumTicketStatus;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class TicketStatusSeedData {

  @Inject
  TicketStatusDao ticketStatusDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    TicketStatus ticketStatus = new TicketStatus();
      ticketStatus.setName(name);
      ticketStatus.setId(id);
    ticketStatusDao.save(ticketStatus);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumTicketStatus enumTicketStatus : EnumTicketStatus.values()) {

      if (pkList.contains(enumTicketStatus.getId())) throw new RuntimeException("Duplicate key "+enumTicketStatus.getId());
      else pkList.add(enumTicketStatus.getId());

      insert(enumTicketStatus.getName(), enumTicketStatus.getId());
    }
  }

}
