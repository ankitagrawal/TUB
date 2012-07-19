package com.hk.impl.service.subscription;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.subscription.EnumSubscriptionLifecycleActivity;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.constants.subscription.SubscriptionConstants;
import com.hk.core.search.SubscriptionSearchCriteria;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.pact.service.subscription.SubscriptionLoggingService;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.pact.dao.subscription.SubscriptionDao;
import com.hk.domain.subscription.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 19, 2012
 * Time: 6:28:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService{

    @Autowired
    SubscriptionDao subscriptionDao;
    @Autowired
    SubscriptionLoggingService subscriptionLoggingService;

    @Transactional
    public Subscription save(Subscription subscription){
        return subscriptionDao.save(subscription);
    }

    public List<Subscription> getSubscriptions(Order order, SubscriptionStatus subscriptionStatus){
        return subscriptionDao.getSubscriptions(order,subscriptionStatus);
    }

    public List<Subscription> getSubscriptions(Order order, EnumSubscriptionStatus subscriptionStatus){
        return subscriptionDao.getSubscriptions(order,subscriptionStatus.asSubscriptionStatus());
    }

    /**
     * Used to update subscription status to placed after the order containing subscriptions is placed
     * @param order
     * @return
     */
    public List<Subscription> placeSubscriptions(Order order){
        List<Subscription> inCartSubscriptions= getSubscriptions(order, EnumSubscriptionStatus.InCart.asSubscriptionStatus());
        for(Subscription subscription : inCartSubscriptions){
            subscription.setSubscriptionStatus(EnumSubscriptionStatus.Placed.asSubscriptionStatus());
            subscription.setAddress(order.getAddress());
            subscriptionDao.save(subscription);
            subscriptionLoggingService.logSubscriptionActivity(subscription, EnumSubscriptionLifecycleActivity.SubscriptionPlaced);
        }
        return  inCartSubscriptions;
    }

    public Subscription abandonSubscription(Subscription subscription){
        subscription.setSubscriptionStatus(EnumSubscriptionStatus.Abandoned.asSubscriptionStatus());
        subscription=this.save(subscription);
        subscriptionLoggingService.logSubscriptionActivity(subscription,EnumSubscriptionLifecycleActivity.SubscriptionAbandoned);
        return subscription;
    }

    public Subscription cancelSubscription(Subscription subscription){
        subscription.setSubscriptionStatus(EnumSubscriptionStatus.Cancelled.asSubscriptionStatus());
        subscription= this.save(subscription);
        subscriptionLoggingService.logSubscriptionActivity(subscription,EnumSubscriptionLifecycleActivity.SubscriptionCancelled);
        //to do award reward point after penalty or write relavent business logic
        return subscription;
    }

    public Page searchSubscriptions(SubscriptionSearchCriteria subscriptionSearchCriteria, int pageNo, int perPage){
        return subscriptionDao.searchSubscriptions(subscriptionSearchCriteria, pageNo, perPage);
    }

    /**
     * used to escalate necessary subscriptoins to subscription action awaiting queue. Customer care
     * calls the customer for information
     */
    public int escalateSubscriptionsToActionQueue(){
        int cusomterBufferDays= SubscriptionConstants.customerBufferDays;
        Date referenceDate=new Date(BaseUtils.getCurrentTimestamp().getTime()+cusomterBufferDays*24*60*60*1000);
        List<SubscriptionStatus> fromStatuses=new ArrayList<SubscriptionStatus>();

        fromStatuses.add(EnumSubscriptionStatus.Placed.asSubscriptionStatus());
        fromStatuses.add(EnumSubscriptionStatus.Idle.asSubscriptionStatus());

        SubscriptionStatus toStatus=EnumSubscriptionStatus.CustomerConfirmationAwaited.asSubscriptionStatus();

        return subscriptionDao.escalateSubscriptionsToActionQueue(fromStatuses,toStatus, referenceDate);
    }

    /**
     * get subscriptions with a particular status
     * @param subscriptionStatus
     * @return
     */
    public List<Subscription> getSubscriptions(EnumSubscriptionStatus subscriptionStatus){
        return subscriptionDao.getSubscriptions(subscriptionStatus);
    }

    /**
     * checks inventory prior to handling subscription orders and generates emails in case of low inventory
     */
    public void checkInventoryForSubscriptionOrders(){

    }

    public Subscription updateSubscriptionAfterOrderDelivery(Subscription subscription){
        long qtyPerDelivery = subscription.getQtyPerDelivery();
        long qtyDelivered = subscription.getQtyDelivered();
        long totalQty = subscription.getQty();

        //update qty Delivered
        qtyDelivered+=qtyPerDelivery;

        subscription.setQtyDelivered(qtyDelivered);
        if(totalQty<=qtyDelivered){
            subscription.setSubscriptionStatus(EnumSubscriptionStatus.Expired.asSubscriptionStatus());
        }else{
            subscription.setSubscriptionStatus(EnumSubscriptionStatus.Idle.asSubscriptionStatus());
        }
        subscriptionLoggingService.logSubscriptionActivity(subscription, EnumSubscriptionLifecycleActivity.SubscriptionOrderDelivered);
        return this.save(subscription);
    }
}
