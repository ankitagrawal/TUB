package com.hk.util;

import java.util.Map;
import java.util.HashMap;

import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.marketing.EnumProductReferrer;
import com.hk.constants.marketing.ProductReferrerConstants;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: May 8, 2012
 * Time: 3:44:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductReferrerMapper {

  static Map<String,Long> referrerMap = new HashMap();

  static{
    referrerMap.put(CategoryConstants.BABY, EnumProductReferrer.parenting.getId());
    referrerMap.put(CategoryConstants.BEAUTY, EnumProductReferrer.beauty.getId());
    referrerMap.put(CategoryConstants.EYE, EnumProductReferrer.eye.getId());
    referrerMap.put(CategoryConstants.HOME_DEVICES, EnumProductReferrer.homeDevices.getId());
    referrerMap.put(CategoryConstants.NUTRITION, EnumProductReferrer.nutrition.getId());
    referrerMap.put(CategoryConstants.PERSONAL_CARE, EnumProductReferrer.personalCare.getId());
    referrerMap.put(CategoryConstants.SERVICES, EnumProductReferrer.services.getId());
    referrerMap.put(CategoryConstants.SPORTS, EnumProductReferrer.sports.getId());
    referrerMap.put(CategoryConstants.DIABETES, EnumProductReferrer.diabetes.getId());
    referrerMap.put(ProductReferrerConstants.BRAND_PAGE, EnumProductReferrer.brandPage.getId());
    referrerMap.put(ProductReferrerConstants.HOME_PAGE, EnumProductReferrer.homePage.getId());
    referrerMap.put(ProductReferrerConstants.RELATED_PRODUCTS_PAGE, EnumProductReferrer.relatedProductsPage.getId());
    referrerMap.put(ProductReferrerConstants.SEARCH_PAGE, EnumProductReferrer.searchPage.getId());
  }

  public static Long getProductReferrerid(String referrer){
    return referrerMap.get(referrer);
  }
}
