package com.hk.pact.dao.inventory;

import java.util.List;

import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface ProductVariantInventoryDao extends BaseDao {

    public Long getNetInventory(Sku sku);

    public Long getNetInventory(List<Sku> skuList);

    public Long getNetInventoryInWarehouse(Sku sku, Warehouse warehouse);

    public Long getNetInventoryInWarehouse(List<Sku> skuList, Warehouse warehouse);

}
