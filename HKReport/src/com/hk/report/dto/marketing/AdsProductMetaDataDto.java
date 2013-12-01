package com.hk.report.dto.marketing;

import com.hk.domain.catalog.product.Product;
import com.hk.dto.menu.MenuNode;

public class AdsProductMetaDataDto {

  private Product product;
  private MenuNode menuNode;

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public MenuNode getMenuNode() {
    return menuNode;
  }

  public void setMenuNode(MenuNode menuNode) {
    this.menuNode = menuNode;
  }
}
