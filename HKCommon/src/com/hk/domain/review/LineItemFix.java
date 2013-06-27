package com.hk.domain.review;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hk.domain.shippingOrder.LineItem;

@Entity
@Table(name = "line_item_fix")
public class LineItemFix {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "line_item_id", nullable = false)
	private LineItem lineItem;

	@Column(name = "is_cod")
	private Boolean isCod;

	@Column(name = "previous_mrp")
	private Double previousMrp;

	@Column(name = "fixed_mrp")
	private Double fixedMrp;

	@Column(name = "fixed_hk_price")
	private Double fixedHkPrice;

	@Column(name = "fixed_hk_discount")
	private Double fixedHkDiscount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LineItem getLineItem() {
		return lineItem;
	}

	public void setLineItem(LineItem lineItem) {
		this.lineItem = lineItem;
	}

	public Boolean getIsCod() {
		return isCod;
	}

	public void setIsCod(Boolean isCod) {
		this.isCod = isCod;
	}

	public Double getPreviousMrp() {
		return previousMrp;
	}

	public void setPreviousMrp(Double previousMrp) {
		this.previousMrp = previousMrp;
	}

	public Double getFixedMrp() {
		return fixedMrp;
	}

	public void setFixedMrp(Double fixedMrp) {
		this.fixedMrp = fixedMrp;
	}

	public Double getFixedHkPrice() {
		return fixedHkPrice;
	}

	public void setFixedHkPrice(Double fixedHkPrice) {
		this.fixedHkPrice = fixedHkPrice;
	}

	public Double getFixedHkDiscount() {
		return fixedHkDiscount;
	}

	public void setFixedHkDiscount(Double fixedHkDiscount) {
		this.fixedHkDiscount = fixedHkDiscount;
	}
}
