package com.hk.admin.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.stripes.action.LocalizableMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import com.akube.framework.util.BaseUtils;
import com.hk.admin.dto.DisplayTicketHistoryDto;
import com.hk.admin.dto.ticket.TicketDto;
import com.hk.admin.dto.ticket.TicketHistoryDto;
import com.hk.admin.pact.dao.ticket.TicketDao;
import com.hk.admin.pact.dao.ticket.TicketHistoryDao;
import com.hk.domain.Ticket;
import com.hk.domain.TicketHistory;
import com.hk.domain.TicketStatus;
import com.hk.domain.TicketType;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;

@Component
public class TicketManager {

    @Autowired
    UserService      userService;

    @Autowired
    TicketDao        ticketDao;

    @Autowired
    TicketHistoryDao ticketHistoryDao;

    public Ticket createTicket(TicketDto ticketDto) {
        Assert.assertNotNull(ticketDto.getOwner());
        Assert.assertNotNull(ticketDto.getReporter());
        Assert.assertNotNull(ticketDto.getShortDescription());
        Assert.assertNotNull(ticketDto.getTicketStatus());
        Assert.assertNotNull(ticketDto.getTicketType());

        if (StringUtils.isNotBlank(ticketDto.getAssociatedEmail())) {
            // UserVO userVO = UserCache.getInstance().getUserByLogin(ticketDto.getAssociatedEmail());
            User associatedUser = getUserService().findByLogin(ticketDto.getAssociatedEmail());
            if (associatedUser != null) {
                ticketDto.setAssociatedUser(associatedUser);
                ticketDto.setAssociatedEmail(null);
            }
            /*
             * if (userVO != null) { ticketDto.setAssociatedUser(userVO.getUser()); ticketDto.setAssociatedEmail(null); }
             */
        }

        if (ticketDto.getAssociatedUser() == null && ticketDto.getAssociatedOrder() != null) {
            ticketDto.setAssociatedUser(ticketDto.getAssociatedOrder().getUser());
        }

        Ticket ticket = new Ticket();
        ticket.setShortDescription(ticketDto.getShortDescription());
        ticket.setFullDescription(ticketDto.getFullDescription());
        ticket.setOwner(ticketDto.getOwner());
        ticket.setReporter(ticketDto.getReporter());
        ticket.setTicketType(ticketDto.getTicketType());
        ticket.setTicketStatus(ticketDto.getTicketStatus());
        ticket.setAssociatedUser(ticketDto.getAssociatedUser());
        ticket.setAssociatedOrder(ticketDto.getAssociatedOrder());
        ticket.setAssociatedEmail(ticketDto.getAssociatedEmail());
        ticket.setAssociatedPhone(ticketDto.getAssociatedPhone());
        ticket.setAssociatedTrackingId(ticketDto.getAssociatedTrackingId());
        ticket.setAssociatedCourier(ticketDto.getAssociatedCourier());
        return ticketDao.save(ticket);
    }

