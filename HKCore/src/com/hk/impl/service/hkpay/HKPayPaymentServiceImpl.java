package com.hk.impl.service.hkpay;

import com.hk.domain.payment.Payment;
import com.hk.dto.payment.PaymentEnquiry;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pojo.HkPaymentResponse;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Arrays;
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
            request.setHttpMethod("POST");

            //Things to try

            //1
            request.body(MediaType.APPLICATION_FORM_URLENCODED_TYPE, "gatewayOrderId=2554-1&accountId=31188");

            //2
            request.getFormParameters().add("gatewayOrderId", "2554-1");
            request.getFormParameters().add("accountId", "31188");

            //3
            MultivaluedMap<String,String> formData= new MultivaluedMapImpl<String,String>();
            formData.put("gatewayOrderId", Arrays.asList("2554-1"));
            formData.put("accountId", Arrays.asList("31188"));

//            request.header("content-type",MediaType.APPLICATION_FORM_URLENCODED_TYPE);

            request.getFormParameters().add("gatewayOrderId", "2554-1");
            request.getFormParameters().add("accountId", "31188");
//            request.body(MediaType.APPLICATION_FORM_URLENCODED_TYPE, "gatewayOrderId=2554-1&accountId=31188");
//            request.getFormParameters().add("gatewayOrderId", basePayment.getGatewayOrderId());
//            request.getFormParameters().add("accountId", accountId);
//            request.body(MediaType.APPLICATION_FORM_URLENCODED,formData);

//            ClientResponse<PaymentEnquiry> response = request.post(PaymentEnquiry.class);
//            request.getQueryParameters().add("authToken", "US3jbSEN5EKVVzlabDl95loyWf_hloCZ");



            ClientResponse<String> response = request.post(String.class);
            int status = response.getStatus();

            logger.info("response status " + status);

            if (status == 200) {
//                PaymentEnquiry output = response.getEntity();
                int i = 0;
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
