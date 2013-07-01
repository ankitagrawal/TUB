package com.hk.admin.impl.service.email;

import com.hk.domain.catalog.product.Product;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 5/30/13
 * Time: 2:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductInventoryDto {

    private Product product;
    private Integer inventory;

    public ProductInventoryDto(Product product, Integer inventory) {
        this.product = product;
        this.inventory = inventory;
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

    public static class InventoryComparator implements Comparator<ProductInventoryDto> {
        @Override
        public int compare(ProductInventoryDto o1, ProductInventoryDto o2) {
            return (o1.getInventory().compareTo(o2.getInventory()));
        }


    }
}
