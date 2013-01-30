package com.hk.admin.impl.dao.courier;

import com.hk.admin.pact.dao.courier.PincodeRegionZoneDao;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.domain.courier.PincodeRegionZone;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/25/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
@Repository
public class PincodeRegionZoneDaoImpl extends BaseDaoImpl implements PincodeRegionZoneDao {

    private static Logger logger = LoggerFactory.getLogger(PincodeRegionZoneDaoImpl.class);


    public List<PincodeRegionZone> getSortedRegionList(List<Courier> courierList, Pincode pincode, Warehouse warehouse) {

		/*   select * from pincode_region_zone prz,region_type rt where prz.region_type_id = rt.id and prz.pincode_id = 1
				and rt.priority =(select min(r.priority) from pincode_region_zone p,region_type r where p.region_type_id = r.id and p.pincode_id = 1)
				*/

        Long minPriority = (Long) getSession().createQuery("select min(prz.regionType.priority) from PincodeRegionZone prz inner join prz.courierGroup.couriers c " +
                "where c in (:courierList) and prz.pincode = :pincode and prz.warehouse = :warehouse")
                .setParameterList("courierList", courierList)
                .setParameter("pincode", pincode)
                .setParameter("warehouse", warehouse)
                .uniqueResult();

        logger.info("minPriority is " + minPriority);

        return getSession().createQuery("select prz from PincodeRegionZone prz inner join prz.courierGroup.couriers c " +
                "where c in (:courierList) and prz.pincode = :pincode and prz.warehouse = :warehouse and prz.regionType.priority = :minPriority")
                .setParameterList("courierList", courierList)
                .setParameter("pincode", pincode)
                .setParameter("warehouse", warehouse)
                .setParameter("minPriority", minPriority)
                .list();

    }

    public List<PincodeRegionZone> getApplicableRegionList(List<Courier> courierList, Pincode pincode, Warehouse warehouse) {

        return getSession().createQuery("select prz from PincodeRegionZone prz inner join prz.courierGroup.couriers c " +
                "where c in (:courierList) and prz.pincode = :pincode and prz.warehouse = :warehouse")
                .setParameterList("courierList", courierList)
                .setParameter("pincode", pincode)
                .setParameter("warehouse", warehouse)
                .list();

    }

    public PincodeRegionZone getPincodeRegionZone(CourierGroup courierGroup, Pincode pincode, Warehouse warehouse) {
        Criteria pincodeRegionZoneCriteria = getPincodeRegionZoneCriteria(courierGroup, pincode, warehouse);
        return (PincodeRegionZone) pincodeRegionZoneCriteria.uniqueResult();
    }

    private Criteria getPincodeRegionZoneCriteria(CourierGroup courierGroup, Pincode pincode, Warehouse warehouse) {
        Criteria pincodeRegionZoneCriteria = getSession().createCriteria(PincodeRegionZone.class);
        if (pincode != null) {
            pincodeRegionZoneCriteria.add(Restrictions.eq("pincode", pincode));
        }
        if (courierGroup != null) {
            pincodeRegionZoneCriteria.add(Restrictions.eq("courierGroup", courierGroup));
        }
        if (warehouse != null) {
            pincodeRegionZoneCriteria.add(Restrictions.eq("warehouse", warehouse));
        }
        return pincodeRegionZoneCriteria;
    }

    public List<PincodeRegionZone> getPincodeRegionZoneList(CourierGroup courierGroup, Pincode pincode, Warehouse warehouse) {
        Criteria pincodeRegionZoneCriteria = getPincodeRegionZoneCriteria(courierGroup, pincode, warehouse);
        return (List<PincodeRegionZone>) pincodeRegionZoneCriteria.list();
    }

    public List<PincodeRegionZone> getPincodeRegionZoneList(Pincode pincode) {
        return getPincodeRegionZoneList(null, pincode, null);
    }

    //get NearbyPincode(Pincodes of same city) , save Pincode Region Zones of new pincode/edited pincode same as NearByPincode
    public int assignPincodeRegionZoneToPincode(Pincode pincode, List<PincodeRegionZone> pincodeRegionZoneList) {
        int recordsSaved = 0;
        List<PincodeRegionZone> alreadyExistingPrzForPincode = getPincodeRegionZoneList(pincode);
        for (PincodeRegionZone pincodeRegionZone : alreadyExistingPrzForPincode) {
            super.delete(pincodeRegionZone);
        }
        for (PincodeRegionZone pincodeRegionZone : pincodeRegionZoneList) {
            PincodeRegionZone newPincodeRegionZone = new PincodeRegionZone();
            newPincodeRegionZone.setRegionType(pincodeRegionZone.getRegionType());
            newPincodeRegionZone.setCourierGroup(pincodeRegionZone.getCourierGroup());
            newPincodeRegionZone.setWarehouse(pincodeRegionZone.getWarehouse());
            newPincodeRegionZone.setPincode(pincode);
            super.save(newPincodeRegionZone);
            recordsSaved++;
        }


        return recordsSaved;
    }

}
