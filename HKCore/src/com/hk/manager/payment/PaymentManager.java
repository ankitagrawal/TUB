package com.hk.manager.payment;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.dao.payment.PaymentDao;
import com.hk.dao.payment.PaymentStatusDao;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.OrderManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.service.InventoryService;
import com.hk.service.PaymentService;
import com.hk.service.order.OrderService;
import com.hk.service.order.RewardPointService;
import com.hk.util.TokenUtils;
import com.hk.web.filter.WebContext;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * Author: Kani Date: Jan 3, 2009
 */
@Component
@SuppressWarnings("unused")
public class PaymentManager {
    private static Logger          logger = LoggerFactory.getLogger(PaymentManager.class);

    
    private final Double           codCharges = 0D;

    @Autowired
    private OrderManager           orderManager;
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

    // @Named(Keys.Env.cashBackPercentage)
    @Value("#{hkEnvProps['cashBackPercentage']}")
    private Double                 cashBackPercentage;
    // @Named(Keys.Env.cashBackLimit)
    @Value("#{hkEnvProps['cashBackLimit']}")
    private Double                 cashBackLimit;
    // @Named(Keys.Env.defaultGateway)
    @Value("#{hkEnvProps['defaultGateway']}")
    private Long                   defaultGateway;

    @Autowired
    private PaymentDao             paymentDao;
    @Autowired
    private PaymentStatusDao       paymentStatusDao;

    /*@Autowired
    public PaymentManager(PaymentDao paymentDao, PaymentStatusDao paymentStatusDao,
    // @Named(Keys.Env.codCharges)
            Double codCharges) {
        this.paymentDao = paymentDao;
        this.paymentStatusDao = paymentStatusDao;
        this.codCharges = codCharges;
    }*/
    
    //TODO: rewrite

    /**
     * This method will throw an {@link com.hk.exception.HealthkartPaymentGatewayException} if the payment request
     * cannot be verified
     * 
     * @param gatewayOrderId
     * @param amount
     * @param merchantParam
     * @throws com.hk.exception.HealthkartPaymentGatewayException
     */
    public void verifyPayment(@NotNull
    String gatewayOrderId, @NotNull
    Double amount, @Nullable
    String merchantParam) throws HealthkartPaymentGatewayException {
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
     * @return
     */
    public Payment createNewPayment(@NotNull
    Order order, @NotNull
    PaymentMode paymentMode, @Nullable
    String remoteAddr, String bankCode) {
        Payment payment = new Payment();
        payment.setAmount(order.getAmount());
        payment.setOrder(order);
        payment.setPaymentMode(paymentMode);
        payment.setIp(remoteAddr);
        payment.setBankCode(bankCode);

        // todo can be set if available for user
        payment.setBillingAddressActual(null);

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

    @NotNull
    public static String getUniqueGatewayOrderId(@NotNull
    Order order) {
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
    @NotNull
    public static String getOrderChecksum(@NotNull
    Order order) {
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

            // Adding online payment cashback reward points for Orders above 1000.
            String wantedCOD = "false";
            if (WebContext.getRequest().getSession().getAttribute(HealthkartConstants.Session.wantedCOD) != null) {
                wantedCOD = (String) WebContext.getRequest().getSession().getAttribute(HealthkartConstants.Session.wantedCOD);
                if (wantedCOD.equals("true") && EnumPaymentMode.getPrePaidPaymentModes().contains(payment.getPaymentMode().getId()) && order.getPayment().getAmount() < 1000.0) {
                    DecimalFormat df = new DecimalFormat("#.##");
                    Double cashBack = Double.valueOf(df.format(order.getPayment().getAmount() * cashBackPercentage));
                    RewardPoint rewardPoint = getRewardPointService().addRewardPoints(order.getUser(), null, order, cashBack, "Online payment cashback",
                            EnumRewardPointStatus.APPROVED, getRewardPointService().getRewardPointMode(EnumRewardPointMode.HK_CASHBACK));
                    getReferrerProgramManager().approveRewardPoints(Arrays.asList(rewardPoint), null);
                    WebContext.getRequest().getSession().setAttribute(HealthkartConstants.Session.wantedCOD, "false");
                    logger.debug("Awarded rewardpoints for COD->Prepaid conversion.");
                }
            }

        }
        return order;
    }

    @Transactional
    public Order codSuccess(String gatewayOrderId, String codContactName, String codContactPhone) {
        Payment payment = paymentDao.findByGatewayOrderId(gatewayOrderId);
        Order order = null;
        if (payment != null) {
            payment.setContactName(codContactName);
            payment.setContactNumber(codContactPhone);

            // todo refactor later. currently increasing the payment amount with added COD charge inside order manager
            // call (depends on whether COD is applicable)
            payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            payment.setGatewayReferenceId(null);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.AUTHORIZATION_PENDING));
            paymentDao.save(payment);
            order = getOrderManager().orderPaymentReceieved(payment);

        }
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
        return order;
    }

    public Payment fail(String gatewayOrderId) {
        return fail(gatewayOrderId, null);
    }

    @Transactional
    public Payment fail(String gatewayOrderId, String gatewayReferenceId) {
        Payment payment = getPaymentService().findByGatewayOrderId(gatewayOrderId);
        if (payment != null) {
            payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            payment.setGatewayReferenceId(gatewayReferenceId);
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
            payment.setPaymentDate(BaseUtils.getCurrentTimestamp());
            payment.setGatewayReferenceId(gatewayReferenceId);
            payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.ERROR));
            payment.setErrorLog(e.getError().getMessage());
            paymentDao.save(payment);
        }
    }

    public Payment verifyCodPayment(Payment payment) {
        payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.ON_DELIVERY));
        return paymentDao.save(payment);
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

}
