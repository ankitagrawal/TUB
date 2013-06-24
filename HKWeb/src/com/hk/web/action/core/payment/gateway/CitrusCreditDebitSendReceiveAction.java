package com.hk.web.action.core.payment.gateway;

import java.util.Date;
import java.util.Properties;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.akube.framework.util.BaseUtils;
import com.citruspay.pg.net.RequestSignature;
import com.hk.constants.payment.EnumCitrusResponseCodes;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.manager.payment.CitrusPaymentGatewayWrapper;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.web.AppConstants;
import com.hk.web.action.core.payment.PaymentFailAction;
import com.hk.web.action.core.payment.PaymentSuccessAction;

@Component
public class CitrusCreditDebitSendReceiveAction extends BasePaymentGatewaySendReceiveAction<CitrusPaymentGatewayWrapper> {

    private static Logger logger = LoggerFactory.getLogger(CitrusCreditDebitSendReceiveAction.class);

    @Autowired
    PaymentDao paymentDao;
    @Autowired
    PaymentManager paymentManager;
    @Autowired
    LinkManager linkManager;
    @Autowired
    EmailManager emailManager;

    protected CitrusPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
        CitrusPaymentGatewayWrapper citrusPaymentGatewayWrapper = new CitrusPaymentGatewayWrapper(AppConstants.appBasePath);
        Payment payment = paymentDao.findByGatewayOrderId(data.getGatewayOrderId());
        Order order = payment.getOrder();
        User user = order.getUser();
        Address address = order.getAddress();
        String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());

        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/citrus.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);

        String key = properties.getProperty(CitrusPaymentGatewayWrapper.key);
        String merchantTxnId = data.getGatewayOrderId();
        String currency = properties.getProperty(CitrusPaymentGatewayWrapper.CurrCode);
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.email, user.getEmail());
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.addressStreet1, address.getLine1());
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.addressCity, address.getCity());
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.addressZip, address.getPincode());
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.addressState, address.getState());
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.addressCountry, "INDIA");
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.merchantTxnId, merchantTxnId);
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.orderAmount, amountStr);
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.currency, currency);
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.firstName, user.getName());
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.lastName, "HK");        //hardcoded last name, disabled it from citrus UI
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.phoneNumber, address.getPhone());
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.returnUrl, linkManager.getCitrusPaymentCreditDebitGatewayUrl());
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.reqtime, new Date().getTime());

        String vanityURLPart = properties.getProperty(CitrusPaymentGatewayWrapper.vanityUrl);
        String tdp = vanityURLPart + amountStr + merchantTxnId + currency;
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.secSignature, RequestSignature.generateHMAC(tdp, key));

        citrusPaymentGatewayWrapper.setGatewayUrl(properties.getProperty(CitrusPaymentGatewayWrapper.merchantURLPart));

        logger.info("sending to payment gateway Citrus for gateway order id " + merchantTxnId + "and amount " + amountStr);

        return citrusPaymentGatewayWrapper;
    }

    @DefaultHandler
    public Resolution callback() {
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/citrus.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String key = properties.getProperty(CitrusPaymentGatewayWrapper.key);
//        logger.info("in citrus callback -> " + getContext().getRequest().getParameterMap());
        String gatewayOrderId = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxId);
        String TxStatus = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxStatus);
        String pgRespCode = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.pgRespCode);
        String amount = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.amount);
        String ePGTxnID = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.pgTxnNo);
//        String rrn = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.RRN);
        String rrn = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxRefNo);
        String authIdCode = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.authIdCode);
        String responseMsg = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxMsg);
        String reqSignature = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.signature);
        String data = CitrusPaymentGatewayWrapper.getRequestSignatureText(getContext());
        com.citruspay.pg.net.RequestSignature sigGenerator = new com.citruspay.pg.net.RequestSignature();

        try {
            String signature = sigGenerator.generateHMAC(data, key);
             if (gatewayOrderId == null || StringUtils.isEmpty(gatewayOrderId)) {
                logger.info("Received Empty gateway Id  from Citrus NetBank Gateway, redirecting to failure page");
                return new ForwardResolution("/pages/payment/paymentFail.jsp");
            }
            if (signature == null || StringUtils.isEmpty(signature) || reqSignature == null || StringUtils.isEmpty(reqSignature)) {
                logger.info("Exception in generating either signature  -->" + signature + " or  Request Signature -->" + reqSignature);
                paymentManager.fail(gatewayOrderId, ePGTxnID, "Signature Generation isssue for GatewayOrder Id -->" + gatewayOrderId);
                return new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            }

            if (reqSignature != null && !reqSignature.equalsIgnoreCase("") && !signature.equalsIgnoreCase(reqSignature)) {
                paymentManager.fail(gatewayOrderId, ePGTxnID, responseMsg);
                return new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);

            }
        } catch (Exception e) {
            logger.info("exception at verifying request signature", e);
            paymentManager.fail(gatewayOrderId, ePGTxnID, responseMsg);
            return new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
        }


        logger.info("in citrus callback -> " + responseMsg + "for gateway order id " + gatewayOrderId + "TxStatus " + TxStatus + pgRespCode);
        String merchantParam = null;
        Resolution resolution = null;
        try {
            // our own validations
            paymentManager.verifyPayment(gatewayOrderId, NumberUtils.toDouble(amount), merchantParam);
            // payment callback has been verified. now see if it is successful or failed from the gateway response

            if (TxStatus.equalsIgnoreCase(EnumCitrusResponseCodes.TxStatusSuccess.getId()) && pgRespCode.equals(EnumCitrusResponseCodes.Transaction_Successful.getId())) {
                paymentManager.success(gatewayOrderId, ePGTxnID, rrn, responseMsg, authIdCode);
                resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } else if (EnumCitrusResponseCodes.TxStatusSESSION_EXPIRED.getId().equals(TxStatus) || EnumCitrusResponseCodes.TxStatusCANCELED.getId().equals(TxStatus)) {
                paymentManager.fail(gatewayOrderId, ePGTxnID, responseMsg);
                resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } else if (EnumCitrusResponseCodes.TxStatusFAIL.getId().equals(TxStatus)) {
                if (EnumCitrusResponseCodes.Rejected_By_Gateway.getId().equals(pgRespCode)) {
                    throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.REJECTED_BY_GATEWAY);
                } else if (EnumCitrusResponseCodes.Rejected_By_Issuer.getId().equals(pgRespCode)) {
                    throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.REJECTED_BY_ISSUER);
                }
            } else {
                throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
            }
        } catch (HealthkartPaymentGatewayException e) {
            emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
            paymentManager.error(gatewayOrderId, ePGTxnID, e, responseMsg);
            resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
        }
        return resolution;
    }

}
