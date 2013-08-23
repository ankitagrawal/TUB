package com.hk.constants.inventory;

/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 7/23/13
 * Time: 11:34 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumReconciliationActionType {
    RefundAmount(200L, "Refund Amount"),
    RewardPoints(210L, "Reward Points"),
    None(220L,"No Action"),
    ;

    private java.lang.String name;
    private java.lang.Long id;

    EnumReconciliationActionType(java.lang.Long id, java.lang.String name) {
        this.id = id;
        this.name = name;
    }

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
}
