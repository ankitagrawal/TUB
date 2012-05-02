package web.action.core.order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.filter.CartLineItemFilter;


@Secure
@Component
public class OrderDetailsAction extends BaseAction {

  private static Logger logger = LoggerFactory.getLogger(OrderDetailsAction.class);
  @Validate(encrypted = true)
  Order order;

  Set<CartLineItem> cartLineItems = new HashSet<CartLineItem>();

  List<LineItem> lineItems = new ArrayList<LineItem>();

  public Resolution pre() {


    if (order.getShippingOrders() != null) {
      for (ShippingOrder shippingOrder : order.getShippingOrders()) {
        lineItems.addAll(shippingOrder.getLineItems());
      }
    } else {
      cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
    }
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