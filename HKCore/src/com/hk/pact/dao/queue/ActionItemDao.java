package com.hk.pact.dao.queue;

import com.akube.framework.dao.Page;
import com.hk.constants.queue.EnumBucket;
import com.hk.core.search.ActionItemSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.TrafficState;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;

/*
 * User: Pratham
 * Date: 15/04/13  Time: 20:12
*/
public interface ActionItemDao extends BaseDao
{

    ActionItem save(ActionItem actionItem);

    ActionItem searchActionItem(ShippingOrder shippingOrder);

//    List<ActionItem> searchActionItem(ShippingOrder shippingOrder, List<Bucket> buckets, Date startPushDate, Date startPopDate, List<TrafficState> trafficStates, User watcher, Boolean flagged, User reporter);

    List<Bucket> getBuckets(List<EnumBucket> enumBuckets);

    List<Bucket> findByName(List<String> bucketNames);

    Page searchActionItems(ActionItemSearchCriteria actionItemSearchCriteria, int pageNo, int perPage);

    List<ActionTask> listNextActionTasks(ActionTask currentActionTask, List<Bucket> buckets);

    public List<ActionItem> getActionItemsOfActionQueue ();
}