    public TicketHistory editTicket(TicketHistoryDto ticketHistoryDto) {
        Ticket ticket = ticketHistoryDto.getTicket();

        if (StringUtils.isNotBlank(ticketHistoryDto.getAssociatedEmail())) {
            //UserVO userVO = UserCache.getInstance().getUserByLogin(ticketHistoryDto.getAssociatedEmail());
             User associatedUser = getUserService().findByLogin(ticketHistoryDto.getAssociatedEmail());

            
              ticketHistoryDto.setAssociatedUser(associatedUser); if (associatedUser != null) {
              ticketHistoryDto.setAssociatedEmail(null); }
             

            /*
             * if (userVO != null) { ticketHistoryDto.setAssociatedUser(userVO.getUser());
             * ticketHistoryDto.setAssociatedEmail(userVO.getEmail()); }
             */
        } else {
            ticketHistoryDto.setAssociatedUser(null);
        }

        User ticketOwner = ticket.getOwner();
        TicketType ticketType = ticket.getTicketType();
        TicketStatus ticketStatus = ticket.getTicketStatus();
        String shortDescription = ticket.getShortDescription();
        String fullDescription = ticket.getFullDescription();
        Order associatedOrder = ticket.getAssociatedOrder();
        String associatedEmail = ticket.getAssociatedEmail();
        String associatedPhone = ticket.getAssociatedPhone();
        String associatedTackingId = ticket.getAssociatedTrackingId();
        User associatedUser = ticket.getAssociatedUser();
        Courier associatedCourier = ticket.getAssociatedCourier();

        if (ticketOwner.equals(ticketHistoryDto.getOwner()) && ticketType.equals(ticketHistoryDto.getTicketType()) && ticketStatus.equals(ticketHistoryDto.getTicketStatus())
                && StringUtils.isBlank(ticketHistoryDto.getComment()) && shortDescription.equals(ticketHistoryDto.getShortDescription())
                && StringUtils.equals(fullDescription, ticketHistoryDto.getFullDescription()) && BaseUtils.compareObjects(associatedOrder, ticketHistoryDto.getAssociatedOrder())
                && BaseUtils.compareObjects(associatedCourier, ticketHistoryDto.getAssociatedCourier())
                && BaseUtils.compareObjects(associatedUser, ticketHistoryDto.getAssociatedUser()) && StringUtils.equals(associatedEmail, ticketHistoryDto.getAssociatedEmail())
                && StringUtils.equals(associatedPhone, ticketHistoryDto.getAssociatedPhone())
                && StringUtils.equals(associatedTackingId, ticketHistoryDto.getAssociatedTrackingId())) {
            return null;
        }

        ticket.setOwner(ticketHistoryDto.getOwner());
        ticket.setTicketStatus(ticketHistoryDto.getTicketStatus());
        ticket.setTicketType(ticketHistoryDto.getTicketType());
        ticket.setShortDescription(ticketHistoryDto.getShortDescription());
        ticket.setFullDescription(ticketHistoryDto.getFullDescription());
        ticket.setAssociatedOrder(ticketHistoryDto.getAssociatedOrder());
        ticket.setAssociatedCourier(ticketHistoryDto.getAssociatedCourier());
        ticket.setAssociatedEmail(ticketHistoryDto.getAssociatedEmail());
        ticket.setAssociatedPhone(ticketHistoryDto.getAssociatedPhone());
        ticket.setAssociatedTrackingId(ticketHistoryDto.getAssociatedTrackingId());
        ticket.setAssociatedUser(ticketHistoryDto.getAssociatedUser());

        ticketHistoryDto.setOwner(ticketOwner);
        ticketHistoryDto.setTicketType(ticketType);
        ticketHistoryDto.setTicketStatus(ticketStatus);
        ticketHistoryDto.setTicket(getTicketDao().save(ticket));
        ticketHistoryDto.setShortDescription(shortDescription);
        ticketHistoryDto.setFullDescriptionChanged(!StringUtils.equals(fullDescription, ticketHistoryDto.getFullDescription()));
        ticketHistoryDto.setAssociatedOrder(associatedOrder);
        ticketHistoryDto.setAssociatedEmail(associatedEmail);
        ticketHistoryDto.setAssociatedUser(associatedUser);
        ticketHistoryDto.setAssociatedCourier(associatedCourier);
        ticketHistoryDto.setAssociatedPhone(associatedPhone);
        ticketHistoryDto.setAssociatedTrackingId(associatedTackingId);

        return getTicketHistoryDao().createTicketHistory(ticketHistoryDto);
    }

    public List<DisplayTicketHistoryDto> getDisplayTicketHistoryDtos(Ticket ticket) {
        List<DisplayTicketHistoryDto> displayTicketHistoryDtos = new ArrayList<DisplayTicketHistoryDto>();

        List<TicketHistory> ticketHistories = ticket.getTicketHistories();

        if (ticketHistories.size() == 0)
            return displayTicketHistoryDtos;

        if (ticketHistories.size() > 1) {
            for (int i = 0; i < ticketHistories.size() - 1; i++) {
                displayTicketHistoryDtos.add(compareHistoryAndReturnDto(ticketHistories.get(i), ticketHistories.get(i + 1)));
            }
        }

        displayTicketHistoryDtos.add(compareHistoryAndReturnDto(ticketHistories.get(ticketHistories.size() - 1), getDummyTicketHistoryFromTicket(ticket)));

        return displayTicketHistoryDtos;
    }

    public DisplayTicketHistoryDto getLatestChangeDispayHistoryDto(TicketHistory ticketHistory, Ticket ticket) {
        return compareHistoryAndReturnDto(ticketHistory, getDummyTicketHistoryFromTicket(ticket));
    }

