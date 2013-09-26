package com.hk.admin.impl.service.shippingOrder;

import com.akube.framework.service.BasePaymentGatewayWrapper;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.courier.reversePickup.ReversePickupService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.EnumJitShippingOrderMailToCategoryReason;
import com.hk.constants.analytics.EnumReason;
import com.hk.constants.core.EnumTax;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.discount.EnumRewardPointTxnType;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.inventory.EnumReconciliationActionType;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumGateway;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.payment.EnumPaymentTransactionType;
import com.hk.constants.queue.EnumClassification;
import com.hk.constants.reversePickup.EnumReverseAction;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Tax;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Shipment;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointTxn;
import com.hk.domain.order.*;
import com.hk.domain.payment.Payment;
import com.hk.domain.reversePickupOrder.ReversePickupOrder;
import com.hk.domain.reversePickupOrder.RpLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.shippingOrder.ShippingOrderCategory;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.domain.user.UserAccountInfo;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.exception.NoSkuException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.impl.service.queue.BucketService;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.TaxDao;
import com.hk.pact.dao.reward.RewardPointDao;
import com.hk.pact.dao.reward.RewardPointTxnDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.user.UserAccountInfoDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.B2BOrderService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pact.service.splitter.ShippingOrderProcessor;
import com.hk.service.ServiceLocatorFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AdminShippingOrderServiceImpl implements AdminShippingOrderService {

	private static Logger logger = LoggerFactory.getLogger(AdminShippingOrderServiceImpl.class);
	@Autowired
	AwbService awbService;
	@Autowired
	UserService userService;
	@Autowired
	AdminEmailManager adminEmailManager;
	@Autowired
	SkuItemLineItemService skuItemLineItemService;
	@Autowired
	InventoryHealthService inventoryHealthService;
	@Autowired
	BaseDao baseDao;
	@Autowired
	PurchaseOrderService purchaseOrderService;
	@Autowired
	LineItemDao lineItemDao;
	@Autowired
	ShippingOrderDao shippingOrderDao;
	@Autowired
	SkuItemDao skuItemDao;
	@Autowired
	private ShippingOrderService shippingOrderService;
	@Autowired
	private AdminInventoryService adminInventoryService;
	@Autowired
	private BucketService bucketService;
	@Autowired
	private PincodeCourierService pincodeCourierService;
	private String cancellationRemark;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ShippingOrderStatusService shippingOrderStatusService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private WarehouseService warehouseService;
	private AdminOrderService adminOrderService;
	@Autowired
	private ShipmentService shipmentService;
	@Autowired
	private AdminShippingOrderDao adminShippingOrderDao;
	@Autowired
	private LoyaltyProgramService loyaltyProgramService;

    @Autowired
    ReversePickupService reversePickupService;

  @Autowired
  private PaymentService paymentService;
  @Autowired
  private RewardPointTxnDao rewardPointTxnDao;
  @Autowired
  RewardPointService rewardPointService;
  @Autowired
  private UserAccountInfoDao userAccountInfoDao;
  @Autowired
  private RewardPointDao rewardPointDao;

  @Autowired
  ShippingOrderProcessor shippingOrderProcessor;

  @Autowired
  B2BOrderService b2BOrderService;

  @Autowired
  TaxDao taxDao;


	public void cancelShippingOrder(ShippingOrder shippingOrder,String cancellationRemark,Long reconciliationType,
                                  boolean reconcileAll) {
		// Check if Order is in Action Queue before cancelling it.
		if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
			logger.warn("Cancelling Shipping order gateway id:::" + shippingOrder.getGatewayOrderId());
			shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Cancelled));
			skuItemLineItemService.freeInventoryForSOCancellation(shippingOrder);
			//shippingOrder = getShippingOrderService().save(shippingOrder);
