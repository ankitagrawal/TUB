package com.hk.admin.impl.dao.courier;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.impl.dao.BaseDaoImpl;
import com.akube.framework.dao.Page;

@Repository
public class CourierDaoImpl extends BaseDaoImpl implements CourierDao{


    @SuppressWarnings("unchecked")
    public List<Courier> getCourierByIds(List<Long> courierIds) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Courier.class);
        criteria1.add(Restrictions.in("id", courierIds));
        return findByCriteria(criteria1);
    }


    public Courier getPreferredCourierForState(String state) {
        return (Courier) getSession().createQuery("select scs.courier from StateCourierService scs where lower(scs.state) = :state").setParameter("state", state.toLowerCase()).uniqueResult();
    }	


    @SuppressWarnings("unchecked")
    public List<Courier> getCouriers(List<Long> courierIds, List<String> courierNames, Boolean disabled) {
	    DetachedCriteria courierCriteria = getCourierCriteria(courierIds, courierNames, null, disabled);
	    return (List<Courier>) findByCriteria(courierCriteria);

    }


	public DetachedCriteria getCourierCriteria(List<Long> courierIds, List<String> courierNames, List<String> courierGroups, Boolean disabled) {
		DetachedCriteria courierCriteria = DetachedCriteria.forClass(Courier.class);
		if (courierIds != null && courierIds.size() > 0) {
			courierCriteria.add(Restrictions.in("id", courierIds));
		}
		if (courierNames != null && courierNames.size() > 0) {
			courierCriteria.add(Restrictions.in("name", courierNames));
		}

		if (disabled != null) {
			courierCriteria.add(Restrictions.eq("disabled", disabled));
		}
		if (courierGroups != null && courierGroups.size() > 0) {
			courierCriteria.createCriteria("courierGroup", "group");
			courierCriteria.add(Restrictions.in("group.name", courierGroups));
		}
		courierCriteria.addOrder(org.hibernate.criterion.Order.asc("name").ignoreCase());
		return courierCriteria;

	}

	public Page getCouriers(String courierName, Boolean disabled, String courierGroup, int page, int perPage) {
		List<String> courierNameList = null;
		if (courierName != null) {
			courierNameList = new ArrayList<String>();
			courierNameList.add(courierName);
		}
		List<String> courierGroupList = null;
		if (courierGroup != null) {
			courierGroupList = new ArrayList<String>();
			courierGroupList.add(courierGroup);
		}
		return list(getCourierCriteria(null, courierNameList, courierGroupList, disabled), page, perPage);
	}
}
