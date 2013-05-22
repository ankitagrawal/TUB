package com.hk.core.search;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.ActionItem;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.TrafficState;
import com.hk.domain.user.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * User: Pratham
 * Date: 20/05/13  Time: 17:18
*/
public class ActionItemSearchCriteria {

    private Long actionItemId;
    private String shippingOrderId;
    private String baseOrderId;

    private List<ShippingOrder> shippingOrders;

    private List<TrafficState> trafficStates;

    private List<ActionTask> currentActionTasks;

    private Boolean flagged;

    private List<ActionTask> previousActionTasks;

    private List<Bucket> buckets;

    private Date startPushDate;

    private Date endPushDate;

    private List<User> reporters;

    public DetachedCriteria getSearchCriteria() {
        return buildSearchCriteriaFromBaseCriteria();
    }

    protected DetachedCriteria buildSearchCriteriaFromBaseCriteria() {
        DetachedCriteria criteria = DetachedCriteria.forClass(ActionItem.class);
        if (actionItemId != null) {
            criteria.add(Restrictions.eq("id", actionItemId));
        }
        if (flagged != null) {
            criteria.add(Restrictions.eq("flagged", flagged));
        }
        DetachedCriteria shippingOrderCriteria = null;
        if (shippingOrderId != null) {
            shippingOrderCriteria = criteria.createCriteria("shippingOrder");
            shippingOrderCriteria.add(Restrictions.eq("id", shippingOrderId));
        }
        if (shippingOrders != null && !shippingOrders.isEmpty()) {
            criteria.add(Restrictions.in("shippingOrder", shippingOrders));
        }
        if (buckets != null && !buckets.isEmpty()) {
            criteria.createCriteria("buckets", Criteria.INNER_JOIN).add(Restrictions.in("id", getBucketIDs(buckets)));
        }
        if (reporters != null && !reporters.isEmpty()) {
            criteria.add(Restrictions.in("reporter", reporters));
        }
        if (trafficStates != null && !trafficStates.isEmpty()) {
            criteria.add(Restrictions.in("trafficState", trafficStates));
        }
        if (currentActionTasks != null && !currentActionTasks.isEmpty()) {
            criteria.add(Restrictions.in("currentActionTask", currentActionTasks));
        }
        if (previousActionTasks != null && !previousActionTasks.isEmpty()) {
            criteria.add(Restrictions.in("previousActionTask", previousActionTasks));
        }
        if (startPushDate != null && endPushDate != null) {
            criteria.add(Restrictions.between("firstPushDate", startPushDate, endPushDate));
        }

        return criteria;
    }


    public ActionItemSearchCriteria setActionItemId(Long actionItemId) {
        this.actionItemId = actionItemId;
        return this;
    }

    public ActionItemSearchCriteria setShippingOrders(List<ShippingOrder> shippingOrders) {
        this.shippingOrders = shippingOrders;
        return this;
    }

    public ActionItemSearchCriteria setTrafficStates(List<TrafficState> trafficStates) {
        this.trafficStates = trafficStates;
        return this;
    }

    public ActionItemSearchCriteria setFlagged(Boolean flagged) {
        this.flagged = flagged;
        return this;
    }

    public ActionItemSearchCriteria setCurrentActionTasks(List<ActionTask> currentActionTasks) {
        this.currentActionTasks = currentActionTasks;
        return this;
    }

    public ActionItemSearchCriteria setPreviousActionTasks(List<ActionTask> previousActionTasks) {
        this.previousActionTasks = previousActionTasks;
        return this;
    }

    public ActionItemSearchCriteria setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
        return this;
    }

    public ActionItemSearchCriteria setStartPushDate(Date startPushDate) {
        this.startPushDate = startPushDate;
        return this;
    }

    public ActionItemSearchCriteria setEndPushDate(Date endPushDate) {
        this.endPushDate = endPushDate;
        return this;
    }

    public ActionItemSearchCriteria setReporters(List<User> reporters) {
        this.reporters = reporters;
        return this;
    }

    public ActionItemSearchCriteria setShippingOrderId(String shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
        return this;
    }

    public void setBaseOrderId(String baseOrderId) {
        this.baseOrderId = baseOrderId;
    }

    private static List<Long> getBucketIDs(List<Bucket> buckets) {
        List<Long> bucketIDs = new ArrayList<Long>();
        if (buckets == null || buckets.isEmpty()) return bucketIDs;
        for (Bucket bucket : buckets) {
            bucketIDs.add(bucket.getId());
        }
        return bucketIDs;
    }

}
