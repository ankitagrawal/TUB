package com.hk.impl.dao.sku;

import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemOwner;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.sku.SkuGroupDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class SkuGroupDaoImpl extends BaseDaoImpl implements SkuGroupDao {

  public List<SkuGroup> getAllCheckedInBatches(ProductVariant productVariant) {
    return (List<SkuGroup>) getSession().createQuery("from SkuGroup sg where sg.sku.productVariant = :productVariant").setParameter("productVariant", productVariant).list();
  }

  public List<SkuGroup> getAllCheckedInBatches(Sku sku) {
    return (List<SkuGroup>) getSession().createQuery("from SkuGroup sg where sg.sku = :sku").setParameter("sku", sku).list();
  }

  public SkuGroup getInStockSkuGroup(String barcode, Long warehouseId, List<SkuItemStatus> skuItemStatusIds) {
    List<SkuGroup> skuGroups = getSession().
        createQuery("select distinct si.skuGroup from SkuItem si where si.skuGroup.barcode = :barcode and si.skuGroup.sku.warehouse.id = :warehouseId" +
            " and si.skuItemStatus in (:skuItemStatusIds)").
        setParameter("barcode", barcode).setParameter("warehouseId", warehouseId).setParameterList("skuItemStatusIds", skuItemStatusIds).
        list();
    return skuGroups != null && !skuGroups.isEmpty() ? skuGroups.get(0) : null;
  }

  public List<SkuGroup> getCurrentCheckedInBatchGrn(GoodsReceivedNote grn, Sku sku) {
    return (List<SkuGroup>) getSession().
        createQuery("from SkuGroup sg where sg.goodsReceivedNote = :grn and sg.sku = :sku").
        setParameter("grn", grn).setParameter("sku", sku).list();
  }


  public List<SkuGroup> getCurrentCheckedInBatchNotInGrn(GoodsReceivedNote grn, Sku sku) {
    List<SkuGroup> stockSkuGroupsListExcludeGrn = null;
    List<SkuGroup> stockSkuGroupsList = getAllInStockSkuGroups(sku);
    if (stockSkuGroupsList != null && stockSkuGroupsList.size() > 0) {
      String query = "select  si from SkuGroup si where si in (:stockSkuGroupsList) " +
          "and si.goodsReceivedNote != :grn ";
      stockSkuGroupsListExcludeGrn = (List<SkuGroup>) getSession().createQuery(query)
          .setParameterList("stockSkuGroupsList", stockSkuGroupsList)
          .setParameter("grn", grn)
          .list();

    }
    return stockSkuGroupsListExcludeGrn;
  }

  /*public List<SkuGroup> getAllInStockSkuGroups(Sku sku) {
     List<SkuGroup> skuGroupList = new ArrayList<SkuGroup>();
     String skuItemListQuery = "select pvi.skuItem.id from ProductVariantInventory pvi where pvi.skuItem is not null " +
       "and pvi.sku = :sku group by pvi.skuItem.id having sum(pvi.qty) > 0";
     List<Long> skuItemIdList = (List<Long>) getSession().createQuery(skuItemListQuery)
       .setParameter("sku", sku)
       .list();
     if (skuItemIdList != null && skuItemIdList.size() > 0) {
       String query = "select distinct si.skuGroup from SkuItem si where si.id in (:skuItemIdList) " +
              "and si.skuGroup.sku = :sku ";

       skuGroupList = (List<SkuGroup>) getSession().createQuery(query)
         .setParameterList("skuItemIdList", skuItemIdList)
         .setParameter("sku", sku)
         .list();
     }
     return skuGroupList;
     }*/

  public List<SkuGroup> getAllInStockSkuGroups(Sku sku) {

    List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
    skuItemStatusList.add(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
    skuItemStatusList.add(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
    skuItemStatusList.add(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());

    List<SkuItemOwner> skuItemOwnerList = new ArrayList<SkuItemOwner>();
    skuItemOwnerList.add(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());

    String query = "select distinct si.skuGroup from SkuItem si where si.skuItemStatus in (:skuItemStatusList) and si.skuItemOwner in (:skuItemOwnerList)" +
        " and si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc  ";
    List<SkuGroup> skuGroupList = findByNamedParams(query, new String[]{"sku", "skuItemStatusList", "skuItemOwnerList"}, new Object[]{sku, skuItemStatusList, skuItemOwnerList});

    if (skuGroupList == null) {
      skuGroupList = new ArrayList<SkuGroup>(0);
    }
    return skuGroupList;
  }

  private DetachedCriteria getSkuGroupCriteria(List<SkuGroup> skuGroupList, String barcode, String batchNumber, Sku sku) {
    DetachedCriteria skuGroupCriteria = DetachedCriteria.forClass(SkuGroup.class);
    List<Long> skuGroupIds = new ArrayList<Long>();
    if (skuGroupList != null) {
      for (SkuGroup skuGroup : skuGroupList) {
        skuGroupIds.add(skuGroup.getId());
      }
    }

    if (skuGroupIds.size() > 0) {
      skuGroupCriteria.add(Restrictions.in("id", skuGroupIds));
    }

    if (barcode != null) {
      skuGroupCriteria.add(Restrictions.eq("barcode", barcode.trim()));
    }

    if (batchNumber != null) {
      skuGroupCriteria.add(Restrictions.eq("batchNumber", batchNumber.trim()));
    }

    if (sku != null) {
      skuGroupCriteria.add(Restrictions.eq("sku", sku));
    }
    return skuGroupCriteria;

  }

  public List<SkuGroup> getSkuGroup(String barcode, Long warehouseId) {
    DetachedCriteria skuGroupCriteria = getSkuGroupCriteria(null, barcode, null, null);
    skuGroupCriteria.createCriteria("sku", "sk");
    if (warehouseId != null) {
      skuGroupCriteria.add(Restrictions.eq("sk.warehouse.id", warehouseId));
    }
    List<SkuGroup> skuGroupList = findByCriteria(skuGroupCriteria);
    return skuGroupList;
  }

  public List<SkuGroup> getSkuGroupsByBarcodeForStockTransfer(String barcode, Long warehouseId) {
    List<SkuGroup> skuGroups = getSession().
        createQuery("select distinct si.skuGroup from SkuItem si where si.skuGroup.barcode = :barcode and si.skuGroup.sku.warehouse.id = :warehouseId and si.skuItemStatus.id = "
            + EnumSkuItemStatus.Stock_Transfer_Out.getId()).
        setParameter("barcode", barcode).setParameter("warehouseId", warehouseId).list();
    return skuGroups;
  }

  public List<SkuGroup> getSkuGroupByGrnLineItem(GrnLineItem grnLineItem) {

    List<SkuGroup> skuGroups = getSession().createQuery("from SkuGroup  sg  where  sg.goodsReceivedNote  is not null  and  sg.goodsReceivedNote =:grn and  sg.sku =:sku")
        .setParameter("grn", grnLineItem.getGoodsReceivedNote()).setParameter("sku", grnLineItem.getSku()).list();
    return skuGroups;
  }

  public List<SkuGroup> getAllCheckedInBatchForGrn(GoodsReceivedNote grn) {
    return (List<SkuGroup>) getSession().
        createQuery("from SkuGroup sg where sg.goodsReceivedNote = :grn").
        setParameter("grn", grn).list();
  }

}
