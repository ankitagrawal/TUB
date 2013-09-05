package com.hk.api.edge.ext.response.user;

import com.hk.api.edge.ext.response.AbstractApiBaseResponse;

import java.util.HashSet;
import java.util.Set;


public class UserApiBaseResponse extends AbstractApiBaseResponse {

    private Long id;
    private String nm;

    private Set<String> roles = new HashSet<String>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }


    @Override
    protected String[] getKeys() {
        return new String[]{
                "id",
                "nm",
                "roles"
        };
    }

    @Override
    protected Object[] getValues() {
        return new Object[]{
                this.id,
                this.nm,
                this.roles
        };
    }
}
