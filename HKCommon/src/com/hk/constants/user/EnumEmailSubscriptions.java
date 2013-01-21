package com.hk.constants.user;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 1/11/13
 * Time: 10:52 PM
 * To change this template use File | Settings | File Templates.
 */

public enum EnumEmailSubscriptions {

    /*public final static int PROMOTIONAL_OFFERS = 2;
    public final static int NEWSLETTERS = 4;
    public final static int NOTIFY_ME = 8;
    public final static int PRODUCT_REPLENISHMENT = 16;*/
    UNSUBSCRIBED(1),
    PROMOTIONAL_OFFERS(2),
    NEWSLETTERS(4),
    NOTIFY_ME(8),
    PRODUCT_REPLENISHMENT(16),
    OTHERS(32);

    private int value;
	public final static int SUBSCRIBE_ALL = 30;  //11110

    EnumEmailSubscriptions(int id){
        this.value = id;
    }

    public int getValue(){
        return value;
    }

    public static boolean isSubscribed(EnumEmailSubscriptions subscriptionEnum,int subscriptionMask){
        return   ((subscriptionMask & subscriptionEnum.getValue()) == subscriptionEnum.getValue());
    }
}

