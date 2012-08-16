package com.hk.domain.builder;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.subscription.EnumSubscriptionOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionOrder;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/13/12
 * Time: 11:44 PM
 */
public class SubscriptionOrderBuilder {

    private ProductVariant productVariant;
    private Subscription subscription;
    private EnumSubscriptionOrderStatus subscriptionOrderStatus;
    private Order baseOrder;

    public SubscriptionOrderBuilder forSubscription(Subscription subscription){
        this.subscription = subscription;
        return this;
    }

    public SubscriptionOrderBuilder setBaseOrder(Order baseOrder){
        this.baseOrder=baseOrder;
        return this;
    }

    public SubscriptionOrderBuilder withStatus(EnumSubscriptionOrderStatus subscriptionOrderStatus){
        this.subscriptionOrderStatus=subscriptionOrderStatus;
        return this;
    }

    public SubscriptionOrder build(){
        SubscriptionOrder subscriptionOrder=new SubscriptionOrder();
        if(subscriptionOrderStatus==null){
            subscriptionOrderStatus=EnumSubscriptionOrderStatus.Placed;
        }

        productVariant = subscription.getProductVariant();

        subscriptionOrder.setSubscriptionOrderStatus(subscriptionOrderStatus.asSubscriptionOrderStatus());
        subscriptionOrder.setBaseOrder(baseOrder);
        subscriptionOrder.setCreateDate(BaseUtils.getCurrentTimestamp());
        subscriptionOrder.setHkDiscountNow(productVariant.getDiscountPercent());
        subscriptionOrder.setHkPriceNow(productVariant.getHkPrice());
        subscriptionOrder.setCostPriceNow(productVariant.getCostPrice());
        subscriptionOrder.setMarkedPriceNow(productVariant.getMarkedPrice());
        subscriptionOrder.setSubscription(subscription);

        return subscriptionOrder;
    }

}
