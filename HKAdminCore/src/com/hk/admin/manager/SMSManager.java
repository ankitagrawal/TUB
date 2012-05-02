package com.hk.admin.manager;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.user.Address;
import com.hk.service.impl.SMSService;

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

   public boolean sendSMSUsingTemplate(String message, Address address) {
    HashMap valuesMap = new HashMap();
    valuesMap.put("address", address);

    return smsService.sendSMSUsingTemplate(message, SMSManager.SMSTemplateConstants.offerSMS, valuesMap, address);
  }

  public static class SMSTemplateConstants {
    public static final String offerSMS = "/offerSMS.ftl";
    public static final String discountCouponSMS = "/discountCouponSMS.ftl";
  }
}
