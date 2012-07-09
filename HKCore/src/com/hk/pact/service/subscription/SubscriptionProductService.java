package com.hk.pact.service.subscription;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.catalog.product.Product;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 17, 2012
 * Time: 1:43:46 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SubscriptionProductService {

  public SubscriptionProduct save(SubscriptionProduct subscriptionProduct);

  public SubscriptionProduct findByProduct(Product product);

  public SubscriptionProduct findByProductVariant(ProductVariant productVariant);
  
}
