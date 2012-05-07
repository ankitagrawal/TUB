package com.hk.web.action.core.payment;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;

@Component
public class PaymentFailAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/payment/paymentFail.jsp");
  }

}
