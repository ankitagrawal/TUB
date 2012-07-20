package com.hk.impl.dao.subscription;

import com.hk.domain.catalog.category.Category;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.pact.dao.subscription.SubscriptionProductDao;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.catalog.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public SubscriptionProduct findByProductId(String productId){
        return (SubscriptionProduct) findUniqueByNamedParams("from SubscriptionProduct sp where sp.product.id = :productId ", new String[]{"productId"}, new Object[]{productId});
    }

    public List<SubscriptionProduct> findByCategory(Category category){
        List<SubscriptionProduct> subscriptionProductList=new ArrayList<SubscriptionProduct>();
        List<Product> productList=new ArrayList<Product>();
        productList = productDao.getAllProductByCategory(category.getName());
        for(Product product: productList){
            if(product.isSubscribable()){
                subscriptionProductList.add(this.findByProduct(product));
            }
        }
        return subscriptionProductList;
    }

}
