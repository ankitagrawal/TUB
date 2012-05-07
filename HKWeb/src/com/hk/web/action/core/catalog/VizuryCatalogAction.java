package com.hk.web.action.core.catalog;

import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.Product;
import com.hk.pact.service.catalog.ProductService;

@Component
public class VizuryCatalogAction extends BaseAction {
    @Autowired
    private ProductService productService;

    private List<Product>  products;

    public Resolution pre() {
        products = getProductService().getAllProducts();
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
