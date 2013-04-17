package com.hk.web.action.core.catalog;

import com.akube.framework.stripes.action.BaseAction;
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

    String category;

    private List<Product> products;

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
//        categories.add("misc");
//        categories.add("diapering");
//        categories.add("bath-skin-care");
//        categories.add("health-safety");
//        categories.add("toys-more");


        //categories = Arrays.asList(getContext().getRequest().getParameterValues("category"));
        products = getProductService().getProductByCategory(categories);
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
