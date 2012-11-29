package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.admin.factory.courier.thirdParty.ThirdPartyAwbServiceFactory;
import org.springframework.stereotype.Component;
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
	private static Logger logger = LoggerFactory.getLogger(CreateUpdateCourierPricingAction.class);
	private Date pickupDate;
	private String courierName;
	private String shippingOrderId;

	Resolution save(){
		ThirdPartyAwbService thirdPartyAwbService = ThirdPartyAwbServiceFactory.getThirdPartyAwbService(courierId);
        thirdPartyAwbService.
		return new ForwardResolution("");
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}
}
