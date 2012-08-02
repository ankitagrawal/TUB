package com.hk.admin.impl.service.subscription;

import com.hk.admin.pact.service.subscription.AdminSubscriptionService;
import com.hk.constants.subscription.EnumSubscriptionOrderStatus;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionOrder;
import com.hk.manager.EmailManager;
import com.hk.pact.service.subscription.SubscriptionOrderService;
import com.hk.pact.service.subscription.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Double getRewardPointsForSubscriptionCancellation(Subscription subscription){
        Double rewardPoints=0.0;
        rewardPoints = subscription.getSubscriptionPrice()*(subscription.getQty()-subscription.getQtyDelivered());
        List<SubscriptionOrder> subscriptionOrderList=subscriptionOrderService.findSubscriptionOrdersForSubscription(subscription);
        double subscriptionPrice=subscription.getSubscriptionPrice();
        for(SubscriptionOrder subscriptionOrder : subscriptionOrderList){
            if(subscriptionOrder.getSubscriptionOrderStatus().getId()!= EnumSubscriptionOrderStatus.Cancelled.getId()){
                double hkPrice=subscriptionOrder.getHkPriceNow();
                double benifit=0.0;
                if(hkPrice>subscriptionPrice){
                    benifit=hkPrice-subscriptionPrice;
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
        sendSubscriptionCancellationEmails(subscription);
        return  subscriptionService.cancelSubscription(subscription,cancellationRemark);
    }
}
