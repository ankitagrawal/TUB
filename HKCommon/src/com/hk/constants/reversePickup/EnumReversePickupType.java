package com.hk.constants.reversePickup;

import com.hk.domain.reversePickupOrder.ReversePickupType;

import java.util.Arrays;
import java.util.List;

public enum EnumReversePickupType {

    REVERSE_PICKUP(10L, "RPU_QC_Checked_In"),
    REPLACEMENT_ORDER(20L, "RPU_QC_Checked_In"),
    LOST(30L, "RPU_QC_Checked_In"),
    APPEASEMENT(40L, "RPU_QC_Checked_In"),
    ;

    private Long id;
    private String name;

    EnumReversePickupType(Long id, String name) {
        this.id = id;
        name = name;
    }

    public static ReversePickupType getReversePickupType(EnumReversePickupType enumReversePickupType) {
        ReversePickupType reversePickupType = new ReversePickupType();
        reversePickupType.setId(enumReversePickupType.getId());
        reversePickupType.setName(enumReversePickupType.getName());
        return reversePickupType;
    }


    public ReversePickupType asReversePickupType() {
        ReversePickupType reversePickupType = new ReversePickupType();
        reversePickupType.setId(this.getId());
        reversePickupType.setName(this.getName());
        return reversePickupType;
    }

    public static List<ReversePickupType> getRPTypeList(){
        return Arrays.asList(
                REVERSE_PICKUP.asReversePickupType(),
                REPLACEMENT_ORDER.asReversePickupType(),
                LOST.asReversePickupType(),
                APPEASEMENT.asReversePickupType());
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
