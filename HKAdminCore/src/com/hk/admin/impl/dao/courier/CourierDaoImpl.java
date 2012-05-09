package com.hk.admin.impl.dao.courier;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.constants.shipment.EnumCourier;
import com.hk.domain.courier.Courier;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class CourierDaoImpl extends BaseDaoImpl implements CourierDao{

    public Courier getCourierByName(String name) {
        return (Courier) getSession().createQuery("from Courier c where c.name = :name").setString("name", name).uniqueResult();
    }

    public Courier getCourierById(Long courierId) {
        return get(Courier.class, courierId);
    }

    public List<Courier> getAllCouriers() {
        return getAll(Courier.class);
    }

    @SuppressWarnings("unchecked")
    public List<Courier> listRestOfIndiaAvailableCouriers() {
        List<EnumCourier> restOfIndiaAvailableCouriers = EnumCourier.getRestOfIndiaAvailableCouriers();

        List<Long> courierIds = EnumCourier.getCourierIDs(restOfIndiaAvailableCouriers);
        Criteria criteria = getSession().createCriteria(Courier.class);
        criteria.add(Restrictions.in("id", courierIds));

        return criteria.list();
    }

    public Courier getPreferredCourierForState(String state) {
        return (Courier) getSession().createQuery("select scs.courier from StateCourierService scs where lower(scs.state) = :state").setParameter("state", state.toLowerCase()).uniqueResult();
    }

}