//            getAdminInventoryService().reCheckInInventory(shippingOrder);
			getAdminInventoryService().reCheckInInventory(shippingOrder, EnumSkuItemStatus.Checked_IN, EnumSkuItemOwner.SELF,
          EnumInvTxnType.CANCEL_CHECKIN, 1L);
			// TODO : Write a generic ROLLBACK util which will essentially release all attached laibilities i.e.
			// inventory, reward points, shipment, discount
			getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Cancelled,
          shippingOrder.getReason(), cancellationRemark);

			orderService.updateOrderStatusFromShippingOrders(shippingOrder.getBaseOrder(),
          EnumShippingOrderStatus.SO_Cancelled, EnumOrderStatus.Cancelled);
			if (shippingOrder.getShipment() != null) {
				Awb awbToRemove = shippingOrder.getShipment().getAwb();
				awbService.preserveAwb(awbToRemove);
				Shipment shipmentToDelete = shippingOrder.getShipment();
				shippingOrder.setShipment(null);
				shipmentService.delete(shipmentToDelete);
				//shippingOrderService.save(shippingOrder);
			}

            if(!reconcileAll) {

                // if shipping order is of RO don't do any thing
                if(shippingOrder instanceof ReplacementOrder) {
                    // do nothing
                } else {
                    reconcileRPLiabilities(shippingOrder,shippingOrder.getBaseOrder());

                    if(reconciliationType != null) {
                        relieveExtraLiabilties(reconciliationType,shippingOrder,null);
                    }
                }

            }


			shippingOrder = getShippingOrderService().save(shippingOrder);
			if (shippingOrder.getPurchaseOrders() != null && shippingOrder.getPurchaseOrders().size() > 0) {
				adminEmailManager.sendJitShippingCancellationMail(shippingOrder, null,
            EnumJitShippingOrderMailToCategoryReason.SO_CANCELLED);
			}
			getBucketService().popFromActionQueue(shippingOrder);
		}
		for (LineItem lineItem : shippingOrder.getLineItems()) {
			getInventoryService().checkInventoryHealth(lineItem.getSku().getProductVariant());
		}
	}

    public void reconcileRPLiabilities(ShippingOrder shippingOrder, Order order) {
        /*Double refundValue = 0D;
        if (shippingOrder == null) {
            refundValue = order.getPayment().getAmount();
        } else {
            refundValue = shippingOrder.getAmount(); //TODO: handle for free cases
        }*/
        Double percentageAmount = calculateWeight(shippingOrder,order);

        // first redeem redeem points used by this SO/BO
        List<RewardPointTxn> rewardPointTxnList = rewardPointTxnDao.findByTxnTypeAndOrder(EnumRewardPointTxnType.REDEEM, order);
        for (RewardPointTxn rewardPointTxn : rewardPointTxnList) {
            rewardPointTxnDao.createRefundTxn(rewardPointTxn, rewardPointTxn.getValue()*percentageAmount);
        }


        /*List<RewardPoint> currentRewardPoints = rewardPointService.findRewardPoints(order, EnumRewardPointMode.getCashBackModes());
        for (RewardPoint currentRewardPoint : currentRewardPoints) {
            List<RewardPointTxn> currentRewardPointTxnList = rewardPointTxnDao.findByRewardPoint(currentRewardPoint);
            for (RewardPointTxn rewardPointTxn : currentRewardPointTxnList) {
                if (rewardPointTxn.isType(EnumRewardPointTxnType.ADD)) {
                    RewardPoint cashBackRewardPoints = rewardPointService.addRewardPoints(order.getUser(), userService.getAdminUser(),
                            order, percentageAmount * rewardPointTxn.getValue(), "", EnumRewardPointStatus.APPROVED,
                            EnumRewardPointMode.HK_ADJUSTMENTS.asRewardPointMode());
                    rewardPointService.approveRewardPoints(Arrays.asList(cashBackRewardPoints), rewardPointTxn.getExpiryDate());
                }
            }
        }*/

        // cancel reward points in partial manner
        List<RewardPoint> currentRewardPoints = rewardPointService.findRewardPoints(order, EnumRewardPointMode.getCashBackModes());

        for (RewardPoint rewardPoint : currentRewardPoints) {

            List<RewardPointTxn> txnList = rewardPointTxnDao.findByRewardPoint(rewardPoint);
            Double totalAddedRewardPoints = 0D;
            Double totalUsedRewardPoints = 0D;
            for (RewardPointTxn rewardPointTxn : txnList) {
                if (!rewardPointTxn.isType(EnumRewardPointTxnType.REFERRED_ORDER_CANCELLED)) {

                    if (rewardPointTxn.isType(EnumRewardPointTxnType.REDEEM) && rewardPointTxn.isType(EnumRewardPointTxnType.REFUND)) {
                        totalUsedRewardPoints += rewardPointTxn.getValue();
                    }

                    if (rewardPointTxn.isType(EnumRewardPointTxnType.ADD)) {
                        totalAddedRewardPoints = rewardPointTxn.getValue();
                    }
                }
            }

            // if total used is zero then, reward point has not been used yet, simple cancel totalRewardPointAdded
            if(totalUsedRewardPoints == 0 && totalAddedRewardPoints > 0) {
                rewardPointTxnDao.createRewardPointCancelTxn(rewardPoint, formatAmount(percentageAmount*totalAddedRewardPoints),txnList.get(0).getExpiryDate());
            } else {

                Double remainingRewardPoints = totalAddedRewardPoints - totalUsedRewardPoints;
                // now first cancel remaining reward points
                if (remainingRewardPoints > 0) {
                    rewardPointTxnDao.createRewardPointCancelTxn(rewardPoint, formatAmount(percentageAmount*totalAddedRewardPoints),txnList.get(0).getExpiryDate());
                }

                // cancel reward points from user total redeemable reward points in lieu of reward points used
                Double redeemablePoints = rewardPointService.getTotalRedeemablePoints(rewardPoint.getUser());
                if (redeemablePoints >= totalUsedRewardPoints) {

                    if(totalAddedRewardPoints > 0) {
                        rewardPointService.cancelRewardPoints(rewardPoint.getUser(), formatAmount(percentageAmount*totalUsedRewardPoints));
                    }

                } else {

                    if(redeemablePoints > 0) {
                        rewardPointService.cancelRewardPoints(rewardPoint.getUser(), formatAmount(percentageAmount*redeemablePoints));
                    }

                    // intimate user that redeemable reward points are being canceled from his account at last SO cancel
                    UserAccountInfo userAccountInfo = getUserAccountInfoDao().getOrCreateUserAccountInfo(rewardPoint.getUser());
                    userAccountInfo.setOverusedRewardPoints(userAccountInfo.getOverusedRewardPoints() + percentageAmount*totalUsedRewardPoints);
                    getUserAccountInfoDao().save(userAccountInfo);
                }
            }

            // now cancel reward Point when its amount has been zero
            if(isZeroRewardPointBalance(rewardPoint)) {
                rewardPointDao.cancelRewardPoint(rewardPoint);
            }
        }



        /*List<RewardPointTxn> redeemedRewardPointTxnList = rewardPointTxnDao.findByTxnTypeAndOrder(EnumRewardPointTxnType.REDEEM, order);
        for (RewardPointTxn rewardPointTxn : redeemedRewardPointTxnList) {
            Order concernedOrder = rewardPointTxn.getRewardPoint().getReferredOrder();
            RewardPoint refundRewardPoints = rewardPointService.addRewardPoints(order.getUser(), userService.getAdminUser(),
                    concernedOrder, percentageAmount * rewardPointTxn.getValue(), "", EnumRewardPointStatus.APPROVED,
                    EnumRewardPointMode.HK_ADJUSTMENTS.asRewardPointMode());
            rewardPointService.approveRewardPoints(Arrays.asList(refundRewardPoints), rewardPointTxn.getExpiryDate());
        }*/

    }


    private boolean isZeroRewardPointBalance(RewardPoint rewardPoint) {
        Double totalBalance = 0D;
        List<RewardPointTxn> txnList = rewardPointTxnDao.findByRewardPoint(rewardPoint);
        for (RewardPointTxn rewardPointTxn : txnList) {
            totalBalance += rewardPointTxn.getValue();
        }
        return (totalBalance < 1);
    }

    private double formatAmount(Double amount) {
        String amtStr = BasePaymentGatewayWrapper.TransactionData.decimalFormat.format(amount);
        return Double.parseDouble(amtStr);
    }

    private double calculateWeight(ShippingOrder shippingOrder, Order order) {
        double weightedFactor = 0D;

        if(shippingOrder == null) {
            weightedFactor = 1D;

        } else {

            if (EnumPaymentMode.FREE_CHECKOUT.getId().equals(order.getPayment().getPaymentMode().getId())) {
                double orderAmount = 0;

                Set<CartLineItem> cartLineItems = order.getCartLineItems();
                for (CartLineItem cartLineItem : cartLineItems) {
                    if (EnumCartLineItemType.Product.getId().equals(cartLineItem.getLineItemType().getId())) {
                        orderAmount += (cartLineItem.getHkPrice() - cartLineItem.getDiscountOnHkPrice()) * cartLineItem.getQty();
                    }
                }


                double shippingOrderAmount = 0;
                Set<LineItem> lineItems = shippingOrder.getLineItems();
                for (LineItem lineItem : lineItems) {
                    shippingOrderAmount += (lineItem.getHkPrice() - lineItem.getDiscountOnHkPrice()) * lineItem.getQty();
                }
                if (shippingOrderAmount != 0 && orderAmount != 0) {
                    weightedFactor = shippingOrderAmount/orderAmount;
                }


            } else {

                weightedFactor = shippingOrder.getAmount() / order.getPayment().getAmount();

            }

        }
        return weightedFactor;
    }

    private void relieveExtraLiabilties(Long reconciliationType, ShippingOrder shippingOrder, String comment) {
        User loggedOnUser = userService.getLoggedInUser();
        if (EnumReconciliationActionType.RewardPoints.getId().equals(reconciliationType)) {
            // TODO: giving reward points for one year - 12
            addRewardPoints(shippingOrder.getBaseOrder(), shippingOrder.getAmount(),loggedOnUser,12,comment);
            //paymentService.setRefundAmount(shippingOrder.getBaseOrder().getPayment(), shippingOrder.getAmount());
            getShippingOrderService().logShippingOrderActivity(shippingOrder, loggedOnUser,
                    EnumShippingOrderLifecycleActivity.Reconciliation.asShippingOrderLifecycleActivity(), EnumReason.RewardGiven.asReason(),comment);
            Payment payment = shippingOrder.getBaseOrder().getPayment();
            paymentService.createNewGenericPayment(payment, EnumPaymentStatus.REFUNDED.asPaymenStatus(), shippingOrder.getAmount(),
                    EnumPaymentMode.REWARD_POINT.asPaymenMode(), EnumPaymentTransactionType.REWARD_POINT);
        } else if (EnumReconciliationActionType.RefundAmount.getId().equals(reconciliationType)) {
            refundPayment(shippingOrder,comment);
        } else if (EnumReconciliationActionType.None.getId().equals(reconciliationType)) {
            getShippingOrderService().logShippingOrderActivity(shippingOrder, loggedOnUser,
                    EnumShippingOrderLifecycleActivity.Reconciliation.asShippingOrderLifecycleActivity(), EnumReason.NoActionTakenAtReconciliation.asReason(),comment);
        }
    }


    private void addRewardPoints(Order order, Double amount,User loggedOnUser,int expiryMonths,String comment) {

        if (amount > 0) {
            RewardPoint cancelRewardPoints = rewardPointService.addRewardPoints(order.getUser(), loggedOnUser,
                    order, amount, comment, EnumRewardPointStatus.APPROVED, EnumRewardPointMode.HK_ORDER_CANCEL_POINTS.asRewardPointMode());

            rewardPointService.approveRewardPoints(Arrays.asList(cancelRewardPoints),new DateTime().plusMonths(expiryMonths).toDate());
        }
    }

    private void refundPayment(ShippingOrder shippingOrder,String comment) {
        User loggedOnUser = userService.getLoggedInUser();
        if (shippingOrder.getAmount() > 0) {
            if(EnumGateway.getManualRefundGateways().contains(shippingOrder.getBaseOrder().getPayment().getGateway().getId())) {
                adminEmailManager.sendManualRefundTaskToAdmin(shippingOrder.getAmount(),
                        shippingOrder.getBaseOrder().getPayment().getGatewayOrderId(),shippingOrder.getBaseOrder().getPayment().getGateway().getName());
                getShippingOrderService().logShippingOrderActivity(shippingOrder, loggedOnUser,
                        EnumShippingOrderLifecycleActivity.Reconciliation.asShippingOrderLifecycleActivity(), EnumReason.ManualRefundInitiated.asReason(),comment);

            } else if (EnumGateway.getHKServiceEnabledGateways().contains(shippingOrder.getBaseOrder().getPayment().getGateway().getId())) {

                try {
                    Payment payment = paymentService.refundPayment(shippingOrder.getBaseOrder().getPayment().getGatewayOrderId(), shippingOrder.getAmount());
                    if (EnumPaymentStatus.REFUNDED.getId().equals(payment.getPaymentStatus().getId())) {
                        getShippingOrderService().logShippingOrderActivity(shippingOrder, loggedOnUser,
                                EnumShippingOrderLifecycleActivity.Reconciliation.asShippingOrderLifecycleActivity(), EnumReason.RefundSuccessful.asReason(),comment);
                    } else if (EnumPaymentStatus.REFUND_FAILURE.getId().equals(payment.getPaymentStatus().getId())) {
                        getShippingOrderService().logShippingOrderActivity(shippingOrder, loggedOnUser,
                                EnumShippingOrderLifecycleActivity.Reconciliation.asShippingOrderLifecycleActivity(),EnumReason.RefundFailed.asReason(),comment);

                    } else if (EnumPaymentStatus.REFUND_REQUEST_IN_PROCESS.getId().equals(payment.getPaymentStatus().getId())){
                        getShippingOrderService().logShippingOrderActivity(shippingOrder, loggedOnUser,
                                EnumShippingOrderLifecycleActivity.Reconciliation.asShippingOrderLifecycleActivity(),EnumReason.RefundInProcess.asReason(),comment);
                    }

                } catch (HealthkartPaymentGatewayException e) {
                    logger.debug("Exception occurred during payment refund",e);
                    getShippingOrderService().logShippingOrderActivity(shippingOrder,loggedOnUser,
                            EnumShippingOrderLifecycleActivity.Reconciliation.asShippingOrderLifecycleActivity(),EnumReason.RefundFailed.asReason(),comment);
                } catch (Exception e) {
                    logger.debug("Exception occurred during payment refund",e);
                    getShippingOrderService().logShippingOrderActivity(shippingOrder, loggedOnUser,
                            EnumShippingOrderLifecycleActivity.Reconciliation.asShippingOrderLifecycleActivity(),EnumReason.RefundFailed.asReason(),comment);
                }

            }

        }
    }


	public boolean updateWarehouseForShippingOrder(ShippingOrder shippingOrder, Warehouse warehouse) {
		Set<LineItem> lineItems = shippingOrder.getLineItems();
		boolean shouldUpdate = true;

		List<Sku> toSkuList = new ArrayList<Sku>();
		try {
			for (LineItem lineItem : lineItems) {
				toSkuList.clear();
				toSkuList.add(getSkuService().getSKU(lineItem.getSku().getProductVariant(), warehouse));
				List<SkuItem> skuItemList = getSkuItemDao().getSkuItems(toSkuList, Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()), Arrays.asList(EnumSkuItemOwner.SELF.getId()), lineItem.getMarkedPrice());
				if (skuItemList != null && skuItemList.size() >= lineItem.getQty()) {
					lineItem.setSku(toSkuList.get(0));
				} else {
					return false;
				}
			}
			shouldUpdate = skuItemLineItemService.isWarehouseBeFlippable(shippingOrder, warehouse);
			logger.debug("isWarehouseBeFlippable = " + shouldUpdate);
			if (shouldUpdate) {
				shippingOrder.setWarehouse(warehouse);
				shipmentService.recreateShipment(shippingOrder);
				shippingOrder = getShippingOrderService().save(shippingOrder);
				if (shippingOrder.getShippingOrderStatus().equals(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus()) && shippingOrder.getPurchaseOrders() != null && shippingOrder.getPurchaseOrders().size() > 0) {
					adminEmailManager.sendJitShippingCancellationMail(shippingOrder, null, EnumJitShippingOrderMailToCategoryReason.SO_WAREHOUSE_FLIPPED);
				}
				getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_WarehouseChanged);
				for (LineItem lineItem : shippingOrder.getLineItems()) {
					inventoryHealthService.inventoryHealthCheck(lineItem.getSku().getProductVariant());
				}
			}

		} catch (NoSkuException noSku) {
			shouldUpdate = false;
		}
		return shouldUpdate;
	}

	public ShippingOrder createSOforManualSplit(Set<CartLineItem> cartLineItems, Warehouse warehouse) {

		if (cartLineItems != null && !cartLineItems.isEmpty() && warehouse != null) {
			Order baseOrder = cartLineItems.iterator().next().getOrder();
			ShippingOrder shippingOrder = getShippingOrderService().createSOWithBasicDetails(baseOrder, warehouse);
			shippingOrder.setBaseOrder(baseOrder);

			for (CartLineItem cartLineItem : cartLineItems) {
				ProductVariant productVariant = cartLineItem.getProductVariant();
				Sku sku = getSkuService().getSKU(productVariant, warehouse);
				if (sku != null) {
					LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
					shippingOrder.getLineItems().add(shippingOrderLineItem);
				} else {
					throw new NoSkuException(productVariant, warehouse);
				}
			}

			ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, baseOrder);
			shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
			shippingOrder = getShippingOrderService().save(shippingOrder);
			/**
			 * this additional call to save is done so that we have shipping order id to generate shipping order gateway
			 * id
			 */
			shippingOrder = getShippingOrderService().setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
			shippingOrder = getShippingOrderService().save(shippingOrder);
			Set<ShippingOrderCategory> categories = getOrderService().getCategoriesForShippingOrder(shippingOrder);
			shippingOrder.setShippingOrderCategories(categories);
			shippingOrder.setBasketCategory(getOrderService().getBasketCategory(categories).getName());
			shippingOrder = getShippingOrderService().save(shippingOrder);

			//shipmentService.createShipment(shippingOrder);
			// auto escalate shipping orders if possible
			//getShippingOrderService().autoEscalateShippingOrder(shippingOrder);

			//		orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(baseOrder);

			//Validate SO for SkuItem booking
			shippingOrderService.validateShippingOrder(shippingOrder);

			return shippingOrder;
		}
		return null;
	}

	@Transactional
	public ShippingOrder putShippingOrderOnHold(ShippingOrder shippingOrder) {
		Long qty = 0L;
		if (shippingOrder.getShippingOrderStatus().getId() >= EnumShippingOrderStatus.SO_CheckedOut.getId()) {
			qty = 1L;
		}
		if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
			shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_OnHold));
