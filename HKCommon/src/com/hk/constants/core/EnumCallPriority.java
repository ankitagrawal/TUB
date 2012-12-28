package com.hk.constants.core;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/12/12
 * Time: 9:26 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * defines the possible values for user calling priority..Has to be used by Drishti for deciding about queue when call comes
 * Higher number means higher Priority
 */
public enum EnumCallPriority {

    PRIORITY_ONE(1),PRIORITY_TWO(2),PRIORITY_THREE(3),PRIORITY_FOUR(4),PRIORITY_FIVE(5);

    private Integer value;

    EnumCallPriority(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
