package com.hk.admin.pact.dao.courier;

import java.util.List;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.domain.courier.PincodeRegionZone;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 5/25/12
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PincodeRegionZoneDao extends BaseDao {

    public List<PincodeRegionZone> getSortedRegionList(List<Courier> courierList, Pincode pincode, Warehouse warehouse);

    public List<PincodeRegionZone> getApplicableRegionList(List<Courier> courierList, Pincode pincode, Warehouse warehouse);

    public PincodeRegionZone getPincodeRegionZone(CourierGroup courierGroup, Pincode pincode, Warehouse warehouse);

    }
