package com.hk.web.action.core.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.payment.PreferredBankGateway;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.web.action.core.auth.LoginAction;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.factory.PaymentModeActionFactory;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.Random;

/**
 * Author: Kani Date: Dec 29, 2008
 */
@Component
@Secure(hasAnyRoles = { RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER }, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
public class PaymentAction extends BaseAction {

	@Validate(required = true)
	private PaymentMode paymentMode;

	Long bankId;

	@Validate(required = true, encrypted = true)
	private Order order;

	private User user;
	PreferredBankGateway bank;
	@Autowired
	PaymentManager paymentManager;
	@Autowired
	OrderManager orderManager;
	@Autowired
	OfferInstanceDao offerInstanceDao;
	@Autowired
	UserDao userDao;
	@Autowired
	RoleDao roleDao;
	@Autowired
	PaymentModeDao paymentModeDao;

	@SuppressWarnings("unchecked")
	public Resolution proceed() {
		if (order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {
			// recalculate the pricing before creating a payment.
			order = orderManager.recalAndUpdateAmount(order);

			if (order.getAmount() == 0) {
				addRedirectAlertMessage(new LocalizableMessage("/CheckoutAction.action.checkout.not.allowed.on.empty.cart"));
				return new RedirectResolution(CartAction.class);
			}

			if (bankId != null) {
				bank = getBaseDao().get(PreferredBankGateway.class, bankId);
			}
			if (bank != null) {
				if (bank.getPreferredGatewayId() == null) {
					Integer random = (new Random()).nextInt(100);
					if (random % 2 == 0) {
						paymentMode = getBaseDao().get(PaymentMode.class, EnumPaymentMode.TECHPROCESS.getId());
					} else {
						paymentMode = getBaseDao().get(PaymentMode.class, EnumPaymentMode.CITRUS_NetBanking_Old.getId());
					}
				} else {
					paymentMode = getBaseDao().get(PaymentMode.class, bank.getPreferredGatewayId());
				}
			}

			String bankCode = this.bankId != null ? this.bankId.toString() : null;
			EnumPaymentMode gateway = EnumPaymentMode.getPaymentModeFromId(paymentMode != null ? paymentMode.getId() : null);
			if (bank != null && gateway != null) {
				if (gateway.equals(EnumPaymentMode.TECHPROCESS)) {
					bankCode = bank.getTpslBankCode();
				} else if (gateway.equals(EnumPaymentMode.CITRUS_NetBanking_Old)) {
					bankCode = bank.getCitrusBankCode();
				} else if (gateway.equals(EnumPaymentMode.CITRUS_CreditDebit)) {
					bankCode = "999";
				}
			}

			RedirectResolution redirectResolution;

            if(gateway != null && !gateway.getId().equals(EnumPaymentMode.CITRUS_CreditDebit.getId()) && !gateway.getId().equals(EnumPaymentMode.PAYPAL_CreditDebit.getId()) && bank == null){
                Integer random = (new Random()).nextInt(100);
                if (random % 2 == 0) {
                    gateway = EnumPaymentMode.ICICI;
                }
                paymentMode = gateway.asPaymenMode();
            }

            // first create a payment row, this will also contain the payment checksum
            Payment payment = paymentManager.createNewPayment(order, paymentMode, BaseUtils.getRemoteIpAddrForUser(getContext()), bankCode);

			if (gateway != null) {
//                gateway = EnumPaymentMode.PAYPAL_CreditDebit;
				Class actionClass = PaymentModeActionFactory.getActionClassForPaymentMode(gateway);
				redirectResolution = new RedirectResolution(actionClass, "proceed");
				return redirectResolution.addParameter(BasePaymentGatewayWrapper.TRANSACTION_DATA_PARAM, BasePaymentGatewayWrapper.encodeTransactionDataParam(order.getAmount(),
						payment.getGatewayOrderId(), order.getId(), payment.getPaymentChecksum(), bankCode));
			} else {
				// ccavneue is the default gateway
				Class actionClass = PaymentModeActionFactory.getActionClassForPaymentMode(EnumPaymentMode.CCAVENUE_DUMMY);
				redirectResolution = new RedirectResolution(actionClass, "proceed");
			}
			return redirectResolution.addParameter(BasePaymentGatewayWrapper.TRANSACTION_DATA_PARAM, BasePaymentGatewayWrapper.encodeTransactionDataParam(order.getAmount(),
					payment.getGatewayOrderId(), order.getId(), payment.getPaymentChecksum(), null));

		}
		addRedirectAlertMessage(new SimpleMessage("Payment for the order is already made."));
		return new RedirectResolution(PaymentModeAction.class).addParameter("order", order);
	}

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}
}
