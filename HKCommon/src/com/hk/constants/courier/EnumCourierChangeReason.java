package com.hk.constants.courier;


import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 21/01/13
 * Time: 20:36
 * To change this template use File | Settings | File Templates.
 */
public enum EnumCourierChangeReason {

    CUSTOMER_REQUEST(10L, "CUSTOMER_REQUEST"),
    SERVICE_NOT_AVAILABLE(20L, "SERVICE_NOT_AVAILABLE");

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String name;
    private Long id;

    private EnumCourierChangeReason(Long id, String name) {
        this.name = name;
        this.id = id;
    }


    public static List<EnumCourierChangeReason> getAllCourierChangeReasons() {
        return Arrays.asList(CUSTOMER_REQUEST, SERVICE_NOT_AVAILABLE);
    }

}