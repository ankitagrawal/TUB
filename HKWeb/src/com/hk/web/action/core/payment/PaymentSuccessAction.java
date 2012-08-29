package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.Keys;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.ga.GAUtil;
import com.hk.web.filter.WebContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.validation.Validate;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
	InventoryService inventoryService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ShippingOrderService shippingOrderService;
	@Autowired
	private OrderLoggingService orderLoggingService;
	@Autowired
	private OrderStatusService orderStatusService;
	@Autowired
	private LineItemDao lineItemDao;
	@Autowired
	ShipmentService shipmentService;
	@Autowired
	RewardPointService rewardPointService;
	@Autowired
	ReferrerProgramManager referrerProgramManager;
	@Value("#{hkEnvProps['" + Keys.Env.cashBackPercentage + "']}")
	private Double cashBackPercentage;


	public Resolution pre() {
		payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
		if (payment != null && EnumPaymentStatus.getPaymentSuccessPageStatusIds().contains(payment.getPaymentStatus().getId())) {

			Long paymentStatusId = payment.getPaymentStatus() != null ? payment.getPaymentStatus().getId() : null;

			logger.info("payment success page payment status " + paymentStatusId);

			order = payment.getOrder();
			pricingDto = new PricingDto(order.getCartLineItems(), payment.getOrder().getAddress());

			//for google analytics
			paymentMode = EnumPaymentMode.getPaymentModeFromId(payment.getPaymentMode().getId());
			purchaseDate = GAUtil.formatDate(order.getCreateDate());

			OfferInstance offerInstance = order.getOfferInstance();
			if (offerInstance != null) {
				Coupon coupon = offerInstance.getCoupon();
				couponCode = coupon.getCode() + "@" + offerInstance.getId();
				couponAmount = pricingDto.getTotalPromoDiscount().intValue();
			}

			Set<CartLineItem> productCartLineItems = new CartLineItemFilter(payment.getOrder().getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();

			boolean shippingOrderExists = false;

			//Check Inventory health of order lineitems
			for (CartLineItem cartLineItem : productCartLineItems) {
				if (lineItemDao.getLineItem(cartLineItem) != null) {
					shippingOrderExists = true;
				}
			}

			Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();

			if (!shippingOrderExists) {
				shippingOrders = getOrderService().createShippingOrders(order);
			}

			if (shippingOrders != null && shippingOrders.size() > 0) {
				// save order with InProcess status since shipping orders have been created
				order.setOrderStatus(getOrderStatusService().find(EnumOrderStatus.InProcess));
				order.setShippingOrders(shippingOrders);
				order = getOrderService().save(order);

				/**
				 * Order lifecycle activity logging - Order split to shipping orders
				 */
				getOrderLoggingService().logOrderActivity(order, getUserService().getAdminUser(), getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderSplit), null);

				// auto escalate shipping orders if possible
				if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(order.getPayment().getPaymentStatus().getId())) {
					for (ShippingOrder shippingOrder : shippingOrders) {
						getShippingOrderService().autoEscalateShippingOrder(shippingOrder);
					}
				}

				for (ShippingOrder shippingOrder : shippingOrders) {
					shipmentService.createShipment(shippingOrder);
				}

			}
			//Check Inventory health of order lineitems
			for (CartLineItem cartLineItem : productCartLineItems) {
				inventoryService.checkInventoryHealth(cartLineItem.getProductVariant());
			}

			RewardPointMode prepayOfferRewardPoint = rewardPointService.getRewardPointMode(EnumRewardPointMode.Prepay_Offer);
			RewardPoint prepayRewardPoints;
			EnumRewardPointStatus rewardPointStatus;

			HttpServletRequest httpRequest = WebContext.getRequest();
			HttpServletResponse httpResponse = WebContext.getResponse();
			if (httpRequest.getCookies() != null) {
				for (Cookie cookie : httpRequest.getCookies()) {
					if (cookie.getName() != null && cookie.getName().equals(HealthkartConstants.Cookie.codConverterID)) {
						if (cookie.getValue() != null && CryptoUtil.decrypt(cookie.getValue()).equalsIgnoreCase(order.getId().toString())) {
//							if (cookie.getValue().equals("true")) {
							if (rewardPointService.findByReferredOrderAndRewardMode(order, prepayOfferRewardPoint) == null) {
								if (EnumPaymentMode.getPrePaidPaymentModes().contains(order.getPayment().getPaymentMode().getId())) {
									rewardPointStatus = EnumRewardPointStatus.APPROVED;
								} else {
									rewardPointStatus = EnumRewardPointStatus.PENDING;
								}
								DecimalFormat df = new DecimalFormat("#.##");
								Double cashBack = Double.valueOf(df.format(order.getPayment().getAmount() * cashBackPercentage));
								prepayRewardPoints = rewardPointService.addRewardPoints(order.getUser(), null, order, cashBack, "Prepay Offer", rewardPointStatus, prepayOfferRewardPoint);
								referrerProgramManager.approveRewardPoints(Arrays.asList(prepayRewardPoints), new DateTime().plusMonths(3).toDate());
							}
							Set<CartLineItem> codCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.CodCharges).filter();
							CartLineItem codCartLineItem = codCartLineItems != null && !codCartLineItems.isEmpty() ? codCartLineItems.iterator().next() : null;
							if (codCartLineItems != null && !codCartLineItems.isEmpty()) {
								Double applicableCodCharges = 0D;
								applicableCodCharges = codCartLineItem.getHkPrice() - codCartLineItem.getDiscountOnHkPrice();
								order.setAmount(order.getAmount() - (applicableCodCharges));
								if (shippingOrders != null) {
									for (ShippingOrder shippingOrder : shippingOrders) {
										shippingOrderService.nullifyCodCharges(shippingOrder);
									}
								}
								Set<CartLineItem> cartLineItems = order.getCartLineItems();
								cartLineItems.removeAll(codCartLineItems);
								getBaseDao().deleteAll(codCartLineItems);
								order.getCartLineItems().addAll(cartLineItems);
								order = getOrderService().save(order);
							}
							break;
						}
					}
				}
			}
			//Resetting value and expiring cookie
			Cookie wantedCODCookie = new Cookie(HealthkartConstants.Cookie.codConverterID, "false");
			wantedCODCookie.setPath("/");
			wantedCODCookie.setMaxAge(0);
			httpResponse.addCookie(wantedCODCookie);
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

	public ShippingOrderService getShippingOrderService() {
		return shippingOrderService;
	}

	public void setShippingOrderService(ShippingOrderService shippingOrderService) {
		this.shippingOrderService = shippingOrderService;
	}

	public OrderLoggingService getOrderLoggingService() {
		return orderLoggingService;
	}

	public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
		this.orderLoggingService = orderLoggingService;
	}

	public OrderStatusService getOrderStatusService() {
		return orderStatusService;
	}

	public void setOrderStatusService(OrderStatusService orderStatusService) {
		this.orderStatusService = orderStatusService;
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
