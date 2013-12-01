package com.hk.web.action;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.catalog.product.Product;
import com.hk.pact.service.catalog.ProductService;
import com.hk.service.ServiceLocatorFactory;

@Component
@Path("/test")
public class TestResource {

    
    @Autowired
    private ProductService productService;
    
    @GET
    @Path("/all")
    @Produces("application/json")
    public String test() {

        ProductService testService = ServiceLocatorFactory.getService(ProductService.class);
        Product product = getProductService().getProductById("NUT304");
        return new String(product.getName() + ":" + product.getBrand());
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
