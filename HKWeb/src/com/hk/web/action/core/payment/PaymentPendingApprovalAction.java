package com.hk.web.action.core.payment;

import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.dao.payment.PaymentDao;
import com.hk.domain.payment.Payment;

@Component
public class PaymentPendingApprovalAction extends BaseAction {

  @Validate(required = true, encrypted = true)
  private String gatewayOrderId;

  private Payment payment;

  
  PaymentDao paymentDao;

  public Resolution pre() {
    payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
    return new ForwardResolution("/pages/payment/paymentPendingApproval.jsp");
  }

  public String getGatewayOrderId() {
    return gatewayOrderId;
  }

  public void setGatewayOrderId(String gatewayOrderId) {
    this.gatewayOrderId = gatewayOrderId;
  }

  public Payment getPayment() {
    return payment;
  }


}
