package db.seed.master;

import com.google.inject.Inject;

import java.util.List;
import java.util.ArrayList;

import mhc.service.dao.TicketTypeDao;
import mhc.domain.TicketType;
import mhc.common.constants.EnumTicketType;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class TicketTypeSeedData {

  @Inject
  TicketTypeDao ticketTypeDao;

  public void insert(java.lang.String name, java.lang.Long id, java.lang.String description) {
    TicketType ticketType = new TicketType();
    ticketType.setName(name);
    ticketType.setId(id);
    ticketType.setDescription(description);
    ticketTypeDao.save(ticketType);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumTicketType enumTicketType : EnumTicketType.values()) {

      if (pkList.contains(enumTicketType.getId()))
        throw new RuntimeException("Duplicate key " + enumTicketType.getId());
      else pkList.add(enumTicketType.getId());

      insert(enumTicketType.getName(), enumTicketType.getId(), enumTicketType.getDescription());
    }
  }

}
