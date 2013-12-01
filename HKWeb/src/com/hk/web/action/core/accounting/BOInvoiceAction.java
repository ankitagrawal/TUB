package com.hk.web.action.core.accounting;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.util.BarcodeGenerator;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.order.Order;
import com.hk.dto.pricing.PricingDto;
import com.hk.impl.dao.catalog.category.CategoryDaoImpl;
import com.hk.manager.OrderManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.user.UserDao;

@Component
public class BOInvoiceAction extends BaseAction {

  /*private static Logger logger = LoggerFactory.getLogger(BOInvoiceAction.class);*/
  private PricingDto pricingDto;

  @Validate (required = true, encrypted = true)
  private Order order;

  @Autowired
  CategoryDaoImpl categoryDao;
  @Autowired
  ReferrerProgramManager referrerProgramManager;
  @Autowired
  UserDao userDao;
  @Autowired
  OrderDao orderDao;
  @Autowired
  BarcodeGenerator barcodeGenerator;
  @Autowired
  OrderManager orderManager;
  @Autowired
  PaymentModeDao paymentModeDao;
  @Autowired
  AddressDao addressDao;
  @Autowired
  CourierDao courierDao;

  private String barcodePath;
  private Coupon coupon;

  @DefaultHandler
  public Resolution pre() {
    pricingDto = new PricingDto(order.getCartLineItems(), order.getAddress());
    coupon = referrerProgramManager.getOrCreateRefferrerCoupon(order.getUser());
    barcodePath = barcodeGenerator.getBarcodePath(order.getGatewayOrderId(),1.0f, 150, false);
      if (isHybridRelease()) {
          return new ForwardResolution("/pages/invoiceBeta.jsp");
      }
      else
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