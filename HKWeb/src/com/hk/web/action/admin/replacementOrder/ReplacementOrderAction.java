package com.hk.web.action.admin.replacementOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hk.domain.order.ReplacementOrderReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.domain.order.ReplacementOrder;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.reverseOrder.ReverseLineItem;
import com.hk.helper.ReplacementOrderHelper;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 4, 2012
 * Time: 3:09:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.CREATE_REPLACEMENT_ORDER})
@Component
public class ReplacementOrderAction extends BaseAction {
	private static Logger logger = LoggerFactory.getLogger(ReplacementOrderAction.class);

	private Long shippingOrderId;
	private String gatewayOrderId;
	private ShippingOrder shippingOrder;
	private Boolean isRto;
	private String roComment;
	private List<LineItem> lineItems = new ArrayList<LineItem>();
	private List<ReverseLineItem> reverseLineItems = new ArrayList<ReverseLineItem>();
	private List<ReplacementOrder> replacementOrderList;
	private ReplacementOrderReason replacementOrderReason; 	
	private ReverseOrder reverseOrder;

	@Autowired
	private ShippingOrderService shippingOrderService;

	@Autowired
	private ReplacementOrderService replacementOrderService;

	@Autowired
	private LineItemDao lineItemDao;

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private ReverseOrderService reverseOrderService;


	private ReplacementOrder replacementOrder;


