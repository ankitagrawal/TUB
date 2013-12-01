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

    // private Set<String> brands = new HashSet<String>();

    public CategoryVO(Category category) {
        if (category != null) {
            this.name = category.getName();
            this.displayName = category.getDisplayName();
        }
        // this.brands = brandsInCategory;
    }

    public Category getCategory() {
        Category category = new Category();
        category.setName(this.name);
        category.setDisplayName(this.displayName);
        return category;
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

    /*
     * public Set<String> getBrands() { return brands; } public void setBrands(Set<String> brands) { this.brands =
     * brands; }
     */

}
