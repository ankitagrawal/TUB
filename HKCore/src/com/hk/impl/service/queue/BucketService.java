package com.hk.impl.service.queue;

import com.akube.framework.dao.Page;
import com.hk.constants.queue.EnumActionTask;
import com.hk.constants.queue.EnumBucket;
import com.hk.core.search.ActionItemSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.Param;

import java.util.List;
import java.util.Map;

/*
 * User: Pratham
 * Date: 10/04/13  Time: 15:54
*/
public interface BucketService {

    List<Param> getParamsForBucket(List<Bucket> bucketList);

    Map<String, Object> getParamMap(List<Bucket> bucketList);

    ActionItem saveActionItem(ActionItem actionItem);

    Bucket find(EnumBucket enumBucket);

    ActionTask find(EnumActionTask enumActionTask);

    public List<Bucket> findByName(List<String> bucketNames);

    List<Bucket> getBuckets(List<EnumBucket> enumBuckets);

    List<Bucket> listAll();

    /*
      createUpdateActionItem  --> inputs needed (SO, calledAuto/EscalatedBack/manual), (Reporter loggedIn/Admin)

      for each actionItem, either pass the buckets (manually, or reason.getBucket(), or calculate the buckets (Auto) at order placement

      can be passed explicitly by a user, or associated to a reason or calculated at order_placement

      so my method may look like

      updateActionItem(shippingOrder,List<Buckets>, boolean isAuto)  {

      checkIfExists ? actionItem.setBuckets(buckets) for the loggedInUser

       : createActionItem (SO/)

     */

    public Page searchActionItems(ActionItemSearchCriteria actionItemSearchCriteria, int pageNo, int perPage);

    ActionItem existsActionItem(ShippingOrder shippingOrder);

    ActionItem autoCreateUpdateActionItem(ShippingOrder shippingOrder);

    ActionItem escalateOrderFromActionQueue(ShippingOrder shippingOrder);

    ActionItem escalateBackToActionQueue(ShippingOrder shippingOrder);

    void popFromActionQueue(ShippingOrder shippingOrder);

    public Bucket getBucketById(Long bucketId);

    ActionItem autoUpdateActionItem(ActionItem actionItem, boolean autoUpdate);

    public ActionItem getActionItemById (Long actionItemId);

    public List<ActionItem> getActionItemsOfActionQueue ();

}
