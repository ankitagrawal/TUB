package com.hk.impl.service.queue;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.Classification;
import com.hk.domain.queue.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
 * User: Pratham
 * Date: 10/04/13  Time: 15:54
*/
public interface BucketService {

    public List<Param> getParamsForBucket(List<Bucket> bucketList);

    public Map<String, Object> getParamMap(List<Bucket> bucketList);

    ActionItem allocateBuckets(ShippingOrder shippingOrder);

    ActionItem existsActionItem(ShippingOrder shippingOrder);

    ActionItem pushToActionQueue(ActionItem actionItem);

    ActionItem popFromActionQueue(ActionItem actionItem);

    ActionItem changeBucket(ActionItem actionItem, List<Bucket> bucketList);

    ActionItem changeBucket(ShippingOrder shippingOrder, List<Bucket> buckets);

    ActionItem changeBucket(ShippingOrder shippingOrder, Bucket bucket);

    List<Bucket> findBucket(List<String> name, Classification classification);

    ActionItem saveActionItem(ActionItem actionItem);

    ActionItem escalateOrderFromActionQueue(ShippingOrder shippingOrder);
}
