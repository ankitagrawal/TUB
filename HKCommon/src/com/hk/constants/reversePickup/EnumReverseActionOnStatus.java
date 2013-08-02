package com.hk.constants.reversePickup;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/19/13
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumReverseActionOnStatus {

    Immediately(10L, "Immediately"),
    On_Pickup(20L, "On Pickup"),
    On_Receiving(30L, "On Receiving"),
    On_Warehouse_QC(40L, "On Warehouse QC"),
    Decide_Later(50L, " Decide Later");

    private Long id;
    private String name;

    EnumReverseActionOnStatus(Long id, String actionTaken) {
        this.id = id;
        name = actionTaken;
    }

    public static List<EnumReverseActionOnStatus> getAllReverseActionOnStatus() {
        return Arrays.asList(Immediately, On_Pickup, On_Receiving, On_Warehouse_QC, Decide_Later);
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
