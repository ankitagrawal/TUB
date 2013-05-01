package com.hk.admin.impl.service.queue;

import com.hk.constants.queue.EnumActionTask;
import com.hk.domain.queue.*;
import com.hk.domain.user.User;
import com.hk.pact.dao.queue.ActionItemDao;
import com.hk.constants.queue.EnumBucket;
import com.hk.constants.queue.EnumTrafficState;
import com.hk.domain.order.ShippingOrder;
import com.hk.impl.service.queue.BucketService;
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
    public ActionItem allocateBuckets(ShippingOrder shippingOrder) {
        ActionItem actionItem = null;
        List<EnumBucket> enumBuckets = BucketAllocator.allocateBuckets(shippingOrder);
        if (!enumBuckets.isEmpty()) {
            List<Bucket> buckets = getBuckets(enumBuckets);
            EnumActionTask currentActionTask = BucketAllocator.listCurrentActionTask(enumBuckets);
            actionItem = existsActionItem(shippingOrder);
            if (actionItem == null) {
                actionItem = new ActionItem();
                actionItem.setFirstPushDate(new Date());
            }
            actionItem.setPreviousActionTask(actionItem.getCurrentActionTask());
            actionItem.setCurrentActionTask(find(currentActionTask));
            actionItem.setBuckets(buckets);
            actionItem.setShippingOrder(shippingOrder);
            actionItem = pushToActionQueue(actionItem);
        }
        return actionItem;
    }

    @Override
    public ActionItem existsActionItem(ShippingOrder shippingOrder) {
        List<ActionItem> actionItems = actionItemDao.searchActionItem(shippingOrder, null, null, null, null, null, null, null);
        return actionItems != null && !actionItems.isEmpty() ? actionItems.get(0) : null;
    }

    @Override
    public ActionItem pushToActionQueue(ActionItem actionItem) {
        actionItem.setFlagged(false);
        User reporter = userService.getLoggedInUser();
        if(reporter == null){
          reporter = userService.getAdminUser();
        }
        actionItem.setReporter(reporter);
        actionItem.setLastPushDate(new Date());
        actionItem.setTrafficState(EnumTrafficState.NORMAL.asTrafficState());
        return saveActionItem(actionItem);
    }

    @Override
    public ActionItem popFromActionQueue(ActionItem actionItem) {
        return null;
    }

    @Override
    public ActionItem changeBucket(ActionItem actionItem, List<Bucket> bucketList) {
        actionItem.setBuckets(bucketList);
        return saveActionItem(actionItem);
    }

    @Override
    public ActionItem changeBucket(ShippingOrder shippingOrder, List<Bucket> bucketList) {
        ActionItem actionItem = existsActionItem(shippingOrder);
        if (actionItem != null) {
            actionItem = changeBucket(actionItem, bucketList);
        }
        return actionItem;
    }

    @Override
    public ActionItem changeBucket(ShippingOrder shippingOrder, Bucket bucket) {
        return changeBucket(shippingOrder, Arrays.asList(bucket));
    }

    @Override
    public List<Bucket> findBucket(List<String> bucketNames, Classification classification) {
        return null;
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
        Bucket actionableBucket = shippingOrder.isDropShipping() ? find(EnumBucket.Vendor) :  find(EnumBucket.Warehouse);
        return changeBucket(shippingOrder, actionableBucket);
    }

}
