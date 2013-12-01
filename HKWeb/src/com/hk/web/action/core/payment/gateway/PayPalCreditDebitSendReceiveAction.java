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
import com.hk.pact.dao.core.AddressDao;
import com.hk.web.AppConstants;
import com.hk.web.action.core.payment.PaymentSuccessAction;
import com.hk.web.action.core.payment.PaymentFailAction;
import com.hk.web.action.core.payment.PaymentPendingApprovalAction;
import com.hk.web.filter.WebContext;
import com.hk.domain.payment.Payment;
import com.hk.domain.payment.CurrencyConverter;
import com.hk.domain.order.Order;
import com.akube.framework.stripes.action.BasePaymentGatewaySendReceiveAction;
import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;
import com.akube.framework.util.BaseUtils;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import com.hk.domain.user.User;
import com.hk.domain.user.Address;
import com.hk.domain.user.BillingAddress;
import com.hk.domain.core.Country;
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
import java.util.List;
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

    @Autowired
    AddressDao addressDao;


    protected PayPalPaymentGatewayWrapper getPaymentGatewayWrapperFromTransactionData(BasePaymentGatewayWrapper.TransactionData data) {
//      PayPalPaymentGatewayWrapper payPalPaymentGatewayWrapper = new PayPalPaymentGatewayWrapper(AppConstants.appBasePath);
        Payment payment = paymentDao.findByGatewayOrderId(data.getGatewayOrderId());
        Order order = payment.getOrder();
        User user = order.getUser();
        BillingAddress address = null;
        Country country = null;
        address = addressDao.getBillingAddressById(data.getBillingAddressId());
        if (address != null) {
            country = address.getCountry();
        }
        String merchantTxnId = data.getGatewayOrderId();

        //  Reading property files
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/paypal.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String userid = properties.getProperty(PayPalPaymentGatewayWrapper.USER);
        String pwd = properties.getProperty(PayPalPaymentGatewayWrapper.PWD);
        String signature = properties.getProperty(PayPalPaymentGatewayWrapper.SIGNATURE);
        String version = properties.getProperty(PayPalPaymentGatewayWrapper.VERSION);
        String paymentAction = properties.getProperty(PayPalPaymentGatewayWrapper.PAYMENTACTION);
        String setExpressMethod = properties.getProperty(PayPalPaymentGatewayWrapper.setExpressMethod);
        String environment = properties.getProperty(PayPalPaymentGatewayWrapper.ENVIRONMENT);
        String currencyCode = properties.getProperty(PayPalPaymentGatewayWrapper.currencyCode);
        String baseCurrencyCode = properties.getProperty(PayPalPaymentGatewayWrapper.baseCurrencyCode);
        String foreignCurrencyCode = properties.getProperty(PayPalPaymentGatewayWrapper.foreignCurrencyCode);

        CurrencyConverter currencyconverter = paymentDao.findLatestConversionRate(baseCurrencyCode, foreignCurrencyCode);
        Double coversion_rate = currencyconverter.getConversionRate();
        Double coverted_amount = data.getAmount() / coversion_rate;

        String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(coverted_amount);
        //  appending gateway and amount in return url
        String return_url = linkManager.getPayPalPaymentGatewayReturnUrl() + "?gateway=" + merchantTxnId + "&amount=" + amountStr;

        PayPalPaymentGatewayWrapper payPalPaymentGatewayWrapper = new PayPalPaymentGatewayWrapper();
        NVPEncoder encoder;
        NVPDecoder decoder = new NVPDecoder();

        try {
            NVPCallerServices caller = new NVPCallerServices();
            APIProfile profile = payPalPaymentGatewayWrapper.createPaypalApiProfile(userid, pwd, signature, environment);
            caller.setAPIProfile(profile);
            NVPEncoder baseEncoder = payPalPaymentGatewayWrapper.createPayPalBasicEncodedRequest(version, setExpressMethod, paymentAction, currencyCode, amountStr);
            encoder = payPalPaymentGatewayWrapper.encodeRequestForSetExpressCheckout(baseEncoder, return_url, linkManager.getPayPalPaymentGatewayCancelUrl(), user, address, merchantTxnId, amountStr, country);
            // Execute the API operation and obtain the response.
            String NVPRequest = encoder.encode();
            String NVPResponse = caller.call(NVPRequest);
            decoder.decode(NVPResponse);

            String ack = decoder.get("ACK");
            String Token = decoder.get("TOKEN");
            String CORRELATIONID = decoder.get("CORRELATIONID");
            String errorCode = decoder.get("L_ERRORCODE0");
            String errorShortMessage = decoder.get("L_SHORTMESSAGE0");
            String errorLongMessage = decoder.get("L_LONGMESSAGE0");

            if (ack != null && !(ack.equals(PayPalPaymentGatewayWrapper.Success_Ack) || ack.equals(PayPalPaymentGatewayWrapper.Success_with_Warning_Ack))) {
                logger.error(" SetExpressCheckout Failed : sending to payment gateway paypal for gateway order id " + merchantTxnId + "and amount " + amountStr + "and correlation id" + CORRELATIONID);
                logger.error("SetExpressCheckout Failed : Error Code : " + errorCode + " Error Short Message :" + errorShortMessage + " Error Long Message :" + errorLongMessage);
                paymentManager.fail(payment.getGatewayOrderId());

            } else {
//              String gatewayUrl = "https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + Token;
                String gatewayUrl = properties.getProperty(PayPalPaymentGatewayWrapper.gatewayUrl) + Token;
                payPalPaymentGatewayWrapper.setGatewayUrl(gatewayUrl);
                logger.info("sending to payment gateway paypal for gateway order id " + merchantTxnId + "and amount " + amountStr + "and correlation id" + CORRELATIONID);
            }

        } catch (Exception e) {
            logger.info("exception", e);
            paymentManager.fail(payment.getGatewayOrderId());
        }
        return payPalPaymentGatewayWrapper;
    }


    @DefaultHandler
    public Resolution callback() {
        NVPEncoder encoder;
        NVPDecoder decoder = new NVPDecoder();
        Resolution resolution = null;
//        NVPCallerServices caller = null;

        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/paypal.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String userid = properties.getProperty(PayPalPaymentGatewayWrapper.USER);
        String pwd = properties.getProperty(PayPalPaymentGatewayWrapper.PWD);
        String signature = properties.getProperty(PayPalPaymentGatewayWrapper.SIGNATURE);
        String version = properties.getProperty(PayPalPaymentGatewayWrapper.VERSION);
        String paymentAction = properties.getProperty(PayPalPaymentGatewayWrapper.PAYMENTACTION);
        String environment = properties.getProperty(PayPalPaymentGatewayWrapper.ENVIRONMENT);
        String DoExpressPaymentMethod = properties.getProperty(PayPalPaymentGatewayWrapper.DoExpressPaymentMethod);
        String currencyCode = properties.getProperty(PayPalPaymentGatewayWrapper.currencyCode);
        String baseCurrencyCode = properties.getProperty(PayPalPaymentGatewayWrapper.baseCurrencyCode);
        String foreignCurrencyCode = properties.getProperty(PayPalPaymentGatewayWrapper.foreignCurrencyCode);

        // Retrieving the mandatory fields
        String Token = getContext().getRequest().getParameter("token");
        String payerid = getContext().getRequest().getParameter("PayerID");
        String gatewayOrderId = getContext().getRequest().getParameter("gateway");
        String amount_thru_set = getContext().getRequest().getParameter("amount");

        PayPalPaymentGatewayWrapper payPalPaymentGatewayWrapper = new PayPalPaymentGatewayWrapper();

        try {
            NVPCallerServices caller = new NVPCallerServices();
            APIProfile profile = payPalPaymentGatewayWrapper.createPaypalApiProfile(userid, pwd, signature, environment);
            caller.setAPIProfile(profile);
            NVPEncoder baseEncoder = payPalPaymentGatewayWrapper.createPayPalBasicEncodedRequest(version, DoExpressPaymentMethod, paymentAction, currencyCode, amount_thru_set);
            encoder = payPalPaymentGatewayWrapper.encodeRequestForDoExpressPayment(baseEncoder, Token, payerid);

            String NVPRequest = encoder.encode();
            String NVPResponse = caller.call(NVPRequest);
            decoder.decode(NVPResponse);

        } catch (Exception e) {
            logger.info("exception", e);
            paymentManager.fail(gatewayOrderId);
        }

        String ack = decoder.get("ACK");
        String amount = decoder.get("PAYMENTINFO_0_AMT");
        String CORRELATIONID = decoder.get("CORRELATIONID");

        String errorCode = decoder.get("L_ERRORCODE0");
        String errorShortMessage = decoder.get("L_SHORTMESSAGE0");
        String errorLongMessage = decoder.get("L_LONGMESSAGE0");

        String merchantParam = null;
        CurrencyConverter currencyconverter = paymentDao.findLatestConversionRate(baseCurrencyCode, foreignCurrencyCode);
        Double coversion_rate = currencyconverter.getConversionRate();
        Double amount_in_rupee = NumberUtils.toDouble(amount) * coversion_rate;

        String paymentStatus = decoder.get("PAYMENTINFO_0_PAYMENTSTATUS");
        String pendingReason = decoder.get("PAYMENTINFO_0_PENDINGREASON");


        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        if (payment != null) {
            /*     comparing the dollar amount           */
            if (BaseUtils.doubleEquality(NumberUtils.toDouble(amount), (payment.getAmount() / coversion_rate))) {
                amount_in_rupee = payment.getAmount();
            }
        }

        try {
            // our own validations
            paymentManager.verifyPayment(gatewayOrderId, amount_in_rupee, merchantParam);
            // payment callback has been verified. now see if it is successful or failed from the gateway response
            if (ack != null && ack.equals(PayPalPaymentGatewayWrapper.Success_Ack) && paymentStatus.equals(PayPalPaymentGatewayWrapper.Payment_Completed_Status)) {
                paymentManager.success(gatewayOrderId);
                resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } else if (ack != null && ack.equals(PayPalPaymentGatewayWrapper.Success_Ack) && paymentStatus.equals(PayPalPaymentGatewayWrapper.Payment_Pending_Status) && pendingReason.equals(PayPalPaymentGatewayWrapper.Payment_Pending_Reason)) {
                paymentManager.pendingApproval(gatewayOrderId);
                resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } else {
                paymentManager.fail(gatewayOrderId);
                logger.error("sending to payment gateway paypal for gateway order id " + gatewayOrderId + "and amount " + amount_in_rupee + "and correlation id" + CORRELATIONID);
                logger.error("DoExpressCheckoutPayment Failed : Error Code : " + errorCode + " Error Short Message :" + errorShortMessage + " Error Long Message :" + errorLongMessage);
                resolution = new RedirectResolution(PaymentFailAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            }
        } catch (HealthkartPaymentGatewayException e) {
            paymentManager.error(gatewayOrderId, e);
            logger.error("DoExpressCheckoutPayment Failed : Error Code : " + errorCode + " Error Short Message :" + errorShortMessage + " Error Long Message :" + errorLongMessage);
            resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
        }
        return resolution;
    }

}


