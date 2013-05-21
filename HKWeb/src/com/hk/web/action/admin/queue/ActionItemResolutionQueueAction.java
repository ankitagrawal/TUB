package com.hk.web.action.admin.queue;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.core.search.ActionItemSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.TrafficState;
import com.hk.domain.user.User;
import com.hk.impl.service.queue.BucketService;
import com.hk.pact.service.UserService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/*
 * User: Pratham
 * Date: 16/05/13  Time: 15:06
*/
@Component
public class ActionItemResolutionQueueAction extends BasePaginatedAction {
    Page actionItemsPage;
    List<ActionItem> actionItems;
    private String shippingOrderId;
    private String baseOrderId;
    private List<ShippingOrder> shippingOrders;
    private List<TrafficState> trafficStates;
    private List<ActionTask> currentActionTasks;
    private Boolean flagged;
    private List<ActionTask> previousActionTasks;
    private List<Bucket> buckets;
    private Date startPushDate;
    private Date endPushDate;
    private List<User> reporters;
    private Integer defaultPerPage = 4;

    @Autowired
    BucketService bucketService;
    @Autowired
    UserService userService;

    public Resolution pre() {
        actionItemsPage = bucketService.searchActionItems(getActionItemSearchCriteria(), getPageNo(), getPerPage());
        if (actionItemsPage != null) actionItems = actionItemsPage.getList();
        return new ForwardResolution("/pages/admin/queue/actionItemResolutionQueue.jsp");
    }

    private ActionItemSearchCriteria getActionItemSearchCriteria() {
        User loggedOnUser = userService.getLoggedInUser();
        ActionItemSearchCriteria actionItemSearchCriteria = new ActionItemSearchCriteria();
        actionItemSearchCriteria.setReporters(Arrays.asList(userService.getAdminUser()));
        actionItemSearchCriteria.setBuckets(loggedOnUser.getBuckets());
        actionItemSearchCriteria.setTrafficStates(trafficStates);
        return actionItemSearchCriteria;
    }

    public List<ActionItem> getActionItems() {
        return actionItems;
    }

    public void setActionItems(List<ActionItem> actionItems) {
        this.actionItems = actionItems;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return actionItemsPage == null ? 0 : actionItemsPage.getTotalPages();
    }

    public int getResultCount() {
        return actionItemsPage == null ? 0 : actionItemsPage.getTotalResults();
    }

    public Integer getDefaultPerPage() {
        return defaultPerPage;
    }

    public void setDefaultPerPage(Integer defaultPerPage) {
        this.defaultPerPage = defaultPerPage;
    }

    @Override
    public Set<String> getParamSet() {
        return null;
    }

    public String getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(String shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    public String getBaseOrderId() {
        return baseOrderId;
    }

    public void setBaseOrderId(String baseOrderId) {
        this.baseOrderId = baseOrderId;
    }

    public List<ShippingOrder> getShippingOrders() {
        return shippingOrders;
    }

    public void setShippingOrders(List<ShippingOrder> shippingOrders) {
        this.shippingOrders = shippingOrders;
    }

    public List<TrafficState> getTrafficStates() {
        return trafficStates;
    }

    public void setTrafficStates(List<TrafficState> trafficStates) {
        this.trafficStates = trafficStates;
    }

    public List<ActionTask> getCurrentActionTasks() {
        return currentActionTasks;
    }

    public void setCurrentActionTasks(List<ActionTask> currentActionTasks) {
        this.currentActionTasks = currentActionTasks;
    }

    public Boolean getFlagged() {
        return flagged;
    }

    public void setFlagged(Boolean flagged) {
        this.flagged = flagged;
    }

    public List<ActionTask> getPreviousActionTasks() {
        return previousActionTasks;
    }

    public void setPreviousActionTasks(List<ActionTask> previousActionTasks) {
        this.previousActionTasks = previousActionTasks;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public Date getStartPushDate() {
        return startPushDate;
    }

    public void setStartPushDate(Date startPushDate) {
        this.startPushDate = startPushDate;
    }

    public Date getEndPushDate() {
        return endPushDate;
    }

    public void setEndPushDate(Date endPushDate) {
        this.endPushDate = endPushDate;
    }

    public List<User> getReporters() {
        return reporters;
    }

    public void setReporters(List<User> reporters) {
        this.reporters = reporters;
    }
}
