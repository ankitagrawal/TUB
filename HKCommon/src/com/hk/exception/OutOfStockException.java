package com.hk.exception;

import com.hk.domain.catalog.product.ProductVariant;

public class OutOfStockException extends HealthkartRuntimeException {

    ProductVariant productVariant;

    public OutOfStockException(String message, ProductVariant productVariant) {
        super(message);
        this.productVariant = productVariant;
    }

    public OutOfStockException(ProductVariant productVariant) {
        super("The selected product(" + productVariant.getProduct().getName() + " with " + productVariant.getOptionsPipeSeparated() + ") is currently out of stock.");
        this.productVariant = productVariant;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

}
