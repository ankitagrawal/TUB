package com.hk.manager;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
public class HKPayPaymentGatewayWrapper extends BasePaymentGatewayWrapper<HKPayPaymentGatewayWrapper> implements PaymentGatewayWrapper {

    private static Logger logger = LoggerFactory.getLogger(HKPayPaymentGatewayWrapper.class);

    String url;
    public static final String address = "address";
    public static final String city = "city";
    public static final String state = "state";
    public static final String phone = "phone";
    public static final String postal_code = "postal_code";
    public static final String name = "name";
    public static final String email = "email";
    public static final String return_url = "return_url";
    public static final String account_id = "account_id";
    public static final String merchantTransactionId = "merchantTransactionId";
    public static final String reference_no = "reference_no";
    public static final String description = "description";
    public static final String secure_hash = "secure_hash";
    public static final String amount = "amount";
    public static final String country = "country";
    public static final String payment_option = "payment_option";


    public String getGatewayUrl() {
        return url;
    }

    @Override
    public void setGatewayUrl(String url) {
        this.url = url;
    }

}
