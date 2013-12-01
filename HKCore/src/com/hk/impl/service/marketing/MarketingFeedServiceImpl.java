package com.hk.impl.service.marketing;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.marketing.MarketingFeed;
import com.hk.pact.dao.marketing.MarketingFeedDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.marketing.MarketingFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 19/04/13
 * Time: 12:38 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MarketingFeedServiceImpl implements MarketingFeedService {
    @Autowired
    MarketingFeedDao marketingFeedDao;

    @Autowired
    ProductService productService;

    public void addProductsToFeed(String feedName, Collection<Product> products) {
        marketingFeedDao.addProductsToFeed(feedName, products);
    }

    public void addProductsToFeed(String feedName, String commaSeparatedProductIds) {
        String[] productIdList = commaSeparatedProductIds.split(",");
        List<Product> products = new ArrayList<Product>();
        for (String productId : productIdList){
            Product product = productService.getProductById(productId);
            if (product != null){
                products.add(product);
            }
        }
        marketingFeedDao.addProductsToFeed(feedName, products);
    }

    public void removeProductsFromFeed(String feedName, String commaSeparatedProductIds) {
        marketingFeedDao.removeProductsFromFeed(feedName, commaSeparatedProductIds);
    }

    public List<Product> getProducts(String feedName) {
        List<String> productIds = marketingFeedDao.getProductIds(feedName);
        List<Product> products = new ArrayList<Product>();
        for (String productId : productIds){
            Product product = productService.getProductById(productId);
            if (product != null && !product.isDeleted()){
               products.add(product);
            }
        }
        return products;
    }
}
