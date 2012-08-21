package com.hk.admin.dto.inventory;

import com.hk.domain.inventory.GrnLineItem;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Dec 2, 2011
 * Time: 10:39:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class GrnLineItemDto {

    private GrnLineItem grnLineItem;

    private Double taxable = 0.0;

    private Double tax = 0.0;

    private Double surcharge = 0.0;

    private Double payable = 0.0;

    private Long checkedInQty = 0L;

    private Double marginMrpVsCP = 0.0;

    public GrnLineItem getGrnLineItem() {
        return grnLineItem;
    }

    public void setGrnLineItem(GrnLineItem grnLineItem) {
        this.grnLineItem = grnLineItem;
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

    public Long getCheckedInQty() {
        return checkedInQty;
    }

    public void setCheckedInQty(Long checkedInQty) {
        this.checkedInQty = checkedInQty;
    }

    public Double getMarginMrpVsCP() {
        return marginMrpVsCP;
    }

    public void setMarginMrpVsCP(Double marginMrpVsCP) {
        this.marginMrpVsCP = marginMrpVsCP;
    }
}