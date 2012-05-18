package com.hk.admin.impl.dao.inventory;

import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class AdminSkuItemDaoImpl extends BaseDaoImpl implements AdminSkuItemDao{

  public List<SkuGroup> getInStockSkuGroups(List<ProductVariant> productVariantList, Warehouse warehouse) {
    List<SkuGroup> skuGroupList = new ArrayList<SkuGroup>();
    String skuItemListQuery = "select pvi.skuItem.id from ProductVariantInventory pvi "
        + "where pvi.skuItem is not null and pvi.sku.productVariant in( :productVariantList) and pvi.skuItem.skuGroup.sku.warehouse =:warehouse "
        + "group by pvi.skuItem.id having sum(pvi.qty) > 0";                                                                
    List<Long> skuItemIdList = (List<Long>) getSession().createQuery(skuItemListQuery).setParameterList("productVariantList", productVariantList).setParameter("warehouse",
        warehouse).list();
    if (skuItemIdList != null && skuItemIdList.size() > 0) {
      String query = "select distinct si.skuGroup from SkuItem si where si.id in (:skuItemIdList) "
          + "and si.skuGroup.sku.productVariant in (:productVariantList) order by si.skuGroup.expiryDate asc";
      skuGroupList = (List<SkuGroup>) getSession().createQuery(query).setParameterList("skuItemIdList", skuItemIdList).setParameterList("productVariantList",
          productVariantList).list();
    }
    return skuGroupList;
  }
  public List<SkuGroup> getInStockSkuGroups(Sku sku) {
    List<SkuGroup> skuGroupList = new ArrayList<SkuGroup>();
    String skuItemListQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem is not null " +
        "and pvi.sku = :sku group by pvi.skuItem.id having sum(pvi.qty) > 0";
    List<Long> skuItemIdList = (List<Long>) getSession().createQuery(skuItemListQuery)
        .setParameter("sku", sku)
        .list();
    if (skuItemIdList != null && skuItemIdList.size() > 0) {
      String query = "select distinct si.skuGroup from SkuItem si where si.id in (:skuItemIdList) " +
          "and si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc, si.skuGroup.mfgDate asc, si.skuGroup.createDate asc ";
      skuGroupList = (List<SkuGroup>) getSession().createQuery(query)
          .setParameterList("skuItemIdList", skuItemIdList)
          .setParameter("sku", sku)
          .list();
    }
    return skuGroupList;
  }
  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup) {
    List<SkuItem> inStockSkuItems = new ArrayList<SkuItem>();
    String inStockSkuItemIdQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem.skuGroup =:skuGroup group by pvi.skuItem.id having sum(pvi.qty) > 0";
    List<Long> inStockSkuItemIds = (List<Long>) getSession().createQuery(inStockSkuItemIdQuery).setParameter("skuGroup", skuGroup).list();
    if (inStockSkuItemIds != null && inStockSkuItemIds.size() > 0) {
      String query = "select si from SkuItem si where si.skuGroup = :skuGroup and si.id in (:inStockSkuItemIds)";
      inStockSkuItems = (List<SkuItem>) getSession().createQuery(query).setParameter("skuGroup", skuGroup).setParameterList("inStockSkuItemIds", inStockSkuItemIds).list();
    }
    return inStockSkuItems;
  }

  public List<SkuItem> getInStockSkuItemsBySku(Sku sku) {
    return getInStockSkuItemsBySku(Arrays.asList(sku));
  }

  public List<SkuItem> getInStockSkuItemsBySku(List<Sku> skuList) {
    List<SkuItem> inStockSkuItems = new ArrayList<SkuItem>();
    String inStockSkuItemIdQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.sku in (:skuList) " + "group by pvi.skuItem.id having sum(pvi.qty) > 0";
    List<Long> inStockSkuItemIds = (List<Long>) getSession().createQuery(inStockSkuItemIdQuery).setParameterList("skuList", skuList).list();
    if (inStockSkuItemIds != null && inStockSkuItemIds.size() > 0) {
      String query = "select si from SkuItem si where si.id in (:inStockSkuItemIds) and si.skuGroup.sku in (:skuList)";
      inStockSkuItems = (List<SkuItem>) getSession().createQuery(query).setParameterList("inStockSkuItemIds", inStockSkuItemIds).setParameterList("skuList", skuList).list();
    }
    return inStockSkuItems;
  }

  public List<SkuItem> getInStockSkuItemsByQty(Sku sku, Integer qty) {
    List<SkuItem> inStockSkuItems = new ArrayList<SkuItem>();
    String inStockSkuItemIdQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.sku =:sku " + "group by pvi.skuItem.id having sum(pvi.qty) > 0";
    List<Long> inStockSkuItemIds = (List<Long>) getSession().createQuery(inStockSkuItemIdQuery).setParameter("sku", sku).list();
    if (inStockSkuItemIds != null && inStockSkuItemIds.size() > 0) {
      String query = "select si from SkuItem si where si.id in (:inStockSkuItemIds) and si.skuGroup.sku = :sku order by si.skuGroup.expiryDate";
      inStockSkuItems = (List<SkuItem>) getSession().createQuery(query).setParameterList("inStockSkuItemIds", inStockSkuItemIds).setParameter("sku", sku).setMaxResults(qty).list();
    }
    return inStockSkuItems;
  }

}
