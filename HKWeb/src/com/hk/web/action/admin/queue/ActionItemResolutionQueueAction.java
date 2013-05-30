package com.hk.web.action.admin.queue;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.queue.EnumActionTask;
import com.hk.core.search.ActionItemSearchCriteria;
import com.hk.domain.analytics.Reason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.TrafficState;
import com.hk.domain.user.User;
import com.hk.impl.service.queue.BucketService;
import com.hk.pact.service.UserService;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

/*
 * User: Pratham
 * Date: 16/05/13  Time: 15:06
*/
@Component
public class ActionItemResolutionQueueAction extends BasePaginatedAction {
    Page actionItemsPage;
    List<ActionItem> actionItems;
    private Long shippingOrderId;
    private String baseOrderId;
    private List<ShippingOrder> shippingOrders;
    private List<TrafficState> trafficStates;
    private List<ActionTask> currentActionTasks;
    private Boolean flagged;
    private List<ActionTask> previousActionTasks;
    private List<Bucket> buckets;
    private List<Reason> reasons;
    private Date startPushDate;
    private Date endPushDate;
    private List<User> reporters;
    private Integer defaultPerPage = 40;
    Map<String, Object> bucketParameters = new HashMap<String, Object>();
    private ActionItem actionItem;
    Long actionTaskId;
    ActionTask currentActionTask;
    Long priorityId;
    List<Bucket> userBuckets = new ArrayList<Bucket>();

    @Autowired
    BucketService bucketService;
    @Autowired
    UserService userService;

    @DontValidate
    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_ACTION_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        User user = getPrincipalUser();

