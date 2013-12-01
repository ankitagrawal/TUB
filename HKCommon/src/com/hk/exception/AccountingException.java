package com.hk.exception;

import com.hk.domain.shippingOrder.LineItem;

/**
 * Created by IntelliJ IDEA.
 * User: Rahul
 * Date: Mar 15, 2012
 * Time: 8:19:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountingException extends HealthkartRuntimeException {

  private LineItem lineItem;

  public AccountingException(String message, LineItem lineItem) {
    super(message);
    this.lineItem = lineItem;
  }

  public AccountingException(LineItem lineItem) {
    super("Cannot calculate discount for lineItemId -> " + lineItem.getId());
    this.lineItem = lineItem;
  }

  public LineItem getlineItem() {
    return lineItem;
  }
}