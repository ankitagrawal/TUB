package com.hk.impl.service.subscription;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.pact.dao.subscription.SubscriptionProductDao;
import com.hk.pact.service.subscription.SubscriptionProductService;

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
    private SubscriptionProductDao subscriptionProductDao;

    @Transactional
    public SubscriptionProduct save(SubscriptionProduct subscriptionProduct){
        return subscriptionProductDao.save(subscriptionProduct);
    }

    @Transactional
    public SubscriptionProduct findByProduct(Product product){
        return subscriptionProductDao.findByProduct(product);
    }

    public SubscriptionProduct findByProductVariant(ProductVariant productVariant){
        return  this.findByProduct(productVariant.getProduct());
    }

    public List<SubscriptionProduct> findByCategory(Category category){
        return subscriptionProductDao.findByCategory(category);
    }

    public SubscriptionProductDao getSubscriptionProductDao() {
        return subscriptionProductDao;
    }

    public void setSubscriptionProductDao(SubscriptionProductDao subscriptionProductDao) {
        this.subscriptionProductDao = subscriptionProductDao;
    }
}
