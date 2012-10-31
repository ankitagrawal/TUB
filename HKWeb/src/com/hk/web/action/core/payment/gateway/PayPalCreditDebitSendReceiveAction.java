package com.hk.web.action.core.payment.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.manager.payment.PayPalPaymentGatewayWrapper;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.web.AppConstants;
import com.hk.web.action.core.payment.PaymentSuccessAction;
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
import com.paypal.sdk.services.NVPCallerServices;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.util.Util;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.profiles.APIProfile;

import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
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
        String amountStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(data.getAmount());
        amountStr = "10.10";
        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/paypal.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String userid = properties.getProperty(PayPalPaymentGatewayWrapper.USER);
        String pwd = properties.getProperty(PayPalPaymentGatewayWrapper.PWD);
        String mode = properties.getProperty(PayPalPaymentGatewayWrapper.MODE);
        String signature = properties.getProperty(PayPalPaymentGatewayWrapper.SIGNATURE);
        String Version = properties.getProperty(PayPalPaymentGatewayWrapper.VERSION);
        String paymentAction = properties.getProperty(PayPalPaymentGatewayWrapper.PAYMENTACTION);
        String method = properties.getProperty(PayPalPaymentGatewayWrapper.METHOD);

        PayPalPaymentGatewayWrapper payPalPaymentGatewayWrapper = new PayPalPaymentGatewayWrapper();

        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.SIGNATURE, signature);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.USER, userid);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.PWD, pwd);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.AMT, amountStr);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.VERSION, Version);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.RETURNURL, linkManager.getPayPalPaymentGatewayReturnUrl());
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.CANCELURL, linkManager.getPayPalPaymentGatewayCancelUrl());
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.PAYMENTACTION, paymentAction);
        payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.METHOD, method);
//        payPalPaymentGatewayWrapper.setGatewayUrl(properties.getProperty(payPalPaymentGatewayWrapper.merchantURLPart));

        NVPEncoder encoder = new NVPEncoder();
        NVPDecoder decoder = new NVPDecoder();

        try {


    /*
            //NVPCallerServiced object is taken from the session
            NVPCallerServices caller = (NVPCallerServices) WebContext.getRequest().getSession().getValue("caller");

            String returnURL = linkManager.getPayPalPaymentGatewayReturnUrl() + "?currencyCodeType=" + "USD";
            String cancelURL = linkManager.getPayPalPaymentGatewayReturnUrl() + "?paymentType=" + "order";

            String strNVPRequest = "";
            StringBuffer sbErrorMessages = new StringBuffer("");

            //NVPEncoder object is created and all the name value pairs are loaded into it.
            NVPEncoder encoder = new NVPEncoder();

            encoder.add("METHOD", "SetExpressCheckout");
            encoder.add("RETURNURL", returnURL + "&paymentAmount=" + "19.85");
            encoder.add("CANCELURL", cancelURL);

            //encoder.add("AMT",request.getParameter("paymentAmount"));
            String paymentType = "Order";
            String currencyCodeType = "USD";
            encoder.add("PAYMENTACTION", paymentType);
            encoder.add("CURRENCYCODE", currencyCodeType);

            encoder.add("SHIPTONAME", "NAME");
            encoder.add("SHIPTOSTREET", "SHIPTOSTREET");
            encoder.add("SHIPTOCITY", "SHIPTOCITY");
            encoder.add("SHIPTOSTATE", "SHIPTOSTATE");
            encoder.add("SHIPTOCOUNTRY", "SHIPTOCOUNTRYCODE");
            encoder.add("SHIPTOZIP", "SHIPTOZIP");
            encoder.add("L_NAME0", "L_NAME0");
            encoder.add("L_NUMBER0", "1000");
            encoder.add("L_DESC0", "Size: 8.8-oz");
            encoder.add("L_AMT0", "10.01");
            encoder.add("L_QTY0", "1");
            encoder.add("L_NAME1", "L_NAME1");
            encoder.add("L_NUMBER1", "10001");
            encoder.add("L_DESC1", "Size: Two 24-piece boxes");
            encoder.add("L_AMT1", "10.02");
            encoder.add("L_QTY1", "1");
            encoder.add("L_ITEMWEIGHTVALUE1", "0.5");
            encoder.add("L_ITEMWEIGHTUNIT1", "lbs");
            encoder.add("ADDROVERRIDE", "1");

            float ft = 11.85F;

            encoder.add("ITEMAMT", String.valueOf(ft));
            encoder.add("TAXAMT", "2.00");
            //amount = itemamount+ shippingamt+shippingdisc+taxamt+insuranceamount;
            float amt = Util.round(ft + 5.00f + 2.00f + 1.00f, 2);
            float maxamt = Util.round(amt + 25.00f, 2);
            encoder.add("SHIPDISCAMT", "-3.00");
            encoder.add("AMT", "11.85");
            encoder.add("SHIPPINGAMT", "8.00");
            encoder.add("MAXAMT", String.valueOf(maxamt));
            encoder.add("CALLBACK", "https://www.ppcallback.com/callback.pl");
            encoder.add("INSURANCEOPTIONOFFERED", "true");
            encoder.add("INSURANCEAMT", "1.00");
            encoder.add("L_SHIPPINGOPTIONISDEFAULT0", "false");
            encoder.add("L_SHIPPINGOPTIONNAME0", "Ground");
            encoder.add("L_SHIPPINGOPTIONLABEL0", "UPS Ground 7 Days");
            encoder.add("L_SHIPPINGOPTIONAMOUNT0", "3.00");
            encoder.add("L_SHIPPINGOPTIONISDEFAULT1", "true");
            encoder.add("L_SHIPPINGOPTIONNAME1", "UPS Air");
            encoder.add("L_SHIPPINGOPTIONlABEL1", "UPS Next Day Air");
            encoder.add("L_SHIPPINGOPTIONAMOUNT1", "8.00");
            encoder.add("CALLBACKTIMEOUT", "4");

            WebContext.getRequest().getSession().setAttribute("paymentType", paymentType);
            WebContext.getRequest().getSession().setAttribute("currencyCodeType", currencyCodeType);

            String testEnv = "beta-developer";

            //encode method will encode the name and value and form NVP string for the request
            strNVPRequest = encoder.encode();

            //call method will send the request to the server and return the response NVPString
            String ppresponse =
                    (String) caller.call(strNVPRequest);

            //NVPDecoder object is created
            NVPDecoder resultValues = new NVPDecoder();

            //decode method of NVPDecoder will parse the request and decode the name and value pair
            resultValues.decode(ppresponse);

            //checks for Acknowledgement and redirects accordingly to display error messages
            String strAck = resultValues.get("ACK");
            if (strAck != null && !(strAck.equals("Success") || strAck.equals("SuccessWithWarning"))) {
                WebContext.getRequest().getSession().setAttribute("response", resultValues);
                paymentManager.fail(payment.getGatewayOrderId());
            } else {
                payPalPaymentGatewayWrapper.setGatewayUrl("https://www." + testEnv + ".paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + resultValues.get("TOKEN"));
            }

            */


          NVPCallerServices   caller = new NVPCallerServices();
			APIProfile profile = ProfileFactory.createSignatureAPIProfile();

            profile.setAPIUsername(userid);
			profile.setAPIPassword(pwd);
			profile.setSignature(signature);
			profile.setEnvironment("sandbox");
			profile.setSubject("");
			caller.setAPIProfile(profile);
			encoder.add("VERSION", "51.0");
			encoder.add("METHOD","SetExpressCheckout");

		// Add request-specific fields to the request string.
			encoder.add("RETURNURL",linkManager.getPayPalPaymentGatewayReturnUrl());
			encoder.add("CANCELURL",linkManager.getPayPalPaymentGatewayCancelUrl());
			encoder.add("AMT","10.10");
			encoder.add("PAYMENTACTION","sale");
			encoder.add("CURRENCYCODE","USD");

		// Execute the API operation and obtain the response.
			String NVPRequest= encoder.encode();
			String NVPResponse =caller.call(NVPRequest);
			decoder.decode(NVPResponse);


        } catch (Exception e) {
            logger.info("exception", e);
            paymentManager.fail(payment.getGatewayOrderId());
        }


            String ack = decoder.get("ACK");
            String Token = decoder.get("TOKEN");
               String CORRELATIONID = decoder.get("CORRELATIONID");
