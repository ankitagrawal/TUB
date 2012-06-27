package com.hk.pact.service.mooga;

import com.hk.domain.catalog.product.ProductVariant;

import java.util.List;
import java.util.Set;

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
    /**
     * returns the recommended products for given product Id
     * @param pId
     * @return
     */
    List<String> getRecommendedProducts(String pId);

    /**
     * returns the recommended product variants for given product variant id
     * @param pvId
     * @return
     */
    List<String> getRecommendedProductVariants(String pvId);
}
