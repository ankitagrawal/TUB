package com.hk.web.action.core.catalog;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.domain.catalog.product.Product;
import com.hk.pact.service.catalog.ProductService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 1/9/13
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class GoogleCatalogAction extends BaseAction {

    @Autowired
    private ProductService productService;

    String category;

    private List<Product> products;

    public Resolution pre() {
        List<String> categories = new ArrayList<String>();
        categories = Arrays.asList(getContext().getRequest().getParameterValues("category"));
        products = getProductService().getProductByCategories(categories);
        return new ForwardResolution("/pages/googleCatalog.jsp");
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
