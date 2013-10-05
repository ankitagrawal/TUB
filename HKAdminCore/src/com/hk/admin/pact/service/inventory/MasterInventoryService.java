package com.hk.admin.pact.service.inventory;

import com.hk.domain.sku.Sku;
import com.hk.domain.catalog.product.ProductVariant;

import java.util.List;

public interface MasterInventoryService {

  public List<SKUDetails> getSkuDetails(ProductVariant productVariant); //Master method to get details of all SKUs of a PV

  public Long getCheckedInUnits(List<Sku> skuList);//CI
  public Long getBookedUnits(List<Sku> skuList);//TB+Booked
  public Long getUncheckedOutUnits(List<Sku> skuList);//CI+TB+Booked = Net Inventory
  public Long getUnbookedCLIUnits(ProductVariant productVariant);//NB_CLI = CLIs for which no booking took place
  public Long getUnbookedLIUnits(List<Sku> skuList);//NB_LI = LIs for which no booking took place
  public Long getLIUnits(List<Sku> skuList, List<Long> shippingOrderStatusIds);//For units in AQ and PQ
  public Long getUnsplitBOCLIUnits(ProductVariant productVariant);//For units of unsplit BOs
  public Long getAvailableUnits(ProductVariant productVariant); //AI = CI-NB-US

  public static class SKUDetails{
    private Sku sku;
    private Long phyQty;
    private Long ciQty;
    private Long bookedQty;
    private Long unbookedLIQty;

    public Sku getSku() {
      return sku;
    }

    public void setSku(Sku sku) {
      this.sku = sku;
    }

    public Long getPhyQty() {
      return phyQty;
    }

    public void setPhyQty(Long phyQty) {
      this.phyQty = phyQty;
    }

    public Long getCiQty() {
      return ciQty;
    }

    public void setCiQty(Long ciQty) {
      this.ciQty = ciQty;
    }

    public Long getBookedQty() {
      return bookedQty;
    }

    public void setBookedQty(Long bookedQty) {
      this.bookedQty = bookedQty;
    }

    public Long getUnbookedLIQty() {
      return unbookedLIQty;
    }

    public void setUnbookedLIQty(Long unbookedLIQty) {
      this.unbookedLIQty = unbookedLIQty;
    }
  }
  
}