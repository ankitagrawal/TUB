package com.hk.util;


import com.hk.domain.order.ShippingOrder;

/*
 * User: Pratham
 * Date: 06/05/13  Time: 13:54
*/
public class ShippingCostCutOff {

    private static final Double percentageOfSOAmount = 0.3D;
    private static final Double maxShippingCharges = 5000D;

    public static Double calculateCutoffAmount(ShippingOrder shippingOrder) {
        Double shippingOrderAmount = shippingOrder.getAmount();
        Double cutOffShippingCharges = shippingOrderAmount * percentageOfSOAmount;
        cutOffShippingCharges = cutOffShippingCharges > maxShippingCharges ? maxShippingCharges : cutOffShippingCharges;
        return cutOffShippingCharges;
    }

}
