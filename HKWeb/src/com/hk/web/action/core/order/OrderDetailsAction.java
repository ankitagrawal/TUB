package com.hk.web.action.core.order;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
public class OrderDetailsAction extends BaseAction {

  @SuppressWarnings("unused")
  private static Logger logger = LoggerFactory.getLogger(OrderDetailsAction.class);
  @Validate(encrypted = true)
  Order order;

  Set<CartLineItem> cartLineItems = new HashSet<CartLineItem>();

  List<LineItem> lineItems = new ArrayList<LineItem>();

  public Resolution pre() {
    Set<ShippingOrder> shippingOrders = order.getShippingOrders();

    if (shippingOrders != null && shippingOrders.size() > 0) {
      for (ShippingOrder shippingOrder : shippingOrders) {
        lineItems.addAll(shippingOrder.getLineItems());
      }
    } else {
      cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
    }
    if (isHybridRelease()) {
        return new ForwardResolution("/pages/orderDetailsBeta.jsp");
    }
    else
        return new ForwardResolution("/pages/orderDetails.jsp");
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Set<CartLineItem> getCartLineItems() {
    return cartLineItems;
  }

  public void setCartLineItems(Set<CartLineItem> cartLineItems) {
    this.cartLineItems = cartLineItems;
  }

  public List<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }
}