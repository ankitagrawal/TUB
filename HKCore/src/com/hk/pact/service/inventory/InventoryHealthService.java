package com.hk.pact.service.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface InventoryHealthService {

  Collection<InventoryInfo> getAvailableInventory(List<Sku> skus);

  Collection<SkuInfo> getAvailableSkusForSplitter(ProductVariant variant, SkuFilter filter, CartLineItem cartLineItem);

  Collection<SkuInfo> getCheckedInInventory(ProductVariant productVariant, List<Warehouse> whs);

  Collection<InventoryInfo> getAvailableInventory(ProductVariant productVariant);

  Collection<SkuInfo> getAvailableSkus(ProductVariant variant, SkuFilter filter);

  public void inventoryHealthCheck(ProductVariant productVariant);

  public void pendingOrdersInventoryHealthCheck(ProductVariant productVariant);

  public void tempBookSkuLineItemForOrder(Order order);


  public static class InventoryInfo {
    private Collection<SkuInfo> skuList = new ArrayList<SkuInfo>();
    private double mrp;
    private long qty;

    public Collection<SkuInfo> getSkuInfoList() {
      return skuList;
    }

    public void addSkuInfo(SkuInfo skuInfo) {
      skuList.add(skuInfo);
    }

    public double getMrp() {
      return mrp;
    }

    public void setMrp(double mrp) {
      this.mrp = mrp;
    }

    public long getQty() {
      return qty;
    }

    public void setQty(long qty) {
      this.qty = qty;
    }

    public SkuInfo getMaxQtySkuInfo() {
      SkuInfo info = null;
      for (SkuInfo skuInfo : skuList) {
        if (info == null) {
          info = skuInfo;
        } else if (info != null && info.getQty() < skuInfo.getQty()) {
          info = skuInfo;
        }
      }
      return info;
    }
  }

  public static class SkuInfo {
    private long skuId;
    private double mrp;
    private double costPrice;
    private long qty;
    private long unbookedQty; //checkedIn + inQueue
    private Date checkinDate;

    public long getSkuId() {
      return skuId;
    }

    public void setSkuId(long skuId) {
      this.skuId = skuId;
    }

    public double getMrp() {
      return mrp;
    }

    public void setMrp(double mrp) {
      this.mrp = mrp;
    }

    public Date getCheckinDate() {
      return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
      this.checkinDate = checkinDate;
    }

    public long getQty() {
      return qty;
    }

    public void setQty(long qty) {
      this.qty = qty;
    }

    public long getUnbookedQty() {
      return unbookedQty;
    }

    public void setUnbookedQty(long unbookedQty) {
      this.unbookedQty = unbookedQty;
    }

    public double getCostPrice() {
      return costPrice;
    }

    public void setCostPrice(double costPrice) {
      this.costPrice = costPrice;
    }


  }

  public static class SkuFilter {
    private Double mrp;
    private long minQty;
    private FetchType fetchType;
    private Long warehouseId;

    public Double getMrp() {
      return mrp;
    }

    public void setMrp(Double mrp) {
      this.mrp = mrp;
    }

    public long getMinQty() {
      return minQty;
    }

    public void setMinQty(long minQty) {
      this.minQty = minQty;
    }

    public FetchType getFetchType() {
      return fetchType;
    }

    public void setFetchType(FetchType fetchType) {
      this.fetchType = fetchType;
    }

    public Long getWarehouseId() {
      return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
      this.warehouseId = warehouseId;
    }
  }

  public static enum FetchType {
    FIRST_ORDER, ALL
  }
}
