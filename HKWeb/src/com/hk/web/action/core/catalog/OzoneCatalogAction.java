package com.hk.web.action.core.catalog;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.pact.service.catalog.ProductService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 26/03/13
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class OzoneCatalogAction extends BaseAction {
    @Autowired
    private ProductService productService;

    private List<Product> products;

    private List<String> excludedCategories = Arrays.asList(CategoryConstants.DIABETES, CategoryConstants.HEALTH_DEVICES);

    @PostConstruct
    void init(){

    }

    public Resolution pre() {
        List<Product> diabetesProduct = getProductService().getAllProductByCategory(CategoryConstants.DIABETES);
        List<Product> healthDevicesProduct = getProductService().getAllProductByCategory(CategoryConstants.HEALTH_DEVICES);
        diabetesProduct.addAll(healthDevicesProduct);
        products = diabetesProduct;
        return new ForwardResolution("/pages/vizuryCatalog.jsp");
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
}
