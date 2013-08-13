package com.hk.pact.dao.inventory;

import java.util.List;

import com.hk.domain.sku.Sku;
import com.hk.pact.dao.BaseDao;

public interface ProductVariantInventoryDao extends BaseDao {

  //todo -- to be deleted and replaced by inventory service meth
    public Long getNetInventory(Sku sku);
     //todo -- to be deleted and replaced by inventory service meth
    public Long getNetInventory(List<Sku> skuList);

}
