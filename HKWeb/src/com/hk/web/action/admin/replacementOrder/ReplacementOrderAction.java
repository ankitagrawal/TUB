package com.hk.web.action.admin.replacementOrder;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
import com.hk.helper.ReplacementOrderHelper;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import net.sourceforge.stripes.action.*;

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

  @Autowired
  ReplacementOrderService replacementOrderService;

  @Autowired
  LineItemDao lineItemDao;

  @DefaultHandler
  public Resolution pre(){
    return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
  }

  public Resolution searchShippingOrder() {
    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    shippingOrderSearchCriteria.setOrderId(shippingOrderId);
    shippingOrder = shippingOrderService.find(shippingOrderId);
    for (LineItem lineItem : shippingOrder.getLineItems()){
        lineItems.add(ReplacementOrderHelper.getLineItemForReplacementOrder(lineItem));
    }
    return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
  }

  public Resolution createReplacementOrder(){
    for(LineItem lineItem : lineItems){
      if(lineItem.getQty() > getLineItemDao().getLineItem(lineItem.getSku(), shippingOrder).getQty()){
        addRedirectAlertMessage(new SimpleMessage("The quantity of "+lineItem.getCartLineItem().getProductVariant().getProduct().getName()+" cannot be more than original quantity."));
        return new ForwardResolution("/pages/admin/createReplacementOrder.jsp");
      }
    }
    addRedirectAlertMessage(new SimpleMessage("The Replacement order created"));  
    replacementOrderService.createReplaceMentOrder(shippingOrder, lineItems, isRto);
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

  public List<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

  public LineItemDao getLineItemDao() {
    return lineItemDao;
  }

  public void setLineItemDao(LineItemDao lineItemDao) {
    this.lineItemDao = lineItemDao;
  }
}