//            getAdminInventoryService().reCheckInInventory(shippingOrder);
			getAdminInventoryService().reCheckInInventory(shippingOrder, EnumSkuItemStatus.BOOKED, EnumSkuItemOwner.SELF, EnumInvTxnType.CANCEL_CHECKIN, qty);
			shippingOrder = getShippingOrderService().save(shippingOrder);
			getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_PutOnHold);
		}
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder unHoldShippingOrder(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ActionAwaiting));
		shippingOrder = getShippingOrderService().save(shippingOrder);

		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_RemovedOnHold);
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder
	markShippingOrderAsDelivered(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Delivered));
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Delivered);
		Order order = shippingOrder.getBaseOrder();
		getAdminOrderService().markOrderAsDelivered(order);
		loyaltyProgramService.approveKarmaPoints(shippingOrder.getBaseOrder());
//	    smsManager.sendOrderDeliveredSMS(shippingOrder);
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder markShippingOrderAsInstalled(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Installed));
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Installed);
		Order order = shippingOrder.getBaseOrder();
		getAdminOrderService().markOrderAsCompletedWithInstallation(order);
//	    smsManager.sendOrderDeliveredSMS(shippingOrder);
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder markShippingOrderAsRTO(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_RTO));
		shippingOrder.getShipment().setReturnDate(new Date());
		shippingOrder.getShipment().setRtoInitiatedDate(new Date());
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Returned);
		Order order = shippingOrder.getBaseOrder();
		getAdminOrderService().markOrderAsRTO(order);
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder markShippingOrderAsLost(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Lost));
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Lost);
		Order order = shippingOrder.getBaseOrder();
		getAdminOrderService().markOrderAsLost(order);
		return shippingOrder;
	}

	public ShippingOrder initiateRTOForShippingOrder(ShippingOrder shippingOrder, ReplacementOrderReason rtoReason) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.RTO_Initiated));
		getShippingOrderService().save(shippingOrder);
		if (rtoReason != null) {
			getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RTO_Initiated, null, rtoReason.getName());


		} else {
			getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RTO_Initiated);
		}
		return shippingOrder;
	}

	public List<ShippingOrder> getShippingOrderListByCouriers(Date startDate, Date endDate, List<Long> courierId) {
		return getAdminShippingOrderDao().getShippingOrderListByCouriers(startDate, endDate, courierId);
	}

	@Transactional
	public ShippingOrder markShippingOrderAsShipped(ShippingOrder shippingOrder) {
		Shipment shipment = shippingOrder.getShipment();
		if (shipment != null) {
			shipment.getAwb().setAwbStatus(EnumAwbStatus.Used.getAsAwbStatus());
			shipment.setShipDate(new Date());
			getShipmentService().save(shipment);
		}
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Shipped));
		getPincodeCourierService().setTargetDeliveryDate(shippingOrder);
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Shipped);
		getBucketService().popFromActionQueue(shippingOrder);
		getAdminOrderService().markOrderAsShipped(shippingOrder.getBaseOrder());
    if(shippingOrder.getBaseOrder().getB2bOrder() != null && shippingOrder.getBaseOrder().getB2bOrder()){
      updateSOForB2BOrders(shippingOrder);
    }
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder markShippingOrderAsPrinted(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_MarkedForPrinting));
		getShippingOrderService().save(shippingOrder);

		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ChosenForPrinting);

		return shippingOrder;
	}

	@Transactional
	public ShippingOrder moveShippingOrderToPickingQueue(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Picking));
		getShippingOrderService().save(shippingOrder);

		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_InPicking);

		return shippingOrder;
	}

	@Transactional
	public ShippingOrder moveShippingOrderBackToActionQueue(ShippingOrder shippingOrder) {
		Long qty = 0L;
		if (shippingOrder.getShippingOrderStatus().getId() >= EnumShippingOrderStatus.SO_CheckedOut.getId()) {
			qty = 1L;
		}
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_OnHold));
//        getAdminInventoryService().reCheckInInventory(shippingOrder);

		getAdminInventoryService().reCheckInInventory(shippingOrder, EnumSkuItemStatus.BOOKED, EnumSkuItemOwner.SELF, EnumInvTxnType.CANCEL_CHECKIN, qty);
		shippingOrder = getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_EscalatedBackToActionQueue, shippingOrder.getReason(), null);

		getBucketService().escalateBackToActionQueue(shippingOrder);
		return shippingOrder;
	}

  /**
   * This overloaded method is used for auto processing at escalate back and is created to support legacy code.
   */
  @Transactional
  public ShippingOrder moveShippingOrderBackToActionQueue(ShippingOrder shippingOrder, Boolean autoProcess) {
    Long qty = 0L;
    if (shippingOrder.getShippingOrderStatus().getId() >= EnumShippingOrderStatus.SO_CheckedOut.getId()) {
      qty = 1L;
    }
    if (autoProcess) {
      shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ActionAwaiting));
    } else {
      shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_OnHold));
    }

    getAdminInventoryService().reCheckInInventory(shippingOrder,
        EnumSkuItemStatus.BOOKED, EnumSkuItemOwner.SELF, EnumInvTxnType.CANCEL_CHECKIN, qty);
    shippingOrder = getShippingOrderService().save(shippingOrder);
    getShippingOrderService().logShippingOrderActivity(shippingOrder,
        EnumShippingOrderLifecycleActivity.SO_EscalatedBackToActionQueue, shippingOrder.getReason(), null);

    getBucketService().escalateBackToActionQueue(shippingOrder);
    // after escalate back auto escalate the SO
    shippingOrderProcessor.manualEscalateShippingOrder(shippingOrder);
    return shippingOrder;
  }

	@Transactional
	public ShippingOrder moveShippingOrderBackToPackingQueue(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForProcess));
