package com.hk.domain;


import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hk.domain.courier.Courier;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;


@SuppressWarnings("serial")
@Entity
@Table(name = "ticket_history")
public class TicketHistory implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner", nullable = false)
  private User owner;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ticket_type_id", nullable = false)
  private TicketType ticketType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ticket_status_id", nullable = false)
  private TicketStatus ticketStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ticket_id", nullable = false)
  private Ticket ticket;


  @Column(name = "comment", length = 65535)
  private String comment;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "create_date", nullable = false, length = 19)
  private Date createDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "changed_by", nullable = false)
  private User changedBy;

  @Column(name = "short_description", nullable = false, length = 200)
  private String shortDescription;

  @Column(name = "full_description_changed", nullable = false)
  private Boolean fullDescriptionChanged;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "associated_user", nullable = true)
  private User associatedUser;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "associated_order", nullable = true)
  private Order associatedOrder;

  @Column(name = "associated_email", length = 45, nullable = true)
  private String associatedEmail;

  @Column(name = "associated_phone", length = 20, nullable = true)
  private String associatedPhone;

  @Column(name = "associated_tracking_id", length = 20, nullable = true)
  private String associatedTrackingId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "associated_courier", nullable = true)
  private Courier associateCourier;


  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getOwner() {
    return this.owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public TicketType getTicketType() {
    return this.ticketType;
  }

  public void setTicketType(TicketType ticketType) {
    this.ticketType = ticketType;
  }

  public TicketStatus getTicketStatus() {
    return this.ticketStatus;
  }

  public void setTicketStatus(TicketStatus ticketStatus) {
    this.ticketStatus = ticketStatus;
  }

  public Ticket getTicket() {
    return this.ticket;
  }

  public void setTicket(Ticket ticket) {
    this.ticket = ticket;
  }

  public String getComment() {
    return this.comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
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

  public Boolean isFullDescriptionChanged() {
    return fullDescriptionChanged;
  }

  public void setFullDescriptionChanged(Boolean fullDescriptionChanged) {
    this.fullDescriptionChanged = fullDescriptionChanged;
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

  public Courier getAssociateCourier() {
    return associateCourier;
  }

  public void setAssociateCourier(Courier associateCourier) {
    this.associateCourier = associateCourier;
  }
}


