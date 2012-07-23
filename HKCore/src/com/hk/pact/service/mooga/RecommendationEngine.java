package com.hk.pact.service.mooga;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.impl.service.mooga.RecommendationEngineImpl;

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
    void notifyAddToCart(long userId, List<ProductVariant> productVariant);
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

    /**
     * Notify Add to cart event
     * @param userId
     * @param pvId
     */
    void notifyAddToCart(long userId, String pvId);

    /**
     * notify purchase event to recommendation service
     * @param userId
     * @param pvId
     */
    void notifyPurchase(long userId, String pvId);
    /**
     * notify remove from cart event to recommendation service
     * @param userId
     * @param pvId
     */
    void notifyRemoveFromCart(long userId, String pvId);
    /**
     * notify user review event to recommendation service
     * @param userId
     * @param pvId
     */
    void notifyWriteReview(long userId, String pvId);

    List<String> getRelatedProducts(String pId, List<String> categories);
}
