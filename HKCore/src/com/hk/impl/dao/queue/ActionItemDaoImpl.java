package com.hk.impl.dao.queue;

import com.akube.framework.dao.Page;
import com.hk.constants.queue.EnumBucket;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ActionItemSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.queue.*;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.queue.ActionItemDao;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * User: Pratham
 * Date: 15/04/13  Time: 20:26
*/
@Service
public class ActionItemDaoImpl extends BaseDaoImpl implements ActionItemDao {

    @Override
    public ActionItem save(ActionItem actionItem) {
        return (ActionItem) super.save(actionItem);
    }

    @Override
    public ActionItem searchActionItem(ShippingOrder shippingOrder) {
        String queryString = "from ActionItem aT where aT.shippingOrder =:shippingOrder";
        return (ActionItem) findUniqueByNamedParams(queryString, new String[]{"shippingOrder"}, new Object[]{shippingOrder});
    }

    public List<ActionItem> searchActionItem(ShippingOrder shippingOrder, List<Bucket> buckets, Date startPushDate, Date startPopDate, List<TrafficState> trafficStates, User watcher, Boolean flagged, User reporter) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ActionItem.class);

        if (buckets != null && !buckets.isEmpty()) {
            detachedCriteria.add(Restrictions.in("bucket", buckets));
        }
        if (shippingOrder != null) {
            detachedCriteria.add(Restrictions.eq("shippingOrder", shippingOrder));
        }

        if (startPushDate != null || startPopDate != null) {
            detachedCriteria.add(Restrictions.between("pushDate", startPushDate, startPopDate));
        }

        detachedCriteria.addOrder(org.hibernate.criterion.Order.asc("id"));

        return (List<ActionItem>) findByCriteria(detachedCriteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Bucket> getBuckets(List<EnumBucket> enumBuckets) {
        List<Long> bucketIds = EnumBucket.getBucketIDs(enumBuckets);
        if (bucketIds == null || bucketIds.isEmpty()) {
            return new ArrayList<Bucket>();
        }
        Criteria criteria = getSession().createCriteria(Bucket.class);
        criteria.add(Restrictions.in("id", bucketIds));
        return criteria.list();
    }

    @Override
    public List<Bucket> findByName(List<String> bucketNames) {
        String queryString = "from Bucket b where b.name in (:bucketNames)";
        return findByNamedParams(queryString, new String[]{"bucketNames"}, new Object[]{bucketNames});
    }

    @Override
    public Page searchActionItems(ActionItemSearchCriteria actionItemSearchCriteria, int pageNo, int perPage) {
        DetachedCriteria searchCriteria = actionItemSearchCriteria.getSearchCriteria();
        return list(searchCriteria, true, pageNo, perPage);
    }

    @Override
    public List<ActionTask> listNextActionTasks(ActionTask currentActionTask, List<Bucket> buckets) {
        Set<ActionTask> nextActionTasks = new HashSet<ActionTask>();
        List<ActionPathWorkflow> actionPathWorkFlows = searchActionPathWorkflow(currentActionTask, buckets);
        for (ActionPathWorkflow actionPathWorkFlow : actionPathWorkFlows) {
            nextActionTasks.add(actionPathWorkFlow.getNextActionTask());
        }
        return new ArrayList<ActionTask>(nextActionTasks);
    }


    public List<ActionTask> listCurrentActionTask(ActionTask currentActionTask, List<Bucket> buckets) {
        Set<ActionTask> currentActionTasks = new HashSet<ActionTask>();
        List<ActionPathWorkflow> actionPathWorkFlows = searchActionPathWorkflow(currentActionTask, buckets);
        for (ActionPathWorkflow actionPathWorkFlow : actionPathWorkFlows) {
            currentActionTasks.add(actionPathWorkFlow.getActionTask());
        }
        return new ArrayList<ActionTask>(currentActionTasks);
    }


    private List<ActionPathWorkflow> searchActionPathWorkflow(ActionTask currentActionTask, List<Bucket> buckets) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ActionPathWorkflow.class);
        if (buckets != null) {
            detachedCriteria.add(Restrictions.in("bucket", buckets));
        }
        if (currentActionTask != null) {
            detachedCriteria.add(Restrictions.eq("actionTask", currentActionTask));
        }
        return findByCriteria(detachedCriteria);
    }


    public List<ActionItem> getActionItemsOfActionQueue() {
        String hql = "select DISTINCT ai from ActionItem ai  where ai.shippingOrder.shippingOrderStatus.id in( :actionAwaitingStatus)";
        Query actionItemListQuery = getSession().createQuery(hql.toString()).setParameterList("actionAwaitingStatus", EnumShippingOrderStatus.getStatusIdsForActionQueue());
        return (List<ActionItem>) actionItemListQuery.list();

    }

}
