package com.hk.util;

import com.hk.constants.core.EnumUserCodCalling;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.queue.EnumActionTask;
import com.hk.constants.queue.EnumBucket;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
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
        Order baseOrder = shippingOrder.getBaseOrder();
        Payment payment = baseOrder.getPayment();
        if(!EnumPaymentStatus.getEscalablePaymentStatusIds().contains(payment.getPaymentStatus().getId())){
            //assign on the basis of payment mode
            if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId())) {
                if (payment.getPaymentMode().getId().equals(EnumPaymentMode.COD.getId())) {
                    Integer userCodCallStatus = baseOrder.getUserCodCall() != null ? baseOrder.getUserCodCall().getCallStatus() : null;
                    if(userCodCallStatus != null){
                        if (userCodCallStatus.equals(EnumUserCodCalling.PENDING_WITH_KNOWLARITY.getId())) {
                            actionableBuckets.add(EnumBucket.Knowlarity);
                        } else if (userCodCallStatus.equals(EnumUserCodCalling.PENDING_WITH_EFFORT_BPO.getId())) {
                            actionableBuckets.add(EnumBucket.Effort_BPO);
                        } else {
                            actionableBuckets.add(EnumBucket.Cod_Confirmation);
                        }
                    }
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
            // actionableBuckets.addAll(getBucketsFromSOC(shippingOrder)); --> moved to an upper call chain

            if(SOFirewall.isAmountMismatch(baseOrder)){
               actionableBuckets.add(EnumBucket.Tech_Support);
            }

             //assign buckets on the basis of shipping_order_properties
            if (shippingOrder.containsJitProducts()) {
                actionableBuckets.add(EnumBucket.Jit);
            }
            if (shippingOrder.isDropShipping()) {
                actionableBuckets.add(EnumBucket.DropShip);
            }
            if (shippingOrder.isServiceOrder()) {
                actionableBuckets.add(EnumBucket.ServiceOrder);
            }

            //assign buckets based on different issues
            if ((!shippingOrder.isDropShipping() || !shippingOrder.isServiceOrder()) && shippingOrder.getShipment() == null) {
                actionableBuckets.add(EnumBucket.Dispatch_Issues);
            } else {
                Double estimatedShippingCharges = shippingOrder.getShipment().getEstmShipmentCharge();
                if (estimatedShippingCharges != null && estimatedShippingCharges > SOFirewall.minAllowedShippingCharges && estimatedShippingCharges >= SOFirewall.calculateCutoffAmount(shippingOrder)) {
                    actionableBuckets.add(EnumBucket.Dispatch_Issues);
                }
            }

        }

        return actionableBuckets;
    }

    public static List<EnumBucket> getBucketsFromSOC(ShippingOrder shippingOrder) {
        List<EnumBucket> categoryBuckets = new ArrayList<EnumBucket>();
        Set<String> categoryNames = new HashSet<String>();
        for (ShippingOrderCategory shippingOrderCategory : shippingOrder.getShippingOrderCategories()) {
            categoryNames.add(shippingOrderCategory.getCategory().getName());
        }
        categoryBuckets.addAll(EnumBucket.findByName(categoryNames));
        return categoryBuckets;
    }

    public static EnumActionTask listCurrentActionTask(List<Bucket> buckets){

        if(buckets.contains(EnumBucket.Cod_Confirmation.asBucket()) || buckets.contains(EnumBucket.Cheque_Cash_Neft.asBucket())){
            return EnumActionTask.Payment_Confirmation;
        }
        if(buckets.contains(EnumBucket.Online_Payment_Disputes.asBucket())){
            return EnumActionTask.Online_Authorization;
        }
        if(buckets.contains(EnumBucket.Dispatch_Issues.asBucket())){
            return EnumActionTask.Create_Shipment;
        }
        if(buckets.contains(EnumBucket.AD_HOC.asBucket()) || buckets.contains(EnumBucket.CM.asBucket())){
            return EnumActionTask.AD_HOC;
        }
        if(buckets.contains(EnumBucket.DropShip.asBucket())){
            return EnumActionTask.Process_DropShip;
        }
        if(buckets.contains(EnumBucket.Jit.asBucket())){
            return EnumActionTask.Create_PO;
        }
        if(buckets.contains(EnumBucket.Tech_Support.asBucket())){
            return EnumActionTask.Payment_Amount_Validation;
        }
        for (Bucket bucket : buckets) {
            if(EnumBucket.getCategoryBuckets().contains(bucket)){
                return EnumActionTask.Insufficient_Inventory;
            }
        }
        return EnumActionTask.AD_HOC;
    }

}
