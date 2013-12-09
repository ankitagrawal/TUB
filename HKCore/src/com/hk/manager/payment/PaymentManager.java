package com.hk.manager.payment;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.EnumUserCodCalling;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.payment.EnumIssuer;
import com.hk.constants.payment.EnumIssuerType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
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
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.util.TokenUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Author: Kani Date: Jan 3, 2009
 */
@Component
@SuppressWarnings("unused")
public class PaymentManager {
    private static Logger          logger     = LoggerFactory.getLogger(PaymentManager.class);

    private final Double           codCharges = 0D;

    @Autowired
    private OrderManager           orderManager;
    @Autowired
    private UserManager            userManager;
    @Autowired
    private OrderService           orderService;
    @Autowired
    private RewardPointService     rewardPointService;
    @Autowired
    private ReferrerProgramManager referrerProgramManager;
    @Autowired
    private InventoryService       inventoryService;
    @Autowired
    private PaymentService         paymentService;
    @Autowired
    SMSManager                     smsManager;
    @Autowired
    OrderEventPublisher            orderEventPublisher;
    @Autowired
    EmailManager                   emailManager;
    @Autowired
    InventoryHealthService         inventoryHealthService;

    @Value("#{hkEnvProps['" + Keys.Env.hybridRelease + "']}")
    private boolean                hybridRelease;

    @Value("#{hkEnvProps['" + Keys.Env.cashBackLimit + "']}")
    private Double                 cashBackLimit;
    @Value("#{hkEnvProps['" + Keys.Env.maxCODCallCount + "']}")
    private int                    maxCODCallCount;
    @Value("#{hkEnvProps['" + Keys.Env.defaultGateway + "']}")
    private Long                   defaultGateway;
    @Value("#{hkEnvProps['" + Keys.Env.codRoute + "']}")
    private String                 codRoute;
    @Value("#{hkEnvProps['" + Keys.Env.hkpayUrl + "']}")
    private String hkPayUrl;

    @Autowired
    private PaymentDao             paymentDao;
    @Autowired
    private PaymentStatusDao       paymentStatusDao;

    // TODO: rewrite

