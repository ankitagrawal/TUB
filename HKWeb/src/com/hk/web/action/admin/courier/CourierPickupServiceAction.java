package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;

import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyPickupService;
import com.hk.admin.factory.courier.thirdParty.ThirdPartyCourierServiceFactory;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.CustomDateTypeConvertor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.validation.Validate;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Nov 29, 2012
 * Time: 12:58:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CourierPickupServiceAction extends BaseAction {
	private static Logger logger = LoggerFactory.getLogger(CourierPickupServiceAction.class);
	private Date pickupDate;
	private Long courierId;
	private String shippingOrderId;
	private static final int maxPossibleDays = 14;
	boolean exceededPolicyLimit;


	@Autowired
	ShippingOrderService shippingOrderService;

	@DefaultHandler
	public Resolution pre() {
		ShippingOrder shippingOrder = shippingOrderService.findByGatewayOrderId(shippingOrderId);
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
			//targetDay = deliveryDay + 14;
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
		return new ForwardResolution("/pages/admin/reversePickupService.jsp");
	}

	public Resolution save() {
		if (shippingOrderId != null && pickupDate != null) {

			ThirdPartyPickupService thirdPartyPickupService = ThirdPartyCourierServiceFactory.getThirdPartyPickupService(courierId);
			ShippingOrder shippingOrder = shippingOrderService.findByGatewayOrderId(shippingOrderId);
			thirdPartyPickupService.createPickupRequest(shippingOrder, pickupDate);
		}
		return new ForwardResolution("/pages/admin/reversePickupService.jsp");
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	@Validate(converter = CustomDateTypeConvertor.class)
	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public Long getCourierId() {
		return courierId;
	}

	public void setCourierId(Long courierId) {
		this.courierId = courierId;
	}

	public String getShippingOrderId() {
		return shippingOrderId;
	}

	public void setShippingOrderId(String shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}

	public boolean isExceededPolicyLimit() {
		return exceededPolicyLimit;
	}

	public void setExceededPolicyLimit(boolean exceededPolicyLimit) {
		this.exceededPolicyLimit = exceededPolicyLimit;
	}
}
