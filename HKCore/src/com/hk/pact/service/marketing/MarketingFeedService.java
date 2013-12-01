package com.hk.pact.service.marketing;

import com.hk.domain.catalog.product.Product;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 19/04/13
 * Time: 12:34 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MarketingFeedService {
    public void addProductsToFeed(String feedName, Collection<Product> productIds);
    public void addProductsToFeed(String feedName, String commaSeparatedProductIds);
    public void removeProductsFromFeed(String feedName, String commaSeparatedProductIds);
    public List<Product> getProducts(String feedName);
}
