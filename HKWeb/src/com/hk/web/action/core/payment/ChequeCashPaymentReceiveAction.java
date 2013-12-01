package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.payment.PaymentService;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

/**
 * User: kani Time: 12 Feb, 2010 3:59:02 PM
 */
@Secure
@Component
public class ChequeCashPaymentReceiveAction extends BaseAction {
  @Autowired
  private PaymentManager paymentManager;
  @Autowired
  private OrderManager orderManager;
  @Autowired
  private RoleService roleService;
  @Autowired
  private UserService userService;
  @Autowired
  private PaymentService paymentService;


  @Validate(required = true)
  private Order order;

  @Validate(required = true)
  private String bankName;

  @Validate(required = true)
  private String bankBranch;

  @Validate(required = true)
  private String bankCity;

  @Validate(required = true)
  private Long paymentMode;

  private String chequeNumber;

  /*private User user;*/

  @ValidationMethod
  public void validate() {
    if (paymentMode.equals(EnumPaymentMode.NEFT.getId()) || paymentMode.equals(EnumPaymentMode.CashDeposit.getId())
        || paymentMode.equals(EnumPaymentMode.ChequeDeposit.getId())) {
      // validated
    } else {
      // if somebody tries to hack and enter using a invalid payment gateway
      addValidationError("paymentMode", new SimpleError("Invalid payement mode is selected."));
    }

    if (paymentMode.equals(EnumPaymentMode.ChequeDeposit.getId())) {
      if (chequeNumber != null) {
        if (chequeNumber.equals("")) {
          addValidationError("chequeNumber", new SimpleError("Please mention cheque number."));
        } else if (chequeNumber.length() < 6) {
          addValidationError("chequeNumber", new SimpleError("ChequeDeposit number should be atleast 6 digits long."));
        }
      } else {
        addValidationError("chequeNumber", new SimpleError("Please mention cheque number."));
      }
    }
  }

  public Resolution pre() {
    Resolution resolution = null;
    if (order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {
      // recalculate the pricing before creating a payment.
      order = getOrderManager().recalAndUpdateAmount(order);
      // first create a payment row, this will also cotain the payment checksum
      Payment payment = getPaymentManager().createNewPayment(order, getPaymentService().findPaymentMode(paymentMode), BaseUtils.getRemoteIpAddrForUser(getContext()), null, null, null);
      String gatewayOrderId = payment.getGatewayOrderId();
      try {
        getPaymentManager().verifyPayment(gatewayOrderId, order.getAmount(), null);
        getPaymentManager().chequeCashSuccess(gatewayOrderId, bankName, bankBranch, bankCity, getPaymentService().findPaymentMode(paymentMode), chequeNumber);
        resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
      } catch (HealthkartPaymentGatewayException e) {
        getPaymentManager().error(gatewayOrderId, e);
        resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
      }
    } else {
      addRedirectAlertMessage(new SimpleMessage("Payment for the order is already made."));
      resolution = new RedirectResolution(PaymentModeAction.class).addParameter("order", order);
    }
    return resolution;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBankBranch() {
    return bankBranch;
  }

  public void setBankBranch(String bankBranch) {
    this.bankBranch = bankBranch;
  }

  public String getBankCity() {
    return bankCity;
  }

  public void setBankCity(String bankCity) {
    this.bankCity = bankCity;
  }

  public Long getPaymentMode() {
    return paymentMode;
  }

  public void setPaymentMode(Long paymentMode) {
    this.paymentMode = paymentMode;
  }

  public String getChequeNumber() {
    return chequeNumber;
  }

  public void setChequeNumber(String chequeNumber) {
    this.chequeNumber = chequeNumber;
  }

  public PaymentManager getPaymentManager() {
    return paymentManager;
  }

  public void setPaymentManager(PaymentManager paymentManager) {
    this.paymentManager = paymentManager;
  }

  public OrderManager getOrderManager() {
    return orderManager;
  }

  public void setOrderManager(OrderManager orderManager) {
    this.orderManager = orderManager;
  }

  public RoleService getRoleService() {
    return roleService;
  }

  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public PaymentService getPaymentService() {
    return paymentService;
  }

  public void setPaymentService(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

}