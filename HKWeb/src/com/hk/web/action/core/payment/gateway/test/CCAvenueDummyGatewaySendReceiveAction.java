package com.hk.web.action.core.payment.gateway.test;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.payment.CCAvenueDummyPaymentGatewayWrapper;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.web.action.core.payment.PaymentFailAction;
import com.hk.web.action.core.payment.PaymentPendingApprovalAction;
import com.hk.web.action.core.payment.PaymentSuccessAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: Kani Date: Jan 14, 2009
 */
@Component
public class CCAvenueDummyGatewaySendReceiveAction extends BasePaymentGatewaySendReceiveAction<CCAvenueDummyPaymentGatewayWrapper> {
	@Autowired
	PaymentDao paymentDao;
	@Autowired
	PaymentManager paymentManager;
	@Autowired
	EmailManager emailManager;

	protected CCAvenueDummyPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
		CCAvenueDummyPaymentGatewayWrapper dummyPaymentGateway = new CCAvenueDummyPaymentGatewayWrapper();
		// Payment payment = paymentDao.findByGatewayOrderId(data.getGatewayOrderId());
		String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());

		dummyPaymentGateway.addParameter(CCAvenueDummyPaymentGatewayWrapper.param_MerchantId, CCAvenueDummyPaymentGatewayWrapper.merchantId);
		dummyPaymentGateway.addParameter(CCAvenueDummyPaymentGatewayWrapper.param_Amount, amountStr);
		dummyPaymentGateway.addParameter(CCAvenueDummyPaymentGatewayWrapper.param_OrderId, data.getGatewayOrderId());
		dummyPaymentGateway.addParameter(CCAvenueDummyPaymentGatewayWrapper.param_RedirectUrl, getRedirectUrl());
		String gatewayChecksum = CCAvenueDummyPaymentGatewayWrapper.getRequestChecksum(CCAvenueDummyPaymentGatewayWrapper.merchantId, data.getGatewayOrderId(), amountStr,
				getRedirectUrl(), CCAvenueDummyPaymentGatewayWrapper.workingKey);

		dummyPaymentGateway.addParameter(CCAvenueDummyPaymentGatewayWrapper.param_MerchantParam, data.getChecksum());
		dummyPaymentGateway.addParameter(CCAvenueDummyPaymentGatewayWrapper.param_Checksum, gatewayChecksum);

		return dummyPaymentGateway;
	}

	@DefaultHandler
	public Resolution callback() {
		String gatewayOrderId = getContext().getRequest().getParameter(CCAvenueDummyPaymentGatewayWrapper.param_OrderId);
		/*
				 * now do all sorts of verifications before proceeding 1. do gateway specific validations first 2. check is this
				 * payment status is already success/fail 3. get order id from this and generate a checksum, the compare
				 * checksum value with the one in payment 4. verify ip (should be same) 5. see if the order is already paid for
				 * (raise a double payment alert to admins) if payment is verified, then proceed with other stuff - change order
				 * status to reflect payment - fire transaction success emails with invoice - generate order - finally redirect
				 * to the required page (success, fail, authPending, double payment, etc)
				 */

		String merchantId = getContext().getRequest().getParameter(CCAvenueDummyPaymentGatewayWrapper.param_MerchantId);
		String amountStr = getContext().getRequest().getParameter(CCAvenueDummyPaymentGatewayWrapper.param_Amount);
		Double amount = NumberUtils.toDouble(amountStr);
		String authDesc = getContext().getRequest().getParameter(CCAvenueDummyPaymentGatewayWrapper.param_AuthDesc);
		String gatewayChecksum = getContext().getRequest().getParameter(CCAvenueDummyPaymentGatewayWrapper.param_Checksum);
		String merchantParam = getContext().getRequest().getParameter(CCAvenueDummyPaymentGatewayWrapper.param_MerchantParam);
		//String customerNotes = getContext().getRequest().getParameter(CCAvenueDummyPaymentGatewayWrapper.param_Notes);

		Resolution resolution = null;
		try {
			// ccavenue specific validation
			CCAvenueDummyPaymentGatewayWrapper.verifyChecksum(merchantId, gatewayOrderId, amountStr, null, CCAvenueDummyPaymentGatewayWrapper.workingKey, gatewayChecksum);

			// our own validations
			paymentManager.verifyPayment(gatewayOrderId, amount, merchantParam);

			// payment callback has been verified. now see if it is successful or failed from the gateway response
			if (CCAvenueDummyPaymentGatewayWrapper.AuthDesc_Success.equals(authDesc)) {
				paymentManager.success(gatewayOrderId);
				resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else if (CCAvenueDummyPaymentGatewayWrapper.AuthDesc_PendingApproval.equals(authDesc)) {
				paymentManager.pendingApproval(gatewayOrderId);
				resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else if (CCAvenueDummyPaymentGatewayWrapper.AuthDesc_Fail.equals(authDesc)) {
				paymentManager.fail(gatewayOrderId);
				emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
				resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else {
				emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
				throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
			}
		} catch (HealthkartPaymentGatewayException e) {
			emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
			paymentManager.error(gatewayOrderId, e);
			resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
		}
		return resolution;
	}

}
