package com.hk.dao.courier;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;

@SuppressWarnings("unchecked")
@Repository
public class PincodeDao extends BaseDaoImpl {

    public Pincode getByPincode(String pincode) {
        String hqlQuery = " from Pincode pincode where pincode.pincode = :pincode";
        return (Pincode) getSession().createQuery(hqlQuery).setParameter("pincode", pincode).uniqueResult();
    }

    public List<PincodeDefaultCourier> getByPincode(Pincode pincode) {
        String hqlQuery = " from PincodeDefaultCourier pincodeDefaultCourier where pincodeDefaultCourier.pincode = :pincode";
        List<PincodeDefaultCourier> pincodeDefaultCouriers = getSession().createQuery(hqlQuery).setParameter("pincode", pincode).list();
        return pincodeDefaultCouriers;
    }

    public PincodeDefaultCourier getByPincodeWarehouse(Pincode pincode, Warehouse warehouse) {
        Criteria pincodeDefaultCourierCriteria = getSession().createCriteria(PincodeDefaultCourier.class);
        pincodeDefaultCourierCriteria.add(Restrictions.eq("warehouse", warehouse));
        pincodeDefaultCourierCriteria.add(Restrictions.eq("pincode", pincode));
        PincodeDefaultCourier pincodeDefaultCourierObj = (PincodeDefaultCourier) pincodeDefaultCourierCriteria.uniqueResult();
        return pincodeDefaultCourierObj;
    }
}
