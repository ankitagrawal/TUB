package com.hk.util.io;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jul 20, 2012
 * Time: 7:58:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class LongStringUniqueObject {

    private Long id;
    private String value;

    public LongStringUniqueObject() {

    }

    public LongStringUniqueObject(Long id, String value) {
        this.id = id;
        this.value = value;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof LongStringUniqueObject)) return false;
        LongStringUniqueObject o = (LongStringUniqueObject) obj;
        if (this.getId().equals(o.getId()) && this.getValue().equalsIgnoreCase(o.getValue())) {
            return true;
        }
        return false;
    }


    @Override
    public int hashCode() {
        int hash = 6;
        hash = 31 * hash + (null == id ? 0 : id.hashCode());
        hash = 31 * hash + (null == value ? 0 : value.hashCode());
        return hash;
    }
}
