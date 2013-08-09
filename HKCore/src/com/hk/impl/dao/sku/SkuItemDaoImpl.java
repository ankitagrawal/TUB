package com.hk.impl.dao.sku;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuGroupStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.constants.warehouse.EnumWarehouseType;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.sku.*;
import com.hk.domain.warehouse.Warehouse;
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
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class SkuItemDaoImpl extends BaseDaoImpl implements SkuItemDao {
  @Autowired
  SkuGroupDao skuGroupDao;
  @Autowired
  WarehouseDao warehouseDao;


  public List<SkuGroup> getInStockSkuGroups(Sku sku) {
    String query = "select distinct si.skuGroup from SkuItem si where si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId() +
        " and si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc ";
    List<SkuGroup> skuGroupList = findByNamedParams(query, new String[]{"sku"}, new Object[]{sku});
    //List<SkuGroup> skuGroupList = (List<SkuGroup>) getSession().createQuery(query).setParameter("sku", sku).list();

    if (skuGroupList == null) {
      skuGroupList = new ArrayList<SkuGroup>(0);
    }
    return skuGroupList;
  }

  public SkuGroup getMinMRPUnbookedSkuGroup(ProductVariant productVariant, Long bookedQty) {
    List<Warehouse> warehouseList = warehouseDao.getAllWarehouses(EnumWarehouseType.Online_B2B.getId(), Boolean.TRUE, Boolean.TRUE);
    SkuGroup minMRPUnbookedSkuGroup = null;
    String skuItemListQuery = "select pvi.skuItem.id from ProductVariantInventory pvi " +
        "where pvi.skuItem is not null and pvi.skuItem.skuGroup.mrp is not null and pvi.sku.warehouse in (:warehouseList) and pvi.sku.productVariant = :productVariant " +
        "group by pvi.skuItem.id having sum(pvi.qty) > 0 order by pvi.skuItem.skuGroup.mrp asc";
    List<Long> skuItemIdList = (List<Long>) getSession().createQuery(skuItemListQuery)
        .setParameter("productVariant", productVariant)
        .setParameterList("warehouseList", warehouseList).list();
    if (skuItemIdList != null && skuItemIdList.size() > bookedQty) {
      List<Long> firstUnBookedSkuItem = skuItemIdList.subList(bookedQty.intValue(), bookedQty.intValue() + 1);
      String query = "select distinct si.skuGroup from SkuItem si where si.id = :skuItemId order by si.skuGroup.mrp asc";
      minMRPUnbookedSkuGroup = (SkuGroup) getSession().createQuery(query).setParameter("skuItemId", firstUnBookedSkuItem.get(0)).uniqueResult();
    }
    return minMRPUnbookedSkuGroup;
  }


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

  public SkuItem getSkuItem(SkuGroup skuGroup, SkuItemStatus skuItemStatus) {
    DetachedCriteria criteria = DetachedCriteria.forClass(SkuItem.class);
    criteria.add(Restrictions.eq("skuGroup", skuGroup));
    criteria.add(Restrictions.eq("skuItemStatus", skuItemStatus));
    List<SkuItem> skuItems = (List<SkuItem>) findByCriteria(criteria);
    return skuItems == null || skuItems.isEmpty() ? null : skuItems.get(0);
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


  public List<SkuItem> getCheckedInSkuItems(Sku sku) {
    List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
    skuItemStatusList.add(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
    skuItemStatusList.add(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
    skuItemStatusList.add(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());

    List<SkuItemOwner> skuItemOwnerList = new ArrayList<SkuItemOwner>();
    skuItemOwnerList.add(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());

    String sql = "from SkuItem si where  si.skuItemStatus in :skuItemStatusList and si.skuItemOwner in (:skuItemOwnerList) and  si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc";
    Query query = getSession().createQuery(sql).setParameter("sku", sku).setParameterList("skuItemStatusList", skuItemStatusList).setParameterList("skuItemOwnerList", skuItemOwnerList);
    return query.list();
  }


  public List<SkuItem> getSkuItem(Sku sku, Long id) {
    String sql = "from SkuItem si where  si.skuItemStatus.id =  :checkedInStatusId  and  si.skuGroup.sku = :sku order by si.skuGroup.expiryDate asc";
    Query query = getSession().createQuery(sql).setParameter("sku", sku).setParameter("checkedInStatusId", id);
    return query.list();
  }

  public List<SkuItem> getSkuItems(List<Sku> skuList, List<Long> statusIds, List<SkuItemOwner> skuItemOwners, Double mrp) {
    String sql = "from SkuItem si where si.skuGroup.sku in (:skuList)";

    if (statusIds != null && statusIds.size() > 0) {
      sql += "and si.skuItemStatus.id in (:statusIds)";
    }
    if (skuItemOwners != null && skuItemOwners.size() > 0) {
      sql += "and si.skuItemOwner in (:skuItemOwners)";
    }
    if (mrp != null) {
      sql += " and si.skuGroup.mrp = :mrp ";
    }
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


  public SkuItem getSkuItemWithStatusAndOwner(SkuGroup skuGroup, SkuItemStatus skuItemStatus, SkuItemOwner skuItemOwner) {
    String sql;
    Query query = null;
    if (skuItemOwner != null) {
      sql = "from SkuItem si where si.skuGroup =:skuGroup and si.skuItemStatus = :skuItemStatus and si.skuItemOwner = :skuItemOwner";
      query = getSession().createQuery(sql).setParameter("skuGroup", skuGroup).setParameter("skuItemStatus", skuItemStatus).setParameter("skuItemOwner", skuItemOwner);
    } else {
      sql = "from SkuItem si where si.skuGroup =:skuGroup and si.skuItemStatus = :skuItemStatus";
      query = getSession().createQuery(sql).setParameter("skuGroup", skuGroup).setParameter("skuItemStatus", skuItemStatus);
    }
    List<SkuItem> skuItems = query.list();
    return skuItems != null && !skuItems.isEmpty() ? skuItems.get(0) : null;
  }

  public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId, SkuItemOwner skuItemOwner) {
    String sql = "select si from SkuItem si where si.barcode = :barcode and si.skuGroup.sku.warehouse.id = :warehouseId ";
    if (statusId != null) {
      sql = sql + "and si.skuItemStatus.id = :statusId ";
    }
    if (skuItemOwner != null) {
      sql = sql + "and si.skuItemOwner = :skuItemOwner";
    }
    Query query = getSession().createQuery(sql).setParameter("barcode", barcode).setParameter("warehouseId", warehouseId);
    if (statusId != null) {
      query.setParameter("statusId", statusId);
    }
    if (skuItemOwner != null) {
      query.setParameter("skuItemOwner", skuItemOwner);
    }
    List<SkuItem> skuItems = query.list();
    if (skuItems != null && skuItems.size() > 1) {
      logger.error(" barcode -> " + barcode + " resulting in more than on sku_item in warehouse id " + warehouseId);
    }
    return skuItems != null && !skuItems.isEmpty() ? skuItems.get(0) : null;
  }


  public SkuItem getSkuItem(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatusList) {
    DetachedCriteria criteria = DetachedCriteria.forClass(SkuItem.class);
    criteria.add(Restrictions.eq("skuGroup", skuGroup));
    criteria.add(Restrictions.in("skuItemStatus", skuItemStatusList));
    List<SkuItem> skuItems = (List<SkuItem>) findByCriteria(criteria);
    return skuItems == null || skuItems.isEmpty() ? null : skuItems.get(0);
  }

  // checked in as always with  owner self
  public Long getAvailableUnBookedInventory(List<Sku> skuList) {
    Long netInv = 0L;
    if (skuList != null && !skuList.isEmpty()) {
      //String query = "select sum(pvi.qty) from ProductVariantInventory pvi where pvi.sku in (:skuList)";
      String query = "select count(si) from SkuItem si where si.skuGroup.status != :skuStatus and si.skuGroup.sku in (:skuList) and si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId();
      netInv = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).setParameter("skuStatus", EnumSkuGroupStatus.UNDER_REVIEW).uniqueResult();
      if (netInv == null) {
        netInv = 0L;
      }
    }
    return netInv;
  }


  public Long getNetInventory(List<Sku> skuList, List<Long> skuItemStatuses) {
    Long netInv = 0L;
    if (skuList != null && !skuList.isEmpty()) {
      //String query = "select sum(pvi.qty) from ProductVariantInventory pvi where pvi.sku in (:skuList)";
      String query = "select count(si) from SkuItem si where si.skuGroup.status != :skuStatus and si.skuGroup.sku in (:skuList) and si.skuItemStatus.id in (:skuItemStatuses) ";
      netInv = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).setParameterList("skuItemStatuses", skuItemStatuses).setParameter("skuStatus", EnumSkuGroupStatus.UNDER_REVIEW).uniqueResult();
      if (netInv == null) {
        netInv = 0L;
      }
    }
    return netInv;
  }


  public Long getNetInventory(List<Sku> skuList, Double mrp) {
    Long netInv = 0L;
    if (skuList != null && !skuList.isEmpty()) {
      //String query = "select sum(pvi.qty) from ProductVariantInventory pvi where pvi.sku in (:skuList)";
      String query = "select count(si) from SkuItem si where si.skuGroup.status != :skuStatus and si.skuGroup.sku in (:skuList) " +
          "and si.skuGroup.mrp = :mrp and si.skuItemStatus.id = " + EnumSkuItemStatus.Checked_IN.getId();
      netInv = (Long) getSession().createQuery(query).setParameterList("skuList", skuList).setParameter("skuStatus", EnumSkuGroupStatus.UNDER_REVIEW).setParameter("mrp", mrp).uniqueResult();
      if (netInv == null) {
        netInv = 0L;
      }
    }
    return netInv;
  }


  public Long getTempOrBookedQtyOfProductVariantInQueue(ProductVariant productVariant, List<Long> skuItemStatusId, List<Long> skuItemOwnerStatusId) {
    String query = "select count(si) from SkuItem si where si.skugroup.productVariant = :productVariant and si.skuItemStatus.id= :skuItemStatusId and  si.skuItemOwner.id= :skuItemOwnerStatusId";
    return (Long) getSession().createQuery(query).setParameter("productVariant", productVariant).setParameter("skuItemStatusId", skuItemStatusId).setParameter("skuItemOwnerStatusId", skuItemOwnerStatusId).uniqueResult();

  }


  public Long getAvailableUnbookedInventory(List<Sku> skuList, boolean addBrightInventory) {
    Long netInventory = getAvailableUnBookedInventory(skuList);
    logger.debug("net inventory " + netInventory);

    Long bookedInventory = 0L;
    if (!skuList.isEmpty()) {
      ProductVariant productVariant = skuList.get(0).getProductVariant();
//            bookedInventory = getTempOrBookedQtyOfProductVariantInQueue(productVariant, EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemOwner.SELF.getId()) + getTempOrBookedQtyOfProductVariantInQueue(productVariant, EnumSkuItemStatus.BOOKED.getId(), EnumSkuItemOwner.SELF.getId());
      logger.debug("booked inventory " + bookedInventory);
    }

    return (netInventory - bookedInventory);

  }


  public List<SkuItem> getCheckedInSkuItems(Sku sku, Double mrp) {
    String sql = " from SkuItem si where si.skuGroup.sku =:sku and si.skuGroup.mrp =:mrp and si.skuItemStatus.id = :skuItemStatusId and (si.skuGroup.status != :reviewStatus or si.skuGroup.status is null)";
    Query query = getSession().createQuery(sql).setParameter("sku", sku).setParameter("mrp", mrp).setParameter("skuItemStatusId", EnumSkuItemStatus.Checked_IN.getId()).setParameter("reviewStatus", EnumSkuGroupStatus.UNDER_REVIEW);
    return query.list();

  }


  public Long getAvailableUnbookedInventory(List<Sku> skuList, Double mrp, boolean addBrightInventory) {
    Long netInventory = getNetInventory(skuList, mrp);
    logger.debug("net inventory " + netInventory);

    Long bookedInventory = 0L;
    if (!skuList.isEmpty()) {
      ProductVariant productVariant = skuList.get(0).getProductVariant();
//               bookedInventory = getTempOrBookedQtyOfProductVariantInQueue(productVariant, EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemOwner.SELF.getId()) + getTempOrBookedQtyOfProductVariantInQueue(productVariant, EnumSkuItemStatus.BOOKED.getId(), EnumSkuItemOwner.SELF.getId());
      logger.debug("booked inventory " + bookedInventory);
    }

    return (netInventory - bookedInventory);

  }


  public Double getFirstcheckedInBatchMRP(ProductVariant productVariant) {
    String sql = "Select si.skuGroup.mrp from SkuItem si where si.skuGroup.sku.productVariant =:productVariant and  si.skuItemStatus.id = :skuItemStatusId and ( si.skuGroup.status != :reviewStatus or si.skuGroup.status is null ) group by si.skuGroup.id order by si.skuGroup.createDate asc  ";
    Query query = getSession().createQuery(sql).setParameter("productVariant", productVariant).setParameter("skuItemStatusId", EnumSkuItemStatus.Checked_IN.getId()).setParameter("reviewStatus", EnumSkuGroupStatus.UNDER_REVIEW);
    return (Double) query.list().get(0);

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


  public List<CartLineItem> getClisForInPlacedOrder(ProductVariant productVariant, Double mrp) {

    String query = "from CartLineItem c  where  c.productVariant = :productVariant and c.markedPrice = :mrp and c.order.orderStatus.id in (:orderStatusIds)"
        + " and c.skuItemCLIs.size <= 0  order by c.order.createDate  asc ";
    return (List<CartLineItem>) getSession().createQuery(query).setParameterList("orderStatusIds", Arrays.asList(EnumOrderStatus.Placed.getId(), EnumOrderStatus.OnHold.getId())).setParameter("productVariant", productVariant).setParameter("mrp", mrp).list();
  }


  public Long getLatestcheckedInBatchInventoryCount(ProductVariant productVariant) {

    String sql = "Select count(si) from SkuItem si where si.skuGroup.sku.productVariant =:productVariant and  si.skuItemStatus.id = :skuItemStatusId and ( si.skuGroup.status != :reviewStatus or si.skuGroup.status is null ) group by si.skuGroup.id order by si.skuGroup.createDate desc  ";
    Query query = getSession().createQuery(sql).setParameter("productVariant", productVariant).setParameter("skuItemStatusId", EnumSkuItemStatus.Checked_IN.getId()).setParameter("reviewStatus", EnumSkuGroupStatus.UNDER_REVIEW);
    return (Long) query.list().get(0);
  }


  public List<CartLineItem> getClisForOrderInProcessingState(ProductVariant productVariant, Long skuId, Double mrp) {
    List<Long> statuses = new ArrayList<Long>();
    statuses.addAll(EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventory()));
    statuses.add(EnumShippingOrderStatus.SO_ReadyForDropShipping.getId());

    String query = " select c from CartLineItem c inner join c.order as o inner join o.shippingOrders as so  inner join so.lineItems as li where  c.productVariant = :productVariant and c.markedPrice = :mrp and so.shippingOrderStatus.id in (:shippingOrderStatusIds) and li.sku.id = :skuId  "
        + " and c.skuItemCLIs.size <= 0  order by so.targetDispatchDate,so.createDate  asc ";
    return (List<CartLineItem>) getSession().createQuery(query).setParameterList("shippingOrderStatusIds", statuses).setParameter("productVariant", productVariant).setParameter("mrp", mrp).setParameter("skuId", skuId).list();

  }


  public boolean sicliAlreadyExists(CartLineItem cartLineItem) {
    String sql = " from SkuItemCLI sicli where sicli.cartLineItem = :cartlineItem ";
    List<SkuItemCLI> siclis = (List<SkuItemCLI>) getSession().createQuery(sql).setParameter("cartlineItem", cartLineItem).list();
    if (siclis != null && siclis.size() > 0) {
      if (siclis.size() == cartLineItem.getQty()) {
        return true;
      }
    }
    return false;
  }

}