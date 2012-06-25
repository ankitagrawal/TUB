package com.hk.pact.service.mooga;

import com.hk.domain.catalog.product.ProductVariant;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Marut
 * Date: 6/23/12
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */
public interface RecommendationEngine  {
     void pushAddToCart(long userId, String productVariantId);

    void pushAddToCart(long userId, List<ProductVariant> productVariants);

    List<String> getRecommendedProducts(String pvID);
}
