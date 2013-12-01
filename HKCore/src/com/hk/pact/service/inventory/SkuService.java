package com.hk.pact.service.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;

import java.util.List;
import java.util.Set;

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

  /**
   * this will return a list of all sku's (instance of product variant at multiple warehouses)
   *
   * @param productVariant
   * @return
   */
  public List<Sku> getSKUsForProductVariant(ProductVariant productVariant);

  public List<Sku> getSKUsForProductVariantAtServiceableWarehouses(ProductVariant productVariant);

  public List<Sku> getSKUsForProductVariantAtServiceableWarehouses(ProductVariant productVariant, Order order);

  public List<Sku> getSKUsForMarkingProductOOS(ProductVariant productVariant);

  /**
   * this will return a list of all sku's (instance of product variant at multiple warehouses) based on category.
   *
   * @param category
   * @param brand
   * @param productId
   * @return
   */
  public List<Sku> getSKUs(String category, String brand, String productId);

  /**
   * this will return a list of all sku's of warehouse
   *
   * @param skuSet
   */
  public void insertSKUs(Set<Sku> skuSet) throws Exception;

  public List<Sku> getSKUsByProductNameAndWarehouse(String productName, Long warehouseId);

  public List<Sku> getSkus(ProductVariant productVariant, List<Warehouse> warehouseList);


}
