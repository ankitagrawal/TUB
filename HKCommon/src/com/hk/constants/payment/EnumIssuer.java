package com.hk.constants.payment;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 11/26/12
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumIssuer {

    //first would be name, second would be display name
    VISA("VISA", "VISA", ""),
    MASTERCARD("MASTERCARD", "MASTERCARD", ""),
    MAESTRO("MAESTRO", "MAESTRO", ""),
    CITRUS("CITRUS", "CITRUS", "faster checkout"),
    PAYPAL("PAYPAL", "PAYPAL", "For International Card Users Only");
    //THEN put all the names of the banks, they are also issuers only

    private java.lang.String name;
    private String id;
    private String tagLine;

    EnumIssuer(String id, java.lang.String name, String tagLine) {
        this.name = name;
        this.id = id;
        this.tagLine = tagLine;
    }

    public java.lang.String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getTagLine() {
        return tagLine;
    }

    public static EnumIssuer getIssuerFromId(String id) {
        for (EnumIssuer issuer : values()) {
            if (issuer.getId().equals(id)) return issuer;
        }
        return null;
    }

}

