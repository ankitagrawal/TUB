package com.hk.pact.service.payment;

import com.hk.domain.payment.Payment;

import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Shakti Singh
 * Date: 5/22/13
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HkPaymentService {
    public Map<String, Object> seekHkPaymentResponse(String gatewayOrderId);

    public Payment updatePayment(String gatewayOrderId);
}
