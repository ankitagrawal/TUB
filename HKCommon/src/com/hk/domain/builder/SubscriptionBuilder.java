package com.hk.domain.builder;

import java.util.Date;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.user.User;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/4/12
 * Time: 3:11 PM
 */
public class SubscriptionBuilder {
    private ProductVariant productVariant;
    private Order order;
    private User user;
    private EnumSubscriptionStatus subscriptionStatus;
    private SubscriptionProduct subscriptionProduct;
    private Long qty;
    private Long qtyPerDelivery;
    private Long subscriptionPeriod;
    private Long frequency;
    private Date startDate;

    public SubscriptionBuilder startDate(Date startDate){
        this.startDate=startDate;
        return this;
    }
    public SubscriptionBuilder forQuantity(Long qty,Long qtyPerDelivery){
        this.qty=qty;
        this.qtyPerDelivery=qtyPerDelivery;
        return this;
    }

    public SubscriptionBuilder subscriptionPeriod(Long subscriptionPeriod){
        this.subscriptionPeriod=subscriptionPeriod;
        return this;
    }

    public SubscriptionBuilder frequency(Long frequency){
        this.frequency=frequency;
        return this;
    }

    public SubscriptionBuilder forSubscriptionProduct(SubscriptionProduct subscriptionProduct){
        this.subscriptionProduct =subscriptionProduct;
        return  this;
    }

    public SubscriptionBuilder forUser(User user){
        this.user=user;
        return  this;
    }

    public SubscriptionBuilder forOrder(Order order){
        this.order=order;
        return  this;
    }

    public SubscriptionBuilder forProductVariant(ProductVariant productVariant){
        this.productVariant=productVariant;
        return  this;
    }

    public SubscriptionBuilder withStatus(EnumSubscriptionStatus subscriptionStatus){
        this.subscriptionStatus=subscriptionStatus;
        return  this;
    }

    public Subscription build(){
        if(subscriptionStatus==null){
            subscriptionStatus=EnumSubscriptionStatus.InCart;
        }
        Subscription subscription=new Subscription();
        subscription.setBaseOrder(order);
        subscription.setUser(user);
        subscription.setSubscriptionStatus(subscriptionStatus.asSubscriptionStatus());

        subscription.setCreateDate(BaseUtils.getCurrentTimestamp());
        subscription.setStartDate(startDate);
        subscription.setNextShipmentDate(startDate);

        subscription.setProductVariant(productVariant);

        subscription.setCostPriceAtSubscription(productVariant.getCostPrice());
        subscription.setMarkedPriceAtSubscription(productVariant.getMarkedPrice());
        subscription.setHkDiscountAtSubscription(productVariant.getDiscountPercent());
        subscription.setHkPriceAtSubscription(productVariant.getHkPrice());

        subscription.setSubscriptionPeriodDays(subscriptionPeriod);
        subscription.setFrequencyDays(frequency);
        subscription.setQty(qty);
        subscription.setQtyPerDelivery(qtyPerDelivery);

        subscription.setQtyDelivered(0L);

        if(subscriptionPeriod<360){
            subscription.setSubscriptionDiscountPercent(subscriptionProduct.getSubscriptionDiscount180Days());
        }else{
            subscription.setSubscriptionDiscountPercent(subscriptionProduct.getSubscriptionDiscount360Days());
        }
        //discount percent in hk are stored differently
        double subscriptionPrice=productVariant.getHkPrice()-Math.round(productVariant.getMarkedPrice()*(subscription.getSubscriptionDiscountPercent()/100.0));
        subscription.setSubscriptionPrice(subscriptionPrice);

        return  subscription;
    }



}
