package com.hk.domain.sku;

// Generated Feb 28, 2012 2:31:32 PM by Hibernate Tools 3.2.4.CR1

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Tax;
import com.hk.domain.warehouse.Warehouse;

import javax.persistence.*;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;

@Entity
@Table(name = "sku")
public class Sku implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long           id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    private Warehouse      warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id")
    private Tax            tax;

    @Column(name = "cut_off_inventory")
    private Long           cutOffInventory;

    @Column(name = "forecasted_quantity")
    private Long           forecastedQuantity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", length = 19)
    private Date           createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date", length = 19)
    private Date           updateDate;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    @Override
    public String toString() {
        // return this.productVariant.toString().concat(this.warehouse.getName());
        return this.id != null ? this.id.toString() : "";
    }

    public Long getCutOffInventory() {
        return cutOffInventory;
    }

    public void setCutOffInventory(Long cutOffInventory) {
        this.cutOffInventory = cutOffInventory;
    }

    public Long getForecastedQuantity() {
        return forecastedQuantity;
    }

    public void setForecastedQuantity(Long forecastedQuantity) {
        this.forecastedQuantity = forecastedQuantity;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Sku)) {
			return false;
		}
		Sku sku = (Sku) o;

		if (id != null && sku.id != null && id.equals(sku.id)) {
			return true;
		} else {
			EqualsBuilder equalsBuilder = new EqualsBuilder();
			if (this.warehouse == null || this.productVariant == null || sku.warehouse == null || sku.productVariant == null) {
				return false;
			}
			equalsBuilder.append(this.warehouse.getId(), sku.warehouse.getId());
			equalsBuilder.append(this.productVariant.getId(), sku.productVariant.getId());
			return equalsBuilder.isEquals();
		}
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
