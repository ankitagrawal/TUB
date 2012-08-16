package com.hk.impl.dao.subscription;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.dao.subscription.SubscriptionProductDao;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 17, 2012
 * Time: 2:00:33 PM
 */
@Repository
@SuppressWarnings("unchecked")
public class SubscriptionProductDaoImpl extends BaseDaoImpl implements SubscriptionProductDao {
    @Autowired
    ProductDao productDao;

    public SubscriptionProduct save(SubscriptionProduct subscriptionProduct){
        return (SubscriptionProduct) super.save(subscriptionProduct);
    }


    public SubscriptionProduct findByProduct(Product product){
        return (SubscriptionProduct) findUniqueByNamedParams("from SubscriptionProduct sp where sp.product = :product ", new String[]{"product"}, new Object[]{product});
    }

    public List<SubscriptionProduct> findByCategory(Category category){
        List<SubscriptionProduct> subscriptionProductList=new ArrayList<SubscriptionProduct>();
        List<Product> productList=new ArrayList<Product>();
        productList = productDao.getAllSubscribableProductsByCategory(category.getName());
        for(Product product: productList){
                subscriptionProductList.add(this.findByProduct(product));
        }
        return subscriptionProductList;
    }

}
