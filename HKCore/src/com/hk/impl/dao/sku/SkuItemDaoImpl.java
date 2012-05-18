package com.hk.impl.dao.sku;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.sku.SkuItemDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class SkuItemDaoImpl extends BaseDaoImpl implements SkuItemDao {

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


  public SkuItem getSkuItemToValidateDayZeroInventory(ProductVariant productVariant, String batchNumber) {
    String query = "select si from SkuItem si where si.skuGroup.sku.productVariant = :productVariant and si.skuGroup.batchNumber = :batchNumber";
    List<SkuItem> skuItems = (List<SkuItem>) getSession().createQuery(query)
        .setParameter("productVariant", productVariant)
        .setParameter("batchNumber", batchNumber)
        .list();
    if (skuItems != null && skuItems.size() > 0) {
      return skuItems.get(0);
    }
    return null;
  }

 public List<Warehouse> getWarehousesForSkuAndQty(List<Sku> skuList, Long qty) {
    String inStockSkusQuery = "select distinct pvi.sku.warehouse from ProductVariantInventory pvi where pvi.sku in (:skuList) " +
        "group by pvi.sku having sum(pvi.qty) >= :qty order by pvi.sku.warehouse.id desc";
    return getSession().createQuery(inStockSkusQuery)
        .setParameterList("skuList", skuList)
        .setParameter("qty", qty)
        .list();
  }


 public void resetInventoryByBrand(String brand) {
    List<Long> toBeRemovedIds = (List<Long>) getSession().
        createQuery("select id from SkuItem si where si.skuGroup.sku.productVariant.product.brand = :brand").
        setParameter("brand", brand).
        list();
    if (toBeRemovedIds != null && !toBeRemovedIds.isEmpty()) {
      getSession().
          createQuery("delete from SkuItem si where si.id in (:toBeRemovedIds)").
          setParameterList("toBeRemovedIds", toBeRemovedIds).
          executeUpdate();
    }
  }


  public void resetInventory(ProductVariant productVariant) {
    List<Long> toBeRemovedIds = (List<Long>)  getSession().
        createQuery("select id from SkuItem si where si.skuGroup.sku.productVariant = :productVariant").
        setParameter("productVariant", productVariant).
        list();
    if (toBeRemovedIds != null && !toBeRemovedIds.isEmpty()) {
       getSession().
          createQuery("delete from SkuItem si where si.id in (:toBeRemovedIds)").
          setParameterList("toBeRemovedIds", toBeRemovedIds).
          executeUpdate();
    }
  }


  //added

  public void resetInventoryBySkuItem(SkuItem skuitem) {

    if (skuitem != null ) {
      getSession().
          createQuery("delete from SkuItem si where si.id = :toBeRemovedId").
          setParameter("toBeRemovedId", skuitem.getId()).
          executeUpdate();
    }
  }


  //added
  public List<SkuGroup> getInStockSkuGroupsByCreateDate(Sku sku) {
     List<SkuGroup> skuGroupList = new ArrayList<SkuGroup>();
     String skuItemListQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem is not null " +
         "and pvi.sku = :sku group by pvi.skuItem.id having sum(pvi.qty) > 0";
     List<Long> skuItemIdList = (List<Long>) getSession().createQuery(skuItemListQuery)
         .setParameter("sku", sku)
         .list();
     if (skuItemIdList != null && skuItemIdList.size() > 0) {
       String query = "select distinct si.skuGroup from SkuItem si where si.id in (:skuItemIdList) " +
           "and si.skuGroup.sku = :sku order by si.skuGroup.createDate asc";
       skuGroupList = (List<SkuGroup>) getSession().createQuery(query)
           .setParameterList("skuItemIdList", skuItemIdList)
           .setParameter("sku", sku)
           .list();
     }
     return skuGroupList;
   }
      //added
    public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup) {
    List<SkuItem> inStockSkuItems = new ArrayList<SkuItem>();
    String inStockSkuItemIdQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem.skuGroup =:skuGroup group by pvi.skuItem.id having sum(pvi.qty) > 0";
    List<Long> inStockSkuItemIds = (List<Long>) getSession().createQuery(inStockSkuItemIdQuery)
        .setParameter("skuGroup", skuGroup)
        .list();
    if (inStockSkuItemIds != null && inStockSkuItemIds.size() > 0) {
      String query = "select si from SkuItem si where si.skuGroup = :skuGroup and si.id in (:inStockSkuItemIds)";
      inStockSkuItems = (List<SkuItem>) getSession().createQuery(query)
          .setParameter("skuGroup", skuGroup)
          .setParameterList("inStockSkuItemIds", inStockSkuItemIds)
          .list();
    }
    return inStockSkuItems;
  }

      }