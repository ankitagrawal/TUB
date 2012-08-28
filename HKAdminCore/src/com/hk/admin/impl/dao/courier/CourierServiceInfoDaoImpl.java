package com.hk.admin.impl.dao.courier;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

@SuppressWarnings("unchecked")
@Repository
public class CourierServiceInfoDaoImpl extends BaseDaoImpl implements CourierServiceInfoDao{

    public List<CourierServiceInfo> getCourierServiceInfo(Long courierId) {
        return getCourierServiceInfo(courierId, null, null);
    }

    public List<CourierServiceInfo> getCourierServicesForPinCode(String pincode) {
        return getCourierServiceInfo(null, pincode, null);
    }

    public CourierServiceInfo getCourierServiceByPincodeAndCourier(Long courierId, String pincode, Boolean isCod) {
        List<CourierServiceInfo> courierServiceInfoList = getCourierServiceInfo(courierId, pincode, isCod);
        if (courierServiceInfoList != null && courierServiceInfoList.size() > 0) {
            return courierServiceInfoList.get(0);
        } else {
            return null;
        }                                                                                                 
    }

     public CourierServiceInfo getCourierServiceByPincodeAndCourierWithoutCOD(Long courierId, String pincode) {
        List<CourierServiceInfo> courierServiceInfoList = getCourierServiceInfoByCourierAndPincode(courierId, pincode);
        if (courierServiceInfoList != null && courierServiceInfoList.size() > 0) {
            return courierServiceInfoList.get(0);
        } else {
            return null;
        }
    }


    public List<CourierServiceInfo> getCourierServiceInfo(Long courierId, String pincode, Boolean isCOD) {
        Criteria criteria = getSession().createCriteria(CourierServiceInfo.class);
        if (courierId != null) {
            Criteria courierCriteria = criteria.createCriteria("courier");
            courierCriteria.add(Restrictions.eq("id", courierId));
        }
        Criteria pinCodeCriteria = null;
        if (pincode != null && StringUtils.isNotBlank(pincode)) {
            pinCodeCriteria = criteria.createCriteria("pincode");
            pinCodeCriteria.add(Restrictions.eq("pincode", pincode));
        }
        if (isCOD != null) {
            criteria.add(Restrictions.eq("codAvailable", isCOD));
        }

        return criteria.list();
    }



    public List<CourierServiceInfo> getCourierServiceInfoByCourierAndPincode(Long courierId, String pincode) {
        Criteria criteria = getSession().createCriteria(CourierServiceInfo.class);
        if (courierId != null) {
            Criteria courierCriteria = criteria.createCriteria("courier");
            courierCriteria.add(Restrictions.eq("id", courierId));
        }
        Criteria pinCodeCriteria = null;
        if (pincode != null && StringUtils.isNotBlank(pincode)) {
            pinCodeCriteria = criteria.createCriteria("pincode");
            pinCodeCriteria.add(Restrictions.eq("pincode", pincode));
        }
     
        return criteria.list();
    }

    public boolean isCodAvailable(String pincode) {
        Criteria criteria = getSession().createCriteria(CourierServiceInfo.class);
        Criteria pincodeCriteria = criteria.createCriteria("pincode");
        pincodeCriteria.add(Restrictions.eq("pincode", pincode));
        criteria.add(Restrictions.eq("codAvailable", true));
        List<CourierServiceInfo> courierServiceInfoList = criteria.list();
        return courierServiceInfoList != null && courierServiceInfoList.size() > 0;
    }

    public List<Courier> getCouriersForPincode(String pincode, boolean forCOD) {
        Criteria courierServiceInfoCriteria = getSession().createCriteria(CourierServiceInfo.class);
        if (forCOD) {
            courierServiceInfoCriteria.add(Restrictions.eq("codAvailable", forCOD));
        }
        Criteria pinCodeCriteria = courierServiceInfoCriteria.createCriteria("pincode");
        pinCodeCriteria.add(Restrictions.eq("pincode", pincode));

        List<CourierServiceInfo> servicesList = courierServiceInfoCriteria.list();

        List<Courier> courierList = new ArrayList<Courier>();
        for (CourierServiceInfo serviceInfo : servicesList) {
            courierList.add(serviceInfo.getCourier());
        }
        return courierList;
    }

    public Courier getDefaultCourierForPincode(Pincode pincode, boolean forCOD, Warehouse warehouse) {
        Criteria pincodeDefaultCourierCriteria = getSession().createCriteria(PincodeDefaultCourier.class);
        pincodeDefaultCourierCriteria.add(Restrictions.eq("warehouse", warehouse));
        pincodeDefaultCourierCriteria.add(Restrictions.eq("pincode", pincode));
        PincodeDefaultCourier pincodeDefaultCourierObj = (PincodeDefaultCourier) pincodeDefaultCourierCriteria.uniqueResult();
        if (pincodeDefaultCourierObj != null) {
            if (forCOD)
                return pincodeDefaultCourierObj.getCodCourier();
            else
                return pincodeDefaultCourierObj.getNonCodCourier();
        }
        return null;
    }

     public boolean isGroundShippingAvailable(String pincode) {
        Criteria criteria = getSession().createCriteria(CourierServiceInfo.class);
        Criteria pincodeCriteria = criteria.createCriteria("pincode");
        pincodeCriteria.add(Restrictions.eq("pincode", pincode));
        criteria.add(Restrictions.eq("groundShippingAvailable", true));
        List<CourierServiceInfo> courierServiceInfoList = criteria.list();
        return courierServiceInfoList != null && courierServiceInfoList.size() > 0;
    }

    public boolean isCodAvailableOnGroundShipping(String pincode) {
        Criteria criteria = getSession().createCriteria(CourierServiceInfo.class);
        Criteria pincodeCriteria = criteria.createCriteria("pincode");
        pincodeCriteria.add(Restrictions.eq("pincode", pincode));
        criteria.add(Restrictions.eq("codAvailableOnGroundShipping", true));
        List<CourierServiceInfo> courierServiceInfoList = criteria.list();
        return courierServiceInfoList != null && courierServiceInfoList.size() > 0;
    }
}
