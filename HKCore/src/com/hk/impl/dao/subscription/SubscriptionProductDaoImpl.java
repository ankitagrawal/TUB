package com.hk.impl.dao.subscription;

import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.pact.dao.subscription.SubscriptionProductDao;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.catalog.product.Product;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 17, 2012
 * Time: 2:00:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
@SuppressWarnings("unchecked")
public class SubscriptionProductDaoImpl extends BaseDaoImpl implements SubscriptionProductDao {

  public SubscriptionProduct save(SubscriptionProduct subscriptionProduct){
     return (SubscriptionProduct) super.save(subscriptionProduct);
  }

  public SubscriptionProduct findByProduct(Product product){
       return (SubscriptionProduct) findUniqueByNamedParams("from SubscriptionProduct sp where sp.product = :product ", new String[]{"product"}, new Object[]{product});
  }
}
