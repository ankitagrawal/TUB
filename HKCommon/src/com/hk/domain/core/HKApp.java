package com.hk.domain.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "city")
public class HKApp implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long    id;

    @Column(name = "name", nullable = false, length = 500)
    private String  name;

    @Column(name = "key", nullable = false, length = 500)
    private String  key;

    @Column(name = "login_success_redirect_url", nullable = false, length = 500)
    private String  loginSuccessURL;

    @Column(name = "home_url", nullable = false, length = 500)
    private String  homeURL;

    @Column(name = "default_auth_token_expiry", nullable = false)
    private int     authTokenDefaultExpiry;

    @Column(name = "enabled", nullable = false, length = 500)
    private boolean enabled;

    @Column(name = "create_dt", nullable = false, length = 500)
    private Date    createDate = new Date();

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLoginSuccessURL() {
        return loginSuccessURL;
    }

    public void setLoginSuccessURL(String loginSuccessURL) {
        this.loginSuccessURL = loginSuccessURL;
    }

    public String getHomeURL() {
        return homeURL;
    }

    public void setHomeURL(String homeURL) {
        this.homeURL = homeURL;
    }

    public int getAuthTokenDefaultExpiry() {
        return authTokenDefaultExpiry;
    }

    public void setAuthTokenDefaultExpiry(int authTokenDefaultExpiry) {
        this.authTokenDefaultExpiry = authTokenDefaultExpiry;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
