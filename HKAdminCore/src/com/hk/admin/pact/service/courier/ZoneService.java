package com.hk.admin.pact.service.courier;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.domain.courier.PincodeRegionZone;
import com.hk.domain.courier.Zone;
import com.hk.domain.warehouse.Warehouse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Dec 26, 2012
 * Time: 12:00:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ZoneService {

	public List<Zone> getAllZones();

	public Zone saveZone(Zone zone);

	public Zone getZoneById(Long zoneId);
}