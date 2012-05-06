package com.hk.web.action.core.referral;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.order.Order;
import com.hk.dto.pricing.PricingDto;
import com.hk.pricing.PricingEngine;


/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: Sep 3, 2011
 * Time: 2:40:33 AM
 * To change this template use File | Settings | File Templates.
 */

@Secure
@Component
public class ReferredOrderDetailsAction extends BaseAction {
  Order order;

  PricingDto pricingDto;

  @Autowired
   PricingEngine pricingEngine;

  public Resolution pre() {
    pricingDto = new PricingDto(order.getCartLineItems(), order.getAddress());
    return new ForwardResolution("/pages/affiliate/referredOrderDetails.jsp");
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public PricingDto getPricingDto() {
    return pricingDto;
  }
}