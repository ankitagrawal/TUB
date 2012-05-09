package com.hk.web.action.admin.ticket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.stripes.controller.Breadcrumb;
import com.hk.admin.dto.ticket.TicketFilterDto;
import com.hk.admin.pact.dao.ticket.TicketDao;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.Ticket;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * User: rahul Time: 10 Mar, 2010 2:33:56 PM
 */
@Secure(hasAnyPermissions = { PermissionConstants.VIEW_TICKETS }, authActionBean = AdminPermissionAction.class)
@Breadcrumb(level = 2, name = "Search Tickets", context = HealthkartConstants.BreadcrumbContext.admin)
@Component
public class SearchTicketAction extends BasePaginatedAction {
    @Autowired
    TicketDao               ticketDao;

    private TicketFilterDto ticketFilterDto;

    private List<Ticket>    ticketList = new ArrayList<Ticket>(0);

    private Page            ticketPage;

    public Set<String> getParamSet() {
        Set<String> params = new HashSet<String>();
        params.add("ticketFilterDto.ticketId");
        params.add("ticketFilterDto.keywords");
        params.add("ticketFilterDto.owner");
        params.add("ticketFilterDto.reporter");
        params.add("ticketFilterDto.ticketType");
        params.add("ticketFilterDto.ticketStatus");
        params.add("ticketFilterDto.createDateFrom");
        params.add("ticketFilterDto.createDateTo");
        params.add("ticketFilterDto.associatedUserId");
        params.add("ticketFilterDto.associatedOrderId");
        params.add("ticketFilterDto.associatedLogin");
        params.add("ticketFilterDto.associatedPhone");
        return params;
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/searchTicket.jsp");
    }

    @ValidationMethod
    public void validate() {
        if (ticketFilterDto == null)
            ticketFilterDto = new TicketFilterDto();
    }

    public Resolution search() {
        ticketPage = ticketDao.search(ticketFilterDto, getPageNo(), getPerPage());
        ticketList = ticketPage.getList();
        return new ForwardResolution("/pages/admin/searchTicket.jsp");
    }

    public TicketFilterDto getTicketFilterDto() {
        return ticketFilterDto;
    }

    public void setTicketFilterDto(TicketFilterDto ticketFilterDto) {
        this.ticketFilterDto = ticketFilterDto;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public int getPerPageDefault() {
        return 50;
    }

    public int getPageCount() {
        return ticketPage == null ? 0 : ticketPage.getTotalPages();
    }

    public int getResultCount() {
        return ticketPage == null ? 0 : ticketPage.getTotalResults();
    }

}
