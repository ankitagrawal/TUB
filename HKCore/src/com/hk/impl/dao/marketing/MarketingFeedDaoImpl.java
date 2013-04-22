package com.hk.impl.dao.marketing;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.marketing.MarketingFeed;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.dao.marketing.MarketingFeedDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 19/04/13
 * Time: 12:09 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class MarketingFeedDaoImpl extends BaseDaoImpl implements MarketingFeedDao {
    public MarketingFeed save(MarketingFeed marketingFeed) {
        return (MarketingFeed)super.save(marketingFeed);
    }

    public void addProductsToFeed(List<MarketingFeed> marketingFeedList) {
        for (MarketingFeed marketingFeed : marketingFeedList){
            if (findProductInFeed(marketingFeed.getFeedName(), marketingFeed.getProductId()) == null){
                save(marketingFeed);
            }
        }
    }

    public MarketingFeed findProductInFeed(String feedName, String productId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(MarketingFeed.class);
        criteria.add(Restrictions.eq("feedName", feedName));
        criteria.add(Restrictions.eq("productId", productId));
        List<MarketingFeed> marketingFeeds = findByCriteria(criteria);
        if (marketingFeeds.size() > 0){
            return marketingFeeds.get(0);
        }
        return null;
    }

    public void addProductsToFeed(String feedName, Collection<Product> products) {
        for (Product product : products){
            if (findProductInFeed(feedName, product.getId()) == null){
                MarketingFeed marketingFeed = new MarketingFeed();
                marketingFeed.setFeedName(feedName);
                marketingFeed.setProductId(product.getId());
                save(marketingFeed);
            }
        }
    }

    public List<String> getProductIds(String feedName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(MarketingFeed.class);
        criteria.add(Restrictions.eq("feedName", feedName.trim()));
        List<MarketingFeed> marketingFeeds = findByCriteria(criteria);
        List<String> feedProducts = new ArrayList<String>();
        for (MarketingFeed marketingFeed : marketingFeeds){
            feedProducts.add(marketingFeed.getProductId());
        }
        return feedProducts;
    }

    public void removeProductsFromFeed(String feedName, String commaSeparatedProductIds) {
        String[] products = commaSeparatedProductIds.split(",");
        for (String product : products){
            MarketingFeed marketingFeed = findProductInFeed(feedName, product);
            if (marketingFeed != null){
               super.delete(marketingFeed);
            }
        }
    }
}
