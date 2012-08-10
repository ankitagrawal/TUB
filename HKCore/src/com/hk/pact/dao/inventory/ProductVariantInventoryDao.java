package com.hk.pact.dao.inventory;

import com.hk.domain.sku.Sku;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface ProductVariantInventoryDao extends BaseDao {

    public Long getNetInventory(Sku sku);

    public Long getNetInventory(List<Sku> skuList);

}