    /**
     * This method will throw an {@link com.hk.exception.HealthkartPaymentGatewayException} if the payment request
     * cannot be verified
     * 
     * @param gatewayOrderId
     * @param amount
     * @param merchantParam
     * @throws com.hk.exception.HealthkartPaymentGatewayException
     */
    public Payment verifyPayment(String gatewayOrderId, Double amount, String merchantParam) throws HealthkartPaymentGatewayException {
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
            // throw new HealthkartPaymentGatewayException(HealthkartPaymentGatewayException.Error.DOUBLE_PAYMENT);
        }
        return payment;
    }

    /**
     * @param order
     * @param paymentMode
     * @param remoteAddr
     * @param gateway
     * @param issuer
     * @return
     * @param billingAddress
     */
    public Payment createNewPayment(Order order, PaymentMode paymentMode, String remoteAddr, Gateway gateway, Issuer issuer, BillingAddress billingAddress) {
        Payment payment = new Payment();
        payment.setAmount(order.getAmount());
        payment.setOrder(order);
        payment.setPaymentMode(paymentMode);
        payment.setIp(remoteAddr);

        if (EnumPaymentMode.ONLINE_PAYMENT.getId().equals(payment.getPaymentMode().getId())) {
            payment.setGateway(gateway);
            payment.setIssuer(issuer);

            // set COD mode if issuer is issuer
            if (EnumIssuerType.COD.getId().equalsIgnoreCase(issuer.getIssuerType())) {
                payment.setPaymentMode(EnumPaymentMode.COD.asPaymenMode());
            }
        }

        payment.setBillingAddress(billingAddress);

        // these two fields must be generated
        // gateway order id is the order id passed to the payment gateway. this can be
        // a string made up of our order id, the payment id, or a random string (unique)

        if (hybridRelease) {
            payment.setGatewayOrderId(getUniqueHybridGatewayOrderId(order));
        } else {
            payment.setGatewayOrderId(getUniqueGatewayOrderId(order));
        }

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

    public static String getUniqueHybridGatewayOrderId(Order order) {

        return TokenUtils.generateHybridGatewayOrderId(order);
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
            }
            order = processOrder(payment);
        }
        return order;
    }

    public Order success(String gatewayOrderId) {
        return success(gatewayOrderId, null);
    }

    public Order success(String gatewayOrderId, String gatewayReferenceId) {
        return success(gatewayOrderId, gatewayReferenceId, null, null, null);
    }

    public Order authPending(String gatewayOrderId, String codContactName, String codContactPhone, String bankName, String bankBranch, String backCity, String chequeNumber) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        Order order = null;
        if (payment != null) {
            payment.setContactName(codContactName);
            payment.setContactNumber(codContactPhone);
            if (payment.getPaymentDate() == null) {
                payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            }
            payment.setBankName(bankName);
            payment.setBankBranch(bankBranch);
            payment.setBankCity(backCity);
            payment.setChequeNumber(chequeNumber);
            order = processOrder(payment);
        }
        return order;
    }

    public Order success(String gatewayOrderId,String hkPayRefId, String gatewayReferenceId, String rrn, String responseMessage, String authIdCode,Gateway gateway) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);

        Order order = null;
        // if payment type is full, then send order to processing also, else just accept and update payment status
        if (payment != null) {
            payment.setGatewayOrderId(hkPayRefId);
            if (gateway != null) {
                payment.setGateway(gateway);
            }
            if (payment.getPaymentDate() == null) {
                payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            }
            payment.setGatewayReferenceId(gatewayReferenceId);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.SUCCESS));
            payment.setResponseMessage(responseMessage);
            payment.setAuthIdCode(authIdCode);
            payment.setRrn(rrn);
            order = processOrder(payment);
        }
        orderEventPublisher.publishOrderPlacedEvent(order);
        return order;

    }

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
            order = processOrder(payment);
        }
        orderEventPublisher.publishOrderPlacedEvent(order);
        return order;
    }

    @Transactional
    public Order processOrder(Payment payment) {
        payment = paymentDao.save(payment);
        Order order = getOrderManager().orderPaymentReceieved(payment);
        /* Notify To JMS for payment success , to discard user who was eligible for Effort Bpo PaymentFailureCall */
        notifyPaymentSuccess(order);
        return order;
    }

    public Order codSuccess(String gatewayOrderId, String codContactName, String codContactPhone, boolean shouldCodCall) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        Order order = null;
        if (payment != null) {
            Long orderCount = getUserManager().getProcessedOrdersCount(payment.getOrder().getUser());
            if (orderCount != null && orderCount >= 2) {
                payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.ON_DELIVERY));
            } else {
                payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.AUTHORIZATION_PENDING));
            }
            order = authPending(gatewayOrderId, codContactName, codContactPhone, null, null, null, null);
            createUserCodCall(order);
            pushCODToThirdParty(shouldCodCall, order.getPayment());
            orderEventPublisher.publishOrderPlacedEvent(order);
        }
        return order;
    }

    @Transactional
    private void createUserCodCall(Order order) {
        Payment payment = order.getPayment();
        if (payment.isCODPayment() && payment.getPaymentStatus().getId().equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId())) {
            // for some orders userCodCall object is not created, a check to create one
            try {
                UserCodCall userCodCall = null;
                if (codRoute != null && codRoute.equalsIgnoreCase("smsCountry")) {
                    userCodCall = orderService.createUserCodCall(order, EnumUserCodCalling.PENDING_WITH_CUSTOMER);
                    userCodCall.setSource("SMSCountry");
                } else {
                    userCodCall = orderService.createUserCodCall(order, EnumUserCodCalling.PENDING_WITH_HEALTHKART);
                }

                orderService.saveUserCodCall(userCodCall);
            } catch (Exception e) {
                logger.info("User Cod Call already exists for " + order.getId());
            }
        }
    }

    private void pushCODToThirdParty(boolean shouldCodCall, Payment payment) {
        Order order = payment.getOrder();
        if (shouldCodCall) {
            if ((payment.getPaymentStatus().getId()).equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId())) {
                /* Make JMS Call For COD Confirmation Only Once */
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

    public Order chequeCashSuccess(String gatewayOrderId, String bankName, String bankBranch, String backCity, PaymentMode paymentMode, String chequeNumber) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        Order order = null;
        if (payment != null) {
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.AUTHORIZATION_PENDING));
            order = authPending(gatewayOrderId, null, null, bankName, bankBranch, backCity, chequeNumber);
        }
        return order;
    }

    public Order counterCashSuccess(String gatewayOrderId, PaymentMode paymentMode) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        Order order = null;
        if (payment != null) {
            payment.setPaymentMode(paymentMode);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.AUTHORIZATION_PENDING));
            order = authPending(gatewayOrderId, null, null, null, null, null, null);
        }
        return order;
    }

    public Order pendingApproval(String gatewayOrderId) {
        return pendingApproval(gatewayOrderId, null);
    }

    public Order pendingApproval(String gatewayOrderId, String gatewayReferenceId) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        Order order = null;
        if (payment != null) {
            if (StringUtils.isBlank(gatewayReferenceId)) {
                gatewayReferenceId = gatewayOrderId;
            }
            payment.setGatewayReferenceId(gatewayReferenceId);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.AUTHORIZATION_PENDING));
            order = authPending(gatewayOrderId, gatewayReferenceId, null, null, null, null, null);
        }
        return order;
    }

    public Order pendingApproval(String gatewayOrderId,String hkPayRefId, String gatewayReferenceId, Gateway gateway) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        Order order = null;
        if (payment != null) {
            // setting the new gatewayOrderId
            payment.setGatewayOrderId(hkPayRefId);
            payment.setGatewayReferenceId(gatewayReferenceId);
            payment.setGateway(gateway);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.AUTHORIZATION_PENDING));
            order = authPending(gatewayOrderId, gatewayReferenceId, null, null, null, null, null);
        }
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

    @Transactional
    public Payment fail(String gatewayOrderId, String hkpayRefId, String gatewayReferenceId, String responseMessage, Gateway gateway) {
        Payment payment = getPaymentService().findByGatewayOrderId(gatewayOrderId);
        if (payment != null) {
            initiatePaymentFailureCall(payment.getOrder());
            payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            // setting the new gatewayOrderId
            payment.setGatewayOrderId(hkpayRefId);
            payment.setGateway(gateway);
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
            if (!(payment.getPaymentStatus().getId().equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId()) || payment.getPaymentStatus().getId().equals(
                    EnumPaymentStatus.SUCCESS.getId()))) {
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
            if (!(payment.getPaymentStatus().getId().equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId()) || payment.getPaymentStatus().getId().equals(
                    EnumPaymentStatus.SUCCESS.getId()))) {
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
                    logger.error("Exception in  inserting  Duplicate UserCodCall by publishing payment faliure: " + dataInt.getMessage() + " on time :: "
                            + BaseUtils.getCurrentTimestamp());
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

    /*
     * public HkPaymentService getHkPaymentServiceByGateway(Gateway gateway){ HkPaymentService hkPaymentService = null;
     * if(gateway!= null && EnumGateway.getHKServiceEnabledGateways().contains(gateway.getId())){ hkPaymentService =
     * ServiceLocatorFactory.getBean(gateway.getName() + "Service", HkPaymentService.class); } return hkPaymentService; }
     */


    public boolean isHKPayWorking() throws Exception {
        boolean isWorking = false;
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(hkPayUrl);
        int status = httpClient.executeMethod(getMethod);
        if (HttpStatus.SC_OK == status) {
            logger.info("HKPay is live");
            isWorking = true;
        }
        return isWorking;
    }

    public boolean verifyPaymentStatus(PaymentStatus changedStatus, PaymentStatus oldStatus) {
        return oldStatus.getId().equals(changedStatus.getId());
    }

    public boolean sendUnVerifiedPaymentStatusChangeToAdmin(PaymentStatus actualStatus, PaymentStatus changedStatus, String gatewayOrderId) {
        return emailManager.sendAdminPaymentStatusChangeEmail(actualStatus.getName(), changedStatus.getName(), gatewayOrderId);
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
