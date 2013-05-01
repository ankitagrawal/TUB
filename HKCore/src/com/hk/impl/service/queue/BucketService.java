package com.hk.impl.service.queue;

import com.hk.constants.queue.EnumActionTask;
import com.hk.constants.queue.EnumBucket;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.*;
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

    ActionItem pushToActionQueue(ActionItem actionItem, boolean isAuto);

    ActionItem popFromActionQueue(ActionItem actionItem);

    ActionItem changeBucket(ActionItem actionItem, List<Bucket> bucketList);

    ActionItem changeBucket(ShippingOrder shippingOrder, List<Bucket> buckets);

    ActionItem changeBucket(ShippingOrder shippingOrder, Bucket bucket);

    List<Bucket> findBucket(List<String> name, Classification classification);

    ActionItem saveActionItem(ActionItem actionItem);

    public Bucket find(EnumBucket enumBucket);

    public ActionTask find(EnumActionTask enumActionTask);

    public List<Bucket> getBuckets(List<EnumBucket> enumBuckets);


    ActionItem escalateOrderFromActionQueue(ShippingOrder shippingOrder);
}
