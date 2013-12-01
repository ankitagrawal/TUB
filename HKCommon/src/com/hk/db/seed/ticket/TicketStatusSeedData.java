package com.hk.db.seed.ticket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.ticket.EnumTicketStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.TicketStatus;

@Component
public class TicketStatusSeedData extends BaseSeedData {

    public void insert(java.lang.String name, java.lang.Long id) {
        TicketStatus ticketStatus = new TicketStatus();
        ticketStatus.setName(name);
        ticketStatus.setId(id);
        save(ticketStatus);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumTicketStatus enumTicketStatus : EnumTicketStatus.values()) {

            if (pkList.contains(enumTicketStatus.getId()))
                throw new RuntimeException("Duplicate key " + enumTicketStatus.getId());
            else
                pkList.add(enumTicketStatus.getId());

            insert(enumTicketStatus.getName(), enumTicketStatus.getId());
        }
    }

}
