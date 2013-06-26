package com.hk.web.action.admin.payment;

import java.text.SimpleDateFormat;
import java.util.*;

import com.hk.constants.payment.*;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.payment.Gateway;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.pojo.HkPaymentResponse;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.util.PaymentFinder;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;
import org.apache.commons.lang.StringUtils;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.manager.payment.EbsPaymentGatewayWrapper;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pricing.PricingEngine;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class CheckPaymentAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(CheckPaymentAction.class);

    @Validate(required = true)
    private Order order;

    private List<Payment> paymentList;

    private List<HkPaymentResponse> hkPaymentResponseList;

    private List<Map<String,List<HkPaymentResponse>>> bulkHkPaymentResponseList;

    @Validate(required = true, on = {"acceptAsAuthPending", "acceptAsSuccessful"})
    private Payment payment;

    private PricingDto pricingDto;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderLoggingService orderLoggingService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderStatusService orderStatusService;
    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private PricingEngine pricingEngine;
    @Autowired
    private PaymentManager paymentManager;

    Map<String, Object> paymentResultMap = new HashMap<String, Object>();
    private String gatewayOrderId;
    private Date txnStartDate;
    private Date txnEndDate;
    private String merchantId;
    private String paymentId;
    private String amount;


    List<Map<String, Object>> transactionList = new ArrayList<Map<String, Object>>();

    @DefaultHandler
    public Resolution show() {
        paymentList = getPaymentService().listByOrderId(order.getId());

        payment = order.getPayment();

        if (EnumOrderStatus.InCart.getId().equals(order.getOrderStatus().getId())) {
            pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), order.getAddress(), 0D), order.getAddress());
        } else {
            pricingDto = new PricingDto(order.getCartLineItems(), order.getAddress());
        }

        return new ForwardResolution("/pages/admin/checkPayment.jsp");
    }

    @DontValidate
    public Resolution seekPayment() {

        try{
            hkPaymentResponseList = paymentService.seekPayment(gatewayOrderId);

        } catch (HealthkartPaymentGatewayException e){
            logger.debug("Payment Seek exception for gateway order id" + gatewayOrderId, e);
        }

        return new ForwardResolution("/pages/admin/payment/paymentDetails.jsp");
    }

    @DontValidate
    public Resolution bulkSeekPayment() {
        try {

            bulkHkPaymentResponseList = new ArrayList<Map<String, List<HkPaymentResponse>>>();
            List orderStatuses = Arrays.asList(EnumOrderStatus.Placed.asOrderStatus(), EnumOrderStatus.InProcess.asOrderStatus());
            paymentList = paymentService.searchPayments(null, EnumPaymentStatus.getSeekPaymentStatuses(), null,
                    Arrays.asList(EnumPaymentMode.ONLINE_PAYMENT.asPaymenMode()), txnStartDate, txnEndDate, orderStatuses, null, EnumGateway.getSeekGateways());
            for (Payment seekPayment : paymentList) {
                if (seekPayment != null) {
                    String gatewayOrderId = seekPayment.getGatewayOrderId();
                    if (gatewayOrderId != null) {
                        try {
                            hkPaymentResponseList = paymentService.seekPayment(gatewayOrderId);
                            Map<String,List<HkPaymentResponse>> tempMap = new HashMap<String, List<HkPaymentResponse>>();
                            tempMap.put(gatewayOrderId,hkPaymentResponseList);
                            bulkHkPaymentResponseList.add(tempMap);
                        } catch (HealthkartPaymentGatewayException e) {
                            logger.info("Payment Seek exception for gateway order id" + gatewayOrderId, e);
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.debug("Payment Seek Date conversion exception", e);
        }

        return new ForwardResolution("/pages/admin/payment/paymentDetails.jsp");
    }



    @DontValidate
    public Resolution searchTransactionByDate() {
        if (merchantId != null) {
            transactionList = PaymentFinder.findTransactionListIcici(txnStartDate, txnEndDate, merchantId);
        } else {
            transactionList = PaymentFinder.findTransactionListCitrus(txnStartDate, txnEndDate);
        }
        return new ForwardResolution("/pages/admin/payment/paymentDetails.jsp");
    }


    @DontValidate
    //@Secure(hasAnyPermissions = {PermissionConstants.REFUND_PAYMENT}, authActionBean = AdminPermissionAction.class)
    public Resolution refundPayment() {
        try{
            paymentService.refundPayment(gatewayOrderId, NumberUtils.toDouble(amount));

        } catch (HealthkartPaymentGatewayException e){
            logger.debug("Payment Seek exception for gateway order id" + gatewayOrderId, e);
            // redirect to error page
        }

        return new ForwardResolution("/pages/admin/payment/paymentDetails.jsp");
    }

    @DontValidate
    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PAYMENT}, authActionBean = AdminPermissionAction.class)
    public Resolution updatePayment() {
        try{
            paymentService.updatePayment(gatewayOrderId);

        } catch (HealthkartPaymentGatewayException e){
            logger.debug("Payment Seek exception for gateway order id" + gatewayOrderId, e);
            // redirect to error page
        }

        return new ForwardResolution("/pages/admin/payment/paymentDetails.jsp");
    }




    @DontValidate
    @Secure(hasAnyPermissions = {PermissionConstants.REFUND_PAYMENT}, authActionBean = AdminPermissionAction.class)
       public Resolution cancelPayment() {
           payment = paymentService.findByGatewayOrderId(gatewayOrderId);
           if (payment != null) {
               if (paymentId == null || StringUtils.isEmpty(paymentId)|| amount == null || StringUtils.isEmpty(amount)) {
                   addRedirectAlertMessage(new SimpleMessage("Payment Id and Amount cannot be null"));
                   return new ForwardResolution("/pages/admin/payment/paymentDetails.jsp");
               }
               paymentResultMap = PaymentFinder.findEbsTransaction(null, paymentId, amount, EbsPaymentGatewayWrapper.TXN_ACTION_CANCEL);
           }
           transactionList.add(paymentResultMap);
           return new ForwardResolution("/pages/admin/payment/paymentDetails.jsp");
       }



     @DontValidate
     @Secure(hasAnyPermissions = {PermissionConstants.REFUND_PAYMENT}, authActionBean = AdminPermissionAction.class)
       public Resolution capturePayment() {
           payment = paymentService.findByGatewayOrderId(gatewayOrderId);
           if (payment != null) {
               if (paymentId == null || StringUtils.isEmpty(paymentId)|| amount == null || StringUtils.isEmpty(amount)) {
                   addRedirectAlertMessage(new SimpleMessage("Payment Id and Amount cannot be null"));
                   return new ForwardResolution("/pages/admin/payment/paymentDetails.jsp");
               }
               paymentResultMap = PaymentFinder.findEbsTransaction(null, paymentId, amount, EbsPaymentGatewayWrapper.TXN_ACTION_CAPTURE);
           }
           transactionList.add(paymentResultMap);
           return new ForwardResolution("/pages/admin/payment/paymentDetails.jsp");
       }
    


    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PAYMENT}, authActionBean = AdminPermissionAction.class)
    public Resolution acceptAsAuthPending() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        getPaymentManager().pendingApproval(payment.getGatewayOrderId());
        getOrderLoggingService().logOrderActivity(payment.getOrder(), loggedOnUser,
                getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.PaymentMarkedAuthPending), null);
        addRedirectAlertMessage(new LocalizableMessage("/admin/CheckPayment.action.payment.auth"));
        return new RedirectResolution(CheckPaymentAction.class).addParameter("order", order.getId());
    }

    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PAYMENT}, authActionBean = AdminPermissionAction.class)
    public Resolution acceptAsSuccessful() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        //todo shakti, method to deduce what is considered as a valid payment
        String gatewayOrderId = payment.getGatewayOrderId();
        if (gatewayOrderId != null) {
            try {
                List<HkPaymentResponse> hkPaymentResponses = paymentService.seekPayment(gatewayOrderId);
                if (hkPaymentResponses != null && !hkPaymentResponses.isEmpty()) {
                    for (HkPaymentResponse hkPaymentResponse : hkPaymentResponses) {
                        if (hkPaymentResponse != null && gatewayOrderId.equalsIgnoreCase(hkPaymentResponse.getGatewayOrderId())) {
                            PaymentStatus changedStatus = paymentService.findPaymentStatus(EnumPaymentStatus.SUCCESS);
                            PaymentStatus paymentGatewayStatus = EnumHKPaymentStatus.getCorrespondingStatus(hkPaymentResponse.getHKPaymentStatus());
                            if (paymentGatewayStatus != null) {
                                boolean isValid = paymentManager.verifyPaymentStatus(changedStatus, paymentGatewayStatus);
                                if (!isValid) {
                                    // send email to admin
                                    paymentManager.sendUnVerifiedPaymentStatusChangeToAdmin(paymentGatewayStatus, changedStatus, gatewayOrderId);
                                }
                            }

                            break;
                        }
                    }
                }

            } catch (HealthkartPaymentGatewayException e) {
                logger.debug("Healthkart payment exception", e);
            }
        }

        getPaymentManager().success(payment.getGatewayOrderId());
        getOrderLoggingService().logOrderActivity(payment.getOrder(), loggedOnUser,
                getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.PaymentMarkedSuccessful), null);
        order.setConfirmationDate(new Date());
        orderService.save(order);
        orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(order);
        orderService.sendEmailToServiceProvidersForOrder(order);
