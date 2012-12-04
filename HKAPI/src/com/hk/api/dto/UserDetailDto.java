package com.hk.api.dto;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 11/23/12
 * Time: 12:16 PM
 */
public class UserDetailDto extends HkAPIBaseDto {

    private String email;
    private String name;
    private String password;
    private Double rewardPoints;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(Double rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
