package com.hk.admin.dto.ticket;

import java.util.Date;

import com.hk.domain.TicketStatus;
import com.hk.domain.TicketType;
import com.hk.domain.user.User;

/**
 * User: rahul
 * Time: 10 Mar, 2010 2:35:26 PM
 */
public class TicketFilterDto {

  private Long          ticketId          ;
  private String        keywords          ;
  private User          owner             ;
  private User          reporter          ;
  private TicketType ticketType        ;
  private TicketStatus ticketStatus      ;
  private Date          createDateFrom    ;
  private Date          createDateTo      ;
  private Long          associatedUserId  ;
  private Long          associatedOrderId ;
  private String        associatedLogin   ;
  private String        associatedPhone   ;

  public Long getTicketId() {
    return ticketId;
  }

  public void setTicketId(Long ticketId) {
    this.ticketId = ticketId;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
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

  public Date getCreateDateFrom() {
    return createDateFrom;
  }

  public void setCreateDateFrom(Date createDateFrom) {
    this.createDateFrom = createDateFrom;
  }

  public Date getCreateDateTo() {
    return createDateTo;
  }

  public void setCreateDateTo(Date createDateTo) {
    this.createDateTo = createDateTo;
  }

  public Long getAssociatedUserId() {
    return associatedUserId;
  }

  public void setAssociatedUserId(Long associatedUserId) {
    this.associatedUserId = associatedUserId;
  }

  public Long getAssociatedOrderId() {
    return associatedOrderId;
  }

  public void setAssociatedOrderId(Long associatedOrderId) {
    this.associatedOrderId = associatedOrderId;
  }

  public String getAssociatedLogin() {
    return associatedLogin;
  }

  public void setAssociatedLogin(String associatedLogin) {
    this.associatedLogin = associatedLogin;
  }

  public String getAssociatedPhone() {
    return associatedPhone;
  }

  public void setAssociatedPhone(String associatedPhone) {
    this.associatedPhone = associatedPhone;
  }
}
