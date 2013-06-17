package com.hk.pact.service.payment;

import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.pojo.HkPaymentResponse;

import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 5/22/13
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HkPaymentService {
    /*public Map<String, Object> seekHkPaymentResponse(String gatewayOrderId);*/

    /*public Payment updatePayment(String gatewayOrderId);*/

    public List<HkPaymentResponse> seekPaymentFromGateway(Payment basePayment) throws HealthkartPaymentGatewayException;

    public HkPaymentResponse refundPayment(Payment basePayment, Double amount) throws HealthkartPaymentGatewayException;
}
