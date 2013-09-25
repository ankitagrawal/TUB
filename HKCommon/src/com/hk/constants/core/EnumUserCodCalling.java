package com.hk.constants.core;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 11, 2013
 * Time: 1:52:55 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumUserCodCalling {

    PENDING_WITH_KNOWLARITY(10, "Pending With Knowlarity"),
    THIRD_PARTY_FAILED(20, "Third Party Failed"),
    CONFIRMED(30, "Payment Successful"),
    CANCELLED(40, "Cancelled"),
    PAYMENT_FAILED(50, "Payment Failed"),
    PENDING_WITH_EFFORT_BPO(60, "Pending With Effort Bpo"),
    PENDING_WITH_HEALTHKART(70, "Pending With Healthkart"),
    PENDING_WITH_DRISHTI(80, "Pending with Drishti"),
    PENDING_WITH_CUSTOMER(110,"Pending with customer"),
    ;

    private int id;
    private String name;


    EnumUserCodCalling(int id, String name) {
        this.id = id;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
