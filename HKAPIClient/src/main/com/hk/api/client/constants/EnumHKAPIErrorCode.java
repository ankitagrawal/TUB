package com.hk.api.client.constants;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 2:44 PM
 */
public enum EnumHKAPIErrorCode {
    //TODO we can segregate operation type specific error codes once we scale up the api

    UserDoesNotExist(10,"user does not exist"),
    InvalidAppToken(20, "Invalid App Token"),
    InvalidUserAccessToken(30, "Invalid User Access Token"),
    InvalidAppKey(40,"Invalid App Key"),
    TokenExpired(50, "Token Expired"),
    InvalidTokenSignature(60, "Invalid Token Signature"),
    UserAlreadyExists(70,"User already exists"),
    InternalError(9999, "Internal Error");

    private java.lang.String message;

    private int id;

    EnumHKAPIErrorCode(int id, java.lang.String message) {
        this.message = message;
        this.id = id;
    }

    public java.lang.String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public static String getMessageFromErrorCode(int id){
        for(EnumHKAPIErrorCode enumHKAPIErrorCode:EnumHKAPIErrorCode.values()){
            if(enumHKAPIErrorCode.getId()==id){
                return enumHKAPIErrorCode.getMessage();
            }
        }
        return "";
    }
}

