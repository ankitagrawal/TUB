package com.hk.domain.clm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.user.User;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 6/25/12
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="user_cat_karma_profile")
public class CategoryKarmaProfile{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_name", nullable = false)
    private Category category;

    @Column(name = "cat_karma_points", nullable = false)
    private Double karmaPoints;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getKarmaPoints() {
        return karmaPoints;
    }

    public void setKarmaPoints(Double karmaPoints) {
        this.karmaPoints = karmaPoints;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
