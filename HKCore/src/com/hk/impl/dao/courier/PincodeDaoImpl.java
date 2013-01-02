package com.hk.impl.dao.courier;

import java.util.List;
import com.hk.domain.courier.Zone;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang.StringUtils;

import com.hk.domain.core.Pincode;
import com.hk.domain.core.City;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.constants.courier.EnumCourierGroup;


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

	public Zone getZoneByName(String zoneName) {
		zoneName = zoneName.trim();
		DetachedCriteria zoneCriteria = DetachedCriteria.forClass(Zone.class);
		zoneCriteria.add(Restrictions.like("name", zoneName));
		List<Zone> zone = findByCriteria(zoneCriteria);
		if (zone != null && zone.size() > 0) {
			return zone.get(0);
		} else
			return null;
	}

	public List<Pincode> getPincodeNotInPincodeRegionZone() {
		String hqlQuery = "from Pincode p  where p not in (select pincode from PincodeRegionZone ) ";
		List<Pincode> pincodeList = getSession().createQuery(hqlQuery).list();
		Integer totalGroupCount = EnumCourierGroup.getValidCourierGroupsInUse().size() * 2;
		String query = "select prz.pincode from PincodeRegionZone prz group by prz.pincode " +
				" having count(prz.id) < :totalGroupCount";
		List<Pincode> incompletePincodeList = getSession().createQuery(query).setParameter("totalGroupCount", totalGroupCount.longValue()).list();
		pincodeList.addAll(incompletePincodeList);
		return pincodeList;
	}


	public List<Pincode> getPincodes(City city) {
		DetachedCriteria pincodeCriteria = DetachedCriteria.forClass(Pincode.class);
		pincodeCriteria.add(Restrictions.eq("city", city));
		return (List<Pincode>) findByCriteria(pincodeCriteria);

	}
}

