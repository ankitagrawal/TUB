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

    public List<PincodeDefaultCourier> searchPincodeDefaultCourierList(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGroundshipping) {

        Criteria pincodeDefaultCourierCriteria = getSession().createCriteria(PincodeDefaultCourier.class);

        if (warehouse != null && StringUtils.isNotBlank(Long.toString(warehouse.getId()))) {
            pincodeDefaultCourierCriteria.add(Restrictions.eq("warehouse", warehouse));
        }
        if (pincode != null && StringUtils.isNotBlank(Long.toString(pincode.getId()))) {
            pincodeDefaultCourierCriteria.add(Restrictions.eq("pincode", pincode));
        }
        if (isCod != null) {
            pincodeDefaultCourierCriteria.add(Restrictions.eq("cod", isCod));
        }
        if (isGroundshipping != null) {
            pincodeDefaultCourierCriteria.add(Restrictions.eq("groundShipping", isGroundshipping));
        }
        List<PincodeDefaultCourier> pincodeDefaultCouriers = pincodeDefaultCourierCriteria.list();

        return pincodeDefaultCouriers;
    }

    public PincodeDefaultCourier searchPincodeDefaultCourier(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGroundshipping) {
        List<PincodeDefaultCourier> pincodeDefaultCouriers = searchPincodeDefaultCourierList(pincode, warehouse, isCod, isGroundshipping);
        if (pincodeDefaultCouriers.size() > 0 && pincodeDefaultCouriers != null) {
            return pincodeDefaultCouriers.get(0);
        }
        return null;
    }

}
