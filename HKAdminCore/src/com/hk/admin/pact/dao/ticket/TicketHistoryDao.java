package com.hk.admin.pact.dao.ticket;

import org.springframework.stereotype.Repository;

import com.hk.admin.dto.ticket.TicketHistoryDto;
import com.hk.domain.TicketHistory;
import com.hk.pact.dao.BaseDao;

@Repository
public interface TicketHistoryDao extends BaseDao {

    public TicketHistory save(TicketHistory ticketHistory);

    public TicketHistory createTicketHistory(TicketHistoryDto ticketHistoryDto);
}
