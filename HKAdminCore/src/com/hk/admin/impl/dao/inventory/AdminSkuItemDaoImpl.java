package com.hk.admin.impl.dao.inventory;

import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.constants.sku.EnumSkuGroupStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class AdminSkuItemDaoImpl extends BaseDaoImpl implements AdminSkuItemDao {
                                                  
  public List<SkuGroup> getInStockSkuGroups(List<ProductVariant> productVariantList, Warehouse warehouse) {
    String query = "select distinct si.skuGroup from SkuItem si where si.skuGroup.sku.productVariant in (:productVariantList) " +
        " and si.skuGroup.sku.warehouse =:warehouse and si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId() +
        " order by si.skuGroup.expiryDate asc ";
    List<SkuGroup> skuGroupList = findByNamedParams(query, new String[]{"productVariantList", "warehouse"}, new Object[]{productVariantList, warehouse});
    //List<SkuGroup> skuGroupList = (List<SkuGroup>) getSession().createQuery(query).setParameterList("productVariantList", productVariantList).setParameter("warehouse",warehouse).list();

    if (skuGroupList == null) {
      skuGroupList = new ArrayList<SkuGroup>(0);
    }
    return skuGroupList;
  }


  public List<SkuGroup> getInStockSkuGroupsForReview(LineItem lineItem) {
    List<Long> skuItemStatusIdList = new ArrayList<Long>();
    skuItemStatusIdList.add(EnumSkuItemStatus.Checked_IN.getId());
    skuItemStatusIdList.add(EnumSkuItemStatus.BOOKED.getId());
    skuItemStatusIdList.add(EnumSkuItemStatus.TEMP_BOOKED.getId());
    String query = "select distinct si.skuGroup from SkuItem si where si.skuGroup.sku = :sku " +
        " and si.skuItemStatus.id in (:skuItemStatusIdList) order by si.skuGroup.expiryDate asc ";
    List<SkuGroup> skuGroupList = (List<SkuGroup>) getSession().createQuery(query).setParameter("sku", lineItem.getSku()).
        setParameterList("skuItemStatusIdList", skuItemStatusIdList).list();

    if (skuGroupList == null) {
      skuGroupList = new ArrayList<SkuGroup>(0);
    }
    return skuGroupList;
  }


  public List<SkuGroup> getSkuGroupsInReviewState() {
    String query = "select sg from SkuGroup sg  where sg.status = :status";
    List<SkuGroup> skuGroupList = (List<SkuGroup>) getSession().createQuery(query).setParameter("status", (EnumSkuGroupStatus.UNDER_REVIEW)).list();
    if (skuGroupList == null) {
      skuGroupList = new ArrayList<SkuGroup>(0);
    }
    return skuGroupList;
  }

  public List<SkuGroup> getInStockSkuGroups(Sku sku) {
    String query = "select distinct si.skuGroup from SkuItem si where si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId() +
        " and si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc, si.skuGroup.mfgDate asc, si.skuGroup.createDate asc ";
    List<SkuGroup> skuGroupList = findByNamedParams(query, new String[]{"sku"}, new Object[]{sku});
    //List<SkuGroup> skuGroupList = (List<SkuGroup>) getSession().createQuery(query).setParameter("sku", sku).list();

    if (skuGroupList == null) {
      skuGroupList = new ArrayList<SkuGroup>(0);
    }
    return skuGroupList;
  }

  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup) {
    String query = "select si from SkuItem si where si.skuGroup = :skuGroup and si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId();
    List<SkuItem> inStockSkuItems = findByNamedParams(query, new String[]{"skuGroup"}, new Object[]{skuGroup});
    //List<SkuItem> inStockSkuItems = (List<SkuItem>) getSession().createQuery(query).setParameter("skuGroup", skuGroup).list();

    if (inStockSkuItems == null) {
      inStockSkuItems = new ArrayList<SkuItem>(0);
    }
    return inStockSkuItems;
  }


  public List<SkuItem> getNetPhysicalAvailableStockSkuItems(SkuGroup skuGroup) {

    List<Long> statusIds = Arrays.asList(EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemStatus.BOOKED.getId(), EnumSkuItemStatus.Checked_IN.getId());
    //String query = "select sum(pvi.qty) from ProductVariantInventory pvi where pvi.sku in (:skuList)";
    String query = "select si from SkuItem si where si.skuGroup = :skuGroup and  si.skuItemStatus.id in (:skuItemStatuses) ";
    List<SkuItem> inStockSkuItems = (List<SkuItem>) getSession().createQuery(query).setParameter("skuGroup", skuGroup).setParameterList("skuItemStatuses", statusIds).list();
    if (inStockSkuItems == null) {
      inStockSkuItems = new ArrayList<SkuItem>(0);
    }
    return inStockSkuItems;
  }


  //This seems to be wrong hence deprecating it, please use it at your own risk
  public List<SkuItem> getCheckedInSkuItems(SkuGroup skuGroup) {
    List<SkuItem> inStockSkuItems = new ArrayList<SkuItem>();
    String inStockSkuItemIdQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem.skuGroup =:skuGroup and pvi.qty = 1";
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
    String query = "select si from SkuItem si where si.skuGroup.sku in (:skuList) and si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId();
    List<SkuItem> inStockSkuItems = findByNamedParams(query, new String[]{"skuList"}, new Object[]{skuList});
    //List<SkuItem> inStockSkuItems = (List<SkuItem>) getSession().createQuery(query).setParameterList("skuList", skuList).list();

    if (inStockSkuItems == null) {
      inStockSkuItems = new ArrayList<SkuItem>(0);
    }
    return inStockSkuItems;
  }


  public List<SkuItem> getInStockSkuItems(String barcode, Warehouse warehouse) {
    String query = "select si from SkuItem si where si.skuGroup.barcode = :barcode and si.skuGroup.sku.warehouse = :warehouse " +
        " and si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId() + " order by si.id ";
    return findByNamedParams(query, new String[]{"barcode", "warehouse"}, new Object[]{barcode, warehouse});
  }


  @Override
  public List<SkuItem> getInStockSkuItems(String barcode, Warehouse warehouse, List<SkuItemStatus> skuItemStatusList, List<SkuItemOwner> skuItemOwners) {
    String sql = "select si from SkuItem si where si.skuGroup.barcode = :barcode and si.skuGroup.sku.warehouse = :warehouse ";
    if (skuItemStatusList != null && skuItemStatusList.size() > 0) {
      sql = sql + "and si.skuItemStatus  in (:skuItemStatusList) ";
    }
    if (skuItemOwners != null && skuItemOwners.size() > 0) {
      sql = sql + "and si.skuItemOwner in (:skuItemOwners)";
    }

    sql += " order by si.id";
    Query query = getSession().createQuery(sql).setParameter("barcode", barcode).setParameter("warehouse", warehouse);
    if (skuItemStatusList != null && skuItemStatusList.size() > 0) {
      query.setParameterList("skuItemStatusList", skuItemStatusList);
    }
    if (skuItemOwners != null && skuItemOwners.size() > 0) {
      query.setParameterList("skuItemOwners", skuItemOwners);
    }

    return query.list();
  }

}
