package com.hk.constants.reversePickup;

import com.hk.domain.reversePickupOrder.ReversePickupType;

import java.util.Arrays;
import java.util.List;

public enum EnumReversePickupType {

    REVERSE_PICKUP(10L, "REVERSE_PICKUP"),
    REPLACEMENT_ORDER(20L, "REPLACEMENT_ORDER"),
    LOST(30L, "LOST"),
    APPEASEMENT(40L, "APPEASEMENT"),
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
