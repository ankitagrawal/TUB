package com.hk.admin.impl.dao.inventory;

import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.constants.sku.EnumSkuGroupStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.impl.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class AdminSkuItemDaoImpl extends BaseDaoImpl implements AdminSkuItemDao {

  /*public List<SkuGroup> getInStockSkuGroups(List<ProductVariant> productVariantList, Warehouse warehouse) {
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
  }*/

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

  /*public List<SkuGroup> getInStockSkuGroups(Sku sku) {
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
  }*/

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

  /*public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup) {
      List<SkuItem> inStockSkuItems = new ArrayList<SkuItem>();
      String inStockSkuItemIdQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem.skuGroup =:skuGroup group by pvi.skuItem.id having sum(pvi.qty) > 0";
      List<Long> inStockSkuItemIds = (List<Long>) getSession().createQuery(inStockSkuItemIdQuery).setParameter("skuGroup", skuGroup).list();
      if (inStockSkuItemIds != null && inStockSkuItemIds.size() > 0) {
          String query = "select si from SkuItem si where si.skuGroup = :skuGroup and si.id in (:inStockSkuItemIds)";
          inStockSkuItems = (List<SkuItem>) getSession().createQuery(query).setParameter("skuGroup", skuGroup).setParameterList("inStockSkuItemIds", inStockSkuItemIds).list();
      }
      return inStockSkuItems;
  }*/

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

  /*public List<SkuItem> getInStockSkuItemsBySku(List<Sku> skuList) {
      List<SkuItem> inStockSkuItems = new ArrayList<SkuItem>();
      String inStockSkuItemIdQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.sku in (:skuList) " + "group by pvi.skuItem.id having sum(pvi.qty) > 0";
      List<Long> inStockSkuItemIds = (List<Long>) getSession().createQuery(inStockSkuItemIdQuery).setParameterList("skuList", skuList).list();
      if (inStockSkuItemIds != null && inStockSkuItemIds.size() > 0) {
          String query = "select si from SkuItem si where si.id in (:inStockSkuItemIds) and si.skuGroup.sku in (:skuList)";
          inStockSkuItems = (List<SkuItem>) getSession().createQuery(query).setParameterList("inStockSkuItemIds", inStockSkuItemIds).setParameterList("skuList", skuList).list();
      }
      return inStockSkuItems;
  }*/

  public List<SkuItem> getInStockSkuItemsBySku(List<Sku> skuList) {
    String query = "select si from SkuItem si where si.skuGroup.sku in (:skuList) and si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId();
    List<SkuItem> inStockSkuItems = findByNamedParams(query, new String[]{"skuList"}, new Object[]{skuList});
    //List<SkuItem> inStockSkuItems = (List<SkuItem>) getSession().createQuery(query).setParameterList("skuList", skuList).list();

    if (inStockSkuItems == null) {
      inStockSkuItems = new ArrayList<SkuItem>(0);
    }
    return inStockSkuItems;
  }

  /*public List<SkuItem> getInStockSkuItemsByQty(Sku sku, Integer qty) {
      List<SkuItem> inStockSkuItems = new ArrayList<SkuItem>();
      String inStockSkuItemIdQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.sku =:sku " + "group by pvi.skuItem.id having sum(pvi.qty) > 0";
      List<Long> inStockSkuItemIds = (List<Long>) getSession().createQuery(inStockSkuItemIdQuery).setParameter("sku", sku).list();

      if (inStockSkuItemIds != null && inStockSkuItemIds.size() > 0) {
          if (inStockSkuItemIds.size() > qty) {
              inStockSkuItemIds = inStockSkuItemIds.subList(0, qty);
          }
          String query = "select si from SkuItem si where si.id in (:inStockSkuItemIds) and si.skuGroup.sku = :sku order by si.skuGroup.expiryDate";
          inStockSkuItems = (List<SkuItem>) getSession().createQuery(query).setParameterList("inStockSkuItemIds", inStockSkuItemIds).setParameter("sku", sku).setMaxResults(qty).list();
      }
      return inStockSkuItems;
  }*/

  public List<SkuItem> getInStockSkuItemsByQty(Sku sku, Integer qty) {
    String query = "select si from SkuItem si where si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId() +
        " and si.skuGroup.sku = :sku order by si.skuGroup.expiryDate ";
    List<SkuItem> inStockSkuItems = (List<SkuItem>) getSession().createQuery(query).setParameter("sku", sku).setMaxResults(qty).list();

    if (inStockSkuItems == null) {
      inStockSkuItems = new ArrayList<SkuItem>(0);
    }
    return inStockSkuItems;
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
    List<Long> toBeRemovedIds = (List<Long>) getSession().
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

  /*public List<SkuGroup> getInStockSkuGroupsByCreateDate(Sku sku) {
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
  }*/

  public List<SkuGroup> getInStockSkuGroupsByCreateDate(Sku sku) {

    String query = "select distinct si.skuGroup from SkuItem si where si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId() +
        " and si.skuGroup.sku = :sku order by si.skuGroup.createDate asc";
    List<SkuGroup> skuGroupList = findByNamedParams(query, new String[]{"sku"}, new Object[]{sku});
    //List<SkuGroup> skuGroupList = (List<SkuGroup>) getSession().createQuery(query).setParameter("sku", sku).list();

    if (skuGroupList == null) {
      skuGroupList = new ArrayList<SkuGroup>(0);
    }
    return skuGroupList;
  }

  public List<SkuItem> getInStockSkuItems(List<SkuGroup> skuGroupList) {
    List<SkuItem> inStockSkuItems = new ArrayList<SkuItem>();
    for (SkuGroup skuGroup : skuGroupList) {
      List<SkuItem> skuItemBykuGroup = getInStockSkuItems(skuGroup);
      if (skuItemBykuGroup != null && skuItemBykuGroup.size() > 0) {
        inStockSkuItems.addAll(skuItemBykuGroup);
      }
    }
    return inStockSkuItems;
  }

  public List<SkuItem> getInStockSkuItems(String barcode, Warehouse warehouse) {
    String query = "select si from SkuItem si where si.skuGroup.barcode = :barcode and si.skuGroup.sku.warehouse = :warehouse " +
        " and si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId() + " order by si.id ";
    return findByNamedParams(query, new String[]{"barcode", "warehouse"}, new Object[]{barcode, warehouse});
  }

}
