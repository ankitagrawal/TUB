package com.hk.web.action.core.payment;

import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;

@Component
public class PaymentFailAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/payment/paymentFail.jsp");
  }

}
