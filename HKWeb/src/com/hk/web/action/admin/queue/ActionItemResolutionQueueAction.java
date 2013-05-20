package com.hk.web.action.admin.queue;

import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.domain.queue.ActionItem;
import com.hk.impl.service.queue.BucketService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/*
 * User: Pratham
 * Date: 16/05/13  Time: 15:06
*/
@Component
public class ActionItemResolutionQueueAction extends BasePaginatedAction{

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

    @Override
    public int getPerPageDefault() {
        return 0;
    }

    @Override
    public int getPageCount() {
        return 0;
    }

    @Override
    public int getResultCount() {
        return 0;
    }

    @Override
    public Set<String> getParamSet() {
        return null;
    }
}
