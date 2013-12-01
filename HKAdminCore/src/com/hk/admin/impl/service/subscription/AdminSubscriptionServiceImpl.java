package com.hk.admin.impl.service.subscription;

import java.util.List;

import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.CartLineItem;
import com.hk.pact.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.service.subscription.AdminSubscriptionService;
import com.hk.constants.subscription.EnumSubscriptionLifecycleActivity;
import com.hk.constants.subscription.EnumSubscriptionOrderStatus;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionOrder;
import com.hk.manager.EmailManager;
import com.hk.pact.service.subscription.SubscriptionLoggingService;
import com.hk.pact.service.subscription.SubscriptionOrderService;
import com.hk.pact.service.subscription.SubscriptionService;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 8/2/12
 * Time: 1:44 PM
 */
@Service
public class AdminSubscriptionServiceImpl implements AdminSubscriptionService{

    @Autowired
    SubscriptionOrderService subscriptionOrderService;
    @Autowired
    EmailManager emailManager;
    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    SubscriptionLoggingService subscriptionLoggingService;
    @Autowired
    OrderService orderService;

    public Double getRewardPointsForSubscriptionCancellation(Subscription subscription){
        Double rewardPoints=0.0;
        rewardPoints = subscription.getSubscriptionPrice()*(subscription.getQty()-subscription.getQtyDelivered());
        List<SubscriptionOrder> subscriptionOrderList=subscriptionOrderService.findSubscriptionOrdersForSubscription(subscription);
        double subscriptionPrice=subscription.getSubscriptionPrice();
        for(SubscriptionOrder subscriptionOrder : subscriptionOrderList){
            if(!subscriptionOrder.getSubscriptionOrderStatus().getId().equals(EnumSubscriptionOrderStatus.Cancelled.getId())){
                double hkPrice=subscriptionOrder.getHkPriceNow();
                double benifit = 0.0;
                if(hkPrice>subscriptionPrice){
                    benifit=(hkPrice-subscriptionPrice)*subscription.getQtyPerDelivery();
                    rewardPoints=rewardPoints-benifit;
                }
            }
        }
        return rewardPoints;
    }

    public boolean sendSubscriptionCancellationEmails(Subscription subscription){
        boolean sent=false;

        sent= emailManager.sendSubscriptionCancellationEmail(subscription) &&  emailManager.sendSubscriptionCancellationEmailToAdmin(subscription);

        return sent;
    }

    public Subscription cancelSubscription(Subscription subscription,String cancellationRemark){
        if(subscription.getSubscriptionStatus().getId().longValue()!= EnumSubscriptionStatus.Cancelled.getId().longValue()){
            subscription.setSubscriptionStatus(EnumSubscriptionStatus.Cancelled.asSubscriptionStatus());
            subscription= subscriptionService.save(subscription);
            subscriptionLoggingService.logSubscriptionActivity(subscription, EnumSubscriptionLifecycleActivity.SubscriptionCancelled,cancellationRemark);
            Order bo=subscription.getBaseOrder();
            boolean parentBOHasProducts=false;
             if(bo.getOrderStatus().getId().equals(EnumOrderStatus.InProcess.getId())||bo.getOrderStatus().getId().equals(EnumOrderStatus.Placed.getId())){
                 for(CartLineItem cartLineItem : bo.getCartLineItems()){
                   if(cartLineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId())){
                        parentBOHasProducts=true;
                        break;
                   }
                 }
               if(!parentBOHasProducts){
                 bo.setOrderStatus(EnumOrderStatus.Cancelled.asOrderStatus());
                 getOrderService().save(bo);
               }
          }
            sendSubscriptionCancellationEmails(subscription);
        }
        return subscription;
    }

  public OrderService getOrderService() {
    return orderService;
  }
}
