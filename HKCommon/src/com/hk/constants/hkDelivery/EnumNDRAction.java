package com.hk.constants.hkDelivery;


public enum EnumNDRAction {

    FutureDeliveryDate(10L,"Future delivery date"),
    MarkRTH(20L, "Mark RTH"),
    AttemptDeliveryAsap(30L, "Attempt delivery asap"),
    CustomerNotContactable(40L, "Customer not contactable");

    private Long id;
    private String ndrAction;


    EnumNDRAction(Long id, String ndrAction) {
        this.id = id;
        this.ndrAction = ndrAction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNdrAction() {
        return ndrAction;
    }

    public void setNdrAction(String ndrAction) {
        this.ndrAction = ndrAction;
    }

}
