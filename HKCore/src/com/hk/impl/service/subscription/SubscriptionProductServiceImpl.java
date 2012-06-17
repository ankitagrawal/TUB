package com.hk.impl.service.subscription;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.pact.dao.subscription.SubscriptionProductDao;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.catalog.product.Product;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 17, 2012
 * Time: 2:00:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SubscriptionProductServiceImpl implements SubscriptionProductService{

  @Autowired
  SubscriptionProductDao subscriptionProductDao;

  @Transactional
  public SubscriptionProduct save(SubscriptionProduct subscriptionProduct){
       return subscriptionProductDao.save(subscriptionProduct);
  }

  @Transactional
  public SubscriptionProduct findByProduct(Product product){
       return subscriptionProductDao.findByProduct(product);
  }

}
