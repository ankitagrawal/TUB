package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.Keys;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.dto.pricing.PricingDto;
import com.hk.impl.service.codbridge.OrderEventPublisher;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.pact.dao.user.UserDao;
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
    OrderEventPublisher orderEventPublisher;

    public Resolution pre() {
        payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        if (payment != null && EnumPaymentStatus.getPaymentSuccessPageStatusIds().contains(payment.getPaymentStatus().getId())) {

            Long paymentStatusId = payment.getPaymentStatus() != null ? payment.getPaymentStatus().getId() : null;

            logger.info("payment success page payment status " + paymentStatusId);

            order = payment.getOrder();
            pricingDto = new PricingDto(order.getCartLineItems(), payment.getOrder().getAddress());

            // for google analytics
            paymentMode = EnumPaymentMode.getPaymentModeFromId(payment.getPaymentMode().getId());
            purchaseDate = GAUtil.formatDate(order.getCreateDate());

            OfferInstance offerInstance = order.getOfferInstance();
            if (offerInstance != null) {
                Coupon coupon = offerInstance.getCoupon();
                if (coupon != null) {
                    couponCode = coupon.getCode() + "@" + offerInstance.getId();
                }
                couponAmount = pricingDto.getTotalPromoDiscount().intValue();
            }
            //moved to orderManager, orderPaymentReceived
//            orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(order);
            orderEventPublisher.publishOrderPlacedEvent(order);

            //todo disabling, cod conversion and repay prepaid order as for now, need to do qa if the functionality still works or not
/*
            RewardPointMode prepayOfferRewardPoint = rewardPointService.getRewardPointMode(EnumRewardPointMode.Prepay_Offer);
            RewardPoint prepayRewardPoints;
            EnumRewardPointStatus rewardPointStatus;

            HttpServletRequest httpRequest = WebContext.getRequest();
            HttpServletResponse httpResponse = WebContext.getResponse();
            if (httpRequest.getCookies() != null) {
                for (Cookie cookie : httpRequest.getCookies()) {
                    if (cookie.getName() != null && cookie.getName().equals(HealthkartConstants.Cookie.codConverterID)) {
                        if (cookie.getValue() != null && CryptoUtil.decrypt(cookie.getValue()).equalsIgnoreCase(order.getId().toString())) {
                            if (rewardPointService.findByReferredOrderAndRewardMode(order, prepayOfferRewardPoint) == null) {
                                if (EnumPaymentMode.getPrePaidPaymentModes().contains(order.getPayment().getPaymentMode().getId())) {
                                    rewardPointStatus = EnumRewardPointStatus.APPROVED;
                                } else {
                                    rewardPointStatus = EnumRewardPointStatus.PENDING;
                                }
                                DecimalFormat df = new DecimalFormat("#.##");
                                Double cashBack = Double.valueOf(df.format(order.getPayment().getAmount() * cashBackPercentage));
                                prepayRewardPoints = rewardPointService.addRewardPoints(order.getUser(), null, order, cashBack, "Prepay Offer", rewardPointStatus,
                                        prepayOfferRewardPoint);
                                rewardPointService.approveRewardPoints(Arrays.asList(prepayRewardPoints), new DateTime().plusMonths(3).toDate());
                            }
                            Set<CartLineItem> codCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.CodCharges).filter();
                            CartLineItem codCartLineItem = codCartLineItems != null && !codCartLineItems.isEmpty() ? codCartLineItems.iterator().next() : null;
                            if (codCartLineItems != null && !codCartLineItems.isEmpty() && codCartLineItem != null) {
                                Double applicableCodCharges = 0D;
                                applicableCodCharges = codCartLineItem.getHkPrice() - codCartLineItem.getDiscountOnHkPrice();
                                order.setAmount(order.getAmount() - (applicableCodCharges));
                                Set<ShippingOrder> shippingOrders = order.getShippingOrders();
                                if (shippingOrders != null) {
                                    for (ShippingOrder shippingOrder : shippingOrders) {
                                        shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.COD_Converter);
                                        shippingOrderService.nullifyCodCharges(shippingOrder);
                                        shipmentService.recreateShipment(shippingOrder);
                                        shippingOrderService.autoEscalateShippingOrder(shippingOrder);                          
                                    }
                                }

                                Set<CartLineItem> cartLineItems = order.getCartLineItems();
                                cartLineItems.removeAll(codCartLineItems);
                                getBaseDao().deleteAll(codCartLineItems);
                                order.getCartLineItems().addAll(cartLineItems);
                                order = getOrderService().save(order);
                                orderLoggingService.logOrderActivity(order, EnumOrderLifecycleActivity.Cod_Conversion);
                            }
                            break;
                        }
                    }
                }
            }
            // Resetting value and expiring cookie
            Cookie wantedCODCookie = new Cookie(HealthkartConstants.Cookie.codConverterID, "false");
            wantedCODCookie.setPath("/");
            wantedCODCookie.setMaxAge(0);
            httpResponse.addCookie(wantedCODCookie);
*/
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

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public EnumPaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(EnumPaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public int getCouponAmount() {
        return couponAmount;
    }
}
