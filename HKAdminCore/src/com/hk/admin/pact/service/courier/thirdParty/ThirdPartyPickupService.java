package com.hk.admin.pact.service.courier.thirdParty;

import com.hk.constants.courier.EnumCourier;
import com.hk.domain.order.ShippingOrder;

import java.util.List;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Nov 29, 2012
 * Time: 1:22:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ThirdPartyPickupService {
	 public List<Long> integratedCouriers = Arrays.asList(new Long[]{EnumCourier.FedEx.getId(),EnumCourier.FedEx_Surface.getId()});

	public List<String> createPickupRequest(ShippingOrder shippingOrder, Date date);
	 
}
