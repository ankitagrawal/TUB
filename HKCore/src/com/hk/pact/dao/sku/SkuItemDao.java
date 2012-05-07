package com.hk.pact.dao.sku;

import java.util.List;

import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.pact.dao.BaseDao;

public interface SkuItemDao extends BaseDao {

    
    public List<SkuGroup> getInStockSkuGroups(Sku sku) ;


}