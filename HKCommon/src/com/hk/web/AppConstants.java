package com.hk.web;

public class AppConstants {

    public static String  contextPath;
    public static String  appBasePath;
    public static boolean isHybridRelease;
    public static boolean syncEdge;

    public static String getContextPath() {
        return contextPath;
    }

    public static String getAppBasePath() {
        return appBasePath;
    }

    public static String getAppClasspathRootPath() {
        return AppConstants.appBasePath + "WEB-INF";
    }

    public static boolean isHybridRelease() {
        return isHybridRelease;
    }

    public static boolean isSyncEdge() {
        return syncEdge;
    }

}
