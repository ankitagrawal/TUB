package com.hk.constants.catalog.product;

/**
 * Created by IntelliJ IDEA.
 * User: PRATHAM
 * Date: 2/22/12
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumProductThumbTag {
  HOME_PAGE(10L, "Home page"),
  SEARCH_RESULT(20L, "Search Results"),
  CATALOG_PAGE(30L, "Cataloging"),
  CATEGORY_HOME_PAGE(40L, "Category home page"),
  RELATED_PRODUCTS(50L, "Related Products")
  ;

  private java.lang.String name;
  private java.lang.Long id;

  EnumProductThumbTag(Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

}

