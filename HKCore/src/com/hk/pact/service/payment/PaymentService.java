package com.hk.pact.service.payment;

import java.util.Date;
import java.util.List;

import com.hk.constants.catalog.product.EnumProductVariantPaymentType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;

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

    /**
     * Send payment emails and return true if emails sent successfully
     *
     * @param order
     * @return
     */
    public boolean sendPaymentEmailForOrder(Order order);
}
