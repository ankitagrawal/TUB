package com.hk.impl.dao.courier;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang.StringUtils;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.courier.PincodeDao;

@SuppressWarnings("unchecked")
@Repository
public class PincodeDaoImpl extends BaseDaoImpl implements PincodeDao {

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


    public List<PincodeDefaultCourier> getByPincode(Pincode pincode, boolean isCod, boolean isGroundshipping) {
        String hqlQuery = " from PincodeDefaultCourier pincodeDefaultCourier where pincodeDefaultCourier.pincode = :pincode and pincodeDefaultCourier.cod = :cod and pincodeDefaultCourier.groundShipping = :groundShipping";
        List<PincodeDefaultCourier> pincodeDefaultCouriers = getSession().createQuery(hqlQuery).setParameter("pincode", pincode).setParameter("cod", isCod).setParameter("groundShipping", isGroundshipping).list();
        return pincodeDefaultCouriers;

    }


    public PincodeDefaultCourier getByPincodeWarehouse(Pincode pincode, Warehouse warehouse, boolean isCod, boolean isGroundshipping) {
        Criteria pincodeDefaultCourierCriteria = getSession().createCriteria(PincodeDefaultCourier.class);
        pincodeDefaultCourierCriteria.add(Restrictions.eq("warehouse", warehouse));
        pincodeDefaultCourierCriteria.add(Restrictions.eq("pincode", pincode));
        pincodeDefaultCourierCriteria.add(Restrictions.eq("cod", isCod));
        pincodeDefaultCourierCriteria.add(Restrictions.eq("groundShipping", isGroundshipping));
        PincodeDefaultCourier pincodeDefaultCourierObj = (PincodeDefaultCourier) pincodeDefaultCourierCriteria.uniqueResult();
        return pincodeDefaultCourierObj;
    }


}
