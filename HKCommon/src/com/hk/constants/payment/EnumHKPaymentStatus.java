package com.hk.constants.payment;


import com.hk.domain.core.PaymentStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 6/11/13
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumHKPaymentStatus {

    REQUESTED(0L, "Requested"),
    AUTHENTICATION_PENDING(1L,"Authentication Pending"),
    SUCCESS(2L,"Success"),
    FAILURE(3L,"Failure"),
    IN_PROCESS(4L, "In Process"),
    ;

    private Long id;
    private String name;

    private EnumHKPaymentStatus(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public static PaymentStatus getCorrespondingStatus(EnumHKPaymentStatus enumHKPaymentStatus){
        if(enumHKPaymentStatus != null && EnumHKPaymentStatus.SUCCESS.getId().equals(enumHKPaymentStatus.getId())){
            return EnumPaymentStatus.SUCCESS.asPaymenStatus();
        }  else if (enumHKPaymentStatus != null && EnumHKPaymentStatus.AUTHENTICATION_PENDING.getId().equals(enumHKPaymentStatus.getId())) {
            return EnumPaymentStatus.AUTHORIZATION_PENDING.asPaymenStatus();
        } else if (enumHKPaymentStatus != null && EnumHKPaymentStatus.FAILURE.getId().equals(enumHKPaymentStatus.getId())){
            return  EnumPaymentStatus.FAILURE.asPaymenStatus();
        }  else if (enumHKPaymentStatus != null && EnumHKPaymentStatus.REQUESTED.getId().equals(enumHKPaymentStatus.getId())){
            return  EnumPaymentStatus.REQUEST.asPaymenStatus();
        }
        return null;
    }


}