	@ValidationMethod(on = "searchShippingOrder")
	public void validateSearch() {
		if (shippingOrderId == null && gatewayOrderId == null) {
			getContext().getValidationErrors().add("1", new SimpleError("Please Enter a Search Parameter"));
		}
	}

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
	}

	public Resolution searchShippingOrder() {
		if (shippingOrderId != null) {
			shippingOrder = shippingOrderService.find(shippingOrderId);
		} else if (gatewayOrderId != null) {
			shippingOrder = shippingOrderService.findByGatewayOrderId(gatewayOrderId);
		}
		if (shippingOrder == null) {
			addRedirectAlertMessage(new SimpleMessage("No shipping order found  "));
			return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
		}
		for (LineItem lineItem : shippingOrder.getLineItems()) {
			lineItems.add(ReplacementOrderHelper.getLineItemForReplacementOrder(lineItem));
		}
		shippingOrderId = shippingOrder.getId();

		reverseOrder = reverseOrderService.getReverseOrderByShippingOrderId(shippingOrderId);
		if(reverseOrder != null){
			lineItems = new ArrayList<LineItem>();
			for(ReverseLineItem reverseLineItem: reverseOrder.getReverseLineItems()){
				LineItem replacementLineItem = ReplacementOrderHelper.getLineItemForReplacementOrder(reverseLineItem.getReferredLineItem());
				replacementLineItem.setQty(reverseLineItem.getReturnQty());
				lineItems.add(replacementLineItem);
			}
		}
		return new ForwardResolution("/pages/admin/createReplacementOrder.jsp").addParameter("shippingOrderId", shippingOrderId);
	}

	public Resolution createReplacementOrder() {
		if ((!shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.RTO_Initiated.getId()))
				&& (!shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_RTO.getId()))
				&& (!shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_Customer_Return_Replaced.getId()))
				&& (!shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_Customer_Appeasement.getId()))
				) {
			addRedirectAlertMessage(new SimpleMessage("Replacement order can be created only for status" + EnumShippingOrderStatus.RTO_Initiated.getName() +
					" OR <br />" + EnumShippingOrderStatus.SO_RTO.getName() +
					" OR <br />" + EnumShippingOrderStatus.SO_Customer_Return_Replaced.getName()  +
					" OR <br />" + EnumShippingOrderStatus.SO_Customer_Appeasement.getName()
			)


			);
			return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
		}

		if (replacementOrderReason == null) {
			addRedirectAlertMessage(new SimpleMessage("Please select a reason for creating a replacement order."));
			return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
		}

		int valid_item_flag = 0;
		for (LineItem lineItem : lineItems) {
			if (lineItem.getQty() > 0) {
				valid_item_flag++;
			}
			
			if(!lineItem.getCartLineItem().getProductVariant().getProduct().isJit()) {
				if (lineItem.getQty() != 0 && lineItem.getQty() >= inventoryService.getUnbookedInventoryInProcessingQueue(Arrays.asList(lineItem.getSku()))) {
					addRedirectAlertMessage(new SimpleMessage("Unable to create replacement order as " + lineItem.getCartLineItem().getProductVariant().getProduct().getName() + " out of stock."));
					return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
				}
				if (lineItem.getQty() < 0) {
					addRedirectAlertMessage(new SimpleMessage("The quantity of " + lineItem.getCartLineItem().getProductVariant().getProduct().getName() + " cannot be less than zero."));
					return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
				}
				if (lineItem.getQty() > getLineItemDao().getMatchingLineItemForDuplicateShippingOrder(lineItem, shippingOrder).getQty()) {
					addRedirectAlertMessage(new SimpleMessage("The quantity of " + lineItem.getCartLineItem().getProductVariant().getProduct().getName() + " cannot be more than original quantity."));
					return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
				}
			}
		}
		if (valid_item_flag == 0) {
			addRedirectAlertMessage(new SimpleMessage("The quantity of at least one item should be greater than 0"));
			return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
		}

		replacementOrder = replacementOrderService.createReplaceMentOrder(shippingOrder, lineItems, isRto, replacementOrderReason, roComment);
		if (replacementOrder == null) {
			addRedirectAlertMessage(new SimpleMessage("Unable to create replacement order."));
			return new RedirectResolution("/pages/admin/createReplacementOrder.jsp");
		}
		addRedirectAlertMessage(new SimpleMessage("The Replacement order created. New gateway order id: " + replacementOrder.getGatewayOrderId()));
		return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
	}

	@SuppressWarnings("unchecked")
    @JsonHandler
	public Resolution checkExistingReplacementOrder() {
		Map dataMap = new HashMap();
		if (shippingOrderId != null) {
			replacementOrderList = replacementOrderService.getReplacementOrderForRefShippingOrder(shippingOrderId);
			if (replacementOrderList.size() > 0) {
				HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Shipping order exist", dataMap);
				noCache();
				return new JsonResolution(healthkartResponse);
			}
		}
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Shipping order does not exists", dataMap);
		noCache();
		return new JsonResolution(healthkartResponse);
	}

	public Resolution searchReplacementOrders() {
		if (shippingOrderId != null) {
			shippingOrder = shippingOrderService.find(shippingOrderId);
		} else if (gatewayOrderId != null) {
			shippingOrder = shippingOrderService.findByGatewayOrderId(gatewayOrderId);
		}
		if (shippingOrder == null) {
			addRedirectAlertMessage(new SimpleMessage("No shipping order found  "));
			return new ForwardResolution("/pages/admin/searchReplacementOrder.jsp");
		}
		replacementOrderList = replacementOrderService.getReplacementOrderForRefShippingOrder(shippingOrder.getId());
		if (replacementOrderList.isEmpty()) {
			addRedirectAlertMessage(new SimpleMessage("No Replacement order for given shipping order"));
		}
		return new ForwardResolution("/pages/admin/searchReplacementOrder.jsp");
	}

	public Long getShippingOrderId() {
		return shippingOrderId;
	}

	public void setShippingOrderId(Long shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}

	public ShippingOrder getShippingOrder() {
		return this.shippingOrder;
	}

	public void setShippingOrder(ShippingOrder shippingOrder) {
		this.shippingOrder = shippingOrder;
	}

	public Boolean getIsRto() {
		return isRto;
	}

	public void setIsRto(Boolean rto) {
		isRto = rto;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public LineItemDao getLineItemDao() {
		return lineItemDao;
	}

	public ReplacementOrder getReplacementOrder() {
		return replacementOrder;
	}

	public void setReplacementOrder(ReplacementOrder replacementOrder) {
		this.replacementOrder = replacementOrder;
	}

	public List<ReplacementOrder> getReplacementOrderList() {
		return replacementOrderList;
	}

	public void setReplacementOrderList(List<ReplacementOrder> replacementOrderList) {
		this.replacementOrderList = replacementOrderList;
	}

	public String getGatewayOrderId() {
		return gatewayOrderId;
	}

	public void setGatewayOrderId(String gatewayOrderId) {
		this.gatewayOrderId = gatewayOrderId;
	}

	public ReplacementOrderReason getReplacementOrderReason() {
		return replacementOrderReason;
	}

	public void setReplacementOrderReason(ReplacementOrderReason replacementOrderReason) {
		this.replacementOrderReason = replacementOrderReason;
	}

	public String getRoComment() {
		return roComment;
	}

	public void setRoComment(String roComment) {
		this.roComment = roComment;
	}

	public ReverseOrder getReverseOrder() {
		return reverseOrder;
	}

	public void setReverseOrder(ReverseOrder reverseOrder) {
		this.reverseOrder = reverseOrder;
	}

	public List<ReverseLineItem> getReverseLineItems() {
		return reverseLineItems;
	}

	public void setReverseLineItems(List<ReverseLineItem> reverseLineItems) {
		this.reverseLineItems = reverseLineItems;
	}
}
