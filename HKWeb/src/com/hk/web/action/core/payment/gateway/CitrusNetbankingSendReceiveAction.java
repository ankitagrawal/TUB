package com.hk.web.action.core.payment.gateway;

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
import com.citruspay.pg.exception.CitruspayException;
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
public class CitrusNetbankingSendReceiveAction extends BasePaymentGatewaySendReceiveAction<CitrusPaymentGatewayWrapper> {

    private static Logger logger = LoggerFactory.getLogger(CitrusNetbankingSendReceiveAction.class);

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
        logger.info("sending to payment gateway Citrus for gateway order id " + merchantTxnId + "and amount " + amountStr);
        com.citruspay.pg.util.CitruspayConstant.merchantKey = key;
        java.util.Map<String, Object> params = new java.util.HashMap<String, Object>();
        com.citruspay.pg.model.Card card = new com.citruspay.pg.model.Card();
        card.setPaymentMode(com.citruspay.pg.util.PaymentMode.NET_BANKING.toString());
        com.citruspay.pg.model.Customer cust = new com.citruspay.pg.model.Customer();
        cust.setEmail(user.getEmail());
        cust.setFirstName(user.getName());
        cust.setLastName("HK");
        cust.setPhoneNumber(address.getPhone());

//        com.citruspay.pg.model.Address addressDummy = new com.citruspay.pg.model.Address();
        params.put(CitrusPaymentGatewayWrapper.key, key);
        params.put(CitrusPaymentGatewayWrapper.merchantAccessKey, properties.get(CitrusPaymentGatewayWrapper.merchantAccessKey));
        params.put(CitrusPaymentGatewayWrapper.issuerCode, data.getPaymentMethod());
        params.put(CitrusPaymentGatewayWrapper.transactionId, merchantTxnId);
        params.put(CitrusPaymentGatewayWrapper.amount, amountStr);
        params.put(CitrusPaymentGatewayWrapper.returnUrl, linkManager.getCitrusPaymentNetBankingGatewayUrl());
        params.put(CitrusPaymentGatewayWrapper.card, card);
        params.put(CitrusPaymentGatewayWrapper.customer, cust);


        com.citruspay.pg.model.Transaction txn = null;
        try {
            txn = com.citruspay.pg.model.Transaction.create(params);
        } catch (CitruspayException e) {
            logger.error("exception at creating  parameter" + e);
            paymentManager.fail(merchantTxnId, null, "API Connection exception");
        }

        if (txn != null) {
            if (txn.getRespCode().equalsIgnoreCase(EnumCitrusResponseCodes.Success.getId()) && txn.getRedirectUrl() != null) {
                String strRedirectionURL = txn.getRedirectUrl();
                logger.debug("redirect url" + strRedirectionURL);
                citrusPaymentGatewayWrapper.setGatewayUrl(strRedirectionURL);
            } else if (txn.getRespCode().equalsIgnoreCase(EnumCitrusResponseCodes.Bad_Request.getId()) || txn.getRespCode().equalsIgnoreCase(EnumCitrusResponseCodes.Unauthorized_User.getId())) {
                logger.debug(txn.getRespMsg());
                paymentManager.fail(merchantTxnId, txn.getTransactionId(), txn.getRespMsg());
            }
        } else {
            paymentManager.fail(merchantTxnId);
        }
        return citrusPaymentGatewayWrapper;
    }

    @DefaultHandler
    public Resolution callback() {
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/citrus.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String key = properties.getProperty(CitrusPaymentGatewayWrapper.key);
//        String merchantAccessKey = properties.getProperty(CitrusPaymentGatewayWrapper.merchantAccessKey);
        String TxMsg = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxMsg);
        String gatewayOrderId = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxId);
        String TxStatus = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxStatus);
        String ePGTxnID = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.pgTxnNo);
        String pgRespCode = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.pgRespCode);
        String rrn = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxRefNo);
        String authIdCode = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.authIdCode);
        String amount = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.amount);
        String reqSignature = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.signature);
//        String merchantTxnId = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.transactionId);
        String data = CitrusPaymentGatewayWrapper.getRequestSignatureText(getContext());
        com.citruspay.pg.net.RequestSignature sigGenerator = new com.citruspay.pg.net.RequestSignature();
        Resolution resolution = null;
        try {
            String signature = sigGenerator.generateHMAC(data, key);
            if (gatewayOrderId == null || StringUtils.isEmpty(gatewayOrderId)) {
                logger.info("Received Empty gateway Id  from Citrus credit debit Gateway, redirecting to failure page");
                if(isHybridRelease())
                    return new ForwardResolution("/pages/payment/paymentFailBeta.jsp");
                else
                    return new ForwardResolution("/pages/payment/paymentFail.jsp");
            }
            if (signature == null || StringUtils.isEmpty(signature) || reqSignature == null || StringUtils.isEmpty(reqSignature)) {
                logger.info("Exception in generating either signature  -->" + signature + " or  Request Signature -->" + reqSignature);
                paymentManager.fail(gatewayOrderId, ePGTxnID, "Signature Generation isssue for GatewayOrder Id -->" + gatewayOrderId);
                return new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            }
            if (reqSignature != null && !reqSignature.equalsIgnoreCase("") && !signature.equalsIgnoreCase(reqSignature)) {
                paymentManager.fail(gatewayOrderId, ePGTxnID, TxMsg);
                return new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);

            }
        } catch (Exception e) {
            logger.info("exception at verifying request signature", e);
            paymentManager.fail(gatewayOrderId, ePGTxnID, TxMsg);
            return new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
        }


        logger.info("in citrus callback -> " + TxMsg + "for gateway order id " + gatewayOrderId + "TxStatus " + TxStatus + pgRespCode);
        String merchantParam = null;

        try {
            // our own validations
            paymentManager.verifyPayment(gatewayOrderId, NumberUtils.toDouble(amount), merchantParam);
            logger.info("Status returned from Citrus Payment Gateway" + TxStatus);
            // payment callback has been verified. now see if it is successful or failed from the gateway response
            if (TxStatus.equals(EnumCitrusResponseCodes.TxStatusSuccess.getId()) && pgRespCode.equals(EnumCitrusResponseCodes.Transaction_Successful.getId())) {
                paymentManager.success(gatewayOrderId, ePGTxnID, rrn, TxMsg, authIdCode);
                resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } else if (TxStatus.equals(EnumCitrusResponseCodes.TxStatusFAIL.getId()) && pgRespCode.equals(EnumCitrusResponseCodes.Rejected_By_Issuer.getId())) {
                paymentManager.fail(gatewayOrderId, ePGTxnID, TxMsg);
                emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
                resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } else {
                throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
            }
        } catch (HealthkartPaymentGatewayException e) {
            emailManager.sendPaymentFailMail(getPrincipalUser(), gatewayOrderId);
            paymentManager.error(gatewayOrderId, ePGTxnID, e, TxMsg);
            resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
        }
        return resolution;
    }

}
