package com.hk.dao.sku;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;

@SuppressWarnings("unchecked")
@Repository
public class SkuItemDao extends BaseDaoImpl {

    
    public List<SkuGroup> getInStockSkuGroups(Sku sku) {
        List<SkuGroup> skuGroupList = new ArrayList<SkuGroup>();
        String skuItemListQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem is not null "
                + "and pvi.sku = :sku group by pvi.skuItem.id having sum(pvi.qty) > 0";
        List<Long> skuItemIdList = (List<Long>) getSession().createQuery(skuItemListQuery).setParameter("sku", sku).list();
        if (skuItemIdList != null && skuItemIdList.size() > 0) {
            String query = "select distinct si.skuGroup from SkuItem si where si.id in (:skuItemIdList) " + "and si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc";
            skuGroupList = (List<SkuGroup>) getSession().createQuery(query).setParameterList("skuItemIdList", skuItemIdList).setParameter("sku", sku).list();
        }
        return skuGroupList;
    }


    /*public SkuItem getSkuItemToValidateDayZeroInventory(ProductVariant productVariant, String batchNumber) {
        String query = "select si from SkuItem si where si.skuGroup.sku.productVariant = :productVariant and si.skuGroup.batchNumber = :batchNumber";
        List<SkuItem> skuItems = (List<SkuItem>) getSession().createQuery(query).setParameter("productVariant", productVariant).setParameter("batchNumber", batchNumber).list();
        if (skuItems != null && skuItems.size() > 0) {
            return skuItems.get(0);
        }
        return null;
    }
*/
    

    

    /*public List<Warehouse> getWarehousesForSkuAndQty(List<Sku> skuList, Long qty) {
        String inStockSkusQuery = "select distinct pvi.sku.warehouse from ProductVariantInventory pvi where pvi.sku in (:skuList) "
                + "group by pvi.sku having sum(pvi.qty) >= :qty order by pvi.sku.warehouse.id desc";
        return getSession().createQuery(inStockSkusQuery).setParameterList("skuList", skuList).setParameter("qty", qty).list();
    }*/

    
    /*public void resetInventoryByBrand(String brand) {
        List<Long> toBeRemovedIds = (List<Long>) getSession().createQuery("select id from SkuItem si where si.skuGroup.sku.productVariant.product.brand = :brand").setParameter(
                "brand", brand).list();
        if (toBeRemovedIds != null && !toBeRemovedIds.isEmpty()) {
            getSession().createQuery("delete from SkuItem si where si.id in (:toBeRemovedIds)").setParameterList("toBeRemovedIds", toBeRemovedIds).executeUpdate();
        }
    }*/

   /* public void resetInventory(ProductVariant productVariant) {
        List<Long> toBeRemovedIds = (List<Long>) getSession().createQuery("select id from SkuItem si where si.skuGroup.sku.productVariant = :productVariant").setParameter(
                "productVariant", productVariant).list();
        if (toBeRemovedIds != null && !toBeRemovedIds.isEmpty()) {
            getSession().createQuery("delete from SkuItem si where si.id in (:toBeRemovedIds)").setParameterList("toBeRemovedIds", toBeRemovedIds).executeUpdate();
        }
    }*/

}