package com.hk.domain.matcher;

import java.util.Set;

import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.subscription.Subscription;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/9/12
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionMatcher {
  private ProductVariant productVariant;
  private EnumSubscriptionStatus subscriptionStatus;

  public SubscriptionMatcher addProduct(ProductVariant productVariant){
      this.productVariant=productVariant;
      return this;
  }

    public SubscriptionMatcher addSubscriptionStatus(EnumSubscriptionStatus subscriptionStatus){
        this.subscriptionStatus=subscriptionStatus;
        return this;
    }

  public Subscription match(Set<Subscription> subscriptionsToMatchFrom){

    for(Subscription subscription : subscriptionsToMatchFrom){

      boolean  matchFound=false;

      if(subscription.getProductVariant()!=null){
        if(productVariant.equals(subscription.getProductVariant())){
             matchFound=true;
        }
      }
      if(subscriptionStatus!=null){
          if(subscriptionStatus.getId()==subscription.getSubscriptionStatus().getId()){
              matchFound=false;
          }
      }
      if(matchFound){
        return subscription;
      }
    }
    return  null;
  }
}
