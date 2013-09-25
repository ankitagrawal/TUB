package com.hk.web.action.admin.crm;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.service.courier.reversePickup.ReversePickupService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
import com.hk.constants.analytics.EnumReason;
import com.hk.constants.analytics.EnumReasonType;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.payment.EnumGateway;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.payment.EnumPaymentTransactionType;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.analytics.Reason;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.order.Order;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ReplacementOrderReason;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.Payment;
import com.hk.domain.reverseOrder.ReverseLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.taglibs.Functions;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ankit Chhabra
 * Date: 9/18/13
 * Time: 6:58 PM
 */
public class MasterResolutionAction extends BaseAction {

    private final Integer REWARD_ACTION = 1;
    private final Integer REFUND_ACTION = 2;
    private final Integer REPLACEMENT_ACTION = 3;


    private boolean replacementFlag;
    private boolean actionFlag;
    private boolean rewardFlag;
    private boolean refundFlag;
    private boolean replacementPossible;

    private Long shippingOrderId;
    private Long baseOrderId;

    private String gatewayOrderId;

    private ShippingOrder shippingOrder;

    private List<LineItem> lineItems = new ArrayList<LineItem>();
    private List<ReverseLineItem> reverseLineItems = new ArrayList<ReverseLineItem>();
    private ReplacementOrderReason replacementOrderReason;
    private Double paymentAmount;
    private Reason refundReason;
    private String refundComments;
    private String replacementComments;
    private ReplacementOrder replacementOrder;
    private Payment payment;

    @Validate(required = true, on = "addRewardPoints")
    private String comment;

    @Validate(required = true, on = "addRewardPoints")
    private RewardPointMode rewardPointMode;

    @Validate(required = true, on = "addRewardPoints")
    private Date expiryDate;

    @Autowired
    AdminEmailManager adminEmailManager;

    @Autowired
    OrderService orderService;

    @Autowired
    RewardPointService rewardPointService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    AdminOrderService adminOrderService;

    @Autowired
    ReplacementOrderService replacementOrderService;

    @Autowired
    ReversePickupService reversePickupService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    AdminShippingOrderService adminShippingOrderService;

