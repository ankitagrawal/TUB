package com.hk.admin.impl.dao.inventory;

import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.inventory.ProductVariantDamageInventoryDao;
import com.hk.domain.inventory.ProductVariantDamageInventory;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItem;
import com.hk.impl.dao.BaseDaoImpl;

@Repository                   
public class ProductVariantDamageInventoryDaoImpl extends BaseDaoImpl implements ProductVariantDamageInventoryDao {

    public ProductVariantDamageInventory getCheckedInPVDI(SkuItem skuItem) {
        return (ProductVariantDamageInventory) getSession().createQuery("from ProductVariantDamageInventory pvi where pvi.skuItem = :skuItem and pvi.qty = :qty").setParameter(
                "skuItem", skuItem).setParameter("qty", 1L).uniqueResult();
    }

    public Long getCheckedInPVDIAgainstRTO(LineItem lineItem) {
        return (Long) getSession().createQuery("select count(pvi.id) from ProductVariantDamageInventory pvi where pvi.lineItem = :lineItem and pvi.qty = :qty").setParameter(
                "lineItem", lineItem).setParameter("qty", 1L).uniqueResult();
    }
}