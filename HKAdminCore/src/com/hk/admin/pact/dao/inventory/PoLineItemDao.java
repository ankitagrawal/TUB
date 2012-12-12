package com.hk.admin.pact.dao.inventory;

import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.sku.Sku;
import com.hk.pact.dao.BaseDao;

public interface PoLineItemDao extends BaseDao {

	public PoLineItem getPoLineItem(PurchaseOrder purchaseOrder, ProductVariant productVariant);

	public int getPoLineItemCountBySku(Sku sku);

}
