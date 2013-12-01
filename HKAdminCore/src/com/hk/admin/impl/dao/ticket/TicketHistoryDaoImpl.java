package com.hk.admin.impl.dao.ticket;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.admin.dto.ticket.TicketHistoryDto;
import com.hk.admin.pact.dao.ticket.TicketHistoryDao;
import com.hk.domain.TicketHistory;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class TicketHistoryDaoImpl extends BaseDaoImpl implements TicketHistoryDao {

    @Transactional
    public TicketHistory save(TicketHistory ticketHistory) {
        if (ticketHistory != null) {
            if (ticketHistory.getCreateDate() == null) {
                ticketHistory.setCreateDate(BaseUtils.getCurrentTimestamp());
            }
        }
        return (TicketHistory) super.save(ticketHistory);
    }

    public TicketHistory createTicketHistory(TicketHistoryDto ticketHistoryDto) {
        TicketHistory ticketHistory = new TicketHistory();
        ticketHistory.setTicket(ticketHistoryDto.getTicket());
        ticketHistory.setChangedBy(ticketHistoryDto.getChangedBy());
        ticketHistory.setComment(ticketHistoryDto.getComment());
        ticketHistory.setOwner(ticketHistoryDto.getOwner());
        ticketHistory.setTicketStatus(ticketHistoryDto.getTicketStatus());
        ticketHistory.setTicketType(ticketHistoryDto.getTicketType());
        ticketHistory.setShortDescription(ticketHistoryDto.getShortDescription());
        ticketHistory.setFullDescriptionChanged(ticketHistoryDto.isFullDescriptionChanged());
        ticketHistory.setAssociatedOrder(ticketHistoryDto.getAssociatedOrder());
        ticketHistory.setAssociatedEmail(ticketHistoryDto.getAssociatedEmail());
        ticketHistory.setAssociatedUser(ticketHistoryDto.getAssociatedUser());
        ticketHistory.setAssociateCourier(ticketHistoryDto.getAssociatedCourier());
        ticketHistory.setAssociatedPhone(ticketHistoryDto.getAssociatedPhone());
        ticketHistory.setAssociatedTrackingId(ticketHistoryDto.getAssociatedTrackingId());

        return save(ticketHistory);
    }
}
