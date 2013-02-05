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
 * User: marut
 * Date: 05/02/13
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class StyleCrazeCatalogAction extends BaseAction {
    @Autowired
    private ProductService productService;

    private List<Product> products = new ArrayList<Product>();


    public Resolution pre() {
        List<Product> beautyProducts = getProductService().getAllProductByCategory(CategoryConstants.BEAUTY);
        products.addAll(beautyProducts);
        return new ForwardResolution("/pages/styleCrazeCatalog.jsp");
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
