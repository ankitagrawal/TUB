package com.hk.constants.courier;

import com.hk.domain.courier.AwbStatus;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jul 13, 2012
 * Time: 9:09:38 AM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumAwbStatus {

    Unused(10L, "Unused"),
    Attach(20L, "Attach"),
    Authorization_Pending(30L, "Authorization Pending"),
    Used(40L, "Used");

    private Long id;
    private String status;

    EnumAwbStatus(Long id, String status) {
        this.id = id;
        this.status = status;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AwbStatus getAsAwbStatus() {
        AwbStatus awbStatus = new AwbStatus();
        awbStatus.setId(this.id);
        awbStatus.setStatus(this.status);
        return awbStatus;
    }
}
