package com.hk.admin.impl.service.courier;

import com.hk.admin.pact.service.courier.ZoneService;
import com.hk.domain.courier.Zone;
import com.hk.pact.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tarun
 * Date: May 6, 2013
 * Time: 2:18:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ZoneServiceImpl implements ZoneService{
	@Autowired
	BaseDao baseDao;

	@Override
	public List<Zone> getAllZones() {
		return getBaseDao().getAll(Zone.class);
	}

	@Override
	public Zone saveZone(Zone zone) {
		return (Zone)getBaseDao().save(zone);
	}

	@Override
	public Zone getZoneById(Long zoneId) {
		return getBaseDao().get(Zone.class, zoneId);
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}
}
