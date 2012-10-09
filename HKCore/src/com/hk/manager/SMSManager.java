package com.hk.manager;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.Address;
import com.hk.impl.service.SMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA. User: Ajeet Date: May 28, 2011 Time: 11:51:32 AM To change this template use File |
 * Settings | File Templates.
 */

@SuppressWarnings("unchecked")
@Component
public class SMSManager {
    private static Logger logger = LoggerFactory.getLogger(SMSManager.class);

    @Autowired
    SMSService            smsService;

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
	    if (order.isCOD()) {
           return smsService.sendSMSUsingTemplate(order.getAddress().getPhone(), SMSTemplateConstants.codOrderPlacedSMS, valuesMap);
        } else {
           return smsService.sendSMSUsingTemplate(order.getAddress().getPhone(), SMSTemplateConstants.orderPlacedSMS, valuesMap);
        }
    }

    public boolean sendOrderShippedSMS(ShippingOrder shippingOrder) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("shippingOrder", shippingOrder);
        valuesMap.put("shipment", shippingOrder.getShipment());

	    Order order = shippingOrder.getBaseOrder();
	    Address address = order.getAddress();
	    if (order.getOrderStatus().getId().equals(EnumOrderStatus.Shipped.getId())) {
		    if (order.isCOD()) {
			    return smsService.sendSMSUsingTemplate(address.getPhone(), SMSTemplateConstants.codOrderShippedSMS, valuesMap);
		    } else {
			    return smsService.sendSMSUsingTemplate(address.getPhone(), SMSTemplateConstants.orderShippedSMS, valuesMap);
		    }
	    } else {
		    if (order.isCOD()) {
			    return smsService.sendSMSUsingTemplate(address.getPhone(), SMSTemplateConstants.codOrderPartialShippedSMS, valuesMap);
		    } else {

			    return smsService.sendSMSUsingTemplate(address.getPhone(), SMSTemplateConstants.orderPartialShippedSMS, valuesMap);
		    }
	    }
    }

    public boolean sendOrderDeliveredSMS(ShippingOrder shippingOrder) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("shippingOrder", shippingOrder);
        valuesMap.put("shipment", shippingOrder.getShipment());

        if (shippingOrder.getBaseOrder().getOrderStatus().getId().equals(EnumOrderStatus.Delivered.getId())) {
            return smsService.sendSMSUsingTemplate(shippingOrder.getBaseOrder().getAddress().getPhone(), SMSTemplateConstants.orderDeliveredSMS, valuesMap);
        }
	    return false;
    }

    public static class SMSTemplateConstants {
	    
        public static final String orderPlacedSMS            = "/sms/orderPlacedSms.ftl";
        public static final String codOrderPlacedSMS         = "/sms/codOrderPlacedSms.ftl";
        public static final String orderPartialShippedSMS    = "/sms/orderPartialShippedSms.ftl";
        public static final String codOrderPartialShippedSMS = "/sms/codOrderPartialShippedSms.ftl";
        public static final String orderShippedSMS           = "/sms/orderShippedSms.ftl";
        public static final String codOrderShippedSMS        = "/sms/codOrderShippedSms.ftl";
        public static final String orderDeliveredSMS         = "/sms/orderDeliveredSms.ftl";

        public static final String offerSMS                  = "/offerSMS.ftl";
        public static final String discountCouponSMS         = "/discountCouponSMS.ftl";
    }
}
