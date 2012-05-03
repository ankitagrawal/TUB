package com.hk.web.action.core.accounting;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.dao.courier.CourierDao;
import com.hk.admin.impl.dao.courier.CourierServiceInfoDao;
import com.hk.dao.catalog.category.CategoryDao;
import com.hk.dao.core.AddressDao;
import com.hk.dao.order.OrderDao;
import com.hk.dao.payment.PaymentModeDao;
import com.hk.dao.user.UserDaoImpl;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.order.Order;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.util.BarcodeGenerator;

@Component
public class BOInvoiceAction extends BaseAction {

  private static Logger logger = LoggerFactory.getLogger(BOInvoiceAction.class);

  private PricingDto pricingDto;

  @Validate (required = true, encrypted = true)
  private Order order;

  
  CategoryDao categoryDao;
  
  ReferrerProgramManager referrerProgramManager;
  
  UserDaoImpl userDao;
  
  OrderDao orderDao;
  
  BarcodeGenerator barcodeGenerator;
  
  OrderManager orderManager;
  
  PaymentModeDao paymentModeDao;
  
  CourierServiceInfoDao courierServiceInfoDao;
  
  AddressDao addressDao;
  
  CourierDao courierDao;

  private String barcodePath;
  private Coupon coupon;

  @DefaultHandler
  public Resolution pre() {
    pricingDto = new PricingDto(order.getCartLineItems(), order.getAddress());
    coupon = referrerProgramManager.getOrCreateRefferrerCoupon(order.getUser());
    barcodePath = barcodeGenerator.getBarcodePath(order.getGatewayOrderId());
    return new ForwardResolution("/pages/invoice.jsp");
  }

  public PricingDto getPricingDto() {
    return pricingDto;
  }


  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Coupon getCoupon() {
    return coupon;
  }

  public String getBarcodePath() {
    return barcodePath;
  }

  public void setBarcodePath(String barcodePath) {
    this.barcodePath = barcodePath;
  }
}