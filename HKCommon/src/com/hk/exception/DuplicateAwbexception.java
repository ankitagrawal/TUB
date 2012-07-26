package com.hk.exception;

import com.hk.util.io.LongStringUniqueObject;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jul 20, 2012
 * Time: 7:53:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DuplicateAwbexception extends HealthkartRuntimeException {
    LongStringUniqueObject uniqueObject;

    public DuplicateAwbexception(String message, LongStringUniqueObject uniqueObject) {
        super(message);
        this.uniqueObject = uniqueObject;

    }

    public DuplicateAwbexception(LongStringUniqueObject uniqueObject) {
        super("The AWb -- >" + uniqueObject.getValue() + "  is present in Excel twice for courier --> " + uniqueObject.getId());
        this.uniqueObject = uniqueObject;
    }


    public LongStringUniqueObject getUniqueObject() {
        return uniqueObject;
    }
}
