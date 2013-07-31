package com.hk.dto.pos;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 7/23/13
 * Time: 5:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class PosSkuGroupDto {

	private Long grnId;
	private String batchNumber;
	private String mfgDate;
	private String expiryDate;
	private Double costPrice;
	private Double mrp;
	private String checkInDate;
	private Integer checkedInQty;
	private Integer inStockQty;

	public Long getGrnId() {
		return grnId;
	}

	public void setGrnId(Long grnId) {
		this.grnId = grnId;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getMfgDate() {
		return mfgDate;
	}

	public void setMfgDate(String mfgDate) {
		this.mfgDate = mfgDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public String getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Integer getCheckedInQty() {
		return checkedInQty;
	}

	public void setCheckedInQty(Integer checkedInQty) {
		this.checkedInQty = checkedInQty;
	}

	public Integer getInStockQty() {
		return inStockQty;
	}

	public void setInStockQty(Integer inStockQty) {
		this.inStockQty = inStockQty;
	}

}
