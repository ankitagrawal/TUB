package com.hk.impl.dao.inventory;

import java.util.Arrays;
import java.util.List;

import com.hk.constants.sku.EnumSkuItemStatus;
import org.springframework.stereotype.Repository;

import com.hk.domain.sku.Sku;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.inventory.ProductVariantInventoryDao;

@Repository
public class ProductVariantInventoryDaoImpl extends BaseDaoImpl implements ProductVariantInventoryDao {

    public Long getNetInventory(Sku sku) {
        return getNetInventory(Arrays.asList(sku));
    }

    public Long getNetInventory(List<Sku> skuList) {
        Long netInv = 0L;
        if (skuList != null && !skuList.isEmpty()) {
            //String query = "select sum(pvi.qty) from ProductVariantInventory pvi where pvi.sku in (:skuList)";
	        String query = "select count(si) from SkuItem si where si.skuGroup.sku in (:skuList) and si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId();
            netInv = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).uniqueResult();
            if (netInv == null) {
                netInv = 0L;
            }
        }
        return netInv;
    }
}
