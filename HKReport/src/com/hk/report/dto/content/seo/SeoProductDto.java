package com.hk.report.dto.content.seo;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.content.SeoData;


public class SeoProductDto {
  private Product  product;
  private SeoData seoData;


  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

   public SeoData getSeoData() {
    return seoData;
  }

  public void setSeoData(SeoData seoData) {
    this.seoData = seoData;
  }
}