    private TicketHistory getDummyTicketHistoryFromTicket(Ticket ticket) {
        TicketHistory ticketHistory = new TicketHistory();
        ticketHistory.setOwner(ticket.getOwner());
        ticketHistory.setTicketStatus(ticket.getTicketStatus());
        ticketHistory.setTicketType(ticket.getTicketType());
        ticketHistory.setShortDescription(ticket.getShortDescription());
        ticketHistory.setAssociatedOrder(ticket.getAssociatedOrder());
        ticketHistory.setAssociatedEmail(ticket.getAssociatedEmail());
        ticketHistory.setAssociatedUser(ticket.getAssociatedUser());
        ticketHistory.setAssociateCourier(ticket.getAssociatedCourier());
        ticketHistory.setAssociatedPhone(ticket.getAssociatedPhone());
        ticketHistory.setAssociatedTrackingId(ticket.getAssociatedTrackingId());

        return ticketHistory;
    }

    private DisplayTicketHistoryDto compareHistoryAndReturnDto(TicketHistory ticketHistory1, TicketHistory ticketHistory2) {
        DisplayTicketHistoryDto displayTicketHistoryDto = new DisplayTicketHistoryDto();
        displayTicketHistoryDto.setChangedBy(ticketHistory1.getChangedBy());
        displayTicketHistoryDto.setChangedDate(ticketHistory1.getCreateDate());
        displayTicketHistoryDto.setComment(ticketHistory1.getComment());
        if (!ticketHistory1.getTicketType().equals(ticketHistory2.getTicketType())) {
            String message = new LocalizableMessage("template.ticket.changedMessage", ticketHistory1.getTicketType().getName(), ticketHistory2.getTicketType().getName()).getMessage(Locale.getDefault());
            displayTicketHistoryDto.setTypeChangeMessage(message);
        }
        if (!ticketHistory1.getTicketStatus().equals(ticketHistory2.getTicketStatus())) {
            String message = new LocalizableMessage("template.ticket.changedMessage", ticketHistory1.getTicketStatus().getName(), ticketHistory2.getTicketStatus().getName()).getMessage(Locale.getDefault());
            displayTicketHistoryDto.setStatusChangeMessage(message);
        }
        if (!ticketHistory1.getOwner().equals(ticketHistory2.getOwner())) {
            String message = new LocalizableMessage("template.ticket.changedMessage", ticketHistory1.getOwner().getName(), ticketHistory2.getOwner().getName()).getMessage(Locale.getDefault());
            displayTicketHistoryDto.setOwnerChangeMessage(message);
        }
        if (!ticketHistory1.getShortDescription().equals(ticketHistory2.getShortDescription())) {
            String message = new LocalizableMessage("template.ticket.shortDescriptionChangeMessage", ticketHistory1.getShortDescription(), ticketHistory2.getShortDescription()).getMessage(Locale.getDefault());
            displayTicketHistoryDto.setShortDescriptionChangeMessage(message);
        }
        if (ticketHistory1.isFullDescriptionChanged()) {
            String message = new LocalizableMessage("template.ticket.fullDescriptionChangeMessage").getMessage(Locale.getDefault());
            displayTicketHistoryDto.setFullDescriptionChangeMessage(message);
        }
        if (!BaseUtils.compareObjects(ticketHistory1.getAssociatedOrder(), ticketHistory2.getAssociatedOrder())) {
            String message = null;
            if (ticketHistory1.getAssociatedOrder() == null) {
                message = new LocalizableMessage("template.ticket.addedMessage", ticketHistory2.getAssociatedOrder().getId()).getMessage(Locale.getDefault());
            } else if (ticketHistory2.getAssociatedOrder() == null) {
                message = new LocalizableMessage("template.ticket.removedMessage", ticketHistory1.getAssociatedOrder().getId()).getMessage(Locale.getDefault());
            } else {
                message = new LocalizableMessage("template.ticket.changedMessage", ticketHistory1.getAssociatedOrder().getId(), ticketHistory2.getAssociatedOrder().getId()).getMessage(Locale.getDefault());
            }
            displayTicketHistoryDto.setAssociatedOrderChangeMessage(message);
        }
        if (!BaseUtils.compareObjects(ticketHistory1.getAssociatedUser(), ticketHistory2.getAssociatedUser())) {
            String message = null;
            if (ticketHistory1.getAssociatedUser() == null) {
                message = new LocalizableMessage("template.ticket.addedMessage", ticketHistory2.getAssociatedUser().getEmail()).getMessage(Locale.getDefault());
            } else if (ticketHistory2.getAssociatedUser() == null) {
                message = new LocalizableMessage("template.ticket.removedMessage", ticketHistory1.getAssociatedUser().getEmail()).getMessage(Locale.getDefault());
            } else {
                message = new LocalizableMessage("template.ticket.changedMessage", ticketHistory1.getAssociatedUser().getEmail(), ticketHistory2.getAssociatedUser().getEmail()).getMessage(Locale.getDefault());
            }
            displayTicketHistoryDto.setAssociatedUserChangeMessage(message);
        }
        if (!StringUtils.equals(ticketHistory1.getAssociatedEmail(), ticketHistory2.getAssociatedEmail())) {
            String message = null;
            if (ticketHistory1.getAssociatedEmail() == null) {
                message = new LocalizableMessage("template.ticket.addedMessage", ticketHistory2.getAssociatedEmail()).getMessage(Locale.getDefault());
            } else if (ticketHistory2.getAssociatedEmail() == null) {
                message = new LocalizableMessage("template.ticket.removedMessage", ticketHistory1.getAssociatedEmail()).getMessage(Locale.getDefault());
            } else {
                message = new LocalizableMessage("template.ticket.changedMessage", ticketHistory1.getAssociatedEmail(), ticketHistory2.getAssociatedEmail()).getMessage(Locale.getDefault());
            }

            displayTicketHistoryDto.setAssociatedEmailChangeMessage(message);
        }
        if (!StringUtils.equals(ticketHistory1.getAssociatedPhone(), ticketHistory2.getAssociatedPhone())) {
            String message = null;
            if (ticketHistory1.getAssociatedPhone() == null) {
                message = new LocalizableMessage("template.ticket.addedMessage", ticketHistory2.getAssociatedPhone()).getMessage(Locale.getDefault());
            } else if (ticketHistory2.getAssociatedPhone() == null) {
                message = new LocalizableMessage("template.ticket.removedMessage", ticketHistory1.getAssociatedPhone()).getMessage(Locale.getDefault());
            } else {
                message = new LocalizableMessage("template.ticket.changedMessage", ticketHistory1.getAssociatedPhone(), ticketHistory2.getAssociatedPhone()).getMessage(Locale.getDefault());
            }
            displayTicketHistoryDto.setAssociatedPhoneChangeMessage(message);
        }
        if (!StringUtils.equals(ticketHistory1.getAssociatedTrackingId(), ticketHistory2.getAssociatedTrackingId())) {
            String message = null;
            if (ticketHistory1.getAssociatedTrackingId() == null) {
                message = new LocalizableMessage("template.ticket.addedMessage", ticketHistory2.getAssociatedTrackingId()).getMessage(Locale.getDefault());
            } else if (ticketHistory2.getAssociatedTrackingId() == null) {
                message = new LocalizableMessage("template.ticket.removedMessage", ticketHistory1.getAssociatedTrackingId()).getMessage(Locale.getDefault());
            } else {
                message = new LocalizableMessage("template.ticket.changedMessage", ticketHistory1.getAssociatedTrackingId(), ticketHistory2.getAssociatedTrackingId()).getMessage(Locale.getDefault());
            }
            displayTicketHistoryDto.setAssociatedTrackingIdChangeMessage(message);
        }
        if (!BaseUtils.compareObjects(ticketHistory1.getAssociateCourier(), ticketHistory2.getAssociateCourier())) {
            String message = null;
            if (ticketHistory1.getAssociateCourier() == null) {
                message = new LocalizableMessage("template.ticket.addedMessage", ticketHistory2.getAssociateCourier().getName()).getMessage(Locale.getDefault());
            } else if (ticketHistory2.getAssociateCourier() == null) {
                message = new LocalizableMessage("template.ticket.removedMessage", ticketHistory1.getAssociateCourier().getName()).getMessage(Locale.getDefault());
            } else {
                message = new LocalizableMessage("template.ticket.changedMessage", ticketHistory1.getAssociateCourier().getName(), ticketHistory2.getAssociateCourier().getName()).getMessage(Locale.getDefault());
            }
            displayTicketHistoryDto.setAssociatedCourierChangeMessage(message);
        }

        return displayTicketHistoryDto;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public TicketDao getTicketDao() {
        return ticketDao;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public TicketHistoryDao getTicketHistoryDao() {
        return ticketHistoryDao;
    }

    public void setTicketHistoryDao(TicketHistoryDao ticketHistoryDao) {
        this.ticketHistoryDao = ticketHistoryDao;
    }

}
