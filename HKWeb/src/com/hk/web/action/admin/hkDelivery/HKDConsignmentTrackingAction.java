package com.hk.web.action.admin.hkDelivery;


import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.domain.hkDelivery.*;
import com.hk.domain.user.User;
import com.hk.util.CustomDateTypeConvertor;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HKDConsignmentTrackingAction extends BasePaginatedAction {

    private static Logger logger = LoggerFactory.getLogger(HKDConsignmentAction.class);
    private Long hubId;
    private Long sourceHubId;
    private Long destinationHubId;
    private Page consignmentTrackingPage;
    private Date startDate;
    private User loggedOnUser;
    private Long consignmentLifecycleStatus;
    private List<ConsignmentTracking> consignmentList = new ArrayList<ConsignmentTracking>();
    private Integer defaultPerPage = 10;

    @Autowired
    private ConsignmentService consignmentService;

    @DefaultHandler
    public Resolution pre() {
        loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        return new ForwardResolution("/pages/admin/hkConsignmentTracking.jsp");
    }

    @SuppressWarnings("unchecked")
    public Resolution searchConsignmentTracking() {
        sourceHubId = getHubId();
        destinationHubId = getHubId();
        consignmentTrackingPage = consignmentService.searchConsignmentTracking(startDate, consignmentLifecycleStatus, sourceHubId, destinationHubId, getPageNo(), getPerPage());
        if (consignmentTrackingPage != null) {
            consignmentList = consignmentTrackingPage.getList();
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
        params.add("sourceHubId");
        params.add("destinationHubId");
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

    public Long getSourceHubId() {
        return sourceHubId;
    }

    public void setSourceHubId(Long sourceHubId) {
        this.sourceHubId = sourceHubId;
    }

    public Long getDestinationHubId() {
        return destinationHubId;
    }

    public void setDestinationHubId(Long destinationHubId) {
        this.destinationHubId = destinationHubId;
    }

}
