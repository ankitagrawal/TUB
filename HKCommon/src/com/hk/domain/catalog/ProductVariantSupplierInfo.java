package com.hk.domain.catalog;

import com.hk.domain.catalog.product.ProductVariant;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 1:04 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "product_variant_supplier_info", uniqueConstraints = @UniqueConstraint(columnNames = {"product_variant_id", "supplier_id"}))
public class ProductVariantSupplierInfo implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_variant_id", nullable = false)
	private ProductVariant productVariant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id", nullable = false)
	private Supplier supplier;

	@Column(name = "asked_qty", nullable = false)
	private Long askedQty = 0L;

	@Column(name = "received_qty", nullable = false)
	private Long receivedQty = 0L;

	@Column(name = "fill_rate", nullable = false)
	private Double fillRate = 0.0;

	public Long getId() {
		return id;
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

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Long getAskedQty() {
		return askedQty;
	}

	public void setAskedQty(Long askedQty) {
		this.askedQty = askedQty;
	}

	public Long getReceivedQty() {
		return receivedQty;
	}

	public void setReceivedQty(Long receivedQty) {
		this.receivedQty = receivedQty;
	}

	public Double getFillRate() {
		return fillRate;
	}

	public void setFillRate(Double fillRate) {
		this.fillRate = fillRate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ProductVariantSupplierInfo)) {
			return false;
		}

		ProductVariantSupplierInfo productVariantSupplierInfo = (ProductVariantSupplierInfo) obj;
		if (this.id != null && productVariantSupplierInfo.getId() != null) {
			return this.id.equals(productVariantSupplierInfo.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

}
