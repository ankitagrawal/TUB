package com.hk.admin.impl.service.queue;

import com.hk.domain.user.User;
import com.hk.pact.dao.queue.ActionItemDao;
import com.hk.constants.queue.EnumBucket;
import com.hk.constants.queue.EnumTrafficState;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.Classification;
import com.hk.impl.service.queue.BucketService;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.Param;
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
        List<EnumBucket> enumBuckets = BucketAllocator.allocateBuckets(shippingOrder);
        ActionItem actionItem;
        actionItem = existsActionItem(shippingOrder);
        if(actionItem == null){
            actionItem = new ActionItem();
            actionItem.setFirstPushDate(new Date());
        }
        if (enumBuckets != null && !enumBuckets.isEmpty()) {
            actionItem.setBuckets(EnumBucket.getBuckets(enumBuckets));
            actionItem.setShippingOrder(shippingOrder);
            pushToActionQueue(actionItem);
        }
        return null;
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
    public ActionItem escalateOrderFromActionQueue(ShippingOrder shippingOrder) {
        Bucket actionableBucket = shippingOrder.isDropShipping() ? EnumBucket.Vendor.asBucket() : EnumBucket.Warehouse.asBucket();
        return changeBucket(shippingOrder, actionableBucket);
    }

}