//            payPalPaymentGatewayWrapper.Ack = ack;
//            payPalPaymentGatewayWrapper.Token = Token;

             payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.ACK ,ack);
             payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.TOKEN ,Token);
             payPalPaymentGatewayWrapper.addParameter(PayPalPaymentGatewayWrapper.CORRELATIONID ,CORRELATIONID);
        /*
            if (ack.equals("Success")) {
                String paypalurl = " https://www.sandbox.paypal.com/cgi-bin/webscr? cmd=_express-checkout&token=" + Token;
//                getContext().getResponse().sendRedirect(paypalurl);
                HttpServletResponse httpResponse = (HttpServletResponse)  getContext().getResponse();
                httpResponse.sendRedirect(paypalurl);

            }
        }catch(Exception e1){
                logger.info("exception", e1);
            }

          */
            return payPalPaymentGatewayWrapper;
    }


    @DefaultHandler
    public Resolution callback() {
       NVPEncoder encoder = new NVPEncoder();
        NVPDecoder decoder = new NVPDecoder();

          String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + "/paypal.live.properties";
        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
        String userid = properties.getProperty(PayPalPaymentGatewayWrapper.USER);
        String pwd = properties.getProperty(PayPalPaymentGatewayWrapper.PWD);
        String mode = properties.getProperty(PayPalPaymentGatewayWrapper.MODE);
        String signature = properties.getProperty(PayPalPaymentGatewayWrapper.SIGNATURE);
        String Version = properties.getProperty(PayPalPaymentGatewayWrapper.VERSION);
        String paymentAction = properties.getProperty(PayPalPaymentGatewayWrapper.PAYMENTACTION);
      

        String Token = getContext().getRequest().getParameter("token");
        String payerid = getContext().getRequest().getParameter("PayerID");
       try {
        NVPCallerServices   caller = new NVPCallerServices();
                   APIProfile profile = ProfileFactory.createSignatureAPIProfile();

                   profile.setAPIUsername(userid);
                   profile.setAPIPassword(pwd);
                   profile.setSignature(signature);
                   profile.setEnvironment("sandbox");
                   profile.setSubject("");
                   caller.setAPIProfile(profile);
                   encoder.add("VERSION", "51.0");
                   encoder.add("METHOD","DoExpressCheckoutPayment");

                   encoder.add("TOKEN",Token);
			encoder.add("PAYERID",payerid);
			encoder.add("AMT","11.20");
			encoder.add("PAYMENTACTION","sale");
			encoder.add("CURRENCYCODE","USD");

           String NVPRequest = encoder.encode();
			String NVPResponse =caller.call(NVPRequest);
			decoder.decode(NVPResponse);

       }catch(Exception e){
           
       }
       String ack =  decoder.get("ACK");

        if (ack.equals("Success")){
         new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", "88-67897");
        }
        return null;
    }
}
