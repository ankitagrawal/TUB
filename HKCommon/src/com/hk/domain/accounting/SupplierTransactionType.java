package com.hk.domain.accounting;

import javax.persistence.*;

@Entity
@Table (name = "supplier_transaction_type")
public class SupplierTransactionType {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
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

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

}
