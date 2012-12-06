package com.hk.admin.impl.dao.courier;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.courier.DispatchLotDao;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.DispatchLot;
import com.hk.impl.dao.BaseDaoImpl;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/5/12
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DispatchLotDaoImpl extends BaseDaoImpl implements DispatchLotDao {

	public Page searchDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, String zone, String source,
	                              String destination, Date deliveryStartDate, Date deliveryEndDate, int pageNo, int perPage) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DispatchLot.class);

		if(dispatchLot != null) {
			criteria.add(Restrictions.eq("id", dispatchLot.getId()));
		}
		if (!StringUtils.isBlank(docketNumber)) {
			criteria.add(Restrictions.eq("docketNumber", docketNumber));
		}

		if (!StringUtils.isBlank(zone)) {
			criteria.add(Restrictions.eq("zone", zone));
		}

		if (!StringUtils.isBlank(source)) {
			criteria.add(Restrictions.eq("source", source));
		}

		if (!StringUtils.isBlank(destination)) {
			criteria.add(Restrictions.eq("destination", destination));
		}

		if (deliveryStartDate != null) {
			criteria.add(Restrictions.ge("deliveryDate", deliveryStartDate));
		}

		if(deliveryEndDate != null) {
			criteria.add(Restrictions.le("deliveryDate", deliveryEndDate));
		}

		if (courier != null) {
			DetachedCriteria courierCriteria = criteria.createCriteria("courier");
			courierCriteria.add(Restrictions.eq("id", courier.getId()));
		}

		return list(criteria, pageNo, perPage);
	}

}
