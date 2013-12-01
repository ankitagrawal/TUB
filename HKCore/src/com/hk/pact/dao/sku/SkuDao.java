package com.hk.pact.dao.sku;

import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface SkuDao extends BaseDao {

    public Sku getSku(ProductVariant productVariant, Warehouse warehouse);



    public List<Sku> getSkus(ProductVariant productVariant);

	public List<Sku> getSkus(ProductVariant productVariant, List<Warehouse> warehouseList);

    public List<Sku> filterProductVariantsByWarehouse(List<ProductVariant> productVariants, Warehouse warehouse);

	public List<Sku> getSKUsByProductNameAndWarehouse(String productName, Long warehouseId);
}
