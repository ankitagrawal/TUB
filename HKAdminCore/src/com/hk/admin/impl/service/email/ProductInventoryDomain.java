package com.hk.admin.impl.service.email;

import com.hk.domain.catalog.product.Product;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 5/30/13
 * Time: 2:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductInventoryDomain implements Comparable<ProductInventoryDomain> {

    private Product product;
    private Integer inventory;

    public ProductInventoryDomain(Product product, Integer inventory) {
        this.product = product;
        this.inventory = inventory;
    }

    public int compareTo(ProductInventoryDomain productInventoryDomain) {
        return this.getInventory().compareTo(productInventoryDomain.getInventory());
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }
}
