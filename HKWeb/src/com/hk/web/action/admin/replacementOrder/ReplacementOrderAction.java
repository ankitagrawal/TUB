package com.hk.web.action.admin.replacementOrder;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
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
  private ArrayList<LineItem> lineItems = new ArrayList<LineItem>();

  @Autowired
  ShippingOrderService shippingOrderService;

  @Autowired
  ReplacementOrderService replacementOrderService;

  @DefaultHandler
  public Resolution pre(){
    return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
  }

  public Resolution searchShippingOrder() {
    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    shippingOrderSearchCriteria.setOrderId(shippingOrderId);
    shippingOrder = shippingOrderService.find(shippingOrderId);
    //return new RedirectResolution(ReplacementOrderAction.class);
    return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
  }

  public Resolution createReplacementOrder(){
    ReplacementOrder replacementOrder = replacementOrderService.createReplaceMentOrder(shippingOrder, lineItems, isRto);
    return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
  }

  public Long getShippingOrderId() {
    return shippingOrderId;
  }

  public void setShippingOrderId(Long shippingOrderId) {
    this.shippingOrderId = shippingOrderId;
  }

  public ShippingOrder getShippingOrder() {
    return this.shippingOrder;
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

  public ArrayList<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(ArrayList<LineItem> lineItems) {
    this.lineItems = lineItems;
  }
}
