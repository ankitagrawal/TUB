package com.hk.rest.mobile.service.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.util.BaseUtils;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.OrderManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.util.ga.GAUtil;
import com.hk.web.HealthkartResponse;

@Secure
@Path("/mPayment")
@Component
public class MCodPaymentReceiveAction extends MBaseAction {

    private Payment payment;
    private PricingDto pricingDto;
    private EnumPaymentMode paymentMode;
    private String purchaseDate;
    private String couponCode;
    private int couponAmount = 0;

    @Autowired
    private OrderService orderService;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    ShipmentService shipmentService;
    @Autowired
    RewardPointService rewardPointService;
    @Autowired
    ReferrerProgramManager referrerProgramManager;
    @Value("#{hkEnvProps['" + Keys.Env.cashBackPercentage + "']}")
    private Double cashBackPercentage;
    @Autowired
    AdminOrderService adminOrderService;
    @Autowired
    OrderLoggingService orderLoggingService;

	@Autowired
	private CourierService courierService;
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private PaymentManager paymentManager;

	@Value("#{hkEnvProps['" + Keys.Env.codMinAmount + "']}")
	private Double codMinAmount;

	@Value("#{hkEnvProps['" + Keys.Env.codMaxAmount + "']}")
	private Double codMaxAmount;

	@Validate(required = true)
	private Order order;

	@Validate(required = true)
	private String codContactName;

	@Validate(required = true)
	private String codContactPhone;

	private User user;

