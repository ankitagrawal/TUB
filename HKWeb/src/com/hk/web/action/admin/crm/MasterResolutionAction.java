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
import com.hk.constants.queue.EnumClassification;
import com.hk.constants.reversePickup.EnumReverseAction;
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
import com.hk.domain.reversePickupOrder.ReversePickupOrder;
import com.hk.domain.reversePickupOrder.RpLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pojo.LedgerMap;
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
    private final Integer SEARCH_ACTION = 4;


    private boolean replacementFlag;
    private boolean actionFlag;
    private boolean rewardFlag;
    private boolean refundFlag;
    private boolean replacementPossible;
    boolean rto = false;

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
    private String actionType;

    @Validate(required = true, on = "addRewardPoints")
    private String comment;

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

    @Autowired
  InventoryHealthService inventoryHealthService;


    Map<String, Map<List<LineItem>, Double>> ledgerLineItemAmountMap = new HashMap<String, Map<List<LineItem>, Double>>();

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
            baseOrderId = shippingOrder.getBaseOrder().getId();
            paymentAmount = shippingOrder.getAmount();
            shippingOrderId = shippingOrder.getId();
            lineItems = new ArrayList<LineItem>();
            Set<LineItem> toBeProcessedItems = new HashSet<LineItem>();
            paymentAmount = (Double)adminShippingOrderService.getActionProcessingElement(shippingOrder, toBeProcessedItems, SEARCH_ACTION);
            lineItems.addAll(toBeProcessedItems);
            rto = EnumShippingOrderStatus.SO_RTO.getId().equals(shippingOrder.getOrderStatus().getId())
            		|| EnumShippingOrderStatus.RTO_Initiated.getId().equals(shippingOrder.getOrderStatus().getId());

            replacementPossible = !shippingOrder.getReversePickupOrders().isEmpty();

            if(actionType.equalsIgnoreCase("addRewardPoints")) {
            	rewardFlag=true;
            } else if (actionType.equalsIgnoreCase("refund")) {
            	refundFlag = true;
            } else {
            	replacementFlag = true;
            }
