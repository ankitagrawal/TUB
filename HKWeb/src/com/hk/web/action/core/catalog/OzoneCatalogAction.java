package com.hk.web.action.core.catalog;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
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

    @Autowired
    private CategoryService categoryService;

    private List<Product> products;



    @PostConstruct
    void init(){

    }

    public Resolution pre() {
        Category category = categoryService.getCategoryByName(CategoryConstants.DIABETES);
        List<Product> diabetesProducts = new ArrayList<Product>();
        products = new ArrayList<Product>();
        if (category != null){
            diabetesProducts = category.getProducts();
        }
        List<Product> healthDevicesProduct = new ArrayList<Product>();
        category = categoryService.getCategoryByName(CategoryConstants.HEALTH_DEVICES);
        if (category != null){
            healthDevicesProduct = category.getProducts();
        }

        //Only products with MRP > 5 will get added.
        for (Product product : diabetesProducts){
            if (product.getMinimumHKPriceProductVariant().getHkPrice() > 5.0){
                products.add(product);
            }
        }
        for (Product product : healthDevicesProduct){
            if (product.getMinimumHKPriceProductVariant().getHkPrice() > 5.0){
                products.add(product);
            }
        }

        return new ForwardResolution("/pages/ozoneCatalog.jsp");
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