    @POST
    @Path("/payment/")
    @Produces("application/json")
	public String receivePayment(@FormParam("codContactName")String codContactName,
                                 @FormParam("codContactPhone")String codContactPhone,
                                 @FormParam("comment")String comments,
                                 @Context HttpServletResponse response,
                                 @Context HttpServletRequest request) {
		//Resolution resolution = null;
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;
        Map<String,Object> payMap = new HashMap<String,Object>();
        user = getUserService().getUserById(getPrincipal().getId());
        order = orderManager.getOrCreateOrder(user);

		if (order != null && order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {

			if (StringUtils.isBlank(codContactName)) {
                message = MHKConstants.COD_CONTCT_NAME_BLANK;
                status = MHKConstants.STATUS_ERROR;
                return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, status));

            } else if (StringUtils.isBlank(codContactPhone)) {
                message = MHKConstants.COD_CONTACT_PHN_BLANK;
                status = MHKConstants.STATUS_ERROR;
                return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, status));
			} else if (codContactName.length() > 80) {
                message = MHKConstants.COD_CONTACT_NAME_LENGTH;
                status = MHKConstants.STATUS_ERROR;
                return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, status));
			} else if (codContactPhone.length() > 25) {
                message = MHKConstants.COD_CONTACT_PHN_LENGTH;
                status = MHKConstants.STATUS_ERROR;
                return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, status));
            }

			Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
			if (subscriptionCartLineItems != null && subscriptionCartLineItems.size() > 0) {
                message = MHKConstants.COD_CONTACT_SUBSCR_CART;
                status = MHKConstants.STATUS_ERROR;
			}
		   if(comments!=null){
		        	order.setUserComments(comments);
		        	order = (Order) getBaseDao().save(order);
		   }
			// recalculate the pricing before creating a payment.
			order = orderManager.recalAndUpdateAmount(order);
			// first create a payment row, this will also cotain the payment checksum
			Payment payment = getPaymentManager().createNewPayment(order, getPaymentService().findPaymentMode(EnumPaymentMode.COD), BaseUtils.getRemoteIpAddrForUser(getContext()),
					null);

			String gatewayOrderId = payment.getGatewayOrderId();

			Address address = order.getAddress();
			String pin = address != null ? address.getPin() : null;

			if (!getCourierService().isCodAllowed(pin)) {
                message = MHKConstants.COD_NOT_IN_PINCODE + pin;
                status = MHKConstants.STATUS_ERROR;
                return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, status));
			} else if (order.getIsExclusivelyServiceOrder()) {
                message = MHKConstants.COD_NOT_FOR_SERVICES;
                status = MHKConstants.STATUS_ERROR;
                return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, status));
			} else if (order.getContainsServices()) {
                message = MHKConstants.COD_NOT_FOR_SERVICES_PYMT;
                status = MHKConstants.STATUS_ERROR;
                return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, status));
			} else if (order.getAmount() < codMinAmount || order.getAmount() > codMaxAmount) {
                message = "Cod is only applicable when total item price is between " + codMinAmount + " and " + codMaxAmount;
                status = MHKConstants.STATUS_ERROR;
                return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, status));
			}

			try {
				getPaymentManager().verifyPayment(gatewayOrderId, order.getAmount(), null);
				getPaymentManager().codSuccess(gatewayOrderId, codContactName, codContactPhone);
				payMap.put("gatewayOrderId", gatewayOrderId);
                paymentSuccess(gatewayOrderId,request,response);
                status = MHKConstants.STATUS_OK;
                message = MHKConstants.STATUS_DONE;
                //resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} catch (HealthkartPaymentGatewayException e) {
				getPaymentManager().error(gatewayOrderId, e);
                payMap.put("gatewayOrderId", gatewayOrderId);
                status = MHKConstants.STATUS_ERROR;
                message = MHKConstants.STATUS_ERROR;
				//resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
			}
		} else {
			//addRedirectAlertMessage(new SimpleMessage("Please try again, else Payment for the order has already been recorded."));
            payMap.put("message", "Please try again, else Payment for the order has already been recorded.");
			//resolution = new RedirectResolution(PaymentModeAction.class).addParameter("order", order);
		}
        healthkartResponse = new HealthkartResponse(status, message, payMap);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

        addHeaderAttributes(response);

        return jsonBuilder;
	}
    public void paymentSuccess(String gatewayOrderId,HttpServletRequest request,HttpServletResponse response){
        payment = paymentService.findByGatewayOrderId(gatewayOrderId);
        if (payment != null && EnumPaymentStatus.getPaymentSuccessPageStatusIds().contains(payment.getPaymentStatus().getId())) {

            Long paymentStatusId = payment.getPaymentStatus() != null ? payment.getPaymentStatus().getId() : null;

//            logger.info("payment success page payment status " + paymentStatusId);

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

            adminOrderService.splitBOEscalateSOCreateShipmentAndRelatedTasks(order);
/*
            RewardPointMode prepayOfferRewardPoint = rewardPointService.getRewardPointMode(EnumRewardPointMode.Prepay_Offer);
            RewardPoint prepayRewardPoints;
            EnumRewardPointStatus rewardPointStatus;

            //HttpServletRequest httpRequest = WebContext.getRequest();
            //HttpServletResponse httpResponse = WebContext.getResponse();
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
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
                                prepayRewardPoints = rewardPointService.addRewardPoints(order.getUser(), null, order, cashBack, "Prepay Offer", rewardPointStatus, prepayOfferRewardPoint);
                                referrerProgramManager.approveRewardPoints(Arrays.asList(prepayRewardPoints), new DateTime().plusMonths(3).toDate());
                            }
                            Set<CartLineItem>
                                    codCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.CodCharges).filter();
                            CartLineItem codCartLineItem = codCartLineItems != null && !codCartLineItems.isEmpty() ? codCartLineItems.iterator().next() : null;
                            if (codCartLineItems != null && !codCartLineItems.isEmpty() && codCartLineItem != null) {
                                Double applicableCodCharges = 0D;
                                applicableCodCharges = codCartLineItem.getHkPrice() - codCartLineItem.getDiscountOnHkPrice();
                                order.setAmount(order.getAmount() - (applicableCodCharges));
                                Set<ShippingOrder> shippingOrders = order.getShippingOrders();
                                if (shippingOrders != null) {
                                    for (ShippingOrder shippingOrder : shippingOrders) {
                                        shippingOrderService.nullifyCodCharges(shippingOrder);
                                        shipmentService.recreateShipment(shippingOrder);
                                        shippingOrderService.autoEscalateShippingOrder(shippingOrder);
                                    }
                                }
                                Set<CartLineItem> cartLineItems = order.getCartLineItems();
                                cartLineItems.removeAll(codCartLineItems);
                                getBaseDao().deleteAll(codCartLineItems);
                                order.getCartLineItems().addAll(cartLineItems);
                                order = orderService.save(order);
                                orderLoggingService.logOrderActivity(order, EnumOrderLifecycleActivity.Cod_Conversion);
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
            response.addCookie(wantedCODCookie);
*/       
        }

    }
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getCodContactName() {
		return codContactName;
	}

	public void setCodContactName(String codContactName) {
		this.codContactName = codContactName;
	}

	public String getCodContactPhone() {
		return codContactPhone;
	}

	public void setCodContactPhone(String codContactPhone) {
		this.codContactPhone = codContactPhone;
	}

	public CourierService getCourierService() {
		return courierService;
	}

	public void setCourierService(CourierService courierService) {
		this.courierService = courierService;
	}

	public OrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public PaymentManager getPaymentManager() {
		return paymentManager;
	}

	public void setPaymentManager(PaymentManager paymentManager) {
		this.paymentManager = paymentManager;
	}

}
