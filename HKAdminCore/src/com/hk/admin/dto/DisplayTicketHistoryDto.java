package com.hk.admin.dto;

import java.util.Date;

import com.hk.domain.user.User;

public class DisplayTicketHistoryDto {

  private String ownerChangeMessage;
  private String statusChangeMessage;
  private String typeChangeMessage;
  private String comment;
  private Date changedDate;
  private User changedBy;
  private String shortDescriptionChangeMessage;
  private String fullDescriptionChangeMessage;
  private String associatedUserChangeMessage;
  private String associatedOrderChangeMessage;
  private String associatedEmailChangeMessage;
  private String associatedCourierChangeMessage;
  private String associatedPhoneChangeMessage;
  private String associatedTrackingIdChangeMessage;

  public String getOwnerChangeMessage() {
    return ownerChangeMessage;
  }

  public void setOwnerChangeMessage(String ownerChangeMessage) {
    this.ownerChangeMessage = ownerChangeMessage;
  }

  public String getStatusChangeMessage() {
    return statusChangeMessage;
  }

  public void setStatusChangeMessage(String statusChangeMessage) {
    this.statusChangeMessage = statusChangeMessage;
  }

  public String getTypeChangeMessage() {
    return typeChangeMessage;
  }

  public void setTypeChangeMessage(String typeChangeMessage) {
    this.typeChangeMessage = typeChangeMessage;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Date getChangedDate() {
    return changedDate;
  }

  public void setChangedDate(Date changedDate) {
    this.changedDate = changedDate;
  }

  public User getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(User changedBy) {
    this.changedBy = changedBy;
  }

  public String getShortDescriptionChangeMessage() {
    return shortDescriptionChangeMessage;
  }

  public void setShortDescriptionChangeMessage(String shortDescriptionChangeMessage) {
    this.shortDescriptionChangeMessage = shortDescriptionChangeMessage;
  }

  public String getFullDescriptionChangeMessage() {
    return fullDescriptionChangeMessage;
  }

  public void setFullDescriptionChangeMessage(String fullDescriptionChangeMessage) {
    this.fullDescriptionChangeMessage = fullDescriptionChangeMessage;
  }

  public String getAssociatedUserChangeMessage() {
    return associatedUserChangeMessage;
  }

  public void setAssociatedUserChangeMessage(String associatedUserChangeMessage) {
    this.associatedUserChangeMessage = associatedUserChangeMessage;
  }

  public String getAssociatedOrderChangeMessage() {
    return associatedOrderChangeMessage;
  }

  public void setAssociatedOrderChangeMessage(String associatedOrderChangeMessage) {
    this.associatedOrderChangeMessage = associatedOrderChangeMessage;
  }

  public String getAssociatedEmailChangeMessage() {
    return associatedEmailChangeMessage;
  }

  public void setAssociatedEmailChangeMessage(String associatedEmailChangeMessage) {
    this.associatedEmailChangeMessage = associatedEmailChangeMessage;
  }

  public String getAssociatedCourierChangeMessage() {
    return associatedCourierChangeMessage;
  }

  public void setAssociatedCourierChangeMessage(String associatedCourierChangeMessage) {
    this.associatedCourierChangeMessage = associatedCourierChangeMessage;
  }

  public String getAssociatedPhoneChangeMessage() {
    return associatedPhoneChangeMessage;
  }

  public void setAssociatedPhoneChangeMessage(String associatedPhoneChangeMessage) {
    this.associatedPhoneChangeMessage = associatedPhoneChangeMessage;
  }

  public String getAssociatedTrackingIdChangeMessage() {
    return associatedTrackingIdChangeMessage;
  }

  public void setAssociatedTrackingIdChangeMessage(String associatedTrackingIdChangeMessage) {
    this.associatedTrackingIdChangeMessage = associatedTrackingIdChangeMessage;
  }

}
