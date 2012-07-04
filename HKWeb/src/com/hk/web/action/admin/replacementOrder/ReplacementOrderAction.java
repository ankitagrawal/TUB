package com.hk.web.action.admin.replacementOrder;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 4, 2012
 * Time: 3:09:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ReplacementOrderAction extends BaseAction {
  private Long shippingOrderId;
  private ShippingOrder shippingOrder;
  private Boolean isRto;
  private List<LineItem> lineItems = new ArrayList<LineItem>();

  @Autowired
  ShippingOrderService shippingOrderService;

  @DefaultHandler
  public Resolution pre(){
    return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
  }

  public Resolution searchShippingOrder() {
    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    shippingOrderSearchCriteria.setOrderId(shippingOrderId);
    shippingOrder = shippingOrderService.find(shippingOrderId);
    return new RedirectResolution(ReplacementOrderAction.class);
  }

  public Resolution createReplacementOrder(){
    return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
  }

  public Long getShippingOrderId() {
    return shippingOrderId;
  }

  public void setShippingOrderId(Long shippingOrderId) {
    this.shippingOrderId = shippingOrderId;
  }

  public ShippingOrder getShippingOrder() {
    return shippingOrder;
  }

  public void setShippingOrder(ShippingOrder shippingOrder) {
    this.shippingOrder = shippingOrder;
  }

  public Boolean getIsRto() {
    return isRto;
  }

  public void setIsRto(Boolean rto) {
    isRto = rto;
  }

  public List<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }
}
