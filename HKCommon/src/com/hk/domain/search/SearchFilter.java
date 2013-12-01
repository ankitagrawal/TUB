package com.hk.domain.search;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 7/22/12
 * Time: 8:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchFilter {
    private String name;
    private Object value;

    public SearchFilter(String name, Object value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}