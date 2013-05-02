package com.hk.admin.impl.service.queue;

import com.hk.constants.queue.EnumActionTask;
import com.hk.constants.queue.EnumBucket;
import com.hk.constants.queue.EnumTrafficState;
import com.hk.domain.analytics.Reason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.Param;
import com.hk.domain.user.User;
import com.hk.impl.service.queue.BucketService;
import com.hk.pact.dao.queue.ActionItemDao;
import com.hk.pact.service.UserService;
import com.hk.util.BucketAllocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * User: Pratham
 * Date: 10/04/13  Time: 16:04
*/
@Service
public class BucketServiceImpl implements BucketService {

    @Autowired
    UserService userService;

    @Autowired
    ActionItemDao actionItemDao;

    @Override
    public List<Param> getParamsForBucket(List<Bucket> bucketList) {
        List<Param> params = new ArrayList();
        for (Bucket bucket : bucketList) {
            params.addAll(bucket.getParams());
        }
        return params;
    }

    @Override
    public Map<String, Object> getParamMap(List<Bucket> bucketList) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        List<Param> params = getParamsForBucket(bucketList);
        for (Param param : params) {
            parameters.put(param.getParamName(), param.getParamValues());
        }
        return parameters;
    }

    @Override
    public ActionItem saveActionItem(ActionItem actionItem) {
        return actionItemDao.save(actionItem);
    }

    @Override
    public Bucket find(EnumBucket enumBucket) {
        return actionItemDao.get(Bucket.class, enumBucket.getId());
    }

    @Override
    public ActionTask find(EnumActionTask enumActionTask) {
        return actionItemDao.get(ActionTask.class, enumActionTask.getId());
    }

    @Override
    public List<Bucket> getBuckets(List<EnumBucket> enumBuckets) {
        return actionItemDao.getBuckets(enumBuckets);
    }

    @Override
    public ActionItem escalateOrderFromActionQueue(ShippingOrder shippingOrder) {
        ActionItem actionItem = actionItemDao.searchActionItem(shippingOrder);
        if(actionItem == null) return null;
        Bucket actionableBucket = shippingOrder.isDropShipping() ? find(EnumBucket.Vendor) : shippingOrder.isServiceOrder() ? find(EnumBucket.ServiceOrder) : find(EnumBucket.Warehouse);
        actionItem = createUpdateActionItem(shippingOrder, Arrays.asList(actionableBucket), false);
        actionItem.setPopDate(new Date());
        actionItem.setFlagged(false);
        actionItem.setTrafficState(EnumTrafficState.NORMAL.asTrafficState());
        return saveActionItem(actionItem);
    }

    @Override
    public ActionItem escalateBackToActionQueue(ShippingOrder shippingOrder) {
        Reason reason = shippingOrder.getReason();
        List<Bucket> actionableBuckets = reason != null ? reason.getBuckets() : Arrays.asList(find(EnumBucket.AD_HOC));
        if (actionableBuckets.contains(find(EnumBucket.CM))){
            actionableBuckets.addAll(EnumBucket.getBuckets(BucketAllocator.getBucketsFromSOC(shippingOrder)));
        }
        ActionItem actionItem = createUpdateActionItem(shippingOrder, actionableBuckets, false);
        actionItem.setTrafficState(EnumTrafficState.RED.asTrafficState());
        actionItem.setFlagged(true);
        return saveActionItem(actionItem);
    }

    @Override
    public void popFromActionQueue(ShippingOrder shippingOrder) {
        ActionItem actionItem = existsActionItem(shippingOrder);
        if (actionItem != null) {
            //release all dependencies
            actionItem.setBuckets(null);
            actionItem.setWatchers(null);
            actionItem = saveActionItem(actionItem);
            actionItemDao.delete(actionItem);
        }
    }

    @Override
    public ActionItem createUpdateActionItem(ShippingOrder shippingOrder, List<Bucket> buckets, boolean isAuto) {
        ActionItem actionItem = getOrCreateActionItem(shippingOrder, buckets);
        User reporter = isAuto ? userService.getAdminUser() : userService.getLoggedInUser();
        actionItem.setReporter(reporter);
        return actionItem;
    }

    @Override
    public ActionItem autoAllocateBuckets(ShippingOrder shippingOrder) {
        ActionItem actionItem = createUpdateActionItem(shippingOrder, null, true);
        return saveActionItem(actionItem);
    }

    @Override
    public ActionItem getOrCreateActionItem(ShippingOrder shippingOrder, List<Bucket> buckets) {
        ActionItem actionItem = existsActionItem(shippingOrder);
        if (actionItem == null) {
            actionItem = new ActionItem();
            //defaults while creating a new ActionItem
            actionItem.setShippingOrder(shippingOrder);
            actionItem.setFirstPushDate(new Date());
            actionItem.setTrafficState(EnumTrafficState.NORMAL.asTrafficState());
            actionItem = allocateBucketsAndTasks(actionItem); //called at 1st create, bucketAllocator.autoAllocateDefaultBuckets
        }
        actionItem.setBuckets(buckets); //called for update
        actionItem.setLastPushDate(new Date());
        return actionItem;
    }

    @Override
    public ActionItem existsActionItem(ShippingOrder shippingOrder) {
       return actionItemDao.searchActionItem(shippingOrder);
    }

    @Override
    public ActionItem allocateBucketsAndTasks(ActionItem actionItem) {
        List<EnumBucket> enumBuckets = BucketAllocator.allocateBuckets(actionItem.getShippingOrder());
        if (!enumBuckets.isEmpty()) {
            List<Bucket> buckets = getBuckets(enumBuckets);
            EnumActionTask currentActionTask = BucketAllocator.listCurrentActionTask(enumBuckets);
            actionItem.setPreviousActionTask(actionItem.getCurrentActionTask());
            actionItem.setCurrentActionTask(find(currentActionTask));
            actionItem.setBuckets(buckets);
        }
        return actionItem;
    }

}
