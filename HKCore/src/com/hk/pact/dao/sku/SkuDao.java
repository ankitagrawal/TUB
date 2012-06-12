package com.hk.pact.dao.sku;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface SkuDao extends BaseDao {

    public Sku getSku(ProductVariant productVariant, Warehouse warehouse);

    public Sku getMaxVATSku(ProductVariant productVariant);

    public List<Sku> getSkus(ProductVariant productVariant);

    public List<Sku> filterProductVariantsByWarehouse(List<ProductVariant> productVariants, Warehouse warehouse);

}
