package com.hk.report.dto.order;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: Sep 23, 2011
 * Time: 4:37:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrderLifecycleStateTransitionDto {

  private Date activityDate;
  private Set<Long> placedOrders = new HashSet<Long>();
  private Set<Long> tpslOrders = new HashSet<Long>();
  private Set<Long> codOrders = new HashSet<Long>();
  private Set<Long> confirmedCODOrders = new HashSet<Long>();
  private Set<Long> escalableOrders = new HashSet<Long>();
  private Set<Long> ordersEscalatedToPackingQeueue = new HashSet<Long>();
  private Set<Long> ordersPushedBackToActionQueue = new HashSet<Long>();
  private Set<Long> ordersEscalatedToShipmentQueue = new HashSet<Long>();
  private Set<Long> shippedOrders = new HashSet<Long>();

  public Date getActivityDate() {
    return activityDate;
  }

  public void setActivityDate(Date activityDate) {
    this.activityDate = activityDate;
  }

  public Set<Long> getPlacedOrders() {
    return placedOrders;
  }

  public void setPlacedOrders(Set<Long> placedOrders) {
    this.placedOrders = placedOrders;
  }

  public Set<Long> getTpslOrders() {
    return tpslOrders;
  }

  public void setTpslOrders(Set<Long> tpslOrders) {
    this.tpslOrders = tpslOrders;
  }

  public Set<Long> getCodOrders() {
    return codOrders;
  }

  public void setCodOrders(Set<Long> codOrders) {
    this.codOrders = codOrders;
  }

  public Set<Long> getConfirmedCODOrders() {
    return confirmedCODOrders;
  }

  public void setConfirmedCODOrders(Set<Long> confirmedCODOrders) {
    this.confirmedCODOrders = confirmedCODOrders;
  }

  public Set<Long> getEscalableOrders() {
    return escalableOrders;
  }

  public void setEscalableOrders(Set<Long> escalableOrders) {
    this.escalableOrders = escalableOrders;
  }

  public Set<Long> getOrdersEscalatedToPackingQeueue() {
    return ordersEscalatedToPackingQeueue;
  }

  public void setOrdersEscalatedToPackingQeueue(Set<Long> ordersEscalatedToPackingQeueue) {
    this.ordersEscalatedToPackingQeueue = ordersEscalatedToPackingQeueue;
  }

  public Set<Long> getOrdersPushedBackToActionQueue() {
    return ordersPushedBackToActionQueue;
  }

  public void setOrdersPushedBackToActionQueue(Set<Long> ordersPushedBackToActionQueue) {
    this.ordersPushedBackToActionQueue = ordersPushedBackToActionQueue;
  }

  public Set<Long> getOrdersEscalatedToShipmentQueue() {
    return ordersEscalatedToShipmentQueue;
  }

  public void setOrdersEscalatedToShipmentQueue(Set<Long> ordersEscalatedToShipmentQueue) {
    this.ordersEscalatedToShipmentQueue = ordersEscalatedToShipmentQueue;
  }

  public Set<Long> getShippedOrders() {
    return shippedOrders;
  }

  public void setShippedOrders(Set<Long> shippedOrders) {
    this.shippedOrders = shippedOrders;
  }
}
