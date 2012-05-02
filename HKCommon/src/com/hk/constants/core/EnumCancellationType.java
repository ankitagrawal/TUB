package com.hk.constants.core;

import com.hk.domain.courier.Courier;


/**
 * Generated
 */
public enum EnumCancellationType {

  Payment_Not_Received(10L, "Payment Not Received"),
  Product_Not_Available(20L, "Product Not Available"),
  Shipment_Delay(30L, "Shipment Delay"),
  Delivery_Delay(40L, "Delivery Delay"),
  Customer_Not_Interested(50L, "Customer Not Interested"),
  COD_Cancellation(60L, "COD Cancellation"),
  Service_Not_Available(70L, "Service Not Available"),
  Other(100L, "Other"),
  ;

  private String name;
  private Long id;

  EnumCancellationType(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }


  public Courier asCourier() {
    Courier courier = new Courier();
    courier.setId(this.id);
    courier.setName(this.name);
    return courier;
  }
}