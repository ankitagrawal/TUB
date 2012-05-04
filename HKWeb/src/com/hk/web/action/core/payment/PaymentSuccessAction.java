package com.hk.web.action.core.payment;

import java.util.Set;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.dao.payment.PaymentDao;
import com.hk.dao.user.UserDao;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.payment.Payment;
import com.hk.dto.pricing.PricingDto;
import com.hk.filter.CartLineItemFilter;
import com.hk.service.InventoryService;

@Component
public class PaymentSuccessAction extends BaseAction {


  @Validate (required = true, encrypted = true)
  private String gatewayOrderId;

  private Payment payment;
  private PricingDto pricingDto;

  @Autowired
  private PaymentDao paymentDao;
  @Autowired
  UserDao userDao;
  @Autowired
  InventoryService inventoryService;

  public Resolution pre() {
    payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
    if (payment != null) {
      pricingDto = new PricingDto(payment.getOrder().getCartLineItems(), payment.getOrder().getAddress());

      //Check Inventory health of order lineitems

      Set<CartLineItem> productCartLineItems = new CartLineItemFilter(payment.getOrder().getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();

      for (CartLineItem cartLineItem : productCartLineItems) {
        inventoryService.checkInventoryHealth(cartLineItem.getProductVariant());
      }
    }
    return new ForwardResolution("/pages/payment/paymentSuccess.jsp");
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

  public PricingDto getPricingDto() {
    return pricingDto;
  }

  
  public void setPaymentDao(PaymentDao paymentDao) {
    this.paymentDao = paymentDao;
  }
}
