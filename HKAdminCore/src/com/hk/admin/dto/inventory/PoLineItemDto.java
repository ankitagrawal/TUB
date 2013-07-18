package com.hk.admin.dto.inventory;

import com.hk.domain.accounting.PoLineItem;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Dec 2, 2011
 * Time: 10:39:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class PoLineItemDto {

    private PoLineItem poLineItem;

    private Double taxable = 0.0;

    private Double tax = 0.0;

    private Double surcharge = 0.0;

    private Double payable = 0.0;

    private Double marginMrpVsCP = 0.0;
    
    private String remarks;

    public PoLineItem getPoLineItem() {
        return poLineItem;
    }

    public void setPoLineItem(PoLineItem poLineItem) {
        this.poLineItem = poLineItem;
    }

    public Double getTaxable() {
        return taxable;
    }

    public void setTaxable(Double taxable) {
        this.taxable = taxable;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(Double surcharge) {
        this.surcharge = surcharge;
    }

    public Double getPayable() {
        return payable;
    }

    public void setPayable(Double payable) {
        this.payable = payable;
    }

    public Double getMarginMrpVsCP() {
        return marginMrpVsCP;
    }

    public void setMarginMrpVsCP(Double marginMrpVsCP) {
        this.marginMrpVsCP = marginMrpVsCP;
    }

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
