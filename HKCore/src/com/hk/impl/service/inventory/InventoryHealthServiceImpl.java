package com.hk.impl.service.inventory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


import com.hk.constants.catalog.product.EnumUpdatePVPriceStatus;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.order.EnumUnitProcessedStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuGroupStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.api.HKAPIBookingInfo;
import com.hk.domain.api.HKAPIForeignBookingResponseInfo;
import com.hk.domain.api.HKApiSkuResponse;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
import com.hk.pact.dao.catalog.product.UpdatePvPriceDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.service.ServiceLocatorFactory;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Service
public class InventoryHealthServiceImpl implements InventoryHealthService {


  @Value("#{hkEnvProps['" + Keys.Env.brightlifecareRestUrl + "']}")
  private String brightlifecareRestUrl;
  @Autowired
  ProductVariantService productVariantService;
  @Autowired
  ProductService productService;
  @Autowired
  WarehouseService warehouseService;
  @Autowired
  BaseDao baseDao;
  @Autowired
  UpdatePvPriceDao updatePvPriceDao;
  @Autowired
  SkuItemDao skuItemDao;
  @Autowired
  SkuService skuService;
  @Autowired
  SkuItemLineItemService skuItemLineItemService;
  @Autowired
  LineItemDao lineItemDao;
  @Autowired
  CartLineItemDao cartLineItemDao;
  @Autowired
  SkuItemLineItemDao skuItemLineItemDao;
  @Autowired
  SkuGroupDao skuGroupDao;


  private Logger logger = LoggerFactory.getLogger(InventoryHealthServiceImpl.class);


  private static final String bookedInventorySql = "select a.marked_price as mrp, sum(a.qty) as qty" +
      " from cart_line_item as a inner join base_order as b on a.order_id = b.id" +
      " where a.product_variant_id = :pvId " +
      " and b.order_status_id in (:statusIds) " +
      " group by a.marked_price";

  private Map<Double, Long> getBookedInventoryQty(ProductVariant productVariant) {
    String sql = bookedInventorySql;
    SQLQuery query = baseDao.createSqlQuery(sql);
    query.addScalar("mrp", Hibernate.DOUBLE);
    query.addScalar("qty", Hibernate.LONG);

    query.setParameter("pvId", productVariant.getId());
    query.setParameterList("statusIds", Arrays.asList(EnumOrderStatus.Placed.getId()));

    Map<Double, Long> map = new LinkedHashMap<Double, Long>();
    @SuppressWarnings("unchecked")
    List<Object[]> list = query.list();
    if (list != null) {
      for (Object[] data : list) {
        map.put((Double) data[0], (Long) data[1]);
      }
    }
    return map;
  }


  private static final String inProcessInventorySql = "select a.marked_price as mrp, sum(a.qty) as qty, a.sku_id as skuId" +
      " from line_item as a inner join shipping_order as b on a.shipping_order_id = b.id" +
      " inner join base_order as c on b.base_order_id = c.id" +
      " inner join sku as e on a.sku_id = e.id" +
      " where e.product_variant_id = :pvId" +
      " and e.warehouse_id in (:whIds)" +
      " and c.order_status_id in (:statusIds)" +
      " and b.shipping_order_status_id in (:sosIds)" +
      " group by a.marked_price, a.sku_id";

  private List<SkuInfo> getInProcessInventory(ProductVariant productVariant, List<Warehouse> whs) {
    String sql = inProcessInventorySql;
    SQLQuery query = baseDao.createSqlQuery(sql);
    query.addScalar("mrp", Hibernate.DOUBLE);
    query.addScalar("qty", Hibernate.LONG);
    query.addScalar("skuId", Hibernate.LONG);

    query.setParameter("pvId", productVariant.getId());
    query.setParameterList("whIds", toWarehouseIds(whs));
    query.setParameterList("statusIds", Arrays.asList(EnumOrderStatus.InProcess.getId(), EnumOrderStatus.OnHold.getId()));
    query.setParameterList("sosIds", EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventory()));

    query.setResultTransformer(Transformers.aliasToBean(SkuInfo.class));

