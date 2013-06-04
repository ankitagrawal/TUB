package com.hk.util;

import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;

/*
 * User: Pratham
 * Date: 28/05/13  Time: 11:43
*/
public class SOFirewall {

    private static Double tolerancePaymentAmount = 0.01;

    public static boolean isAmountMismatch(Order order) {
        Double paymentAmount = order.getPayment().getAmount();
        Double orderAmount = order.getAmount();
        Double tolerancePayableAmount = paymentAmount * tolerancePaymentAmount;
        return Math.abs(paymentAmount - orderAmount) > tolerancePayableAmount;
    }

    private static final Double percentageOfSOAmount = 0.5D;
    private static final Double maxShippingCharges = 5000D;
    public static final Double minAllowedShippingCharges = 150D;

    public static Double calculateCutoffAmount(ShippingOrder shippingOrder) {
        Double shippingOrderAmount = 0D;
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            shippingOrderAmount += lineItem.getHkPrice();
        }
        Double cutOffShippingCharges = shippingOrderAmount * percentageOfSOAmount;
        cutOffShippingCharges = cutOffShippingCharges > maxShippingCharges ? maxShippingCharges : cutOffShippingCharges;
        return cutOffShippingCharges;
    }

}
