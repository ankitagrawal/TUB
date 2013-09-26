package com.hk.web.action.admin.shippingOrder;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 9/26/13
 * Time: 9:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DropShipChangeAction extends BaseAction {
  ShippingOrder shippingOrder;
  private String gatewayOrderId;
  @Autowired
  ShippingOrderService shippingOrderService;

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/shippingOrder/dropShippingOrder.jsp");
  }

  public Resolution search() {
    shippingOrder = shippingOrderService.findByGatewayOrderId(gatewayOrderId);
    if (shippingOrder == null) {
      addRedirectAlertMessage(new SimpleMessage("Invalid Gateway Order id"));
      return new RedirectResolution("/pages/admin/shippingOrder/dropShippingOrder.jsp");
    }
    return new ForwardResolution("/pages/admin/shippingOrder/dropShippingOrder.jsp");
  }

  public Resolution saveStatus() {

    return new RedirectResolution(DropShipChangeAction.class);
  }

  public ShippingOrder getShippingOrder() {
    return shippingOrder;
  }

  public void setShippingOrder(ShippingOrder shippingOrder) {
    this.shippingOrder = shippingOrder;
  }
}
