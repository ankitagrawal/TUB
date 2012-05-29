package com.hk.constants.inventory;

/**
 * Created by IntelliJ IDEA.
 * User: Rajni
 * Date: May 29, 2012
 * Time: 12:04:39 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumPurchaseFormType {

    PurchaseFormType_C("C","Central Purchase against 2%"),
    PurchaseFormType_E1("E1","Subsequent Sale E1"),
    PurchaseFormType_E2("E2","Subsequent Sale E2"),
    PurchaseFormType_H("H","Export"),
    PurchaseFormType_I("I","Sale to units in SEZ"),
    PurchaseFormType_F("F","Stock Transfer"),
    ;

    private String name;
    private String description;

    EnumPurchaseFormType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
