package com.hk.web.action.admin.queue;

import com.hk.domain.queue.ActionItem;
import com.hk.impl.service.queue.BucketService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * User: Pratham
 * Date: 16/05/13  Time: 15:06
*/
@Component
public class ActionItemResolutionQueueAction {

    List<ActionItem> actionItems;

    @Autowired
    BucketService bucketService;

    public Resolution pre() {
        actionItems = bucketService.createActionQueue();

        return new ForwardResolution("/pages/admin/queue/actionItemResolutionQueue.jsp");
    }

    public List<ActionItem> getActionItems() {
        return actionItems;
    }

    public void setActionItems(List<ActionItem> actionItems) {
        this.actionItems = actionItems;
    }
}
