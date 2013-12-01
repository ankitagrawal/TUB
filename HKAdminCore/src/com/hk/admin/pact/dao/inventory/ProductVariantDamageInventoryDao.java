package com.hk.admin.pact.dao.inventory;

import com.hk.domain.inventory.ProductVariantDamageInventory;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItem;
import com.hk.pact.dao.BaseDao;

public interface ProductVariantDamageInventoryDao extends BaseDao {

    public ProductVariantDamageInventory getCheckedInPVDI(SkuItem skuItem);

    public Long getCheckedInPVDIAgainstRTO(LineItem lineItem);
}