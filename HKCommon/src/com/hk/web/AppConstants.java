package com.hk.web;

public class AppConstants {

    public static String contextPath;
    public static String appBasePath;
    public static final String healthkartMainSite = "http://www.healthkart.com";


    public static String getContextPath() {
        return contextPath;
    }

    public static String getAppBasePath() {
        return appBasePath;
    }


    public static String getAppClasspathRootPath() {
        return AppConstants.appBasePath + "WEB-INF";
    }


}
