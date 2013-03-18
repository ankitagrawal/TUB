package com.hk.constants.marketing;

public enum EnumProductReferrer {

  searchPage(1L, "search-page"),
  nutrition(2L, "nutrition"),
  sports(3L, "sports"),
  diabetes(4L, "diabetes"),
  healthDevices(5L, "health-devices"),
  eye(6L, "eye"),
  personalCare(7L, "personal-care"),
  beauty(8L, "beauty"),
  parenting(9L, "parenting"),
  services(10L, "services"),
  homePage(11L, "home-page"),
  relatedProductsPage(12L, "related-products-page"),
  brandPage(13L, "brand-page"),
  mobile_catalog(14L, "mobile-catalog"),
  mobile_search(15L, "mobile-search"),
  replenish_email(16L, "replenish-email"),
  notify_email(17L, "notify-email"),
  homePageBanner(18L, "home_page_banner"),
  categoryHomePage(19L, "category_home_page"),
  categoryHomePageBanner(20L, "category_home_page_banner"),

	;

  private String name;
  private Long id;

  EnumProductReferrer(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

}