    @Autowired
    ShippingOrderService shippingOrderService;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/crm/crmMasterControl.jsp");
    }


    public Resolution searchShippingOrder() {
        if (shippingOrderId != null) {
            shippingOrder = shippingOrderService.find(shippingOrderId);
        } else if (gatewayOrderId != null) {
            shippingOrder = shippingOrderService.findByGatewayOrderId(gatewayOrderId);
        }
        if (shippingOrder == null) {
            addRedirectAlertMessage(new SimpleMessage("No shipping order found  "));
        } else {
            actionFlag = true;
            payment = shippingOrder.getBaseOrder().getPayment();
            baseOrderId = shippingOrder.getBaseOrder().getId();
            paymentAmount = shippingOrder.getAmount();
            shippingOrderId = shippingOrder.getId();
            lineItems = new ArrayList<LineItem>();
            lineItems.addAll(shippingOrder.getLineItems());
            replacementPossible = shippingOrder.getReversePickupOrders() != null
                    && !shippingOrder.getReversePickupOrders().isEmpty();
            if (!replacementPossible) {
                replacementPossible = EnumShippingOrderStatus.getReconcilableShippingOrderStatus()
                        .contains(shippingOrder.getShippingOrderStatus().getId());
            }
        }
        return new ForwardResolution("/pages/admin/crm/crmMasterControl.jsp");
    }

    public Resolution addRewardPoints() {
        rewardFlag = true;
        Order order = shippingOrder.getBaseOrder();
        Payment payment = order.getPayment();
        Double rewardAmount = (Double) adminShippingOrderService.getActionProcessingElement(shippingOrder, REWARD_ACTION);
        if (rewardAmount == 0d) {
            addRedirectAlertMessage(new SimpleMessage("No items found for which reward points could be added."));
        } else if (payment.isCODPayment() && EnumShippingOrderStatus.getReconcilableShippingOrderStatus().contains(shippingOrder.getOrderStatus().getId())) {
            addRedirectAlertMessage(new SimpleMessage("Its a COD Order, How can reward points be added, for an RTO/Lost Order"));
        } else {
            RewardPoint rewardPoint = rewardPointService.addRewardPoints(order.getUser(), getUserService().getLoggedInUser(), order, rewardAmount, comment, EnumRewardPointStatus.APPROVED, EnumRewardPointMode.RESOLUTION_SCREEN.asRewardPointMode());
            rewardPointService.approveRewardPoints(Arrays.asList(rewardPoint), expiryDate);
            paymentService.createNewGenericPayment(payment, EnumPaymentStatus.REFUNDED.asPaymenStatus(), rewardAmount, EnumPaymentMode.REWARD_POINT.asPaymenMode(), EnumPaymentTransactionType.REWARD_POINT);
            addRedirectAlertMessage(new SimpleMessage("Reward Points added successfully"));
        }
        return new ForwardResolution(MasterResolutionAction.class, "pre");
    }

    public Resolution createReplacementOrder() {
        replacementFlag = true;
        List<LineItem> lineItems = new ArrayList<LineItem>();
        lineItems.addAll((Set<LineItem>) adminShippingOrderService.getActionProcessingElement(shippingOrder, REPLACEMENT_ACTION));

        if (lineItems.isEmpty() || replacementOrderReason == null) {
            addRedirectAlertMessage(new SimpleMessage("No reason selected or no appropriate items found for creating replacement order."));
            return new ForwardResolution(MasterResolutionAction.class, "pre");
        }

        for (LineItem lineItem : lineItems) {
            String productName = lineItem.getCartLineItem().getProductVariant().getProduct().getName();
            lineItem.setRQty(lineItem.getQty());
            if (lineItem.getQty() > inventoryService.getAllowedStepUpInventory(lineItem.getSku().getProductVariant())) {
                addRedirectAlertMessage(new SimpleMessage("Unable to create replacement order as " + productName + " out of stock."));
                return new ForwardResolution(MasterResolutionAction.class, "pre");
            }
        }

        boolean isRTO = EnumShippingOrderStatus.SO_RTO.getId().equals(shippingOrder.getOrderStatus().getId()) || EnumShippingOrderStatus.RTO_Initiated.getId().equals(shippingOrder.getOrderStatus().getId());

        replacementOrder = replacementOrderService.createReplaceMentOrder(shippingOrder, lineItems, isRTO, replacementOrderReason, replacementComments);
        if (replacementOrder == null) {
            addRedirectAlertMessage(new SimpleMessage("Unable to create replacement order."));
        } else {
            addRedirectAlertMessage(new SimpleMessage("The Replacement order created. New gateway order id: " + replacementOrder.getGatewayOrderId()));
        }
        return new ForwardResolution(MasterResolutionAction.class, "pre");
    }

    @DontValidate
    @Secure(hasAnyPermissions = {PermissionConstants.REFUND_PAYMENT}, authActionBean = AdminPermissionAction.class)
    public Resolution refundPayment() {
        refundFlag = true;
        User loggedOnUser = getUserService().getLoggedInUser();
        Payment basePayment = shippingOrder.getBaseOrder().getPayment();
        Double refundAmount = (Double) adminShippingOrderService.getActionProcessingElement(shippingOrder, REFUND_ACTION);
        Gateway gateway = basePayment.getGateway();
        String paymentGatewayOrderId = basePayment.getGatewayOrderId();

        if (basePayment.getPaymentMode().getId().equals(EnumPaymentMode.ONLINE_PAYMENT.getId()) && gateway != null && refundAmount > 0 && refundReason != null && refundComments != null && !refundComments.isEmpty()) {
            if (EnumGateway.getManualRefundGateways().contains(gateway.getId())) {
                adminEmailManager.sendManualRefundTaskToAdmin(shippingOrder.getAmount(), paymentGatewayOrderId, gateway.getName());
                shippingOrderService.logShippingOrderActivity(shippingOrder, loggedOnUser, EnumShippingOrderLifecycleActivity.Reconciliation.asShippingOrderLifecycleActivity(), EnumReason.ManualRefundInitiated.asReason(), comment);
                paymentService.createNewGenericPayment(basePayment, EnumPaymentStatus.REFUNDED.asPaymenStatus(), refundAmount, EnumPaymentMode.ONLINE_PAYMENT.asPaymenMode(), EnumPaymentTransactionType.REFUND);
            }
            else if (EnumGateway.getHKServiceEnabledGateways().contains(gateway.getId())) {
                try {
                    if (EnumPaymentStatus.SUCCESS.getId().equals(basePayment.getPaymentStatus().getId())) {
                        basePayment = paymentService.updatePayment(gatewayOrderId);
                        Payment refundPayment = paymentService.refundPayment(paymentGatewayOrderId, refundAmount);
                        String loggingComment = refundReason.getClassification().getPrimary() + "- " + refundComments;
                        if (refundPayment != null) {
                            if (EnumPaymentStatus.REFUNDED.getId().equals(refundPayment.getPaymentStatus().getId())) {
                                adminOrderService.logOrderActivity(basePayment.getOrder(), loggedOnUser,
                                        EnumOrderLifecycleActivity.AmountRefundedOrderCancel.asOrderLifecycleActivity(),
                                        loggingComment);

                            } else if (EnumPaymentStatus.REFUND_FAILURE.getId().equals(refundPayment.getPaymentStatus().getId())) {
                                adminOrderService.logOrderActivity(basePayment.getOrder(), loggedOnUser,
                                        EnumOrderLifecycleActivity.RefundAmountFailed.asOrderLifecycleActivity(), loggingComment);
                            } else if (EnumPaymentStatus.REFUND_REQUEST_IN_PROCESS.getId().equals(refundPayment.getPaymentStatus().getId())) {
                                adminOrderService.logOrderActivity(basePayment.getOrder(), loggedOnUser,
                                        EnumOrderLifecycleActivity.RefundAmountInProcess.asOrderLifecycleActivity(), loggingComment);
                            }
                        } else {
                            adminOrderService.logOrderActivity(basePayment.getOrder(), loggedOnUser,
                                    EnumOrderLifecycleActivity.RefundAmountFailed.asOrderLifecycleActivity(), loggingComment);
                            addRedirectAlertMessage(new SimpleMessage("Refund failed."));
                        }
                    } else {
                        addRedirectAlertMessage(new SimpleMessage("Refund can only be initiated on successful payment, Please call update payment"));
                    }
                } catch (HealthkartPaymentGatewayException e) {
                    addRedirectAlertMessage(new SimpleMessage("Payment Seek exception for gateway order id " + paymentGatewayOrderId));
                }
            }
            addRedirectAlertMessage(new SimpleMessage("Please ensure, its an online payment, plus enter reason and related comments for the refund."));
        }
        return new ForwardResolution(MasterResolutionAction.class, "pre");
    }


    public boolean isReplacementFlag() {
        return replacementFlag;
    }

    public void setReplacementFlag(boolean replacementFlag) {
        this.replacementFlag = replacementFlag;
    }

    public boolean isActionFlag() {
        return actionFlag;
    }

    public void setActionFlag(boolean actionFlag) {
        this.actionFlag = actionFlag;
    }

    public Long getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(Long shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    public Long getBaseOrderId() {
        return baseOrderId;
    }

    public void setBaseOrderId(Long baseOrderId) {
        this.baseOrderId = baseOrderId;
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public List<ReverseLineItem> getReverseLineItems() {
        return reverseLineItems;
    }

    public void setReverseLineItems(List<ReverseLineItem> reverseLineItems) {
        this.reverseLineItems = reverseLineItems;
    }

    public ReplacementOrderReason getReplacementOrderReason() {
        return replacementOrderReason;
    }

    public void setReplacementOrderReason(ReplacementOrderReason replacementOrderReason) {
        this.replacementOrderReason = replacementOrderReason;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public boolean isRewardFlag() {
        return rewardFlag;
    }

    public void setRewardFlag(boolean rewardFlag) {
        this.rewardFlag = rewardFlag;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RewardPointMode getRewardPointMode() {
        return rewardPointMode;
    }

    public void setRewardPointMode(RewardPointMode rewardPointMode) {
        this.rewardPointMode = rewardPointMode;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<Reason> getRefundReasons() {
        return Functions.getReasonsByType(EnumReasonType.REFUND.getName());
    }

    public boolean isRefundFlag() {
        return refundFlag;
    }

    public void setRefundFlag(boolean refundFlag) {
        this.refundFlag = refundFlag;
    }

    public Reason getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(Reason refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundComments() {
        return refundComments;
    }

    public void setRefundComments(String refundComments) {
        this.refundComments = refundComments;
    }

    public ReplacementOrder getReplacementOrder() {
        return replacementOrder;
    }

    public void setReplacementOrder(ReplacementOrder replacementOrder) {
        this.replacementOrder = replacementOrder;
    }

    public String getReplacementComments() {
        return replacementComments;
    }

    public void setReplacementComments(String replacementComments) {
        this.replacementComments = replacementComments;
    }


    /**
     * @return the replacementPossible
     */
    public boolean isReplacementPossible() {
        return replacementPossible;
    }


    /**
     * @param replacementPossible the replacementPossible to set
     */
    public void setReplacementPossible(boolean replacementPossible) {
        this.replacementPossible = replacementPossible;
    }
}
