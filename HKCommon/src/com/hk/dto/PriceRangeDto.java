package com.hk.dto;

import com.hk.domain.catalog.product.ProductOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


public class PriceRangeDto implements java.io.Serializable {

	private Double minPrice;
	private Double maxPrice;

	public PriceRangeDto(Double minPrice, Double maxPrice) {
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}
}