package com.hk.pact.dao.queue;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.TrafficState;
import com.hk.domain.user.User;

import java.util.Date;
import java.util.List;

/*
 * User: Pratham
 * Date: 15/04/13  Time: 20:12
*/
public interface ActionItemDao {

    ActionItem save(ActionItem actionItem);

    List<ActionItem> searchActionItem(ShippingOrder shippingOrder, List<Bucket> buckets, Date startPushDate, Date startPopDate, List<TrafficState> trafficStates, User watcher, Boolean flagged, User reporter);
}
