package com.hk.constants.shippingOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hk.domain.order.ShippingOrderStatus;


public enum EnumShippingOrderStatus {

  SO_ActionAwaiting(110L, "SO Action Awaiting"),     //when shipping order could not be auto escalated
  SO_OnHold(112L, "SO On Hold"),
  SO_EscalatedBack(115L, "SO Escalated Back"),
  SO_ReadyForProcess(120L, "SO Ready for Process"),     //same as escalated to packing queue
  SO_ReadyForDropShipping(125L, "SO ready for drop shipping"),
  SO_MarkedForPrinting(130L, "SO Gone for Printing"),
  SO_Picking(140L, "SO Picking"),
  SO_CheckedOut(150L, "SO Checked Out"),
  SO_Packed(160L, "SO Packed"),
  SO_Shipped(180L, "SO Shipped"),
  SO_Delivered(190L, "SO Delivered"),
  SO_Installed(195L, "SO Installed"),
  RTO_Initiated(230L, "RTO Initiated"),
  SO_RTO(200L, "SO RTO"),
  SO_Lost(210L, "SO Lost"),
  SO_Customer_Return_Replaced(250L, "SO Customer Return and Replace"),
  SO_Customer_Return_Refunded(260L, "SO Customer Return and Refund"),
  SO_Customer_Appeasement(270L, "SO Customer Satisfaction"),
  SO_ReversePickup_Initiated(280L, "SO Reverse Pickup Initiated"),
  SO_Cancelled(999L, "SO Cancelled");


  private java.lang.String name;

  private java.lang.Long id;

  EnumShippingOrderStatus(Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

  public ShippingOrderStatus asShippingOrderStatus() {
    ShippingOrderStatus shippingOrderStatus = new ShippingOrderStatus();
    shippingOrderStatus.setId(this.getId());
    shippingOrderStatus.setName(this.getName());
    return shippingOrderStatus;
  }

  public static List<Long> getShippingOrderStatusIDs(List<EnumShippingOrderStatus> enumShippingOrderStatuses) {
    List<Long> shippingOrderStatusIds = new ArrayList<Long>();
    for (EnumShippingOrderStatus enumOrderStatus : enumShippingOrderStatuses) {
      shippingOrderStatusIds.add(enumOrderStatus.getId());
    }
    return shippingOrderStatusIds;
  }

  public static List<EnumShippingOrderStatus> getStatusForProcessingQueue() {
    return Arrays.asList(EnumShippingOrderStatus.SO_ReadyForProcess,
        EnumShippingOrderStatus.SO_MarkedForPrinting, EnumShippingOrderStatus.SO_Picking, EnumShippingOrderStatus.SO_CheckedOut);
  }


  public static List<EnumShippingOrderStatus> getStatusForDropShippingQueue() {
    return Arrays.asList(EnumShippingOrderStatus.SO_ReadyForDropShipping, EnumShippingOrderStatus.SO_CheckedOut,
        EnumShippingOrderStatus.SO_Shipped, EnumShippingOrderStatus.SO_Delivered, EnumShippingOrderStatus.SO_Installed);
  }

  public static List<EnumShippingOrderStatus> getStatusForBookedInventory() {
    return Arrays.asList(EnumShippingOrderStatus.SO_ActionAwaiting,
        EnumShippingOrderStatus.SO_OnHold,
        EnumShippingOrderStatus.SO_ReadyForProcess,
        EnumShippingOrderStatus.SO_MarkedForPrinting,
        EnumShippingOrderStatus.SO_Picking,
        EnumShippingOrderStatus.SO_EscalatedBack);
  }

  public static List<EnumShippingOrderStatus> getStatusForBookedInventoryInProcessingQueue() {
    return Arrays.asList(
        EnumShippingOrderStatus.SO_ReadyForProcess,
        EnumShippingOrderStatus.SO_MarkedForPrinting,
        EnumShippingOrderStatus.SO_Picking
    );
  }

  public static List<EnumShippingOrderStatus> getStatusForPuttingOrderOnHold() {

    return Arrays.asList(EnumShippingOrderStatus.SO_ActionAwaiting
//                ,
//                EnumShippingOrderStatus.SO_ReadyForProcess,
//                EnumShippingOrderStatus.SO_MarkedForPrinting,
//                EnumShippingOrderStatus.SO_Picking,
//                EnumShippingOrderStatus.SO_CheckedOut,
//                EnumShippingOrderStatus.SO_Packed
    );
  }


  public static List<EnumShippingOrderStatus> getStatusForActionQueue() {
    return Arrays.asList(EnumShippingOrderStatus.SO_ActionAwaiting, EnumShippingOrderStatus.SO_OnHold);//, EnumShippingOrderStatus.SO_EscalatedBack);
  }

  public static List<Long> getStatusIdsForActionQueue() {
    return Arrays.asList(SO_ActionAwaiting.getId(), SO_OnHold.getId());
  }

  public static List<Long> getStatusIdsForProcessingQueue() {
    return getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForProcessingQueue());
  }

