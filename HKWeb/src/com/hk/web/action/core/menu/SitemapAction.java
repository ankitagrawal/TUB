package com.hk.web.action.core.menu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.Product;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.manager.LinkManager;
import com.hk.service.ProductService;

@UrlBinding("/sitemap.xml")
@Component
public class SitemapAction extends BaseAction {

    List<String>           categoryUrls = new ArrayList<String>();
    List<String>           productUrls  = new ArrayList<String>();

    MenuHelper             menuHelper;
    LinkManager            linkManager;
    private ProductService productService;

    public Resolution pre() {
        List<MenuNode> nodes = menuHelper.getMenuNodesFlat();
        for (MenuNode node : nodes) {
            categoryUrls.add("http://www.healthkart.com" + node.getUrl());
        }
        List<Product> products = getProductService().getAllProducts();
        for (Product product : products) {
            productUrls.add("http://www.healthkart.com/product/" + product.getSlug() + "/" + product.getId());
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
