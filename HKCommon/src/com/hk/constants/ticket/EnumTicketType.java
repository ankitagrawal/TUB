package com.hk.constants.ticket;

import com.hk.domain.TicketType;

/**
 * Generated
 */
public enum EnumTicketType {

  Tracking(1L, "Courier Tracking", "Courier Tracking", "admin@test.com"),
  Order_Status(2L, "Order Status", "Order Status", "admin@test.com"),
  Change_Email(3L, "Change Email", "Email Change Request", "admin@test.com"),
  Delete_Account(4L, "Delete Account", "Account Delete Request", "admin@test.com"),
  Payment(5L, "Payment", "Payment refund, track etc.", "admin@test.com"),
  Order_Related(6L, "Order Related", "Gift wrap, message with order, delivery on particular date etc.", "admin@test.com"),
  Product_Enhancement(7L, "Product Enhancement", "Product_Enhancement", "admin@test.com"),
  Other(100L, "Other", "other", "admin@test.com"),
  ;

  private java.lang.String name;
  private java.lang.Long id;
  private java.lang.String description;
  private String defaultOwnerEmail;

  EnumTicketType(Long id, java.lang.String name, java.lang.String description, String defaultOwnerEmail) {
    this.name = name;
    this.id = id;
    this.description = description;
    this.defaultOwnerEmail = defaultOwnerEmail;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

  public java.lang.String getDescription() {
    return description;
  }

  public String getDefaultOwnerEmail() {
    return defaultOwnerEmail;
  }

  public TicketType asTicketType() {
    TicketType ticketType = new TicketType();
    ticketType.setId(id);
    ticketType.setName(name);
    ticketType.setDescription(description);
    return ticketType;
    
  }
}

