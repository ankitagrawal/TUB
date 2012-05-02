package com.hk.dto.menu;

import java.util.ArrayList;
import java.util.List;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;

public class MenuNode {

  private String name;
  private String url;

  private int level;

  private List<MenuNode> childNodes = new ArrayList<MenuNode>();
  private MenuNode parentNode;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getSlug() {
    if (this.url == null) return null;
    return url.substring(url.lastIndexOf("/") + 1, url.length());
  }

  public List<MenuNode> getChildNodes() {
    return childNodes;
  }

  public void setChildNodes(List<MenuNode> childNodes) {
    this.childNodes = childNodes;
  }

  public MenuNode getParentNode() {
    return parentNode;
  }

  public void setParentNode(MenuNode parentNode) {
    this.parentNode = parentNode;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  @Override
  public String toString() {
    int childCount = this.childNodes != null ? childNodes.size() : 0;
    return name + " - " + url + " ["+childCount+" children]";
  }

  public boolean hasProduct(Product product) {
    boolean hasProduct = true;
    if (!doesMenuHaveAnyCategory(product.getCategories(), this)) {
      hasProduct = false;
    }
    MenuNode parentMenuNode = this.getParentNode();
    while (parentMenuNode != null) {
      if (!doesMenuHaveAnyCategory(product.getCategories(), parentMenuNode)) {
        hasProduct = false;
      }
      parentMenuNode = parentMenuNode.getParentNode();
    }
    return hasProduct;
  }

  private boolean doesMenuHaveAnyCategory(List<Category> categories, MenuNode menuNode) {
    for (Category category : categories) {
      if (menuNode.getSlug().equals(category.getName())) return true;
    }
    return false;
  }

}
