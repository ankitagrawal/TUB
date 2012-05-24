package com.hk.impl.service.payment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.catalog.product.EnumProductVariantPaymentType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.payment.PaymentStatusDao;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private EmailManager     emailManager;
    @Autowired
    private PaymentStatusDao paymentStatusDao;
    @Autowired
    private PaymentModeDao   paymentModeDao;
    @Autowired
    private OrderService     orderService;
    @Autowired
    private PaymentDao       paymentDao;

    public List<Payment> listByOrderId(Long orderId) {
        return getPaymentDao().listByOrderId(orderId);
    }

    public PaymentMode findPaymentMode(EnumPaymentMode paymentMode) {
        return getPaymentModeDao().getPaymentModeById(paymentMode.getId());
    }

    public PaymentMode findPaymentMode(Long paymentModeId) {
        return getPaymentModeDao().getPaymentModeById(paymentModeId);
    }

    public PaymentStatus findPaymentStatus(EnumPaymentStatus enumPaymentStatus) {
        return getPaymentStatusDao().getPaymentStatusById(enumPaymentStatus.getId());
    }

    public Payment save(Payment payment) {
        return getPaymentDao().save(payment);
    }

    public Payment findByGatewayOrderId(String gatewayOrderId) {
        return getPaymentDao().findByGatewayOrderId(gatewayOrderId);
    }

    public ProductVariantPaymentType findVariantPaymentType(EnumProductVariantPaymentType variantPaymentType) {
        return getPaymentModeDao().getVariantPaymentTypeById(variantPaymentType.getId());
    }

    public List<PaymentStatus> listWorkingPaymentStatuses() {
        return getPaymentStatusDao().listWorkingPaymentStatuses();
    }

    public List<PaymentMode> listWorkingPaymentModes() {
        return getPaymentModeDao().listWorkingPaymentModes();
    }

    /**
     * Send payment emails and return true if emails sent successfully
     * 
     * @param order
     * @return
     */
    public boolean sendPaymentEmailForOrder(Order order) {
        boolean paymentEmailSent = false;
        Payment payment = order.getPayment();
        EnumPaymentMode enumPaymentMode = EnumPaymentMode.getPaymentModeFromId(payment.getPaymentMode().getId());

        switch (enumPaymentMode) {
            case CITRUS:
            case TECHPROCESS:
            case CCAVENUE_DUMMY:
            case FREE_CHECKOUT:
                if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.getId())) {
                    paymentEmailSent = getEmailManager().sendOrderConfirmEmailToUser(order);
                    getOrderService().sendEmailToServiceProvidersForOrder(order);
                } else if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId())) {
                    paymentEmailSent = getEmailManager().sendOrderPlacedPaymentPendingEmailToUser(order);
                }
                break;
            case COD:
                paymentEmailSent = getEmailManager().sendOrderPlacedCodEmailToUser(order);
                break;

            case NEFT:
            case CashDeposit:
            case ChequeDeposit:
                paymentEmailSent = getEmailManager().sendOrderPlacedOtherPaymentModeEmailToUser(order);
        }

        return paymentEmailSent;
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public PaymentStatusDao getPaymentStatusDao() {
        return paymentStatusDao;
    }

    public void setPaymentStatusDao(PaymentStatusDao paymentStatusDao) {
        this.paymentStatusDao = paymentStatusDao;
    }

    public PaymentModeDao getPaymentModeDao() {
        return paymentModeDao;
    }

    public void setPaymentModeDao(PaymentModeDao paymentModeDao) {
        this.paymentModeDao = paymentModeDao;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public PaymentDao getPaymentDao() {
        return paymentDao;
    }

    public void setPaymentDao(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

}
