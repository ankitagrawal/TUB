package com.hk.pact.dao.subscription;

import java.util.List;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 17, 2012
 * Time: 1:43:04 PM
 */
public interface SubscriptionProductDao extends BaseDao {

    public SubscriptionProduct save(SubscriptionProduct subscriptionProduct);

    public SubscriptionProduct findByProduct(Product product);

    public List<SubscriptionProduct> findByCategory(Category category);

}
