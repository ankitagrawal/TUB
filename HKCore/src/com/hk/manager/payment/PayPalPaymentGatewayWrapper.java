package com.hk.manager.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;
import com.hk.domain.user.BillingAddress;
import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.core.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Oct 30, 2012
 * Time: 1:32:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class PayPalPaymentGatewayWrapper extends BasePaymentGatewayWrapper<PayPalPaymentGatewayWrapper> implements PaymentGatewayWrapper {

    private static Logger logger = LoggerFactory.getLogger(PayPalPaymentGatewayWrapper.class);

    private String url;
    //request paramters
    public static final String USER = "USER";
    public static final String PWD = "PWD";
    public static final String SIGNATURE = "SIGNATURE";
    public static final String VERSION = "VERSION";
    public static final String AMT = "AMT";
    public static final String RETURNURL = "RETURNURL";
    public static final String CANCELURL = "CANCELURL";
    public static final String MODE = "MODE";
    public static final String PAYMENTACTION = "PAYMENTACTION";
    public static String merchantURLPart = "merchantURLPart";
    public static final String DoExpressPaymentMethod = "DoExpressPaymentMethod";
    public static final String setExpressMethod = "SetExpressMethod";
    public static final String merchantTxnId = "merchantTxnId";
    public static final String ENVIRONMENT = "ENVIRONMENT";
    public static final String baseCurrencyCode = "baseCurrencyCode";
    public static final String foreignCurrencyCode = "foreignCurrencyCode";
    public static final String currencyCode = "currencyCode";
    public static final String gatewayUrl = "gatewayUrl";


//   response parameters

    //public static final String Amount  = "Amount";
    public static final String TOKEN = "TOKEN";
    public static final String CORRELATIONID = "CORRELATIONID";
    public static final String ACK = "ACK";
    public static String Success_Ack = "Success";
    public static String Success_with_Warning_Ack = "SuccessWithWarning";
    public static String Payment_Completed_Status = "Completed";
    public static String Payment_Pending_Status = "Pending";
    public static String Payment_Pending_Reason = "echeck";


    public String getGatewayUrl() {
        return url;
    }


    public void setGatewayUrl(String url) {
        this.url = url;
    }


    public APIProfile createPaypalApiProfile(String userid, String pwd, String signature, String environment) {
        APIProfile profile = null;
        try {
            profile = ProfileFactory.createSignatureAPIProfile();
            profile.setAPIUsername(userid);
            profile.setAPIPassword(pwd);
            profile.setSignature(signature);
            profile.setEnvironment(environment);
            profile.setSubject("");
        }
        catch (Exception e) {
            logger.error("exception at creating API signature :", e);
        }
        return profile;
    }


    public NVPEncoder createPayPalBasicEncodedRequest(String version, String method, String paymentAction, String currencyCode, String amount) {
        NVPEncoder encoder = new NVPEncoder();
        encoder.add("VERSION", version);
        encoder.add("METHOD", method);
        encoder.add("PAYMENTREQUEST_0_PAYMENTACTION", paymentAction);
        encoder.add("PAYMENTREQUEST_0_CURRENCYCODE", currencyCode);
        encoder.add("PAYMENTREQUEST_0_AMT", amount);
        return encoder;
    }


    public NVPEncoder encodeRequestForDoExpressPayment(NVPEncoder encoder, String Token, String payerid) {
        encoder.add("TOKEN", Token);
        encoder.add("PAYERID", payerid);
        return encoder;
    }


    public NVPEncoder encodeRequestForSetExpressCheckout(NVPEncoder encoder, String return_url, String cancel_url, User user, BillingAddress address, String merchantTxnId, String amount , Country country) {
        encoder.add("RETURNURL", return_url);
        encoder.add("CANCELURL", cancel_url);
        encoder.add("NOSHIPPING", "1");
        encoder.add("SOLUTIONTYPE", "Sole");

        encoder.add("PAYMENTREQUEST_0_INVNUM", merchantTxnId);
        encoder.add("L_PAYMENTREQUEST_0_DESC0", "HealthKart");
        encoder.add("L_PAYMENTREQUEST_0_AMT0", amount);
//        encoder.add("CUSTOMERSERVICENUMBER", "011-8887777");
        encoder.add("BRANDNAME", "HEALTHKART");
//            encoder.add("ADDROVERRIDE","1");

        if (address != null) {
            encoder.add("PAYMENTREQUEST_0_SHIPTONAME", address.getName());
            encoder.add("PAYMENTREQUEST_0_SHIPTONAME2", "HK");

            encoder.add("PAYMENTREQUEST_0_SHIPTOSTREET", address.getLine1());
            if (address.getLine2() != null && !address.getLine2().isEmpty()) {
                encoder.add("PAYMENTREQUEST_0_SHIPTOSTREET2", address.getLine2());
            }
            encoder.add("PAYMENTREQUEST_0_SHIPTOCITY", address.getCity());
            encoder.add("PAYMENTREQUEST_0_SHIPTOSTATE", address.getState());
//      Countrycode need to be discuss
            encoder.add("PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE", country.getCountryCode());
            encoder.add("PAYMENTREQUEST_0_SHIPTOZIP", address.getPin());
            encoder.add("EMAIL", user.getEmail());
            encoder.add("PAYMENTREQUEST_0_SHIPTOPHONENUM", address.getPhone());
        }
        return encoder;
    }

}
