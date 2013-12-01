package com.hk.report.dto.sales;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;

/**
 * Created by IntelliJ IDEA.
 * User: Developer
 * Date: Feb 1, 2012
 * Time: 2:28:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class NetMarginPerProductVariantDto {
	Category primaryCategory;
	String productVariantId;
	String productName;
	ProductVariant productVariant;
	Double hkPrice;
	Double costPrice;
	Double taxRate;
	Long quantity;
	Double taxAdjPrice;
	Double netMargin;
	Double totalShipping;
	Double totalCollection;
	Double shippingCollectionPerPV;
	Double netContMargin;

	public Category getPrimaryCategory() {
		return primaryCategory;
	}

	public void setPrimaryCategory(Category primaryCategory) {
		this.primaryCategory = primaryCategory;
	}

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public Double getHkPrice() {
		return hkPrice;
	}

	public void setHkPrice(Double hkPrice) {
		this.hkPrice = hkPrice;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Double getTaxAdjPrice() {
		return taxAdjPrice;
	}

	public void setTaxAdjPrice(Double taxAdjPrice) {
		this.taxAdjPrice = taxAdjPrice;
	}

	public Double getNetMargin() {
		return netMargin;
	}

	public void setNetMargin(Double netMargin) {
		this.netMargin = netMargin;
	}

	public Double getTotalShipping() {
		return totalShipping;
	}

	public void setTotalShipping(Double totalShipping) {
		this.totalShipping = totalShipping;
	}

	public Double getTotalCollection() {
		return totalCollection;
	}

	public void setTotalCollection(Double totalCollection) {
		this.totalCollection = totalCollection;
	}

	public Double getShippingCollectionPerPV() {
		return shippingCollectionPerPV;
	}

	public void setShippingCollectionPerPV(Double shippingCollectionPerPV) {
		this.shippingCollectionPerPV = shippingCollectionPerPV;
	}

	public Double getNetContMargin() {
		return netContMargin;
	}

	public void setNetContMargin(Double netContMargin) {
		this.netContMargin = netContMargin;
	}
	  public Object get(int colIdx) {
    switch (colIdx) {
      case 0:
        return getPrimaryCategory();
      case 1:
        return getProductVariantId();
      case 2:
        return getHkPrice();
      case 3:
        return getCostPrice();
      case 4:
        return getTaxRate();
      case 5:
        return getQuantity();
      case 6:
        return getTaxAdjPrice();
      case 7:
	      return getNetMargin() ;
      case 8:
	      return getTotalShipping();
      case 9:
	      return getTotalCollection();
      case 10:
	      return getProductName();
      case 11:
	      return getProductVariant().getOptionsCommaSeparated();
      case 12:
	      return getShippingCollectionPerPV();
      case 13:
	      return getNetContMargin();
      default:
        return "NA";
    }
  }
}
