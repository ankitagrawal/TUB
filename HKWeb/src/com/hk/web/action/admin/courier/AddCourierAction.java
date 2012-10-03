package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.gson.JsonUtils;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.web.HealthkartResponse;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Sep 14, 2012
 * Time: 12:33:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class AddCourierAction extends BaseAction {

	@Autowired
	CourierService courierService;
	@Autowired
	CourierGroupService courierGroupService;

	private List<Courier> courierList;

	private List<CourierGroup> courierGroupList;

	//	@Validate(required = true, on = "saveCourier assignCourierGroup getCourierGroupForCourier")
	private Courier courier;
	//	@Validate(required = true, on = "addNewCourierGroup assignCourierGroup")
	private CourierGroup courierGroup;

	@DefaultHandler
	public Resolution pre() {
		courierList = courierService.getAllCouriers();
		courierGroupList = courierGroupService.getAllCourierGroup();
		return new ForwardResolution("/pages/addCourier.jsp");
	}

	public Resolution saveCourier() {
		if (courier != null) {
			if (courierService.getCourierByName(courier.getName().trim()) != null) {
				addRedirectAlertMessage(new SimpleMessage("Courier With same name already exist"));
			} else {
				courierService.save(courier);
				addRedirectAlertMessage(new SimpleMessage("Courier Saved"));
			}
		}

		return pre();
	}


	public Resolution addNewCourierGroup() {
		if (courierGroup != null) {
			if ((courierGroupService.getByName(courierGroup.getName().trim())) != null) {
				addRedirectAlertMessage(new SimpleMessage("Courier Group already exist"));
			} else {
				courierGroupService.save(courierGroup);
				addRedirectAlertMessage(new SimpleMessage("Courier Group Saved"));
			}
		}
		return pre();
	}


	public Resolution assignCourierGroup() {
		Set<Courier> couriergroupset = new HashSet<Courier>();
		couriergroupset.add(courier);
		courierGroup.setCouriers(couriergroupset);
		courierGroupService.save(courierGroup);
		addRedirectAlertMessage(new SimpleMessage("Courier Group Saved"));
		return pre();
	}

	@JsonHandler
	public Resolution getCourierGroupForCourier() {
		courierGroup = courier.getCourierGroup();
		HealthkartResponse healthkartResponse = null;
		if (courierGroup != null) {
			courierGroup = (CourierGroup) JsonUtils.hydrateHibernateObject(courierGroup);
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", courierGroup);
		} else {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Not assiged to any group");
		}
		return new JsonResolution(healthkartResponse);
	}


	public CourierGroup getCourierGroup() {
		return courierGroup;
	}

	public void setCourierGroup(CourierGroup courierGroup) {
		this.courierGroup = courierGroup;
	}

	public List<Courier> getCourierList() {
		return courierList;
	}

	public void setCourierList(List<Courier> courierList) {
		this.courierList = courierList;
	}

	public Courier getCourier() {
		return courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}


	public CourierService getCourierService() {
		return courierService;
	}

	public void setCourierService(CourierService courierService) {
		this.courierService = courierService;
	}

	public List<CourierGroup> getCourierGroupList() {
		return courierGroupList;
	}

	public void setCourierGroupList(List<CourierGroup> courierGroupList) {
		this.courierGroupList = courierGroupList;
	}
}
