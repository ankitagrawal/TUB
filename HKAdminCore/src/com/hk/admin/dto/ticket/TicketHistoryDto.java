package com.hk.admin.dto.ticket;

import com.hk.domain.Ticket;
import com.hk.domain.TicketStatus;
import com.hk.domain.TicketType;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;

/**
 * User: rahul
 * Time: 9 Mar, 2010 6:43:10 PM
 */
public class TicketHistoryDto {

  private Ticket ticket;
  private String Comment;
  private TicketStatus ticketStatus;
  private TicketType ticketType;
  private User owner;
  private User changedBy;
  private String shortDescription;
  private String fullDescription;
  private User associatedUser;
  private Order associatedOrder;
  private String associatedEmail;
  private String associatedPhone;
  private String associatedTrackingId;
  private Courier associatedCourier;
  private boolean fullDescriptionChanged;

  public Ticket getTicket() {
    return ticket;
  }

  public void setTicket(Ticket ticket) {
    this.ticket = ticket;
  }

  public String getComment() {
    return Comment;
  }

  public void setComment(String comment) {
    Comment = comment;
  }

  public TicketStatus getTicketStatus() {
    return ticketStatus;
  }

  public void setTicketStatus(TicketStatus ticketStatus) {
    this.ticketStatus = ticketStatus;
  }

  public TicketType getTicketType() {
    return ticketType;
  }

  public void setTicketType(TicketType ticketType) {
    this.ticketType = ticketType;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public User getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(User changedBy) {
    this.changedBy = changedBy;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getFullDescription() {
    return fullDescription;
  }

  public void setFullDescription(String fullDescription) {
    this.fullDescription = fullDescription;
  }

  public User getAssociatedUser() {
    return associatedUser;
  }

  public void setAssociatedUser(User associatedUser) {
    this.associatedUser = associatedUser;
  }

  public Order getAssociatedOrder() {
    return associatedOrder;
  }

  public void setAssociatedOrder(Order associatedOrder) {
    this.associatedOrder = associatedOrder;
  }

  public String getAssociatedEmail() {
    return associatedEmail;
  }

  public void setAssociatedEmail(String associatedEmail) {
    this.associatedEmail = associatedEmail;
  }

  public String getAssociatedPhone() {
    return associatedPhone;
  }

  public void setAssociatedPhone(String associatedPhone) {
    this.associatedPhone = associatedPhone;
  }

  public String getAssociatedTrackingId() {
    return associatedTrackingId;
  }

  public void setAssociatedTrackingId(String associatedTrackingId) {
    this.associatedTrackingId = associatedTrackingId;
  }

  public Courier getAssociatedCourier() {
    return associatedCourier;
  }

  public void setAssociatedCourier(Courier associatedCourier) {
    this.associatedCourier = associatedCourier;
  }

  public boolean isFullDescriptionChanged() {
    return fullDescriptionChanged;
  }

  public void setFullDescriptionChanged(boolean fullDescriptionChanged) {
    this.fullDescriptionChanged = fullDescriptionChanged;
  }
}
