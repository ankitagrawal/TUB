package com.hk.constants.sku;

import com.hk.domain.sku.SkuItemStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/26/12
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumSkuItemStatus {

  Checked_IN(10L, "CHECKED IN"),
  Checked_OUT(20L, "CHECKED OUT"),
  Stock_Transfer_Out(30L, "Stock Transfer Out"),
  Damaged(40L, "Damaged"),
  Expired(50L, "Expired"),
  Lost(60L, "Lost"),
  BatchMismatch(70L, "Batch Mismatch"),
  MrpMismatch(80L, "Mrp Mismatch"),
  FreeVariant(90L, "Free Variant"),
  NonMoving(100L, "Non Moving"),
  ProductVariantAudited(110L, "Product Variant Audited"),
  IncorrectCounting(120L, "Incorrect Counting"),
  NearExpiry(130L,"Near Expiry"),
  AuditSubstract(140L,"Audit Substract"),
  TEMP_BOOKED(150L, "TEMP BOOKED"),
  BOOKED(160L, "BOOKED");

  private Long id;
  private String name;

  EnumSkuItemStatus(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public SkuItemStatus getSkuItemStatus() {
    SkuItemStatus skuItemStatus = new SkuItemStatus();
    skuItemStatus.setId(this.id);
    skuItemStatus.setName(this.name);
    return skuItemStatus;
  }

  public static EnumSkuItemStatus getSkuItemStatusById(Long id){
    for(EnumSkuItemStatus status : values()){
      if (status.getId().equals(id)){
        return status;
      }
    }
    return null;
  }



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public static List<EnumSkuItemStatus> getStatusForNetPhysicalInventory() {
    return Arrays.asList(EnumSkuItemStatus.Checked_IN, EnumSkuItemStatus.TEMP_BOOKED, EnumSkuItemStatus.BOOKED);
  }

   public static List<Long> getCheckedInPlusBookedStatus() {
    return Arrays.asList(EnumSkuItemStatus.Checked_IN.getId(), EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemStatus.BOOKED.getId());
  }


  public static List<Long> getSkuItemStatusIDs(List<EnumSkuItemStatus> enumSkuItemStatuses) {
    List<Long> skuItemStatusIds = new ArrayList<Long>();
    for (EnumSkuItemStatus enumOrderStatus : enumSkuItemStatuses) {
      skuItemStatusIds.add(enumOrderStatus.getId());
    }
    return skuItemStatusIds;
  }

}
