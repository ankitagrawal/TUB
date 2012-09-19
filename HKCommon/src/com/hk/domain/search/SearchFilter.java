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
    private String value;

    public SearchFilter(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}