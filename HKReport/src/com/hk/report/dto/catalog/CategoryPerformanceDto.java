package com.hk.report.dto.catalog;

import com.hk.domain.catalog.category.Category;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Jul 11, 2011
 * Time: 12:14:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryPerformanceDto {
  private Category category;
  private Integer distinctOrders;
  private Integer mixedOrders;
  private Integer totalOrders;
  Long skuCount = 0L;
  Double sumOfMrp = 0D;
  Double sumOfHkPrice = 0D;
  Double sumOfHkPricePostAllDiscounts = 0D;
  Double avgOfMrp = 0D;
  Double avgOfHkPrice = 0D;
  Double avgOfHkPricePostAllDiscounts = 0D;
  Double avgOfTotalOrders = 0D;
  Double projectedMrp = 0D;

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public int getDistinctOrders() {
    return distinctOrders;
  }

  public void setDistinctOrders(int distinctOrders) {
    this.distinctOrders = distinctOrders;
  }

  public int getMixedOrders() {
    return mixedOrders;
  }

  public void setMixedOrders(int mixedOrders) {
    this.mixedOrders = mixedOrders;
  }

  public Long getSkuCount() {
    if(skuCount != null){
      return skuCount;
    }else{
      return 0L;
    }
  }

  public void setSkuCount(Long skuCount) {
    this.skuCount = skuCount;
  }

  public Double getSumOfMrp() {
    if(sumOfMrp != null){
      return sumOfMrp;
    }else{
      return 0D;
    }
  }

  public void setSumOfMrp(Double sumOfMrp) {
    this.sumOfMrp = sumOfMrp;
  }

  public Double getSumOfHkPrice() {
    if(sumOfHkPrice != null){
      return sumOfHkPrice;
    }else{
      return 0D;
    }
  }

  public void setSumOfHkPrice(Double sumOfHkPrice) {
    this.sumOfHkPrice = sumOfHkPrice;
  }

  public Double getSumOfHkPricePostAllDiscounts() {
    if(sumOfHkPricePostAllDiscounts != null){
      return sumOfHkPricePostAllDiscounts;
    }
    else{
      return 0D;
    }
  }

  public void setSumOfHkPricePostAllDiscounts(Double sumOfHkPricePostAllDiscounts) {
    this.sumOfHkPricePostAllDiscounts = sumOfHkPricePostAllDiscounts;
  }

  public Double getAvgOfMrp() {
    if(avgOfMrp != null){
      return avgOfMrp;
    }
    else{
      return 0D;
    }
  }

  public void setAvgOfMrp(Double avgOfMrp) {
    this.avgOfMrp = avgOfMrp;
  }

  public Double getAvgOfHkPrice() {
    if(avgOfHkPrice != null){
      return avgOfHkPrice;
    }else{
      return 0D;
    }
  }

  public void setAvgOfHkPrice(Double avgOfHkPrice) {
    this.avgOfHkPrice = avgOfHkPrice;
  }

  public Double getAvgOfHkPricePostAllDiscounts() {
    if(avgOfHkPricePostAllDiscounts != null){
      return avgOfHkPricePostAllDiscounts;
    }else{
      return 0D;
    }
  }

  public void setAvgOfHkPricePostAllDiscounts(Double avgOfHkPricePostAllDiscounts) {
    this.avgOfHkPricePostAllDiscounts = avgOfHkPricePostAllDiscounts;
  }

  public Integer getTotalOrders() {
    return totalOrders;
  }

  public void setTotalOrders(Integer totalOrders) {
    this.totalOrders = totalOrders;
  }

  public Double getAvgOfTotalOrders() {
    if(avgOfTotalOrders != null){
      return avgOfTotalOrders;
    }else{
      return 0D;
    }
  }

  public void setAvgOfTotalOrders(Double avgOfTotalOrders) {
    this.avgOfTotalOrders = avgOfTotalOrders;
  }

  public Double getProjectedMrp() {
    if(projectedMrp != null){
      return projectedMrp;
    }else{
      return 0D;
    }
  }

  public void setProjectedMrp(Double projectedMrp) {
    this.projectedMrp = projectedMrp;
  }
}
