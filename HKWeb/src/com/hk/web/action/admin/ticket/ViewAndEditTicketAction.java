package com.hk.web.action.admin.ticket;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.Breadcrumb;
import com.hk.admin.dto.DisplayTicketHistoryDto;
import com.hk.admin.dto.ticket.TicketHistoryDto;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.manager.TicketManager;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.Ticket;
import com.hk.domain.TicketHistory;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * User: rahul Time: 9 Mar, 2010 6:07:36 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.CREATE_TICKETS }, authActionBean = AdminPermissionAction.class)
@Breadcrumb(level = 4, name = "Ticket #{ticket.id}", context = HealthkartConstants.BreadcrumbContext.admin)
@Component
public class ViewAndEditTicketAction extends BaseAction {

    @Autowired
    private TicketManager                 ticketManager;
    @Autowired
    private UserService                   userService;
    @Autowired
    private AdminEmailManager             adminEmailManager;

    @Validate(required = true, on = "pre")
    private Ticket                        ticket;

    private List<DisplayTicketHistoryDto> displayTicketHistoryDtos;

    @ValidateNestedProperties( {
            @Validate(field = "owner", required = true, on = "edit"),
            @Validate(field = "ticketStatus", required = true, on = "edit"),
            @Validate(field = "ticketType", required = true, on = "edit"),
            @Validate(field = "ticket", required = true, on = "edit"),
            @Validate(field = "shortDescription", required = true, on = "edit") })
    private TicketHistoryDto              ticketHistoryDto;

    @DefaultHandler
    public Resolution pre() {
        displayTicketHistoryDtos = ticketManager.getDisplayTicketHistoryDtos(ticket);
        return new ForwardResolution("/pages/admin/viewAndEditTicket.jsp");
    }

    public Resolution edit() {
        User changedBy = getUserService().getUserById(getPrincipal().getId());
        ticketHistoryDto.setChangedBy(changedBy);
        TicketHistory ticketHistory = ticketManager.editTicket(ticketHistoryDto);
        if (ticketHistory == null) {
            addRedirectAlertMessage(new LocalizableMessage("/ViewAndEditTicketaction.nothing.to.change"));
        } else {
            // DisplayTicketHistoryDto displayTicketHistoryDto =
            ticketManager.getLatestChangeDispayHistoryDto(ticketHistory, ticketHistory.getTicket());
            // getAdminEmailManager().sendEditTicketEmail(ticketHistory.getTicket(), displayTicketHistoryDto);
            addRedirectAlertMessage(new LocalizableMessage("/ViewAndEditTicketaction.edited"));
        }
        return new RedirectResolution(ViewAndEditTicketAction.class).addParameter("ticket", ticketHistoryDto.getTicket().getId());
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public TicketHistoryDto getTicketHistoryDto() {
        return ticketHistoryDto;
    }

    public void setTicketHistoryDto(TicketHistoryDto ticketHistoryDto) {
        this.ticketHistoryDto = ticketHistoryDto;
    }

    public List<DisplayTicketHistoryDto> getDisplayTicketHistoryDtos() {
        return displayTicketHistoryDtos;
    }

    public void setDisplayTicketHistoryDtos(List<DisplayTicketHistoryDto> displayTicketHistoryDtos) {
        this.displayTicketHistoryDtos = displayTicketHistoryDtos;
    }

    public TicketManager getTicketManager() {
        return ticketManager;
    }

    public void setTicketManager(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public AdminEmailManager getAdminEmailManager() {
        return adminEmailManager;
    }

    public void setAdminEmailManager(AdminEmailManager adminEmailManager) {
        this.adminEmailManager = adminEmailManager;
    }

}
