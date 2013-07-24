package com.hk.dto.pos;

import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 7/23/13
 * Time: 7:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class PosSkuGroupSearchDto {

	private String productName;
	private String size;
	private String flavor;
	private String color;
	private String form;
	private Integer availableInventory;
	private String productVariantId;
	private Sku sku;
	private SkuGroup skuGroup;
	private Double costPrice;
	private Double mrp;
	private Date mfgDate;
	private Date expiryDate;
	private String batchNumber;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Integer getAvailableInventory() {
		return availableInventory;
	}

	public void setAvailableInventory(Integer availableInventory) {
		this.availableInventory = availableInventory;
	}

	public String getFlavor() {
		List<ProductOption> productOptions = this.sku.getProductVariant().getProductOptions();
		for (ProductOption productOption : productOptions) {
			if (productOption.getName().equalsIgnoreCase("flavor")) {
				return productOption.getValue();
			}
		}
		return "";
	}

	public String getSize() {
		List<ProductOption> productOptions = this.sku.getProductVariant().getProductOptions();
		for (ProductOption productOption : productOptions) {
			if (productOption.getName().equalsIgnoreCase("size")) {
				return productOption.getValue();
			}
		}
		return "";
	}

	public String getColor() {
		List<ProductOption> productOptions = this.sku.getProductVariant().getProductOptions();
		for (ProductOption productOption : productOptions) {
			if (productOption.getName().equalsIgnoreCase("color")) {
				return productOption.getValue();
			}
		}
		return "";
	}

	public String getForm() {
		List<ProductOption> productOptions = this.sku.getProductVariant().getProductOptions();
		for (ProductOption productOption : productOptions) {
			if (productOption.getName().equalsIgnoreCase("form")) {
				return productOption.getValue();
			}
		}
		return "";
	}

	public SkuGroup getSkuGroup() {
		return skuGroup;
	}

	public void setSkuGroup(SkuGroup skuGroup) {
		this.skuGroup = skuGroup;
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

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
}
