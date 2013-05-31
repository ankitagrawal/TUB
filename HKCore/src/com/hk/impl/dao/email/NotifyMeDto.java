package com.hk.impl.dao.email;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.user.User;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 5/30/13
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class NotifyMeDto {


    private Long id;


    private String name;


    private String email;


    private ProductVariant productVariant;


    int userCount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }


    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
