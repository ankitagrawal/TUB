package com.hk.pact.service.subscription;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.catalog.product.Product;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 17, 2012
 * Time: 1:43:46 PM
 */
public interface SubscriptionProductService {

    public SubscriptionProduct save(SubscriptionProduct subscriptionProduct);

    public SubscriptionProduct findByProduct(Product product);

    public SubscriptionProduct findByProductId(String productId);

    public SubscriptionProduct findByProductVariant(ProductVariant productVariant);

    public List<SubscriptionProduct> findByCategory(Category category);

}