//        }
        addRedirectAlertMessage(new LocalizableMessage("/admin/CheckPayment.action.payment.received"));
        return new RedirectResolution(CheckPaymentAction.class).addParameter("order", order.getId());
    }

    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PAYMENT}, authActionBean = AdminPermissionAction.class)
    public Resolution associateToPayment() {
        getPaymentManager().associateToOrder(payment.getGatewayOrderId());
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        getOrderLoggingService().logOrderActivity(payment.getOrder(), loggedOnUser,
                getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.PaymentAssociatedToOrder), null);

        addRedirectAlertMessage(new LocalizableMessage("/admin/CheckPayment.action.payment.associated"));
        return new RedirectResolution(CheckPaymentAction.class).addParameter("order", order.getId());
    }

    @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PAYMENT}, authActionBean = AdminPermissionAction.class)
    public Resolution updateToSuccess() {
        Payment payment = order.getPayment();
        payment.setPaymentStatus(getPaymentService().findPaymentStatus(EnumPaymentStatus.SUCCESS));
        getPaymentService().save(payment);

        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        getOrderLoggingService().logOrderActivity(payment.getOrder(), loggedOnUser,
                getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.PaymentUpdatedAsSuccessful), null);

        orderService.sendEmailToServiceProvidersForOrder(order);
        orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(order);

        addRedirectAlertMessage(new LocalizableMessage("/admin/CheckPayment.action.payment.received"));
        return new RedirectResolution(CheckPaymentAction.class).addParameter("order", order.getId());
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public PricingDto getPricingDto() {
        return pricingDto;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    public OrderLoggingService getOrderLoggingService() {
        return orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }

    public OrderStatusService getOrderStatusService() {
        return orderStatusService;
    }

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public PricingEngine getPricingEngine() {
        return pricingEngine;
    }

    public void setPricingEngine(PricingEngine pricingEngine) {
        this.pricingEngine = pricingEngine;
    }

    public void setPricingDto(PricingDto pricingDto) {
        this.pricingDto = pricingDto;
    }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    public void setPaymentManager(PaymentManager paymentManager) {
        this.paymentManager = paymentManager;
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public Map<String, Object> getPaymentResultMap() {
        return paymentResultMap;
    }

    public void setPaymentResultMap(Map<String, Object> paymentResultMap) {
        this.paymentResultMap = paymentResultMap;
    }

    public List<Map<String, Object>> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Map<String, Object>> transactionList) {
        this.transactionList = transactionList;
    }

    public Date getTxnStartDate() {
        return txnStartDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setTxnStartDate(Date txnStartDate) {
        this.txnStartDate = txnStartDate;
    }

    public Date getTxnEndDate() {
        return txnEndDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setTxnEndDate(Date txnEndDate) {
        this.txnEndDate = txnEndDate;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<HkPaymentResponse> getHkPaymentResponseList() {
        return hkPaymentResponseList;
    }

    public void setHkPaymentResponseList(List<HkPaymentResponse> hkPaymentResponseList) {
        this.hkPaymentResponseList = hkPaymentResponseList;
    }

    public List<Map<String, List<HkPaymentResponse>>> getBulkHkPaymentResponseList() {
        return bulkHkPaymentResponseList;
    }

    public void setBulkHkPaymentResponseList(List<Map<String, List<HkPaymentResponse>>> bulkHkPaymentResponseList) {
        this.bulkHkPaymentResponseList = bulkHkPaymentResponseList;
    }
}
