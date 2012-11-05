package com.hk.manager.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Oct 30, 2012
 * Time: 1:32:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class PayPalPaymentGatewayWrapper  extends BasePaymentGatewayWrapper<PayPalPaymentGatewayWrapper> implements PaymentGatewayWrapper {

    private static Logger logger = LoggerFactory.getLogger(PayPalPaymentGatewayWrapper.class);

   private String url;
//request paramters
public static final String USER  = "USER";
public static final String PWD = "PWD";
public static final String SIGNATURE  = "SIGNATURE";
public static final String VERSION = "VERSION";
public static final String AMT = "AMT";
public static final String RETURNURL = "RETURNURL";
public static final String CANCELURL = "CANCELURL";
public static final String MODE = "MODE";
public static final String PAYMENTACTION = "PAYMENTACTION";
public static String merchantURLPart = "merchantURLPart";
public static final String  DoExpressPaymentMethod = "DoExpressPaymentMethod";
public static final String  setExpressMethod = "SetExpressMethod";
public static final String merchantTxnId = "merchantTxnId";
public static final String  ENVIRONMENT   = "ENVIRONMENT" ;
public static final String  baseCurrencyCode = "baseCurrencyCode";
public static final String  foreignCurrencyCode = "foreignCurrencyCode";
public static final String  currencyCode = "currencyCode";


//   response parameters

//public static final String Amount  = "Amount";
public static final String TOKEN = "TOKEN";
public static final String CORRELATIONID =  "CORRELATIONID";
public static final String ACK =  "ACK";


    public String getGatewayUrl() {
		return url;
	}


    public void setGatewayUrl(String url) {
		this.url = url;
	}

}
