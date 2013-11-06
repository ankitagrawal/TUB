package com.hk.impl.dao.shippingOrder;

import java.util.*;

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
      Map<Long,String> soCommentMap  = new HashMap<Long,String>();
      String commentIdentifer="awbNumber='";
      List<ShippingOrderLifecycle> shippingOrderLifecycles = shippingOrder.getShippingOrderLifecycles();
      for (ShippingOrderLifecycle shippingOrderLifecycle : shippingOrderLifecycles) {
        if (shippingOrderLifecycle.getShippingOrderLifeCycleActivity().getId().equals(EnumShippingOrderLifecycleActivity.SO_Shipment_Auto_Created.getId()) ||
            shippingOrderLifecycle.getShippingOrderLifeCycleActivity().getId().equals(EnumShippingOrderLifecycleActivity.SHIPMENT_RESOLUTION_ACTIVITY.getId())) {
          if(shippingOrderLifecycle.getComments().contains(commentIdentifer)){
          soCommentMap.put(shippingOrderLifecycle.getId(),shippingOrderLifecycle.getComments());
          }
        }
      }
      String comments= soCommentMap.get(Collections.max(soCommentMap.keySet()));
       //comments= codAirAwb{courier=500, awbNumber='HK00178749'}
      String[] awbSpilt=comments.split(commentIdentifer);
      return awbSpilt[1].substring(0, awbSpilt[1].length()-2);
    }
}
