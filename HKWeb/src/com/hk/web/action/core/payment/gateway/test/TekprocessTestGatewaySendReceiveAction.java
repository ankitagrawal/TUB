package com.hk.web.action.core.payment.gateway.test;

import com.CheckSumRequestBean;
import com.TPSLUtil;
import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.payment.PaymentManager;
import com.hk.manager.payment.TekprocessTestPaymentGatewayWrapper;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.web.AppConstants;
import com.hk.web.action.core.payment.PaymentFailAction;
import com.hk.web.action.core.payment.PaymentPendingApprovalAction;
import com.hk.web.action.core.payment.PaymentSuccessAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TekprocessTestGatewaySendReceiveAction extends BasePaymentGatewaySendReceiveAction<TekprocessTestPaymentGatewayWrapper> {

	private static Logger logger = LoggerFactory.getLogger(TekprocessTestGatewaySendReceiveAction.class);

	@Autowired
	PaymentDao paymentDao;
	@Autowired
	PaymentManager paymentManager;
	/*@Value("#{hkEnvProps['" + Keys.App.environmentDir + "']}")
		String                environmemtDir;*/

	protected TekprocessTestPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
		TekprocessTestPaymentGatewayWrapper tekprocessTestPaymentGatewayWrapper = new TekprocessTestPaymentGatewayWrapper();
		Payment payment = paymentDao.findByGatewayOrderId(data.getGatewayOrderId());
		String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());

		CheckSumRequestBean checkSumRequestBean = new CheckSumRequestBean();
		checkSumRequestBean.setStrMerchantTranId(data.getGatewayOrderId());
		checkSumRequestBean.setStrMarketCode(TekprocessTestPaymentGatewayWrapper.merchantCode);
		checkSumRequestBean.setStrAccountNo("1");
		checkSumRequestBean.setStrAmt(amountStr);
		checkSumRequestBean.setStrBankCode("470");
		// checkSumRequestBean.setStrBankCode(data.getPaymentMethod());
		checkSumRequestBean.setStrPropertyPath(AppConstants.getAppClasspathRootPath() + "/tekprocess.properties");

		TPSLUtil tpslUtil = new TPSLUtil();
		String msg = tpslUtil.transactionRequestMessage(checkSumRequestBean);

		logger.info("sending to payment gateway TekProcessTest with the parameter string msg : " + msg);

		tekprocessTestPaymentGatewayWrapper.addParameter(TekprocessTestPaymentGatewayWrapper.param_msg, msg);

		return tekprocessTestPaymentGatewayWrapper;
	}

	@DefaultHandler
	public Resolution callback() {
		String msg = getContext().getRequest().getParameter(TekprocessTestPaymentGatewayWrapper.param_msg);
		/*
				 * now do all sorts of verifications before proceeding 1. do gateway specific validations first 2. check is this
				 * payment status is already success/fail 3. get order id from this and generate a checksum, the compare
				 * checksum value with the one in payment 4. verify ip (should be same) 5. see if the order is already paid for
				 * (raise a double payment alert to admins) if payment is verified, then proceed with other stuff - change order
				 * status to reflect payment - fire transaction success emails with invoice - generate order - finally redirect
				 * to the required page (success, fail, authPending, double payment, etc)
				 */

		logger.info("returning from payment gateway TekProcessTest with the parameter string msg : " + msg);

		String propertyFilePath = AppConstants.getAppClasspathRootPath() + "/tekprocess.properties";

		Map<String, String> paramMap = TekprocessTestPaymentGatewayWrapper.parseResponse(msg);

		String gatewayOrderId = paramMap.get(TekprocessTestPaymentGatewayWrapper.key_TxnReferenceNo);
		String amountStr = paramMap.get(TekprocessTestPaymentGatewayWrapper.key_TxnAmount);
		Double amount = NumberUtils.toDouble(amountStr);
		String authStatus = paramMap.get(TekprocessTestPaymentGatewayWrapper.key_AuthStatus);
		String merchantParam = null;

		Resolution resolution = null;
		try {
			// tekprocess specific validation
			TekprocessTestPaymentGatewayWrapper.verifyChecksum(msg, propertyFilePath);

			// our own validations
			paymentManager.verifyPayment(gatewayOrderId, amount, merchantParam);

			// payment callback has been verified. now see if it is successful or failed from the gateway response
			if (TekprocessTestPaymentGatewayWrapper.authStatus_Success.equals(authStatus)) {
				paymentManager.success(gatewayOrderId);
				resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else if (TekprocessTestPaymentGatewayWrapper.authStatus_PendingApproval.equals(authStatus)) {
				paymentManager.pendingApproval(gatewayOrderId);
				resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else if (TekprocessTestPaymentGatewayWrapper.authStatus_Fail.equals(authStatus)) {
				paymentManager.fail(gatewayOrderId);
				resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else {
				throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
			}
		} catch (HealthkartPaymentGatewayException e) {
			paymentManager.error(gatewayOrderId, e);
			resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
		}
		return resolution;
	}

}
