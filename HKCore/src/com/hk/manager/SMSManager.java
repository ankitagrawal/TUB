package com.hk.manager;

import java.util.HashMap;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.impl.service.SMSService;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: May 28, 2011
 * Time: 11:51:32 AM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("unchecked")
@Component
public class SMSManager {
  private static Logger logger = LoggerFactory.getLogger(SMSManager.class);

  private  SMSService smsService;

  @Autowired
  public SMSManager(SMSService smsService) {
    this.smsService = smsService;
  }

  public boolean sendSMS(String message, String mobile) {
    return smsService.sendSMS(message, mobile);
  }

  
public boolean sendDiscountCoupon(String name, String mobile, String coupon) {
    HashMap valuesMap = new HashMap();
    valuesMap.put("name", name);
    valuesMap.put("coupon", coupon);

    return smsService.sendSMSUsingTemplate(mobile, SMSManager.SMSTemplateConstants.discountCouponSMS, valuesMap);
  }

	public boolean sendOrderPlacedSMS(Order order) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("order", order);
		valuesMap.put("payment", order.getPayment());

		return smsService.sendSMSUsingTemplate(order.getAddress().getPhone(), SMSTemplateConstants.orderPlacedSMS, valuesMap);
	}

	public boolean sendOrderConfirmedSMS(Order order) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("order", order);

		return smsService.sendSMSUsingTemplate(order.getAddress().getPhone(), SMSTemplateConstants.orderConfirmedSMS, valuesMap);
	}

	public boolean sendOrderShippedSMS(ShippingOrder shippingOrder) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("shippingOrder", shippingOrder);
		valuesMap.put("shipment", shippingOrder.getShipment());

		if (shippingOrder.getBaseOrder().getOrderStatus().getId().equals(EnumOrderStatus.Shipped.getId())) {
			return smsService.sendSMSUsingTemplate(shippingOrder.getBaseOrder().getAddress().getPhone(), SMSManager.SMSTemplateConstants.orderShippedSMS, valuesMap);
		} else {
			return smsService.sendSMSUsingTemplate(shippingOrder.getBaseOrder().getAddress().getPhone(), SMSTemplateConstants.orderPartialShippedSMS, valuesMap);
		}
	}

	public boolean sendOrderDeliveredSMS(ShippingOrder shippingOrder) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("shippingOrder", shippingOrder);
		valuesMap.put("shipment", shippingOrder.getShipment());

		if (shippingOrder.getBaseOrder().getOrderStatus().getId().equals(EnumOrderStatus.Delivered.getId())) {
			return smsService.sendSMSUsingTemplate(shippingOrder.getBaseOrder().getAddress().getPhone(), SMSTemplateConstants.orderDeliveredSMS, valuesMap);
		} else {
			return smsService.sendSMSUsingTemplate(shippingOrder.getBaseOrder().getAddress().getPhone(), SMSTemplateConstants.shippingOrderDeliveredSMS, valuesMap);
		}
	}

	public static class SMSTemplateConstants {
    public static final String orderPlacedSMS = "/orderPlacedSMS.ftl";
    public static final String orderConfirmedSMS = "/orderConfirmedSMS.ftl";
    public static final String orderPartialShippedSMS = "/orderPartialShippedSMS.ftl";
    public static final String orderShippedSMS = "/orderShippedSMS.ftl";
    public static final String shippingOrderDeliveredSMS = "/shippingOrderDeliveredSMS.ftl";
    public static final String orderDeliveredSMS = "/orderDeliveredSMS.ftl";
    public static final String offerSMS = "/offerSMS.ftl";
    public static final String discountCouponSMS = "/discountCouponSMS.ftl";
  }
}
