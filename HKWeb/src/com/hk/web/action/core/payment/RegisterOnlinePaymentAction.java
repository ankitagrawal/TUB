package com.hk.web.action.core.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.payment.PreferredBankGateway;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pricing.PricingEngine;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.factory.PaymentModeActionFactory;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 8/27/12
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */

/*
  parametrized read encrypted gateway order id from url

  the payment amount would again be precomputed using pricing engine, good thing is pricing engine doesn't apply cod charges
  whereas the shipping charges will be as applicable

  read encrypted offerID from url

  steps involved, if all is well, create a payment and mark it as successful  --> like always

  then for the previous payment if any, mark the payment status as Retried for Prepay  --> no need to

  change the payment id for the corresponding order     --> automatically plan done

  no need to change the order status, but at the final step it should not be in cart.   --> done

  remove cod cart_line_item if any, and nullify cod charges on line_item        --> done

 */


@Component
public class RegisterOnlinePaymentAction extends BaseAction {

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
	@Autowired
	PricingEngine pricingEngine;

	List<PreferredBankGateway> bankList;
	PricingDto pricingDto;

	@DefaultHandler
	public Resolution pre() {
		bankList = getBaseDao().getAll(PreferredBankGateway.class); //todo verify if pricing engine will return the right amount or not, i would prefer using the previous payment amount as the base parameter
		Set<CartLineItem> pCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
		pricingDto = new PricingDto(pricingEngine.calculatePricing(pCartLineItems, order.getOfferInstance(), order.getAddress(), order.getRewardPointsUsed()), order.getAddress());
		return new ForwardResolution("/pages/payment/prePayment.jsp");
	}

	@SuppressWarnings("unchecked")
	public Resolution proceed() {
		if (!order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {
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

			// first create a payment row, this will also contain the payment checksum
			Payment payment = paymentManager.createNewPayment(order, paymentMode, BaseUtils.getRemoteIpAddrForUser(getContext()), bankCode);

			RedirectResolution redirectResolution;
			if (gateway != null) {
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
		addRedirectAlertMessage(new SimpleMessage("Some Error Occurred, Unable to process your request"));
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

	public PricingDto getPricingDto() {
		return pricingDto;
	}

	public void setPricingDto(PricingDto pricingDto) {
		this.pricingDto = pricingDto;
	}

	public List<PreferredBankGateway> getBankList() {
		return bankList;
	}

	public void setBankList(List<PreferredBankGateway> bankList) {
		this.bankList = bankList;
	}

	public PreferredBankGateway getBank() {
		return bank;
	}

	public void setBank(PreferredBankGateway bank) {
		this.bank = bank;
	}
}

