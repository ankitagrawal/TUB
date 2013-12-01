package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.cache.RoleCache;
import com.hk.constants.core.EnumRole;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pricing.PricingEngine;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.action.core.order.OrderSummaryAction;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

/**
 * User: kani Time: 14 Apr, 2010 4:56:25 PM
 */
@Secure
@Component
public class FreeCheckoutConfirmAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(FreeCheckoutConfirmAction.class);
	@Autowired
	PaymentManager paymentManager;
	@Autowired
	OrderManager orderManager;
	@Autowired
	RoleDao roleDao;
	@Autowired
	PaymentModeDao paymentModeDao;
	@Autowired
	PricingEngine pricingEngine;

	private String email;

	private User user;

	@ValidationMethod
	public void validate() {
		//Role tempUserRole = roleDao.getRoleByName(RoleConstants.TEMP_USER);
	    
	    Role tempUserRole = RoleCache.getInstance().getRoleByName(EnumRole.TEMP_USER).getRole();
	    
		user = getUserService().getUserById(getPrincipal().getId());
		if (user != null && user.getRoles().contains(tempUserRole)) {
			if (StringUtils.isBlank(email)) {
				addValidationError("email", new LocalizableError("/CodPaymentReceive.action.email.required"));
			}
		} else {
			// if somebody tries to enter the email field though its not required then set it to null
			// so that we should not change it.
			email = null;
		}
	}

	public Resolution confirm() {
		Order order = orderManager.getOrCreateOrder(user);

		PricingDto pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), order.getAddress(), order.getRewardPointsUsed()),
				order.getAddress());
		logger.debug("pricingDto.getGrandTotalPayable(): " + pricingDto.getGrandTotalPayable());
		if (pricingDto.getGrandTotalPayable() != 0) {
			addRedirectAlertMessage(new LocalizableMessage("/FreeCheckoutConfirm.action.method.mode.not.allowed"));
			return new RedirectResolution(OrderSummaryAction.class);
		}
		if (pricingDto.getProductLineCount() == 0) {
			addRedirectAlertMessage(new LocalizableMessage("/CheckoutAction.action.checkout.not.allowed.on.empty.cart"));
			return new RedirectResolution(CartAction.class);
		}

		if (email != null) {
			user.setEmail(email);
			user = getUserService().save(user);
		}

		// recalculate the pricing before creating a payment.
		order = orderManager.recalAndUpdateAmount(order);

		// first create a payment row, this will also cotain the payment checksum
		Payment payment = paymentManager.createNewPayment(order, getBaseDao().get(PaymentMode.class, EnumPaymentMode.FREE_CHECKOUT.getId()),
				BaseUtils.getRemoteIpAddrForUser(getContext()), null, null, null);

		paymentManager.success(payment.getGatewayOrderId());
		// return new RedirectResolution(FreeCheckoutSuccessAction.class).addParameter("gatewayOrderId",
		// payment.getGatewayOrderId());
		return new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", payment.getGatewayOrderId());
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
