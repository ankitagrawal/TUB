package com.hk.admin.impl.service.order;

import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.cache.UserCache;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.fliter.ShippingOrderFilter;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.CancellationType;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.manager.StoreOrderService;
import com.hk.manager.SMSManager;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AffilateService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.store.StoreService;
import com.hk.pact.service.subscription.SubscriptionOrderService;
import com.hk.service.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private SMSManager                smsManager;

    @Value("#{hkEnvProps['" + Keys.Env.codMinAmount + "']}")
    private Double                    codMinAmount;

    @Value("#{hkEnvProps['codMaxAmount']}")
    private Double                    codMaxAmount;

    @Transactional
    public Order putOrderOnHold(Order order) {

        ShippingOrderFilter shippingOrderFilter = new ShippingOrderFilter(order.getShippingOrders());
        Set<ShippingOrder> shippingOrdersToPutOnHold = shippingOrderFilter.filterShippingOrdersByStatus(EnumShippingOrderStatus.getStatusForPuttingOrderOnHold());

        for (ShippingOrder shippingOrder : shippingOrdersToPutOnHold) {
            getAdminShippingOrderService().putShippingOrderOnHold(shippingOrder);
        }
        order.setOrderStatus(getOrderService().getOrderStatus(EnumOrderStatus.OnHold));
        order = getOrderService().save(order);

        /**
         * Order lifecycle activity logging - Order Put OnHold
         */
        logOrderActivity(order, EnumOrderLifecycleActivity.OrderPutOnHold);

        return order;
    }

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
            order.setOrderStatus((getOrderStatusService().find(EnumOrderStatus.Cancelled)));
            order.setCancellationType(cancellationType);
            order.setCancellationRemark(cancellationRemark);
            order = getOrderService().save(order);

            Set<ShippingOrder> shippingOrders = order.getShippingOrders();
            if (shippingOrders != null) {
                for (ShippingOrder shippingOrder : order.getShippingOrders()) {
                    getAdminShippingOrderService().cancelShippingOrder(shippingOrder);
                }
            }

            affilateService.cancelTxn(order);

            if (order.getRewardPointsUsed() != null && order.getRewardPointsUsed() > 0) {
                referrerProgramManager.refundRedeemedPoints(order);
            }
            List<RewardPoint> rewardPointList = getRewardPointService().findByReferredOrder(order);
            if (rewardPointList != null && rewardPointList.size() > 0) {
                for (RewardPoint rewardPoint : rewardPointList) {
                    rewardPointService.cancelReferredOrderRewardPoint(rewardPoint);
                }
            }
            // Send Email Comm. for HK Users Only
            if (order.getStore() != null && order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID)) {
                emailManager.sendOrderCancelEmailToUser(order);
            }
            emailManager.sendOrderCancelEmailToAdmin(order);

            this.logOrderActivity(order, loggedOnUser, getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderCancelled), null);
        } else {
            String comment = "All SOs of BO#" + order.getGatewayOrderId() + " are not in Action Awaiting Status - Aborting Cancellation.";
            logger.info(comment);
            this.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.LoggedComment, comment);
        }
    }

    @Transactional
    public Order unHoldOrder(Order order) {

        ShippingOrderFilter shippingOrderFilter = new ShippingOrderFilter(order.getShippingOrders());
        Set<ShippingOrder> shippingOrdersToPutOnHold = shippingOrderFilter.filterShippingOrdersByStatus(Arrays.asList(EnumShippingOrderStatus.SO_OnHold));

        for (ShippingOrder shippingOrder : shippingOrdersToPutOnHold) {
            getAdminShippingOrderService().unHoldShippingOrder(shippingOrder);
        }

        Set<ShippingOrder> shippingOrders = order.getShippingOrders();
        if (shippingOrders != null && !shippingOrders.isEmpty()) {
            order.setOrderStatus(getOrderService().getOrderStatus(EnumOrderStatus.InProcess));
        } else {
            order.setOrderStatus(getOrderService().getOrderStatus(EnumOrderStatus.Placed));
        }

        order = getOrderService().save(order);

        /**
         * Order lifecycle activity logging - Order Put OnHold
         */
        logOrderActivity(order, EnumOrderLifecycleActivity.OrderRemovedOnHold);

        return order;
    }

    public void logOrderActivity(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity) {
        User user = userService.getLoggedInUser();
        //User user = UserCache.getInstance().getLoggedInUser();
        OrderLifecycleActivity orderLifecycleActivity = getOrderLoggingService().getOrderLifecycleActivity(enumOrderLifecycleActivity);
        logOrderActivity(order, user, orderLifecycleActivity, null);
    }

    public void logOrderActivityByAdmin(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity, String comments) {
        //User user = UserCache.getInstance().getAdminUser();
        User user = userService.getAdminUser();
        OrderLifecycleActivity orderLifecycleActivity = getOrderLoggingService().getOrderLifecycleActivity(enumOrderLifecycleActivity);
        logOrderActivity(order, user, orderLifecycleActivity, comments);
    }

    @Override
    public void logOrderActivity(Order order, User user, OrderLifecycleActivity orderLifecycleActivity, String comments) {
        getOrderLoggingService().logOrderActivity(order, user, orderLifecycleActivity, comments);
    }

    /**
     * if all shipping orders for a order are in shipped/delievered or escalted to packing queue status update base
     * order's status to statusToUpdate
     */
    @Transactional
    private boolean updateOrderStatusFromShippingOrders(Order order, EnumShippingOrderStatus soStatus, EnumOrderStatus boStatusOnSuccess) {

        boolean shouldUpdate = true;

        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
	        if (!shippingOrderService.shippingOrderHasReplacementOrder(shippingOrder)) {
		        if (!soStatus.getId().equals(shippingOrder.getOrderStatus().getId())) {
			        shouldUpdate = false;
			        break;
		        }
	        }
        }

        if (shouldUpdate) {
            order.setOrderStatus(getOrderStatusService().find(boStatusOnSuccess));
            order = getOrderService().save(order);
        }
        /*
         * else { order.setOrderStatus(orderStatusDao.find(boStatusOnFailure.getId())); order =
         * orderDaoProvider.get().save(order); }
         */

        return shouldUpdate;
    }

    @Transactional
    public Order markOrderAsShipped(Order order) {
        boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Shipped, EnumOrderStatus.Shipped);
        if (isUpdated) {
            logOrderActivity(order, EnumOrderLifecycleActivity.OrderShipped);
            // update in case of subscription orders
            subscriptionOrderService.markSubscriptionOrderAsShipped(order);

            // incase of other store orders
            if (!order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID)) {
                order = orderService.save(order);
                storeOrderService.updateOrderStatusInStore(order);
            }
        }
        return order;
    }

    @Transactional
    public Order markOrderAsDelivered(Order order) {
        if (!order.getOrderStatus().getId().equals(EnumOrderStatus.Delivered.getId())) {
            boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Delivered, EnumOrderStatus.Delivered);
            if (isUpdated) {
                logOrderActivity(order, EnumOrderLifecycleActivity.OrderDelivered);
                rewardPointService.approvePendingRewardPointsForOrder(order);
                affilateService.approvePendingAffiliateTxn(order);
                // Currently commented as we aren't doing COD for services as of yet, When we start, We may have to put
                // a
                // check if payment mode was COD and email hasn't been sent yet
                // sendEmailToServiceProvidersForOrder(order);

                // if the order is a subscription order update subscription status
                subscriptionOrderService.markSubscriptionOrderAsDelivered(order);

                // incase of other store orders
                if (!order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID)) {
                    order = orderService.save(order);
                    storeOrderService.updateOrderStatusInStore(order);
                }
                if (!order.isDeliveryEmailSent() && order.getStore() != null && order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID)) {
                    if (getAdminEmailManager().sendOrderDeliveredEmail(order)) {
                        order.setDeliveryEmailSent(true);
                        getOrderService().save(order);
                    }
	                smsManager.sendOrderDeliveredSMS(order);
                }
            }
        }
        return order;
    }

    @Transactional
    public Order markOrderAsRTO(Order order) {
        boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Returned, EnumOrderStatus.RTO);
        if (isUpdated) {
            logOrderActivity(order, EnumOrderLifecycleActivity.OrderReturned);
        } else {
            logOrderActivity(order, EnumOrderLifecycleActivity.OrderPartiallyReturned);
        }
        return order;
    }

    @Transactional
    public Order markOrderAsCompletedWithInstallation(Order order){
       boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Installed, EnumOrderStatus.Installed);
        if (isUpdated) {
            logOrderActivity(order, EnumOrderLifecycleActivity.OrderInstalled);
            getAdminEmailManager().sendOrderInstalltionEmail(order);
        }
        return order;
    }


    @Transactional
    public Order markOrderAsLost(Order order) {
        boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Lost, EnumOrderStatus.Lost);
        if (isUpdated) {
            logOrderActivity(order, EnumOrderLifecycleActivity.OrderLost);
        } else {
            logOrderActivity(order, EnumOrderLifecycleActivity.OrderPartiallyLost);
        }
        return order;
    }

    @Override
    @Transactional
    public Order moveOrderBackToActionQueue(Order order, String shippingOrderGatewayId) {
        //User loggedInUser = UserCache.getInstance().getLoggedInUser();
        User loggedInUser = getUserService().getLoggedInUser();
        OrderLifecycleActivity orderLifecycleActivity = getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.EscalatedBackToAwaitingQueue);
        logOrderActivity(order, loggedInUser, orderLifecycleActivity, shippingOrderGatewayId + "escalated back to  action queue");

        return order;
    }

    @Override
    @Transactional
    public boolean splitBOEscalateSOCreateShipmentAndRelatedTasks(Order order) {
        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        boolean shippingOrderExists = orderService.isShippingOrderExists(order);

        Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();

        if (shippingOrderExists) {
            if (EnumOrderStatus.Placed.getId().equals(order.getOrderStatus().getId())) {
                order.setOrderStatus(EnumOrderStatus.InProcess.asOrderStatus());
                order = getOrderService().save(order);
            }
        } else {
	        if (order.isB2bOrder() != null && order.isB2bOrder().equals(Boolean.TRUE)) {
	            //User adminUser = UserCache.getInstance().getAdminUser();
	            User adminUser = getUserService().getAdminUser();
		        orderLoggingService.logOrderActivity(order, adminUser, orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit), "Aboring Split for B2B Order");
		        //DO Nothing for B2B Orders
	        } else {
		        shippingOrders = getOrderService().createShippingOrders(order);
	        }
        }

        if (shippingOrders != null && shippingOrders.size() > 0) {
            shippingOrderExists = true;
            // save order with InProcess status since shipping orders have been created
            order.setOrderStatus(getOrderStatusService().find(EnumOrderStatus.InProcess));
            order.setShippingOrders(shippingOrders);
            order = getOrderService().save(order);

            /**
             * Order lifecycle activity logging - Order split to shipping orders
             */
            String comments = "No. of Shipping Orders created  " + shippingOrders.size();
            //User adminUser = UserCache.getInstance().getAdminUser();
            User adminUser = getUserService().getAdminUser();
            orderLoggingService.logOrderActivity(order, adminUser, orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderSplit), comments);

            // auto escalate shipping orders if possible
            if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(order.getPayment().getPaymentStatus().getId())) {
                for (ShippingOrder shippingOrder : shippingOrders) {
                    shippingOrderService.autoEscalateShippingOrder(shippingOrder);
                }
            }
            
            for (ShippingOrder shippingOrder : shippingOrders) {
                shipmentService.createShipment(shippingOrder);
            }
            
        }
        // Check Inventory health of order lineitems
        for (CartLineItem cartLineItem : productCartLineItems) {
            inventoryService.checkInventoryHealth(cartLineItem.getProductVariant());
        }

        return shippingOrderExists;
    }

    /**
     * TODO:#ankit please make keys in the map as some constants.
     */
    public Map<String, String> isCODAllowed(Order order,Double payable) {
        Map<String, String> codFailureMap = new HashMap<String, String>();
        CartLineItemFilter cartLineItemFilter = new CartLineItemFilter(order.getCartLineItems());
        Set<CartLineItem> productCartLineItems = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).filter();
        Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
        Set<CartLineItem> groundShippedCartLineItemSet = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).hasOnlyGroundShippedItems(true).filter();
        boolean codAllowedonProduct = true;
        // boolean codAllowed = false;

        for (CartLineItem productCartLineItem : productCartLineItems) {
            ProductVariant productVariant = productCartLineItem.getProductVariant();
            if (productVariant != null && productVariant.getProduct() != null) {
                Product product = productVariant.getProduct();
                if (product.isCodAllowed() != null && !product.isCodAllowed()) {
                    codFailureMap.put("ProductName", product.getName());
                    codAllowedonProduct = false;

                }
                if (product.isGroundShipping()) {
                    codFailureMap.put("GroundShipProduct", product.getName());
                }
            }
        }

        Address address = order.getAddress();
        String pin = address != null ? address.getPin() : null;

        // Double payable = pricingDto.getGrandTotalPayable();
        //Double payable = order.getAmount();
        if (!courierService.isCodAllowed(pin)) {
            codFailureMap.put("CodAllowedOnPin", "N");
            codFailureMap.put("Pincode", pin);
        } else if (payable < codMinAmount || payable > codMaxAmount) {
            codFailureMap.put("CodOnAmount", "N");
        } else if (!codAllowedonProduct) {
            codFailureMap.put("CodAllowedOnProduct", "N");
        } else if (subscriptionCartLineItems != null && subscriptionCartLineItems.size() > 0) {
            codFailureMap.put("CodOnSubscription", "N");
        } else if (groundShippedCartLineItemSet != null && groundShippedCartLineItemSet.size() > 0) {
            if (courierService.isGroundShippingAllowed(pin)) {
                codFailureMap.put("GroundShippingAllowed", "Y");
            }
            if (!courierService.isCodAllowedOnGroundShipping(pin)) {
                codFailureMap.put("CodAllowedOnGroundShipping", "N");
            }

        }
        return codFailureMap;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public OrderStatusService getOrderStatusService() {
        return orderStatusService;
    }

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public RewardPointService getRewardPointService() {
        return rewardPointService;
    }

    public void setRewardPointService(RewardPointService rewardPointService) {
        this.rewardPointService = rewardPointService;
    }

    public AdminShippingOrderService getAdminShippingOrderService() {
        if (adminShippingOrderService == null) {
            adminShippingOrderService = ServiceLocatorFactory.getService(AdminShippingOrderService.class);
        }
        return adminShippingOrderService;
    }

    public void setAdminShippingOrderService(AdminShippingOrderService adminShippingOrderService) {
        this.adminShippingOrderService = adminShippingOrderService;
    }

    public AffilateService getAffilateService() {
        return affilateService;
    }

    public void setAffilateService(AffilateService affilateService) {
        this.affilateService = affilateService;
    }

    public ReferrerProgramManager getReferrerProgramManager() {
        return referrerProgramManager;
    }

    public void setReferrerProgramManager(ReferrerProgramManager referrerProgramManager) {
        this.referrerProgramManager = referrerProgramManager;
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderLoggingService getOrderLoggingService() {
        return orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }

    public SubscriptionOrderService getSubscriptionOrderService() {
        return subscriptionOrderService;
    }

    public void setSubscriptionOrderService(SubscriptionOrderService subscriptionOrderService) {
        this.subscriptionOrderService = subscriptionOrderService;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public StoreOrderService getStoreOrderService() {
        return storeOrderService;
    }

    public void setStoreOrderService(StoreOrderService storeOrderService) {
        this.storeOrderService = storeOrderService;
    }

    public AdminEmailManager getAdminEmailManager() {
        return adminEmailManager;
    }

}
