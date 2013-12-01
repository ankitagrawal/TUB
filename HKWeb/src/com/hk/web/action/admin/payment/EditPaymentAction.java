package com.hk.web.action.admin.payment;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.payment.Payment;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PAYMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class EditPaymentAction extends BaseAction {

  @Validate(required = true, on = "pre")
  private Long paymentId;

  @ValidateNestedProperties({
      @Validate(field = "amount", required = true, on = "update"),
      @Validate(field = "paymentStatus", required = true, on = "update"),
      @Validate(field = "paymentDate")
  })
  private Payment payment;

  private Payment existingPayment;
  @Autowired
   PaymentDao paymentDao;

  @ValidationMethod(on = "pre")
  public void validatePayment() {
    payment = getBaseDao().get(Payment.class,paymentId);
    if (payment == null) {
      getContext().getValidationErrors().add("notFound", new LocalizableError("/admin/EditPayment.action.payment.not.found"));
    }
  }

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/editPayment.jsp");
  }

  @ValidationMethod(on = "update")
  public void validateEditedPayment() {
    existingPayment = getBaseDao().get(Payment.class, payment.getId());
    if (existingPayment == null) {
      getContext().getValidationErrors().add("notFound", new LocalizableError("/admin/EditPayment.action.payment.not.found"));
    }
  }

  public Resolution update() {
    existingPayment.setAmount(payment.getAmount());
//    existingPayment.setPaymentStatus(payment.getPaymentStatus());
    existingPayment.setPaymentDate(payment.getPaymentDate());
    paymentDao.save(existingPayment);

    addRedirectAlertMessage(new LocalizableMessage("/admin/EditPayment.action.payment.updated"));
    return new RedirectResolution(CheckPaymentAction.class).addParameter("order", existingPayment.getOrder().getId());
  }

  public void setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

}
