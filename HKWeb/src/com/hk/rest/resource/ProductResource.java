package com.hk.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.hk.domain.catalog.product.Product;
import com.hk.service.ProductService;
import com.hk.service.ServiceLocatorFactory;

@Path("/product")
public class ProductResource {
    
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
