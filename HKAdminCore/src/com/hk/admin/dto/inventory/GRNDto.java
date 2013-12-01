package com.hk.admin.dto.inventory;

import com.hk.domain.inventory.GoodsReceivedNote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Dec 2, 2011
 * Time: 10:38:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class GRNDto {

	private GoodsReceivedNote grn;

	private List<GrnLineItemDto> grnLineItemDtoList = new ArrayList<GrnLineItemDto>();

	private Double totalTax = 0.0;

	private Double totalPayable = 0.0;

	private Double totalTaxable = 0.0;

	private Double totalSurcharge = 0.0;

	private Double finalPayable = 0.0;

    private Long totalQty=0L;

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

	public GoodsReceivedNote getGrn() {
		return grn;
	}

	public void setGrn(GoodsReceivedNote grn) {
		this.grn = grn;
	}

	public List<GrnLineItemDto> getGrnLineItemDtoList() {
		return grnLineItemDtoList;
	}

	public void setGrnLineItemDtoList(List<GrnLineItemDto> grnLineItemDtoList) {
		this.grnLineItemDtoList = grnLineItemDtoList;
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

    public Long getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Long totalQty) {
        this.totalQty = totalQty;
    }
}