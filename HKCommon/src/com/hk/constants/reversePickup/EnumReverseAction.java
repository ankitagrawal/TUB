package com.hk.constants.reversePickup;

import com.hk.constants.queue.EnumClassification;
import com.hk.domain.queue.Classification;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/19/13
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumReverseAction {

    Refund_In_Cash(2110L, EnumClassification.Refund_In_Cash),
    Refund_Reward_Points(2120L, EnumClassification.Refund_Reward_Points),
    Replacement_Order(2130L, EnumClassification.Replacement_Order),
    Decide_Later(2170L, EnumClassification.Decide_Later),
    /* CS Action Status */

    Pending_Approval(2010L, EnumClassification.Pending_Approval),
    Approved(2020L, EnumClassification.Approved),
    Force_Approval(2030L, EnumClassification.Force_Approval),
    Reconciled(2040L,EnumClassification.ReconciledGeneric );

    private Long id;
    private Classification classification;

    EnumReverseAction(Long id, EnumClassification enumClassification) {
        this.id = id;
        classification = enumClassification.asClassification();
    }

    public static List<EnumReverseAction> getAllReversePickAction() {
        return Arrays.asList(Refund_In_Cash, Refund_Reward_Points, Replacement_Order, Decide_Later);
    }

    public static List<EnumReverseAction> getAllCustomerActionStatus() {
        return Arrays.asList(Pending_Approval, Approved, Reconciled);
    }

    public static String getNameById(Long id) {
        for (EnumReverseAction enumReverseAction : EnumReverseAction.values()) {
            if (enumReverseAction.getId().equals(id)) {
                return enumReverseAction.getName();
            }
        }
        return null;
    }


    public Long getId() {
        return id;
    }

    public Classification getClassification() {
        return classification;
    }

    public String getName() {
        return this.getClassification().getPrimary();
    }

}