//            ledgerLineItemAmountMap = generateReconcileMap(shippingOrder);
        }

        return new ForwardResolution("/pages/admin/crm/crmMasterControl.jsp");
    }

    public Resolution addRewardPoints() {
        Order order = shippingOrder.getBaseOrder();
        Payment basePayment = order.getPayment();
        Set<LineItem> toBeProcessedItems = new HashSet<LineItem>();
        Double rewardAmount = (Double) adminShippingOrderService.getActionProcessingElement(shippingOrder,toBeProcessedItems, REWARD_ACTION);
        if (rewardAmount == 0d) {
            addRedirectAlertMessage(new SimpleMessage("No items found for which reward points could be added."));
        } else if (basePayment.isCODPayment() && EnumShippingOrderStatus.getReconcilableShippingOrderStatus().contains(shippingOrder.getOrderStatus().getId())) {
            addRedirectAlertMessage(new SimpleMessage("Its a COD Order, How can reward points be added, for an RTO/Lost Order"));
        } else {
            RewardPoint rewardPoint = rewardPointService.addRewardPoints(order.getUser(), getUserService().getLoggedInUser(), order, rewardAmount, comment, EnumRewardPointStatus.APPROVED, EnumRewardPointMode.RESOLUTION_SCREEN.asRewardPointMode());
            rewardPointService.approveRewardPoints(Arrays.asList(rewardPoint), expiryDate);
            payment = paymentService.createNewGenericPayment(basePayment, EnumPaymentStatus.REFUNDED.asPaymenStatus(), rewardAmount, EnumPaymentMode.REWARD_POINT.asPaymenMode(), EnumPaymentTransactionType.REWARD_POINT);
            shippingOrderService.logShippingOrderActivity(shippingOrder, getUserService().getLoggedInUser(),
                    shippingOrderService.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.POST_SHIPPED_RECONCILIATION),
                          null, "Reward points given for SO after shipping.");

            addRedirectAlertMessage(new SimpleMessage("Reward Points added successfully"));
        }
        return new ForwardResolution(MasterResolutionAction.class, "pre");
    }

    public Resolution createReplacementOrder() {
        List<LineItem> lineItems = new ArrayList<LineItem>();
        Set<LineItem> toBeProcessedItems = new HashSet<LineItem>();
        adminShippingOrderService.getActionProcessingElement(shippingOrder,toBeProcessedItems, REPLACEMENT_ACTION);
        lineItems.addAll(toBeProcessedItems);

        if (lineItems.isEmpty() || replacementOrderReason == null) {
            addRedirectAlertMessage(new SimpleMessage("No reason selected or no appropriate items found for creating replacement order."));
            return new ForwardResolution(MasterResolutionAction.class, "pre");
        }

        for (LineItem lineItem : lineItems) {
            String productName = lineItem.getCartLineItem().getProductVariant().getProduct().getName();
          Long countOfAvailableUnBookedSkuItemsInBright = inventoryHealthService.getUnbookedInventoryOfBrightForMrp(lineItem.getSku().getProductVariant(), lineItem.getSku().getWarehouse().getFulfilmentCenterCode(), lineItem.getMarkedPrice());
//            lineItem.setRQty(lineItem.getQty());
            if (lineItem.getRQty() > inventoryService.getAllowedStepUpInventory(lineItem.getSku().getProductVariant()) &&  lineItem.getRQty() > countOfAvailableUnBookedSkuItemsInBright ) {
                addRedirectAlertMessage(new SimpleMessage("Unable to create replacement order as " + productName + " out of stock."));
                return new ForwardResolution(MasterResolutionAction.class, "pre");
            }
        }

        replacementOrder = replacementOrderService.createReplaceMentOrder(shippingOrder, lineItems, rto, replacementOrderReason, replacementComments);
        if (replacementOrder == null) {
            addRedirectAlertMessage(new SimpleMessage("Unable to create replacement order."));
        } else {
        	shippingOrderService.logShippingOrderActivity(shippingOrder, getUserService().getLoggedInUser(),
                    shippingOrderService.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.POST_SHIPPED_RECONCILIATION),
                          null, "Repalcement Order created for SO after shipping.");
            addRedirectAlertMessage(new SimpleMessage("The Replacement order created. New gateway order id: " + replacementOrder.getGatewayOrderId()));
        }
        return new ForwardResolution(MasterResolutionAction.class, "pre");
    }

    @DontValidate
    @Secure(hasAnyPermissions = {PermissionConstants.REFUND_PAYMENT}, authActionBean = AdminPermissionAction.class)
    public Resolution refundPayment() {
        User loggedOnUser = getUserService().getLoggedInUser();
        Payment basePayment = shippingOrder.getBaseOrder().getPayment();
        Set<LineItem> toBeProcessedItems = new HashSet<LineItem>();
        Double refundAmount = (Double) adminShippingOrderService.getActionProcessingElement(shippingOrder,toBeProcessedItems, REFUND_ACTION);
        Gateway gateway = basePayment.getGateway();
        String paymentGatewayOrderId = basePayment.getGatewayOrderId();

        if (basePayment.getPaymentMode().getId().equals(EnumPaymentMode.ONLINE_PAYMENT.getId()) && gateway != null && refundAmount > 0 && refundReason != null && refundComments != null && !refundComments.isEmpty()) {
            if (EnumGateway.getManualRefundGateways().contains(gateway.getId())) {
                adminEmailManager.sendManualRefundTaskToAdmin(shippingOrder.getAmount(), paymentGatewayOrderId, gateway.getName());
                shippingOrderService.logShippingOrderActivity(shippingOrder, loggedOnUser,
                		EnumShippingOrderLifecycleActivity.Reconciliation.asShippingOrderLifecycleActivity(),
                		EnumReason.ManualRefundInitiated.asReason(), comment);
                payment = paymentService.createNewGenericPayment(basePayment, EnumPaymentStatus.REFUNDED.asPaymenStatus(),
                		refundAmount, EnumPaymentMode.ONLINE_PAYMENT.asPaymenMode(), EnumPaymentTransactionType.REFUND);
            } else if (EnumGateway.getHKServiceEnabledGateways().contains(gateway.getId())) {
                try {
                    if (EnumPaymentStatus.SUCCESS.getId().equals(basePayment.getPaymentStatus().getId())) {
                        basePayment = paymentService.updatePayment(gatewayOrderId);
                        if(basePayment == null){
                            basePayment = shippingOrder.getBaseOrder().getPayment();
                        }
                        payment = paymentService.refundPayment(paymentGatewayOrderId, refundAmount);
                        String loggingComment = refundReason.getClassification().getPrimary() + "- " + refundComments;
                        if (payment != null) {
                            if (EnumPaymentStatus.REFUNDED.getId().equals(payment.getPaymentStatus().getId())) {
                                adminOrderService.logOrderActivity(basePayment.getOrder(), loggedOnUser,
                                        EnumOrderLifecycleActivity.AmountRefundedOrderCancel.asOrderLifecycleActivity(),
                                        loggingComment);
                                addRedirectAlertMessage(new SimpleMessage("Refund successful"));
                                shippingOrderService.logShippingOrderActivity(shippingOrder, getUserService().getLoggedInUser(),
                                        shippingOrderService.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.POST_SHIPPED_RECONCILIATION),
                                              null, "Refund given for SO after shipping.");
                             
                            } else if (EnumPaymentStatus.REFUND_FAILURE.getId().equals(payment.getPaymentStatus().getId())) {
                                adminOrderService.logOrderActivity(basePayment.getOrder(), loggedOnUser,
                                        EnumOrderLifecycleActivity.RefundAmountFailed.asOrderLifecycleActivity(), loggingComment);
                                addRedirectAlertMessage(new SimpleMessage("Refund failed."));
                            } else if (EnumPaymentStatus.REFUND_REQUEST_IN_PROCESS.getId().equals(payment.getPaymentStatus().getId())) {
                                adminOrderService.logOrderActivity(basePayment.getOrder(), loggedOnUser,
                                        EnumOrderLifecycleActivity.RefundAmountInProcess.asOrderLifecycleActivity(), loggingComment);
                                addRedirectAlertMessage(new SimpleMessage("Refund in process."));
                                shippingOrderService.logShippingOrderActivity(shippingOrder, getUserService().getLoggedInUser(),
                                        shippingOrderService.getShippingOrderLifeCycleActivity(EnumShippingOrderLifecycleActivity.POST_SHIPPED_RECONCILIATION),
                                              null, "Refund in process which was given for SO after shipping.");
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
        } else {
            addRedirectAlertMessage(new SimpleMessage("Please ensure, its an online payment, plus enter reason and related comments for the refund or No Applicable amount to be refunded, or all units already reconciled"));
        }
        return new ForwardResolution(MasterResolutionAction.class, "pre");
    }


    private Map<String, Map<List<LineItem>, Double>> generateReconcileMap(ShippingOrder shippingOrder) {
        Map<String, Map<List<LineItem>, Double>> ledgerLineItemAmountMap = new HashMap<String, Map<List<LineItem>, Double>>();
        List<ReversePickupOrder> reversePickupOrders = reversePickupService.getReversePickupsForSO(shippingOrder);
        //get all bookings
        if (reversePickupOrders != null && !reversePickupOrders.isEmpty()) {
            //ledger vs Set of RpLineItems
            Map<String, Set<RpLineItem>> reconcileItemMap = new HashMap<String, Set<RpLineItem>>();
            //for each booking
            for (ReversePickupOrder reversePickupOrder : reversePickupOrders) {
                List<RpLineItem> rpLineItems = reversePickupOrder.getRpLineItems();
                for (RpLineItem rpLineItem : rpLineItems) {
                    //for approved lineItem
                    if (rpLineItem.getCustomerActionStatus() != null && EnumReverseAction.Approved.getId().equals(rpLineItem.getCustomerActionStatus().getId())) {
                        String actionTask = rpLineItem.getActionTaken().getPrimary();
                        if (reconcileItemMap.containsKey(actionTask)) {
                            Set<RpLineItem> rpLineItemMapValue = reconcileItemMap.get(actionTask);
                            rpLineItemMapValue.add(rpLineItem);
                            reconcileItemMap.put(actionTask, rpLineItemMapValue);
                        } else {
                            Set<RpLineItem> rpLineItemMapNewValue = new HashSet<RpLineItem>();
                            rpLineItemMapNewValue.add(rpLineItem);
                            reconcileItemMap.put(actionTask, rpLineItemMapNewValue);
                        }
                    }
                }
            }
            Map<List<LineItem>, Double> lineItemAmountMap = new HashMap<List<LineItem>, Double>();
            for (Map.Entry<String, Set<RpLineItem>> reconcileItemMapEntry : reconcileItemMap.entrySet()) {
                for (Set<RpLineItem> rpLineItemSet : reconcileItemMap.values()) {
                    List<LineItem> reconcileLineItemList = new ArrayList<LineItem>();
                    Double reconcilableAmount = 0D;
                    for (RpLineItem rpLineItem : rpLineItemSet) {
                        LineItem lineItemForRP = rpLineItem.getLineItem();
                        if (reconcileLineItemList.add(lineItemForRP)) {
                            lineItemForRP.setQty(1l);
                        } else {
                            lineItemForRP.setQty(lineItemForRP.getQty() + 1);
                        }
                        reconcilableAmount += rpLineItem.getAmount();
                    }
                    lineItemAmountMap.put(reconcileLineItemList, reconcilableAmount);
                }
                ledgerLineItemAmountMap.put(reconcileItemMapEntry.getKey(), lineItemAmountMap);
            }
        }
        return ledgerLineItemAmountMap;
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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


	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}


	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

    public Map<String, Map<List<LineItem>, Double>> getLedgerLineItemAmountMap() {
        return ledgerLineItemAmountMap;
    }

    public void setLedgerLineItemAmountMap(Map<String, Map<List<LineItem>, Double>> ledgerLineItemAmountMap) {
        this.ledgerLineItemAmountMap = ledgerLineItemAmountMap;
    }

	/**
	 * @return the rto
	 */
	public boolean isRto() {
		return rto;
	}


	/**
	 * @param rto the rto to set
	 */
	public void setRto(boolean rto) {
		this.rto = rto;
	}

}
