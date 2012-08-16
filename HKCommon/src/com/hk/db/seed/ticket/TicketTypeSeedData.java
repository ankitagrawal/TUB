package com.hk.db.seed.ticket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.ticket.EnumTicketType;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.TicketType;

@Component
public class TicketTypeSeedData extends BaseSeedData{


  public void insert(java.lang.String name, java.lang.Long id, java.lang.String description) {
    TicketType ticketType = new TicketType();
    ticketType.setName(name);
    ticketType.setId(id);
    ticketType.setDescription(description);
    save(ticketType);
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
