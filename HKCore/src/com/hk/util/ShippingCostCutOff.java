package com.hk.util;


import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;

/*
 * User: Pratham
 * Date: 06/05/13  Time: 13:54
*/
public class ShippingCostCutOff {

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
