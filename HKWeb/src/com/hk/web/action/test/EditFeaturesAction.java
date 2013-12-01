package com.hk.web.action.test;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductFeature;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.pact.service.catalog.ProductService;

@Component
public class EditFeaturesAction extends BaseAction {
  private static Logger logger = Logger.getLogger(EditFeaturesAction.class);

  private List<ProductFeature> productFeatures;
  private Product product;


  @Autowired
  ProductService productService;
  

  @DefaultHandler
  public Resolution pre() {
    logger.info("product id " + product);
    productFeatures = product.getProductFeatures();
    return new ForwardResolution("/pages/editFeatures.jsp");
  }

  public Resolution save() {
    logger.info("product id " + product);
    if (productFeatures != null) {
      for (ProductFeature productFeature : productFeatures) {
        if (productFeature != null) {
          productFeature.setProduct(product);
        }
        getBaseDao().save(productFeature);
      }
    }
    productService.save(product);
    return new ForwardResolution("/pages/test/close.jsp");
  }

  public List<ProductFeature> getProductFeatures() {
    return productFeatures;
  }

  public void setProductFeatures(List<ProductFeature> productFeatures) {
    this.productFeatures = productFeatures;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

}
