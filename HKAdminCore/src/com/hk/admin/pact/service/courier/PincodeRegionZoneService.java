package com.hk.admin.pact.service.courier;

import com.hk.domain.courier.PincodeRegionZone;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.domain.core.Pincode;
import com.hk.domain.warehouse.Warehouse;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Dec 26, 2012
 * Time: 12:00:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PincodeRegionZoneService {

public List<PincodeRegionZone> getSortedRegionList(List<Courier> courierList, Pincode pincode, Warehouse warehouse);

    public List<PincodeRegionZone> getApplicableRegionList(List<Courier> courierList, Pincode pincode, Warehouse warehouse);

    public PincodeRegionZone getPincodeRegionZone(CourierGroup courierGroup, Pincode pincode, Warehouse warehouse);

     public List<PincodeRegionZone> getPincodeRegionZoneList(CourierGroup courierGroup, Pincode pincode, Warehouse warehouse);

	public int assignPincodeRegionZoneToPincode(Pincode pincode);

	public List<PincodeRegionZone> getPincodeRegionZoneList(Pincode pincode);

	public PincodeRegionZone save(PincodeRegionZone pincodeRegionZone);

	public void saveOrUpdate(PincodeRegionZone pincodeRegionZone);
}
