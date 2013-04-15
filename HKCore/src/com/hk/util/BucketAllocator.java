package com.hk.util;

import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.queue.EnumBucket;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.shippingOrder.ShippingOrderCategory;

import java.util.ArrayList;
import java.util.List;

/*
 * User: Pratham
 * Date: 15/04/13  Time: 16:03
*/
public class BucketAllocator {

    public static List<EnumBucket> allocateBuckets(ShippingOrder shippingOrder) {

        List<EnumBucket> actionableBuckets = new ArrayList<EnumBucket>();

        //decide on the basis of payment status, whether the bucket lies with customer support or category team
        Payment payment = shippingOrder.getBaseOrder().getPayment();
        if(!EnumPaymentStatus.getEscalablePaymentStatusIds().contains(payment.getPaymentStatus().getId())){
            //assign on the basis of payment mode
            if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId())) {
                if (payment.getPaymentMode().getId().equals(EnumPaymentMode.COD.getId())) {
                    actionableBuckets.add(EnumBucket.Cod_Confirmation);
                } else if (EnumPaymentMode.getAuthorizationPendingPaymentModes().contains(payment.getPaymentMode().getId())) {
                    actionableBuckets.add(EnumBucket.Cheque_Cash_Neft);
                }
            }
            //for online error cases
            if (EnumPaymentMode.ONLINE_PAYMENT.getId().equals(payment.getPaymentMode().getId())) {
                if (EnumPaymentStatus.getOnlinePaymentErrorStatuses().contains(payment.getPaymentStatus())) {
                    actionableBuckets.add(EnumBucket.Online_Payment_Disputes);
                }
            }
        } else {

            // now that its decided that its category call, decide which category buckets is the order applicable
            List<String> categoryNames = new ArrayList<String>();
            for (ShippingOrderCategory shippingOrderCategory : shippingOrder.getShippingOrderCategories()) {
                categoryNames.add(shippingOrderCategory.getCategory().getName());
            }
            actionableBuckets.addAll(EnumBucket.findByName(categoryNames));

            //assign buckets on the basis of shipping_order_properties
            if (shippingOrder.containsJitProducts()) {
                actionableBuckets.add(EnumBucket.Jit);
            }
            if (shippingOrder.isDropShipping()) {
                actionableBuckets.add(EnumBucket.DropShip);
            }
            if (shippingOrder.isServiceOrder()) {
                actionableBuckets.add(EnumBucket.Services);
            }

            //assign buckets based on different issues
            if ((!shippingOrder.isDropShipping() || !shippingOrder.isServiceOrder()) && shippingOrder.getShipment() == null) {
                actionableBuckets.add(EnumBucket.Dispatch_Issues);
            }

        }

        return actionableBuckets;
    }

}
