package com.hk.pact.service.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.order.Order;

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

  // todo -- to be deleted
	public Sku findSKU(ProductVariant productVariant, Warehouse warehouse);

  // todo -- to be deleted -- and also deleted commented code in pricing engine
	public Sku findMaxVATSKU(ProductVariant productVariant);

	/**
	 * this will return a list of all sku's (instance of product variant at multiple warehouses)
	 *
	 * @param productVariant
	 * @return
	 */
	public List<Sku> getSKUsForProductVariant(ProductVariant productVariant);

  // todo -- need to merged in one method
	public List<Sku> getSKUsForProductVariantAtServiceableWarehouses(ProductVariant productVariant);

	public List<Sku> getSKUsForProductVariantAtServiceableWarehouses(ProductVariant productVariant, Order order);

	public List<Sku> getSKUsForMarkingProductOOS(ProductVariant productVariant);

	/**
	 * this will return a list of all sku's (instance of product variant at multiple warehouses) based on category.
	 *
	 * @param category
	 * @return
	 */
	public List<Sku> getSKUs(String category, String brand, String productId);

	/**
	 * this will return a list of all sku's of warehouse
	 *
	 * @return
	 */
	public void insertSKUs(Set<Sku> skuSet) throws Exception;

	public List<Sku> getSKUsByProductNameAndWarehouse(String productName, Long warehouseId);

  // todo -- need to merged in one method
 public List<Sku> getSkus(ProductVariant productVariant, List<Warehouse> warehouseList);
  

}
