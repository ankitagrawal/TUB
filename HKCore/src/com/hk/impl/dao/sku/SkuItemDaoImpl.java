package com.hk.impl.dao.sku;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuGroupStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.*;
import com.hk.dto.pos.PosProductSearchDto;
import com.hk.dto.pos.PosSkuGroupSearchDto;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.warehouse.WarehouseDao;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class SkuItemDaoImpl extends BaseDaoImpl implements SkuItemDao {
  @Autowired
  SkuGroupDao skuGroupDao;
  @Autowired
  WarehouseDao warehouseDao;

  private DetachedCriteria getSkuItemCriteria(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatus) {
    DetachedCriteria skuItemCriteria = DetachedCriteria.forClass(SkuItem.class);
    if (skuGroup != null) {
      skuItemCriteria.add(Restrictions.eq("skuGroup", skuGroup));
    }
    if (skuItemStatus != null && skuItemStatus.size() > 0) {
      skuItemCriteria.add(Restrictions.in("skuItemStatus", skuItemStatus));
    }
    return skuItemCriteria;
  }


  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup) {
    if (skuGroup == null) {
      return new ArrayList<SkuItem>();
    }
    List<SkuItemStatus> itemStatus = new ArrayList<SkuItemStatus>();
    itemStatus.add(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
    DetachedCriteria skuItemCriteria = getSkuItemCriteria(skuGroup, itemStatus);
    return findByCriteria(skuItemCriteria);
  }

  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatus) {
    if (skuGroup == null) {
      return new ArrayList<SkuItem>();
    }
    DetachedCriteria skuItemCriteria = getSkuItemCriteria(skuGroup, skuItemStatus);
    return findByCriteria(skuItemCriteria);
  }

  public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId) {
    String sql = "select si from SkuItem si where si.barcode = :barcode and si.skuGroup.sku.warehouse.id = :warehouseId ";
    if (statusId != null) {
      sql = sql + "and si.skuItemStatus.id = :statusId ";
    }
    Query query = getSession().createQuery(sql).setParameter("barcode", barcode).setParameter("warehouseId", warehouseId);
    if (statusId != null) {
      query.setParameter("statusId", statusId);
    }
    List<SkuItem> skuItems = query.list();
    if (skuItems != null && skuItems.size() > 1) {
      logger.error(" barcode -> " + barcode + " resulting in more than on sku_item in warehouse id " + warehouseId);
    }
    return skuItems != null && !skuItems.isEmpty() ? skuItems.get(0) : null;
  }


  public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, List<SkuItemStatus> skuItemStatusList, List<SkuItemOwner> skuItemOwners) {
    String sql = "select si from SkuItem si where si.barcode = :barcode and si.skuGroup.sku.warehouse.id = :warehouseId ";
    if (skuItemStatusList != null && skuItemStatusList.size() > 0) {
      sql = sql + "and si.skuItemStatus  in (:skuItemStatusList) ";
    }
    if (skuItemOwners != null && skuItemOwners.size() > 0) {
      sql = sql + "and si.skuItemOwner in (:skuItemOwners)";
    }
    Query query = getSession().createQuery(sql).setParameter("barcode", barcode).setParameter("warehouseId", warehouseId);
    if (skuItemStatusList != null && skuItemStatusList.size() > 0) {
      query.setParameterList("skuItemStatusList", skuItemStatusList);
    }
    if (skuItemOwners != null && skuItemOwners.size() > 0) {
      query.setParameterList("skuItemOwners", skuItemOwners);
    }

    List<SkuItem> skuItems = query.list();
    if (skuItems != null && skuItems.size() > 1) {
      logger.error(" barcode -> " + barcode + " resulting in more than on sku_item in warehouse id " + warehouseId);
    }
    return skuItems != null && !skuItems.isEmpty() ? skuItems.get(0) : null;
  }

  public List<SkuItem> getSkuItems(List<Sku> skuList, List<Long> statusIds, List<Long> skuItemOwners, Double mrp) {
    String sql = "from SkuItem si where si.skuGroup.sku in (:skuList) ";

    if (statusIds != null && statusIds.size() > 0) {
      sql += "and si.skuItemStatus.id in (:statusIds) ";
    }
    if (skuItemOwners != null && skuItemOwners.size() > 0) {
      sql += "and si.skuItemOwner.id in (:skuItemOwners) ";
    }
    if (mrp != null) {
      sql += "and si.skuGroup.mrp = :mrp ";
    }
    sql += "and si.skuGroup.status != :skuStatus ";
    String orderByClause = " order by si.skuGroup.expiryDate asc";
    sql += orderByClause;
    Query query = getSession().createQuery(sql).setParameterList("skuList", skuList);
    if (statusIds != null && statusIds.size() > 0) {
      query.setParameterList("statusIds", statusIds);
    }
    if (skuItemOwners != null && skuItemOwners.size() > 0) {
      query.setParameterList("skuItemOwners", skuItemOwners);
    }
    if (mrp != null) {
      query.setParameter("mrp", mrp);
    }
    query.setParameter("skuStatus", EnumSkuGroupStatus.UNDER_REVIEW);
    return query.list();
  }


  public List<PosProductSearchDto> getCheckedInSkuItems(String productVariantId, String primaryCategory, String productName, String brand, String flavor, String size, String color, String form, Long warehouseId) {

    DetachedCriteria skuItemCriteria = DetachedCriteria.forClass(SkuItem.class);
    skuItemCriteria.add(Restrictions.eq("skuItemStatus", EnumSkuItemStatus.Checked_IN.getSkuItemStatus()));
    DetachedCriteria skuGroupCriteria = skuItemCriteria.createCriteria("skuGroup", "skuGrp");
    DetachedCriteria skuCriteria = skuGroupCriteria.createCriteria("sku", "sku");
    DetachedCriteria warehouseCriteria = skuCriteria.createCriteria("warehouse");
    warehouseCriteria.add(Restrictions.eq("id", warehouseId));
    DetachedCriteria productVariantCriteria = skuCriteria.createCriteria("productVariant", "productVariant");

    if (!StringUtils.isBlank(productVariantId)) {
      productVariantCriteria.add(Restrictions.eq("id", productVariantId));
    }

    DetachedCriteria productCriteria = productVariantCriteria.createCriteria("product", "product");

    if (!StringUtils.isBlank(productName)) {
      productCriteria.add(Restrictions.like("name", "%" + productName + "%"));
    }

    if (!StringUtils.isBlank(brand)) {
      productCriteria.add(Restrictions.eq("brand", brand));
    }

    if (!StringUtils.isBlank(primaryCategory)) {
      DetachedCriteria categoryCriteria = productCriteria.createCriteria("primaryCategory", "primaryCategory");
      categoryCriteria.add(Restrictions.eq("name", primaryCategory));
    }

    productVariantCriteria.createAlias("productOptions", "option", CriteriaSpecification.LEFT_JOIN);
    //.add(Restrictions.or(Restrictions.eq("option.name", "flavor"), Restrictions.eq("option.name", "size")));

    if (!StringUtils.isBlank(flavor)) {
      productVariantCriteria.add(Restrictions.eq("option.name", "flavor"));
      productVariantCriteria.add(Restrictions.like("option.value", "%" + flavor + "%"));
    }

    if (!StringUtils.isBlank(size)) {
      productVariantCriteria.add(Restrictions.eq("option.name", "size"));
      productVariantCriteria.add(Restrictions.like("option.value", "%" + size + "%"));
    }

    if (!StringUtils.isBlank(color)) {
      productVariantCriteria.add(Restrictions.eq("option.name", "color"));
      productVariantCriteria.add(Restrictions.like("option.value", "%" + color + "%"));
    }

    if (!StringUtils.isBlank(form)) {
      productVariantCriteria.add(Restrictions.eq("option.name", "form"));
      productVariantCriteria.add(Restrictions.like("option.value", "%" + form + "%"));
    }

    skuItemCriteria.setProjection(Projections.projectionList().add(Projections.countDistinct("id").as("countId"))
        .add(Projections.property("product.name").as("productName")).add(Projections.groupProperty("skuGrp.sku").as("sku"))
        .add(Projections.property("productVariant.id").as("productVariantId")))
        .addOrder(Order.asc("productVariantId"))
        .setResultTransformer(Transformers.aliasToBean(PosProductSearchDto.class));

    return findByCriteria(skuItemCriteria);
  }

  public List<PosSkuGroupSearchDto> getCheckedInSkuItemsByGroup(String productVariantId, String primaryCategory, String productName, String brand, String flavor, String size, String color, String form, Long warehouseId) {

    DetachedCriteria skuItemCriteria = DetachedCriteria.forClass(SkuItem.class);
    skuItemCriteria.add(Restrictions.eq("skuItemStatus", EnumSkuItemStatus.Checked_IN.getSkuItemStatus()));
    DetachedCriteria skuGroupCriteria = skuItemCriteria.createCriteria("skuGroup", "skuGrp");
    DetachedCriteria skuCriteria = skuGroupCriteria.createCriteria("sku", "sku");
    DetachedCriteria warehouseCriteria = skuCriteria.createCriteria("warehouse");
    warehouseCriteria.add(Restrictions.eq("id", warehouseId));
    DetachedCriteria productVariantCriteria = skuCriteria.createCriteria("productVariant", "productVariant");
    if (!StringUtils.isBlank(productVariantId)) {
      productVariantCriteria.add(Restrictions.eq("id", productVariantId));
    }

    DetachedCriteria productCriteria = productVariantCriteria.createCriteria("product", "product");

    if (!StringUtils.isBlank(productName)) {
      productCriteria.add(Restrictions.like("name", "%" + productName + "%"));
    }

    if (!StringUtils.isBlank(brand)) {
      productCriteria.add(Restrictions.eq("brand", brand));
    }

    if (!StringUtils.isBlank(primaryCategory)) {
      DetachedCriteria categoryCriteria = productCriteria.createCriteria("primaryCategory", "primaryCategory");
      categoryCriteria.add(Restrictions.eq("name", primaryCategory));
    }

    productVariantCriteria.createAlias("productOptions", "option", CriteriaSpecification.LEFT_JOIN);

    if (!StringUtils.isBlank(flavor)) {
      productVariantCriteria.add(Restrictions.eq("option.name", "flavor"));
      productVariantCriteria.add(Restrictions.like("option.value", "%" + flavor + "%"));
    }

    if (!StringUtils.isBlank(size)) {
      productVariantCriteria.add(Restrictions.eq("option.name", "size"));
      productVariantCriteria.add(Restrictions.like("option.value", "%" + size + "%"));
    }

    if (!StringUtils.isBlank(color)) {
      productVariantCriteria.add(Restrictions.eq("option.name", "color"));
      productVariantCriteria.add(Restrictions.like("option.value", "%" + color + "%"));
    }

    if (!StringUtils.isBlank(form)) {
      productVariantCriteria.add(Restrictions.eq("option.name", "form"));
      productVariantCriteria.add(Restrictions.like("option.value", "%" + form + "%"));
    }

    skuItemCriteria.setProjection(Projections.projectionList().add(Projections.countDistinct("id").as("availableInventory"))
        .add(Projections.property("product.name").as("productName"))
        .add(Projections.groupProperty("skuGroup").as("skuGroup"))
        .add(Projections.property("skuGrp.costPrice").as("costPrice"))
        .add(Projections.property("skuGrp.mrp").as("mrp"))
        .add(Projections.property("skuGrp.batchNumber").as("batchNumber"))
        .add(Projections.property("skuGrp.mfgDate").as("mfgDate"))
        .add(Projections.property("skuGrp.expiryDate").as("expiryDate"))
        .add(Projections.property("skuGrp.sku").as("sku"))
        .add(Projections.property("productVariant.id").as("productVariantId")))
        .addOrder(Order.asc("productVariantId"))
        .addOrder(Order.asc("skuGroup"))
        .setResultTransformer(Transformers.aliasToBean(PosSkuGroupSearchDto.class));

    return findByCriteria(skuItemCriteria);
  }


  public SkuItem getSkuItem(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatusList) {
    DetachedCriteria criteria = DetachedCriteria.forClass(SkuItem.class);
    criteria.add(Restrictions.eq("skuGroup", skuGroup));
    criteria.add(Restrictions.in("skuItemStatus", skuItemStatusList));
    List<SkuItem> skuItems = (List<SkuItem>) findByCriteria(criteria);
    return skuItems == null || skuItems.isEmpty() ? null : skuItems.get(0);
  }

  public Long getInventoryCount(List<Sku> skuList, List<Long> skuItemStatuses) {
    Long netInv = 0L;
    if (skuList != null && !skuList.isEmpty()) {
      String query = "select count(si) from SkuItem si where si.skuGroup.status != :skuStatus and si.skuGroup.sku in (:skuList) and si.skuItemStatus.id in (:skuItemStatuses) ";
      netInv = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).setParameterList("skuItemStatuses", skuItemStatuses).setParameter("skuStatus", EnumSkuGroupStatus.UNDER_REVIEW).uniqueResult();
      if (netInv == null) {
        netInv = 0L;
      }
    }
    return netInv;
  }



  public Long getBookedQtyOfSkuInQueue(List<Sku> skuList) {
    Long qtyInQueue = 0L;
    if (skuList != null && !skuList.isEmpty()) {
      String query = "select sum(li.qty) from LineItem li " + "where li.sku in (:skuList) " + "and li.shippingOrder.shippingOrderStatus.id in (:orderStatusIdList) ";
      qtyInQueue = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).setParameterList("orderStatusIdList",
          EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventory())).uniqueResult();
      if (qtyInQueue == null) {
        qtyInQueue = 0L;
      }
    }
    return qtyInQueue;
  }

  public Long getLatestcheckedInBatchInventoryCount(ProductVariant productVariant) {

    String sql = "Select count(si) from SkuItem si where si.skuGroup.sku.productVariant =:productVariant and  si.skuItemStatus.id = :skuItemStatusId and ( si.skuGroup.status != :reviewStatus or si.skuGroup.status is null ) group by si.skuGroup.id order by si.skuGroup.createDate desc  ";
    Query query = getSession().createQuery(sql).setParameter("productVariant", productVariant).setParameter("skuItemStatusId", EnumSkuItemStatus.Checked_IN.getId()).setParameter("reviewStatus", EnumSkuGroupStatus.UNDER_REVIEW);
    return (Long) query.list().get(0);
  }



  public SkuItem getSkuItem(Long fsicliId) {
    String sql = "select si from SkuItem si where si.foreignSkuItemCLI.id = :fsicliId";
    Query query = getSession().createQuery(sql).setParameter("fsicliId" , fsicliId);
     List<SkuItem> skuItems =  (List<SkuItem>)query.list();
    if(skuItems!=null && skuItems.size() > 0){
      return skuItems.get(0);
    }
    return  null;
  }
  
  public SkuItem getSkuItemByBarcode(String barcode){
  	String sql = "from SkuItem si where si.barcode = :barcode";
  	Query query = getSession().createQuery(sql).setParameter("barcode", barcode);
  	return (SkuItem) query.uniqueResult();
  }
}