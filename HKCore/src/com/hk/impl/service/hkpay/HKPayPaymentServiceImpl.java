package com.hk.impl.service.hkpay;

import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pojo.HkPaymentResponse;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * User: Pratham
 * Date: 12/1/13  Time: 10:51 PM
*/
@Service("HKPayService")
public class HKPayPaymentServiceImpl implements HkPaymentService {

    private static Logger logger = LoggerFactory.getLogger(HKPayPaymentServiceImpl.class);

    String baseUrl = "http://stag.securepay.healthkart.com";
    String postUrl = baseUrl + "/rest/api/payment/search";
    String accountId = "10258";

    @Override
    public List<HkPaymentResponse> seekPaymentFromGateway(Payment basePayment) throws HealthkartPaymentGatewayException {

        try {

            ClientRequest request = new ClientRequest(postUrl);
//            request.getQueryParameters().add("authToken", "US3jbSEN5EKVVzlabDl95loyWf_hloCZ");
            request.getQueryParameters().add("gatewayOrderId", basePayment.getGatewayOrderId());
            request.getQueryParameters().add("accountId", accountId);
            request.setHttpMethod("POST");
            ClientResponse<String> response = request.post();
            int status = response.getStatus();

            logger.info("response status " + status);

            if (status == 200) {
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
