package com.hk.domain.inventory.po;
// Generated Feb 14, 2012 1:22:29 PM by Hibernate Tools 3.2.4.CR1


import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PurchaseInvoiceStatus generated by hbm2java
 */
@Entity
@Table(name = "purchase_invoice_status")
public class PurchaseInvoiceStatus implements java.io.Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name")
	private String name;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return id == null ? "" : id.toString();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PurchaseInvoiceStatus)) {
			return false;
		}
		PurchaseInvoiceStatus purchaseInvoiceStatus = (PurchaseInvoiceStatus) obj;
		if (this.id != null && purchaseInvoiceStatus.getId() != null) {
			return this.id.equals(purchaseInvoiceStatus.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}
}


