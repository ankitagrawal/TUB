package com.hk.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@SuppressWarnings("serial")
@Entity
@Table(name = "item_detail")
public class ItemDetail implements java.io.Serializable {


	@Id
	@Column(name = "item_code", unique = true, nullable = false, length = 12)
	private String itemCode;

	@Column(name = "item_name", length = 40)
	private String itemName;

	@Column(name = "alias", length = 40)
	private String alias;

	@Column(name = "print_name", length = 40)
	private String printName;

	@Column(name = "parent_group", length = 40)
	private String parentGroup;

	@Column(name = "unit", length = 40)
	private String unit;

	@Column(name = "opening_stock")
	private Double openingStock;

	@Column(name = "opening_stock_value")
	private Double openingStockValue;

	@Column(name = "sale_price")
	private Double salePrice;

	@Column(name = "purchase_price")
	private Double purchasePrice;

	@Column(name = "mrp")
	private Double mrp;

	@Column(name = "self_value_price")
	private Double selfValuePrice;

	@Column(name = "sale_discount")
	private Double saleDiscount;

	@Column(name = "purchase_discount")
	private Double purchaseDiscount;

	@Column(name = "min_sale_price")
	private Double minSalePrice;

	@Column(name = "tax_rate_local")
	private Double taxRateLocal;

	@Column(name = "tax_rate_central")
	private Double taxRateCentral;

	@Column(name = "tax_on_mrp")
	private Byte taxOnMrp;

	@Column(name = "item_description_1", length = 40)
	private String itemDescription1;

	@Column(name = "item_description_2", length = 40)
	private String itemDescription2;

	@Column(name = "item_description_3", length = 40)
	private String itemDescription3;

	@Column(name = "item_description_4", length = 40)
	private String itemDescription4;

	@Column(name = "imported")
	private Byte imported;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false, length = 19)
	private Date createDate;

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPrintName() {
		return this.printName;
	}

	public void setPrintName(String printName) {
		this.printName = printName;
	}

	public String getParentGroup() {
		return this.parentGroup;
	}

	public void setParentGroup(String parentGroup) {
		this.parentGroup = parentGroup;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getOpeningStock() {
		return this.openingStock;
	}

	public void setOpeningStock(Double openingStock) {
		this.openingStock = openingStock;
	}

	public Double getOpeningStockValue() {
		return this.openingStockValue;
	}

	public void setOpeningStockValue(Double openingStockValue) {
		this.openingStockValue = openingStockValue;
	}

	public Double getSalePrice() {
		return this.salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getPurchasePrice() {
		return this.purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Double getMrp() {
		return this.mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getSelfValuePrice() {
		return this.selfValuePrice;
	}

	public void setSelfValuePrice(Double selfValuePrice) {
		this.selfValuePrice = selfValuePrice;
	}

	public Double getSaleDiscount() {
		return this.saleDiscount;
	}

	public void setSaleDiscount(Double saleDiscount) {
		this.saleDiscount = saleDiscount;
	}

	public Double getPurchaseDiscount() {
		return this.purchaseDiscount;
	}

	public void setPurchaseDiscount(Double purchaseDiscount) {
		this.purchaseDiscount = purchaseDiscount;
	}

	public Double getMinSalePrice() {
		return this.minSalePrice;
	}

	public void setMinSalePrice(Double minSalePrice) {
		this.minSalePrice = minSalePrice;
	}

	public Double getTaxRateLocal() {
		return this.taxRateLocal;
	}

	public void setTaxRateLocal(Double taxRateLocal) {
		this.taxRateLocal = taxRateLocal;
	}

	public Double getTaxRateCentral() {
		return this.taxRateCentral;
	}

	public void setTaxRateCentral(Double taxRateCentral) {
		this.taxRateCentral = taxRateCentral;
	}

	public Byte getTaxOnMrp() {
		return this.taxOnMrp;
	}

	public void setTaxOnMrp(Byte taxOnMrp) {
		this.taxOnMrp = taxOnMrp;
	}

	public String getItemDescription1() {
		return this.itemDescription1;
	}

	public void setItemDescription1(String itemDescription1) {
		this.itemDescription1 = itemDescription1;
	}

	public String getItemDescription2() {
		return this.itemDescription2;
	}

	public void setItemDescription2(String itemDescription2) {
		this.itemDescription2 = itemDescription2;
	}

	public String getItemDescription3() {
		return this.itemDescription3;
	}

	public void setItemDescription3(String itemDescription3) {
		this.itemDescription3 = itemDescription3;
	}

	public String getItemDescription4() {
		return this.itemDescription4;
	}

	public void setItemDescription4(String itemDescription4) {
		this.itemDescription4 = itemDescription4;
	}

	public Byte getImported() {
		return this.imported;
	}

	public void setImported(Byte imported) {
		this.imported = imported;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
