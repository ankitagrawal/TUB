package com.hk.web.action.admin.marketing;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.pact.service.marketing.MarketingFeedService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 18/04/13
 * Time: 6:54 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Used for maintaining specific products
 */
@Component
public class MarketingProductFeedAction extends BaseAction{

    private static Logger logger = LoggerFactory.getLogger(MarketingProductFeedAction.class);
    /**
     * Comma separated ids of products
     */
    private String productIds;
    /**
     * Name of Marketing feed
     */
    private String marketingFeed;
    private List<String> feedNames;

    @Autowired
    MarketingFeedService marketingFeedService;



    public Resolution pre() {
        return new ForwardResolution("/pages/admin/marketing/marketingProductFeed.jsp");
    }

    public Resolution saveProductsForFeed() {

    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getMarketingFeed() {
        return marketingFeed;
    }

    public void setMarketingFeed(String marketingFeed) {
        this.marketingFeed = marketingFeed;
    }

    public List<String> getFeedNames() {
        return feedNames;
    }

    public void setFeedNames(List<String> feedNames) {
        this.feedNames = feedNames;
    }
}
