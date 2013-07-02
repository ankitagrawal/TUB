package com.hk.impl.service.payment;

import com.hk.constants.catalog.product.EnumProductVariantPaymentType;
import com.hk.constants.payment.*;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.EmailManager;
import com.hk.manager.SMSManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.payment.PaymentStatusDao;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pojo.HkPaymentResponse;
import com.hk.service.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

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
    public List<Payment> searchPayments(Order order, List<PaymentStatus> paymentStatuses, String gatewayOrderId,
                                        List<PaymentMode> paymentModes, Date startCreateDate, Date endCreateDate,
                                        List<OrderStatus> orderStatuses, Payment parentPayment,List<Gateway> gatewayList) {
        return getPaymentDao().searchPayments(order, paymentStatuses, gatewayOrderId, paymentModes, startCreateDate, endCreateDate, orderStatuses,parentPayment,gatewayList);
    }

    @Override
    public List<HkPaymentResponse> seekPayment(String gatewayOrderId) throws HealthkartPaymentGatewayException {
        List<HkPaymentResponse> hkPaymentResponseList = null;
        Map<String, Object> faultyAmountMap = new HashMap<String, Object>();
        Payment basePayment = findByGatewayOrderId(gatewayOrderId);

        if (basePayment != null) {
            Gateway gateway = basePayment.getGateway();
            if (gateway != null && EnumGateway.getHKServiceEnabledGateways().contains(gateway.getId())) {
                HkPaymentService hkPaymentService = getHkPaymentService(gateway);
                if (hkPaymentService != null) {
                    try {

                        hkPaymentResponseList = hkPaymentService.seekPaymentFromGateway(basePayment);

                        List<Payment> hkPaymentRequestList = listPaymentFamily(gatewayOrderId);
                        //TODO: remove it later, stuffing gateway Order Id in refund objects
                        if (hkPaymentResponseList != null && hkPaymentRequestList != null) {
                            for (HkPaymentResponse resp : hkPaymentResponseList) {
                                for (Payment req : hkPaymentRequestList) {
                                    stuffGatewayOrderIdInRespObj(resp, req);
                                }
                            }
                        }

                        List<Map<String, Object>> requestResponseMappedList = mapRequestAndResponseObject(hkPaymentRequestList, hkPaymentResponseList);
                        //TODO: in case of ICICI stuff amount in response code
                        if(EnumGateway.ICICI.getId().equals(gateway.getId())){
                            stuffAmountInRespObj(requestResponseMappedList) ;
                        }

                        verifyRequestAndResponseList(requestResponseMappedList);
                        verifyAmountOfRequestAndResponseList(requestResponseMappedList, faultyAmountMap);

                    } catch (HealthkartPaymentGatewayException e) {
                        if (e.getError().equals(HealthkartPaymentGatewayException.Error.AMOUNT_MISMATCH)) {
                            emailManager.sendPaymentMisMatchMailToAdmin((Double) faultyAmountMap.get("RequestAmount"), (Double) faultyAmountMap.get("ResponseAmount"), (String) faultyAmountMap.get("GatewayOrderId"));
                        }
                        return hkPaymentResponseList;
                    }

                }
            }
        }
        return hkPaymentResponseList;
    }

    private void stuffAmountInRespObj(List<Map<String, Object>> requestResponseMappedList) {
        for(Map<String,Object> requestResponseMap : requestResponseMappedList){
            Payment request = (Payment) requestResponseMap.get("Request");
            HkPaymentResponse response = (HkPaymentResponse) requestResponseMap.get("Response");
            response.setAmount(request.getAmount());
        }
    }

    @Override
    public void updatePayment(String gatewayOrderId) throws HealthkartPaymentGatewayException {

        Payment basePayment = findByGatewayOrderId(gatewayOrderId);
        if (basePayment != null && basePayment.getGateway() != null && EnumGateway.getHKServiceEnabledGateways().contains(basePayment.getGateway().getId())) {
            try {
                if(EnumPaymentStatus.getUpdatePaymentStatusesIds().contains(basePayment.getPaymentStatus().getId())) {
                    List<HkPaymentResponse> hkPaymentResponseList = seekPayment(gatewayOrderId);
                    // handle the case of citrus here
                    if(EnumGateway.CITRUS.getId().equals(basePayment.getGateway().getId()) && !isCitrusResponseSuccessful(hkPaymentResponseList)){
                        HkPaymentService  hkPaymentService = getHkPaymentService(EnumGateway.ICICI.asGateway());
                        if(hkPaymentService != null){
                            hkPaymentResponseList = hkPaymentService.seekPaymentFromGateway(basePayment);
                        }
                    }

                    List<Payment> hkPaymentRequestList = listPaymentFamily(gatewayOrderId);
                    List<Map<String, Object>> requestResponseMappedList = mapRequestAndResponseObject(hkPaymentRequestList, hkPaymentResponseList);
                    verifyRequestAndResponseList(requestResponseMappedList);
                    updateRequestResponseList(requestResponseMappedList);
                }

            } catch (HealthkartPaymentGatewayException e) {
                logger.debug("Healthkart Payment Exception : " + e);
                getPaymentManager().error(gatewayOrderId, e);
            }
        }
    }


    private HkPaymentService getHkPaymentService(Gateway gateway) {
        HkPaymentService hkPaymentService = null;
        if(gateway != null && EnumGateway.getHKServiceEnabledGateways().contains(gateway.getId())){
            hkPaymentService = ServiceLocatorFactory.getBean(gateway.getName() + "Service", HkPaymentService.class);
        }
        return hkPaymentService;
    }

    @Override
    public void refundPayment(String gatewayOrderId, Double amount) throws HealthkartPaymentGatewayException {
        Double gatewayAmount = null;
        Payment basePayment = findByGatewayOrderId(gatewayOrderId);

        if (basePayment != null && basePayment.getGateway() != null && EnumGateway.getHKServiceEnabledGateways().contains(basePayment.getGateway().getId())) {
            if (EnumPaymentStatus.SUCCESS.getId().equals(basePayment.getPaymentStatus().getId())) {
                HkPaymentService hkPaymentService = getHkPaymentService(basePayment.getGateway());
                Payment refundRequestPayment = createNewRefundPayment(basePayment, EnumPaymentStatus.REFUND_REQUEST_IN_PROCESS.asPaymenStatus(), amount, EnumPaymentMode.ONLINE_PAYMENT.asPaymenMode());
                try {
                    HkPaymentResponse hkRefundPaymentResponse = hkPaymentService.refundPayment(basePayment, amount);
                    // handle the case of citrus
                    if (EnumGateway.CITRUS.getId().equals(basePayment.getGateway().getId()) && !isCitrusResponseSuccessful(hkRefundPaymentResponse)) {
                        hkPaymentService = getHkPaymentService(EnumGateway.ICICI.asGateway());
                        if (hkPaymentService != null) {
                            hkRefundPaymentResponse = hkPaymentService.refundPayment(basePayment, amount);
                        }
                    }

                    if (hkRefundPaymentResponse != null && hkRefundPaymentResponse.getHKPaymentStatus() != null
                            && EnumHKPaymentStatus.SUCCESS.getId().equals(hkRefundPaymentResponse.getHKPaymentStatus().getId())) {
                        gatewayAmount = hkRefundPaymentResponse.getAmount();

                        if (EnumGateway.ICICI.getId().equals(hkRefundPaymentResponse.getGateway().getId())) {
                            gatewayAmount = amount;
                        }
                        verifyPaymentAmount(gatewayAmount, amount);
                    }
                    updatePaymentBasedOnResponse(hkRefundPaymentResponse, refundRequestPayment);

                } catch (HealthkartPaymentGatewayException e) {
                    if (e.getError().equals(HealthkartPaymentGatewayException.Error.AMOUNT_MISMATCH)) {
                        emailManager.sendPaymentMisMatchMailToAdmin(amount, gatewayAmount, gatewayOrderId);
                    }
                    error(refundRequestPayment.getGatewayOrderId(), e);
                }
            }
        }
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


    private void verifyPaymentAmount(Double gatewayAmount, Double actualAmount) throws HealthkartPaymentGatewayException {
        if(!gatewayAmount.equals(actualAmount)){
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.AMOUNT_MISMATCH);
        }
    }


    private void updatePaymentBasedOnResponse(HkPaymentResponse hkPaymentResponse, Payment hkPaymentRequest) {
        if (hkPaymentResponse != null && hkPaymentRequest != null) {
            //TODO: Change it with HK payment status
            EnumHKPaymentStatus paymentStatus = hkPaymentResponse.getHKPaymentStatus();
            if (paymentStatus != null && EnumHKPaymentStatus.SUCCESS.getId().equals(paymentStatus.getId())) {
                success(hkPaymentRequest.getGatewayOrderId(), hkPaymentResponse.getGatewayReferenceId(), hkPaymentResponse.getRrn(), hkPaymentResponse.getAuthIdCode(), hkPaymentResponse.getResponseMsg());
            } else if (paymentStatus != null && EnumHKPaymentStatus.FAILURE.getId().equals(paymentStatus.getId())) {
                error(hkPaymentRequest.getGatewayOrderId(), hkPaymentResponse.getErrorLog());
            } else {
                inProcess(hkPaymentRequest.getGatewayOrderId(), hkPaymentResponse.getGatewayReferenceId(), hkPaymentResponse.getRrn(), hkPaymentResponse.getAuthIdCode(), hkPaymentResponse.getResponseMsg());
            }
        }
    }

    @Transactional
    private void success(String gatewayOrderId, String gatewayReferenceId, String rrn, String authIdCode, String respMsg) {
        Payment payment = findByGatewayOrderId(gatewayOrderId);
        if(payment != null){
            payment.setGatewayReferenceId(gatewayReferenceId);
            payment.setRrn(rrn);
            payment.setAuthIdCode(authIdCode);
            payment.setResponseMessage(respMsg);
            payment.setPaymentStatus(EnumPaymentStatus.REFUNDED.asPaymenStatus());
            Payment parent = payment.getParent();
            if(parent != null && payment.getAmount() != null){
                double refundAmount = parent.getRefundAmount() + payment.getAmount();
                parent.setRefundAmount(refundAmount);
            }
            save(payment);
        }
    }

    private void error(String gatewayOrderId, HealthkartPaymentGatewayException e){
        error(gatewayOrderId, e.getError().getMessage());
    }

    @Transactional
    private void error(String gatewayOrderId, String errorLog){
        Payment payment = findByGatewayOrderId(gatewayOrderId);
        if(payment != null){
            payment.setPaymentStatus(EnumPaymentStatus.ERROR.asPaymenStatus());
            payment.setErrorLog(errorLog);
            save(payment);
        }
    }

    @Transactional
    public void inProcess(String gatewayOrderId, String gatewayReferenceId, String rrn, String authIdCode, String respMsg) {
        Payment payment = findByGatewayOrderId(gatewayOrderId);
        if(payment != null){
            payment.setGatewayReferenceId(gatewayReferenceId);
            payment.setRrn(rrn);
            payment.setAuthIdCode(authIdCode);
            payment.setResponseMessage(respMsg);
            payment.setPaymentStatus(EnumPaymentStatus.REFUND_REQUEST_IN_PROCESS.asPaymenStatus());
            save(payment);
        }
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
            List<Payment> listOfBrotherPayments = getPaymentDao().searchPayments(null, null, null, null, null, null,null,basePayment,null);
            if(listOfBrotherPayments != null && !listOfBrotherPayments.isEmpty()){
                paymentFamilyList.addAll(listOfBrotherPayments);
            }
        }
        return paymentFamilyList;
    }

    @Override
    public boolean isRefundAmountValid(String gatewayOrderId, Double amount) {
        Payment payment = findByGatewayOrderId(gatewayOrderId);
        if (payment != null && payment.getAmount() != null) {
            double remainAmt = -1;
            if (payment.getRefundAmount() != null) {
                remainAmt = payment.getAmount() - (payment.getRefundAmount() + amount);
            } else {
                remainAmt = payment.getAmount() - amount;
            }
            if (remainAmt >= 0f) {
                return true;
            }
        }
        return false;
    }


    private List<Map<String, Object>> mapRequestAndResponseObject(List<Payment> hkPaymentRequestList, List<HkPaymentResponse> hkPaymentResponseList) {
        List<Map<String, Object>> requestRespList = new ArrayList<Map<String, Object>>();
        if(hkPaymentRequestList!=null && !hkPaymentRequestList.isEmpty() && hkPaymentResponseList != null && !hkPaymentResponseList.isEmpty()){
            for (HkPaymentResponse hkPaymentResponse : hkPaymentResponseList){
                for (Payment hkPaymentRequest : hkPaymentRequestList){

                    if(EnumPaymentTransactionType.SALE.getName().equalsIgnoreCase(hkPaymentResponse.getTransactionType())){
                        if(hkPaymentRequest.getGatewayOrderId().equalsIgnoreCase(hkPaymentResponse.getGatewayOrderId())){
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


    private void verifyRequestAndResponseList(List<Map<String, Object>> requestResponseMappedList) throws HealthkartPaymentGatewayException {
        if(requestResponseMappedList != null && !requestResponseMappedList.isEmpty()){
            for(Map<String,Object> reqResponseMap : requestResponseMappedList){
                Payment requestPayment = (Payment)reqResponseMap.get("Request");
                HkPaymentResponse hkPaymentResponse = (HkPaymentResponse) reqResponseMap.get("Response");
                verifyRequestAndResponse(requestPayment,hkPaymentResponse);
            }
        }
    }


    private void verifyRequestAndResponse(Payment request, HkPaymentResponse response) throws HealthkartPaymentGatewayException {
        if(request == null || response == null){
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.REQUEST_RESPONSE_INCONSISTENCY);
        }
    }



    private void verifyAmountOfRequestAndResponseList(List<Map<String, Object>> requestResponseMappedList, Map<String, Object> faultyMap) throws HealthkartPaymentGatewayException {
        if (requestResponseMappedList != null && !requestResponseMappedList.isEmpty()) {
            for (Map<String, Object> reqResponseMap : requestResponseMappedList) {
                Payment requestPayment = (Payment) reqResponseMap.get("Request");
                HkPaymentResponse hkPaymentResponse = (HkPaymentResponse) reqResponseMap.get("Response");
                verifyAmountOfRequestAndResponse(requestPayment, hkPaymentResponse, faultyMap);
            }
        }
    }


    private void verifyAmountOfRequestAndResponse(Payment request, HkPaymentResponse response,Map<String,Object> faultyAmountMap ) throws HealthkartPaymentGatewayException {
        if(request != null && response != null && response.getHKPaymentStatus() != null
                && EnumHKPaymentStatus.SUCCESS.getId().equals(response.getHKPaymentStatus().getId())){
            faultyAmountMap.put("RequestAmount", request.getAmount());
            faultyAmountMap.put("ResponseAmount",response.getAmount());
            faultyAmountMap.put("GatewayOrderId",request.getGatewayOrderId());
            verifyPaymentAmount(response.getAmount(),request.getAmount());
        }
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    /*
   * A utility for time being used to stuff refund gateway order id into response object
   * TODO: remove later if a better method is construed.
   * */
    private void stuffGatewayOrderIdInRespObj(HkPaymentResponse resp, Payment req) {
        if(resp != null && req != null){
            if(EnumPaymentTransactionType.REFUND.getName().equalsIgnoreCase(resp.getTransactionType())){
                String gatewayReferenceId = req.getGatewayReferenceId();
                String rrn = req.getRrn();
                if(rrn != null && gatewayReferenceId !=null && gatewayReferenceId.equalsIgnoreCase(resp.getGatewayReferenceId()) && rrn.equalsIgnoreCase(resp.getRrn())){
                    resp.setGatewayOrderId(req.getGatewayOrderId());
                }
            }
        }
    }


    private void updateRequestResponseList(List<Map<String, Object>> requestResponseMappedList)  {
        Payment requestPayment = null;
        HkPaymentResponse hkPaymentResponse = null;
        try{
            if(requestResponseMappedList != null && !requestResponseMappedList.isEmpty()){
                for(Map<String,Object> reqResponseMap : requestResponseMappedList){
                    requestPayment = (Payment)reqResponseMap.get("Request");
                    hkPaymentResponse = (HkPaymentResponse) reqResponseMap.get("Response");
                    updateRequestResponseMap(requestPayment, hkPaymentResponse);
                }
            }

        } catch (HealthkartPaymentGatewayException e){
            getPaymentManager().error(requestPayment.getGatewayOrderId(), e);
        }

    }

    private void updateRequestResponseMap(Payment requestPayment, HkPaymentResponse hkPaymentResponse) throws HealthkartPaymentGatewayException {

        if(requestPayment != null && hkPaymentResponse != null){
            EnumHKPaymentStatus gatewayPaymentStatus = hkPaymentResponse.getHKPaymentStatus();
            if(gatewayPaymentStatus != null && EnumHKPaymentStatus.SUCCESS.getId().equals(gatewayPaymentStatus.getId())){
                getPaymentManager().success(requestPayment.getGatewayOrderId(), hkPaymentResponse.getGatewayReferenceId(), hkPaymentResponse.getRrn(), hkPaymentResponse.getResponseMsg(), hkPaymentResponse.getAuthIdCode());
            } else if (gatewayPaymentStatus != null && EnumHKPaymentStatus.FAILURE.getId().equals(gatewayPaymentStatus.getId())){
                getPaymentManager().fail(requestPayment.getGatewayOrderId(), hkPaymentResponse.getGatewayReferenceId(), hkPaymentResponse.getResponseMsg());
            } else if (gatewayPaymentStatus != null && EnumHKPaymentStatus.AUTHENTICATION_PENDING.getId().equals(gatewayPaymentStatus.getId())){
                getPaymentManager().pendingApproval(requestPayment.getGatewayOrderId(), hkPaymentResponse.getGatewayReferenceId());
            }  else {
                throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
            }
        }

    }

    @Transactional
    private Payment createNewRefundPayment(Payment basePayment, PaymentStatus paymentStatus, Double amount, PaymentMode paymentMode){
        Payment refundPayment = getPaymentManager().createNewPayment(basePayment.getOrder(), paymentMode,
                basePayment.getIp(), basePayment.getGateway(), basePayment.getIssuer(), basePayment.getBillingAddress());

        refundPayment.setPaymentStatus(paymentStatus);
        refundPayment.setParent(basePayment);
        refundPayment.setAmount(amount);

        refundPayment = paymentDao.save(refundPayment);

        return refundPayment;
    }

    private boolean isCitrusResponseSuccessful(List<HkPaymentResponse> hkPaymentResponseList) {
        if (hkPaymentResponseList != null && !hkPaymentResponseList.isEmpty()) {
            for (HkPaymentResponse hkPaymentResponse : hkPaymentResponseList) {
                if (EnumPaymentTransactionType.SALE.getName().equalsIgnoreCase(hkPaymentResponse.getTransactionType())) {
                    return isCitrusResponseSuccessful(hkPaymentResponse);
                }
            }
        }
        return false;
    }

    private boolean isCitrusResponseSuccessful(HkPaymentResponse hkPaymentResponse) {
        if(hkPaymentResponse != null && EnumHKPaymentStatus.SUCCESS.getId().equals(hkPaymentResponse.getHKPaymentStatus().getId())){
            return true;
        }
        return false;
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

    public PaymentManager getPaymentManager() {
        return ServiceLocatorFactory.getBean(PaymentManager.class);
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
