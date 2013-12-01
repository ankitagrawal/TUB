package com.hk.constants.ticket;

import com.hk.domain.TicketStatus;

/**
 * Generated
 */
public enum EnumTicketStatus {

  OPEN(1L, "Open"),
  CLOSSE(2L, "Close"),
  ;

  private java.lang.String name;
  private java.lang.Long id;

  EnumTicketStatus(Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }


  public TicketStatus asTicketStatus() {
    TicketStatus ticketStatus = new TicketStatus();
    ticketStatus.setId(id);
    ticketStatus.setName(name);
    return ticketStatus;
  }
}

