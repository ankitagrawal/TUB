package com.hk.dto.pos;

import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 7/18/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class PosProductSearchDto {

	private String productName;
	private String size;
	private String flavor;
	private String color;
	private String form;
	private Integer countId;
	private String productVariantId;
	private Sku sku;

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

	public Integer getCountId() {
		return countId;
	}

	public void setCountId(Integer countId) {
		this.countId = countId;
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
}
