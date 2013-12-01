package com.hk.domain.search;

public class SortFilter{

    private String name;
    private String sortOrder;

    public SortFilter(String name, String sortOrder){
        this.name = name;
        this.sortOrder = sortOrder;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
