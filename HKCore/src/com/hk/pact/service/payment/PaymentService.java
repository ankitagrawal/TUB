package com.hk.pact.service.payment;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hk.constants.catalog.product.EnumProductVariantPaymentType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.pojo.HkPaymentResponse;

public interface PaymentService {

    public List<Payment> listByOrderId(Long orderId);

    public Payment save(Payment payment);

    public Payment findByGatewayOrderId(String gatewayOrderId);

    public PaymentMode findPaymentMode(EnumPaymentMode paymentMode);

    public PaymentMode findPaymentMode(Long paymentModeId);

    public PaymentStatus findPaymentStatus(EnumPaymentStatus enumPaymentStatus);

    public ProductVariantPaymentType findVariantPaymentType(EnumProductVariantPaymentType variantPaymentType);

    public List<PaymentStatus> listWorkingPaymentStatuses();

    public List<PaymentStatus> listActionablePaymentStatuses();

    public List<PaymentStatus> listSuccessfulPaymentStatuses();

    public List<PaymentMode> listWorkingPaymentModes();

    public List<Payment> searchPayments(Order order, List<PaymentStatus> paymentStatuses, String gatewayOrderId, List<PaymentMode> paymentModes, Date startCreateDate, Date endCreateDate, List<OrderStatus> orderStatuses);

    public List<HkPaymentResponse> seekPayment(String gatewayOrderId) throws HealthkartPaymentGatewayException;// returns a non-persistable Payment object, created by what we get from gateway

    public HkPaymentService getHkPaymentService(Gateway gateway);

    public HkPaymentResponse refundPayment(String gatewayOrderId, Double amount) throws HealthkartPaymentGatewayException;
    /**
     * Send payment emails and return true if emails sent successfully
     *
     * @param order
     * @return
     */
    public boolean sendPaymentEmailForOrder(Order order);

    public Payment findByGatewayReferenceIdAndRrn(String gatewayReferenceId, String rrn);

    public void verifyPaymentAmount(Double gatewayAmount, Double actualAmount) throws HealthkartPaymentGatewayException;

    public void sendPaymentMisMatchMailToAdmin(Double actualAmt, Double gatewayAmount, String gatewayOrderIdForFaultyPayments);

    public void verifyPaymentStatus(PaymentStatus gatewayPaymentStatus, PaymentStatus paymentStatus) throws HealthkartPaymentGatewayException;

    public void sendInValidPaymentStatusChangeToAdmin(PaymentStatus gatewayPaymentStatus, PaymentStatus paymentStatus, String gatewayOrderIdForFaultyPayments);

    public boolean updatePaymentBasedOnResponse(HkPaymentResponse gatewayPayment, Payment actualPayment);

    public List<Payment> findByBasePayment(Payment basePayment);

    public void verifyIfRefundAmountValid(List<Payment> paymentList, Double amount) throws HealthkartPaymentGatewayException;

    public List<Payment> listPaymentFamily(String gatewayOrderId);

    public void verifyHkRequestAndResponse(List<Payment> hkPaymentRequestList, List<HkPaymentResponse> hkPaymentResponseList) throws HealthkartPaymentGatewayException;

    public boolean updatePaymentFamily(List<Payment> hkPaymentRequestList, List<HkPaymentResponse> hkPaymentResponseList);

    public List<Map<String,Object>> mapRequestAndResponseObject(List<Payment> hkPaymentRequestList, List<HkPaymentResponse> hkPaymentResponseList);

    public void verifyForConsistencyOfRequestAndResponseList(List<Map<String,Object>> requestResponseMappedList) throws HealthkartPaymentGatewayException;

    public void verifyForConsistencyOfRequestAndResponse(Payment request, HkPaymentResponse response) throws HealthkartPaymentGatewayException;

    public Map<String,Object> verifyForAmountConsistencyOfRequestAndResponseList(List<Map<String,Object>> requestResponseMappedList) throws HealthkartPaymentGatewayException;

    public Map<String,Object> verifyForAmountConsistencyOfRequestAndResponse(Payment request, HkPaymentResponse response) throws HealthkartPaymentGatewayException;
}
