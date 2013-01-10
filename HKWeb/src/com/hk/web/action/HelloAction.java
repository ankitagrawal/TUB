package com.hk.web.action;


import java.util.Date;
import java.util.Random;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.catalog.product.Product;
import com.hk.pact.service.catalog.ProductService;
import com.hk.service.ServiceLocatorFactory;
import org.springframework.stereotype.Component;

@Component
public class HelloAction implements ActionBean {
    
    @Autowired
    private ProductService productService;
    
    private ActionBeanContext ctx;
    public ActionBeanContext getContext(){return ctx;}
    public void setContext(ActionBeanContext ctx){this.ctx = ctx;}
    private Date date;
    
    public Date getDate(){
        return date;
    }


    public Resolution currentDate(){
        date = new Date();
        ProductService testService = ServiceLocatorFactory.getService(ProductService.class);
        Product product = getProductService().getProductById("NUT304");
        return new ForwardResolution(VIEW);
    }

    @DefaultHandler
    public Resolution randomDate(){
        long max = System.currentTimeMillis();
        long random = new Random().nextLong() % max;
        date = new Date(random);
        return new ForwardResolution(VIEW);
    }
    private static final String VIEW = "/jsp/hello.jsp" ;
    
    public ProductService getProductService() {
        return productService;
    }
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
    
}


