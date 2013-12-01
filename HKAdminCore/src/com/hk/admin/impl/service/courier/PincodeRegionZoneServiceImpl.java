package com.hk.admin.impl.service.courier;

import com.hk.admin.pact.service.courier.PincodeRegionZoneService;
import com.hk.admin.pact.dao.courier.PincodeRegionZoneDao;
import com.hk.domain.courier.PincodeRegionZone;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.domain.core.Pincode;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.PincodeService;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Dec 26, 2012
 * Time: 12:01:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PincodeRegionZoneServiceImpl implements PincodeRegionZoneService {
	@Autowired
	PincodeRegionZoneDao pincodeRegionZoneDao;
	@Autowired
	PincodeService pincodeService;


	public List<PincodeRegionZone> getSortedRegionList(List<Courier> courierList, Pincode pincode, Warehouse warehouse){
		return pincodeRegionZoneDao.getSortedRegionList(courierList,pincode,warehouse);
	}

    public List<PincodeRegionZone> getApplicableRegionList(List<Courier> courierList, Pincode pincode, Warehouse warehouse){
	    return pincodeRegionZoneDao.getApplicableRegionList(courierList, pincode,warehouse);
    }

    public PincodeRegionZone getPincodeRegionZone(CourierGroup courierGroup, Pincode pincode, Warehouse warehouse){
	    return pincodeRegionZoneDao.getPincodeRegionZone(courierGroup, pincode,warehouse);
    }

     public List<PincodeRegionZone> getPincodeRegionZoneList(CourierGroup courierGroup, Pincode pincode, Warehouse warehouse){
	     return pincodeRegionZoneDao.getPincodeRegionZoneList(courierGroup, pincode,warehouse);
     }

	public List<PincodeRegionZone> getPincodeRegionZoneList(Pincode pincode){
		 return pincodeRegionZoneDao.getPincodeRegionZoneList(pincode);
	}

	public int assignPincodeRegionZoneToPincode(Pincode pincode) {
		int recordsSaved = 0;
		List<Pincode> nearByPincodes = pincodeService.getPincodes(pincode.getCity());
		if (nearByPincodes != null && nearByPincodes.size() > 0) {
			List<PincodeRegionZone> pincodeRegionZoneList = new ArrayList<PincodeRegionZone>();
			for (Pincode nearByPin : nearByPincodes) {
				if (nearByPin.equals(pincode)) {
					continue;
				}
				List<PincodeRegionZone> pincodeRegionZoneListDb = getPincodeRegionZoneList(nearByPin);
				if ((pincodeRegionZoneListDb != null) && (pincodeRegionZoneListDb.size() > 0)) {
					pincodeRegionZoneList = pincodeRegionZoneListDb;
					break;
				}
			}
			if (pincodeRegionZoneList.size() > 0) {
				recordsSaved = pincodeRegionZoneDao.assignPincodeRegionZoneToPincode(pincode, pincodeRegionZoneList);
			}
		}
		return recordsSaved;
	}


	public PincodeRegionZone save(PincodeRegionZone pincodeRegionZone) {
		return (PincodeRegionZone) pincodeRegionZoneDao.save(pincodeRegionZone);
	}

	public void saveOrUpdate(PincodeRegionZone pincodeRegionZone) {
		pincodeRegionZoneDao.saveOrUpdate(pincodeRegionZone);
	}

}