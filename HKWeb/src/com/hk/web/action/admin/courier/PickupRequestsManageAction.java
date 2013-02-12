package com.hk.web.action.admin.courier;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;

import com.hk.pact.dao.courier.ReversePickupDao;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.courier.EnumPickupStatus;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.HashSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Dec 5, 2012
 * Time: 11:48:52 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PickupRequestsManageAction extends BasePaginatedAction{

	private List<ReverseOrder> orderRequestsList;
	private ReverseOrder orderRequest;
	Page pickupRequestsPage;
    private Integer defaultPerPage = 30;

	private Long pickupStatusId;
	private Long reconciliationStatusId;
	private String shippingOrderId;

	@Autowired
	ReversePickupDao reversePickupDao;

	@DefaultHandler
	public Resolution pre() {
		pickupRequestsPage = reversePickupDao.getPickupRequestsByStatuses(shippingOrderId, pickupStatusId, reconciliationStatusId, getPageNo(), getPerPage());
		orderRequestsList = pickupRequestsPage.getList();
		return new ForwardResolution("/pages/admin/orderRequestsList.jsp");
	}

	public Resolution markPicked(){
		if(orderRequest != null){
			orderRequest.setPickupStatus(EnumPickupStatus.CLOSE.asPickupStatus());
			reversePickupDao.save(orderRequest);
		}
		return new RedirectResolution(PickupRequestsManageAction.class).addParameter("shippingOrderId", shippingOrderId);
	}

	public Resolution markReconciled(){
		if(orderRequest != null){
			orderRequest.setReconciliationStatus(EnumReconciliationStatus.DONE.asReconciliationStatus());
			reversePickupDao.save(orderRequest);
		}
		return new RedirectResolution(PickupRequestsManageAction.class).addParameter("shippingOrderId", shippingOrderId);
	}

	public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return pickupRequestsPage == null ? 0 : pickupRequestsPage.getTotalPages();
    }

    public int getResultCount() {
        return pickupRequestsPage == null ? 0 : pickupRequestsPage.getTotalResults();
    }

	 public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("pickupStatusId");
        params.add("reconciliationStatusId");
		params.add("shippingOrderId");
        return params;
    }

	public List<ReverseOrder> getPickupRequestsList() {
		return orderRequestsList;
	}

	public void setPickupRequestsList(List<ReverseOrder> orderRequestsList) {
		this.orderRequestsList = orderRequestsList;
	}

	public Long getPickupStatusId() {
		return pickupStatusId;
	}

	public void setPickupStatusId(Long pickupStatusId) {
		this.pickupStatusId = pickupStatusId;
	}

	public Long getReconciliationStatusId() {
		return reconciliationStatusId;
	}

	public void setReconciliationStatusId(Long reconciliationStatusId) {
		this.reconciliationStatusId = reconciliationStatusId;
	}

	public ReverseOrder getPickupRequest() {
		return orderRequest;
	}

	public void setPickupRequest(ReverseOrder orderRequest) {
		this.orderRequest = orderRequest;
	}

	public String getShippingOrderId() {
		return shippingOrderId;
	}

	public void setShippingOrderId(String shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}
}