    @SuppressWarnings("unchecked")
    List<SkuInfo> list = query.list();
    return list;
  }

  private List<SkuInfo> getPostActionQueueInventory(ProductVariant productVariant, List<Warehouse> whs) {
    String sql = inProcessInventorySql;
    SQLQuery query = baseDao.createSqlQuery(sql);
    query.addScalar("mrp", Hibernate.DOUBLE);
    query.addScalar("qty", Hibernate.LONG);
    query.addScalar("skuId", Hibernate.LONG);

    query.setParameter("pvId", productVariant.getId());
    query.setParameterList("whIds", toWarehouseIds(whs));
    query.setParameterList("statusIds", Arrays.asList(EnumOrderStatus.InProcess.getId(), EnumOrderStatus.OnHold.getId()));
    query.setParameterList("sosIds", EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventoryInProcessingQueue()));

    query.setResultTransformer(Transformers.aliasToBean(SkuInfo.class));

    @SuppressWarnings("unchecked")
    List<SkuInfo> list = query.list();
    return list;
  }


  private static final String netUnCheckedOutInInvSql = "select c.id as skuId, b.mrp as mrp, b.cost_price as costPrice, " +
      " count(a.id) as qty, b.create_date as checkinDate" +
      " from sku_item as a" +
      " inner join sku_group as b on a.sku_group_id = b.id" +
      " inner join sku as c on b.sku_id = c.id" +
      " where c.product_variant_id = :pvId" +
      " and c.warehouse_id in (:whIds)" +
      " and (b.status != :reviewStatus or b.status is null)" +
      " and a.sku_item_status_id in (:itemStatus)" +
      " and b.mrp is not null" +
      " group by b.id" +
      " order by checkinDate asc";

  public Collection<SkuInfo> getUnCheckedOutInventory(ProductVariant productVariant, List<Warehouse> whs) {
    String sql = netUnCheckedOutInInvSql;
    SQLQuery query = baseDao.createSqlQuery(sql);

    query.addScalar("skuId", Hibernate.LONG);
    query.addScalar("mrp", Hibernate.DOUBLE);
    query.addScalar("qty", Hibernate.LONG);
    query.addScalar("costPrice", Hibernate.DOUBLE);
    query.addScalar("checkinDate", Hibernate.DATE);

    query.setParameter("pvId", productVariant.getId());
    query.setParameterList("whIds", toWarehouseIds(whs));
    query.setParameterList("itemStatus", Arrays.asList(EnumSkuItemStatus.Checked_IN.getId(), EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemStatus.BOOKED.getId()));
    query.setParameter("reviewStatus", EnumSkuGroupStatus.UNDER_REVIEW.name());

    query.setResultTransformer(Transformers.aliasToBean(SkuInfo.class));

    @SuppressWarnings("unchecked")
    List<SkuInfo> list = query.list();

    LinkedList<SkuInfo> skuList = new LinkedList<SkuInfo>();
    for (SkuInfo skuInfo : list) {
      SkuInfo info = getLast(skuList);
      if (info != null && skuInfo.getSkuId() == info.getSkuId() && skuInfo.getMrp() == info.getMrp()) {
        info.setQty(info.getQty() + skuInfo.getQty());
        info.setUnbookedQty(info.getQty());
      } else {
        skuInfo.setUnbookedQty(skuInfo.getQty());
        skuList.add(skuInfo);
      }
    }
    return skuList;
  }

  private static final String checkedInInvSql = "select c.id as skuId, b.mrp as mrp, b.cost_price as costPrice, " +
      " count(a.id) as qty, b.create_date as checkinDate" +
      " from sku_item as a" +
      " inner join sku_group as b on a.sku_group_id = b.id" +
      " inner join sku as c on b.sku_id = c.id" +
      " where c.product_variant_id = :pvId" +
      " and c.warehouse_id in (:whIds)" +
      " and (b.status != :reviewStatus or b.status is null)" +
      " and a.sku_item_status_id = :itemStatus" +
      " and b.mrp is not null" +
      " group by b.id" +
      " order by checkinDate asc";

  @Override
  public Collection<SkuInfo> getCheckedInInventory(ProductVariant productVariant, List<Warehouse> whs) {
    String sql = checkedInInvSql;
    SQLQuery query = baseDao.createSqlQuery(sql);

    query.addScalar("skuId", Hibernate.LONG);
    query.addScalar("mrp", Hibernate.DOUBLE);
    query.addScalar("qty", Hibernate.LONG);
    query.addScalar("costPrice", Hibernate.DOUBLE);
    query.addScalar("checkinDate", Hibernate.DATE);

    query.setParameter("pvId", productVariant.getId());
    query.setParameterList("whIds", toWarehouseIds(whs));
    query.setParameter("itemStatus", EnumSkuItemStatus.Checked_IN.getId());
    query.setParameter("reviewStatus", EnumSkuGroupStatus.UNDER_REVIEW.name());

    query.setResultTransformer(Transformers.aliasToBean(SkuInfo.class));

    @SuppressWarnings("unchecked")
    List<SkuInfo> list = query.list();

    LinkedList<SkuInfo> skuList = new LinkedList<SkuInfo>();
    for (SkuInfo skuInfo : list) {
      SkuInfo info = getLast(skuList);
      if (info != null && skuInfo.getSkuId() == info.getSkuId() && skuInfo.getMrp() == info.getMrp()) {
        info.setQty(info.getQty() + skuInfo.getQty());
        info.setUnbookedQty(info.getQty());
      } else {
        skuInfo.setUnbookedQty(skuInfo.getQty());
        skuList.add(skuInfo);
      }
    }
    return skuList;
  }

  private List<Long> toWarehouseIds(List<Warehouse> whs) {
    List<Long> list = new ArrayList<Long>();
    for (Warehouse wh : whs) {
      list.add(wh.getId());
    }

    return list;
  }

  private Collection<InventoryInfo> getAvailableInventory(ProductVariant productVariant, List<Warehouse> whs) {
    Collection<SkuInfo> checkedInInvList = getCheckedInInventory(productVariant, whs);

    Map<Double, Long> bookedQtyMap = getBookedInventoryQty(productVariant);

    List<SkuInfo> inProcessList = getInProcessInventory(productVariant, whs);
    if (inProcessList != null) {
      for (SkuInfo inProcessInfo : inProcessList) {
        List<SkuInfo> infos = searchBySkuIdAndMrp(checkedInInvList, inProcessInfo.getSkuId(), inProcessInfo.getMrp());
        long leftQty = inProcessInfo.getQty();
        for (SkuInfo skuInfo : infos) {
          long qty = skuInfo.getQty() - leftQty;
          if (qty < 0) {
            leftQty = -qty;
            skuInfo.setQty(0);
            skuInfo.setUnbookedQty(0);
          } else {
            leftQty = 0;
            skuInfo.setQty(qty);
            skuInfo.setUnbookedQty(qty);
          }
        }
      }
    }

    List<InventoryInfo> invList = new LinkedList<InventoryInfo>();
    Map<Double, List<InventoryInfo>> mrpMap = new LinkedHashMap<Double, List<InventoryInfo>>();

    for (SkuInfo skuInfo : checkedInInvList) {
      InventoryInfo info = getLast(invList);
      if (info != null && skuInfo.getMrp() == info.getMrp()) {
        info.setQty(info.getQty() + skuInfo.getQty());
      } else {
        info = new InventoryInfo();
        info.setMrp(skuInfo.getMrp());
        info.setQty(skuInfo.getQty());
        invList.add(info);

        List<InventoryInfo> infos = mrpMap.get(skuInfo.getMrp());
        if (infos == null) {
          infos = new ArrayList<InventoryInfo>();
          mrpMap.put(Double.valueOf(skuInfo.getMrp()), infos);
        }
        infos.add(info);
      }
      info.addSkuInfo(skuInfo);
    }

    for (Map.Entry<Double, List<InventoryInfo>> entry : mrpMap.entrySet()) {
      Double mrp = entry.getKey();
      Long bookedQty = bookedQtyMap.get(mrp);

      if (bookedQty != null) {
        long leftQty = bookedQty;
        for (InventoryInfo inventoryInfo : entry.getValue()) {
          long netInveQty = 0l;
          for (SkuInfo skuInfo : inventoryInfo.getSkuInfoList()) {
            long qty = skuInfo.getQty() - leftQty;
            if (qty < 0) {
              leftQty = -qty;
              skuInfo.setQty(0);
            } else {
              leftQty = 0;
              skuInfo.setQty(qty);
            }
            netInveQty += skuInfo.getQty();
          }
          inventoryInfo.setQty(netInveQty);
        }
      }
    }
    return invList;
  }


  private List<SkuInfo> searchBySkuIdAndMrp(Collection<SkuInfo> list, long skuId, double mrp) {
    List<SkuInfo> infos = new ArrayList<SkuInfo>();
    for (SkuInfo info : list) {
      if (info.getSkuId() == skuId && info.getMrp() == mrp) {
        infos.add(info);
      }
    }
    return infos;
  }

  @Override
  public Collection<InventoryInfo> getAvailableInventory(ProductVariant productVariant) {
    return getAvailableInventory(productVariant, warehouseService.getServiceableWarehouses());
  }

  @Override
  public Collection<InventoryInfo> getAvailableInventory(List<Sku> skus) {
    List<Warehouse> whs = new ArrayList<Warehouse>();
    for (Sku sku : skus) {
      whs.add(sku.getWarehouse());
    }
    return getAvailableInventory(skus.get(0).getProductVariant(), whs);
  }

  @Override
  public Collection<SkuInfo> getAvailableSkus(ProductVariant variant, SkuFilter filter) {
    List<SkuInfo> skus = new ArrayList<SkuInfo>();

    Collection<InventoryInfo> infos = this.getAvailableInventory(variant);
    boolean invAdded = false;
    for (InventoryInfo inventoryInfo : infos) {
      if (filter.getMrp() == null || inventoryInfo.getMrp() == filter.getMrp().doubleValue()) {
        for (SkuInfo skuInfo : inventoryInfo.getSkuInfoList()) {
          if (skuInfo.getUnbookedQty() >= filter.getMinQty()) {
            Sku sku = baseDao.get(Sku.class, skuInfo.getSkuId());
            if (filter.getWarehouseId() == null
                || filter.getWarehouseId().equals(sku.getWarehouse().getId())) {
              skus.add(skuInfo);
              invAdded = true;
            }
          }
        }
      }
      if ((filter.getFetchType() != null && filter.getFetchType() == FetchType.FIRST_ORDER) && invAdded) break;
    }
    return skus;
  }

  @Override
  public Collection<SkuInfo> getAvailableSkusForSplitter(ProductVariant variant, SkuFilter filter, CartLineItem cartLineItem) {
    List<SkuInfo> skus = new ArrayList<SkuInfo>();
    SkuItem tempBookedSkuItem = null;
    List<SkuItemCLI> skuItemCLIs = cartLineItem.getSkuItemCLIs();
    if (skuItemCLIs != null && skuItemCLIs.size() > 0) {
      tempBookedSkuItem = skuItemCLIs.get(0).getSkuItem();
    }
    boolean brightInventoryInfoAdded = false;
    Collection<InventoryInfo> infos = this.getAvailableInventory(variant);
    if ((infos != null && infos.size() <= 0) && (skuItemCLIs != null && skuItemCLIs.size() <= 0 )) {
      // make a call to Bright side for inventory Info
      infos = avaialbleSkuInfoInfoOfBright(variant, cartLineItem.getId());
      brightInventoryInfoAdded = true;

    }
    boolean invAdded = false;
    boolean newSkuInfoFlag = false;
    boolean updateSkuInfoFlag = false;

    if (!brightInventoryInfoAdded) {
      //
      for (InventoryInfo inventoryInfo : infos) {
        if (filter.getMrp() == null || inventoryInfo.getMrp() == filter.getMrp().doubleValue()) {
          for (SkuInfo skuInfo : inventoryInfo.getSkuInfoList()) {
            if (tempBookedSkuItem != null && skuInfo.getSkuId() == tempBookedSkuItem.getSkuGroup().getSku().getId().longValue()) {
              skuInfo.setUnbookedQty(skuInfo.getUnbookedQty() + cartLineItem.getQty());
              updateSkuInfoFlag = true;
              break;
            }
          }
          if (!updateSkuInfoFlag && tempBookedSkuItem != null) {
            SkuInfo newSkuInfo = new SkuInfo();
            newSkuInfo.setSkuId(tempBookedSkuItem.getSkuGroup().getSku().getId());
            newSkuInfo.setMrp(tempBookedSkuItem.getSkuGroup().getMrp());
            newSkuInfo.setCostPrice(tempBookedSkuItem.getSkuGroup().getCostPrice());
            newSkuInfo.setUnbookedQty(cartLineItem.getQty());
            newSkuInfo.setCheckinDate(tempBookedSkuItem.getSkuGroup().getCreateDate());
            if (tempBookedSkuItem.getSkuGroup().getQty() != null) {
              newSkuInfo.setQty(tempBookedSkuItem.getSkuGroup().getQty());
            }
            inventoryInfo.getSkuInfoList().add(newSkuInfo);
            newSkuInfoFlag = true;
            break;
          }
        }
      }

      if (!updateSkuInfoFlag && !newSkuInfoFlag && tempBookedSkuItem != null) {
        InventoryInfo newInventoryInfo = new InventoryInfo();
        List<SkuInfo> skuInfoList = new ArrayList<SkuInfo>();
        SkuInfo newSkuInfo = new SkuInfo();
        newSkuInfo.setSkuId(tempBookedSkuItem.getSkuGroup().getSku().getId());
        newSkuInfo.setMrp(tempBookedSkuItem.getSkuGroup().getMrp());
        newSkuInfo.setCostPrice(tempBookedSkuItem.getSkuGroup().getCostPrice());
        newSkuInfo.setUnbookedQty(cartLineItem.getQty());
        newSkuInfo.setCheckinDate(tempBookedSkuItem.getSkuGroup().getCreateDate());
        if (tempBookedSkuItem.getSkuGroup().getQty() != null) {
          newSkuInfo.setQty(tempBookedSkuItem.getSkuGroup().getQty());
        }

        skuInfoList.add(newSkuInfo);
        newInventoryInfo.setMrp(newSkuInfo.getMrp());
        newInventoryInfo.setQty(newSkuInfo.getUnbookedQty());
        newInventoryInfo.getSkuInfoList().add(newSkuInfo);
        infos.add(newInventoryInfo);
      }

    }

    for (InventoryInfo inventoryInfo : infos) {
      if (filter.getMrp() == null || inventoryInfo.getMrp() == filter.getMrp().doubleValue()) {
        for (SkuInfo skuInfo : inventoryInfo.getSkuInfoList()) {
          if (skuInfo.getUnbookedQty() >= filter.getMinQty()) {
            Sku sku = baseDao.get(Sku.class, skuInfo.getSkuId());
            if (filter.getWarehouseId() == null
                || filter.getWarehouseId().equals(sku.getWarehouse().getId())) {
              skus.add(skuInfo);
              invAdded = true;
            }
          }
        }
      }
      if ((filter.getFetchType() != null && filter.getFetchType() == FetchType.FIRST_ORDER) && invAdded) break;
    }
    return skus;
  }

  private static <T> T removeFirst(List<T> list) {
    if (list == null || list.size() == 0) return null;
    if (list instanceof LinkedList) {
      try {
        return ((LinkedList<T>) list).removeFirst();
      } catch (Exception e) {

      }
    }
    return list.remove(0);
  }

  private static <T> T getLast(List<T> list) {
    if (list == null || list.size() == 0) return null;
    if (list instanceof LinkedList) {
      try {
        return ((LinkedList<T>) list).getLast();
      } catch (Exception e) {

      }
    }
    return list.get(list.size() - 1);
  }


  public void updateVariantInfo(ProductVariant productVariant, Set<InventoryHealthService.SkuInfo> skuInfos) {
    double newHkPrice = 0d;
    if (skuInfos == null || skuInfos.isEmpty()) {
      SkuInfo skuInfo = new SkuInfo();
      skuInfo.setSkuId(0l);
      skuInfo.setCostPrice(productVariant.getCostPrice());
      skuInfo.setMrp(productVariant.getMarkedPrice());
      skuInfo.setQty(0l);
      skuInfo.setUnbookedQty(0l);
      skuInfos.add(skuInfo);
    }
    Iterator<InventoryHealthService.SkuInfo> iterator = skuInfos.iterator();
    InventoryHealthService.SkuInfo selectedInfo = iterator.next();
    Long maxQty = selectedInfo.getQty();
    for (InventoryHealthService.SkuInfo info : skuInfos) {
      if (maxQty.compareTo(info.getQty()) < 0) {
        maxQty = info.getQty();
        selectedInfo = info;
      }
    }

    if (selectedInfo.getMrp() != 0d && !productVariant.getMarkedPrice().equals(Double.valueOf(selectedInfo.getMrp()))) {
      UpdatePvPrice updatePvPrice = updatePvPriceDao.getPVForPriceUpdate(productVariant, EnumUpdatePVPriceStatus.Pending.getId());
      if (updatePvPrice == null) {
        updatePvPrice = new UpdatePvPrice();
      }
      updatePvPrice.setProductVariant(productVariant);
      updatePvPrice.setOldCostPrice(productVariant.getCostPrice());
      updatePvPrice.setNewCostPrice(selectedInfo.getCostPrice());
      updatePvPrice.setOldMrp(productVariant.getMarkedPrice());
      updatePvPrice.setNewMrp(selectedInfo.getMrp());
      updatePvPrice.setOldHkprice(productVariant.getHkPrice());
      newHkPrice = selectedInfo.getMrp() * (1 - productVariant.getDiscountPercent());
      updatePvPrice.setNewHkprice(newHkPrice);
      updatePvPrice.setTxnDate(new Date());
      updatePvPrice.setStatus(EnumUpdatePVPriceStatus.Pending.getId());
      baseDao.save(updatePvPrice);
    }
    // get Net quantity for product variant
    List<Sku> variantSkus = skuService.getSKUsForProductVariant(productVariant);
    long netQty = skuItemDao.getInventoryCount(variantSkus, Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()));
    productVariant.setNetQty(netQty);

    productVariant.setMrpQty(maxQty);
    productVariant.setMarkedPrice(selectedInfo.getMrp());
    productVariant.setCostPrice(selectedInfo.getCostPrice());
    if (newHkPrice != 0d) {
      productVariant.setHkPrice(newHkPrice);
    }
    if (!productVariant.getDeleted() && productVariant.getMrpQty() > 0) {
      productVariant.setOutOfStock(false);
    } else {
      productVariant.setOutOfStock(true);
    }
    long skuId = selectedInfo.getSkuId();
    if (skuId != 0) {
      Sku sku = getBaseDao().get(Sku.class, skuId);
      productVariant.setWarehouse(sku.getWarehouse());
    }
    productVariant =(ProductVariant) getBaseDao().save(productVariant);
    getBaseDao().refresh(productVariant);
    Product product = productVariant.getProduct();

    List<ProductVariant> inStockVariants = product.getInStockVariants();
    if (inStockVariants != null && inStockVariants.isEmpty()) {
      product.setOutOfStock(true);
    } else {
      product.setOutOfStock(false);
    }

    boolean updateStockStatus = !(product.isJit() || product.isDropShipping() || product.isService());
    if (!updateStockStatus) {
      productVariant.setOutOfStock(false);
      product.setOutOfStock(false);
      getBaseDao().save(productVariant);
    }
    getBaseDao().save(product);
  }


  // Call this method from just  action  java
  @Transactional
  public void tempBookSkuLineItemForOrder(Order order) {
    InventoryService inventoryManageService = ServiceLocatorFactory.getService(InventoryService.class);
    Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
    for (CartLineItem cartLineItem : cartLineItems) {
      if (!cartLineItem.getLineItemType().getId().equals(EnumCartLineItemType.Subscription.getId())) {
        if (!skuItemLineItemService.sicliAlreadyExists(cartLineItem)) {
          ProductVariant productVariant = cartLineItem.getProductVariant();
          productVariant = (ProductVariant) productVariantService.getVariantById(productVariant.getId());
          List<Warehouse> servicableWarehouse = warehouseService.getServiceableWarehouses();
          List<Long> whIds = new ArrayList<Long>();
          for (Warehouse warehouse : servicableWarehouse) {
            whIds.add(warehouse.getId());
          }
          Long warehouseIdAtPV = productVariant.getWarehouse().getId();
          productVariant =(ProductVariant) productVariantService.getVariantById(productVariant.getId());
          logger.debug("Temp Booking On Aqua Side");
//          Sku sku = skuService.getSKU(productVariant, productVariant.getWarehouse());

          if (whIds.contains(warehouseIdAtPV)) {
            cartLineItem = tempBookAquaInventory(cartLineItem,warehouseIdAtPV);
          } else {
            //book inventory on Bright
            if (isBookingRequireAtBright(cartLineItem)) {
              if (cartLineItem.getForeignSkuItemCLIs() == null || cartLineItem.getForeignSkuItemCLIs().size() < 1) {
              cartLineItem = tempBookBrightInventory(cartLineItem,warehouseIdAtPV);
              populateSICLI(cartLineItem);
              }
            }
          }
        }
      }
    }
    order = (Order) baseDao.save(order);
    Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
    // calling health check
    for (CartLineItem cartLineItem : productCartLineItems) {
      inventoryHealthCheck(cartLineItem.getProductVariant());
    }

  }

  @Transactional
  public CartLineItem createSkuGroupAndItem(CartLineItem cartLineItem, Long warehouseId) {

      ForeignSkuItemCLI fsicli = cartLineItem.getForeignSkuItemCLIs().get(0);
      ProductVariant productVariant = cartLineItem.getProductVariant();
      Warehouse warehouse = getBaseDao().get(Warehouse.class, warehouseId);
      List<Warehouse> whs = warehouseService.findWarehousesByPrefix(warehouse.getTinPrefix());
      whs.remove(warehouse);
      // now whs contain aqua warehouse
      Sku sku = skuService.getSKU(productVariant, whs.get(0));
      SkuGroup skuGroup = null;

      Long foreignSkuGroupId = fsicli.getForeignSkuGroupId();
      skuGroup = skuGroupDao.getForeignSkuGroup(foreignSkuGroupId);
      if (fsicli.getSkuItemId() != null) {
        if (skuGroup == null) {
          skuGroup = skuItemLineItemDao.createSkuGroupWithoutBarcode(fsicli.getFsgBatchNumber(), fsicli.getFsgMfgDate(), fsicli.getFsgExpiryDate(),
              fsicli.getFsgCostPrice(), fsicli.getFsgMrp(), null, null, null, sku);
          skuGroup.setForeignSkuGroupId(foreignSkuGroupId);
          skuGroup = (SkuGroup) getBaseDao().save(skuGroup);
        }

        // Need to discus about setting of foreign sku_group id

        List<SkuItem> skuItems = new ArrayList<SkuItem>();
        for (ForeignSkuItemCLI fsicli1 : cartLineItem.getForeignSkuItemCLIs()) {
          SkuItem skuItem = new SkuItem();
          skuItem.setSkuGroup(skuGroup);
          skuItem.setCreateDate(new Date());
          skuItem.setSkuItemStatus(EnumSkuItemStatus.EXPECTED_CHECKED_IN.getSkuItemStatus());
          skuItem.setBarcode(fsicli1.getForeignBarcode());
          skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
          skuItem.setForeignSkuItemCLI(fsicli1);
          skuItem = (SkuItem) getBaseDao().save(skuItem);
          skuItems.add(skuItem);
        }
      }

    return cartLineItem;
  }


  public void populateSICLI(CartLineItem cartLineItem) {
    if (cartLineItem != null) {
      for (ForeignSkuItemCLI foreignSkuItemCLI : cartLineItem.getForeignSkuItemCLIs()) {
        SkuItemCLI skuItemCLI = new SkuItemCLI();
        skuItemCLI.setCartLineItem(cartLineItem);
        skuItemCLI.setUnitNum(foreignSkuItemCLI.getUnitNum());
        skuItemCLI.setCreateDate(new Date());
        skuItemCLI.setProductVariant(cartLineItem.getProductVariant());
        skuItemCLI.setSkuItem(skuItemLineItemService.getSkuItem(foreignSkuItemCLI.getId()));
        skuItemCLI = (SkuItemCLI) getBaseDao().save(skuItemCLI);
      }
    }
  }

  @Transactional
  public CartLineItem tempBookBrightInventory(CartLineItem cartLineItem, Long warehouseId) {
    logger.debug("Going to book inv on Bright Side");
    ProductVariant productVariant = cartLineItem.getProductVariant();
    productVariant = productVariantService.getVariantById(productVariant.getId());
     Long warehouseIdAtOrderPlacement = warehouseId;
    // now make a API call to booked inventory at Bright

    List<ForeignSkuItemCLI> foreignSkuItemCLis = populateForeignSICLITable(cartLineItem);
    List<HKAPIBookingInfo> hkapiBookingInfos = new ArrayList<HKAPIBookingInfo>();
    for (ForeignSkuItemCLI foreignSkuItemCLI : foreignSkuItemCLis) {
      HKAPIBookingInfo hkapiBookingInfo = new HKAPIBookingInfo();
      hkapiBookingInfo.setWhId(warehouseIdAtOrderPlacement);
      hkapiBookingInfo.setUnitNum(foreignSkuItemCLI.getUnitNum());
      hkapiBookingInfo.setBoDate(cartLineItem.getOrder().getPayment().getPaymentDate().toString());
      hkapiBookingInfo.setPvId(foreignSkuItemCLI.getProductVariant().getId());
      hkapiBookingInfo.setBoId(cartLineItem.getOrder().getId());
      hkapiBookingInfo.setFsiCLIId(foreignSkuItemCLI.getId());
      hkapiBookingInfo.setUnitNum(foreignSkuItemCLI.getUnitNum());
      hkapiBookingInfo.setCliId(cartLineItem.getId());
      hkapiBookingInfo.setSoId(null);
      hkapiBookingInfo.setMrp(foreignSkuItemCLI.getAquaMrp());
      hkapiBookingInfos.add(hkapiBookingInfo);

    }

    for (HKAPIBookingInfo hkapiBookingInfo : hkapiBookingInfos) {
      List<HKAPIForeignBookingResponseInfo> infos = null;
      try {
        Gson gson = new Gson();
        String json = gson.toJson(Arrays.asList(hkapiBookingInfo));
        logger.debug("json to be sent on Bright for Inv Booking - " + json);
        String url = brightlifecareRestUrl + "product/variant/" + "tempBookInventoryOnBright/";
        ClientRequest request = new ClientRequest(url);
        request.body("application/json", json);
        ClientResponse response = request.post();
        int status = response.getStatus();
        if (status == 200) {
          String data = (String) response.getEntity(String.class);
          Type listType = new TypeToken<List<HKAPIForeignBookingResponseInfo>>() {
          }.getType();
          infos = new Gson().fromJson(data, listType);
        }
      } catch (Exception e) {
        logger.error("Exception while booking Bright Inventory against BO# " + cartLineItem.getOrder().getId() + e.getMessage());
      }
      if (infos != null && infos.size() > 0) {
        updateForeignSICLITable(infos);
      }

    }
    cartLineItem = (CartLineItem) getBaseDao().save(cartLineItem);
    cartLineItem  = (CartLineItem) createSkuGroupAndItem(cartLineItem, warehouseIdAtOrderPlacement);
    return cartLineItem;
  }

