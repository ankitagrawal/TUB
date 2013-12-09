package com.hk.impl.service.hkpay;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.HKPayPaymentGatewayWrapper;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pojo.HkPaymentResponse;
import com.hk.web.AppConstants;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

/*
 * User: Pratham
 * Date: 12/1/13  Time: 10:51 PM
*/
@Service("HKPayService")
public class HKPayPaymentServiceImpl implements HkPaymentService {
    private Properties properties = BaseUtils.getPropertyFile(AppConstants.getAppClasspathRootPath() + "/hkPay.live.properties");
    private static Logger logger = LoggerFactory.getLogger(HKPayPaymentServiceImpl.class);

    // Seek Payment constants
    public static final String gatewayOrderId = "gatewayOrderId";
    public static final String accountId = "accountId";

    @Override
    public List<HkPaymentResponse> seekPaymentFromGateway(Payment basePayment) throws HealthkartPaymentGatewayException {
        try {
            String postUrl = properties.getProperty("secureHKPaySearch");
            ClientRequest request = new ClientRequest(postUrl);
            request.setHttpMethod("POST");
            request.getFormParameters().add(gatewayOrderId, basePayment.getGatewayOrderId());
            String accountId = properties.getProperty("accountId");
            request.getFormParameters().add(accountId, accountId);

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
