package com.hk.manager.payment;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.manager.LinkManager;
import com.hk.pact.service.order.OrderService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 6/18/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class EbsPaymentGatewayWrapper extends BasePaymentGatewayWrapper<EbsPaymentGatewayWrapper> implements PaymentGatewayWrapper {

    private static Logger logger = LoggerFactory.getLogger(EbsPaymentGatewayWrapper.class);


    //request paramters
    public static final String secret_key = "secret_key";
    public static final String secure_hash = "secure_hash";
    public static final String account_id = "account_id";
    public static final String mode = "mode";
    public static final String reqResParameter = "DR";
    public static final String address = "address";
    public static final String city = "city";
    public static final String state = "state";
    public static final String phone = "phone";
    public static final String postal_code = "postal_code";
    public static final String name = "name";
    public static final String email = "email";
    public static final String return_url = "return_url";
    public static final String secure_hash_decrypted = "secure_hash_decrypted";
    public static final String amount = "amount";
    public static final String country = "country";


    // response paramters
    public static String ResponseCode = "ResponseCode";
    public static String ResponseMessage = "ResponseMessage";
    public static String DateCreated = "DateCreated";
    public static String PaymentID = "PaymentID";
    public static String MerchantRefNo = "MerchantRefNo";
    public static String Amount = "Amount";
    public static String Mode = "Mode";
    public static String BillingName = "BillingName";
    public static String BillingAddress = "BillingAddress";
    public static String BillingCity = "BillingCity";
    public static String BillingState = "BillingState";
    public static String BillingPostalCode = "BillingPostalCode";
    public static String BillingCountry = "BillingCountry";
    public static String BillingPhone = "BillingPhone";
    public static String BillingEmail = "BillingEmail";
    public static String IsFlagged = "IsFlagged";

    public static String is_Flagged_True = "YES";
    public static String is_Flagged_False = "NO";

    public static String TransactionID = "TransactionID";

    public static String reference_no = "reference_no";
    public static String description = "description";

    public static String authStatus_Success = "0";

    public String getGatewayUrl() {
        return "https://secure.ebs.in/pg/ma/sale/pay";
    }

    @Override
    public void setGatewayUrl(String url) {
    }


    public static Map<String, String> parseResponse(String recvString) {
        Map<String, String> paramMap = new HashMap<String, String>();
        StringTokenizer st = new StringTokenizer(recvString, "=&");
        String field, val;
        while (st.hasMoreTokens()) {
            field = st.nextToken();
            val = st.nextToken();
            paramMap.put(field, val);
            logger.info("field " + field + "value " + val);
        }
        return paramMap;
    }
}
