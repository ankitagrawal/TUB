package com.hk.web.action.admin.crm;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.reversePickup.ReversePickupService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.admin.pact.service.shippingOrder.ReplacementOrderService;
import com.hk.constants.analytics.EnumReasonType;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.discount.RewardPointConstants;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.payment.EnumGateway;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.payment.EnumPaymentTransactionType;
import com.hk.constants.reversePickup.EnumReverseAction;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.analytics.Reason;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.order.*;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.Payment;
import com.hk.domain.reverseOrder.ReverseLineItem;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.reversePickupOrder.ReversePickupOrder;
import com.hk.domain.reversePickupOrder.RpLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.exception.InvalidRewardPointsException;
import com.hk.helper.ReplacementOrderHelper;
import com.hk.pact.dao.reward.RewardPointDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.taglibs.Functions;
import com.hk.web.action.admin.user.SearchUserAction;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.apache.commons.lang.math.NumberUtils;
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

  private Long shippingOrderId;
  private Long baseOrderId;

  private String gatewayOrderId;

  private ShippingOrder shippingOrder;

  private List<LineItem> lineItems = new ArrayList<LineItem>();
  private List<ReverseLineItem> reverseLineItems = new ArrayList<ReverseLineItem>();
  private ReplacementOrderReason replacementOrderReason;
  private ReverseOrder reverseOrder;
  private Double paymentAmount;
  private Reason refundReason;
  private String refundComments;
  private String replacementComments;
  private ReplacementOrder replacementOrder;

  private  Payment payment;

  @Validate(required = true, on = "addRewardPoints")
  private String comment;

  @Validate(required = true, on = "addRewardPoints")
  private RewardPointMode rewardPointMode;

  @Validate(required = true, on = "addRewardPoints")
  private Date expiryDate;

  @Autowired
  ShippingOrderService shippingOrderService;

  @Autowired
  ReverseOrderService reverseOrderService;

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
  LineItemDao lineItemDao;

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/crm/crmMasterControl.jsp");
  }

  @ValidationMethod(on = "searchShippingOrder")
  public void validateSearch() {
    if (shippingOrderId == null && gatewayOrderId == null) {
      getContext().getValidationErrors().add("1", new SimpleError("Please Enter a Search Parameter"));
    }
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
      reverseOrder = reverseOrderService.getReverseOrderByShippingOrderId(shippingOrderId);
      if(reverseOrder != null){
        lineItems = new ArrayList<LineItem>();
        for(ReverseLineItem reverseLineItem: reverseOrder.getReverseLineItems()){
          LineItem replacementLineItem = ReplacementOrderHelper.getLineItemForReplacementOrder(reverseLineItem.getReferredLineItem());
          replacementLineItem.setQty(reverseLineItem.getReturnQty());
          lineItems.add(replacementLineItem);
        }
      }
    }
    return new ForwardResolution(MasterResolutionAction.class).addParameter("shippingOrderId", shippingOrderId);
  }

  public Resolution addRewardPoints() {
    rewardFlag = true;
    Double rewardAmount = (Double)this.getActionProcessingElement(shippingOrder, this.REWARD_ACTION);
    User referredUser = getUserService().getUserById(getPrincipal().getId());
    // referredUser stores the id of the user who added reward points
    // this logs the user who has added reward points
    boolean rewardPointsAdded = true;
    User user = shippingOrder.getBaseOrder().getUser();
    if(user.equals(referredUser)){
      addRedirectAlertMessage(new SimpleMessage("A user cannot give reward points to himself"));
      return new RedirectResolution(SearchUserAction.class, "search");
    }
    RewardPoint rewardPoint = new RewardPoint();
    Order order = orderService.find(baseOrderId);
    try {
      if (rewardAmount >= RewardPointConstants.MAX_REWARD_POINTS) {
        throw new InvalidRewardPointsException(rewardAmount);
      }
      rewardPoint = rewardPointService.addRewardPoints(user, referredUser, order, rewardAmount, comment,
          EnumRewardPointStatus.APPROVED, rewardPointMode);
    } catch (InvalidRewardPointsException e) {
      rewardPointsAdded = false;
    }
    if (rewardPointsAdded) {
      rewardPointService.approveRewardPoints(Arrays.asList(rewardPoint), expiryDate);
      Payment payment = order.getPayment();
      paymentService.createNewGenericPayment(payment, EnumPaymentStatus.REWARD.asPaymenStatus(), rewardAmount,
          EnumPaymentMode.REWARD_POINT_MODE.asPaymenMode(), EnumPaymentTransactionType.REWARD);
      addRedirectAlertMessage(new SimpleMessage("Reward Points added successfully"));
      return new RedirectResolution(SearchUserAction.class, "search");
    } else {
      addRedirectAlertMessage(new SimpleMessage("Reward Points cannot be more than " + RewardPointDao.MAX_REWARD_POINTS));
      return new RedirectResolution(SearchUserAction.class, "search");
    }
  }

  public Resolution createReplacementOrder() {
    replacementFlag = true;
    List<LineItem> lineItems = new ArrayList<LineItem>();
    lineItems.addAll((Set<LineItem>)this.getActionProcessingElement(shippingOrder,this.REPLACEMENT_ACTION));

    if (lineItems.isEmpty() || replacementOrderReason == null) {
      addRedirectAlertMessage(new SimpleMessage("No reason selected or no appropriate items found for creating replacement order."));
      return new ForwardResolution(MasterResolutionAction.class);
    }

    for (LineItem lineItem : lineItems) {
      String productName = lineItem.getCartLineItem().getProductVariant().getProduct().getName();
      if (lineItem.getQty() > inventoryService.getAllowedStepUpInventory(lineItem.getSku().getProductVariant())) {
        addRedirectAlertMessage(new SimpleMessage("Unable to create replacement order as " +
           productName  + " out of stock."));
        return new ForwardResolution(MasterResolutionAction.class);
      }
    }

    boolean isRTO = EnumShippingOrderStatus.SO_RTO.getId().equals(shippingOrder.getOrderStatus().getId()) ||
                      EnumShippingOrderStatus.RTO_Initiated.getId().equals(shippingOrder.getOrderStatus().getId());
    replacementOrder = replacementOrderService.createReplaceMentOrder(shippingOrder, lineItems, isRTO,
        replacementOrderReason, replacementComments);
    if (replacementOrder == null) {
      addRedirectAlertMessage(new SimpleMessage("Unable to create replacement order."));
    } else {
      addRedirectAlertMessage(new SimpleMessage("The Replacement order created. New gateway order id: " + replacementOrder.getGatewayOrderId()));
    }
    return new ForwardResolution(MasterResolutionAction.class);
  }

  @DontValidate
  @Secure(hasAnyPermissions = {PermissionConstants.REFUND_PAYMENT}, authActionBean = AdminPermissionAction.class)
  public Resolution refundPayment() {
    refundFlag = true;
    Double refundAmount = (Double) this.getActionProcessingElement(shippingOrder, this.REFUND_ACTION);
      if (refundAmount > 0 && refundReason !=null && refundComments!= null && !refundComments.isEmpty()) {
        Payment basePayment = shippingOrder.getBaseOrder().getPayment();
        String paymentGatewayOrderId = basePayment.getGatewayOrderId();
        Gateway gateway = basePayment.getGateway();
        if (gateway != null && EnumGateway.getHKServiceEnabledGateways().contains(gateway.getId())) {
          if (isRefundAmountValid(paymentGatewayOrderId, refundAmount)) {
            try {

              if (EnumPaymentStatus.SUCCESS.getId().equals(basePayment.getPaymentStatus().getId())) {
                payment = paymentService.refundPayment(paymentGatewayOrderId, refundAmount);
                User loggedOnUser = getUserService().getLoggedInUser();
                String loggingComment = refundReason.getClassification().getPrimary() + "- " + refundComments;
                if (payment != null) {
                  if (EnumPaymentStatus.REFUNDED.getId().equals(payment.getPaymentStatus().getId())) {
                    adminOrderService.logOrderActivity(basePayment.getOrder(), loggedOnUser,
                        EnumOrderLifecycleActivity.AmountRefundedOrderCancel.asOrderLifecycleActivity(),
                        loggingComment);

                  } else if (EnumPaymentStatus.REFUND_FAILURE.getId().equals(payment.getPaymentStatus().getId())) {
                    adminOrderService.logOrderActivity(basePayment.getOrder(), loggedOnUser,
                        EnumOrderLifecycleActivity.RefundAmountFailed.asOrderLifecycleActivity(), loggingComment);
                  } else if (EnumPaymentStatus.REFUND_REQUEST_IN_PROCESS.getId().equals(payment.getPaymentStatus().getId())){
                    adminOrderService.logOrderActivity(basePayment.getOrder(), loggedOnUser,
                        EnumOrderLifecycleActivity.RefundAmountInProcess.asOrderLifecycleActivity(), loggingComment);
                  }
                } else {
                  adminOrderService.logOrderActivity(basePayment.getOrder(), loggedOnUser,
                      EnumOrderLifecycleActivity.RefundAmountFailed.asOrderLifecycleActivity(), loggingComment);
                  addRedirectAlertMessage(new SimpleMessage("Refund failed."));
                }
              } else {
                addRedirectAlertMessage(new SimpleMessage("Refund can only be initiated on successful payment"));
              }
            } catch (HealthkartPaymentGatewayException e) {
              addRedirectAlertMessage(new SimpleMessage("Payment Seek exception for gateway order id " + paymentGatewayOrderId));
            }
          } else {
            addRedirectAlertMessage(new SimpleMessage("Amount cannot exceed total remaining amount"));
          }

        } else {
          addRedirectAlertMessage(new SimpleMessage("Refund feature only works for citrus/icici/ebs"));
        }
      } else {
        addRedirectAlertMessage(new SimpleMessage("Please enter amount as well as reason and related comments for the refund."));
      }
    return new ForwardResolution(MasterResolutionAction.class);
  }

  private boolean isRefundAmountValid(String gatewayOrderId, Double amount) {
    Payment basePayment = paymentService.findByGatewayOrderId(gatewayOrderId);
    List<PaymentStatus> refundStatus = Arrays.asList(EnumPaymentStatus.REFUNDED.asPaymenStatus());
    List<Payment> refundPayments = paymentService.searchPayments(null, refundStatus, null, null, null, null, null, basePayment, null);
    double totalRefundAmount = 0;
    if (refundPayments != null && !refundPayments.isEmpty()) {
      for (Payment payment : refundPayments) {
        totalRefundAmount = totalRefundAmount +  payment.getAmount();
      }
    }
    if (basePayment != null && basePayment.getAmount() != null) {
      if ((basePayment.getAmount() - (totalRefundAmount + amount)) >= 0f) {
        return true;
      }
    }
    return false;
  }


  /**
   * This method returns the lineItems for replacement order and amount for refunf/reward
   * It has been created in generic way so as to move to a helper class for code reuse.
   * @param localShippingOrder
   * @param actionTypeConstant
   * @return
   */

  private Object getActionProcessingElement(ShippingOrder localShippingOrder, Integer actionTypeConstant) {
    Set<LineItem> toBeProcessedLineItemSet = new HashSet<LineItem>();
    Double toBeProcessedAmount = 0d;
    if (localShippingOrder!=null) {
      Long shippingOrderStatusId = localShippingOrder.getOrderStatus().getId();
      if(EnumShippingOrderStatus.SO_RTO.getId().equals(shippingOrderStatusId)
          || EnumShippingOrderStatus.SO_Lost.getId().equals(shippingOrderStatusId)
          || EnumShippingOrderStatus.RTO_Initiated.getId().equals(shippingOrderStatusId)){
        // all lineItems would be considered
        toBeProcessedLineItemSet.addAll(localShippingOrder.getLineItems());
        toBeProcessedAmount+= localShippingOrder.getAmount();
        if(actionTypeConstant.equals(this.REPLACEMENT_ACTION)) {
          return toBeProcessedLineItemSet;
        } else {
          return toBeProcessedAmount;
        }
      }

      List<ReversePickupOrder> reversePickupOrders = reversePickupService.getReversePickupsForSO(localShippingOrder);
      if (reversePickupOrders!= null && !reversePickupOrders.isEmpty() ) {
        for (ReversePickupOrder reversePickupOrder: reversePickupOrders) {
          List<RpLineItem> rpLineItems = reversePickupOrder.getRpLineItems();
          if (rpLineItems!=null && !rpLineItems.isEmpty()) {
            for (RpLineItem rpLineItem: rpLineItems) {
              if (EnumReverseAction.Approved.getId().equals(rpLineItem.getCustomerActionStatus())) {
                LineItem lineItemForRP = rpLineItem.getLineItem();
                if (toBeProcessedLineItemSet.add(lineItemForRP)) {
                  lineItemForRP.setQty(1l);
                } else {
                  lineItemForRP.setQty(lineItemForRP.getQty() + 1);
                }
                toBeProcessedAmount+= rpLineItem.getLineItem().getHkPrice();
              }
            }
          }
        }
      }
    }
    if(actionTypeConstant.equals(this.REPLACEMENT_ACTION)) {
      return toBeProcessedLineItemSet;
    } else {
      return toBeProcessedAmount;
    }
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

  public ReverseOrder getReverseOrder() {
    return reverseOrder;
  }

  public void setReverseOrder(ReverseOrder reverseOrder) {
    this.reverseOrder = reverseOrder;
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
    return  Functions.getReasonsByType(EnumReasonType.REFUND.getName());
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

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public String getReplacementComments() {
    return replacementComments;
  }

  public void setReplacementComments(String replacementComments) {
    this.replacementComments = replacementComments;
  }
}
