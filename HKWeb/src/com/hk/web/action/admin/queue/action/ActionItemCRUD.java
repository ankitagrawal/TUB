package com.hk.web.action.admin.queue.action;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.ActionTask;
import com.hk.impl.service.queue.BucketService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.RedirectResolution;

import java.util.List;
import java.util.ArrayList;

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
    ActionTask actionTask;

    @Autowired
    private BucketService bucketService;

    public Resolution pre() {
        return new ForwardResolution("/pages/admin/queue/actionItemCRUD.jsp");
    }


    public Resolution view() {
        buckets = getBaseDao().getAll(Bucket.class);
        if (actionItem != null) {
            List<Bucket> bucketList = actionItem.getBuckets();
            for (Bucket bucket : bucketList) {
                if (buckets.contains(bucket)) {
                    bucket.setSelected(true);
                }
            }
        }
        return new ForwardResolution("/pages/admin/queue/actionItemCRUD.jsp");
    }


    public Resolution save() {
        List<Bucket> actionItemBuckets = new ArrayList<Bucket>();
        for (Bucket bucket : buckets) {
            if (bucket.getId() != null) {
                if (bucket.isSelected()) {
                    actionItemBuckets.add(bucket);
                }
            }
        }
        actionItem.setBuckets(actionItemBuckets);
        getBaseDao().save(actionItem);
       

        return null;
    }



    public Resolution updateTask(){
//      actionItem =  bucketService.autoUpdateActionItem(actionItem, false);
        actionItem.setPreviousActionTask(actionItem.getCurrentActionTask());
        actionItem.setCurrentActionTask(actionTask);
        getBaseDao().save(actionItem);
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

    public ActionTask getActionTask() {
        return actionTask;
    }

    public void setActionTask(ActionTask actionTask) {
        this.actionTask = actionTask;
    }
}
