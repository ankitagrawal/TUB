package com.hk.web.action.admin.courier;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.constants.core.PermissionConstants;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.error.AdminPermissionAction;
import com.akube.framework.stripes.action.BaseAction;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
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
	private static final int maxPossibleDays = 14;

	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	ReverseOrderService reverseOrderService;

	@DefaultHandler
	public Resolution pre(){

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
			int interimDays = presentDay - 1;
			int interimSum = interimDays + (31 - deliveryDay);
			diff = interimSum;
		}

		if (diff <= maxPossibleDays) {
			exceededPolicyLimit = false;
		} else {
			exceededPolicyLimit = true;

		}
	  return new ForwardResolution("/pages/admin/createReverseOrder.jsp");
	}

	public Resolution submit(){		
		ReverseOrder reverseOrder = reverseOrderService.createReverseOrder(shippingOrder);
		reverseOrderService.createReverseLineItems(reverseOrder, itemMap);
		return new RedirectResolution(ReversePickupCourierAction.class).addParameter("reverseOrderId", reverseOrder.getId());
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
}

