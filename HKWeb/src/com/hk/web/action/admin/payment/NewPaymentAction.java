package com.hk.web.action.admin.payment;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PAYMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class NewPaymentAction extends BaseAction {

  @Validate(required = true)
  private Order order;

  @Validate(required = true, on = "update")
  private Double amount;

  @Validate(required = true, on = "update")
  private PaymentMode paymentMode;

  @Validate(required = true, on = "update")
  private PaymentStatus paymentStatus;

  @Autowired
  PaymentDao paymentDao;

  @DefaultHandler
  public Resolution pre() {

    return new ForwardResolution("/pages/admin/newPayment.jsp");
  }

  public Resolution update() {

    Payment payment = new Payment();
    payment.setAmount(amount);
    payment.setPaymentMode(paymentMode);
    payment.setPaymentStatus(paymentStatus);
    payment.setOrder(order);
    payment.setGatewayOrderId(PaymentManager.getUniqueGatewayOrderId(order));
    payment.setPaymentChecksum(PaymentManager.getOrderChecksum(order));

    paymentDao.save(payment);

    addRedirectAlertMessage(new LocalizableMessage("/admin/NewPayment.action.success"));
    return new RedirectResolution(CheckPaymentAction.class).addParameter("order", order.getId());
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public PaymentMode getPaymentMode() {
    return paymentMode;
  }

  public void setPaymentMode(PaymentMode paymentMode) {
    this.paymentMode = paymentMode;
  }

  public PaymentStatus getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(PaymentStatus paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

}
