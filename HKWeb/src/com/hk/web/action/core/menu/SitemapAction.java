package com.hk.web.action.core.menu;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.Product;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.manager.LinkManager;
import com.hk.pact.service.catalog.ProductService;

@UrlBinding("/sitemap.xml")
@Component
public class SitemapAction extends BaseAction {

    List<String>           categoryUrls = new ArrayList<String>();
    List<String>           productUrls  = new ArrayList<String>();
    @Autowired
    MenuHelper             menuHelper;
    
    @Autowired
    LinkManager            linkManager;
    @Autowired
    private ProductService productService;

    public Resolution pre() {
        List<MenuNode> nodes = menuHelper.getMenuNodesFlat();
        for (MenuNode node : nodes) {
            categoryUrls.add("http://www.healthkart.com" + node.getUrl());
        }
        List<Product> products = getProductService().getAllProducts();
        for (Product product : products) {
            if ((product.isDeleted() != null) && !product.isDeleted()){
                productUrls.add("http://www.healthkart.com/product/" + product.getSlug() + "/" + product.getId());
            }
        }
        return new ForwardResolution("/sitemap.jsp");
    }

    public List<String> getCategoryUrls() {
        return categoryUrls;
    }

    public List<String> getProductUrls() {
        return productUrls;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

}
