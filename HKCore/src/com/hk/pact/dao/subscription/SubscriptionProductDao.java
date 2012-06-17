package com.hk.pact.dao.subscription;

import com.hk.pact.dao.BaseDao;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.catalog.product.Product;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 17, 2012
 * Time: 1:43:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SubscriptionProductDao extends BaseDao {

  public SubscriptionProduct save(SubscriptionProduct subscriptionProduct);

  public SubscriptionProduct findByProduct(Product product);

}
