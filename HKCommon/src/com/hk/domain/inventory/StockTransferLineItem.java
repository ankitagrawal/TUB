package com.hk.domain.inventory;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Tax;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;


@Entity
@Table(name = "stock_transfer_line_item", uniqueConstraints = @UniqueConstraint(columnNames = {"stock_transfer_id", "sku_id", "checked_out_sku_group_id"}))
public class StockTransferLineItem implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_transfer_id", nullable = false)
	private StockTransfer stockTransfer;

	@Column(name = "checkedout_qty", nullable = false)
	private Long checkedoutQty;

	@Column(name = "checkedin_qty")
	private Long checkedinQty;

	@Column(name = "cost_price", precision = 8)
	private Double costPrice;

	@Column(name = "mrp", precision = 8)
	private Double mrp;

	@Column(name = "batch_number")
	private String batchNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "mfg_date", length = 19)
	private Date mfgDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiry_date", length = 19)
	private Date expiryDate;

	@Transient
	private ProductVariant productVariant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sku_id", nullable = false)
	private Sku sku;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "checked_out_sku_group_id")
	private SkuGroup checkedOutSkuGroup;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "checked_in_sku_group_id")
	private SkuGroup checkedInSkuGroup;

	@ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "tax_id")
    private Tax tax;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StockTransfer getStockTransfer() {
		return stockTransfer;
	}

	public void setStockTransfer(StockTransfer stockTransfer) {
		this.stockTransfer = stockTransfer;
	}

	public Long getCheckedoutQty() {
		return checkedoutQty;
	}

	public void setCheckedoutQty(Long checkedoutQty) {
		this.checkedoutQty = checkedoutQty;
	}

	public Long getCheckedinQty() {
		return checkedinQty;
	}

	public void setCheckedinQty(Long checkedinQty) {
		this.checkedinQty = checkedinQty;
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

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Date getMfgDate() {
		return mfgDate;
	}

	public void setMfgDate(Date mfgDate) {
		this.mfgDate = mfgDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	@Override
	public String toString() {
		return this.id != null ? this.id.toString() : "";
	}

	public SkuGroup getCheckedOutSkuGroup() {
		return checkedOutSkuGroup;
	}

	public void setCheckedOutSkuGroup(SkuGroup checkedOutSkuGroup) {
		this.checkedOutSkuGroup = checkedOutSkuGroup;
	}

	public SkuGroup getCheckedInSkuGroup() {
		return checkedInSkuGroup;
	}

	public void setCheckedInSkuGroup(SkuGroup checkedInSkuGroup) {
		this.checkedInSkuGroup = checkedInSkuGroup;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}
}
