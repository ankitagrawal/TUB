package com.hk.exception;

import com.hk.domain.courier.Courier;

/**
 * Created by IntelliJ IDEA. User:Seema Date: Jul 20, 2012 Time: 7:53:32 PM To change this template use File | Settings |
 * File Templates.
 */
public class DuplicateAwbexception extends HealthkartRuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Courier courier;
    private String  awbNumber;

    public DuplicateAwbexception(String message, Courier courier, String awbNumber) {
        super(message);
        this.courier = courier;
        this.awbNumber = awbNumber;
    }

    public Courier getCourier() {
        return courier;
    }

    public String getAwbNumber() {
        return awbNumber;
    }
}
