package com.hk.admin.pact.service.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;

import java.util.List;

public interface MasterInventoryService {

  public List<SKUDetails> getSkuDetails(ProductVariant productVariant, Double mrp); //Master method to get details of all SKUs of a PV

  public Long getCheckedInUnits(List<Sku> skuList, Double mrp);//CI

  public Long getUnderReviewCheckedInUnits(List<Sku> skuList, Double mrp);//CI + Under Review

  public Long getBookedUnits(List<Sku> skuList, Double mrp);//TB+Booked

  public Long getUnderReviewBookedUnits(List<Sku> skuList, Double mrp);//(TB+Booked) + UnderReview

  public Long getUncheckedOutUnits(List<Sku> skuList, Double mrp);//CI+TB+Booked = Net Inventory

  public Long getUnbookedCLIUnits(ProductVariant productVariant, Double mrp);//NB_CLI = CLIs for which no booking took place

  public Long getUnbookedLIUnits(List<Sku> skuList, Double mrp);//NB_LI = LIs for which no booking took place

  public Long getLIUnits(List<Sku> skuList, List<Long> shippingOrderStatusIds);//For units in AQ and PQ

  public Long getUnsplitBOCLIUnits(ProductVariant productVariant);//For units of unsplit BOs

  public Long getAvailableUnits(ProductVariant productVariant, Double mrp); //AI = CI-NB-US

  public Long getUnderReviewUnits(ProductVariant productVariant, Double mrp);

  public static class SKUDetails {
    private Sku sku;
    private Long phyQty;
    private Long ciQty; //Not under review
    private Long ciQtyUnderReview;
    private Long bookedQty; //Not under review
    private Long bookedQtyUnderReview;
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

    public Long getCiQtyUnderReview() {
      return ciQtyUnderReview;
    }

    public void setCiQtyUnderReview(Long ciQtyUnderReview) {
      this.ciQtyUnderReview = ciQtyUnderReview;
    }

    public Long getBookedQty() {
      return bookedQty;
    }

    public void setBookedQty(Long bookedQty) {
      this.bookedQty = bookedQty;
    }

    public Long getBookedQtyUnderReview() {
      return bookedQtyUnderReview;
    }

    public void setBookedQtyUnderReview(Long bookedQtyUnderReview) {
      this.bookedQtyUnderReview = bookedQtyUnderReview;
    }

    public Long getUnbookedLIQty() {
      return unbookedLIQty;
    }

    public void setUnbookedLIQty(Long unbookedLIQty) {
      this.unbookedLIQty = unbookedLIQty;
    }
  }

}