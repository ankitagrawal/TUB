package com.hk.web.action.core.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.payment.PreferredBankGateway;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.web.factory.PaymentModeActionFactory;
import com.hk.web.filter.WebContext;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Random;

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

	//	@Validate(required = true)
	private PaymentMode paymentMode;
	//	@Validate(required = true)
	private boolean isCodConversion;
	@Validate(required = true, encrypted = true)
	private Order order;

	@Autowired
	PaymentManager paymentManager;
	@Autowired
	PaymentModeDao paymentModeDao;

	List<PreferredBankGateway> bankList;
	PreferredBankGateway bank;
	Long bankId;

	@DefaultHandler
	public Resolution pre() {
		//currently i can safely assume, that most people whom we give conversion benefit will have 0 cod charges only, no order amount is pretty much their online payment amount
		//todo verify if pricing engine will return the right amount or not, i would prefer using the previous payment amount as the base parameter
		bankList = getBaseDao().getAll(PreferredBankGateway.class);
		if (order != null && order.isCOD()) {
			HttpServletResponse httpResponse = WebContext.getResponse();
			Cookie wantedCODCookie = new Cookie(HealthkartConstants.Cookie.wantedCOD, "true");
			wantedCODCookie.setPath("/");
			wantedCODCookie.setMaxAge(600);
			httpResponse.addCookie(wantedCODCookie);
		}
		return new ForwardResolution("/pages/prePayment.jsp");
	}

	@SuppressWarnings("unchecked")
	public Resolution prepay() {
		if (!order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {

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

	public boolean isCodConversion() {
		return isCodConversion;
	}

	public void setCodConversion(boolean codConversion) {
		isCodConversion = codConversion;
	}
}

