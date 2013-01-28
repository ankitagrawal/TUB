package com.hk.domain.loyaltypg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hk.domain.catalog.product.ProductVariant;

@Entity
@Table(name = "loyalty_product")
public class LoyaltyProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@OneToOne
	@JoinColumn(name = "variant_id", unique=true)
	private ProductVariant variant;

	@Column(name = "points")
	private Double points;
	
	@Transient
	public String getProductUrl() {
		return "/loyaltypg/product/" + getId();
	}
	
	private transient long qty;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public ProductVariant getVariant() {
		return variant;
	}

	public void setVariant(ProductVariant variant) {
		this.variant = variant;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}
	
	public long getQty() {
		return qty;
	}
	
	public void setQty(long qty) {
		this.qty = qty;
	}
}
