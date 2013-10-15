package com.hk.constants.edge;

public class ServiceEndPoints {

    public static final String  URI_SEPARATOR    = "/";

    private static final String END_POINT_PREFIX = "/api";

    public static final String  STORE_MENU       = END_POINT_PREFIX.concat("/menu/");
    public static final String  STORE_VARIANT    = END_POINT_PREFIX.concat("/variant/");
    public static final String  SYNC             = END_POINT_PREFIX.concat("/edge/sync/");
    
    public static final String  USER_SESSION             = END_POINT_PREFIX.concat("/user/session/");
}
