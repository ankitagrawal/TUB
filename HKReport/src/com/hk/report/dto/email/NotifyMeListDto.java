package com.hk.report.dto.email;

import java.util.ArrayList;
import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.marketing.NotifyMe;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Nov 29, 2011
 * Time: 12:46:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class NotifyMeListDto {

  private ProductVariant productVariant;
  private List<NotifyMe> notifyMeList = new ArrayList<NotifyMe>();
  private Boolean isSelected;

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public List<NotifyMe> getNotifyMeList() {
    return notifyMeList;
  }

  public void setNotifyMeList(List<NotifyMe> notifyMeList) {
    this.notifyMeList = notifyMeList;
  }

  public Boolean isSelected() {
    return isSelected;
  }

  public void setSelected(Boolean selected) {
    isSelected = selected;
  }
}
