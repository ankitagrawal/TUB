package com.hk.constants.reversePickup;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/23/13
 * Time: 6:58 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumCourierConstant {

    HealthKart_Managed(10L, "HealthKart Managed"),
    Customer_Managed(20L, "Customer_Managed");

    private Long id;
    private String status;

    EnumCourierConstant(Long id, String actionTaken) {
        this.id = id;
        status = actionTaken;
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
}
