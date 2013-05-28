package com.hk.web.action.admin.queue.action;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.util.DateUtils;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.user.User;
import com.hk.impl.service.queue.BucketService;
import com.hk.constants.queue.EnumActionTask;
import com.hk.constants.queue.EnumTrafficState;
import com.hk.web.action.admin.queue.ActionItemResolutionQueueAction;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

/*
 * User: Pratham
 * Date: 08/05/13  Time: 20:23
*/
public class ActionItemCRUD extends BaseAction {

    /*
        so what's the intent of writing this class?

        mainly to perform all possible operations category managers would want to do with a shipping order (which is now a actionItem)

        to summarize

         1. a) view the list of current buckets  (READ)  b) update the list of buckets (remove some, add some)

         2. change the current action task, hence an auto update to the previous task

         3. club action tasks in one group (bring all task associated to cancellation on one level, JIT processing on one level)

         and show relevant task(s) for an actionItem

         3b preferably intelligence to deduce what will be the nextActionTask

         4. flag an actionItem, acknowledge an actionTask (similar to seen), so out of a pool of actionItems, on each actionTask level,

         different users, can go and pick one by one and acknowledge their responsibility

         5. inbuilt capability to change traffic state with each passing day (1 day old, 2 days old, 3 days old  etc)

         7. prioritize an order, on a scale of 1-10

     */

    ActionItem actionItem;
    List<Bucket> buckets = new ArrayList<Bucket>();
    List<ActionTask> actionTasks = new ArrayList<ActionTask>();
    List<ActionItem> actionItems = new ArrayList<ActionItem>();
    Long actionTaskId;


    @Autowired
    private BucketService bucketService;

    @DefaultHandler
    @JsonHandler
    public Resolution view() {
        Map dataMap = new HashMap();
        
        buckets = getBaseDao().getAll(Bucket.class);
        if (actionItem != null) {
            List<Bucket> bucketList = actionItem.getBuckets();
            List <Bucket> checkedBucket = new ArrayList<Bucket>();
            for (Bucket bucket : buckets) {
                if (bucketList.contains(bucket)) {
                    bucket.setSelected(true);
                    checkedBucket.add(bucket);
                }
            }
            dataMap.put("name", checkedBucket);         }

          HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Added", dataMap);
        noCache();
        return new JsonResolution(healthkartResponse);
    }


    public Resolution save() {
        List<Bucket> actionItemBuckets = new ArrayList<Bucket>();
        User user = getUserService().getUserById(getPrincipal().getId());
        List<Bucket> userBuckets = user.getBuckets();

        for (Bucket bucket : buckets) {
            if (bucket.getId() != null) {
                if (bucket.isSelected()) {
                    if (userBuckets.contains(bucket)) {
                        actionItemBuckets.add(bucketService.getBucketById(bucket.getId()));
                    }
                }
            }
        }
        actionItem.setBuckets(actionItemBuckets);
        getBaseDao().save(actionItem);
        addRedirectAlertMessage(new SimpleMessage("Action Item Updated Successfully"));
        return new RedirectResolution(ActionItemCRUD.class).addParameter("actionItem", actionItem.getId());
    }


