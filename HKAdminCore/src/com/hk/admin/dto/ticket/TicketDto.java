package com.hk.admin.dto.ticket;

import com.hk.domain.TicketStatus;
import com.hk.domain.TicketType;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;

/**
 * User: rahul
 * Time: 9 Mar, 2010 3:29:28 PM
 */
public class TicketDto {

  private String shortDescription;
  private TicketType ticketType;
  private TicketStatus ticketStatus;
  private User owner;
  private User reporter;
  private String fullDescription;
  private User associatedUser;

  /**
   * Since we have to show a warning to the user in case we are unable
   * to asscociate order info with the ticket, we are using 2 seperate fields
   * for associated order. Because in case order will not be found in the db,
   * OrderTypeConverter will put null into the associated order field, so the order
   * information will be lost.
   * In order to avoid this situtaion we will use the field associatedOrderId to get
   * order id from the user interface of ticket creation and will manually
   * convert this id into Order object and set that into associatedOrder field.
   * In case order does not exist, we will show some warning to the user after
   * ticket creation. 
   */
  private Long associatedOrderId;
  private Order associatedOrder;

  private String associatedEmail;
  private String associatedPhone;
  private String associatedTrackingId;
  private Courier associatedCourier;

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public TicketType getTicketType() {
    return ticketType;
  }

  public void setTicketType(TicketType ticketType) {
    this.ticketType = ticketType;
  }

  public TicketStatus getTicketStatus() {
    return ticketStatus;
  }

  public void setTicketStatus(TicketStatus ticketStatus) {
    this.ticketStatus = ticketStatus;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public User getReporter() {
    return reporter;
  }

  public void setReporter(User reporter) {
    this.reporter = reporter;
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

  public Long getAssociatedOrderId() {
    return associatedOrderId;
  }

  public void setAssociatedOrderId(Long associatedOrderId) {
    this.associatedOrderId = associatedOrderId;
  }
}
