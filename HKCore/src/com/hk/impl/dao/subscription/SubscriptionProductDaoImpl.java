package com.hk.impl.dao.subscription;

import com.hk.domain.catalog.category.Category;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.pact.dao.subscription.SubscriptionProductDao;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.catalog.product.Product;
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

    public SubscriptionProduct save(SubscriptionProduct subscriptionProduct){
        return (SubscriptionProduct) super.save(subscriptionProduct);
    }


    public SubscriptionProduct findByProduct(Product product){
        return (SubscriptionProduct) findUniqueByNamedParams("from SubscriptionProduct sp where sp.product = :product ", new String[]{"product"}, new Object[]{product});
    }

    public List<SubscriptionProduct> findByCategory(Category category){
        List<SubscriptionProduct> subscriptionProductList=new ArrayList<SubscriptionProduct>();
        return subscriptionProductList;
    }
}
