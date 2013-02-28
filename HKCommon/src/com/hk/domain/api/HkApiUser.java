package com.hk.domain.api;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@NamedQueries({
    @NamedQuery(name = "getApiUserByName", query = "select au from HkApiUser au  where au.name = :apiUserName "),
    @NamedQuery(name = "getApiUserByApiKey", query = "select au from HkApiUser au  where au.apiKey = :apiKey")
})
@SuppressWarnings("serial")
@Entity
@Table(name = "hk_api_user")
public class HkApiUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long    id;

    @Column(name = "name", nullable = false, length = 500)
    private String  name;

    @Column(name = "api_key", nullable = false, length = 500)
    private String  apiKey;

    @Column(name = "secret_key", nullable = false, length = 300)
    private String  secretKey;

    @Column(name = "login_success_redirect_url", nullable = false, length = 500)
    private String  loginSuccessRedirectUrl;

    @Column(name = "home_url", nullable = false, length = 500)
    private String  homeUrl;

    @Column(name = "default_auth_token_expiry", nullable = false)
    private int     defaultTokenExpiry;

    @Column(name = "enabled", nullable = false, length = 500)
    private boolean enabled;

    @Column(name = "create_dt", nullable = false, length = 500)
    private Date    createDate;

    @Column(name = "order_placement_enabled", nullable = false)
    private boolean orderPlacementEnabled;

    @Column(name = "terms", length = 500)
    private String  terms;

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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getLoginSuccessRedirectUrl() {
        return loginSuccessRedirectUrl;
    }

    public void setLoginSuccessRedirectUrl(String loginSuccessRedirectUrl) {
        this.loginSuccessRedirectUrl = loginSuccessRedirectUrl;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public int getDefaultTokenExpiry() {
        return defaultTokenExpiry;
    }

    public void setDefaultTokenExpiry(int defaultTokenExpiry) {
        this.defaultTokenExpiry = defaultTokenExpiry;
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean isOrderPlacementEnabled() {
        return orderPlacementEnabled;
    }

    public void setOrderPlacementEnabled(boolean orderPlacementEnabled) {
        this.orderPlacementEnabled = orderPlacementEnabled;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }
}
