package com.hk.constants.shippingOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hk.domain.order.ShippingOrderLifeCycleActivity;

                                            
public enum EnumShippingOrderLifecycleActivity {

  SO_Created(600L, "SO Created"),
  SO_ConfirmedAuthorization(610L, "SO Confirmed Authorization"),
  SO_WarehouseChanged(612L, "Warehouse Changed"),
  SO_Split(613L, "Shipping Order Split"),
  SO_AutoEscalatedToDropShippingQueue(614L,"SO Auto-esclated TO DropShipping Queue"),  
  SO_AutoEscalatedToProcessingQueue(615L, "SO Auto-escalated To Processing Queue"),
  SO_Shipment_Auto_Created(616L, "SO Shipment Auto Created"),
  SO_CouldNotBeAutoEscalatedToProcessingQueue(617L, "SO Could not be Auto-escalated"),
  SO_CouldNotBeManuallyEscalatedToProcessingQueue(618L, "SO Could not be Manually-escalated"),
  SO_ShipmentNotCreated(619L,"SO Shipment Not Created"),
  SO_EscalatedToProcessingQueue(620L, "SO Manually Escalated To Processing Queue"),
  SO_EscalatedToDropShippingQueue(622L, "SO Manually Escalated To Drop Shipping Queue"),  
  SO_ChosenForPrinting(625L, "SO  Chosen for Printing and sent to Printing Queue"),
  SO_InPicking(626L, "SO in picking"),
  SO_BackToPackingQueue(627L, "SO Back To Processing Queue"),
  SO_BackToDropShippingQueue(628L, "SO Back To Drop Shipping Queue"),  
  SO_CheckedOut(630L, "SO Checked out"),
  SO_ReCheckedout(632L, "SO Re-Checked out"),
  SO_EscalatedToShipmentQueue(635L, "SO Escalated To Shipment Queue"),
  SO_EscalatedBackToActionQueue(640L, "SO Escalated Back To Action Queue"),
  SO_Packed(650L, "SO Packed"),
  SO_ShipmentDetailSaved(652L, "Shipment Details saved"),
  SO_Shipped(655L, "SO Shipped"),
  SO_ShippedEmailFired(658L, "SO Shipped Email Fired"),
  SO_Delivered(660L, "SO Delivered"),
  SO_Installed(662L, "SO Installed"),  
  SO_DeliveredUpdated(665L, "SO Delivered Updated"),
  SO_Returned(670L, "SO Returned"),
  SO_ReCheckedIn(675L, "SO Re-CheckedIn Item"),
  SO_CheckedInDamageItem(678L, "SO CheckedIn Damage Item"),
  SO_LoggedComment(680L, "SO Logged Comment"),
  SO_PutOnHold(685L, "SO Put OnHold"),
  SO_RemovedOnHold(690L, "SO Removed OnHold"),
  SO_Lost(700L, "SO Lost"),
  SO_Edited(710L, "SO Edited"),
  SO_LineItemDeleted(711L, "SO Line Item Deleted"),
  SO_ProductLineItemAdded(712L, "SO Product Line Item Added"),
  SO_ShippingLineItemAdded(713L, "SO Shipping Line Item Added"),
  SO_LineItemUpdated(714L, "SO Line Item Updated"),
  SO_PaymentUpdated(715L, "SO Order Payment Updated"),
  SO_RewardPointLineItemAdded(716L, "SO Reward Point Line Item Added"),
  SO_StatusChanged(750L,"SO Status changed"),
  RTO_Initiated(760L,"RTO Initiated for SO"),
  RO_Created(770L, "Replacement Order Created for shipping order"),
  SHIPMENT_RESOLUTION_ACTIVITY(800L, "SHIPMENT RESOLUTION ACTIVITY"),
  SO_Cancelled(999L, "SO  Cancelled");


  private String name;
  private Long id;

  EnumShippingOrderLifecycleActivity(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

  public ShippingOrderLifeCycleActivity asShippingOrderLifecycleActivity() {
    ShippingOrderLifeCycleActivity shippingOrderLifeCycleActivity = new ShippingOrderLifeCycleActivity();
    shippingOrderLifeCycleActivity.setId(this.getId());
    shippingOrderLifeCycleActivity.setName(this.getName());
    return shippingOrderLifeCycleActivity;
  }


  public static List<Long> getSOLifecycleActivityIDs(List<EnumShippingOrderLifecycleActivity> enumSOLifeCycleActivities) {
    List<Long> lifeCycleActivityIds = new ArrayList<Long>();
    for (EnumShippingOrderLifecycleActivity enumSOLifecycleActivity : enumSOLifeCycleActivities) {
      lifeCycleActivityIds.add(enumSOLifecycleActivity.getId());
    }
    return lifeCycleActivityIds;
  }

  public static List<EnumShippingOrderLifecycleActivity> getActivitiesForPackingQueue() {
    return Arrays.asList(EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToProcessingQueue,
        EnumShippingOrderLifecycleActivity.SO_EscalatedToProcessingQueue);
  }

    public static List<EnumShippingOrderLifecycleActivity> getActivitiesForDropShippingQueue() {
    return Arrays.asList(EnumShippingOrderLifecycleActivity.SO_AutoEscalatedToDropShippingQueue,
        EnumShippingOrderLifecycleActivity.SO_EscalatedToDropShippingQueue);
  }

  public static List<EnumShippingOrderLifecycleActivity> getActivitiesForActionQueue() {
     return Arrays.asList(EnumShippingOrderLifecycleActivity.SO_EscalatedBackToActionQueue,EnumShippingOrderLifecycleActivity.SO_CouldNotBeAutoEscalatedToProcessingQueue,SO_CouldNotBeManuallyEscalatedToProcessingQueue,EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated);
  }
}
