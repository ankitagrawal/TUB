package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.loyaltypg.UserOrderKarmaProfile;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.dto.pricing.PricingDto;
import com.hk.impl.service.codbridge.OrderEventPublisher;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.ga.GAUtil;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentSuccessAction extends BaseAction {

  private static Logger logger = LoggerFactory.getLogger(PaymentSuccessAction.class);

  @Validate(required = true, encrypted = true)
  private String gatewayOrderId;

  private Payment payment;
  private Order order;
  private PricingDto pricingDto;
  private EnumPaymentMode paymentMode;
  private String purchaseDate;
  private String couponCode;
  private int couponAmount = 0;
  private double loyaltyPointsEarned = 0;

  @Autowired
  private PaymentDao paymentDao;
  @Autowired
  UserDao userDao;
  @Autowired
  private ShippingOrderService shippingOrderService;
  @Autowired
  ShipmentService shipmentService;
  @Autowired
  RewardPointService rewardPointService;
  @Value("#{hkEnvProps['" + Keys.Env.cashBackPercentage + "']}")
  private Double cashBackPercentage;
  @Autowired
  OrderService orderService;
  @Autowired
  OrderLoggingService orderLoggingService;
  @Autowired
  LoyaltyProgramService loyaltyProgramService;
  @Autowired
  OrderEventPublisher orderEventPublisher;

  @Autowired
  InventoryService inventoryService;


  public Resolution pre() {
    this.payment = this.paymentDao.findByGatewayOrderId(this.gatewayOrderId);
    if (this.payment != null && EnumPaymentStatus.getPaymentSuccessPageStatusIds().contains(this.payment.getPaymentStatus().getId())) {

      // todo Ankit ERP booking the inventory in case of payment
      /*inventoryManageService.tempBookSkuLineItemForOrder(this.payment.getOrder());*/

      Long paymentStatusId = this.payment.getPaymentStatus() != null ? this.payment.getPaymentStatus().getId() : null;

      logger.info("payment success page payment status " + paymentStatusId);

      this.order = this.payment.getOrder();
      this.pricingDto = new PricingDto(this.order.getCartLineItems(), this.payment.getOrder().getAddress());

      // for google analytics
      this.paymentMode = EnumPaymentMode.getPaymentModeFromId(this.payment.getPaymentMode().getId());
      this.purchaseDate = GAUtil.formatDate(this.order.getCreateDate());

      OfferInstance offerInstance = this.order.getOfferInstance();
      if (offerInstance != null) {
        Coupon coupon = offerInstance.getCoupon();
        if (coupon != null) {
          this.couponCode = coupon.getCode() + "@" + offerInstance.getId();
        }
        this.couponAmount = this.pricingDto.getTotalPromoDiscount().intValue();
      }
    }

    //Loyalty program
    UserOrderKarmaProfile karmaProfile = loyaltyProgramService.getUserOrderKarmaProfile(order.getId());
    if (karmaProfile != null) {
      loyaltyPointsEarned = karmaProfile.getKarmaPoints();
    }
    if(isHybridRelease()){
      return new ForwardResolution("/pages/payment/paymentSuccessBeta.jsp");
    }
    return new ForwardResolution("/pages/payment/paymentSuccess.jsp");
  }

  public String getGatewayOrderId() {
    return this.gatewayOrderId;
  }

  public void setGatewayOrderId(String gatewayOrderId) {
    this.gatewayOrderId = gatewayOrderId;
  }

  public Payment getPayment() {
    return this.payment;
  }

  public PricingDto getPricingDto() {
    return this.pricingDto;
  }

  public void setPaymentDao(PaymentDao paymentDao) {
    this.paymentDao = paymentDao;
  }

  public OrderService getOrderService() {
    return this.orderService;
  }

  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  public Order getOrder() {
    return this.order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public EnumPaymentMode getPaymentMode() {
    return this.paymentMode;
  }

  public void setPaymentMode(EnumPaymentMode paymentMode) {
    this.paymentMode = paymentMode;
  }

  public String getPurchaseDate() {
    return this.purchaseDate;
  }

  public void setPurchaseDate(String purchaseDate) {
    this.purchaseDate = purchaseDate;
  }

  public String getCouponCode() {
    return this.couponCode;
  }

  public int getCouponAmount() {
    return this.couponAmount;
  }

  /**
   * @return the loyaltyPointsEarned
   */
  public double getLoyaltyPointsEarned() {
    return this.loyaltyPointsEarned;
  }

  /**
   * @param loyaltyPointsEarned the loyaltyPointsEarned to set
   */
  public void setLoyaltyPointsEarned(double loyaltyPointsEarned) {
    this.loyaltyPointsEarned = loyaltyPointsEarned;
  }
}
