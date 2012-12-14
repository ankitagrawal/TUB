package com.hk.constants.clm;

import com.hk.constants.catalog.category.CategoryConstants;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 5, 2012
 * Time: 3:35:43 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumCLMMargin {
    Diabetes(CategoryConstants.DIABETES,7.36),
    Beauty(CategoryConstants.BEAUTY,1.99),
    Parenting(CategoryConstants.BABY,1.99),
    HomeDevices(CategoryConstants.HEALTH_DEVICES,1.23),
    Sports(CategoryConstants.SPORTS,0.93),
    Eye(CategoryConstants.EYE,1.00),
    PersonalCare(CategoryConstants.PERSONAL_CARE,3.36 ),
    Nutrition(CategoryConstants.NUTRITION,2.17),
    Services(CategoryConstants.SERVICES,1.08)
    ;
    private java.lang.String category;
    private java.lang.Double margin;
    
    private EnumCLMMargin(String category,Double margin){
        this.category=category;
        this.margin=margin;
    }

    public static EnumCLMMargin getMarginFromCategory(String category) {
        for (EnumCLMMargin clmMargin : values()) {
            if (clmMargin.getCategory().equals(category)) return clmMargin;
        }
        return null;
    }
    
    public String getCategory() {
        return category;
    }

    public Double getMargin() {
        return margin;
    }
}
