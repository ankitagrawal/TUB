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

    COURIER_SERVICE_CHANGE(10L, "Courier_Service_Change"),
    CHANGE_FROM_GROUND_TO_AIR_SHIPPING(20L, "Change_from_Ground_to_Air_shipping"),
    CC_REQUEST(30L,"CC_Request"),
    BRIGHT_MOVEMENT(40L,"Bright_Movement"),
    RTO_DUE_TO_ODA(50L,"RTO_due_to_ODA");


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
        return Arrays.asList(COURIER_SERVICE_CHANGE,CHANGE_FROM_GROUND_TO_AIR_SHIPPING,CC_REQUEST,BRIGHT_MOVEMENT,RTO_DUE_TO_ODA);
    }

}