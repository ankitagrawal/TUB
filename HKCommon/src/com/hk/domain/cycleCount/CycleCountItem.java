package com.hk.domain.cycleCount;



import com.hk.domain.sku.SkuGroup;

import javax.persistence.*;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 11, 2013
 * Time: 12:08:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "cycle_count_item")
public class CycleCountItem implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cycle_count_id", nullable = false)
	private CycleCount cycleCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sku_group_id", nullable = false)
	private SkuGroup skuGroup;


	@Column(name = "scanned_qty", unique = true, nullable = false)
	private Integer scannedQty;

	@Transient
	private Integer  pviQuantity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CycleCount getCycleCount() {
		return cycleCount;
	}

	public void setCycleCount(CycleCount cycleCount) {
		this.cycleCount = cycleCount;
	}

	public SkuGroup getSkuGroup() {
		return skuGroup;
	}

	public void setSkuGroup(SkuGroup skuGroup) {
		this.skuGroup = skuGroup;
	}

	public Integer getScannedQty() {
		return scannedQty;
	}

	public void setScannedQty(Integer scannedQty) {
		this.scannedQty = scannedQty;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof CycleCountItem)) {
			return false;
		}
		CycleCountItem cCItem = (CycleCountItem) o;

		if (id != null && cCItem.id != null && id.equals(cCItem.id)) {
			return true;
		} else {
			EqualsBuilder equalsBuilder = new EqualsBuilder();
			if (this.getSkuGroup().getId() != null || cCItem.getSkuGroup().getId() != null) {
				equalsBuilder.append(this.getSkuGroup().getId(), cCItem.getSkuGroup().getId());
				return equalsBuilder.isEquals();
			}
			return false;
		}


	}


	public Integer getPviQuantity() {
		return pviQuantity;
	}

	public void setPviQuantity(Integer pviQuantity) {
		this.pviQuantity = pviQuantity;
	}
}
