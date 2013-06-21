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
  email_newsletter(18L, "email_newsletter"),
  homePageBanner(19L, "home_page_banner"),
  categoryHomePage(20L, "category_home_page"),
  categoryHomePageBanner(21L, "category_home_page_banner"),
	healthNutrition(22L, "health-nutrition"),
	sportsNutrition(23L, "sports-nutrition"),
  homeLiving(24L, "home-living"),
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

  public static Long getProductReferrerId(String name){
    for (EnumProductReferrer productReferrer : EnumProductReferrer.values()) {
      if(productReferrer.getName().equals(name)){
        return productReferrer.getId();
      }
    }
    return 0L;
  }

}