package com.hk.web.action.admin.courier;

import net.sourceforge.stripes.action.*;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.Keys;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.web.action.admin.order.search.SearchShippingOrderAction;
import com.akube.framework.stripes.action.BaseAction;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Feb 6, 2013
 * Time: 7:24:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.CREATE_REVERSE_PICKUP_ORDER}, authActionBean = AdminPermissionAction.class)
@Component
public class CreateReverseOrderAction extends BaseAction {

	private ShippingOrder shippingOrder;
	private Map<LineItem, Long> itemMap = new HashMap<LineItem, Long>();

	private boolean exceededPolicyLimit;

	@Value("#{hkEnvProps['" + Keys.Env.maxReturnPolicyDays + "']}")
	private int 	maxReturnPolicyDays;

	private String returnOrderReason;

	private String reverseOrderType;

	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	ReverseOrderService reverseOrderService;

	@DefaultHandler
	public Resolution pre(){
		if (shippingOrder.getShipment().getDeliveryDate() != null) {
			if (reverseOrderService.getReverseOrderByShippingOrderId(shippingOrder.getId()) == null) {
				exceededPolicyLimit = false;
				Calendar todayCal = Calendar.getInstance(); //current date and time
				int presentDay = todayCal.get(Calendar.DATE);
				int diff;

				Date deliveryDate = shippingOrder.getShipment().getDeliveryDate();
				Calendar deliveryCal = Calendar.getInstance();
				deliveryCal.setTime(deliveryDate);
				int deliveryDay = deliveryCal.get(Calendar.DATE);

				if (todayCal.get(Calendar.MONTH) == deliveryCal.get(Calendar.MONTH)) {
					diff = presentDay - deliveryDay;
				} else {
					int interimSum = presentDay + (31 - deliveryDay);
					diff = interimSum;
				}


				if (diff <= maxReturnPolicyDays) {
					exceededPolicyLimit = false;
				} else {
					exceededPolicyLimit = true;
				}
				return new ForwardResolution("/pages/admin/createReverseOrder.jsp");
			} else {
				addRedirectAlertMessage(new SimpleMessage("Reverse Order has been already created for this SO"));
				return new RedirectResolution(SearchShippingOrderAction.class,"searchShippingOrder").addParameter("shippingOrderGatewayId", shippingOrder.getGatewayOrderId());
			}
		} else {
			addRedirectAlertMessage(new SimpleMessage("Delivery date is not found"));
			return new RedirectResolution(SearchShippingOrderAction.class,"searchShippingOrder").addParameter("shippingOrderGatewayId", shippingOrder.getGatewayOrderId());
		}
	}

	public Resolution submit(){
		if (reverseOrderService.getReverseOrderByShippingOrderId(shippingOrder.getId()) == null) {
			ReverseOrder reverseOrder = reverseOrderService.createReverseOrder(shippingOrder, returnOrderReason, reverseOrderType);
			reverseOrderService.createReverseLineItems(reverseOrder, itemMap);
			shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Reverse_Pickup_Initiated, null, returnOrderReason);
			shippingOrder.setOrderStatus(EnumShippingOrderStatus.SO_ReversePickup_Initiated.asShippingOrderStatus());
			shippingOrderService.save(shippingOrder);
			return new RedirectResolution(ReversePickupCourierAction.class).addParameter("reverseOrderId", reverseOrder.getId());
		} else {
			addRedirectAlertMessage(new SimpleMessage("Reverse Order has been created for this Shipping order"));
			return new RedirectResolution(CreateReverseOrderAction.class).addParameter("shippingOrder", shippingOrder);
		}
	}

	public ShippingOrder getShippingOrder() {
		return shippingOrder;
	}

	public void setShippingOrder(ShippingOrder shippingOrder) {
		this.shippingOrder = shippingOrder;
	}

	public boolean isExceededPolicyLimit() {
		return exceededPolicyLimit;
	}

	public void setExceededPolicyLimit(boolean exceededPolicyLimit) {
		this.exceededPolicyLimit = exceededPolicyLimit;
	}

	public Map<LineItem, Long> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<LineItem, Long> itemMap) {
		this.itemMap = itemMap;
	}

	public String getReturnOrderReason() {
		return returnOrderReason;
	}

	public void setReturnOrderReason(String returnOrderReason) {
		this.returnOrderReason = returnOrderReason;
	}

	public String getReverseOrderType() {
		return reverseOrderType;
	}

	public void setReverseOrderType(String reverseOrderType) {
		this.reverseOrderType = reverseOrderType;
	}
}


