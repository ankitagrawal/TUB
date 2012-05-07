package com.hk.impl.dao.sku;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.sku.SkuGroupDao;

@SuppressWarnings("unchecked")
@Repository
public class SkuGroupDaoImpl extends BaseDaoImpl implements SkuGroupDao {

    public List<SkuGroup> getAllCheckedInBatches(ProductVariant productVariant) {
        return (List<SkuGroup>) getSession().createQuery("from SkuGroup sg where sg.sku.productVariant = :productVariant").setParameter("productVariant", productVariant).list();
    }

    public List<SkuGroup> getAllCheckedInBatches(Sku sku) {
        return (List<SkuGroup>) getSession().createQuery("from SkuGroup sg where sg.sku = :sku").setParameter("sku", sku).list();
    }

    public void resetInventoryByBrand(String brand) {
        List<Long> toBeRemovedIds = (List<Long>) getSession().createQuery("select id from SkuGroup sg where sg.sku.productVariant.product.brand = :brand").setParameter("brand",
                brand).list();
        if (toBeRemovedIds != null && !toBeRemovedIds.isEmpty()) {
            getSession().createQuery("delete from SkuGroup sg where sg.id in (:toBeRemovedIds)").setParameterList("toBeRemovedIds", toBeRemovedIds).executeUpdate();
        }
    }

    public void resetInventory(ProductVariant productVariant) {
        List<Long> toBeRemovedIds = (List<Long>) getSession().createQuery("select id from SkuGroup sg where sg.sku.productVariant = :productVariant").setParameter(
                "productVariant", productVariant).list();
        if (toBeRemovedIds != null && !toBeRemovedIds.isEmpty()) {
            getSession().createQuery("delete from SkuGroup sg where sg.id in (:toBeRemovedIds)").setParameterList("toBeRemovedIds", toBeRemovedIds).executeUpdate();
        }
    }

}
