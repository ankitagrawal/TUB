package com.hk.admin.impl.dao.courier;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.constants.courier.EnumCourier;
import com.hk.domain.courier.Courier;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class CourierDaoImpl extends BaseDaoImpl implements CourierDao{

    public Courier getCourierByName(String name) {
        return (Courier) findUniqueByNamedParams("from Courier c where c.name = :name", new String[]{"name"}, new Object[]{name});
    }

    public List<Courier> getCourierByIds(List<Long> courierIds) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(Courier.class);
        criteria1.add(Restrictions.in("id", courierIds));
        return findByCriteria(criteria1);
    }

    public List<Courier> getAllCouriers() {
        return getAll(Courier.class);
    }

    public Courier getPreferredCourierForState(String state) {
        return (Courier) getSession().createQuery("select scs.courier from StateCourierService scs where lower(scs.state) = :state").setParameter("state", state.toLowerCase()).uniqueResult();
    }

}
