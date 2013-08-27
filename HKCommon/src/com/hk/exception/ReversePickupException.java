package com.hk.exception;

import com.hk.domain.catalog.product.ProductVariant;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 */
public class ReversePickupException extends RuntimeException {

    ProductVariant productVariant;

    public ReversePickupException(String message, ProductVariant productVariant) {
        super(message);
        this.productVariant = productVariant;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }
}
