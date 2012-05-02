package com.hk.dto;

public class TaxComponent {

    public TaxComponent(Double surcharge, Double tax, Double payable) {
        this.surcharge = surcharge;
        this.tax = tax;
        this.payable = payable;
    }

    private Double tax;
    private Double surcharge;
    private Double payable;

    public Double getTax() {
        return tax;
    }

    public Double getSurcharge() {
        return surcharge;
    }

    public Double getPayable() {
        return payable;
    }

}
