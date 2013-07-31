package com.hk.admin.pact.dao.inventory;

import java.util.List;

import com.hk.admin.dto.inventory.CreateInventoryFileDto;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.domain.inventory.StockTransferLineItem;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.catalog.product.VariantConfig;
import com.hk.pact.dao.BaseDao;

public interface AdminProductVariantInventoryDao extends BaseDao {

    public Long getCheckedoutItemCount(LineItem lineItem);

    public Long getChechedinItemCount(GrnLineItem grnLineItem);

//    public void resetInventoryByBrand(String brand);

    public void removeInventory(SkuItem skuItem);

    public Long getCheckedInPVIAgainstReturn(LineItem lineItem);

    public List<ProductVariantInventory> getPVIForRV(Sku sku, RvLineItem rvLineItem);

    public List<ProductVariantInventory> getPVIByLineItem(LineItem lineItem);

    public List<ProductVariantInventory> getCheckedOutSkuItems(ShippingOrder shippingOrder, LineItem lineItem);

/*
    public List<CreateInventoryFileDto> getDetailsForUncheckedItems(String brand);
*/

    public List<CreateInventoryFileDto> getDetailsForUncheckedItems(String brand, Warehouse warehouse);

    public Long getCheckedinItemCountForStockTransferLineItem(StockTransferLineItem stockTransferLineItem);

    public List<ProductVariantInventory> getPVIForStockTransfer(Sku sku, StockTransferLineItem stockTransferLineItem);

    public void updateProductVariantsConfig(String id, Long variantconfigId);

    public List<VariantConfig> getAllVariantConfig();

    public List<SkuItem> getCheckedInOrOutSkuItems(RvLineItem rvLineItem, StockTransferLineItem stockTransferLineItem, GrnLineItem grnLineItem, LineItem lineItem, Long transferQty);

    public List<CreateInventoryFileDto> getCheckedInSkuGroup(String brand, Warehouse warehouse, Product product, ProductVariant productVariant);

    public void deletePVIBySkuItem(List<SkuItem> skuItemList);

}