package com.hk.domain.matcher;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.subscription.Subscription;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/9/12
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionMatcher {
  private ProductVariant productVariant;

  public SubscriptionMatcher addProduct(ProductVariant productVariant){
      this.productVariant=productVariant;
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
      if(matchFound){
        return subscription;
      }
    }
    return  null;
  }
}
