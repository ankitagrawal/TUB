package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.gson.JsonUtils;
import com.akube.framework.dao.Page;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.category.Category;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.web.HealthkartResponse;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.catalog.category.CategoryDao;

import java.util.*;

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
public class AddCourierAction extends BasePaginatedAction {

	@Autowired
	ProductDao productDao;

	@Autowired
	CategoryDao categoryDao;


	@Autowired
	CourierService courierService;
	@Autowired
	CourierGroupService courierGroupService;

	private List<Courier> courierList;

	private Courier courier;

	private CourierGroup courierGroup;

	private String courierName;

	Page courierPage;
	private Integer defaultPerPage = 30;

	private Boolean status;

	@DefaultHandler
	public Resolution pre() {
		courierPage = courierService.getCouriers(courierName, status, getPageNo(), getPerPage());
		List<Courier> courierListDb = courierPage.getList();
		courierList = courierListDb;
		if (courierGroup != null) {
			courierList = new ArrayList<Courier>();
			for (Courier courier : courierListDb) {
				if (courier.getCourierGroup() != null && (courier.getCourierGroup().equals(courierGroup))) {
					courierList.add(courier);
				}
			}
		}
		return new ForwardResolution("/pages/searchAndAddCourier.jsp");
	}


	public Resolution save() {

		Courier courierObj = courierService.getCourierByName(courierName);
		if (courierObj != null) {
			if (courier.getName() == null || (courier.getName() != null && (!(courierObj.getName().equalsIgnoreCase(courier.getName()))))) {
				addRedirectAlertMessage(new SimpleMessage("Courier Name Already exist"));
				return new ForwardResolution("/pages/searchAndAddCourier.jsp");
			}
		}
		if (courier.getId() != null) {
			if (courier.getCourierGroup() != null && courierGroup != courier.getCourierGroup()) {
				CourierGroup oldCourierGroup = courier.getCourierGroup();
				oldCourierGroup.getCouriers().remove(courierObj);
				courierGroupService.save(oldCourierGroup);
			}
		}
		courier.setName(courierName.trim());
		if (courierGroup != null) {
			courierGroup.getCouriers().add(courier);
			courierGroupService.save(courierGroup);
		}
		courierService.save(courier);
		addRedirectAlertMessage(new SimpleMessage("Courier Saved sucessfully"));
		return new RedirectResolution(AddCourierAction.class);
	}

	public Resolution editCourier() {
		courier = getCourier();
		return new ForwardResolution("/pages/courier.jsp");
	}

	public Resolution editCourierGroup() {
		courier = getCourier();
		return new ForwardResolution("/pages/courierGroup.jsp");
	}


	public Resolution saveGroup() {
			if ((courierGroupService.getByName(courierGroup.getName().trim())) != null) {
				addRedirectAlertMessage(new SimpleMessage("Courier Group already exist"));
				return new ForwardResolution("/pages/courierGroup.jsp");
			} else {
				courierGroupService.save(courierGroup);
				addRedirectAlertMessage(new SimpleMessage("Courier Group Saved")); 				
			}

	   return new RedirectResolution(AddCourierAction.class);
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

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public int getPerPageDefault() {
		return defaultPerPage;
	}

	public int getPageCount() {
		return courierPage == null ? 0 : courierPage.getTotalPages();
	}

	public int getResultCount() {
		return courierPage == null ? 0 : courierPage.getTotalResults();
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("courierName");
		params.add("courierGroup");
		return params;
	}

	public Boolean isStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
