package com.hk.manager.payment;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;
import com.akube.framework.util.BaseUtils;
import com.opus.epg.sfa.java.EPGCryptLib;
import com.opus.epg.sfa.java.EPGMerchantEncryptionLib;
import com.opus.epg.sfa.java.SFAApplicationException;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 11/3/12
 * Time: 1:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class IciciPaymentGatewayWrapper extends BasePaymentGatewayWrapper<IciciPaymentGatewayWrapper> implements PaymentGatewayWrapper {

    private static Logger logger = LoggerFactory.getLogger(IciciPaymentGatewayWrapper.class);

    private String url;
    private String environmemtDir;
    public static String param_data = "DATA";
    public static String RespCode = "RespCode";
    public static String Amount = "Amount";
    public static String Message = "Message";
    public static String AuthIdCode = "AuthIdCode";
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


    public static String email = "email";
    public static String merchantTxnId = "merchantTxnId";
    public static String orderAmount = "orderAmount";
    public static String amount = "amount";
    public static String CurrCode = "CurrCode";
    public static String currency = "currency";
    public static String firstName = "firstName";
    public static String secSignature = "secSignature";
    public static String merchantAccessKey = "merchantAccessKey";
    public static String transactionId = "transactionId";


    public IciciPaymentGatewayWrapper(String environmemtDir) {
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

    String respcd = null;
    String respmsg = null;
    String MerchantTxnId = null;
    String TxnRefNo = null;
    String FDMSScore = null;
    String FDMSResult = null;


    public static String validateEncryptedData(String astrResponseData, String propertyLocatorFileLocation) throws Exception {
        EPGMerchantEncryptionLib oEncryptionLib = new EPGMerchantEncryptionLib();
        String astrClearData = null;
        try {
            Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);
            String merchantId = properties.getProperty("MerchantId");
            String strModulus = properties.getProperty("modulus");
            String strExponent = properties.getProperty("exponent");
            if (strModulus == null) {
                throw new SFAApplicationException("Invalid credentials. Transaction cannot be processed");
            }
            strModulus = decryptMerchantKey(strModulus, merchantId);
            if (strModulus == null) {
                throw new SFAApplicationException("Invalid credentials. Transaction cannot be processed");
            }
            if (strExponent == null) {
                throw new SFAApplicationException("Invalid credentials. Transaction cannot be processed");
            }
            strExponent = decryptMerchantKey(strExponent, merchantId);
            if (strExponent == null) {
                throw new SFAApplicationException("Invalid credentials. Transaction cannot be processed");
            }
            astrClearData = oEncryptionLib.decryptDataWithPrivateKeyContents(astrResponseData, strModulus, strExponent);

        } catch (Exception oEx) {
            oEx.printStackTrace();
        } finally {
            return astrClearData;
        }
    }

    public static String decryptMerchantKey(String astrData, String astrMerchantId) throws Exception {
        return (decryptData(astrData, (astrMerchantId + astrMerchantId).substring(0, 16)));
    }


    public static String decryptData(String strData, String strKey) throws Exception {
        if (strData == null || StringUtils.isEmpty(strData)) {
            return null;
        }
        if (strKey == null || StringUtils.isEmpty(strKey)) {
            return null;
        }
        EPGCryptLib moEPGCryptLib = new EPGCryptLib();
        return moEPGCryptLib.Decrypt(strKey, strData);
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


}
