package com.hk.impl.dao.shippingOrder;

import java.util.Date;
import java.util.List;

import com.hk.domain.analytics.Reason;
import com.hk.domain.order.ShippingOrderLifecycle;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;

@Repository
public class ShippingOrderLifecycleDaoImpl extends BaseDaoImpl implements ShippingOrderLifecycleDao {

    public Date getActivityDateForShippingOrder(ShippingOrder shippingOrder, List<EnumShippingOrderLifecycleActivity> shippingOrderLifecycleActivites) {
        List<Long> lifecycleActivityIds = EnumShippingOrderLifecycleActivity.getSOLifecycleActivityIDs(shippingOrderLifecycleActivites);
        return (Date) getSession().createQuery(
                "select max(sol.activityDate) from ShippingOrderLifecycle sol where sol.shippingOrder.id = :shippingOrderId "
                        + " and sol.shippingOrderLifeCycleActivity.id in ( :lifecycleActivityIds)").setLong("shippingOrderId", shippingOrder.getId()).setParameterList(
                "lifecycleActivityIds", lifecycleActivityIds).uniqueResult();
    }

    public List<ShippingOrderLifeCycleActivity> getOrderActivities(List<EnumShippingOrderLifecycleActivity> enumShippingOrderActivities) {
        List<Long> orderLifecycleIds = EnumShippingOrderLifecycleActivity.getSOLifecycleActivityIDs(enumShippingOrderActivities);
        Criteria criteria = getSession().createCriteria(ShippingOrderLifeCycleActivity.class);
        criteria.add(Restrictions.in("id", orderLifecycleIds));

        return criteria.list();
    }

    public List<ShippingOrderLifecycle> getShippingOrderLifecycleBySOAndActivities(Long shippingOrderId, List<Long> shippingOrderLifeCycleActivityIds) {
		return findByNamedParams("select sl from ShippingOrderLifecycle sl where sl.shippingOrder.id = :shippingOrderId " +
				" and sl.shippingOrderLifeCycleActivity.id in (:shippingOrderLifeCycleActivityIds) ", new String[]{"shippingOrderId", "shippingOrderLifeCycleActivityIds"},
				new Object[]{shippingOrderId, shippingOrderLifeCycleActivityIds});
	}

    @Override
    public List<Reason> getReasonsByType(String type) {
        String queryString = "from Classification r where r.primary=:type";
        return findByNamedParams(queryString, new String[]{"type"}, new Object[]{type});
    }
}
