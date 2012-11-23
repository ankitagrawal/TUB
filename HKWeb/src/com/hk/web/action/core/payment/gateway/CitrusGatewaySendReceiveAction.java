package com.hk.web.action.core.payment.gateway;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.akube.framework.util.BaseUtils;
import com.ecs.epg.sfa.java.Merchant;
import com.ecs.epg.sfa.java.PGResponse;
import com.ecs.epg.sfa.java.PostLib;
import com.hk.constants.payment.EnumCitrusResponseCodes;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.manager.payment.CitrusPaymentGatewayWrapper;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.web.AppConstants;
import com.hk.web.action.core.payment.PaymentFailAction;
import com.hk.web.action.core.payment.PaymentSuccessAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class CitrusGatewaySendReceiveAction extends BasePaymentGatewaySendReceiveAction<CitrusPaymentGatewayWrapper> {

	private static Logger logger = LoggerFactory.getLogger(CitrusGatewaySendReceiveAction.class);

	@Autowired
	PaymentDao paymentDao;
	@Autowired
	PaymentManager paymentManager;
	@Autowired
	LinkManager linkManager;

	/*@Value("#{hkEnvProps['" + Keys.App.environmentDir + "']}")
		String                environmemtDir;*/
	@Autowired
	EmailManager emailManager;

	@SuppressWarnings("unchecked")
	protected CitrusPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
		CitrusPaymentGatewayWrapper citrusPaymentGatewayWrapper = new CitrusPaymentGatewayWrapper(AppConstants.appBasePath);
		Payment payment = paymentDao.findByGatewayOrderId(data.getGatewayOrderId());
		String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());

		String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/citrus.live.properties";
		Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);

		Merchant oMerchant = new Merchant();
		com.ecs.epg.sfa.java.IssuerData oIssuerData = new com.ecs.epg.sfa.java.IssuerData();
		com.ecs.epg.sfa.java.PGReserveData oPGReserveData = new com.ecs.epg.sfa.java.PGReserveData();

		Map testMap = new HashMap();
		testMap.put("abc", 1);

		oPGReserveData.setAdditionalData((HashMap) testMap);

		PostLib oPostLib = null;
		try {
			oPostLib = new PostLib("ABC");
		} catch (Exception e) {
			logger.info("some exception");
		}

		oMerchant.setMerchantDetails(properties.getProperty("MerchantId"), properties.getProperty("MerchantId"), properties.getProperty("MerchantId"), "127.0.0.1",
				payment.getGatewayOrderId(),
				payment.getGatewayOrderId()
				//, "http://www.healthkart.com/core/payment/CitrusGatewaySendReceiveAction.action",
				, linkManager.getCitrusPaymentGatewayUrl(),
//                , properties.getProperty("ResponseUrl"),
				properties.getProperty("ResponseMethod"), properties.getProperty("CurrCode"), payment.getGatewayOrderId(), "P",
				amountStr, "GMT+05:30", "Ext1", "true", "Ext3", "Ext4", "Ext5a");

		/* added for NetBanking Transaction */
		oIssuerData.setIssuerDetails(data.getPaymentMethod());

		logger.info("Citrus url being generated is " + linkManager.getCitrusPaymentGatewayUrl());

		PGResponse oPgResp = oPostLib.postNBMOTO(null, null, oMerchant, oPGReserveData, oIssuerData);

		if (oPgResp.getRedirectionUrl() != null) {
			String strRedirectionURL = oPgResp.getRedirectionUrl();
			citrusPaymentGatewayWrapper.setGatewayUrl(strRedirectionURL);
		} else {
			logger.info("Error encountered. Error Code : " + oPgResp.getRespCode() + " . Message " + oPgResp.getRespMessage());
		}
		logger.info("sending to payment gateway Citrus");

		return citrusPaymentGatewayWrapper;
	}

	@DefaultHandler
	public Resolution callback() {
		/*
				 * now do all sorts of verifications before proceeding 1. do gateway specific validations first 2. check is this
				 * payment status is already success/fail 3. get order id from this and generate a checksum, the compare
				 * checksum value with the one in payment 4. verify ip (should be same) 5. see if the order is already paid for
				 * (raise a double payment alert to admins) if payment is verified, then proceed with other stuff - change order
				 * status to reflect payment - fire transaction success emails with invoice - generate order - finally redirect
				 * to the required page (success, fail, authPending, double payment, etc)
				 */

//		System.out.println("in citrus callback");
		logger.error("in callback -> " + getContext().getRequest().getParameterMap());
		System.out.println("in callback -> " + getContext().getRequest().getParameterMap());
		String data = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.param_data);
		String responseMethod = getContext().getRequest().getMethod();
		String propertyFilePath = AppConstants.getAppClasspathRootPath() + "/citrus.live.properties";
		String validatedData = "";

		if (data != null) {
			logger.info("returning from payment gateway Citrus with the parameter string msg : " + data);
			// citrus specific validation
			validatedData = CitrusPaymentGatewayWrapper.validateEncryptedData(data, propertyFilePath);
		}

		Map<String, String> paramMap = CitrusPaymentGatewayWrapper.parseResponse(validatedData, responseMethod);
		logger.error("validated date -> " + validatedData);
		logger.error("data -> " + data);
		logger.error("responseMethod -> " + responseMethod);
		logger.error("propertyFilePath -> " + propertyFilePath);
		logger.error("param map->" + paramMap);
		String amountStr = paramMap.get(CitrusPaymentGatewayWrapper.Amount);
		Double amount = NumberUtils.toDouble(amountStr);
		String authStatus = paramMap.get(CitrusPaymentGatewayWrapper.RespCode);
		String responseMsg = ((String) paramMap.get(CitrusPaymentGatewayWrapper.Message)).replace('+', ' ');
		String gatewayOrderId = paramMap.get(CitrusPaymentGatewayWrapper.TxnID);
//        String authIdCode = paramMap.get(CitrusPaymentGatewayWrapper.AuthIdCode);
//        String issuerRefNo = paramMap.get(CitrusPaymentGatewayWrapper.RRN);
//        String txnRefNo = paramMap.get(CitrusPaymentGatewayWrapper.ePGTxnID);

		logger.info("response msg received from citrus is " + responseMsg + "for gateway order id " + gatewayOrderId);

		String merchantParam = null;

		Resolution resolution = null;
		try {

			// our own validations
			paymentManager.verifyPayment(gatewayOrderId, amount, merchantParam);

			logger.info("Status returned from Citrus Paymnet Gateway" + authStatus);

			// payment callback has been verified. now see if it is successful or failed from the gateway response
			if (authStatus.equals(EnumCitrusResponseCodes.Transaction_Successful.getId())) {
				paymentManager.success(gatewayOrderId);
				resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else if (EnumCitrusResponseCodes.Rejected_By_Issuer.getId().equals(authStatus)) {
				paymentManager.fail(gatewayOrderId);
				emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
				resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
			} else {
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
