package com.hk.constants.payment;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 11/26/12
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumIssuer {

    VISA("VISA", "VISA"),
    MASTERCARD("MASTERCARD", "MASTERCARD"),
    MAESTRO("MAESTRO", "MAESTRO"),
    CITRUS("CITRUS", "CITRUS"),
    PAYPAL("PAYPAL", "PAYPAL");
    //THEN put all the names of the banks, they are also issuers only

    private java.lang.String name;
    private String id;

    EnumIssuer(String id, java.lang.String name) {
        this.name = name;
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public static EnumIssuer getIssuerFromId(String id) {
        for (EnumIssuer issuer : values()) {
            if (issuer.getId().equals(id)) return issuer;
        }
        return null;
    }

}

