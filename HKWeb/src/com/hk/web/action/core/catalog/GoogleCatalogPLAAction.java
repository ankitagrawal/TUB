package com.hk.web.action.core.catalog;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.marketing.EnumMarketingFeed;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.marketing.MarketingFeedService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 12/04/13
 * Time: 1:31 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * List of extra products for google
 */
@Component
public class GoogleCatalogPLAAction extends BaseAction {
    @Autowired
    private ProductService productService;
    @Autowired
    private MarketingFeedService marketingFeedService;

    String category;

    private List<Product> products = new ArrayList<Product>();

    List<ProductVariant> productVariantList = new ArrayList<ProductVariant>();
    private Double priceConstant = 150.0;
    Boolean priceBool = true;

    public Resolution pre() {

        List<String> categories = new ArrayList<String>();
        categories = Arrays.asList(getContext().getRequest().getParameterValues("category"));
        //todo: For now these are hard-coded
//        categories.add("workout-essentials");
//        categories.add("accessories");
//        categories.add("healthy-food");
//        categories.add("diabetic-food");
//        categories.add("weight-management");
//        categories.add("foot-care");
//        categories.add("pain-management");
//        categories.add("medical-care");
//        categories.add("devices");
//        categories.add("eye");
//        categories.add("foot-care-pedicure");
//        categories.add("oral-hygiene");
//        categories.add("misc");                                 u
//        categories.add("diapering");
//        categories.add("bath-skin-care");
//        categories.add("health-safety");
//        categories.add("toys-more");


        //categories = Arrays.asList(getContext().getRequest().getParameterValues("category"));
        List<Product> catProducts = getProductService().getProductByCategory(categories);
        List<Product> individualProducts = marketingFeedService.getProducts(EnumMarketingFeed.Google_PLA.getName());
        catProducts.addAll(individualProducts);


        Map<String, Product> productMap = new HashMap<String, Product>();
        //Need to ensure that there are not duplicate items
        for (Product product : catProducts) {
            productVariantList = product.getProductVariants();
            for (ProductVariant productVariant : productVariantList) {
                if (productVariant.getHkPrice() < priceConstant) {
                    priceBool = false;
                }
            }
            if (!productMap.containsKey(product.getId()) && priceBool) {
                products.add(product);
                productMap.put(product.getId(), product);
            }
        }

        return new ForwardResolution("/pages/googlePLACatalog.jsp");
    }

    public List<Product> getProducts() {
        return products;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
