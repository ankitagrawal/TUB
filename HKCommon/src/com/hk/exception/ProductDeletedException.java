package com.hk.exception;

import com.hk.domain.catalog.product.Product;

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 7/11/13
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductDeletedException extends HealthkartRuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Product product;


    public ProductDeletedException(String message, Product product) {
        super(message);
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
