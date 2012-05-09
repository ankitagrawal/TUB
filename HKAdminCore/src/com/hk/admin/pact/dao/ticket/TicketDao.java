package com.hk.admin.pact.dao.ticket;

import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.admin.dto.ticket.TicketFilterDto;
import com.hk.domain.Ticket;
import com.hk.pact.dao.BaseDao;

@Repository
public interface TicketDao extends BaseDao {

    public Ticket save(Ticket ticket);

    public Page search(TicketFilterDto ticketFilterDto, int page, int perPage);

}
