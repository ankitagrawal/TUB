package com.hk.web.action.admin.hkDelivery;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.constants.core.EnumPermission;
import com.hk.domain.hkDelivery.*;
import com.hk.domain.user.User;
import com.hk.util.CustomDateTypeConvertor;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HKDConsignmentTrackingAction extends BasePaginatedAction {

    private Long hubId;
    private Page consignmentTrackingPage;
    private Date startDate;
    private Date endDate;
    private User loggedOnUser;
    private Long consignmentLifecycleStatus;
    private List<ConsignmentTracking> consignmentList = new ArrayList<ConsignmentTracking>();
    private Integer defaultPerPage = 30;

    @Autowired
    private ConsignmentService consignmentService;
    @Autowired
    private HubService hubService;

    @DefaultHandler
    public Resolution pre() {
        loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        return new ForwardResolution("/pages/admin/hkConsignmentTracking.jsp");
    }

    @SuppressWarnings("unchecked")
    public Resolution searchConsignmentTracking() {
        loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        if (getStartDate() != null && getEndDate() != null  && getStartDate().compareTo(getEndDate()) > 0) {
            addRedirectAlertMessage(new SimpleMessage("Invalid dates!"));
        } else {
            if (!loggedOnUser.hasPermission(EnumPermission.SELECT_HUB)) {
                hubId = getHubService().getHubForUser(loggedOnUser).getId();
            }
            consignmentTrackingPage = getConsignmentService().searchConsignmentTracking(startDate, endDate, consignmentLifecycleStatus, hubId, getPageNo(), getPerPage());
            if (consignmentTrackingPage != null) {
                consignmentList = consignmentTrackingPage.getList();
            }
        }
        return new ForwardResolution("/pages/admin/hkConsignmentTracking.jsp");
    }

    public Long getHubId() {
        return hubId;
    }

    public void setHubId(Long hubId) {
        this.hubId = hubId;
    }

    public int getPerPageDefault() {
        return defaultPerPage; // To change body of implemented methods use File | Settings | File Templates.
    }

    public int getPageCount() {
        return consignmentTrackingPage == null ? 0 : consignmentTrackingPage.getTotalPages();
    }

    public int getResultCount() {
        return consignmentTrackingPage == null ? 0 : consignmentTrackingPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("hubId");
        params.add("endDate");
        params.add("startDate");
        params.add("consignmentLifecycleStatus");
        return params;
    }

    public Date getStartDate() {
        return startDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public User getLoggedOnUser() {
        return loggedOnUser;
    }

    public void setLoggedOnUser(User loggedOnUser) {
        this.loggedOnUser = loggedOnUser;
    }

    public void setConsignmentLifecycleStatus(Long consignmentLifecycleStatus) {
        this.consignmentLifecycleStatus = consignmentLifecycleStatus;
    }

    public Long getConsignmentLifecycleStatus() {
        return consignmentLifecycleStatus;
    }

    public List<ConsignmentTracking> getConsignmentList() {
        return consignmentList;
    }

    public void setConsignmentList(List<ConsignmentTracking> consignmentList) {
        this.consignmentList = consignmentList;
    }

    public ConsignmentService getConsignmentService() {
        return consignmentService;
    }

    public HubService getHubService() {
        return hubService;
    }

}
