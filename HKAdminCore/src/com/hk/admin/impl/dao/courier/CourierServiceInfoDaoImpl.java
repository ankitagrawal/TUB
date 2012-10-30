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
public class CourierServiceInfoDaoImpl extends BaseDaoImpl implements CourierServiceInfoDao {

    public Courier searchDefaultCourier(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse) {
        Criteria pincodeDefaultCourierCriteria = getSession().createCriteria(PincodeDefaultCourier.class);
        pincodeDefaultCourierCriteria.add(Restrictions.eq("warehouse", warehouse));
        pincodeDefaultCourierCriteria.add(Restrictions.eq("pincode", pincode));
        pincodeDefaultCourierCriteria.add(Restrictions.eq("cod", isCOD));
        pincodeDefaultCourierCriteria.add(Restrictions.eq("groundShipping", isGroundShipping));
        PincodeDefaultCourier pincodeDefaultCourierObj = (PincodeDefaultCourier) pincodeDefaultCourierCriteria.uniqueResult();
        if (pincodeDefaultCourierObj != null) {
            return pincodeDefaultCourierObj.getCourier();
        }
        return null;
    }

    public List<Courier> searchCouriers(String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping ,Boolean active) {
        List<CourierServiceInfo> servicesList = getCourierServiceInfoList(null, pincode, forCOD, forGroundShipping, forCodAvailableOnGroundShipping ,active);
        List<Courier> courierList = new ArrayList<Courier>();
        if (servicesList != null && servicesList.size() > 0) {
            for (CourierServiceInfo serviceInfo : servicesList) {
                courierList.add(serviceInfo.getCourier());
            }
        }
        return courierList;
    }

    public List<CourierServiceInfo> getCourierServiceInfoList(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping ,Boolean active) {

        Criteria courierServiceInfoCriteria = getSession().createCriteria(CourierServiceInfo.class);
        if (forCOD) {
            courierServiceInfoCriteria.add(Restrictions.eq("codAvailable", forCOD));
        }
        if (forGroundShipping) {
            courierServiceInfoCriteria.add(Restrictions.eq("groundShippingAvailable", forGroundShipping));
        }
        if (forCodAvailableOnGroundShipping) {
            courierServiceInfoCriteria.add(Restrictions.eq("codAvailableOnGroundShipping", forCodAvailableOnGroundShipping));
        }

	    Criteria courierCriteria = courierServiceInfoCriteria.createCriteria("courier");
	    if (courierId != null) {
		    courierCriteria.add(Restrictions.eq("id", courierId));
	    }
	    if (active != null) {
		    boolean value = !active;
		    courierCriteria.add(Restrictions.eq("deleted", value));
	    }


        if (pincode != null && StringUtils.isNotBlank(pincode)) {
            Criteria pinCodeCriteria = courierServiceInfoCriteria.createCriteria("pincode");
            pinCodeCriteria.add(Restrictions.eq("pincode", pincode));
        }
              
        return courierServiceInfoCriteria.list();
    }

    public CourierServiceInfo searchCourierServiceInfo(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping) {
        List<CourierServiceInfo> courierServiceInfoList = getCourierServiceInfoList(courierId, pincode, forCOD, forGroundShipping, forCodAvailableOnGroundShipping ,null);
        return courierServiceInfoList != null && courierServiceInfoList.size() > 0 ? courierServiceInfoList.get(0) : null;

    }

    public boolean isCourierServiceInfoAvailable(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping) {
        List<CourierServiceInfo> courierServiceInfoList = getCourierServiceInfoList(courierId, pincode, forCOD, forGroundShipping, forCodAvailableOnGroundShipping ,null);
        return courierServiceInfoList != null && courierServiceInfoList.size() > 0 ? true : false;
    }

}
