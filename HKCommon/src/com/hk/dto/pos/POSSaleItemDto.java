package com.hk.dto.pos;

import com.hk.domain.order.Order;

/**
 * Created with IntelliJ IDEA.
 * User: Nitin Kumar
 * Date: 7/23/13
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class POSSaleItemDto {
  private Double discount;
  private Order order;

  public POSSaleItemDto(Double discount, Order order) {
    this.discount = discount;
    this.order = order;
  }

  public Double getDiscount() {
    return discount;
  }

  public void setDiscount(Double discount) {
    this.discount = discount;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }
}