    public Resolution saveBuckets() {
        List<Bucket> actionItemBuckets = new ArrayList<Bucket>();
        List<Bucket> currentActionItemBuckets = actionItem.getBuckets();
        if (currentActionItemBuckets == null || currentActionItemBuckets.size() < 1) {
            addRedirectAlertMessage(new SimpleMessage("There should be one bucket asscociated with action Item"));
            return new RedirectResolution(ActionItemCRUD.class).addParameter("actionItem", actionItem.getId());
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
        addRedirectAlertMessage(new SimpleMessage("Action Item Updated Successfully"));
//        return new RedirectResolution(ActionItemResolutionQueueAction.class);
         return new RedirectResolution(ActionItemCRUD.class).addParameter("actionItem", actionItem.getId());
    }


    public Resolution updateTask() {
//      actionItem =  bucketService.autoUpdateActionItem(actionItem, false);
        actionItem.setPreviousActionTask(actionItem.getCurrentActionTask());
        ActionTask actionTask = EnumActionTask.getActionTaskById(actionTaskId);
        actionItem.setCurrentActionTask(actionTask);
        getBaseDao().save(actionItem);
        addRedirectAlertMessage(new SimpleMessage("Action Task  Updated Successfully"));
//        return new RedirectResolution(ActionItemCRUD.class).addParameter("actionItem", actionItem.getId());
        return new RedirectResolution("ActionItemResolutionQueue.class");
    }


    public Resolution flagActionItems() {
        if (actionItem == null) {
            addRedirectAlertMessage(new SimpleMessage("Please select the action Item"));
            return new RedirectResolution(ActionItemCRUD.class).addParameter("actionItem", actionItem.getId());

        }
        actionItem.setFlagged(true);
        getBaseDao().save(actionItem);
        addRedirectAlertMessage(new SimpleMessage("Action Item marked flagged"));
        return new RedirectResolution(ActionItemCRUD.class).addParameter("actionItem", actionItem.getId());

    }


    public Resolution acknowledgeActionTask() {
        ActionTask actionTask = actionItem.getCurrentActionTask();
//        actionTask.setAcknowledged(true);
//        actionTask.setAcknowledgedBy(getUserService().getUserById(getPrincipal().getId()));
        getBaseDao().save(actionTask);
        addRedirectAlertMessage(new SimpleMessage("Current action Task has been acknowledged"));
        return new RedirectResolution(ActionItemCRUD.class).addParameter("actionItem", actionItem.getId());

    }


    public Resolution changeTrafficState() {
        Date firstPushDate = actionItem.getFirstPushDate();
        Date currDate = new Date();
        Long noOfDaysLapse = Math.round((currDate.getTime() - firstPushDate.getTime()) / 86400000D);
        int priority = 1;
        if (noOfDaysLapse.intValue() < 2) {
            priority = 1;
        } else {
            priority = 2;
        }
        switch (priority) {
            case 1:
                actionItem.setTrafficState(EnumTrafficState.NORMAL.asTrafficState());
                break;
            case 2:

                actionItem.setTrafficState(EnumTrafficState.RED.asTrafficState());
                break;
        }
        getBaseDao().save(actionItem);
        addRedirectAlertMessage(new SimpleMessage("Traffic State has been changed"));
        return new RedirectResolution(ActionItemCRUD.class).addParameter("actionItem", actionItem.getId());

    }


    public Resolution changeBulkTrafficState() {
        List<ActionItem> actionItems = bucketService.getActionItemsOfActionQueue();
        Date currDate = new Date();
        for (ActionItem actionItem : actionItems) {
            Date target_dispatch_date = actionItem.getShippingOrder().getTargetDispatchDate();
            Date previous_day_of_dispatch_date = DateUtils.getStartOfPreviousDay(target_dispatch_date);
            Long noOfDaysLapse = Math.round((previous_day_of_dispatch_date.getTime() - currDate.getTime()) / 86400000D);
            int priority;
            if (noOfDaysLapse > 2) {
                priority = 1;
            } else if (noOfDaysLapse <= 2 && noOfDaysLapse > 0) {
                priority = 2;
            } else {
                priority = 3;
            }

            switch (priority) {
                case 1:
                    actionItem.setTrafficState(EnumTrafficState.NORMAL.asTrafficState());
                    break;
                case 2:
                    actionItem.setTrafficState(EnumTrafficState.YELLOW.asTrafficState());
                    break;
                case 3:
                    actionItem.setTrafficState(EnumTrafficState.RED.asTrafficState());
                    break;
            }
            getBaseDao().save(actionItem);

        }
        addRedirectAlertMessage(new SimpleMessage("Traffic State has been changed for all pending Action Items"));
//        return new RedirectResolution(ActionItemCRUD.class).addParameter("actionItem", actionItem.getId());
          return new RedirectResolution(ActionItemResolutionQueueAction.class);

    }


    public Resolution prioritizeActionItems() {

        return null;
    }


    public ActionItem getActionItem() {
        return actionItem;
    }

    public void setActionItem(ActionItem actionItem) {
        this.actionItem = actionItem;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public List<ActionTask> getActionTasks() {
        return actionTasks;
    }

    public void setActionTasks(List<ActionTask> actionTasks) {
        this.actionTasks = actionTasks;
    }

    public Long getActionTaskId() {
        return actionTaskId;
    }

    public void setActionTaskId(Long actionTaskId) {
        this.actionTaskId = actionTaskId;
    }

    public List<ActionItem> getActionItems() {
        return actionItems;
    }

    public void setActionItems(List<ActionItem> actionItems) {
        this.actionItems = actionItems;
    }
}
