package com.hk.store;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.loyaltypg.LoyaltyProduct;

/**
 * This class should be replaced with some meaningful blueprint.
 * This class is just used to provide an abstraction to Product/LoyaltyProduct domain.
 * So, in any case either <code>loyaltyProduct</code> or <code>product</code>
 * will be populated.
 */
public class ProductAdapter {
	
	private LoyaltyProduct loyaltyProduct;

	private Product product;

	public LoyaltyProduct getLoyaltyProduct() {
		return loyaltyProduct;
	}

	public void setLoyaltyProduct(LoyaltyProduct loyaltyProduct) {
		this.loyaltyProduct = loyaltyProduct;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	
}
