package com.hk.api.dto.brand;

import com.hk.api.dto.product.HKAPIProductDTO;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 5/14/13
 * Time: 2:21 PM
 */
public class HKAPIBrandProductsDTO {

    private String brand;

    private HKAPIProductDTO[] products;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public HKAPIProductDTO[] getProducts() {
        return products;
    }

    public void setProducts(HKAPIProductDTO[] products) {
        this.products = products;
    }
}
