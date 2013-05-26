package com.hk.web.action.admin.queue.action;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.queue.ActionItem;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

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

    public Resolution pre(){
        return new ForwardResolution("/pages/admin/queue/actionItemCRUD.jsp");
    }


}
