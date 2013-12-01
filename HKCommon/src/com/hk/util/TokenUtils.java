package com.hk.util;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.order.Order;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.impl.service.LoadPropertyServiceImpl;

public class TokenUtils {

    public static String generateCouponCode(String couponCode, String endPart) {
        return couponCode + BaseUtils.getRandomStringTypable(6) + endPart;
    }

    public static String generateGuestLogin() {
        return BaseUtils.getRandomString(6) + BaseUtils.getCurrentTimestamp().getTime();
    }

    public static String generateTempToken() {
        return BaseUtils.getMD5Checksum(BaseUtils.getRandomString(6) + BaseUtils.getCurrentTimestamp().getTime());
    }

    // todo - do better than this, right now we're just using a random number generator
    public static String generateGatewayOrderId(Order order) {
        return order.getId() + "-" + (order.isReferredOrder() ? "R" : "") + BaseUtils.getRandomNumber(5);
    }
    
    public static String generateHybridGatewayOrderId(Order order) {
        return order.getId() + "-" + (order.isReferredOrder() ? "RB" : "B") + BaseUtils.getRandomNumber(5);
    }

    public static String generateReversePickupOrderId(ShippingOrder shippingOrder) {
        return BaseUtils.getRandomNumber(5) + "-" + "RP" + shippingOrder.getId();
    }

    public static String generateShippingOrderGatewayOrderId(ShippingOrder shippingOrder) {
        Order order = shippingOrder.getBaseOrder();
        String baseOrderGatewayOrderId[] = order.getGatewayOrderId().split("-");
        String prefix = "S";
        if (shippingOrder instanceof ReplacementOrder) {
            prefix = "RO";
        }
        return shippingOrder.getId() + "-" + prefix + baseOrderGatewayOrderId[1];

    }

    public static String generateImageSecret(String imageId) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("123").append(imageId).append("23ja");
        return BaseUtils.getMD5Checksum(stringBuffer.toString()).substring(10, 20);
    }

    public static String generateUserHash() {
        return BaseUtils.getMD5Checksum(BaseUtils.getCurrentTimestamp() + BaseUtils.getRandomString(10));
    }

    public static String generateDesignSecret(String id) {
        return BaseUtils.getMD5Checksum("23432" + id + "213");
    }

    public static String getTokenToUnsubscribeWommEmail(String email) {
        return BaseUtils.getMD5Checksum("2348kwbfdbskdjf" + email);
    }
}
