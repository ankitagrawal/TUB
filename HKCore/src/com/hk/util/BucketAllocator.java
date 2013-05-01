package com.hk.util;

import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.queue.EnumActionTask;
import com.hk.constants.queue.EnumBucket;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.queue.Bucket;
import com.hk.domain.shippingOrder.ShippingOrderCategory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                if (EnumPaymentStatus.getOnlinePaymentErrorStatusIds().contains(payment.getPaymentStatus().getId())) {
                    actionableBuckets.add(EnumBucket.Online_Payment_Disputes);
                }
            }
        } else {

            // now that its decided that its category call, decide which category buckets is the order applicable
            Set<String> categoryNames = new HashSet<String>();
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

    public static EnumActionTask listCurrentActionTask(List<EnumBucket> buckets){

        if(buckets.contains(EnumBucket.Cod_Confirmation) || buckets.contains(EnumBucket.Cheque_Cash_Neft)){
            return EnumActionTask.Payment_Confirmation;
        }
        if(buckets.contains(EnumBucket.Online_Payment_Disputes)){
            return EnumActionTask.Online_Authorization;
        }
        if(buckets.contains(EnumBucket.Dispatch_Issues)){
            return EnumActionTask.Create_Shipment;
        }
        if(buckets.contains(EnumBucket.Jit) || buckets.contains(EnumBucket.DropShip)){
            return EnumActionTask.Create_PO;
        }

        return null;
    }

}
