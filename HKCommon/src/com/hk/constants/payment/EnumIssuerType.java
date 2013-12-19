package com.hk.constants.payment;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 11/21/12
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumIssuerType {

    Bank("Bank", "Bank"),
    Card("Card", "Card"),
    Debit("Debit","Debit"),
    COD("COD","Cash On Delivery");
    private java.lang.String name;
    private String id;

    EnumIssuerType(String id, java.lang.String name) {
        this.name = name;
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public static EnumIssuerType getIssuerFromId(String id) {
        for (EnumIssuerType issuerType : values()) {
            if (issuerType.getId().equals(id)) return issuerType;
        }
        return null;
    }

}

