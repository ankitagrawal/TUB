package com.hk.constants.queue;

import com.hk.domain.queue.Classification;

/*
 * User: Pratham
 * Date: 15/04/13  Time: 16:23
*/
public enum EnumClassification {

    BASKET_CATEGORY_BEAUTY(10L, "Basket Category", "Beauty"),
    Cheque_Cash_Neft(110L, "Cheque/Cash/Neft", "Authorization Pending"),
    COD(210L, "COD", "Authorization Pending"),
    Online_Payment_Disputes(120L, "Online Payment Disputes", "Auth_Pending/Error"),
    JIT(220L, "JIT", "JIT"),
    DropShip(230L, "JIT", "DropShip"),
    Services(240L, "JIT", "Services"),
    Dispatch_Issues(310L, "Dispatch Issue", "Shipment Not Created"),
    Warehouse(410L, "Warehouse Processing" , "In Process"),
    AD_HOC(410L, "AD_HOC" , "Pending with CC"),
    Vendor(460L, "Vendor", "Procurement");

    private Long id;
    private String primary;
    private String secondary;

    EnumClassification(Long id, String primary, String secondary) {
        this.id = id;
        this.primary = primary;
        this.secondary = secondary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public Classification asClassification() {
        Classification classification = new Classification();
        classification.setId(id);
        classification.setPrimary(primary);
        classification.setSecondary(secondary);
        return classification;
    }
}
