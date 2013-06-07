package com.hk.web.action.admin.payment;

import java.text.SimpleDateFormat;
import java.util.*;

import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.GatewayResponseKeys;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.payment.Gateway;
import com.hk.pact.service.payment.HkPaymentService;
import com.hk.util.PaymentFinder;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;

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
import com.hk.constants.payment.EnumPaymentStatus;
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
    private String txnStartDate;
    private String txnEndDate;
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
        payment = paymentService.findByGatewayOrderId(gatewayOrderId);
        HkPaymentService hkPaymentService;
        if (payment != null) {
            Gateway gateway = payment.getGateway();
            if (gateway != null) {
                hkPaymentService = paymentManager.getHkPaymentServiceByGateway(gateway);
                try {
                    if(hkPaymentService != null){
                        paymentResultMap = hkPaymentService.seekHkPaymentResponse(gatewayOrderId);
                    }
                } catch (Exception e) {
                    logger.info("Payment Seek exception for gateway order id" + gatewayOrderId, e);
                }
            }
        }
        transactionList.add(paymentResultMap);
        return new ForwardResolution("/pages/admin/payment/paymentDetails.jsp");
    }

    @DontValidate
    public Resolution bulkSeekPayment() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date startDate = sdf.parse(txnStartDate);
            Date endDate = sdf.parse(txnEndDate);
            List orderStatuses = Arrays.asList(EnumOrderStatus.Placed.asOrderStatus(), EnumOrderStatus.InProcess.asOrderStatus());
            paymentList = paymentService.searchPayments(null, EnumPaymentStatus.getSeekPaymentStatuses(), null, Arrays.asList(EnumPaymentMode.ONLINE_PAYMENT.asPaymenMode()), startDate, endDate, orderStatuses);
            HkPaymentService hkPaymentService;
            for (Payment seekPayment : paymentList) {
                if (seekPayment != null) {
                    Gateway gateway = seekPayment.getGateway();
                    if (gateway != null) {
                        hkPaymentService = paymentManager.getHkPaymentServiceByGateway(gateway);
                        try {
                            if(hkPaymentService != null){
                                paymentResultMap = hkPaymentService.seekHkPaymentResponse(seekPayment.getGatewayOrderId());
                            }
                        } catch (Exception e) {
                            logger.info("Payment Seek exception for gateway order id" + seekPayment.getGatewayOrderId(), e);
                        }
                    }
                }
                transactionList.add(paymentResultMap);
            }

        } catch (Exception e) {
            logger.info("Payment Seek Date conversion exception", e);
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
    @Secure(hasAnyPermissions = {PermissionConstants.REFUND_PAYMENT}, authActionBean = AdminPermissionAction.class)
    public Resolution refundPayment() {
        payment = paymentService.findByGatewayOrderId(gatewayOrderId);
        if (payment != null) {
            int gatewayId = payment.getGateway().getId().intValue();
            switch (gatewayId) {
                case 80:
                    paymentResultMap = PaymentFinder.refundCitrusPayment(payment);
                    if (paymentResultMap.isEmpty()) {
                        paymentResultMap = PaymentFinder.refundIciciPayment(payment, "00007751");
                    }
                    break;
                case 90:
                    if (paymentId == null || StringUtils.isEmpty(paymentId) || amount == null || StringUtils.isEmpty(amount)) {
                        addRedirectAlertMessage(new SimpleMessage("Payment Id and Amount cannot be null"));
                        return new ForwardResolution("/pages/admin/payment/paymentDetails.jsp");
                    }
                    paymentResultMap = PaymentFinder.findEbsTransaction(null, paymentId, amount, EbsPaymentGatewayWrapper.TXN_ACTION_REFUND);
                    break;
                case 100:
                    paymentResultMap = PaymentFinder.refundIciciPayment(payment, "00007518");
            }

        }
        transactionList.add(paymentResultMap);
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
        /*Gateway gateway = payment.getGateway();
        String gatewayOrderId = payment.getGatewayOrderId();
        Map<String, Object> hkrespObj;
        EnumPaymentStatus hkRespPayStatus;
        PaymentStatus hkPaymentStatus;*/
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        //todo shakti, method to deduce what is considered as a valid payment

        /*if (gateway != null && gatewayOrderId != null) {
            HkPaymentService  hkPaymentService = paymentManager.getHkPaymentServiceByGateway(gateway);
            if(hkPaymentService != null){
                hkrespObj = hkPaymentService.seekHkPaymentResponse(gatewayOrderId);
                PaymentStatus changedStatus = paymentService.findPaymentStatus(EnumPaymentStatus.SUCCESS);

                if(hkrespObj != null){
                    hkRespPayStatus = EnumPaymentStatus.getCorrespondingStatus((String)hkrespObj.get(GatewayResponseKeys.HKConstants.RESPONSE_CODE.getKey()));
                    if(hkRespPayStatus != null){
                        hkPaymentStatus = hkRespPayStatus.asPaymenStatus();
                        boolean isValid = paymentManager.verifyPaymentStatus(changedStatus, hkPaymentStatus);
                        if (!isValid) {
                            // send email to admin
                            paymentManager.sendUnVerifiedPaymentStatusChangeToAdmin(hkPaymentStatus, changedStatus, gatewayOrderId);
                        }
                    }
                }
            }
        }*/

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

    public String getTxnStartDate() {
        return txnStartDate;
    }

    public void setTxnStartDate(String txnStartDate) {
        this.txnStartDate = txnStartDate;
    }

    public String getTxnEndDate() {
        return txnEndDate;
    }

    public void setTxnEndDate(String txnEndDate) {
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
}
