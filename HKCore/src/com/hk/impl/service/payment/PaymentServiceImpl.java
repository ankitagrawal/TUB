package com.hk.impl.service.payment;

import java.util.*;

import com.hk.constants.payment.EnumGateway;
import com.hk.constants.payment.EnumPaymentTransactionType;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.payment.Gateway;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.SMSManager;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pojo.HkPaymentResponse;
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
    public List<Payment> searchPayments(Order order, List<PaymentStatus> paymentStatuses, String gatewayOrderId, List<PaymentMode> paymentModes, Date startCreateDate, Date endCreateDate, List<OrderStatus> orderStatuses, Payment parentPayment) {
        return getPaymentDao().searchPayments(order, paymentStatuses, gatewayOrderId, paymentModes, startCreateDate, endCreateDate, orderStatuses,parentPayment);
    }

    @Override
    public List<HkPaymentResponse> seekPayment(String gatewayOrderId) throws HealthkartPaymentGatewayException {
        List<HkPaymentResponse> hkPaymentResponseList = null;
        Payment basePayment = findByGatewayOrderId(gatewayOrderId);
        if(basePayment != null){
            Gateway gateway = basePayment.getGateway();
            HkPaymentService hkPaymentService = getHkPaymentService(gateway);

            if(hkPaymentService != null){
                hkPaymentResponseList = hkPaymentService.seekPaymentFromGateway(basePayment);
            }
        }
        return hkPaymentResponseList;
    }

    @Override
    public HkPaymentService getHkPaymentService(Gateway gateway) {
        if(gateway != null){
            return ServiceLocatorFactory.getBean(gateway.getName() + "Service", HkPaymentService.class);
        }
        return null;
    }

    @Override
    public HkPaymentResponse refundPayment(String gatewayOrderId, Double amount) throws HealthkartPaymentGatewayException {
        HkPaymentResponse hkPaymentResponse = null;
        Payment basePayment = findByGatewayOrderId(gatewayOrderId);
        if(basePayment != null){
            Gateway gateway = basePayment.getGateway();
            HkPaymentService hkPaymentService = getHkPaymentService(gateway);

            if(hkPaymentService != null){
                hkPaymentResponse = hkPaymentService.refundPayment(basePayment,amount);
            }
        }
        return hkPaymentResponse;
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
    public void verifyPaymentAmount(Double gatewayAmount, Double actualAmount) throws HealthkartPaymentGatewayException {  //TODO: change ==
        if(!gatewayAmount.equals(actualAmount)){
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.AMOUNT_MISMATCH);
        }
    }

    @Override
    public void sendPaymentMisMatchMailToAdmin(Double actualAmt, Double gatewayAmount, String gatewayOrderId) {
        if(gatewayOrderId != null){
            emailManager.sendPaymentMisMatchMailToAdmin(actualAmt,gatewayAmount,gatewayOrderId);
        }
    }

    @Override
    public boolean updatePaymentBasedOnResponse(HkPaymentResponse hkPaymentResponse, Payment hkPaymentRequest) {
        boolean isUpdated = false;
        if(hkPaymentResponse != null && hkPaymentResponse.getPaymentStatus().getId() == EnumPaymentStatus.REFUNDED.asPaymenStatus().getId()){
            isUpdated = updatePayment(hkPaymentRequest, hkPaymentResponse.getAmount(), hkPaymentResponse.getGatewayReferenceId(),
                           hkPaymentResponse.getRrn(), hkPaymentResponse.getAuthIdCode(), hkPaymentResponse.getResponseMsg(),EnumPaymentStatus.REFUNDED.asPaymenStatus(),null);
        }  else if(hkPaymentResponse != null && hkPaymentResponse.getPaymentStatus().getId() == EnumPaymentStatus.FAILURE.asPaymenStatus().getId()){
            isUpdated = updatePayment(hkPaymentRequest, hkPaymentResponse.getAmount(), hkPaymentResponse.getGatewayReferenceId(),
                    hkPaymentResponse.getRrn(), hkPaymentResponse.getAuthIdCode(), hkPaymentResponse.getResponseMsg(),EnumPaymentStatus.FAILURE.asPaymenStatus(),null);
        }  else if(hkPaymentResponse != null && hkPaymentResponse.getPaymentStatus().getId() == EnumPaymentStatus.REFUND_REQUEST_IN_PROCESS.asPaymenStatus().getId()){
            isUpdated = updatePayment(hkPaymentRequest, hkPaymentResponse.getAmount(), hkPaymentResponse.getGatewayReferenceId(),
                    hkPaymentResponse.getRrn(), hkPaymentResponse.getAuthIdCode(), hkPaymentResponse.getResponseMsg(),EnumPaymentStatus.REFUND_REQUEST_IN_PROCESS.asPaymenStatus(),null);
        }  else {
            isUpdated = updatePayment(hkPaymentRequest, hkPaymentResponse.getAmount(), hkPaymentResponse.getGatewayReferenceId(),
                    hkPaymentResponse.getRrn(), hkPaymentResponse.getAuthIdCode(), hkPaymentResponse.getResponseMsg(),EnumPaymentStatus.ERROR.asPaymenStatus(),hkPaymentResponse.getResponseMsg());
        }

        return isUpdated;
    }

    private boolean updatePayment(Payment hkPaymentRequest, Double amount, String gatewayReferenceId, String rrn, String authIdCode, String respMsg, PaymentStatus paymentStatus, String errorMsg) {
        boolean isUpdated = false;
        if(hkPaymentRequest != null){
            hkPaymentRequest.setAmount(amount);
            hkPaymentRequest.setGatewayReferenceId(gatewayReferenceId);
            hkPaymentRequest.setRrn(rrn);
            hkPaymentRequest.setAuthIdCode(authIdCode);
            hkPaymentRequest.setResponseMessage(respMsg);
            hkPaymentRequest.setPaymentStatus(paymentStatus);
            hkPaymentRequest.setErrorLog(errorMsg);
            save(hkPaymentRequest);
            isUpdated = true;
        }

        return isUpdated;
    }

    // TODO: Not needed now, check if needed later
    /*@Override
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
    }*/

    @Override
    public List<Payment> listPaymentFamily(String gatewayOrderId) {
        List<Payment> paymentFamilyList = null;
        Payment basePayment = findByGatewayOrderId(gatewayOrderId);
        if(basePayment != null){
            paymentFamilyList = new ArrayList<Payment>();
            paymentFamilyList.add(basePayment);

            // get all brothers related to base Payment
            List<Payment> listOfBrotherPayments = getPaymentDao().searchPayments(null, null, null, null, null, null,null,basePayment);
            if(listOfBrotherPayments != null && !listOfBrotherPayments.isEmpty()){
                paymentFamilyList.addAll(listOfBrotherPayments);
            }
        }
        return paymentFamilyList;
    }

    @Override
    public List<Map<String, Object>> mapRequestAndResponseObject(List<Payment> hkPaymentRequestList, List<HkPaymentResponse> hkPaymentResponseList) {
        List<Map<String, Object>> requestRespList = new ArrayList<Map<String, Object>>();
        if(hkPaymentRequestList!=null && !hkPaymentRequestList.isEmpty() && hkPaymentResponseList != null && !hkPaymentResponseList.isEmpty()){
            for (HkPaymentResponse hkPaymentResponse : hkPaymentResponseList){
                for (Payment hkPaymentRequest : hkPaymentRequestList){

                    if(EnumPaymentTransactionType.SALE.getName().equalsIgnoreCase(hkPaymentResponse.getTransactionType())){
                        if(hkPaymentRequest.getGatewayOrderId() == hkPaymentResponse.getGatewayOrderId()){
                            Map<String,Object> respRequestMap = new HashMap<String,Object>();
                            respRequestMap.put("Request",hkPaymentRequest);
                            respRequestMap.put("Response",hkPaymentResponse);
                            requestRespList.add(respRequestMap);
                        }
                    } /*else if(EnumPaymentTransactionType.REFUND.getName().equalsIgnoreCase(hkPaymentResponse.getTransactionType())){
                        if (hkPaymentRequest.getGatewayReferenceId() == hkPaymentResponse.getGatewayReferenceId() && hkPaymentRequest.getRrn() == hkPaymentResponse.getRrn()){
                            Map<String,Object> respRequestMap = new HashMap<String,Object>();
                            respRequestMap.put("Request",hkPaymentRequest);
                            respRequestMap.put("Response",hkPaymentResponse);
                            requestRespList.add(respRequestMap);
                        }
                    }*/  //TODO: Think a better method to map request and response object, not needed now
                }
            }
        }
        return requestRespList;
    }

    @Override
    public void verifyRequestAndResponseList(List<Map<String, Object>> requestResponseMappedList) throws HealthkartPaymentGatewayException {
        if(requestResponseMappedList != null && !requestResponseMappedList.isEmpty()){
            for(Map<String,Object> reqResponseMap : requestResponseMappedList){
                Payment requestPayment = (Payment)reqResponseMap.get("Request");
                HkPaymentResponse hkPaymentResponse = (HkPaymentResponse) reqResponseMap.get("Response");
                verifyRequestAndResponse(requestPayment,hkPaymentResponse);
            }
        }
    }

    @Override
    public void verifyRequestAndResponse(Payment request, HkPaymentResponse response) throws HealthkartPaymentGatewayException {
        if(request == null || response == null){
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.REQUEST_RESPONSE_INCONSISTENCY);
        }
    }


    @Override
    public Map<String,Object> verifyAmountOfRequestAndResponseList(List<Map<String, Object>> requestResponseMappedList) throws HealthkartPaymentGatewayException {
        Map<String,Object> faultyAmountMap=null;
        if(requestResponseMappedList != null && !requestResponseMappedList.isEmpty()){
            for(Map<String,Object> reqResponseMap : requestResponseMappedList){
                Payment requestPayment = (Payment)reqResponseMap.get("Request");
                HkPaymentResponse hkPaymentResponse = (HkPaymentResponse) reqResponseMap.get("Response");
                faultyAmountMap = verifyAmountOfRequestAndResponse(requestPayment,hkPaymentResponse);
            }
        }
        return faultyAmountMap;
    }

    @Override
    public Map<String,Object> verifyAmountOfRequestAndResponse(Payment request, HkPaymentResponse response) throws HealthkartPaymentGatewayException {
        Map<String,Object> faultyAmountMap=null;
        if(request != null && response != null){
            faultyAmountMap = new HashMap<String,Object>();
            faultyAmountMap.put("RequestAmount", request.getAmount());
            faultyAmountMap.put("ResponseAmount",response.getAmount());
            faultyAmountMap.put("GatewayOrderId",request.getGatewayOrderId());
            verifyPaymentAmount(response.getAmount(),request.getAmount());
        }
        return faultyAmountMap;
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