//        getAdminInventoryService().reCheckInInventory(shippingOrder);

		getAdminInventoryService().reCheckInInventory(shippingOrder, EnumSkuItemStatus.BOOKED, EnumSkuItemOwner.SELF, EnumInvTxnType.CANCEL_CHECKIN, 0L);
		getShippingOrderService().save(shippingOrder);

		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_BackToPackingQueue);

		return shippingOrder;
	}

	@Transactional
	public ShippingOrder moveShippingOrderBackToDropShippingQueue(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForDropShipping));
//           getAdminInventoryService().reCheckInInventory(shippingOrder);
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_BackToDropShippingQueue);
		return shippingOrder;
	}

	public ReplacementOrderReason getRTOReasonForShippingOrder(ShippingOrder shippingOrder) {
		String rtoReason = null;
		ReplacementOrderReason replacementOrderReason = null;
		for (ShippingOrderLifecycle shippingOrderLifecycle : shippingOrder.getShippingOrderLifecycles()) {
			if (shippingOrderLifecycle.getShippingOrderLifeCycleActivity().getId().equals(EnumShippingOrderLifecycleActivity.RTO_Initiated.getId())) {
				if (shippingOrderLifecycle.getComments() != null) {
					replacementOrderReason = getReplacementOrderReasonByName(shippingOrderLifecycle.getComments());
				}
			}
		}
		return replacementOrderReason;
	}

	public ReplacementOrderReason getReplacementOrderReasonByName(String replacementOrderReasonString) {
		List<ReplacementOrderReason> replacementOrderReasonList = getAdminShippingOrderDao().getAll(ReplacementOrderReason.class);
		for (ReplacementOrderReason replacementOrderReason : replacementOrderReasonList) {
			if (replacementOrderReasonString.contains(replacementOrderReason.getName())) {
				return replacementOrderReason;
			}
		}
		return null;
	}

	public void adjustPurchaseOrderForSplittedShippingOrder(ShippingOrder shippingOrder, ShippingOrder newShippingOrder) {
		List<PurchaseOrder> poList = shippingOrder.getPurchaseOrders();
		Set<PurchaseOrder> newShippingOrderPoSet = new HashSet<PurchaseOrder>();
		Set<PurchaseOrder> parentShippingOrderPoSet = new HashSet<PurchaseOrder>();
		List<ProductVariant> variantListFromSO = new ArrayList<ProductVariant>();
		for (LineItem item : shippingOrder.getLineItems()) {
			variantListFromSO.add(item.getSku().getProductVariant());
		}

		if (poList != null && poList.size() > 0) {
			for (PurchaseOrder order : poList) {
				boolean flag = false;
				List<ProductVariant> productVariants = purchaseOrderService.getAllProductVariantFromPO(order);
				if (productVariants != null && productVariants.size() > 0) {
					for (ProductVariant pv : productVariants) {
						if (variantListFromSO.contains(pv)) {
							flag = true;
						}
						boolean soHasPv = shippingOrderService.shippingOrderContainsProductVariant(newShippingOrder, pv, pv.getMarkedPrice());
						if (soHasPv) {
							newShippingOrderPoSet.add(order);
						}
					}
				}
				if (flag == true) {
					parentShippingOrderPoSet.add(order);
				}
			}
		}


		//shippingOrder = shippingOrderService.save(shippingOrder);
		newShippingOrder.setPurchaseOrders(new ArrayList<PurchaseOrder>(newShippingOrderPoSet));
		shippingOrder.setPurchaseOrders(new ArrayList<PurchaseOrder>(parentShippingOrderPoSet));

		newShippingOrder = shippingOrderService.save(newShippingOrder);
		shippingOrder = shippingOrderService.save(shippingOrder);

		for (PurchaseOrder po : newShippingOrderPoSet) {

			List<ShippingOrder> soList = po.getShippingOrders();
			soList.add(newShippingOrder);
			if (!parentShippingOrderPoSet.contains(po)) {
				soList.remove(shippingOrder);
			}
			po.setShippingOrders(soList);
			baseDao.save(po);
		}
		adminEmailManager.sendJitShippingCancellationMail(shippingOrder, newShippingOrder, EnumJitShippingOrderMailToCategoryReason.SO_SPLITTED);

	}

  @Override
  public Boolean updateSOForB2BOrders(ShippingOrder shippingOrder) {
    Order baseOrder = shippingOrder.getBaseOrder();
    if(!baseOrder.getB2bOrder()){
      return false;
    }
    else{
      if(b2BOrderService.checkCForm(baseOrder)){
        Tax cstTax = taxDao.findById(EnumTax.CST.getId());
        for(LineItem lineItem : shippingOrder.getLineItems()){
          lineItem.setTax(cstTax);
          getAdminShippingOrderDao().save(lineItem);
        }
 //       getAdminShippingOrderDao().save(shippingOrder.getLineItems());
      }
      return true;
    }
  }

    /**
     * This method returns the lineItems for replacement order and amount for refund/reward
     * It has been created in generic way so as to move to a helper class for code reuse.
     *
     * @param localShippingOrder
     * @param actionTypeConstant
     * @return
     */
    public Object getActionProcessingElement(ShippingOrder localShippingOrder,
    				Set<LineItem> toBeProcessedLineItemSet, Integer actionTypeConstant) {
        Integer SEARCH_ACTION = 4;
        Double toBeProcessedAmount = 0d;

        if (localShippingOrder != null) {
            List<ReversePickupOrder> reversePickupOrders = reversePickupService.getReversePickupsForSO(localShippingOrder);
            if (reversePickupOrders != null && !reversePickupOrders.isEmpty()) {
                for (ReversePickupOrder reversePickupOrder : reversePickupOrders) {
                    List<RpLineItem> rpLineItems = reversePickupOrder.getRpLineItems();
                    if (rpLineItems != null && !rpLineItems.isEmpty()) {
                        for (RpLineItem rpLineItem : rpLineItems) {
                            if (EnumReverseAction.Approved.getId().equals(rpLineItem.getCustomerActionStatus().getId())) {
                            	if (!SEARCH_ACTION.equals(actionTypeConstant)) {
                            		rpLineItem.setCustomerActionStatus(EnumClassification.ReconciledGeneric.asClassification());
                            	}
                                LineItem lineItemForRP = rpLineItem.getLineItem();
                                if (toBeProcessedLineItemSet.contains(lineItemForRP)) {
                                	toBeProcessedLineItemSet.remove(lineItemForRP);
                                    lineItemForRP.setQty(lineItemForRP.getQty() + 1);
                                } else {
                                    lineItemForRP.setQty(1l);
                                }
                                toBeProcessedLineItemSet.add(lineItemForRP);
                                toBeProcessedAmount += rpLineItem.getAmount();
                            }
                        }
                    }
                }
            }
        }
        return  toBeProcessedAmount;
    }


    public ShippingOrderService getShippingOrderService() {
		return shippingOrderService;
	}

	public void setShippingOrderService(ShippingOrderService shippingOrderService) {
		this.shippingOrderService = shippingOrderService;
	}

	public ShippingOrderStatusService getShippingOrderStatusService() {
		return shippingOrderStatusService;
	}

	public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
		this.shippingOrderStatusService = shippingOrderStatusService;
	}

	public SkuService getSkuService() {
		return skuService;
	}

	public void setSkuService(SkuService skuService) {
		this.skuService = skuService;
	}

	public WarehouseService getWarehouseService() {
		return warehouseService;
	}

	public void setWarehouseService(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	public AdminOrderService getAdminOrderService() {
		if (adminOrderService == null) {
			adminOrderService = ServiceLocatorFactory.getService(AdminOrderService.class);
		}
		return adminOrderService;
	}

	public void setAdminOrderService(AdminOrderService adminOrderService) {
		this.adminOrderService = adminOrderService;
	}

	public ShipmentService getShipmentService() {
		return shipmentService;
	}

	public void setShipmentService(ShipmentService shipmentService) {
		this.shipmentService = shipmentService;
	}

	public AdminInventoryService getAdminInventoryService() {
		return adminInventoryService;
	}

	public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
		this.adminInventoryService = adminInventoryService;
	}

	public InventoryService getInventoryService() {
		return inventoryService;
	}

	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	public AdminShippingOrderDao getAdminShippingOrderDao() {
		return adminShippingOrderDao;
	}

	public void setAdminShippingOrderDao(AdminShippingOrderDao adminShippingOrderDao) {
		this.adminShippingOrderDao = adminShippingOrderDao;
	}

	public OrderService getOrderService() {
		if (orderService == null) {
			this.orderService = ServiceLocatorFactory.getService(OrderService.class);
		}
		return orderService;
	}

	public UserService getUserService() {
		return userService;
	}

	public BucketService getBucketService() {
		return bucketService;
	}

	public void setBucketService(BucketService bucketService) {
		this.bucketService = bucketService;
	}

	public PincodeCourierService getPincodeCourierService() {
		return pincodeCourierService;
	}

	public void setPincodeCourierService(PincodeCourierService pincodeCourierService) {
		this.pincodeCourierService = pincodeCourierService;
	}

	public String getCancellationRemark() {
		return cancellationRemark;
	}

	public void setCancellationRemark(String cancellationRemark) {
		this.cancellationRemark = cancellationRemark;
	}

	public SkuItemDao getSkuItemDao() {
		return skuItemDao;
	}

    public UserAccountInfoDao getUserAccountInfoDao() {
        return userAccountInfoDao;
    }

    public void setUserAccountInfoDao(UserAccountInfoDao userAccountInfoDao) {
        this.userAccountInfoDao = userAccountInfoDao;
    }

}
