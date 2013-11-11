package com.hk.web.action.admin.shippingOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.admin.queue.ActionAwaitingQueueAction;
import com.hk.web.action.error.AdminPermissionAction;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;
import org.apache.commons.lang.StringUtils;

public class ShippingOrderUtilityAction extends BasePaginatedAction {

	private static Logger logger = LoggerFactory.getLogger(ShippingOrderUtilityAction.class);

	@Autowired
	ShippingOrderService ShippingOrderService;
	@Autowired
	ShippingOrderService shippingOrderService;

	private String gatewayOrderIds;
	private Date startDate;
	private Date endDate;
	private List<ShippingOrder> shippingOrders;
	private List<ShippingOrder> shippingOrderMarked;
	private Integer defaultPerPage = 20;
	private Page shippingOrderPage;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/admin/shippingOrder/soValidationAndOtherUtility.jsp");
	}

	public Resolution searchSO() {
		shippingOrders = new ArrayList<ShippingOrder>();
		List<ShippingOrderStatus> shippingOrderStatus = new ArrayList<ShippingOrderStatus>();
		if (StringUtils.isNotBlank(gatewayOrderIds)) {
			String[] orderArray = gatewayOrderIds.split(",");
			for (String gatewayOrderId : orderArray) {
				ShippingOrder shippingOrder = ShippingOrderService.findByGatewayOrderId(StringUtils.deleteWhitespace(gatewayOrderId));
				shippingOrders.add(shippingOrder);
			}
		} else if (startDate != null && endDate != null) {
			ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
			shippingOrderSearchCriteria.setPaymentStartDate(startDate).setPaymentEndDate(endDate);
			shippingOrderSearchCriteria.setSortByDispatchDate(false);
			shippingOrderStatus.add(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus());
			shippingOrderStatus.add(EnumShippingOrderStatus.SO_Ready_For_Validation.asShippingOrderStatus());
			shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatus);

			shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, 1, 1000);
			List<ShippingOrder> shippingOrdersList = shippingOrderPage.getList();
			for (ShippingOrder shippingOrder : shippingOrdersList) {
				shippingOrders.add(shippingOrder);
			}
		}
		return new RedirectResolution(ShippingOrderUtilityAction.class).addParameter("shippingOrders", shippingOrders);
	}

	@Secure(hasAnyPermissions = { PermissionConstants.UPDATE_ACTION_QUEUE }, authActionBean = AdminPermissionAction.class)
	public Resolution validate() {
		if (!shippingOrderMarked.isEmpty()) {
			for (ShippingOrder shippingOrder : shippingOrderMarked) {
				shippingOrderService.validateShippingOrderAB(shippingOrder);
			}
		} else {
			addRedirectAlertMessage(new SimpleMessage("Please select a SO for validation"));
		}
		addRedirectAlertMessage(new SimpleMessage("Selected Shipping Orders have been validated"));
		return new RedirectResolution(ActionAwaitingQueueAction.class);
	}

	public String getGatewayOrderIds() {
		return gatewayOrderIds;
	}

	public void setGatewayOrderIds(String gatewayOrderIds) {
		this.gatewayOrderIds = gatewayOrderIds;
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

	public List<ShippingOrder> getShippingOrders() {
		return shippingOrders;
	}

	public void setShippingOrders(List<ShippingOrder> shippingOrders) {
		this.shippingOrders = shippingOrders;
	}

	public List<ShippingOrder> getShippingOrderMarked() {
		return shippingOrderMarked;
	}

	public void setShippingOrderMarked(List<ShippingOrder> shippingOrderMarked) {
		this.shippingOrderMarked = shippingOrderMarked;
	}

	public int getPerPageDefault() {
		return defaultPerPage;
	}

	public int getPageCount() {
		return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalPages();
	}

	public int getResultCount() {
		return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalResults();
	}

	public Integer getDefaultPerPage() {
		return defaultPerPage;
	}

	public void setDefaultPerPage(Integer defaultPerPage) {
		this.defaultPerPage = defaultPerPage;
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		// params.add("SkuItemStatus");
		return params;
	}
}
