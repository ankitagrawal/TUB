package com.hk.web.action.report;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.reconciliation.AdminReconciliationService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 9/21/12
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReconciliationDiffReportAction extends BaseAction {

	@Autowired
	private AdminReconciliationService adminReconciliationService;

	private Long    shippingOrderId;
	private Long    baseOrderId;
	private String  gatewayOrderId;
	private String  baseGatewayOrderId;
	private Date    startDate;
	private Date    endDate;
	private String  paymentMode;


	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/admin/report/reconciliationDiffReport.jsp");
	}

	public Resolution findPaymentDifferenceInCODOrders() {
		return null;

	}

	public AdminReconciliationService getAdminReconciliationService() {
		return adminReconciliationService;
	}

	public void setAdminReconciliationService(AdminReconciliationService adminReconciliationService) {
		this.adminReconciliationService = adminReconciliationService;
	}

	public Long getShippingOrderId() {
		return shippingOrderId;
	}

	public void setShippingOrderId(Long shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}

	public Long getBaseOrderId() {
		return baseOrderId;
	}

	public void setBaseOrderId(Long baseOrderId) {
		this.baseOrderId = baseOrderId;
	}

	public String getGatewayOrderId() {
		return gatewayOrderId;
	}

	public void setGatewayOrderId(String gatewayOrderId) {
		this.gatewayOrderId = gatewayOrderId;
	}

	public String getBaseGatewayOrderId() {
		return baseGatewayOrderId;
	}

	public void setBaseGatewayOrderId(String baseGatewayOrderId) {
		this.baseGatewayOrderId = baseGatewayOrderId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
}
