package com.hk.impl.dao.shippingOrder;

import java.util.Date;
import java.util.List;

import com.hk.domain.analytics.Reason;
import com.hk.domain.courier.Awb;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.pact.dao.BaseDao;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
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
        String queryString = "from Reason r where r.type=:type";
        return findByNamedParams(queryString, new String[]{"type"}, new Object[]{type});
    }

    public String getAwbByShippingOrderLifeCycle(ShippingOrder shippingOrder) {
        Long shippingOrderId = shippingOrder.getId();
        String queryAwb = "select final.sol_awb as solcAwb from ( select shipping_order_id , left(substr(comments,locate('=''', comments)+2),length(substr(comments,locate('=''', comments)+2))-2) as sol_awb," +
                "max(create_dt) FROM shipping_order_lifecycle where shipping_order_lifecycle_activity_id=" + EnumShippingOrderLifecycleActivity.SO_Shipment_Auto_Created.getId() +
                " group by shipping_order_id  ) final where final.sol_awb REGEXP '^[A-Z,0-9]*[0-9]$' " +
                "and final.sol_awb is not null and final.shipping_order_id =:shippingOrderId";
        SQLQuery query = this.createSqlQuery(queryAwb);
        query.addScalar("solcAwb", Hibernate.STRING);
        query.setParameter("shippingOrderId", shippingOrder.getId());
        List<String> awbList = query.list();
        if (awbList != null && awbList.size() > 0) {
            return awbList.get(0);
        } else {
            return null;
        }
    }
}
