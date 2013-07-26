package com.hk.constants.order;


public enum EnumOrderLifecycleActivity {
  OrderPlaced(10L, "Order Placed"),
  OrderSplit(11L, "Order Auto Split"),
  OrderCouldNotBeAutoSplit(12L, "Order Could not be Split"),
  OrderManualSplit(13L, "Order Manual Split"),
  Cod_Conversion(14L, "Payment Mode changed from COD to Online Payment"),
  PaymentMarkedSuccessful(20L, "Payment Marked Successful"),
  PaymentUpdatedAsSuccessful(25L, "Payment Updated As Successful"),
  PaymentMarkedAuthPending(30L, "Payment Marked Auth Pending"),
  PaymentAssociatedToOrder(40L, "Payment Associated To Order"),
  ConfirmedAuthorization(50L, "Confirmed Authorization"),
  AutoEscalatedToProcessingQueue(55L, "Auto-escalated To Processing Queue"),
  EscalatedToProcessingQueue(60L, "Escalated To Processing Queue"),
  EscalatedPartiallyToProcessingQueue(65L, "Escalated Partially To Processing Queue"),
  SpecialyChosenForPrinting(61L,"Specialy Chosen For Printing"),
  ChosenForPrinting(62L, "Chosen for Printing and sent to Printing Queue"),
  OrderInProcess(63L, "Order in Process, i.e picking going on"),
  BackToProcessingQueue(64L, "Back To Processing Queue"),
  CheckedoutItem(67L, "Checkedout Item"),
  ReCheckedoutItem(68L, "Re-Checkedout Item"),
  EscalatedToShipmentQueue(70L, "Escalated To Shipment Queue"),
  EscalatedBackToAwaitingQueue(80L, "Escalated Back To Awaiting Queue"),
  OrderPacked(82L, "Shipping Order Packed"),
  OrderHandedOverToCourier(85L, "Order Handed Over To Courier"),
  OrderShipped(90L, "Order Shipped"),
  OrderShippedEmailFired(95L, "Order Shipped Email Fired"),
  OrderDelivered(100L, "Order Delivered"),
  OrderDeliveredUpdated(105L, "Order Delivered Updated"),
  OrderInstalled(108L, "Order Installed"),  
  OrderReturned(110L, "Order Returned"),
  OrderPartiallyReturned(111L, "Partial Order Returned"),
  ReCheckedInItem(113L, "Re-CheckedIn Item"),
  CheckedInDamageItem(117L, "CheckedIn Damage Item"),
  LoggedComment(120L, "Logged Comment"),
  OrderPutOnHold(130L, "Order Put OnHold"),
  OrderRemovedOnHold(140L, "Order Removed OnHold"),
  OrderLost(150L, "Order Lost"),
  OrderPartiallyLost(160L, "Order Partially Lost"),
  OrderEdited(170L, "Order Edited"),
  LineItemDeleted(180L, "Line Item Deleted"),
  ProductLineItemAdded(190L, "Product Line Item Added"),
  ShippingLineItemAdded(200L, "Shipping Line Item Added"),
  LineItemUpdated(210L, "Line Item Updated"),
  OrderPaymentUpdated(220L, "Order Payment Updated"),
  OrderKiMBOpened(230L, "Order Ki MB"),
  RewardPointLineItemAdded(240L, "Reward Point Line Item Added"),
  RewardPointsApproved(250L, "Pending Reward Point Associated With Order Approved"),
  EmailSentToServiceProvider(260L, "Email Sent to Service Provider for Service Order"),
  OrderCancelled(500L, "Order Cancelled"),
  RewardPointOrderCancel(270L,"Reward Point given on order cancellation"),
  AmountRefundedOrderCancel(280L,"Amount Refunded on order cancellation"),
  RefundAmountFailed(290L,"Amount couldn't be refunded to user, Please contact tech support"),
  RefundAmountExceedsFailed(300L,"Amount exceeds the refundable amount"),
  RefundAmountInProcess(400L,"Refund is in process, Please contact tech support"),
  ;

  private String name;
  private Long id;

  EnumOrderLifecycleActivity(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

 /* public OrderLifecycleActivity asOrderLifecycleActivity() {
    OrderLifecycleActivity orderLifecycleActivity = new OrderLifecycleActivity();
    orderLifecycleActivity.setId(this.getId());
    orderLifecycleActivity.setName(this.getName());
    return orderLifecycleActivity;
  }


  public static List<Long> getLifecycleActivityIDs(List<EnumOrderLifecycleActivity> enumOrderLifeCycleActivities) {
    List<Long> lifeCycleActivityIds = new ArrayList<Long>();
    for (EnumOrderLifecycleActivity enumOrderLifecycleActivity : enumOrderLifeCycleActivities) {
      lifeCycleActivityIds.add(enumOrderLifecycleActivity.getId());
    }
    return lifeCycleActivityIds;
  }

  public static List<EnumOrderLifecycleActivity> getActivitiesForPackingQueue() {
    return Arrays.asList(EnumOrderLifecycleActivity.AutoEscalatedToProcessingQueue, EnumOrderLifecycleActivity.EscalatedToProcessingQueue, EnumOrderLifecycleActivity.EscalatedPartiallyToProcessingQueue);
  }*/

}
