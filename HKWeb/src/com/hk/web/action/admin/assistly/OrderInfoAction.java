package com.hk.web.action.admin.assistly;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.order.Order;
import com.hk.dto.pricing.PricingDto;

@Component
public class OrderInfoAction extends BaseAction {

  String orderId;

  Order order;
  PricingDto pricingDto;

  public Resolution pre() {

    /*if (StringUtils.isBlank(orderId)) {
      return new ForwardResolution("/pages/admin/assistly/orderInfo_noOrderId.jsp");
    }

    // now check if orderId is numeric
    Long orderIdLong = null;
    try {
      orderIdLong = Long.parseLong(orderId);
    } catch (NumberFormatException e) {
    }

    // try fetching by order id first
    if (orderIdLong != null) {
      order = orderDao.find(orderIdLong);
    }

    // if no order found, try fetching by gateway order id
    if (order == null) {
      order = orderDao.findByGatewayOrderId(orderId);
    }

    if (order == null) {
      return new ForwardResolution("/pages/admin/assistly/orderInfo_notFound.jsp");
    }

    pricingDto = new PricingDto(order.getLineItems(), order.getAddress());*/

    //TODO: # warehouse fix this.
    
    return new ForwardResolution("/pages/admin/assistly/orderInfo.jsp");
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public Order getOrder() {
    return order;
  }

  public PricingDto getPricingDto() {
    return pricingDto;
  }
}
