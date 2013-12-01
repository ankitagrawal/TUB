package com.hk.web.action.admin.courier;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.ZoneService;
import com.hk.domain.courier.Zone;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User:Tarun
 * Date: May 6, 2014
 * Time: 12:33:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ZoneAction extends BaseAction{

	@Autowired
	ZoneService zoneService;

	private List<Zone> zoneList = new ArrayList<Zone>();
	private Zone zone;

	@DefaultHandler
	public Resolution pre() {
		zoneList = getZoneService().getAllZones();
		return new ForwardResolution("/pages/admin/courier/zoneList.jsp");
	}


	public Resolution saveZone() {
		if(zone != null){
			zone = getZoneService().saveZone(zone);
			addRedirectAlertMessage(new SimpleMessage("Zone saved !"));
		}
		else{
			addRedirectAlertMessage(new SimpleMessage(" Unable to save zone !!"));
		}
		return new RedirectResolution(ZoneAction.class);
	}

	public Resolution editZone() {
		return new ForwardResolution("/pages/editZone.jsp");
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public List<Zone> getZoneList() {
		return zoneList;
	}

	public void setZoneList(List<Zone> zoneList) {
		this.zoneList = zoneList;
	}

	public ZoneService getZoneService() {
		return zoneService;
	}
}