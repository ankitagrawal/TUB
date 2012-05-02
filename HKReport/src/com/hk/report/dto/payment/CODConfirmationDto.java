package com.hk.report.dto.payment;

import java.util.Date;

import com.hk.domain.order.Order;


public class CODConfirmationDto {

  Date orderPlacedDate;
  Order order;
  String userEmail;
  String userName;
  Date orderConfirmationDate;
  Long totalTimeTakenToConfirm;
  Long hoursTakenToConfirm;
  Long minutesTakenToConfirm;
  Long calculatedTimeTakenToConfirm;
  Long calculatedHoursTakenToConfirm;
  Long calculatedMinutesTakenToConfirm;
  Boolean highlightOrder;

  public Date getOrderPlacedDate() {
    return orderPlacedDate;
  }

  public void setOrderPlacedDate(Date orderPlacedDate) {
    this.orderPlacedDate = orderPlacedDate;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Date getOrderConfirmationDate() {
    return orderConfirmationDate;
  }

  public void setOrderConfirmationDate(Date orderConfirmationDate) {
    this.orderConfirmationDate = orderConfirmationDate;
  }

  public Long getTotalTimeTakenToConfirm() {
    return totalTimeTakenToConfirm;
  }

  public void setTotalTimeTakenToConfirm(Long totalTimeTakenToConfirm) {
    this.totalTimeTakenToConfirm = totalTimeTakenToConfirm;
  }

  public Long getHoursTakenToConfirm() {
    setHoursTakenToConfirm(totalTimeTakenToConfirm / 3600);
    if(hoursTakenToConfirm > 0){
      return hoursTakenToConfirm;
    } else {
      return (long) 0;
    }
  }

  public void setHoursTakenToConfirm(Long hoursTakenToConfirm) {
    this.hoursTakenToConfirm = hoursTakenToConfirm;
  }

  public Long getMinutesTakenToConfirm() {
    setMinutesTakenToConfirm((totalTimeTakenToConfirm % 3600) / 60);
    if(minutesTakenToConfirm > 0){
      return minutesTakenToConfirm;
    } else {
      return (long) 0;
    }
  }

  public void setMinutesTakenToConfirm(Long minutesTakenToConfirm) {
    this.minutesTakenToConfirm = minutesTakenToConfirm;
  }

  public Long getCalculatedHoursTakenToConfirm() {
    setCalculatedHoursTakenToConfirm(calculatedTimeTakenToConfirm / 3600);
    if (calculatedHoursTakenToConfirm > 0 ){
      return calculatedHoursTakenToConfirm;
    } else {
      return (long) 0;
    }
  }

  public void setCalculatedHoursTakenToConfirm(Long calculatedHoursTakenToConfirm) {
    this.calculatedHoursTakenToConfirm = calculatedHoursTakenToConfirm;
  }

  public Long getCalculatedMinutesTakenToConfirm() {
    setCalculatedMinutesTakenToConfirm((calculatedTimeTakenToConfirm % 3600) / 60);
    if (calculatedMinutesTakenToConfirm > 0) {
      return calculatedMinutesTakenToConfirm;
    } else {
      return (long)0;
    }
  }

  public void setCalculatedMinutesTakenToConfirm(Long calculatedMinutesTakenToConfirm) {
    this.calculatedMinutesTakenToConfirm = calculatedMinutesTakenToConfirm;
  }

  public Boolean isHighlightOrder() {
    return highlightOrder;
  }

  public void setHighlightOrder(Boolean highlightOrder) {
    this.highlightOrder = highlightOrder;
  }

  public Long getCalculatedTimeTakenToConfirm() {
    if (calculatedTimeTakenToConfirm > 0){
      return calculatedTimeTakenToConfirm;
    } else {
      return (long)0;
    }
  }

  public void setCalculatedTimeTakenToConfirm(Long calculatedTimeTakenToConfirm) {
    this.calculatedTimeTakenToConfirm = calculatedTimeTakenToConfirm;
  }
}
