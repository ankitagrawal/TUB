package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;

import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyPickupService;
import com.hk.admin.factory.courier.thirdParty.ThirdPartyAwbServiceFactory;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;

import java.util.Date;

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
	private ShippingOrder shippingOrder;

	@Autowired
	ShippingOrderService shippingOrderService;

	Resolution save(){
		ThirdPartyPickupService thirdPartyPickupService = ThirdPartyAwbServiceFactory.getThirdPartyPickupService(courierId);
		shippingOrder = shippingOrderService.findByGatewayOrderId(shippingOrderId);
        thirdPartyPickupService.createPickupRequest(shippingOrder, pickupDate.toString());
		return new ForwardResolution("/pages/admin/reversePickupService.jsp");
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}
}
