package com.hk.exception;

import com.hk.domain.order.Order;

public class OrderSplitException extends HealthkartRuntimeException {

  Order order;

  public OrderSplitException(String message, Order order) {
    super(message);
    this.order = order;
  }

  public OrderSplitException(Order order) {
    super("The placed order(" + order.getId() + ") cannot be split.");
    this.order = order;
  }

  public Order getOrder() {
    return order;
  }
}