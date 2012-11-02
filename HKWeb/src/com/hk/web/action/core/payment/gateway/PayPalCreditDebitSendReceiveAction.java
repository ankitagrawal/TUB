package com.hk.web.action.core.payment.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.math.NumberUtils;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.manager.payment.PayPalPaymentGatewayWrapper;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.web.AppConstants;
import com.hk.web.action.core.payment.PaymentSuccessAction;
import com.hk.web.action.core.payment.PaymentFailAction;
import com.hk.web.action.core.payment.PaymentPendingApprovalAction;
import com.hk.web.filter.WebContext;
import com.hk.domain.payment.Payment;
import com.hk.domain.order.Order;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.util.BaseUtils;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import com.hk.domain.user.User;
import com.hk.domain.user.Address;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.paypal.sdk.services.NVPCallerServices;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.util.Util;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.profiles.APIProfile;

import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.net.URLConnection;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Oct 30, 2012
 * Time: 1:26:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class PayPalCreditDebitSendReceiveAction extends BasePaymentGatewaySendReceiveAction<PayPalPaymentGatewayWrapper> {
    private static Logger logger = LoggerFactory.getLogger(PayPalCreditDebitSendReceiveAction.class);

    @Autowired
    PaymentDao paymentDao;
    @Autowired
    PaymentManager paymentManager;
    @Autowired
    LinkManager linkManager;
    @Autowired
    EmailManager emailManager;


    protected PayPalPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
//      PayPalPaymentGatewayWrapper payPalPaymentGatewayWrapper = new PayPalPaymentGatewayWrapper(AppConstants.appBasePath);
        Payment payment = paymentDao.findByGatewayOrderId(data.getGatewayOrderId());
        Order order = payment.getOrder();
        User user = order.getUser();
        Address address = order.getAddress();
        String merchantTxnId = data.getGatewayOrderId();
//        String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());

        // conversion logic
        Double coversion_rate = 55.05;
        Double coverted_amount = data.getAmount()/coversion_rate ;
        String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(coverted_amount);

        //  Reading property files
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/paypal.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String userid = properties.getProperty(PayPalPaymentGatewayWrapper.USER);
        String pwd = properties.getProperty(PayPalPaymentGatewayWrapper.PWD);
        String mode = properties.getProperty(PayPalPaymentGatewayWrapper.MODE);
        String signature = properties.getProperty(PayPalPaymentGatewayWrapper.SIGNATURE);
        String Version = properties.getProperty(PayPalPaymentGatewayWrapper.VERSION);
        String paymentAction = properties.getProperty(PayPalPaymentGatewayWrapper.PAYMENTACTION);
        String method = properties.getProperty(PayPalPaymentGatewayWrapper.METHOD);
        String environment = properties.getProperty(PayPalPaymentGatewayWrapper.ENVIRONMENT);

//  appending gateway and amount in return url      
        String return_url = linkManager.getPayPalPaymentGatewayReturnUrl() + "?gateway=" + merchantTxnId + "&amount=" + amountStr;

        PayPalPaymentGatewayWrapper payPalPaymentGatewayWrapper = new PayPalPaymentGatewayWrapper();
        NVPEncoder encoder = new NVPEncoder();
        NVPDecoder decoder = new NVPDecoder();

        try {

            NVPCallerServices caller = new NVPCallerServices();
            APIProfile profile = ProfileFactory.createSignatureAPIProfile();

//    setting up profile           
            profile.setAPIUsername(userid);
            profile.setAPIPassword(pwd);
            profile.setSignature(signature);
            profile.setEnvironment(environment);
            profile.setSubject("");
            caller.setAPIProfile(profile);

            encoder.add("VERSION", Version);
            encoder.add("METHOD", "SetExpressCheckout");
            encoder.add("RETURNURL", return_url);
            encoder.add("CANCELURL", linkManager.getPayPalPaymentGatewayCancelUrl());
            encoder.add("AMT", amountStr);
            encoder.add("PAYMENTACTION", "sale");
            encoder.add("CURRENCYCODE", "USD");

            encoder.add("ITEMAMT",amountStr);

            encoder.add("SOLUTIONTYPE", "Sole");
//            SOLUTIONTYPE= Sole


            // Execute the API operation and obtain the response.
            String NVPRequest = encoder.encode();
            String NVPResponse = caller.call(NVPRequest);
            decoder.decode(NVPResponse);


        } catch (Exception e) {
            logger.info("exception", e);
            paymentManager.fail(payment.getGatewayOrderId());
        }

        String ack = decoder.get("ACK");
        String Token = decoder.get("TOKEN");
        String CORRELATIONID = decoder.get("CORRELATIONID");

        /*
            payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.ACK, ack);
            payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.TOKEN, Token);
            payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.CORRELATIONID, CORRELATIONID);
        */

        try {
            if (ack.equals("Success")) {
                String gatewayUrl = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + Token;
                payPalPaymentGatewayWrapper.setGatewayUrl(gatewayUrl);
            }
        } catch (Exception e1) {
//                logger.info("exception", e1);
        }
        logger.info("sending to payment gateway paypal for gateway order id " + merchantTxnId + "and amount " + amountStr + "and correlation id" + CORRELATIONID );
        return payPalPaymentGatewayWrapper;
    }


    @DefaultHandler
    public Resolution callback() {

        NVPEncoder encoder = new NVPEncoder();
        NVPDecoder decoder = new NVPDecoder();
        Resolution resolution = null;
        NVPCallerServices caller = null;

        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/paypal.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String userid = properties.getProperty(PayPalPaymentGatewayWrapper.USER);
        String pwd = properties.getProperty(PayPalPaymentGatewayWrapper.PWD);
        String mode = properties.getProperty(PayPalPaymentGatewayWrapper.MODE);
        String signature = properties.getProperty(PayPalPaymentGatewayWrapper.SIGNATURE);
        String Version = properties.getProperty(PayPalPaymentGatewayWrapper.VERSION);
        String paymentAction = properties.getProperty(PayPalPaymentGatewayWrapper.PAYMENTACTION);

        // Retrieving the mandatory fields
        String Token = getContext().getRequest().getParameter("token");
        String payerid = getContext().getRequest().getParameter("PayerID");
        String gatewayOrderId = getContext().getRequest().getParameter("gateway");
        String amount_1 = getContext().getRequest().getParameter("amount");

        try {
            APIProfile profile = ProfileFactory.createSignatureAPIProfile();
            caller = new NVPCallerServices();
            profile.setAPIUsername(userid);
            profile.setAPIPassword(pwd);
            profile.setSignature(signature);
            profile.setEnvironment("sandbox");
            profile.setSubject("");
            caller.setAPIProfile(profile);

            encoder.add("VERSION", Version);
            encoder.add("METHOD", "DoExpressCheckoutPayment");
            encoder.add("TOKEN", Token);
            encoder.add("PAYERID", payerid); ;
            encoder.add("PAYMENTACTION", "sale");
            encoder.add("CURRENCYCODE", "USD");
            encoder.add("AMT", amount_1);
            String NVPRequest = encoder.encode();
            String NVPResponse = caller.call(NVPRequest);
            decoder.decode(NVPResponse);

        } catch (Exception e) {

        }

        String ack = decoder.get("ACK");
        String amount = decoder.get("AMT");
        String merchantParam = null;


        Double amount_in_rupee = NumberUtils.toDouble(amount)*55.05;


        if (ack.equals("Success")) {
            try {
                // our own validations
//                paymentManager.verifyPayment(gatewayOrderId, NumberUtils.toDouble(amount), merchantParam);
                  paymentManager.verifyPayment(gatewayOrderId, amount_in_rupee, merchantParam);

                // payment callback has been verified. now see if it is successful or failed from the gateway response
                if (ack.equals("Success")) {
                    paymentManager.success(gatewayOrderId);
                    resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
//			} else if (EbsPaymentGatewayWrapper.is_Flagged_True.equalsIgnoreCase(flag_status)) {
//				paymentManager.pendingApproval(gatewayOrderId);
//				resolution = new RedirectResolution(PaymentPendingApprovalAction.class).addParameter("gatewayOrderId", gatewayOrderId);
                } else {
                    paymentManager.fail(gatewayOrderId);
                    resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
                }
            } catch (HealthkartPaymentGatewayException e) {
                paymentManager.error(gatewayOrderId, e);
                resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
            }
        }
        return resolution;
    }


}


