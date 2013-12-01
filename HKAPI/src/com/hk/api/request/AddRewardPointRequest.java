package com.hk.api.request;

public class AddRewardPointRequest extends SecureAPIRequest {

    private String login;
    private double rewardPoints;

    private String comment;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public double getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(double rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