        if (user != null) {
            buckets = user.getBuckets();
            if (buckets != null && !buckets.isEmpty()) {
                bucketParameters = bucketService.getParamMap(user.getBuckets());
            }
        }
        return new ForwardResolution(ActionItemResolutionQueueAction.class, "search");
//                .addParameters(bucketParameters);
    }

        @Secure(hasAnyPermissions = {PermissionConstants.VIEW_ACTION_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution search() {
             User user = getPrincipalUser();
            userBuckets = user.getBuckets();
        actionItemsPage = bucketService.searchActionItems(getActionItemSearchCriteria(), getPageNo(), getPerPage());
        if (actionItemsPage != null) actionItems = actionItemsPage.getList();
        return new ForwardResolution("/pages/admin/queue/actionItemResolutionQueue.jsp");
    }


    private ActionItemSearchCriteria getActionItemSearchCriteria() {
        User loggedOnUser = userService.getLoggedInUser();
        ActionItemSearchCriteria actionItemSearchCriteria = new ActionItemSearchCriteria();
        actionItemSearchCriteria.setShippingOrderId(shippingOrderId).setFlagged(flagged);
        actionItemSearchCriteria.setReporters(reporters);
        buckets = buckets != null ? buckets : loggedOnUser.getBuckets();
        actionItemSearchCriteria.setBuckets(buckets);
        actionItemSearchCriteria.setTrafficStates(trafficStates);
        actionItemSearchCriteria.setStartPushDate(startPushDate).setEndPushDate(endPushDate);
        actionItemSearchCriteria.setCurrentActionTasks(currentActionTasks).setPreviousActionTasks(previousActionTasks);
        return actionItemSearchCriteria;
    }

       @JsonHandler
    public Resolution updateTask() {

        Map datamap = new HashMap();
        actionItem.setPreviousActionTask(actionItem.getCurrentActionTask());
        actionItem.setCurrentActionTask(currentActionTask);
        getBaseDao().save(actionItem);
        datamap.put("name",actionItem.getId());
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Task has been updated", datamap);
        noCache();
        return new JsonResolution(healthkartResponse);
    }


    public Resolution setPriority() {
        actionItem.setPriority(priorityId);
        getBaseDao().save(actionItem);
        addRedirectAlertMessage(new SimpleMessage("Action Item priority has been changed"));
        return new RedirectResolution(ActionItemResolutionQueueAction.class);
    }


    @JsonHandler
    public Resolution saveBuckets() {
        HashMap datamap = new HashMap();

        List<Bucket> actionItemBuckets = new ArrayList<Bucket>();
        List<Bucket> currentActionItemBuckets = actionItem.getBuckets();
        if (currentActionItemBuckets == null || currentActionItemBuckets.size() < 1) {
            addRedirectAlertMessage(new SimpleMessage("There should be one bucket asscociated with action Item"));
            return new RedirectResolution(ActionItemResolutionQueueAction.class);
        }
        User user = getUserService().getUserById(getPrincipal().getId());
        List<Bucket> userBuckets = user.getBuckets();
        // other user Buckets
        currentActionItemBuckets.removeAll(userBuckets);
        if (currentActionItemBuckets.size() > 0) {
            actionItemBuckets.addAll(currentActionItemBuckets);
        }

        for (Bucket bucket : buckets) {
            if (bucket.getId() != null) {
                if (bucket.isSelected()) {
//      Now add only new Buckets
                    if (!currentActionItemBuckets.contains(bucket)) {
                        actionItemBuckets.add(bucketService.getBucketById(bucket.getId()));
                    }

                }
            }
        }

        actionItem.setBuckets(actionItemBuckets);
        getBaseDao().save(actionItem);
        datamap.put("name",actionItemBuckets);
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Added", datamap);
        noCache();
        return new JsonResolution(healthkartResponse);

//            return new RedirectResolution(ActionItemCRUD.class).addParameter("actionItem", actionItem.getId());
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

    public List<Reason> getReasons() {
        return reasons;
    }

    public void setReasons(List<Reason> reasons) {
        this.reasons = reasons;
    }

    @Override
    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("startPushDate");
        params.add("endPushDate");
        params.add("shippingOrderId");
        params.add("baseOrderId");
        params.add("bucketParameters");
        params.add("flagged");

        int ctr = 0;
        if (trafficStates != null) {
            for (TrafficState trafficState : trafficStates) {
                if (trafficState != null) {
                    params.add("trafficStates[" + ctr + "]");
                }
                ctr++;
            }
        }
        int ctr2 = 0;
        if (currentActionTasks != null) {
            for (ActionTask currentActionTask : currentActionTasks) {
                if (currentActionTask != null) {
                    params.add("currentActionTasks[" + ctr2 + "]");
                }
                ctr2++;
            }
        }
        int ctr3 = 0;
        if (previousActionTasks != null) {
            for (ActionTask previousActionTask : previousActionTasks) {
                if (previousActionTask != null) {
                    params.add("previousActionTasks[" + ctr3 + "]");
                }
                ctr3++;
            }
        }
        int ctr4 = 0;
        if (buckets != null) {
            for (Bucket bucket : buckets) {
                if (bucket != null) {
                    params.add("buckets[" + ctr4 + "]");
                }
                ctr4++;
            }
        }
        int ctr5 = 0;
        if (reasons != null) {
            for (Reason reason : reasons) {
                if (reason != null) {
                    params.add("reasons[" + ctr5 + "]");
                }
                ctr5++;
            }
        }
        return params;
    }

    public Long getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(Long shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    public Map<String, Object> getBucketParameters() {
        return bucketParameters;
    }

    public void setBucketParameters(Map<String, Object> bucketParameters) {
        this.bucketParameters = bucketParameters;
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

    public ActionItem getActionItem() {
        return actionItem;
    }

    public void setActionItem(ActionItem actionItem) {
        this.actionItem = actionItem;
    }

    public Long getActionTaskId() {
        return actionTaskId;
    }

    public void setActionTaskId(Long actionTaskId) {
        this.actionTaskId = actionTaskId;
    }

    public Long getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Long priorityId) {
        this.priorityId = priorityId;
    }

    public ActionTask getCurrentActionTask() {
        return currentActionTask;
    }

    public void setCurrentActionTask(ActionTask currentActionTask) {
        this.currentActionTask = currentActionTask;
    }

     public List<Bucket> getUserBuckets() {
        return userBuckets;
    }

    public void setUserBuckets(List<Bucket> userBuckets) {
        this.userBuckets = userBuckets;
    }
}
