package com.hk.util;

import com.hk.domain.catalog.product.Product;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Aug 18, 2011
 * Time: 11:36:49 AM
 * To change this template use File | Settings | File Templates.
 */

public class AffiliateLinkManager {
  
  public String generateTextLinkForAffiliate(Product product, String code, String anchor) {
    return "<a href='http://www.healthkart.com/product/${product.getSlug()}/${product.getId()}?affid=${code}'>${anchor}</a>"
  }

  public String generateButtonLinkForAffiliate(Product product, String code, String anchor) {
    return "<a href='http://www.healthkart.com/product/${product.getSlug()}/${product.getId()}?affid=${code}'<img src='http://img.healthkart.com/images/icons/${anchor}.png'></a>"
  }

  public String generateAnchorText(Product product) {
    return "Buy ${product.getName()} from HealthKart.com"
  }

}