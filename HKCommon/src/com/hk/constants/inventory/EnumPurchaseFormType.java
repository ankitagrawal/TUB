package com.hk.constants.inventory;

public enum EnumPurchaseFormType {

    PurchaseFormType_C(1L, "C","Central Purchase against 2%"),
    PurchaseFormType_E1(2L, "E1","Subsequent Sale E1"),
    PurchaseFormType_E2(3L, "E2","Subsequent Sale E2"),
    PurchaseFormType_H(4L, "H","Export"),
    PurchaseFormType_I(5L, "I","Sale to units in SEZ"),
    PurchaseFormType_F(6L, "F","Stock Transfer"),
    ;

    private String name;
    private String description;
    private Long id;

    EnumPurchaseFormType(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getId(){
      return id;
    }
}
