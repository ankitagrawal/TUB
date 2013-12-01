package com.hk.manager.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;
import com.akube.framework.util.BaseUtils;
import com.ecs.epg.sfa.java.EPGCryptLib;
import com.ecs.epg.sfa.java.EPGMerchantEncryptionLib;
import com.hk.exception.HealthkartPaymentGatewayException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import net.sourceforge.stripes.action.ActionBeanContext;

public class CitrusPaymentGatewayWrapper extends BasePaymentGatewayWrapper<CitrusPaymentGatewayWrapper> implements PaymentGatewayWrapper {

    private static Logger logger = LoggerFactory.getLogger(CitrusPaymentGatewayWrapper.class);

    private String url;
    private String environmemtDir;
    public static String param_data = "DATA";
    public static String RespCode = "RespCode";
    public static String Amount = "Amount";
    public static String Message = "Message";
    public static String AuthIdCode = "AuthIdCode";
    public static String authIdCode = "authIdCode";
    public static String TxnDateTime = "TxnDateTime";
    public static String RRN = "RRN";
    public static String TxnID = "TxnID";
    public static String ePGTxnID = "ePGTxnID";
    public static String key = "key";

    public static String TxId = "TxId";
    public static String TxRefNo = "TxRefNo";
    public static String pgTxnNo = "pgTxnNo";
    public static String TxStatus = "TxStatus";
    public static String pgRespCode = "pgRespCode";
    public static String TxMsg = "TxMsg";
    public static String mandatoryErrorMsg = "mandatoryErrorMsg";
    public static String paidTxnExists = "paidTxnExists";


    public static String addressStreet1 = "addressStreet1";
    public static String addressCity = "addressCity";
    public static String addressZip = "addressZip";
    public static String addressState = "addressState";
    public static String addressCountry = "addressCountry";
    public static String email = "email";
    public static String merchantTxnId = "merchantTxnId";
    public static String orderAmount = "orderAmount";
    public static String amount = "amount";
    public static String CurrCode = "CurrCode";
    public static String currency = "currency";
    public static String firstName = "firstName";
    public static String lastName = "lastName";
    public static String phoneNumber = "phoneNumber";
    public static String returnUrl = "returnUrl";
    public static String reqtime = "reqtime";
    public static String merchantURLPart = "merchantURLPart";
    public static String vanityURLPart = "healthkart";
    public static String secSignature = "secSignature";
    public static String merchantAccessKey = "merchantAccessKey";
    public static String transactionId = "transactionId";
    public static String MerchantId = "MerchantId";
    public static String issuerCode = "issuerCode";
    public static String customer = "customer";
    public static String card = "card";
    public static String issuerRefNo = "issuerRefNo" ;
    public static String signature = "signature";
    public static String vanityUrl = "vanity_url";


    public CitrusPaymentGatewayWrapper(String environmemtDir) {
        this.environmemtDir = environmemtDir;
    }

    public String getGatewayUrl() {
        return url;
    }

    public void setGatewayUrl(String url) {
        this.url = url;
    }

    public String getEnvironmemtDir() {
        return environmemtDir;
    }

    public static String validateEncryptedData(String responseData, String propertyLocatorFileLocation) {
        EPGMerchantEncryptionLib oEncryptionLib = new EPGMerchantEncryptionLib();
        String clearData = null;
        try {
            Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
            String merchantId = properties.getProperty("MerchantId");
            String strModulus = properties.getProperty("modulus");

            if (strModulus == null) {
                throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.KEY_NOT_FOUND);
            }
            strModulus = decryptMerchantKey(strModulus, merchantId);
            if (strModulus == null) {
                throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
            }
            String strExponent = properties.getProperty("exponent");
            if (strExponent == null) {
                throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.KEY_NOT_FOUND);
            }
            strExponent = decryptMerchantKey(strExponent, merchantId);
            if (strExponent == null) {
                throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
            }
            clearData = oEncryptionLib.decryptDataWithPrivateKeyContents(responseData, strModulus, strExponent);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("transaction could not be processed " + e);
        }
        return clearData;
    }

    public static String decryptMerchantKey(String astrData, String merchantId) throws Exception {
        return (decryptData(astrData, (merchantId + merchantId).substring(0, 16)));
    }

    public static String decryptData(String strData, String strKey) throws Exception {
        if (strData == null || StringUtils.isEmpty(strData)) {
            return null;
        }
        if (strKey == null || StringUtils.isEmpty(strKey)) {
            return null;
        }
        EPGCryptLib moEPGCryptLib = new EPGCryptLib();
        return moEPGCryptLib.Decrypt1(strKey, strData);
    }

    public static Map<String, String> parseResponse(String data, String responseMethod) {
        Map<String, String> paramMap = new HashMap<String, String>();
        StringTokenizer stringTokenizer = new StringTokenizer(data, "&");
        if (responseMethod.equalsIgnoreCase("POST")) {
            while (stringTokenizer.hasMoreElements()) {
                String strData = (String) stringTokenizer.nextElement();
                StringTokenizer object = new StringTokenizer(strData, "=");
                String key = (String) object.nextElement();
                String value = (String) object.nextElement();
                paramMap.put(key, value);
                logger.info("key " + key + "value " + value);
            }
        }
        return paramMap;
    }


    public static String getRequestSignatureText(ActionBeanContext context) {
        String gatewayOrderId = context.getRequest().getParameter(CitrusPaymentGatewayWrapper.TxId);
        String pgTxnId = context.getRequest().getParameter(CitrusPaymentGatewayWrapper.pgTxnNo);
        String issuerRefNo = context.getRequest().getParameter(CitrusPaymentGatewayWrapper.issuerRefNo);
        String authIdCode = context.getRequest().getParameter(CitrusPaymentGatewayWrapper.authIdCode);
        String firstName = context.getRequest().getParameter(CitrusPaymentGatewayWrapper.firstName);
        String lastName = context.getRequest().getParameter(CitrusPaymentGatewayWrapper.lastName);
        String TxStatus = context.getRequest().getParameter(CitrusPaymentGatewayWrapper.TxStatus);
        String zipCode = context.getRequest().getParameter(CitrusPaymentGatewayWrapper.addressZip);
        String pgRespCode = context.getRequest().getParameter(CitrusPaymentGatewayWrapper.pgRespCode);
        String amount = context.getRequest().getParameter(CitrusPaymentGatewayWrapper.amount);

        String data = "";
        if (gatewayOrderId != null) {
            data += gatewayOrderId;
        }
        if (TxStatus != null) {
            data += TxStatus;
        }
        if (amount != null) {
            data += amount;
        }
        if (pgTxnId != null) {
            data += pgTxnId;
        }
        if (issuerRefNo != null) {
            data += issuerRefNo;
        }
        if (authIdCode != null) {
            data += authIdCode;
        }
        if (firstName != null) {
            data += firstName;
        }
        if (lastName != null) {
            data += lastName;
        }
        if (pgRespCode != null) {
            data += pgRespCode;
        }
        if (zipCode != null) {
            data += zipCode;
        }
        return data;
    }
}
