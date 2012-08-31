package com.hk.dto;


public class ProductOptionDto implements java.io.Serializable {

	private Long id;
	private String name;
	private String value;
	private Long qty;
	private boolean applicable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	public boolean isApplicable() {
		return applicable;
	}

	public boolean getApplicable() {
		return applicable;
	}

	public void setApplicable(boolean applicable) {
		this.applicable = applicable;
	}
}