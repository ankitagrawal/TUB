package com.hk.pact.dao.marketing;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.marketing.MarketingFeed;
import com.hk.pact.dao.BaseDao;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 19/04/13
 * Time: 12:11 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MarketingFeedDao extends BaseDao {
    public MarketingFeed save(MarketingFeed marketingFeed);
    public void addProductsToFeed(List<MarketingFeed> marketingFeedList);
    public MarketingFeed findProductInFeed(String feedName,String productId);
    public void addProductsToFeed(String feedName, Collection<Product> productIds);
    public List<String> getProductIds(String feedName);

    void removeProductsFromFeed(String feedName, String commaSeparatedProductIds);
}
