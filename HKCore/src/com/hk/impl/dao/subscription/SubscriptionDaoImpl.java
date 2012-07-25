package com.hk.impl.dao.subscription;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.search.SubscriptionSearchCriteria;
import com.hk.domain.subscription.SubscriptionStatus;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.subscription.SubscriptionDao;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.User;
import com.hk.domain.order.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 18, 2012
 * Time: 12:51:44 PM
 */
@Repository
@SuppressWarnings("unchecked")
public class SubscriptionDaoImpl extends BaseDaoImpl implements SubscriptionDao {

    public Subscription save(Subscription subscription){
        subscription.setUpdateDate(BaseUtils.getCurrentTimestamp());
        return (Subscription) super.save(subscription);
    }

    public List<Subscription> getSubscriptions(Order order, SubscriptionStatus subscriptionStatus){
        return (List<Subscription>) findByNamedParams(" from Subscription sub where sub.baseOrder = :order and sub.subscriptionStatus =:subscriptionStatus ", new String[]{"order","subscriptionStatus"}, new Object[]{order, subscriptionStatus});
    }

    public List<Subscription> getSubscriptions(EnumSubscriptionStatus subscriptionStatus){
        return (List<Subscription>) findByNamedParams(" from Subscription sub where sub.subscriptionStatus =:subscriptionStatus ", new String[]{"subscriptionStatus"}, new Object[]{subscriptionStatus.asSubscriptionStatus()});
    }

    public Page searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria, int pageNo, int perPage){
        DetachedCriteria searchCriteria = subscriptionSearchCriteria.getSearchCriteria();
        return list(searchCriteria, true, pageNo, perPage);
    }

    public Page getSubscriptionsForUsers(List<SubscriptionStatus> subscriptionStatusList,User user, int page, int perPage){
        DetachedCriteria criteria = DetachedCriteria.forClass(Subscription.class);
        criteria.add(Restrictions.in("subscriptionStatus", subscriptionStatusList));
        criteria.add(Restrictions.eq("user", user));
        criteria.addOrder(org.hibernate.criterion.Order.desc("createDate"));
        return list(criteria, page, perPage);
    }

    public int escalateSubscriptionsToActionQueue(List<SubscriptionStatus> fromStatuses, SubscriptionStatus toStatus, Date referenceDate){
        String query="update Subscription set subscriptionStatus = ?";
        Object[] values=new Object[2+fromStatuses.size()];
        int i=1;
        values[0]=toStatus;
        for(SubscriptionStatus subscriptionStatus : fromStatuses){
            if(i>1){
                query+=" or subscriptionStatus = ? ";
            }else{
                query+=" where subscriptionStatus = ? ";
            }
            values[i] = subscriptionStatus;
            i++;
        }
        query+=" and nextShipmentDate < ?";
        values[i]=referenceDate;
        return bulkUpdate(query,values);
    }

}
