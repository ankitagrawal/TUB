package com.hk.web.action.core.b2b;

public class B2BProduct {
	
	String productId;
	int quantity;
	
	public B2BProduct()
	{
		
	}
	
	public B2BProduct(String productId, int quantity) {
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
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
