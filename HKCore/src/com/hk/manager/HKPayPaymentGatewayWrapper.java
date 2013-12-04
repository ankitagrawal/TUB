package com.hk.manager;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.akube.framework.service.PaymentGatewayWrapper;
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

    public String getGatewayUrl() {
        return url;
    }

    @Override
    public void setGatewayUrl(String url) {
        this.url = url;
    }

}
