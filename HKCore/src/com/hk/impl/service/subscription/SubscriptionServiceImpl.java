package com.hk.impl.service.subscription;

import java.util.*;

import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.core.fliter.CartLineItemFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.subscription.EnumSubscriptionLifecycleActivity;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.constants.subscription.SubscriptionConstants;
import com.hk.core.search.SubscriptionSearchCriteria;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.subscription.SubscriptionDao;
import com.hk.pact.service.subscription.SubscriptionLoggingService;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.pact.service.subscription.SubscriptionStatusService;

/**
 * Created by IntelliJ IDEA. User: Pradeep Date: Jun 19, 2012 Time: 6:28:41 PM To change this template use File |
 * Settings | File Templates.
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionDao            subscriptionDao;
    @Autowired
    private SubscriptionLoggingService subscriptionLoggingService;
    @Autowired
    private SubscriptionStatusService  subscriptionStatusService;
    @Autowired
    private EmailManager               emailManager;
    @Autowired
    private BaseDao                    baseDao;

    @Transactional
    public Subscription save(Subscription subscription) {
        return subscriptionDao.save(subscription);
    }

    public Subscription getSubscriptionFromCartLineItem(CartLineItem cartLineItem) {
        return subscriptionDao.getSubscriptionFromCartLineItem(cartLineItem);
    }

    public List<Subscription> getSubscriptions(Order order, SubscriptionStatus subscriptionStatus) {
        return subscriptionDao.getSubscriptions(order, subscriptionStatus);
    }

    public List<Subscription> getSubscriptions(Order order, EnumSubscriptionStatus subscriptionStatus) {
        return subscriptionDao.getSubscriptions(order, subscriptionStatus.asSubscriptionStatus());
    }

    /**
     * Used to update subscription status to placed after the order containing subscriptions is placed
     *
     * @param order
     * @return
     */
    public List<Subscription> placeSubscriptions(Order order) {
        List<Subscription> inCartSubscriptions = getSubscriptions(order, EnumSubscriptionStatus.InCart.asSubscriptionStatus());
        if(order.getPayment().getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.getId())){
            for (Subscription subscription : inCartSubscriptions) {
                subscription.setSubscriptionStatus(EnumSubscriptionStatus.Placed.asSubscriptionStatus());
                subscription.setAddress(order.getAddress());
                if (subscription.getStartDate().getTime() < BaseUtils.getCurrentTimestamp().getTime()) {
                    subscription.setStartDate(BaseUtils.getCurrentTimestamp());
                    subscription.setNextShipmentDate(BaseUtils.getCurrentTimestamp());
                }
                subscription = subscriptionDao.save(subscription);
                subscriptionLoggingService.logSubscriptionActivityByAdmin(subscription, EnumSubscriptionLifecycleActivity.SubscriptionPlaced);
                emailManager.sendSubscriptionPlacedEmailToUser(subscription);
                emailManager.sendSubscriptionPlacedEmailToAdmin(subscription);
            }
            //the following if is added to handle orders which have just subscriptions in them.
            Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            if(productCartLineItems.size()==0){
                Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
                if(subscriptionCartLineItems.size()>0){
                    if (EnumOrderStatus.Placed.getId().equals(order.getOrderStatus().getId())) {
                        order.setOrderStatus(EnumOrderStatus.SubscriptionInProgress.asOrderStatus());
                        order = (Order)baseDao.save(order);
                    }
                }
            }
        }
        return inCartSubscriptions;
    }

    public Subscription abandonSubscription(Subscription subscription) {
        subscription.setSubscriptionStatus(EnumSubscriptionStatus.Abandoned.asSubscriptionStatus());
        subscription = this.save(subscription);
        subscriptionLoggingService.logSubscriptionActivity(subscription, EnumSubscriptionLifecycleActivity.SubscriptionAbandoned);
        return subscription;
    }

    public Page searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria, int pageNo, int perPage) {
        return subscriptionDao.searchSubscriptions(subscriptionSearchCriteria, pageNo, perPage);
    }

    public List<Subscription> searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria) {
        return subscriptionDao.searchSubscriptions(subscriptionSearchCriteria);
    }

    public Page getSubscriptionsForUsers(User user, int page, int perPage) {
        return subscriptionDao.getSubscriptionsForUsers(subscriptionStatusService.getSubscriptionStatusesForUsers(), user, page, perPage);
    }

    /**
     * used to escalate necessary subscriptoins to subscription action awaiting queue. Customer care calls the customer
     * for information
     */
    public int escalateSubscriptionsToActionQueue() {
        SubscriptionSearchCriteria subscriptionSearchCriteria = new SubscriptionSearchCriteria();

        int cusomterBufferDays = SubscriptionConstants.subscriptionCustomerBufferDays;
        // going back a week just in case this process din't run on schedule
        Date currentDate = new Date(BaseUtils.getCurrentTimestamp().getTime() - 120 * 24 * 60 * 60 * 1000);
        Date referenceDate = new Date(BaseUtils.getCurrentTimestamp().getTime() + cusomterBufferDays * 24 * 60 * 60 * 1000);
        subscriptionSearchCriteria.setStartNextShipmentDate(currentDate);
        subscriptionSearchCriteria.setEndNextShipmentDate(referenceDate);

        List<SubscriptionStatus> fromStatuses = new ArrayList<SubscriptionStatus>();

        fromStatuses.add(EnumSubscriptionStatus.Placed.asSubscriptionStatus());
        fromStatuses.add(EnumSubscriptionStatus.Idle.asSubscriptionStatus());
        fromStatuses.add(EnumSubscriptionStatus.OutOfStock.asSubscriptionStatus());

        SubscriptionStatus toStatus = EnumSubscriptionStatus.CustomerConfirmationAwaited.asSubscriptionStatus();

        subscriptionSearchCriteria.setSubscriptionStatusList(fromStatuses);

        List<Subscription> subscriptions = this.searchSubscriptions(subscriptionSearchCriteria);
        for (Subscription subscription : subscriptions) {
            subscription.setSubscriptionStatus(toStatus);
            this.save(subscription);
            subscriptionLoggingService.logSubscriptionActivityByAdmin(subscription, EnumSubscriptionLifecycleActivity.EscalatedToActionQueue, "automated escalation on due date");
        }

        return subscriptions.size();
    }

    /**
     * get subscriptions with a particular status
     *
     * @param subscriptionStatus
     * @return
     */
    public List<Subscription> getSubscriptions(EnumSubscriptionStatus subscriptionStatus) {
        return subscriptionDao.getSubscriptions(subscriptionStatus);
    }

    /**
     * checks inventory prior to handling subscription orders and generates emails in case of low inventory
     */
    public void checkInventoryForSubscriptionOrders() {
        SubscriptionSearchCriteria subscriptionSearchCriteria = new SubscriptionSearchCriteria();

        int inventoryBufferDays = SubscriptionConstants.subscriptionInventoryBufferDays;
        // going back a week just in case this process din't run on schedule
        Date currentDate = new Date(BaseUtils.getCurrentTimestamp().getTime() - 7 * 24 * 60 * 60 * 1000);
        Date referenceDate = new Date(BaseUtils.getCurrentTimestamp().getTime() + inventoryBufferDays * 24 * 60 * 60 * 1000);
        subscriptionSearchCriteria.setStartNextShipmentDate(currentDate);
        subscriptionSearchCriteria.setEndNextShipmentDate(referenceDate);

        List<SubscriptionStatus> fromStatuses = new ArrayList<SubscriptionStatus>();

        fromStatuses.add(EnumSubscriptionStatus.Placed.asSubscriptionStatus());
        fromStatuses.add(EnumSubscriptionStatus.Idle.asSubscriptionStatus());

        subscriptionSearchCriteria.setSubscriptionStatusList(fromStatuses);

        List<Subscription> subscriptions = this.searchSubscriptions(subscriptionSearchCriteria);
        for (Subscription subscription : subscriptions) {
            if (subscription.getProductVariant().isOutOfStock()) {
                subscription.setSubscriptionStatus(EnumSubscriptionStatus.OutOfStock.asSubscriptionStatus());
                subscription = this.save(subscription);
                if (emailManager.sendSubscriptionVariantOutOfStockEmailAdmin(subscription)) {
                    subscriptionLoggingService.logSubscriptionActivityByAdmin(subscription, EnumSubscriptionLifecycleActivity.OutOfStockEmailSent,
                            "out of stock email sent to admin");
                } else {
                    subscriptionLoggingService.logSubscriptionActivityByAdmin(subscription, EnumSubscriptionLifecycleActivity.OutOfStockEmailSent,
                            "out of stock email to admin failed");
                }
            }
        }
    }

    public Subscription updateSubscriptionAfterOrderDelivery(Subscription subscription) {
        // long qtyPerDelivery = subscription.getQtyPerDelivery();
        long qtyDelivered = subscription.getQtyDelivered();
        long totalQty = subscription.getQty();

        // update qty Delivered - done in subscription order service due to errors ordes marked shipped and delivered
        // status

        subscriptionLoggingService.logSubscriptionActivity(subscription, EnumSubscriptionLifecycleActivity.SubscriptionOrderDelivered);
        if (totalQty <= qtyDelivered) {
            subscription.setSubscriptionStatus(EnumSubscriptionStatus.Expired.asSubscriptionStatus());
            subscriptionLoggingService.logSubscriptionActivityByAdmin(subscription, EnumSubscriptionLifecycleActivity.SubscriptionExpired, "Subscription marked as expired");
            Order order = subscription.getBaseOrder();
            boolean parentBOHasProducts = false;
            if (order.getOrderStatus().getId().equals(EnumOrderStatus.InProcess.getId()) || order.getOrderStatus().getId().equals(EnumOrderStatus.Placed.getId())
                    || order.getOrderStatus().getId().equals(EnumOrderStatus.SubscriptionInProgress.getId())) {
                for (CartLineItem cartLineItem : order.getCartLineItems()) {
                    if (cartLineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId())) {
                        parentBOHasProducts = true;
                        break;
                    }
                }
                if (!parentBOHasProducts) {
                    order.setOrderStatus(EnumOrderStatus.Delivered.asOrderStatus());
                    baseDao.save(order);
                }
            }
        } else {
            subscription.setSubscriptionStatus(EnumSubscriptionStatus.Idle.asSubscriptionStatus());
            Calendar c = Calendar.getInstance();
            c.setTime(subscription.getNextShipmentDate());
            c.add(Calendar.DATE, Integer.parseInt(subscription.getFrequencyDays().toString()));

            subscription.setNextShipmentDate(c.getTime());
            subscriptionLoggingService.logSubscriptionActivityByAdmin(subscription, EnumSubscriptionLifecycleActivity.NextShipmentDateChanged, "Automatic shipment date change");
        }

        return this.save(subscription);
    }

    public SubscriptionDao getSubscriptionDao() {
        return subscriptionDao;
    }

    public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    public SubscriptionLoggingService getSubscriptionLoggingService() {
        return subscriptionLoggingService;
    }

    public void setSubscriptionLoggingService(SubscriptionLoggingService subscriptionLoggingService) {
        this.subscriptionLoggingService = subscriptionLoggingService;
    }

    public SubscriptionStatusService getSubscriptionStatusService() {
        return subscriptionStatusService;
    }

    public void setSubscriptionStatusService(SubscriptionStatusService subscriptionStatusService) {
        this.subscriptionStatusService = subscriptionStatusService;
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }
}
