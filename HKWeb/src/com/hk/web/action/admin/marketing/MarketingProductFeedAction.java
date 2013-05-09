package com.hk.web.action.admin.marketing;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.marketing.EnumMarketingFeed;
import com.hk.domain.catalog.product.Product;
import com.hk.pact.service.marketing.MarketingFeedService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    private String productIds = "";
    /**
     * Name of Marketing feed
     */
    private String marketingFeed;

    private List<String> feedNames = new ArrayList<String>();

    @Autowired
    MarketingFeedService marketingFeedService;

    @DefaultHandler
    public Resolution pre() {
        List<String> marketingFeedNames = new ArrayList<String>();
        feedNames = EnumMarketingFeed.getAllFeeds();

        return new ForwardResolution("/pages/admin/marketing/marketingProductFeed.jsp");
    }

    public Resolution saveProductsForFeed() {
        if (StringUtils.isNotEmpty(productIds)) {
            marketingFeedService.addProductsToFeed(marketingFeed, productIds);
        }
        return new ForwardResolution("/pages/admin/adminHome.jsp");
    }

    public Resolution removeProductsFromFeed() {
        if (StringUtils.isNotEmpty(productIds)) {
            marketingFeedService.removeProductsFromFeed(marketingFeed, productIds);
        }
        return new ForwardResolution("/pages/admin/adminHome.jsp");
    }

    public Resolution getProductsForFeed() {

        List<Product> products = marketingFeedService.getProducts("Google PLA");
        StringBuffer productStr = new StringBuffer();
        for (Product product : products){
            productStr.append(product.getId());
            productStr.append(",");
        }
        feedNames = EnumMarketingFeed.getAllFeeds();
        return new RedirectResolution("/pages/admin/marketing/marketingProductFeed.jsp").addParameter("productIds",productStr.toString());
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

    public MarketingFeedService getMarketingFeedService() {
        return marketingFeedService;
    }

    public void setMarketingFeedService(MarketingFeedService marketingFeedService) {
        this.marketingFeedService = marketingFeedService;
    }
}
