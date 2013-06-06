package com.hk.manager.payment;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.EnumUserCodCalling;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.payment.EnumPaymentTransactionType;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.Issuer;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.BillingAddress;
import com.hk.domain.user.UserCodCall;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.impl.service.codbridge.OrderEventPublisher;
import com.hk.manager.*;
import com.hk.pact.dao.payment.PaymentDao;
import com.hk.pact.dao.payment.PaymentStatusDao;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pojo.HkPaymentResponse;
import com.hk.service.ServiceLocatorFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.hk.util.TokenUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: Kani Date: Jan 3, 2009
 */
@Component
@SuppressWarnings("unused")
public class PaymentManager {
    private static Logger logger = LoggerFactory.getLogger(PaymentManager.class);

    private final Double codCharges = 0D;

    @Autowired
    private OrderManager orderManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RewardPointService rewardPointService;
    @Autowired
    private ReferrerProgramManager referrerProgramManager;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    SMSManager smsManager;
    @Autowired
    OrderEventPublisher orderEventPublisher;
    @Autowired
    EmailManager emailManager;
    @Autowired
    private ShippingOrderService shippingOrderService;

    @Value("#{hkEnvProps['" + Keys.Env.cashBackLimit + "']}")
    private Double cashBackLimit;
    @Value("#{hkEnvProps['" + Keys.Env.maxCODCallCount + "']}")
    private int maxCODCallCount;
    @Value("#{hkEnvProps['" + Keys.Env.defaultGateway + "']}")
    private Long defaultGateway;

    @Autowired
    private PaymentDao paymentDao;
    @Autowired
    private PaymentStatusDao paymentStatusDao;

    // TODO: rewrite

    /**
     * This method will throw an {@link com.hk.exception.HealthkartPaymentGatewayException} if the payment request
     * cannot be verified
     *
     * @param gatewayOrderId
     * @param amount
     * @param merchantParam
     * @throws com.hk.exception.HealthkartPaymentGatewayException
     *
     */
    public void verifyPayment(String gatewayOrderId, Double amount, String merchantParam) throws HealthkartPaymentGatewayException {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        if (payment == null) {
            logger.info("Payment not found with gateway order id {}", gatewayOrderId);
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.PAYMENT_NOT_FOUND);
        }