@Transactional
 public CartLineItem tempBookAquaInventory (CartLineItem cartLineItem, Long warehouseId){
   InventoryService inventoryManageService = ServiceLocatorFactory.getService(InventoryService.class);
    if (!skuItemLineItemService.sicliAlreadyExists(cartLineItem)) {
      ProductVariant productVariant = cartLineItem.getProductVariant();
        logger.debug("Temp Booking On Aqua Side");
       Warehouse warehouse =  warehouseService.getWarehouseById(warehouseId);
        Sku sku =  skuService.getSKU(productVariant, warehouse);
         List<Sku> skus = Arrays.asList(sku);

        Long availableUnBookedInventory = skuItemDao.getInventoryCount(skus, Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()));

        if (availableUnBookedInventory > 0) {
          // picking the  sku for current MRP available at max qty on product variant
          long qtyToBeSet = cartLineItem.getQty();
          if (availableUnBookedInventory >= qtyToBeSet) {
            Set<SkuItem> skuItemsToBeBooked = new HashSet<SkuItem>();

            for (int i = 0; i < qtyToBeSet; i++) {
              List<SkuItem> skuItemList = skuItemDao.getSkuItems(skus, Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()), null, cartLineItem.getMarkedPrice());

              if (skuItemList != null && skuItemList.size() > 0) {
                SkuItem skuItem = skuItemList.get(0);
                skuItem.setSkuItemStatus(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
                skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
                skuItem = (SkuItem) getBaseDao().save(skuItem);
                // inventoryHealthCheck call
                inventoryHealthCheck(productVariant);
                skuItemsToBeBooked.add(skuItem);
              }
            }
            // Call method to make new entries in SKUItemCLI  only those for which inventory availa
            List<SkuItemCLI> skuItemCLIList = inventoryManageService.saveSkuItemCLI(skuItemsToBeBooked, cartLineItem);
            cartLineItem.setSkuItemCLIs(skuItemCLIList);
          }
        }
      }

   return cartLineItem;
  }





  private void populateForeignSICLITable(CartLineItem cartLineItem, HashMap<Long, Long> siLiMap) {
    if (siLiMap != null && siLiMap.size() > 0) {
      Set<Entry<Long, Long>> entrySet = siLiMap.entrySet();
      for (Entry<Long, Long> entry : entrySet) {
        ForeignSkuItemCLI foreignSkuItemCLI = new ForeignSkuItemCLI();
        foreignSkuItemCLI.setProductVariant(cartLineItem.getProductVariant());
        foreignSkuItemCLI.setUnitNum(entry.getKey());
        foreignSkuItemCLI.setSkuItemId(entry.getValue());
        foreignSkuItemCLI.setCounter(1L);
        foreignSkuItemCLI.setCartLineItem(cartLineItem);
        foreignSkuItemCLI = (ForeignSkuItemCLI) getBaseDao().save(foreignSkuItemCLI);
      }
    }

  }


  private List<ForeignSkuItemCLI> populateForeignSICLITable(CartLineItem cartLineItem) {
    List<ForeignSkuItemCLI> foreignSkuItemCLIs = new ArrayList<ForeignSkuItemCLI>();
    for (int i = 1; i <= cartLineItem.getQty(); i++) {
      ForeignSkuItemCLI foreignSkuItemCLI = new ForeignSkuItemCLI();
      foreignSkuItemCLI.setAquaMrp(cartLineItem.getMarkedPrice());
      foreignSkuItemCLI.setCartLineItem(cartLineItem);
      foreignSkuItemCLI.setSkuItemId(null);
      foreignSkuItemCLI.setProductVariant(cartLineItem.getProductVariant());
      foreignSkuItemCLI.setUnitNum((long) i);
      foreignSkuItemCLI.setUpdateDate(new Date());
      foreignSkuItemCLI.setProcessedStatus(EnumUnitProcessedStatus.UNPROCESSED.getId());
      foreignSkuItemCLI.setCounter(1L);
      foreignSkuItemCLI = (ForeignSkuItemCLI) getBaseDao().save(foreignSkuItemCLI);
      foreignSkuItemCLIs.add(foreignSkuItemCLI);

    }
     cartLineItem.setForeignSkuItemCLIs(foreignSkuItemCLIs);
     getBaseDao().save(cartLineItem);
     return foreignSkuItemCLIs;
  }


  public void updateForeignSICLITable(List<HKAPIForeignBookingResponseInfo> infos) {
    for (HKAPIForeignBookingResponseInfo info : infos) {
      long fsiliId = info.getFsiCLIId();
      ForeignSkuItemCLI foreignSkuItemCLI = skuItemLineItemService.getForeignSkuItemCLI(fsiliId);
      if (foreignSkuItemCLI != null) {
        foreignSkuItemCLI.setForeignBarcode(info.getBarcode());
        foreignSkuItemCLI.setForeignSkuGroupId(info.getFsgId());
        foreignSkuItemCLI.setFsgCostPrice(info.getCp());
        foreignSkuItemCLI.setSkuItemId(info.getFsiId());

        if (info.getExpdt() != null) {
          foreignSkuItemCLI.setFsgExpiryDate(getFormattedDate(info.getExpdt()));
        } else {
          foreignSkuItemCLI.setFsgExpiryDate(new Date());
        }
        if (info.getMfgdt() != null) {
          foreignSkuItemCLI.setFsgMfgDate(getFormattedDate(info.getMfgdt()));
        } else {
          foreignSkuItemCLI.setFsgMfgDate(new Date());
        }
        foreignSkuItemCLI.setFsgMrp(info.getMrp());
        foreignSkuItemCLI.setFsgBatchNumber(info.getBatch());
        foreignSkuItemCLI.setProcessedStatus(info.getProcessed());
        if(info.getFboId() != null){
          foreignSkuItemCLI.setForeignOrderId(info.getFboId());
        }
       if(info.getFsoId() != null){
         foreignSkuItemCLI.setForeignShippingOrderId(info.getFsoId());
       }
        foreignSkuItemCLI = (ForeignSkuItemCLI) getBaseDao().save(foreignSkuItemCLI);
      }
    }
  }


  public void inventoryHealthCheck(ProductVariant productVariant) {
    InventoryService inventoryManageService = ServiceLocatorFactory.getService(InventoryService.class);
    Long availableUnbookedInventory = inventoryManageService.getAvailableUnBookedInventory(productVariant);

    if (availableUnbookedInventory > 0) {
      Collection<InventoryHealthService.SkuInfo> availableCheckedInInvnList = getCheckedInInventory(productVariant, warehouseService.getServiceableWarehouses());

      List<Sku> skus = skuService.getSKUsForProductVariantAtServiceableWarehouses(productVariant);
      if (!skus.isEmpty()) {
        Long unbookedInventory = inventoryManageService.getAvailableUnbookedInventory(skus, false);
        Long countOfJustCheckedInBatch = inventoryManageService.getLatestcheckedInBatchInventoryCount(productVariant);
        unbookedInventory = unbookedInventory - countOfJustCheckedInBatch;
        // it means we had booked some orders on zero inventory and now i need to create sicli for that

        Set<SkuInfo> availableUnBookedInvnList = new HashSet<SkuInfo>();
        Set<SkuInfo> differentMrpCheckedinBatch = new HashSet<SkuInfo>();
        Iterator it = availableCheckedInInvnList.iterator();
        SkuInfo sk = (SkuInfo) it.next();
        availableUnBookedInvnList.add(sk);
        for (SkuInfo skuInfo : availableCheckedInInvnList) {
          if (sk.getMrp() == skuInfo.getMrp() && sk.getSkuId() != skuInfo.getSkuId()) {
            availableUnBookedInvnList.add(skuInfo);
          } else {
            differentMrpCheckedinBatch.add(skuInfo);
          }
        }

        differentMrpCheckedinBatch.remove(sk);
        Set<SkuInfo> availableUnBookedInvnListToUpdate = new HashSet<SkuInfo>();
        Iterator itetaror = differentMrpCheckedinBatch.iterator();
        if (differentMrpCheckedinBatch.size() > 1) {
          SkuInfo differentCheckedInBatchFirstElement = (SkuInfo) itetaror.next();

          if (differentCheckedInBatchFirstElement != null) {
            for (SkuInfo info : availableUnBookedInvnList) {
              if (info.getCheckinDate().compareTo(differentCheckedInBatchFirstElement.getCheckinDate()) <= 0) {
                availableUnBookedInvnListToUpdate.add(info);
              }
            }
          } else {
            availableUnBookedInvnListToUpdate.addAll(availableUnBookedInvnList);
          }
        } else {
          availableUnBookedInvnListToUpdate.addAll(availableUnBookedInvnList);
        }

        if (availableUnBookedInvnListToUpdate.size() > 0) {
          updateVariantInfo(productVariant, availableUnBookedInvnListToUpdate);
        }
      }

    } else {
      Long unBoookedInventoryOfBright = getUnbookedInventoryOfBright(productVariant);

      // check available inventory for aqua  in Bright
      if (unBoookedInventoryOfBright > 0) {
        // now need to update pv with Bright Inventory
        logger.debug("Unbooked Qty of Bright - " + unBoookedInventoryOfBright);
        HKApiSkuResponse hkApiSkuResponse = getPVInfoFromBright(productVariant);
        logger.debug("hKApiSkuResponse got -" + hkApiSkuResponse.getVariantId() + ", qty - " + hkApiSkuResponse.getQty() + ", MRP -" + hkApiSkuResponse.getMrp());
        productService.updatePVForBrightInventory(hkApiSkuResponse, productVariant);

      } else {
        productVariant.setOutOfStock(true);
        productVariant.setNetQty(0L);
        productVariant.setMrpQty(0L);
      }
      Product product = productVariant.getProduct();
      boolean updateStockStatus = !(product.isJit() || product.isDropShipping() || product.isService());
      if (!updateStockStatus) {
        productVariant.setOutOfStock(false);
      }
      getBaseDao().save(productVariant);

      List<ProductVariant> inStockVariants = product.getInStockVariants();
      if (inStockVariants != null && inStockVariants.isEmpty()) {
        product.setOutOfStock(true);
      } else {
        product.setOutOfStock(false);
      }
      getBaseDao().save(product);
    }
  }

  public void pendingOrdersInventoryHealthCheck(ProductVariant productVariant) {
    InventoryService inventoryManageService = ServiceLocatorFactory.getService(InventoryService.class);
    Collection<InventoryHealthService.SkuInfo> availableUnBookedInvnList = getCheckedInInventory(productVariant, warehouseService.getServiceableWarehouses());
    if (availableUnBookedInvnList != null && !availableUnBookedInvnList.isEmpty()) {

      Iterator it = availableUnBookedInvnList.iterator();
      SkuInfo newSkuInfo = (SkuInfo) it.next();
      Long remainingQty = newSkuInfo.getQty();
      Double newMrp = newSkuInfo.getMrp();
      it.remove();
      long skuId = newSkuInfo.getSkuId();
      Sku sku = getBaseDao().get(Sku.class, skuId);
      productVariant.setWarehouse(sku.getWarehouse());
      getBaseDao().save(productVariant);
      List<CartLineItem> cartLineItems = cartLineItemDao.getClisForInPlacedOrder(productVariant, newMrp);
      Set<CartLineItem> clis = new HashSet<CartLineItem>(cartLineItems);
      if (clis.size() > 0) {
        remainingQty = tempBookSkuLineItemForPendingOrder(clis, newSkuInfo.getQty(), false);
      }
      //

//       considering scenario of orders in  processing queue
      if (remainingQty > 0) {
        List<CartLineItem> cartLineItemsInProcessing = inventoryManageService.getClisForOrderInProcessingState(productVariant, newSkuInfo.getSkuId(), newMrp);
        Set<CartLineItem> clisInProcessing = new HashSet<CartLineItem>(cartLineItemsInProcessing);
        if (clisInProcessing.size() > 0) {
          remainingQty = tempBookSkuLineItemForPendingOrder(clisInProcessing, remainingQty, true);
        }
      }
//   end scenario
//      newSkuInfo.setQty(remainingQty);
//      Set<SkuInfo> newBatchSkuInfo = new HashSet<SkuInfo>();
//      newBatchSkuInfo.add(newSkuInfo);
//      updateVariantInfo(productVariant, newBatchSkuInfo);
    }
  }


  public Long tempBookSkuLineItemForPendingOrder(Set<CartLineItem> cartLineItems, Long maxQty, boolean siliToBeCreated) {
    InventoryService inventoryManageService = ServiceLocatorFactory.getService(InventoryService.class);
    for (CartLineItem cartLineItem : cartLineItems) {
      if (lineItemDao.getLineItem(cartLineItem) != null) {

        ProductVariant productVariant = cartLineItem.getProductVariant();
        // picking the  sku for current MRP available at max qty on product variant
        Sku sku = skuService.getSKU(productVariant, productVariant.getWarehouse());
        long qtyToBeSet = cartLineItem.getQty();
        Set<SkuItem> skuItemsToBeBooked = new HashSet<SkuItem>();
        if (maxQty >= qtyToBeSet) {
          for (int i = 0; i < qtyToBeSet; i++) {
            List<SkuItem> skuItemList = skuItemDao.getSkuItems(Arrays.asList(sku), Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()), null, cartLineItem.getMarkedPrice());
            if (skuItemList != null && skuItemList.size() > 0) {
              SkuItem skuItem = skuItemList.get(0);
              skuItem.setSkuItemStatus(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
              skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
              // todo Pvi entries
              skuItem = (SkuItem) getBaseDao().save(skuItem);
              skuItemsToBeBooked.add(skuItem);
            }
          }
          // Call method to make new entries in SKUItemCLI  only those for which inventory availa
          logger.debug("calling saveSICLI line 881 from InventoryHealthServiceImpl");
          inventoryManageService.saveSkuItemCLI(skuItemsToBeBooked, cartLineItem);
          if (siliToBeCreated) {
            logger.debug("calling createNewSkuItemLineItem from InventoryHealthServiceImpl");
            skuItemLineItemService.createNewSkuItemLineItem(lineItemDao.getLineItem(cartLineItem));
          }
          maxQty = maxQty - qtyToBeSet;
        }
      }
    }
    return maxQty;
  }


  public BaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }


  public Long getUnbookedInventoryOfBright(ProductVariant productVariant) {
    Long qty = 0L;
    try {
      String url = brightlifecareRestUrl + "product/variant/unbookedInventory/" + productVariant.getId();
      ClientRequest request = new ClientRequest(url);
      ClientResponse response = request.get();
      int status = response.getStatus();
      if (status == 200) {
        String data = (String) response.getEntity(String.class);
        HKApiSkuResponse skuResponse = new Gson().fromJson(data, HKApiSkuResponse.class);
        qty = skuResponse.getQty();
      }
    } catch (Exception e) {
      logger.error("Exception while getting Bright unbooked inventory for Sku", e.getMessage());
    }

    return qty;
  }


  public Long getUnbookedInventoryOfBrightForMrp(ProductVariant productVariant, String tinPrefix, Double mrp) {
    Long qty = 0L;
    try {
      String url = brightlifecareRestUrl + "product/variant/unbookedInventoryForMrp/" + productVariant.getId()+"/" + tinPrefix +"/" + mrp;
      ClientRequest request = new ClientRequest(url);
      ClientResponse response = request.get();
      int status = response.getStatus();
      if (status == 200) {
        String data = (String) response.getEntity(String.class);
        HKApiSkuResponse skuResponse = new Gson().fromJson(data, HKApiSkuResponse.class);
        qty = skuResponse.getQty();
      }
    } catch (Exception e) {
      logger.error("Exception while getting Bright unbooked inventory for Sku", e.getMessage());
    }

    return qty;
  }


  private Collection<InventoryInfo> avaialbleSkuInfoInfoOfBright(ProductVariant productVariant, Long cartlineItemId) {
    Collection<InventoryInfo> infos = null;
    try {
      String url = brightlifecareRestUrl + "product/variant/splitterInfo/" + productVariant.getId() + "/" + cartlineItemId;
      ClientRequest request = new ClientRequest(url);
      ClientResponse response = request.get();
      int status = response.getStatus();
      if (status == 200) {
        String data = (String) response.getEntity(String.class);
        Type listType = new TypeToken<Collection<InventoryInfo>>() {
        }.getType();
        infos = new Gson().fromJson(data, listType);
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Exception while getting Bright InventoryInfo list", e.getStackTrace());
    }
    return infos;
  }


  public HKApiSkuResponse getPVInfoFromBright(ProductVariant productVariant) {
    try {

      String url = brightlifecareRestUrl + "product/variant/" + productVariant.getId() + "/details/";
      ClientRequest request = new ClientRequest(url);
      ClientResponse response = request.get();
      int status = response.getStatus();
      if (status == 200) {
        String data = (String) response.getEntity(String.class);
        HKApiSkuResponse skuResponse = new Gson().fromJson(data, HKApiSkuResponse.class);
        return skuResponse;
      }
    } catch (Exception e) {
      logger.error("Exception while getting Bright product variant Details inventory for Sku", e.getMessage());
    }

    return null;
  }

  public Date getFormattedDate(String strDate) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
      date = formatter.parse(strDate);
    } catch (ParseException e) {
      date = new Date();

    }
    return date;
  }

  public void freezeInventoryForAB(HKAPIForeignBookingResponseInfo info) {
    ForeignSkuItemCLI existingFscli = getBaseDao().get(ForeignSkuItemCLI.class, info.getFsiCLIId());
    SkuItem existingSkuItem = skuItemLineItemService.getSkuItem(existingFscli.getId());
    Long existingSkuItemId =existingFscli.getSkuItemId();

    Sku existingSku = existingSkuItem.getSkuGroup().getSku();
    Long actualSkuItemId = info.getFsiId();
    Long actualSkuGroupId = info.getFsgId();

    // check SkuGroup is already created or not
    SkuGroup skuGroup = skuGroupDao.getForeignSkuGroup(actualSkuGroupId);
    if (skuGroup == null) {
      skuGroup = skuItemLineItemDao.createSkuGroupWithoutBarcode(info.getBatch(), getFormattedDate(info.getMfgdt()), getFormattedDate(info.getExpdt()),
          info.getCp(), info.getMrp(), null, null, null, existingSku);
      skuGroup.setForeignSkuGroupId(actualSkuGroupId);
      skuGroup = (SkuGroup) getBaseDao().save(skuGroup);
    }
    if (actualSkuItemId.equals(existingSkuItemId)) {
      existingSkuItem.setSkuGroup(skuGroup);

    } else {
      // need to check item which i got in response got booked in different order
      ForeignSkuItemCLI fsicliBookedForDifferentOrder = skuItemLineItemService.getFSICI(actualSkuItemId);
      if (fsicliBookedForDifferentOrder != null) {
        SkuItem bookedSkuItemFordifferentOrder = skuItemLineItemService.getSkuItem(fsicliBookedForDifferentOrder.getId());
        String tempBarcode = bookedSkuItemFordifferentOrder.getBarcode();
        bookedSkuItemFordifferentOrder.setBarcode(existingSkuItem.getBarcode());
        String tempBarcodeId = existingSkuItem.getBarcode() + existingSkuItem.getId();
        existingSkuItem.setBarcode(tempBarcodeId);
        existingSkuItem = (SkuItem)  getBaseDao().save(existingSkuItem);
        bookedSkuItemFordifferentOrder = (SkuItem) getBaseDao().save(bookedSkuItemFordifferentOrder);
        existingSkuItem.setBarcode(tempBarcode);
        existingSkuItem.setSkuGroup(skuGroup);

      } else {
        existingSkuItem.setBarcode(info.getBarcode());
        existingSkuItem.setSkuGroup(skuGroup);
      }
    }
    getBaseDao().save(existingSkuItem);
    updateForeignSICLITable(Arrays.asList(info));
  }


  public boolean isBookingRequireAtBright (CartLineItem cartLineItem){
     Product product = cartLineItem.getProductVariant().getProduct();
     if(product.isJit() || product.isDropShipping() || product.isService()){
       return false;
     }
    return true;
  }


  public Boolean bookInventory(CartLineItem cartLineItem) {
    OrderService orderService = ServiceLocatorFactory.getService(OrderService.class);
    LineItem lineItem = lineItemDao.getLineItem(cartLineItem);
    boolean isLineItemCreated = false;
    String tinPrefix = null;
    if (lineItem != null) {
      isLineItemCreated = true;
      tinPrefix = lineItem.getSku().getWarehouse().getTinPrefix();
    }
    Long warehouseIdForBright = null;
    Long warehousIdForAqua = null;
    List<Sku> skuList = new ArrayList<Sku>();
    if (isLineItemCreated) {
      warehousIdForAqua = lineItem.getSku().getWarehouse().getId();
      List<Warehouse> warehouses = warehouseService.findWarehousesByPrefix(tinPrefix);
      warehouses.remove(lineItem.getSku().getWarehouse());
      warehouseIdForBright = warehouses.get(0).getId();

    } else {
      ProductVariant productVariant = cartLineItem.getProductVariant();
      List<Warehouse> aquaWareHouses = warehouseService.getServiceableWarehouses();

      tinPrefix = productVariant.getWarehouse().getTinPrefix();
      List<Warehouse> warehouses = warehouseService.findWarehousesByPrefix(tinPrefix);

      if (aquaWareHouses.contains(productVariant.getWarehouse())) {
        warehousIdForAqua = productVariant.getWarehouse().getId();
        warehouses.remove(productVariant.getWarehouse());
        warehouseIdForBright = warehouses.get(0).getId();

      } else {
        warehouseIdForBright = productVariant.getWarehouse().getId();
        warehouses.remove(productVariant.getWarehouse());
        warehousIdForAqua = warehouses.get(0).getId();
      }

    }
    List<Long> skuStatusIdList = Arrays.asList(EnumSkuItemStatus.Checked_IN.getId());
    List<Long> skuItemOwnerList = Arrays.asList(EnumSkuItemOwner.SELF.getId());
    Warehouse aquaWarehouse = warehouseService.getWarehouseById(warehousIdForAqua);
    Sku sku = skuService.getSKU(cartLineItem.getProductVariant(), aquaWarehouse);
    skuList.add(sku);

    Long countOfAvailableUnBookedSkuItemsInBright = 0L;
    Long countOfAvailableUnBookedSkuItemsInAqua = 0L;

    if (cartLineItem.getSkuItemCLIs() != null && cartLineItem.getSkuItemCLIs().size() > 0) {
      logger.debug("Booking has already been done for CartlineItem : " + cartLineItem.getId());
      return false;
    }

    List<SkuItem> checkAvailableUnbookedSkuItemsInAqua = skuItemDao.getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, cartLineItem.getMarkedPrice());
    if (checkAvailableUnbookedSkuItemsInAqua != null && checkAvailableUnbookedSkuItemsInAqua.size() > 0) {
      countOfAvailableUnBookedSkuItemsInAqua = (long) checkAvailableUnbookedSkuItemsInAqua.size();
    }
    if (countOfAvailableUnBookedSkuItemsInAqua >= cartLineItem.getQty()) {
      if(cartLineItem.getSkuItemCLIs() == null || cartLineItem.getSkuItemCLIs().size() < 1) {
       cartLineItem = tempBookAquaInventory(cartLineItem, warehousIdForAqua);
      }
      if(lineItem.getSkuItemLineItems() == null || lineItem.getSkuItemLineItems().size() < 1){
         skuItemLineItemService.createNewSkuItemLineItem(lineItem);
      }
    } else {
      // Bright call
      if (isLineItemCreated) {
        countOfAvailableUnBookedSkuItemsInBright = getUnbookedInventoryOfBrightForMrp(cartLineItem.getProductVariant(), tinPrefix, cartLineItem.getMarkedPrice());
      } else {
        countOfAvailableUnBookedSkuItemsInBright = getUnbookedInventoryOfBright(cartLineItem.getProductVariant());
      }
      if (countOfAvailableUnBookedSkuItemsInBright >= cartLineItem.getQty()) {
        // Bright inventory booking required
        createSicliAndSiliAndTempBookingForBright(cartLineItem,warehouseIdForBright);
      }
    }
    return true;
  }


  public Map<String,Long> getInventoryCountOfAB (CartLineItem cartLineItem , Warehouse targetWarehouse){

    LineItem lineItem = lineItemDao.getLineItem(cartLineItem);
    List<Sku> skuList = new ArrayList<Sku>();

    String  tinPrefix = null;
    if (targetWarehouse == null) {
        tinPrefix = lineItem.getSku().getWarehouse().getTinPrefix();
        skuList.add(lineItem.getSku());
    } else {
        tinPrefix = targetWarehouse.getTinPrefix();
       Sku sku =  skuService.getSKU(cartLineItem.getProductVariant(),targetWarehouse);
       skuList.add(sku)  ;
    }

    List<Long> skuStatusIdList =Arrays.asList(EnumSkuItemStatus.Checked_IN.getId());
    List<Long> skuItemOwnerList = Arrays.asList(EnumSkuItemOwner.SELF.getId());
    Map<String, Long> invMap = new HashMap <String, Long> ();

    Long countOfAvailableUnBookedSkuItemsInBright = 0L;
    Long countOfAvailableUnBookedSkuItemsInAqua = 0L;
    List<SkuItem> checkAvailableUnbookedSkuItemsInAqua = skuItemDao.getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, lineItem.getMarkedPrice());

    if (checkAvailableUnbookedSkuItemsInAqua != null && checkAvailableUnbookedSkuItemsInAqua.size() > 0){
      countOfAvailableUnBookedSkuItemsInAqua = (long)checkAvailableUnbookedSkuItemsInAqua.size();
    }
     invMap.put("aquaInventory",countOfAvailableUnBookedSkuItemsInAqua);
      countOfAvailableUnBookedSkuItemsInBright = getUnbookedInventoryOfBrightForMrp(cartLineItem.getProductVariant(), tinPrefix, lineItem.getMarkedPrice());
     invMap.put("brtInventory",countOfAvailableUnBookedSkuItemsInBright);
    return invMap;
  }



   public void createSicliAndSiliAndTempBookingForBright(CartLineItem cartLineItem, Long warehouseIdForBright) {
     OrderService orderService = ServiceLocatorFactory.getService(OrderService.class);
     if (cartLineItem.getForeignSkuItemCLIs() == null || cartLineItem.getForeignSkuItemCLIs().size() < 1) {
       if (cartLineItem.getSkuItemCLIs() == null || cartLineItem.getSkuItemCLIs().size() < 1) {
         cartLineItem = tempBookBrightInventory(cartLineItem, warehouseIdForBright);
         populateSICLI(cartLineItem);
       }
       LineItem lineItem = lineItemDao.getLineItem(cartLineItem);
       if (orderService.bookedOnBright(cartLineItem) && (lineItem.getSkuItemLineItems() == null || lineItem.getSkuItemLineItems().size() < 1)) {
         logger.debug("Update booking on Bright");
         List<HKAPIForeignBookingResponseInfo> infos = orderService.updateBookedInventoryOnBright(lineItem, warehouseIdForBright);
         List<ForeignSkuItemCLI> ForeignSkuItemCLIs = skuItemLineItemService.updateSkuItemForABJit(infos);
         skuItemLineItemService.populateSILIForABJit(ForeignSkuItemCLIs, lineItem);

       }
     }
   }


}
