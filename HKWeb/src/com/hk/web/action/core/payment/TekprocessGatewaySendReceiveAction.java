package com.hk.web.action.core.payment;

import com.CheckSumRequestBean;
import com.TPSLUtil;
import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.manager.payment.TekprocessPaymentGatewayWrapper;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.web.AppConstants;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TekprocessGatewaySendReceiveAction extends BasePaymentGatewaySendReceiveAction<TekprocessPaymentGatewayWrapper> {

	private static Logger logger = LoggerFactory.getLogger(TekprocessGatewaySendReceiveAction.class);

	@Autowired
	PaymentDao paymentDao;
	@Autowired
	PaymentManager paymentManager;
	@Autowired
	EmailManager emailManager;

	protected TekprocessPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
		TekprocessPaymentGatewayWrapper tekprocessPaymentGatewayWrapper = new TekprocessPaymentGatewayWrapper();
		//Payment payment = paymentDao.findByGatewayOrderId(data.getGatewayOrderId());
		String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());

		CheckSumRequestBean checkSumRequestBean = new CheckSumRequestBean();
		checkSumRequestBean.setStrMerchantTranId(data.getGatewayOrderId());
		checkSumRequestBean.setStrMarketCode(TekprocessPaymentGatewayWrapper.merchantCode);
		checkSumRequestBean.setStrAccountNo("1");
		checkSumRequestBean.setStrAmt(amountStr);
		checkSumRequestBean.setStrBankCode(data.getPaymentMethod());
		checkSumRequestBean.setStrPropertyPath(AppConstants.getAppClasspathRootPath() + "/tekprocess.live.properties");

		TPSLUtil tpslUtil = new TPSLUtil();
		String msg = tpslUtil.transactionRequestMessage(checkSumRequestBean);

		logger.info("sending to payment gateway TekProcess with the parameter string msg : " + msg);

		tekprocessPaymentGatewayWrapper.addParameter(TekprocessPaymentGatewayWrapper.param_msg, msg);

		return tekprocessPaymentGatewayWrapper;
	}

	@DefaultHandler
	public Resolution callback() {
		String msg = getContext().getRequest().getParameter(TekprocessPaymentGatewayWrapper.param_msg);
		/*
				 * now do all sorts of verifications before proceeding 1. do gateway specific validations first 2. check is this
				 * payment status is already success/fail 3. get order id from this and generate a checksum, the compare
				 * checksum value with the one in payment 4. verify ip (should be same) 5. see if the order is already paid for
				 * (raise a double payment alert to admins) if payment is verified, then proceed with other stuff - change order
				 * status to reflect payment - fire transaction success emails with invoice - generate order - finally redirect
				 * to the required page (success, fail, authPending, double payment, etc)
				 */

		logger.info("returning from payment gateway TekProcess with the parameter string msg : " + msg);

        if(msg == null || StringUtils.isEmpty(msg)){
            logger.info("Received Empty response from TekProcess Gateway, redirecting to failure page");
            return new ForwardResolution("/pages/payment/paymentFail.jsp");
        }

		String propertyFilePath = AppConstants.getAppClasspathRootPath() + "/tekprocess.live.properties";

		Map<String, String> paramMap = TekprocessPaymentGatewayWrapper.parseResponse(msg);

		String gatewayOrderId = paramMap.get(TekprocessPaymentGatewayWrapper.key_TxnReferenceNo);
		String ePGTxnID = paramMap.get(TekprocessPaymentGatewayWrapper.key_TxnReferenceNo);
		String amountStr = paramMap.get(TekprocessPaymentGatewayWrapper.key_TxnAmount);
		Double amount = NumberUtils.toDouble(amountStr);
		String authStatus = paramMap.get(TekprocessPaymentGatewayWrapper.key_AuthStatus);
		String merchantParam = null;

		Resolution resolution = null;
		try {
			// tekprocess specific validation
			TekprocessPaymentGatewayWrapper.verifyChecksum(msg, propertyFilePath);

			// our own validations
			paymentManager.verifyPayment(gatewayOrderId, amount, merchantParam);

			// payment callback has been verified. now see if it is successful or failed from the gateway response
			if (TekprocessPaymentGatewayWrapper.authStatus_Success.equals(authStatus)) {
				paymentManager.success(gatewayOrderId,ePGTxnID);
				resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else if (TekprocessPaymentGatewayWrapper.authStatus_PendingApproval.equals(authStatus)) {
				paymentManager.pendingApproval(gatewayOrderId);
				resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else if (TekprocessPaymentGatewayWrapper.authStatus_Fail.equals(authStatus)) {
				paymentManager.fail(gatewayOrderId);
				emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
				resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else {
				throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
			}
		} catch (HealthkartPaymentGatewayException e) {
			paymentManager.error(gatewayOrderId, ePGTxnID, e, "");
			emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
			resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
		}
		return resolution;
	}

}
