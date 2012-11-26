package com.hk.admin.dto.inventory;

import java.util.ArrayList;
import java.util.List;

import com.hk.domain.inventory.po.PurchaseOrder;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Dec 2, 2011
 * Time: 10:38:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseOrderDto {

    private PurchaseOrder purchaseOrder;

    private List<PoLineItemDto> poLineItemDtoList = new ArrayList<PoLineItemDto>();

    private Double totalTax = 0.0;

    private Double totalPayable = 0.0;

    private Double totalTaxable = 0.0;

    private Double totalSurcharge = 0.0;

	private Double finalPayable = 0.0;

    public List<PoLineItemDto> getPoLineItemDtoList() {
        return poLineItemDtoList;
    }

    public void setPoLineItemDtoList(List<PoLineItemDto> poLineItemDtoList) {
        this.poLineItemDtoList = poLineItemDtoList;
    }

    public Double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(Double totalPayable) {
        this.totalPayable = totalPayable;
    }

    public Double getTotalTaxable() {
        return totalTaxable;
    }

    public void setTotalTaxable(Double totalTaxable) {
        this.totalTaxable = totalTaxable;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Double getTotalSurcharge() {
        return totalSurcharge;
    }

    public void setTotalSurcharge(Double totalSurcharge) {
        this.totalSurcharge = totalSurcharge;
    }

	public Double getFinalPayable() {
		return finalPayable;
	}

	public void setFinalPayable(Double finalPayable) {
		this.finalPayable = finalPayable;
	}
}
