package com.hk.constants.reversePickup;

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

    Refund_In_Cash(10L, "Refund In Cash"),
    Refund_Reward_Points(20L, "Refund Reward Points "),
    Replacement_Order(30L, "Replacement Order"),
    Return_Initiated(40L, "Return Initiated "),
    Return_Received(50L, "Return Received "),
    Return_QC_Checkin(60L, "Return QC Checkin "),
    Decide_Later(70L, "Decide Later "),
    /* CS Action Status */
    Pending_Approval(80L, "Pending Approval"),
    Approved(90L, "Approved");

    private Long id;
    private String name;

    EnumReverseAction(Long id, String actionTaken) {
        this.id = id;
        name = actionTaken;
    }

    public static List<EnumReverseAction> getAllReversePickAction() {
        return Arrays.asList(Refund_In_Cash, Refund_Reward_Points, Replacement_Order, Decide_Later);
    }

    public static List<EnumReverseAction> getAllCustomerActionStatus() {
        return Arrays.asList(Pending_Approval, Approved);
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
