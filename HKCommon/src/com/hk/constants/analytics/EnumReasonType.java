package com.hk.constants.analytics;

/*
 * User: Pratham
 * Date: 26/03/13  Time: 01:06
*/
public enum  EnumReasonType {

    Escalate_Back(10L, "Escalate_Back");

    private String name;

    private Long id;

    EnumReasonType(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
