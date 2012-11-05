package com.hk.dto;

public class TaxComponent {

	public TaxComponent(Double surcharge, Double tax, Double payable, Double taxRate) {
        this.surcharge = surcharge;
        this.tax = tax;
        this.payable = payable;
	    this.taxRate = taxRate;
    }

    private Double tax;
    private Double surcharge;
    private Double payable;
	private Double taxRate;

    public Double getTax() {
        return tax;
    }

    public Double getSurcharge() {
        return surcharge;
    }

    public Double getPayable() {
        return payable;
    }

	public Double getTaxRate() {
		return taxRate;
	}
}
