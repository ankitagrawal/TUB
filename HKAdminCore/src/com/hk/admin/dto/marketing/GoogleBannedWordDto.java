package com.hk.admin.dto.marketing;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.marketing.GoogleBannedWord;

/**
 * Created by IntelliJ IDEA.
 * User: Rahul
 * Date: Dec 30, 2011
 * Time: 4:23:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoogleBannedWordDto {

  GoogleBannedWord googleBannedWord;
  Product product;
  String existsInProductName;
  String existsInProductOverview;
  String existsInProductDescription;
  String existsInProductFeatureName;
  String existsInProductFeatureValue;

  public GoogleBannedWord getGoogleBannedWord() {
    return googleBannedWord;
  }

  public void setGoogleBannedWord(GoogleBannedWord googleBannedWord) {
    this.googleBannedWord = googleBannedWord;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public String getExistsInProductName() {
    return existsInProductName;
  }

  public void setExistsInProductName(String existsInProductName) {
    this.existsInProductName = existsInProductName;
  }

  public String getExistsInProductOverview() {
    return existsInProductOverview;
  }

  public void setExistsInProductOverview(String existsInProductOverview) {
    this.existsInProductOverview = existsInProductOverview;
  }

  public String getExistsInProductDescription() {
    return existsInProductDescription;
  }

  public void setExistsInProductDescription(String existsInProductDescription) {
    this.existsInProductDescription = existsInProductDescription;
  }

  public String getExistsInProductFeatureName() {
    return existsInProductFeatureName;
  }

  public void setExistsInProductFeatureName(String existsInProductFeatureName) {
    this.existsInProductFeatureName = existsInProductFeatureName;
  }

  public String getExistsInProductFeatureValue() {
    return existsInProductFeatureValue;
  }

  public void setExistsInProductFeatureValue(String existsInProductFeatureValue) {
    this.existsInProductFeatureValue = existsInProductFeatureValue;
  }
}
