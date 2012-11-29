package com.hk.cache.vo;

import java.io.Serializable;

import com.hk.domain.catalog.category.Category;

/**
 * @author vaibhav.adlakha
 */
@SuppressWarnings("serial")
public class CategoryVO implements Serializable {

    private String name;

    private String displayName;

    public CategoryVO(Category category) {
        this.name = category.getName();
        this.displayName = category.getDisplayName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
