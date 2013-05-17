package com.hk.admin.impl.service.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.fliter.ShippingOrderFilter;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.core.CancellationType;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.User;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.manager.EmailManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.manager.SMSManager;
import com.hk.manager.StoreOrderService;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AffilateService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.review.ReviewCollectionFrameworkService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.store.StoreService;
import com.hk.pact.service.subscription.SubscriptionOrderService;
import com.hk.service.ServiceLocatorFactory;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private static Logger             logger = LoggerFactory.getLogger(AdminOrderService.class);

    @Autowired
    private UserService               userService;
    @Autowired
    private OrderStatusService        orderStatusService;
    @Autowired
    private RewardPointService        rewardPointService;
    @Autowired
    private OrderService              orderService;
    private AdminShippingOrderService adminShippingOrderService;
    @Autowired
    ShippingOrderService              shippingOrderService;
    @Autowired
    ShipmentService                   shipmentService;
    @Autowired
    private AffilateService           affilateService;
    @Autowired
    InventoryService                  inventoryService;
    @Autowired
    LineItemDao                       lineItemDao;
    @Autowired
    private ReferrerProgramManager    referrerProgramManager;
    @Autowired
    private EmailManager              emailManager;
    @Autowired
    private OrderLoggingService       orderLoggingService;
    @Autowired
    private SubscriptionOrderService  subscriptionOrderService;
    @Autowired
    private StoreService              storeService;
    @Autowired
    private StoreOrderService         storeOrderService;
    @Autowired
    private AdminEmailManager         adminEmailManager;
    @Autowired
    private CourierService            courierService;
    @Autowired
    private PincodeCourierService pincodeCourierService;
	@Autowired
    private SMSManager                smsManager;

	@Autowired
	private LoyaltyProgramService loyaltyProgramService;
	
	@Autowired
	PaymentManager paymentManager;

    @Autowired
    ReviewCollectionFrameworkService reviewCollectionFrameworkService;

    @Value("#{hkEnvProps['" + Keys.Env.codMinAmount + "']}")
    private Double                    codMinAmount;

    @Value("#{hkEnvProps['codMaxAmount']}")
    private Double                    codMaxAmount;



    @Override
	@Transactional
    public Order putOrderOnHold(Order order) {

        ShippingOrderFilter shippingOrderFilter = new ShippingOrderFilter(order.getShippingOrders());
        Set<ShippingOrder> shippingOrdersToPutOnHold = shippingOrderFilter.filterShippingOrdersByStatus(EnumShippingOrderStatus.getStatusForPuttingOrderOnHold());

        for (ShippingOrder shippingOrder : shippingOrdersToPutOnHold) {
            this.getAdminShippingOrderService().putShippingOrderOnHold(shippingOrder);
        }
        order.setOrderStatus(this.getOrderService().getOrderStatus(EnumOrderStatus.OnHold));
        order = this.getOrderService().save(order);

        /**
         * Order lifecycle activity logging - Order Put OnHold
         */
        this.logOrderActivity(order, EnumOrderLifecycleActivity.OrderPutOnHold);

        return order;
    }

    @Override
	@Transactional
    public void cancelOrder(Order order, CancellationType cancellationType, String cancellationRemark, User loggedOnUser) {
        boolean shouldCancel = true;

        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
            if (!EnumShippingOrderStatus.SO_ActionAwaiting.getId().equals(shippingOrder.getOrderStatus().getId())) {
                shouldCancel = false;
                break;
            }
        }

        if (shouldCancel) {
            order.setOrderStatus((this.getOrderStatusService().find(EnumOrderStatus.Cancelled)));
            order.setCancellationType(cancellationType);
            order.setCancellationRemark(cancellationRemark);
            order = this.getOrderService().save(order);

            Set<ShippingOrder> shippingOrders = order.getShippingOrders();
            if (shippingOrders != null && !shippingOrders.isEmpty()) {
                for (ShippingOrder shippingOrder : order.getShippingOrders()) {
                    this.getAdminShippingOrderService().cancelShippingOrder(shippingOrder,null);
                }
            } else {
                Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
                for (CartLineItem cartLineItem : cartLineItems) {
                    this.inventoryService.checkInventoryHealth(cartLineItem.getProductVariant());
                }
            }

            this.affilateService.cancelTxn(order);

            if (order.getRewardPointsUsed() != null && order.getRewardPointsUsed() > 0) {
                this.referrerProgramManager.refundRedeemedPoints(order);
            }
            List<RewardPoint> rewardPointList = this.getRewardPointService().findByReferredOrder(order);
            if (rewardPointList != null && rewardPointList.size() > 0) {
                for (RewardPoint rewardPoint : rewardPointList) {
                    this.rewardPointService.cancelReferredOrderRewardPoint(rewardPoint);
                }
            }

	        this.loyaltyProgramService.cancelLoyaltyPoints(order);

            // Send Email Comm. for HK Users Only
            if (order.getStore() != null && order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID)) {
                this.emailManager.sendOrderCancelEmailToUser(order);
            }
            this.emailManager.sendOrderCancelEmailToAdmin(order);

            this.logOrderActivity(order, loggedOnUser, this.getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderCancelled), cancellationRemark);
        } else {
            String comment = "All SOs of BO#" + order.getGatewayOrderId() + " are not in Action Awaiting Status - Aborting Cancellation.";
            logger.info(comment);
            this.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.LoggedComment, comment);
        }
    }

    @Override
	@Transactional
    public Order unHoldOrder(Order order) {

        ShippingOrderFilter shippingOrderFilter = new ShippingOrderFilter(order.getShippingOrders());
        Set<ShippingOrder> shippingOrdersToPutOnHold = shippingOrderFilter.filterShippingOrdersByStatus(Arrays.asList(EnumShippingOrderStatus.SO_OnHold));

        for (ShippingOrder shippingOrder : shippingOrdersToPutOnHold) {
            this.getAdminShippingOrderService().unHoldShippingOrder(shippingOrder);
        }

        Set<ShippingOrder> shippingOrders = order.getShippingOrders();
        if (shippingOrders != null && !shippingOrders.isEmpty()) {
            order.setOrderStatus(this.getOrderService().getOrderStatus(EnumOrderStatus.InProcess));
        } else {
            order.setOrderStatus(this.getOrderService().getOrderStatus(EnumOrderStatus.Placed));
        }

        order = this.getOrderService().save(order);

        /**
         * Order lifecycle activity logging - Order Put OnHold
         */
        this.logOrderActivity(order, EnumOrderLifecycleActivity.OrderRemovedOnHold);

        return order;
    }

    @Override
	public void logOrderActivity(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity) {
        User user = this.userService.getLoggedInUser();
        //User user = UserCache.getInstance().getLoggedInUser();
        OrderLifecycleActivity orderLifecycleActivity = this.getOrderLoggingService().getOrderLifecycleActivity(enumOrderLifecycleActivity);
        this.logOrderActivity(order, user, orderLifecycleActivity, null);
    }

    @Override
	public void logOrderActivityByAdmin(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity, String comments) {
        //User user = UserCache.getInstance().getAdminUser();
        User user = this.userService.getAdminUser();
        OrderLifecycleActivity orderLifecycleActivity = this.getOrderLoggingService().getOrderLifecycleActivity(enumOrderLifecycleActivity);
        this.logOrderActivity(order, user, orderLifecycleActivity, comments);
    }

    @Override
    public void logOrderActivity(Order order, User user, OrderLifecycleActivity orderLifecycleActivity, String comments) {
        this.getOrderLoggingService().logOrderActivity(order, user, orderLifecycleActivity, comments);
    }

    /**
     * if all shipping orders for a order are in shipped/delievered or escalted to packing queue status update base
     * order's status to statusToUpdate
     */
    @Transactional
    private boolean updateOrderStatusFromShippingOrders(Order order, EnumShippingOrderStatus soStatus, EnumOrderStatus boStatusOnSuccess) {

        boolean shouldUpdate = true;

        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
	        if (!this.shippingOrderService.shippingOrderHasReplacementOrder(shippingOrder)) {
		        if (!soStatus.getId().equals(shippingOrder.getOrderStatus().getId())) {
			        shouldUpdate = false;
					break;
		        }
	        }
        }

        if (shouldUpdate) {
            order.setOrderStatus(this.getOrderStatusService().find(boStatusOnSuccess));
            order = this.getOrderService().save(order);
        }
        /*
         * else { order.setOrderStatus(orderStatusDao.find(boStatusOnFailure.getId())); order =
         * orderDaoProvider.get().save(order); }
         */

        return shouldUpdate;
    }

    @Override
	@Transactional
    public Order markOrderAsShipped(Order order) {
        boolean isUpdated = this.updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Shipped, EnumOrderStatus.Shipped);
        if (isUpdated) {
            this.logOrderActivity(order, EnumOrderLifecycleActivity.OrderShipped);
            // update in case of subscription orders
            this.subscriptionOrderService.markSubscriptionOrderAsShipped(order);

            // incase of other store orders
            if (!order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID)) {
                order = this.orderService.save(order);
                this.storeOrderService.updateOrderStatusInStore(order);
            }
        }
        return order;
    }

    @Override
	@Transactional
    public Order markOrderAsDelivered(Order order) {
        if (!order.getOrderStatus().getId().equals(EnumOrderStatus.Delivered.getId())) {
            boolean isUpdated = this.updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Delivered, EnumOrderStatus.Delivered);
            if (isUpdated) {
                this.logOrderActivity(order, EnumOrderLifecycleActivity.OrderDelivered);
                this.rewardPointService.approvePendingRewardPointsForOrder(order);
                this.affilateService.approvePendingAffiliateTxn(order);
                // Currently commented as we aren't doing COD for services as of yet, When we start, We may have to put
                // a
                // check if payment mode was COD and email hasn't been sent yet
                // sendEmailToServiceProvidersForOrder(order);

                // if the order is a subscription order update subscription status
                this.subscriptionOrderService.markSubscriptionOrderAsDelivered(order);

                // incase of other store orders
                if (!order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID)) {
                    order = this.orderService.save(order);
                    this.storeOrderService.updateOrderStatusInStore(order);









                }
                // to do order delivered mail
                if (!order.isDeliveryEmailSent() && order.getStore() != null) {
                	
                	if(order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID)){
                        if (this.getAdminEmailManager().sendOrderDeliveredEmail(order)) {
                            order.setDeliveryEmailSent(true);
                            this.getOrderService().save(order);
                            this.smsManager.sendOrderDeliveredSMS(order);
                            this.reviewCollectionFrameworkService.doUserEntryForReviewMail(order);
                        }
                	} else if ((order.getStore().getId().equals(StoreService.LOYALTYPG_ID))) {
                		// separate condition added in case future changes happen to the email or messages
                        if (this.getAdminEmailManager().sendOrderDeliveredEmail(order)) {
                            order.setDeliveryEmailSent(true);
                            this.getOrderService().save(order);
                            this.smsManager.sendOrderDeliveredSMS(order);
                            this.reviewCollectionFrameworkService.doUserEntryForReviewMail(order);
                        }
                	}
	            }
            }
        }
        return order;
    }

    @Override
	@Transactional
    public Order markOrderAsRTO(Order order) {
        boolean isUpdated = this.updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_RTO, EnumOrderStatus.RTO);
        if (isUpdated) {
            this.logOrderActivity(order, EnumOrderLifecycleActivity.OrderReturned);
        } else {
            this.logOrderActivity(order, EnumOrderLifecycleActivity.OrderPartiallyReturned);
        }
        return order;
    }

    @Override
	@Transactional
    public Order markOrderAsCompletedWithInstallation(Order order){
//       boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Installed, EnumOrderStatus.Installed);
        boolean isUpdated = this.updateOrderStatusFromShippingOrdersForInstallation(order, EnumShippingOrderStatus.SO_Installed, EnumOrderStatus.Installed);
        if (isUpdated) {
            this.logOrderActivity(order, EnumOrderLifecycleActivity.OrderInstalled);
            this.getAdminEmailManager().sendOrderInstalltionEmail(order);
        }
        return order;
    }


    @Override
	@Transactional
    public Order markOrderAsLost(Order order) {
        boolean isUpdated = this.updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Lost, EnumOrderStatus.Lost);
        if (isUpdated) {
            this.logOrderActivity(order, EnumOrderLifecycleActivity.OrderLost);
        } else {
            this.logOrderActivity(order, EnumOrderLifecycleActivity.OrderPartiallyLost);
        }
        return order;
    }







    @Transactional
       private boolean updateOrderStatusFromShippingOrdersForInstallation(Order order, EnumShippingOrderStatus soStatus, EnumOrderStatus boStatusOnSuccess) {

           boolean shouldUpdate = true;
           Set<ShippingOrder> baseShippingOrderList = order.getShippingOrders();
           List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();
            for (ShippingOrder shippingOrder : baseShippingOrderList) {
                if (shippingOrder.isDropShipping() && this.shipmentService.isShippingOrderHasInstallableItem(shippingOrder)) {
                    shippingOrderList.add(shippingOrder);
                }
            }










           for (ShippingOrder shippingOrder : shippingOrderList) {
               if (!this.shippingOrderService.shippingOrderHasReplacementOrder(shippingOrder)) {
                   if (!soStatus.getId().equals(shippingOrder.getOrderStatus().getId())) {
                       shouldUpdate = false;
                       break;
                   }
               }
           }

           if (shouldUpdate) {

               order.setOrderStatus(this.getOrderStatusService().find(boStatusOnSuccess));
               order = this.getOrderService().save(order);
           }
           /*
            * else { order.setOrderStatus(orderStatusDao.find(boStatusOnFailure.getId())); order =
            * orderDaoProvider.get().save(order); }
            */

           return shouldUpdate;
       }
    

    @Override
    @Transactional
    public Order moveOrderBackToActionQueue(Order order, String shippingOrderGatewayId) {
        //User loggedInUser = UserCache.getInstance().getLoggedInUser();
        User loggedInUser = this.getUserService().getLoggedInUser();
        OrderLifecycleActivity orderLifecycleActivity = this.getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.EscalatedBackToAwaitingQueue);
        this.logOrderActivity(order, loggedInUser, orderLifecycleActivity, shippingOrderGatewayId + "escalated back to  action queue");

        return order;
    }

    /**
     * TODO:#ankit please make keys in the map as some constants.
     */
    @Override
	public Map<String, String> isCODAllowed(Order order,Double payable) {
        Map<String, String> codFailureMap = new HashMap<String, String>();
        CartLineItemFilter cartLineItemFilter = new CartLineItemFilter(order.getCartLineItems());
        Set<CartLineItem> productCartLineItems = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).filter();
        Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
        boolean codAllowedonProduct = true;

        for (CartLineItem productCartLineItem : productCartLineItems) {
            Product product = productCartLineItem.getProductVariant().getProduct();
            if (product.isCodAllowed() != null && !product.isCodAllowed()) {
                codAllowedonProduct = false;
                break;
            }
        }

        OrderSearchCriteria osc = new OrderSearchCriteria();
        osc.setEmail(order.getUser().getLogin()).setOrderStatusList(Arrays.asList(EnumOrderStatus.RTO.asOrderStatus()));
        List<Order> rtoOrders = this.getOrderService().searchOrders(osc);

        if (payable < this.codMinAmount || payable > this.codMaxAmount) {
            codFailureMap.put("CodOnAmount", "N");
        } else if (subscriptionCartLineItems != null && subscriptionCartLineItems.size() > 0) {
            codFailureMap.put("CodOnSubscription", "N");
        } else if (!codAllowedonProduct) {
            codFailureMap.put("CodAllowedOnProduct", "N");
        } else if (!this.pincodeCourierService.isCourierAvailable(order.getAddress().getPincode(), null, this.pincodeCourierService.getShipmentServiceType(productCartLineItems, true), true)) {
            codFailureMap.put("OverallCodAllowedByPincodeProduct", "N");
        } else if (!rtoOrders.isEmpty() && rtoOrders.size() >= 2) {
            osc.setEmail(order.getUser().getLogin()).setOrderStatusList(Arrays.asList(EnumOrderStatus.Delivered.asOrderStatus()));
            List<Order> totalDeliveredOrders = this.getOrderService().searchOrders(osc);
            if (rtoOrders.size() >= totalDeliveredOrders.size()) {
				codFailureMap.put("MutipleRTOs", "Y");
			}
        }
        return codFailureMap;
    }

      @Override
	@Transactional
    public Payment confirmCodOrder(Order order, String source, User user) {
        Payment payment = null;
        if (user == null) {
            user = this.userService.getAdminUser();
        }
        if (EnumPaymentStatus.AUTHORIZATION_PENDING.getId().equals(order.getPayment().getPaymentStatus().getId())) {
            payment = this.paymentManager.verifyCodPayment(order.getPayment());
            order.setConfirmationDate(new Date());
            this.orderService.save(order);

            this.orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(order);
            this.getOrderLoggingService().logOrderActivity(order, user, this.getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.ConfirmedAuthorization), source);
        }
        return payment;
    }

    public UserService getUserService() {
        return this.userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public OrderStatusService getOrderStatusService() {
        return this.orderStatusService;
    }

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public RewardPointService getRewardPointService() {
        return this.rewardPointService;
    }

    public void setRewardPointService(RewardPointService rewardPointService) {
        this.rewardPointService = rewardPointService;
    }

    public AdminShippingOrderService getAdminShippingOrderService() {
        if (this.adminShippingOrderService == null) {
            this.adminShippingOrderService = ServiceLocatorFactory.getService(AdminShippingOrderService.class);
        }
        return this.adminShippingOrderService;
    }

    public void setAdminShippingOrderService(AdminShippingOrderService adminShippingOrderService) {
        this.adminShippingOrderService = adminShippingOrderService;
    }

    public AffilateService getAffilateService() {
        return this.affilateService;
    }

    public void setAffilateService(AffilateService affilateService) {
        this.affilateService = affilateService;
    }

    public ReferrerProgramManager getReferrerProgramManager() {
        return this.referrerProgramManager;
    }

    public void setReferrerProgramManager(ReferrerProgramManager referrerProgramManager) {
        this.referrerProgramManager = referrerProgramManager;
    }

    public EmailManager getEmailManager() {
        return this.emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public OrderService getOrderService() {
        return this.orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderLoggingService getOrderLoggingService() {
        return this.orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }

    public SubscriptionOrderService getSubscriptionOrderService() {
        return this.subscriptionOrderService;
    }

    public void setSubscriptionOrderService(SubscriptionOrderService subscriptionOrderService) {
        this.subscriptionOrderService = subscriptionOrderService;
    }

    public StoreService getStoreService() {
        return this.storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public StoreOrderService getStoreOrderService() {
        return this.storeOrderService;
    }

    public void setStoreOrderService(StoreOrderService storeOrderService) {
        this.storeOrderService = storeOrderService;
    }

    public AdminEmailManager getAdminEmailManager() {
        return this.adminEmailManager;
    }

}