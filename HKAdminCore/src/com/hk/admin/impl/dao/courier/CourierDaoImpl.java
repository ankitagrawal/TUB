package com.hk.admin.impl.dao.courier;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.domain.courier.Courier;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.constants.courier.CourierConstants;

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
	public List<Courier> getCouriers(String name, Boolean disabled) {
		DetachedCriteria courierCriteria = DetachedCriteria.forClass(Courier.class);
		if (name != null) {
			courierCriteria.add(Restrictions.eq("name", name));
		}
		if (disabled != null) {
			courierCriteria.add(Restrictions.eq("disabled", disabled));
		}
		return (List<Courier>) findByCriteria(courierCriteria);

	}
}
