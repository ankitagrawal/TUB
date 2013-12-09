package com.hk.impl.service.hkpay;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.Keys;
import com.hk.constants.payment.GatewayResponseKeys;
import com.hk.domain.payment.Payment;
import com.hk.dto.payment.PaymentEnquiry;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pojo.HkPaymentResponse;
import com.hk.web.AppConstants;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/*
 * User: Pratham
 * Date: 12/1/13  Time: 10:51 PM
*/
@Service("HKPayService")
public class HKPayPaymentServiceImpl implements HkPaymentService {
    //    @Value("#{hkPayProps['" + GatewayResponseKeys.HKPayConstants.secureHKPay + "']}")
//    String postUrl;
//
//    @Value("#{hkPayProps['" + GatewayResponseKeys.HKPayConstants.accountId + "']}")
//    String accountId;
//    private static final String HKPAY_LIVE_PROPERTIES = "/hkPay.live.properties";

    private static Logger logger = LoggerFactory.getLogger(HKPayPaymentServiceImpl.class);

    @Override
    public List<HkPaymentResponse> seekPaymentFromGateway(Payment basePayment) throws HealthkartPaymentGatewayException {
//        String propertyLocatorFileLocation = AppConstants.getAppClasspathRootPath() + HKPAY_LIVE_PROPERTIES;
//        Properties properties = BaseUtils.getPropertyFile(propertyLocatorFileLocation);

        try {
//            String postUrl = (String) properties.get(properties.get(GatewayResponseKeys.HKPayConstants.secureHKPay));
            String postUrl = "http://stag.securepay.healthkart.com/rest/api/payment/search";
            ClientRequest request = new ClientRequest(postUrl);
            request.setHttpMethod("POST");
            request.getFormParameters().add("gatewayOrderId", basePayment.getGatewayOrderId());
//            String accountId = (String) properties.get(properties.get(GatewayResponseKeys.HKPayConstants.accountId));
            String accountId = "10258";
            request.getFormParameters().add("accountId", accountId);

            ClientResponse<String> response = request.post(String.class);

            int status = response.getStatus();
            logger.info("response status " + status);

            if (status == 200) {
                String output = response.getEntity();
                //parse using gson
//               PaymentEnquiry paymentEnquiry =  (PaymentEnquiry) response.getEntity();
            }

        } catch (Exception e) {
            logger.error("Catching Exception in hkpay", e);
            return null;
        }

        return null;
    }

    @Override
    public HkPaymentResponse refundPayment(Payment basePayment, Double amount) throws HealthkartPaymentGatewayException {
        return null;
    }
}
