package com.hk.domain.search;

import javax.persistence.*;

/*
 * User: Pratham
 * Date: 24/07/13  Time: 19:24
*/
@SuppressWarnings("serial")
@Entity
@Table(name = "synonym")
public class Synonym {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "search_synonym", unique = true)
    private String searchSynonym;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "active")
    private boolean active;

    @Column(name = "deleted")
    private boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSearchSynonym() {
        return searchSynonym;
    }

    public void setSearchSynonym(String searchSynonym) {
        this.searchSynonym = searchSynonym;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
