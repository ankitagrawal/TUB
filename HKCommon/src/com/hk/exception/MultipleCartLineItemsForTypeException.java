package com.hk.exception;

import com.hk.domain.order.CartLineItem;

/**
 * Created by IntelliJ IDEA.
 * User: AdminUser
 * Date: Mar 17, 2012
 * Time: 5:49:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultipleCartLineItemsForTypeException extends HealthkartRuntimeException {

  private CartLineItem cartLineItem;

  public MultipleCartLineItemsForTypeException(String message, CartLineItem lineItem) {
    super(message);
    this.cartLineItem = lineItem;
  }

  public MultipleCartLineItemsForTypeException(CartLineItem cartLineItem) {
    super("Error while using cartLine item in accoutning -> " + cartLineItem.getId());
    this.cartLineItem = cartLineItem;
  }

  public CartLineItem getCartLineItem() {
    return cartLineItem;
  }
}