  public static List<EnumShippingOrderStatus> getStatusForPicking() {
    return Arrays.asList(EnumShippingOrderStatus.SO_MarkedForPrinting);
  }

  public static List<EnumShippingOrderStatus> getStatusForPrinting() {
    return Arrays.asList(EnumShippingOrderStatus.SO_ReadyForProcess);
  }

  public static List<EnumShippingOrderStatus> getStatusForShipmentAwaiting() {
    return Arrays.asList(EnumShippingOrderStatus.SO_Packed);
  }

  public static List<EnumShippingOrderStatus> getStatusForDeliveryAwaiting() {
    return Arrays.asList(EnumShippingOrderStatus.SO_Shipped);
  }

  public static List<EnumShippingOrderStatus> getStatusSearchingInDeliveryQueue() {
    return Arrays.asList(EnumShippingOrderStatus.SO_Shipped, EnumShippingOrderStatus.SO_RTO, EnumShippingOrderStatus.SO_Lost);
  }


  public static List<EnumShippingOrderStatus> getStatusSearchingInInstallationQueue() {
    return Arrays.asList(EnumShippingOrderStatus.SO_Shipped, EnumShippingOrderStatus.SO_Delivered, EnumShippingOrderStatus.SO_RTO, EnumShippingOrderStatus.SO_Lost);
  }

  public static List<EnumShippingOrderStatus> getStatusForCRMReport() {

    return Arrays.asList(EnumShippingOrderStatus.SO_Packed,
        EnumShippingOrderStatus.SO_Shipped,
        EnumShippingOrderStatus.SO_Delivered);
  }

  public static List<ShippingOrderStatus> getStatusForChangingShipmentDetails() {
    return Arrays.asList(EnumShippingOrderStatus.SO_Shipped.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_Delivered.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_Lost.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_RTO.asShippingOrderStatus(),
        EnumShippingOrderStatus.RTO_Initiated.asShippingOrderStatus());
  }

  public static List<ShippingOrderStatus> getStatusForReconcilationReport() {
    return Arrays.asList(EnumShippingOrderStatus.SO_Shipped.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_Delivered.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_RTO.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_Lost.asShippingOrderStatus(),
        EnumShippingOrderStatus.RTO_Initiated.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_ReversePickup_Initiated.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_Customer_Appeasement.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_Customer_Return_Refunded.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_Customer_Return_Replaced.asShippingOrderStatus());
  }

  public static List<EnumShippingOrderStatus> getStatusForShipmentResolution() {
    return Arrays.asList(EnumShippingOrderStatus.SO_ActionAwaiting,
        EnumShippingOrderStatus.SO_OnHold,
        EnumShippingOrderStatus.SO_ReadyForDropShipping,
        EnumShippingOrderStatus.SO_Shipped
//                EnumShippingOrderStatus.SO_Picking,
//                EnumShippingOrderStatus.SO_Installed,
//                EnumShippingOrderStatus.SO_ReadyForProcess,
//                EnumShippingOrderStatus.SO_MarkedForPrinting,
//                EnumShippingOrderStatus.SO_Packed,
//                EnumShippingOrderStatus.SO_CheckedOut
    );
  }

  public static List<EnumShippingOrderStatus> getStatusForCreateUpdateShipment() {
    return Arrays.asList(
//                   EnumShippingOrderStatus.SO_Packed,
        EnumShippingOrderStatus.SO_CheckedOut,
        EnumShippingOrderStatus.SO_ReadyForDropShipping);
  }

  public static List<EnumShippingOrderStatus> getStatusForEnteringShippingCost() {
    return Arrays.asList(EnumShippingOrderStatus.SO_Shipped,
        EnumShippingOrderStatus.SO_Lost,
        EnumShippingOrderStatus.SO_RTO,
        EnumShippingOrderStatus.SO_Delivered);
  }

  public static List<ShippingOrderStatus> getStatusForReCheckinReturnItems() {
    return Arrays.asList(EnumShippingOrderStatus.SO_RTO.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_Customer_Return_Replaced.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_Customer_Return_Refunded.asShippingOrderStatus(),
        EnumShippingOrderStatus.SO_ReversePickup_Initiated.asShippingOrderStatus());
  }

  public static List<Long> getApplicableShippingOrderStatus() {
    return Arrays.asList(EnumShippingOrderStatus.SO_Shipped.getId(),
        EnumShippingOrderStatus.SO_Delivered.getId(),
        EnumShippingOrderStatus.RTO_Initiated.getId(),
        EnumShippingOrderStatus.SO_RTO.getId(),
        EnumShippingOrderStatus.SO_ReversePickup_Initiated.getId());
  }

}
