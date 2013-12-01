package com.hk.admin.dto.inventory;


import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;


/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 4/3/13
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class CycleCountDto {

    String brand;
    Product product;
    ProductVariant productVariant;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }
}
