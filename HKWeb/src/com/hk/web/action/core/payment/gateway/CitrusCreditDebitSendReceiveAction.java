package com.hk.web.action.core.payment.gateway;

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
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Properties;

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
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.merchantTxnId, merchantTxnId);
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.orderAmount, amountStr);
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.currency, currency);
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.firstName, user.getName());
//        citrusPaymentGatewayWrapper.addParameter("lastName", "HK");
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.phoneNumber, address.getPhone());
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.returnUrl, linkManager.getCitrusPaymentGatewayUrl());
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.reqtime, new Date().getTime());

        String vanityURLPart = CitrusPaymentGatewayWrapper.vanityURLPart;
        String tdp = vanityURLPart + amountStr + merchantTxnId + currency;
        citrusPaymentGatewayWrapper.addParameter(CitrusPaymentGatewayWrapper.secSignature, RequestSignature.generateHMAC(tdp, key));

        citrusPaymentGatewayWrapper.setGatewayUrl(properties.getProperty(CitrusPaymentGatewayWrapper.merchantURLPart));

        logger.info("sending to payment gateway Citrus for gateway order id " + merchantTxnId + "and amount " + amountStr);

        return citrusPaymentGatewayWrapper;
    }

    @DefaultHandler
    public Resolution callback() {
//        logger.info("in citrus callback -> " + getContext().getRequest().getParameterMap());

        String TxMsg = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxMsg);
        String gatewayOrderId = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxId);
        String TxStatus = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.TxStatus);
        String pgRespCode = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.pgRespCode);
        String amount = getContext().getRequest().getParameter(CitrusPaymentGatewayWrapper.amount);

        logger.info("in citrus callback -> " + TxMsg + "for gateway order id " + gatewayOrderId + "TxStatus " + TxStatus + pgRespCode);
        String merchantParam = null;
        Resolution resolution = null;
        try {
            // our own validations
            paymentManager.verifyPayment(gatewayOrderId, NumberUtils.toDouble(amount), merchantParam);
            logger.info("Status returned from Citrus Payment Gateway" + TxStatus);
            // payment callback has been verified. now see if it is successful or failed from the gateway response
            if (TxStatus.equals(EnumCitrusResponseCodes.TxStatusSuccess.getId()) && pgRespCode.equals(EnumCitrusResponseCodes.Transaction_Successful.getId())) {
                paymentManager.success(gatewayOrderId);
                resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } else if (TxStatus.equals(EnumCitrusResponseCodes.TxStatusFAIL.getId()) && pgRespCode.equals(EnumCitrusResponseCodes.Rejected_By_Issuer.getId())) {
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
