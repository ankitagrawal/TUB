package com.hk.web.action.admin.courier;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.domain.courier.ReversePickup;
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

	private List<ReversePickup> pickupRequestsList;
	private ReversePickup pickupRequest;
	Page pickupRequestsPage;
    private Integer defaultPerPage = 30;

	private Long pickupStatusId;
	private Long reconciliationStatusId;
	private Long shippingOrderId;

	@Autowired
	ReversePickupDao reversePickupDao;

	@DefaultHandler
	public Resolution pre() {
		pickupRequestsPage = reversePickupDao.getPickupRequestsByStatuses(shippingOrderId, pickupStatusId, reconciliationStatusId, getPageNo(), getPerPage());
		pickupRequestsList = pickupRequestsPage.getList();
		return new ForwardResolution("/pages/admin/pickupRequestsList.jsp");
	}

	public Resolution markPicked(){
		if(pickupRequest != null){
			pickupRequest.setPickupStatus(EnumPickupStatus.CLOSE.asPickupStatus());
			reversePickupDao.save(pickupRequest);
		}
		return new RedirectResolution(PickupRequestsManageAction.class);
	}

	public Resolution markReconciled(){
		if(pickupRequest != null){
			pickupRequest.setReconciliationStatus(EnumReconciliationStatus.DONE.asReconciliationStatus());
			reversePickupDao.save(pickupRequest);
		}
		return new RedirectResolution(PickupRequestsManageAction.class);
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
        params.add("pickupStatus");
        params.add("reconciliationStatusId");
        return params;
    }

	public List<ReversePickup> getPickupRequestsList() {
		return pickupRequestsList;
	}

	public void setPickupRequestsList(List<ReversePickup> pickupRequestsList) {
		this.pickupRequestsList = pickupRequestsList;
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

	public ReversePickup getPickupRequest() {
		return pickupRequest;
	}

	public void setPickupRequest(ReversePickup pickupRequest) {
		this.pickupRequest = pickupRequest;
	}

	public Long getShippingOrderId() {
		return shippingOrderId;
	}

	public void setShippingOrderId(Long shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}
}
