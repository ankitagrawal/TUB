package com.hk.dto;

public class B2BProduct {
	
	String productId;
	Long quantity;
	
	public B2BProduct()
	{
		
	}
	
	public B2BProduct(String productId, Long quantity) {
		super();
		this.productId = productId;
		this.quantity = quantity;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

}
