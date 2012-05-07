package com.hk.pact.service.inventory;

import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;

public interface SkuService {

    public Sku saveSku(Sku sku);

    /**
     * this will return sku (instance of product variant at selected warehouses)
     * 
     * @param productVariant
     * @param warehouse
     * @return
     */
    public Sku getSKU(ProductVariant productVariant, Warehouse warehouse);

    public Sku findSKU(ProductVariant productVariant, Warehouse warehouse);

    public Sku findMaxVATSKU(ProductVariant productVariant);

    /**
     * this will return a list of all sku's (instance of product variant at multiple warehouses)
     * 
     * @param productVariant
     * @return
     */
    public List<Sku> getSKUsForProductVariant(ProductVariant productVariant);

    /**
     * this will return a list of all sku's (instance of product variant at multiple warehouses) based on category.
     * 
     * @param category
     * @return
     */
    public List<Sku> getSKUs(String category, String brand, String productId);

}
