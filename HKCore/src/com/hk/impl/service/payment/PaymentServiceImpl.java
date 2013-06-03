package com.hk.impl.service.payment;

import java.util.Date;
import java.util.List;

import com.hk.constants.payment.EnumGateway;
import com.hk.constants.payment.EnumPaymentTransactionType;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.payment.Gateway;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.SMSManager;
import com.hk.pact.service.payment.HkPaymentService;
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
import com.hk.service.ServiceLocatorFactory;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private EmailManager     emailManager;
    @Autowired
    private PaymentStatusDao paymentStatusDao;
    @Autowired
    private PaymentModeDao   paymentModeDao;
    // @Autowired
    private OrderService     orderService;
    @Autowired
    private PaymentDao       paymentDao;
	@Autowired
	SMSManager smsManager;

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

    public List<PaymentStatus> listActionablePaymentStatuses() {
        return getPaymentStatusDao().listActionablePaymentStatuses();
    }

    @Override
    public List<PaymentStatus> listSuccessfulPaymentStatuses() {
        return getPaymentStatusDao().listSuccessfulPaymentStatuses();
    }

    public List<PaymentMode> listWorkingPaymentModes() {
        return getPaymentModeDao().listWorkingPaymentModes();
    }

    @Override
    public List<Payment> searchPayments(Order order, List<PaymentStatus> paymentStatuses, String gatewayOrderId, List<PaymentMode> paymentModes, Date startCreateDate, Date endCreateDate, List<OrderStatus> orderStatuses) {
        return getPaymentModeDao().searchPayments(order, paymentStatuses, gatewayOrderId, paymentModes, startCreateDate, endCreateDate, orderStatuses);
    }

    @Override
    public List<Payment> seekPayment(String gatewayOrderId) throws HealthkartPaymentGatewayException {

        Payment basePayment = findByGatewayOrderId(gatewayOrderId);
        if(basePayment != null){
            Gateway gateway = basePayment.getGateway();
            HkPaymentService hkPaymentService = getHkPaymentService(gateway);

            if(hkPaymentService != null){
                return hkPaymentService.seekPaymentFromGateway(basePayment);
            }
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HkPaymentService getHkPaymentService(Gateway gateway) {
        if(gateway != null){
            return ServiceLocatorFactory.getBean(gateway.getName() + "Service", HkPaymentService.class);
        }
        return null;
    }

    @Override
    public Payment refundPayment(String gatewayOrderId, Double amount) throws HealthkartPaymentGatewayException {
        Payment basePayment = findByGatewayOrderId(gatewayOrderId);
        if(basePayment != null){
            Gateway gateway = basePayment.getGateway();
            HkPaymentService hkPaymentService = getHkPaymentService(gateway);

            if(hkPaymentService != null){
                return hkPaymentService.refundPayment(basePayment,amount);
            }
        }
        return null;
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
            case ONLINE_PAYMENT:
            case SUBSCRIPTION_PAYMENT:
            case FREE_CHECKOUT:
                if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.getId())) {
                    paymentEmailSent = getEmailManager().sendOrderConfirmEmailToUser(order);
                    getOrderService().sendEmailToServiceProvidersForOrder(order);
                } else if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId())) {
                    paymentEmailSent = getEmailManager().sendOrderPlacedPaymentPendingEmailToUser(order);
                }
                break;
            case COD:
                if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.ON_DELIVERY.getId())) {
                  paymentEmailSent = getEmailManager().sendOrderPlacedAuthorizedCodEmailToUser(order);
                } else {
                  paymentEmailSent = getEmailManager().sendOrderPlacedCodEmailToUser(order);
                }
                break;

            case NEFT:
            case CashDeposit:
            case ChequeDeposit:
                paymentEmailSent = getEmailManager().sendOrderPlacedOtherPaymentModeEmailToUser(order);
        }

        return paymentEmailSent;
    }

    @Override
    public Payment findByGatewayReferenceIdAndRrn(String gatewayReferenceId, String rrn) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void verifyPaymentAmount(Double gatewayAmount, Double actualAmount) throws HealthkartPaymentGatewayException {
        if(!gatewayAmount.equals(actualAmount)){
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.AMOUNT_MISMATCH);
        }
    }

    @Override
    public void sendPaymentMisMatchMailToAdmin(Double actualAmt, Double gatewayAmount, String gatewayOrderIdForFaultyPayments) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void verifyPaymentStatus(PaymentStatus gatewayPaymentStatus, PaymentStatus paymentStatus) throws HealthkartPaymentGatewayException {
        if(!(gatewayPaymentStatus!= null && paymentStatus!= null && gatewayPaymentStatus.equals(paymentStatus))){
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_STATUS_CHANGE);
        }
    }

    @Override
    public void sendInValidPaymentStatusChangeToAdmin(PaymentStatus gatewayPaymentStatus, PaymentStatus paymentStatus, String gatewayOrderIdForFaultyPayments) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean updatePayment(Payment gatewayPayment, Payment actualPayment) {
        boolean isUpdated = false;
        if(actualPayment != null && gatewayPayment != null){
            actualPayment.setAmount(gatewayPayment.getAmount());
            actualPayment.setGatewayReferenceId(gatewayPayment.getGatewayReferenceId());
            actualPayment.setRrn(gatewayPayment.getRrn());
            actualPayment.setAuthIdCode(gatewayPayment.getAuthIdCode());
            actualPayment.setResponseMessage(gatewayPayment.getResponseMessage());
            actualPayment.setPaymentStatus(gatewayPayment.getPaymentStatus());

            save(actualPayment);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public List<Payment> findByBasePayment(Payment basePayment) {
        Gateway gateway = basePayment.getGateway();
        if(gateway!=null && EnumGateway.CITRUS.getId().equals(gateway.getId())){
            return getPaymentDao().listByRRN(basePayment.getRrn());
        } else if(gateway != null && EnumGateway.EBS.getId().equals(gateway.getId())){
            return null;
        }
        return null;
    }

    @Override
    public void verifyIfRefundAmountValid(List<Payment> paymentList, Double amount) throws HealthkartPaymentGatewayException {
        Double totalAmount = null;
        for(Payment payment : paymentList){
            //TODO: change comparison with id
            if(EnumPaymentTransactionType.REFUND.getName().equalsIgnoreCase(payment.getTransactionType())){
                totalAmount -= payment.getAmount();
            } else {
                totalAmount += payment.getAmount();
            }
        }

        if (totalAmount < amount){
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_REFUND_AMOUNT);
        }
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
        if (orderService == null) {
            this.orderService = ServiceLocatorFactory.getService(OrderService.class);
        }
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
