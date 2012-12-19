package com.hk.api.client.dto.user;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 */
public class HKAPIUserDTO {
    private String name;
    private String email;
    private String password;
    private Long storeId;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
