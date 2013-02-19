package com.hk.web.action.admin.courier;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;

import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.courier.EnumPickupStatus;
import com.hk.constants.courier.EnumAdviceProposed;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.web.action.core.accounting.AccountingInvoiceAction;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Dec 5, 2012
 * Time: 11:48:52 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ReverseOrdersManageAction extends BasePaginatedAction{

	private List<ReverseOrder> orderRequestsList;
	private Long orderRequestId;
	Page orderRequestsPage;
    private Integer defaultPerPage = 30;

	private Long pickupStatusId;
	private Long reconciliationStatusId;
	private String shippingOrderId;
	private String adviceId;

	@Autowired
	ReverseOrderService reverseOrderService;

	@Autowired
	ShippingOrderService shippingOrderService;

	@DefaultHandler
	public Resolution pre() {
		orderRequestsPage = reverseOrderService.getPickupRequestsByStatuses(shippingOrderId, pickupStatusId, reconciliationStatusId, getPageNo(), getPerPage());
		orderRequestsList = orderRequestsPage.getList();
		return new ForwardResolution("/pages/admin/reverseOrderList.jsp");
	}

	public Resolution markPicked(){
		if(orderRequestId != null){
			ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(orderRequestId);
			reverseOrder.getCourierPickupDetail().setPickupStatus(EnumPickupStatus.CLOSE.asPickupStatus());
			reverseOrderService.save(reverseOrder);
		}
		return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
	}

	public Resolution markReceived(){
		if(orderRequestId != null){
			ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(orderRequestId);
			reverseOrder.setReceivedDate(new Date());
			reverseOrderService.save(reverseOrder);
		}
		return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
	}

	public Resolution markReconciled(){
		if(orderRequestId != null){
			ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(orderRequestId);
			reverseOrder.setReconciliationStatus(EnumReconciliationStatus.DONE.asReconciliationStatus());
			reverseOrderService.save(reverseOrder);
		}
		return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
	}

	public Resolution reschedulePickup(){
		if(orderRequestId != null){
			return new RedirectResolution(ReversePickupCourierAction.class).addParameter("reverseOrderId", orderRequestId);
		} else{
			return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
		}
	}

	public Resolution adviceProposed(){
		if(orderRequestId != null){
			ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(orderRequestId);
			reverseOrder.setActionProposed(EnumAdviceProposed.valueOf(adviceId).getName());
			reverseOrderService.save(reverseOrder);
		}
		return new RedirectResolution(ReverseOrdersManageAction.class).addParameter("shippingOrderId", shippingOrderId);
	}

	public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return orderRequestsPage == null ? 0 : orderRequestsPage.getTotalPages();
    }

    public int getResultCount() {
        return orderRequestsPage == null ? 0 : orderRequestsPage.getTotalResults();
    }

	 public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("pickupStatusId");
        params.add("reconciliationStatusId");
		params.add("shippingOrderId");
        return params;
    }

	public List<ReverseOrder> getOrderRequestsList() {
		return orderRequestsList;
	}

	public void setOrderRequestsList(List<ReverseOrder> orderRequestsList) {
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

	public Long getOrderRequestId() {
		return orderRequestId;
	}

	public void setOrderRequestId(Long orderRequestId) {
		this.orderRequestId = orderRequestId;
	}

	public String getShippingOrderId() {
		return shippingOrderId;
	}

	public void setShippingOrderId(String shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}

	public String getAdviceId() {
		return adviceId;
	}

	public void setAdviceId(String adviceId) {
		this.adviceId = adviceId;
	}
}
