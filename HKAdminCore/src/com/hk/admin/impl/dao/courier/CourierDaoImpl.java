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

    public Courier getCourierByName(String name) {
        return (Courier) findUniqueByNamedParams("from Courier c where c.name = :name", new String[]{"name"}, new Object[]{name});
    }

    @SuppressWarnings("unchecked")
    public List<Courier> getCourierByIds(List<Long> courierIds) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Courier.class);
        criteria1.add(Restrictions.in("id", courierIds));
        return findByCriteria(criteria1);
    }

    public List<Courier> getAvailableCouriers() {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Courier.class);
        criteria1.add(Restrictions.eq("deleted",new Boolean(false) ));
        return (List<Courier>)  findByCriteria(criteria1);
    }

    public Courier getPreferredCourierForState(String state) {
        return (Courier) getSession().createQuery("select scs.courier from StateCourierService scs where lower(scs.state) = :state").setParameter("state", state.toLowerCase()).uniqueResult();
    }

	 public List<Courier> getDisableCourier(){
		 DetachedCriteria criteria1 = DetachedCriteria.forClass(Courier.class);
        criteria1.add(Restrictions.eq("deleted", new Boolean(true)));
        return(List<Courier>) findByCriteria(criteria1);
	 }
}
