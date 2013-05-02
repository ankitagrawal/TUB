package com.hk.constants.catalog.category;

import com.hk.domain.catalog.category.Category;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("serial")
public class CategoryConstants {

  //Categories Specified in Alphabetical Order

  public static final String BABY = "parenting";
  public static final String BEAUTY = "beauty";
  public static final String DIABETES = "diabetes";
  public static final String EYE = "eye";
  public static final String HOME = "home";
  public static final String HEALTH_DEVICES = "health-devices";
  public static final String DYNAMIC_REMARKETING="dynamic-remarketing";

	//TODO remove nutrition after sometime
  public static final String NUTRITION = "nutrition";
  public static final String PERSONAL_CARE = "personal-care";
  public static final String SERVICES = "services";
  public static final String SPORTS = "sports";
  public static final String HK_BRAND = "hk-brand";
  public static final String HEALTH_NUTRITION = "health-nutrition";
  public static final String SPORTS_NUTRITION = "sports-nutrition";
  public static final String HOME_LIVING = "home-living";


  public static final Long BABY_TARGET_SALES = 272827L;
  public static final Long BEAUTY_TARGET_SALES = 1471747L;
  public static final Long DIABETES_TARGET_SALES = 1795070L;
  public static final Long EYE_TARGET_SALES = 716364L;
  public static final Long HEALTH_DEVICES_TARGET_SALES = 1619206L;
  public static final Long NUTRITION_TARGET_SALES = 13040424L;
  public static final Long PERSONAL_CARE_TARGET_SALES = 764110L;
  public static final Long SERVICES_TARGET_SALES = 49348L;
  public static final Long SPORTS_TARGET_SALES = 658710L;


  public static final Long BABY_TARGET_ORDER_COUNT = 18L;
  public static final Long BEAUTY_TARGET_ORDER_COUNT = 88L;
  public static final Long DIABETES_TARGET_ORDER_COUNT = 50L;
  public static final Long EYE_TARGET_ORDER_COUNT = 32L;
  public static final Long HEALTH_DEVICES_TARGET_ORDER_COUNT = 38L;
  public static final Long NUTRITION_TARGET_ORDER_COUNT = 204L;
  public static final Long PERSONAL_CARE_TARGET_ORDER_COUNT = 62L;
  public static final Long SERVICES_TARGET_ORDER_COUNT = 2L;
  public static final Long SPORTS_TARGET_ORDER_COUNT = 32L;


  public static final String YESTERDAY_CATEGORIERS_ORDER_REPORT_DTO = "YesterdayCategoriesOrderReportDto";
  public static final String MONTHLY_CATEGORIES_ORDER_REPORT_DTO = "MonthlyCategoriesOrderReportDto";
  public static final String TARGET_MRP_SALES_MAP = "targetMrpSalesMap";
  public static final String TARGET_ORDER_COUNT_MAP = "targetOrderCountMap";
  public static final String CATEGORIES_ORDER_REPORT_DTOS_MAP = "categoriesOrderReportDtosMap";
  public static final String SIX_HOUR_CATEGORIES_ORDER_REPORT_DTO = "SixHourlyCategoriesReportDto";
  public static final String DAILY_CATEGORIES_ORDER_REPORT_DTO = "DailyCategoriesReportDto";
  public static final String SIX_HOURLY_CATEGORIES_ORDER_REPORT_DTOS_MAP = "sixHourlyCategoryOrderReportDtosMap";
  public static final String YESTERDAY_DATE = "yesterdayDate";
  public static final String START_SIX_HOUR = "startSixHour";
  public static final String END_SIX_HOUR = "endSixHour";
  public static final String TARGET_DAILY_MRP_SALES_MAP = "targetDailyMrpSalesMap";

	public static  Category eyeGlasses = new  Category("eyeglasses", "Eyeglasses");

  public static final Map<String, Long> targetMrpSalesMap = new HashMap<String, Long>() {
    {
      put(CategoryConstants.BABY, CategoryConstants.BABY_TARGET_SALES);
      put(CategoryConstants.BEAUTY, CategoryConstants.BEAUTY_TARGET_SALES);
      put(CategoryConstants.DIABETES, CategoryConstants.DIABETES_TARGET_SALES);
      put(CategoryConstants.EYE, CategoryConstants.EYE_TARGET_SALES);
      put(CategoryConstants.HEALTH_DEVICES, CategoryConstants.HEALTH_DEVICES_TARGET_SALES);
      put(CategoryConstants.NUTRITION, CategoryConstants.NUTRITION_TARGET_SALES);
      put(CategoryConstants.PERSONAL_CARE, CategoryConstants.PERSONAL_CARE_TARGET_SALES);
      put(CategoryConstants.SERVICES, CategoryConstants.SERVICES_TARGET_SALES);
      put(CategoryConstants.SPORTS, CategoryConstants.SPORTS_TARGET_SALES);
    }
  };

  public static final Map<String, Long> targetOrderCountMap = new HashMap<String, Long>() {
    {
      put(CategoryConstants.BABY, CategoryConstants.BABY_TARGET_ORDER_COUNT);
      put(CategoryConstants.BEAUTY, CategoryConstants.BEAUTY_TARGET_ORDER_COUNT);
      put(CategoryConstants.DIABETES, CategoryConstants.DIABETES_TARGET_ORDER_COUNT);
      put(CategoryConstants.EYE, CategoryConstants.EYE_TARGET_ORDER_COUNT);
      put(CategoryConstants.HEALTH_DEVICES, CategoryConstants.HEALTH_DEVICES_TARGET_ORDER_COUNT);
      put(CategoryConstants.NUTRITION, CategoryConstants.NUTRITION_TARGET_ORDER_COUNT);
      put(CategoryConstants.PERSONAL_CARE, CategoryConstants.PERSONAL_CARE_TARGET_ORDER_COUNT);
      put(CategoryConstants.SERVICES, CategoryConstants.SERVICES_TARGET_ORDER_COUNT);
      put(CategoryConstants.SPORTS, CategoryConstants.SPORTS_TARGET_ORDER_COUNT);
    }
  };

}
