package com.hk.domain.cycleCount;



import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;

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
	@JoinColumn(name = "sku_group_id")
	private SkuGroup skuGroup;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sku_item_id")
	private SkuItem skuItem;

    @Column(name = "scanned_qty", unique = true, nullable = false)
    private Integer scannedQty;

    @Transient
    private int systemQty;


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

		if (id != null && cCItem.getId() != null) {
			return id.equals(cCItem.getId());
		} else {
			EqualsBuilder equalsBuilder = new EqualsBuilder();
			if (this.getSkuGroup().getId() != null || cCItem.getSkuGroup().getId() != null) {
				equalsBuilder.append(this.getSkuGroup().getId(), cCItem.getSkuGroup().getId());
				return equalsBuilder.isEquals();
			}
			return false;
		}


	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return this.id != null ? this.id.toString() : "";
	}

    public SkuItem getSkuItem() {
        return skuItem;
    }

    public void setSkuItem(SkuItem skuItem) {
        this.skuItem = skuItem;
    }

    public int getSystemQty() {
        return systemQty;
    }

    public void setSystemQty(int systemQty) {
        this.systemQty = systemQty;
    }
}