        logger.info("Verifying payment : gateway amount = " + amount + ", db amount = " + payment.getAmount());
        if (!BaseUtils.doubleEquality(amount, payment.getAmount())) {
            logger.info("Payment amount mismatch! Failing.");
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.AMOUNT_MISMATCH);
        }

        String checksum = getOrderChecksum(payment.getOrder());
        if (StringUtils.isBlank(merchantParam)) {
            // we're doing this because the merchant param that we get from the gateway may or may not be there
            // depending on the gateway we're going to use.
            // in gateways where such a facility is not availble, we simply want to check that the current checksum
            // matches the checksum that was generated just before going to the gateway
            merchantParam = checksum;
        }

        // Hence, the first condition is always false if merchantParam is blank
        // otherwise that is also validated

        // noinspection ConstantConditions
        if (!merchantParam.equals(checksum) || !checksum.equals(payment.getPaymentChecksum())) {
            logger.info("checksum mismatch: merchantParam = {}, checksum = {}, payment.getPaymentChecksum = " + payment.getPaymentChecksum(), merchantParam, checksum);
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.CHECKSUM_MISMATCH);
        }

        // all seems well, if we've come this far. the request seems to be an authentic one
        // now we can check if this is a double payment by mistake
        if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId())
                || payment.getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.getId())) {
            // a payment is either successful or the payment is awaiting authorization means that this is a double
            // payment.
            logger.info("Seems like a double payment attempt. (or a page refresh)");
            throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.DOUBLE_PAYMENT);
        }
    }

    /**
     * @param order
     * @param paymentMode
     * @param remoteAddr
     * @param gateway
     * @param issuer         @return
     * @param billingAddress
     */
    public Payment createNewPayment(Order order, PaymentMode paymentMode, String remoteAddr, Gateway gateway, Issuer issuer, BillingAddress billingAddress) {
        Payment payment = new Payment();
        payment.setAmount(order.getAmount());
        payment.setOrder(order);
        payment.setPaymentMode(paymentMode);
        payment.setIp(remoteAddr);
        payment.setGateway(gateway);
        payment.setIssuer(issuer);

        payment.setBillingAddress(billingAddress);

        // these two fields must be generated
        // gateway order id is the order id passed to the payment gateway. this can be
        // a string made up of our order id, the payment id, or a random string (unique)
        payment.setGatewayOrderId(getUniqueGatewayOrderId(order));

        // checksum is based on the items in the cart. this must be generated again when
        // the payment gateway callback returns control back to us. This is used to make sure that
        // the users do not alter the shopping cart when a payment is being made.
        payment.setPaymentChecksum(getOrderChecksum(order));

        payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.REQUEST));

        payment = paymentDao.save(payment);
        return payment;
    }

    public static String getUniqueGatewayOrderId(Order order) {
        return TokenUtils.generateGatewayOrderId(order);
        /*
                   * Set<Payment> payments = order.getPayments(); if(payments != null){ for (Payment payment : payments) {
                   * if(payment.getGatewayOrderId().equals(gatewayOrderId)){ return TokenUtils.generateGatewayOrderId(order); } } }
                   * return gatewayOrderId;
                   */
    }

    /**
     * Calculates the order checksum based on the line items. It should be roughly unique for different carts. Roughly
     * unique will do.
     *
     * @param order
     * @return
     */
    public static String getOrderChecksum(Order order) {
        // lets simply do an md5 checksum of line item ids + scaffold id's + qty's
        StringBuffer checksumString = new StringBuffer();
        Set<CartLineItem> cartLineItems = order.getCartLineItems();

        // TODO: # warehouse fix this.

        // Collections.sort(cartLineItems);

        for (CartLineItem lineItem : cartLineItems) {
            if (lineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId())) {
                if (lineItem.getProductVariant() != null) {
                    checksumString.append(lineItem.getId()).append(lineItem.getProductVariant().getId()).append(lineItem.getQty());
                } else if (lineItem.getComboInstance().getCombo() != null) {
                    // PSXYZ
                    checksumString.append(lineItem.getId()).append(lineItem.getComboInstance().getCombo()).append(lineItem.getQty());
                }
            }
        }
        // also add the address id to the checksum as pricing may depend on that
        checksumString.append(order.getAddress().getId());

        return BaseUtils.getMD5Checksum(checksumString.toString());
    }

    public Order associateToOrder(String gatewayOrderId) {
        return associateToOrder(gatewayOrderId, null);
    }

    @Transactional
    public Order associateToOrder(String gatewayOrderId, String gatewayReferenceId) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);

        Order order = null;
        // if payment type is full, then send order to processing also, else just accept and update payment status
        if (payment != null) {
            if (StringUtils.isBlank(gatewayReferenceId)) {
                gatewayReferenceId = gatewayOrderId;
            }
            payment.setGatewayReferenceId(gatewayReferenceId);
            payment = paymentDao.save(payment);

            // if order already has a payment associated, simply update the association
            if (payment.getOrder().getPayment() != null) {
                payment.getOrder().setPayment(payment);
                getOrderService().save(payment.getOrder());
            } else {
                order = getOrderManager().orderPaymentReceieved(payment);
            }
        }
        return order;
    }

    public Order success(String gatewayOrderId) {
        return success(gatewayOrderId, null);
    }

    @Transactional
    public Order success(String gatewayOrderId, String gatewayReferenceId, String rrn, String responseMessage, String authIdCode) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);

        Order order = null;
        // if payment type is full, then send order to processing also, else just accept and update payment status
        if (payment != null) {
            if (payment.getPaymentDate() == null) {
                payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            }
            payment.setGatewayReferenceId(gatewayReferenceId);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.SUCCESS));
            payment.setResponseMessage(responseMessage);
            payment.setAuthIdCode(authIdCode);
            payment.setRrn(rrn);
            payment = paymentDao.save(payment);
            order = getOrderManager().orderPaymentReceieved(payment);
            /*Notify To JMS for payment success , to discard user who was eligible for Effort Bpo PaymentFailureCall */
            notifyPaymentSuccess(order);
        }
        return order;
    }

    @Transactional
    public Order success(String gatewayOrderId, String gatewayReferenceId) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);

        Order order = null;
        // if payment type is full, then send order to processing also, else just accept and update payment status
        if (payment != null) {
            if (payment.getPaymentDate() == null) {
                payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            }
            if (StringUtils.isBlank(gatewayReferenceId)) {
                gatewayReferenceId = gatewayOrderId;
            }
            payment.setGatewayReferenceId(gatewayReferenceId);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.SUCCESS));
            payment = paymentDao.save(payment);
            order = getOrderManager().orderPaymentReceieved(payment);
            /*Notify To JMS for payment success , to discard user who was eligible for Effort Bpo PaymentFailureCall */
            notifyPaymentSuccess(order);
        }
        return order;
    }

    @Transactional
    public Order codSuccess(String gatewayOrderId, String codContactName, String codContactPhone, boolean shouldCodCall) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        Order order = null;
        if (payment != null) {
            payment.setContactName(codContactName);
            payment.setContactNumber(codContactPhone);

            // todo refactor later. currently increasing the payment amount with added COD charge inside order manager
            // call (depends on whether COD is applicable)
            payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            payment.setGatewayReferenceId(null);
            Long orderCount = getUserManager().getProcessedOrdersCount(payment.getOrder().getUser());
            if (orderCount != null && orderCount >= 3) {
                payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.ON_DELIVERY));
            } else {
                payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.AUTHORIZATION_PENDING));
            }
            payment = paymentDao.save(payment);
            order = getOrderManager().orderPaymentReceieved(payment);
            if (shouldCodCall) {
                if ((payment.getPaymentStatus().getId()).equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId())) {
                    /* Make JMS Call For COD Confirmation Only Once*/
                    Integer PaymentFailedStatus = EnumUserCodCalling.PAYMENT_FAILED.getId();
                    if ((order.getUserCodCall() == null) || ((order.getUserCodCall().getCallStatus().equals(PaymentFailedStatus)))) {
                        try {
                            boolean messagePublished = orderEventPublisher.publishCODEvent(order);
                            UserCodCall userCodCall = null;

                            if (order.getUserCodCall() != null) {
                                userCodCall = order.getUserCodCall();
                            }

                            if (messagePublished) {
                                if (userCodCall != null) {
                                    EnumUserCodCalling thirdPartyPending = EnumUserCodCalling.PENDING_WITH_KNOWLARITY;
                                    userCodCall.setRemark(thirdPartyPending.getName());
                                    userCodCall.setCallStatus(thirdPartyPending.getId());
                                } else {
                                    userCodCall = orderService.createUserCodCall(order, EnumUserCodCalling.PENDING_WITH_KNOWLARITY);
                                }

                            } else {
                                if (userCodCall != null) {
                                    EnumUserCodCalling thirdPartyFailed = EnumUserCodCalling.THIRD_PARTY_FAILED;
                                    userCodCall.setRemark(thirdPartyFailed.getName());
                                    userCodCall.setCallStatus(thirdPartyFailed.getId());
                                } else {
                                    userCodCall = orderService.createUserCodCall(order, EnumUserCodCalling.THIRD_PARTY_FAILED);
                                }
                            }
                            if (userCodCall != null) {
                                orderService.saveUserCodCall(userCodCall);
                            }
                        } catch (DataIntegrityViolationException dataInt) {
                            logger.error("Exception in  inserting  Duplicate UserCodCall by publishing COD : " + dataInt.getMessage());
                        } catch (Exception ex) {
                            logger.error("error occurred in calling JMS in Payment Manager  :::: " + ex.getMessage());
                        }
                    }

                }
            }
        }
        /* Call CodPayment Success */
        notifyPaymentSuccess(order);
        return order;
    }

    @Transactional
    public Order chequeCashSuccess(String gatewayOrderId, String bankName, String bankBranch, String backCity, PaymentMode paymentMode, String chequeNumber) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        Order order = null;
        if (payment != null) {
            payment.setBankName(bankName);
            payment.setBankBranch(bankBranch);
            payment.setBankCity(backCity);
            payment.setPaymentMode(paymentMode);
            payment.setChequeNumber(chequeNumber);

            // todo refactor later. currently increasing the payment amount with added COD charge inside order manager
            // call (depends on whether COD is applicable)
            payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            payment.setGatewayReferenceId(null);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.AUTHORIZATION_PENDING));
            paymentDao.save(payment);
            order = getOrderManager().orderPaymentReceieved(payment);
            /*Notify To JMS for payment success , to discard user who was eligible for Effort Bpo PaymentFailureCall */
            notifyPaymentSuccess(order);

        }
        return order;
    }

    @Transactional
    public Order counterCashSuccess(String gatewayOrderId, PaymentMode paymentMode) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        Order order = null;
        if (payment != null) {
            payment.setPaymentMode(paymentMode);
            // todo refactor later. currently increasing the payment amount with added COD charge inside order manager
            // call (depends on whether COD is applicable)
            payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            payment.setGatewayReferenceId(null);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.AUTHORIZATION_PENDING));
            paymentDao.save(payment);
            order = getOrderManager().orderPaymentReceieved(payment);
            /*Notify To JMS for payment success , to discard user who was eligible for Effort Bpo PaymentFailureCall */
            notifyPaymentSuccess(order);
        }
        return order;
    }

    public Order pendingApproval(String gatewayOrderId) {
        return pendingApproval(gatewayOrderId, null);
    }

    @Transactional
    public Order pendingApproval(String gatewayOrderId, String gatewayReferenceId) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);

        Order order = null;
        // if payment type is full, then send order to processing also, else just accept and update payment status
        if (payment != null) {
            if (payment.getPaymentDate() == null) {
                payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            }
            if (StringUtils.isBlank(gatewayReferenceId)) {
                gatewayReferenceId = gatewayOrderId;
            }
            payment.setGatewayReferenceId(gatewayReferenceId);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.AUTHORIZATION_PENDING));
            payment = getPaymentService().save(payment);
            order = getOrderManager().orderPaymentAuthPending(payment);
        }
        initiatePaymentFailureCall(order);
        return order;
    }

    public Payment fail(String gatewayOrderId) {
        return fail(gatewayOrderId, null, null);
    }

    @Transactional
    public Payment fail(String gatewayOrderId, String gatewayReferenceId, String responseMessage) {
        Payment payment = getPaymentService().findByGatewayOrderId(gatewayOrderId);
        if (payment != null) {
            initiatePaymentFailureCall(payment.getOrder());
            payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            payment.setGatewayReferenceId(gatewayReferenceId);
            payment.setResponseMessage(responseMessage);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.FAILURE));
            payment = getPaymentService().save(payment);
        }
        return payment;
    }

    public void error(String gatewayOrderId, HealthkartPaymentGatewayException e) {
        error(gatewayOrderId, null, e);
    }

    @Transactional
    public void error(String gatewayOrderId, String gatewayReferenceId, HealthkartPaymentGatewayException e) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        if (payment != null) {
            if (!(payment.getPaymentStatus().getId().equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId())
                    || payment.getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.getId()))) {
                initiatePaymentFailureCall(payment.getOrder());
                payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
                payment.setGatewayReferenceId(gatewayReferenceId);
                payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.ERROR));
                payment.setErrorLog(e.getError().getMessage());
                paymentDao.save(payment);
            }
        }
    }

    @Transactional
    public void error(String gatewayOrderId, String gatewayReferenceId, HealthkartPaymentGatewayException e, String responseMessage) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        if (payment != null) {
            if (!(payment.getPaymentStatus().getId().equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId())
                    || payment.getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.getId()))) {
                initiatePaymentFailureCall(payment.getOrder());
                payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
                payment.setGatewayReferenceId(gatewayReferenceId);
                payment.setResponseMessage(responseMessage);
                payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.ERROR));
                payment.setErrorLog(e.getError().getMessage());
                paymentDao.save(payment);
            }
        }
    }

    public Payment verifyCodPayment(Payment payment) {
        payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.ON_DELIVERY));
        return paymentDao.save(payment);
    }

    private void initiatePaymentFailureCall(Order order) {
        if (order != null) {
            if (order.getUserCodCall() == null) {
                try {

                    UserCodCall userCodCall = orderService.createUserCodCall(order, EnumUserCodCalling.PAYMENT_FAILED);
                    if (userCodCall != null) {
                        orderService.saveUserCodCall(userCodCall);
                    }
                    orderEventPublisher.publishPaymentFailureEvent(order);
                } catch (DataIntegrityViolationException dataInt) {
                    logger.error("Exception in  inserting  Duplicate UserCodCall by publishing payment faliure: " + dataInt.getMessage() + " on time :: " + BaseUtils.getCurrentTimestamp());
                } catch (Exception ex) {
                    logger.error("Error occurred in calling JMS in Payment Manager  :::: " + ex.getMessage());
                }
            }
        } else {
            logger.error("Null Order Id Received");
        }

    }


    public void notifyPaymentSuccess(Order order) {
        try {
            orderEventPublisher.publishPaymentSuccessEvent(order);
        } catch (Exception ex) {
            logger.error("Error in Notifying JMS for payment success" + ex.getMessage());
        }
    }

    public HkPaymentService getHkPaymentServiceByGateway(Gateway gateway){
        return ServiceLocatorFactory.getBean(gateway.getName() + "Service", HkPaymentService.class);
    }

    public boolean verifyPaymentStatus(PaymentStatus changedStatus, PaymentStatus oldStatus){
        return oldStatus.getId().equals(changedStatus.getId());
    }

    public boolean sendUnVerifiedPaymentStatusChangeToAdmin(PaymentStatus actualStatus, PaymentStatus changedStatus,String gatewayOrderId){
        return emailManager.sendAdminPaymentStatusChangeEmail(actualStatus.getName(),changedStatus.getName(),gatewayOrderId);
    }

    public List<HkPaymentResponse> seekPayment(String gatewayOrderId) throws HealthkartPaymentGatewayException {
        Map<String, Object> faultyAmountMap = null;
        List<HkPaymentResponse> hkPaymentResponseList = null;
        // verify payment amount what we have and what gateway returns
        try {
            hkPaymentResponseList = paymentService.seekPayment(gatewayOrderId);
            List<Payment> hkPaymentRequestList = paymentService.listPaymentFamily(gatewayOrderId);

            //TODO: remove it later, stuffing gateway Order Id in refund objects
            if(hkPaymentResponseList != null && hkPaymentRequestList != null){
                for(HkPaymentResponse resp : hkPaymentResponseList){
                    for (Payment req : hkPaymentRequestList){
                        stuffGatewayOrderIdInRespObj(resp,req);
                    }
                }
            }


            //TODO: Uncomment this call, if necessary later, if we could map both
            //paymentService.verifyHkRequestAndResponse(hkPaymentRequestList, hkPaymentResponseList);
            List<Map<String, Object>> requestResponseMappedList = paymentService.mapRequestAndResponseObject(hkPaymentRequestList, hkPaymentResponseList);
            paymentService.verifyRequestAndResponseList(requestResponseMappedList);
            faultyAmountMap = paymentService.verifyAmountOfRequestAndResponseList(requestResponseMappedList);

        } catch (HealthkartPaymentGatewayException e) {
            if (e.getError().equals(HealthkartPaymentGatewayException.Error.AMOUNT_MISMATCH)) {
                if (faultyAmountMap != null) {
                    paymentService.sendPaymentMisMatchMailToAdmin((Double) faultyAmountMap.get("RequestAmount"), (Double) faultyAmountMap.get("ResponseAmount"), (String) faultyAmountMap.get("GatewayOrderId"));
                }
            }
            logger.info("HealthKart Payment Exception occurred : "+e);
        }

        return hkPaymentResponseList;
    }

    public Payment updatePayment(String gatewayOrderId) throws HealthkartPaymentGatewayException {
        Payment payment = null;
        try{
            List<HkPaymentResponse> hkPaymentResponseList = paymentService.seekPayment(gatewayOrderId);
            List<Payment> hkPaymentRequestList = paymentService.listPaymentFamily(gatewayOrderId);
            // First verify both request and response must have same number of elements
            //paymentService.verifyHkRequestAndResponse(hkPaymentRequestList,hkPaymentResponseList);
            List<Map<String,Object>> requestResponseMappedList = paymentService.mapRequestAndResponseObject(hkPaymentRequestList,hkPaymentResponseList);
            paymentService.verifyRequestAndResponseList(requestResponseMappedList);
            Order order = updateRequestResponseList(requestResponseMappedList);
            // bring updated payment
            payment = order.getPayment();

        } catch(HealthkartPaymentGatewayException e){
             logger.info("Healthkart Payment Exception : " + e);
        }
        return payment;
    }

    private Order updateRequestResponseList(List<Map<String, Object>> requestResponseMappedList)  {
        Payment requestPayment = null;
        Order order = null;
        HkPaymentResponse hkPaymentResponse = null;
        try{
            if(requestResponseMappedList != null && !requestResponseMappedList.isEmpty()){
                for(Map<String,Object> reqResponseMap : requestResponseMappedList){
                    requestPayment = (Payment)reqResponseMap.get("Request");
                    hkPaymentResponse = (HkPaymentResponse) reqResponseMap.get("Response");
                    order = updateRequestResponseMap(requestPayment,hkPaymentResponse);
                }
            }

        } catch (HealthkartPaymentGatewayException e){
            error(requestPayment.getGatewayOrderId(),e);
        }
        return order;
    }

    private Order updateRequestResponseMap(Payment requestPayment, HkPaymentResponse hkPaymentResponse) throws HealthkartPaymentGatewayException {
        Order order = null;
        if(requestPayment != null && hkPaymentResponse != null){
            PaymentStatus gatewayPaymentStatus = hkPaymentResponse.getPaymentStatus();
            if(gatewayPaymentStatus != null && EnumPaymentStatus.SUCCESS.getName().equalsIgnoreCase(gatewayPaymentStatus.getName())){
                order = success(requestPayment.getGatewayOrderId(), hkPaymentResponse.getGatewayReferenceId(), hkPaymentResponse.getRrn(), hkPaymentResponse.getResponseMsg(), hkPaymentResponse.getAuthIdCode());
            } else if (gatewayPaymentStatus != null && EnumPaymentStatus.FAILURE.getName().equalsIgnoreCase(gatewayPaymentStatus.getName())){
                fail(requestPayment.getGatewayOrderId(), hkPaymentResponse.getGatewayReferenceId(), hkPaymentResponse.getResponseMsg());
            } else if (gatewayPaymentStatus != null && EnumPaymentStatus.AUTHORIZATION_PENDING.getName().equalsIgnoreCase(gatewayPaymentStatus.getName())){
                order = pendingApproval(requestPayment.getGatewayOrderId(),hkPaymentResponse.getGatewayReferenceId());
            }  else {
                throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.INVALID_RESPONSE);
            }
        }
        return order;
    }

    public HkPaymentResponse refundPayment(String gatewayOrderId, Double amount) throws HealthkartPaymentGatewayException {
        Double gatewayAmount =null;
        HkPaymentResponse hkRefundPaymentResponse=null;

        try{
            Payment basePayment = paymentService.findByGatewayOrderId(gatewayOrderId);
            // create a refund Payment Object
            Payment refundRequestPayment = createNewRefundPayment(basePayment, EnumPaymentStatus.REFUND_REQUEST_IN_PROCESS.asPaymenStatus(), amount, EnumPaymentMode.ONLINE_PAYMENT.asPaymenMode());

            hkRefundPaymentResponse = paymentService.refundPayment(gatewayOrderId,amount);
            //TODO: stuffing gatewayOrderId like this, but remove it later
            hkRefundPaymentResponse.setGatewayOrderId(refundRequestPayment.getGatewayOrderId());
            //TODO: remove this check, call it at
            // verify this refund gateway Payment
            //Do verification on refund amount, it should not be greater than (sale amount-sum(refund amounts))
            // from gateway order Id, we will get the base sale amount
            // from base payment gateway reference id, Since it is very gateway specific we will go to respective gateway
            /*List<Payment> hkPaymentRequestList = paymentService.listPaymentFamily(gatewayOrderId);
            paymentService.verifyIfRefundAmountValid(hkPaymentRequestList, amount);*/

            // Also verify if payment amount refunded be gateway and amount requested for refund is same, if not send mail to admin

            if(hkRefundPaymentResponse != null && hkRefundPaymentResponse.getPaymentStatus() != null
                    && EnumPaymentStatus.REFUNDED.getId() == hkRefundPaymentResponse.getPaymentStatus().getId()){
                gatewayAmount = hkRefundPaymentResponse.getAmount();
                paymentService.verifyPaymentAmount(gatewayAmount, amount);
            }
            paymentService.updatePaymentBasedOnResponse(hkRefundPaymentResponse,refundRequestPayment);
            // TODO: logging needs to be implemented
        } catch(HealthkartPaymentGatewayException e){
            if(e.getError().equals(HealthkartPaymentGatewayException.Error.AMOUNT_MISMATCH)){
                paymentService.sendPaymentMisMatchMailToAdmin(amount,gatewayAmount,gatewayOrderId);
            }

        }

        return hkRefundPaymentResponse;
    }

    public Payment createNewRefundPayment(Payment basePayment, PaymentStatus paymentStatus, Double amount, PaymentMode paymentMode){
        Payment refundPayment = createNewPayment(basePayment.getOrder(),paymentMode,
                basePayment.getIp(),basePayment.getGateway(),basePayment.getIssuer(),basePayment.getBillingAddress());

        refundPayment.setPaymentStatus(paymentStatus);
        refundPayment.setParent(basePayment);
        refundPayment.setAmount(amount);

        refundPayment = paymentDao.save(refundPayment);

        return refundPayment;
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

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public RewardPointService getRewardPointService() {
        return rewardPointService;
    }

    public void setRewardPointService(RewardPointService rewardPointService) {
        this.rewardPointService = rewardPointService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public ReferrerProgramManager getReferrerProgramManager() {
        return referrerProgramManager;
    }

    public void setReferrerProgramManager(ReferrerProgramManager referrerProgramManager) {
        this.referrerProgramManager = referrerProgramManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
