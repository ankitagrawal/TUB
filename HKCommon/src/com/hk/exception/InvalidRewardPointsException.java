package com.hk.exception;

/**
 * Created by IntelliJ IDEA. User: meenal Date: Apr 12, 2012 Time: 1:48:53 PM To change this template use File |
 * Settings | File Templates.
 */
@SuppressWarnings("serial")
public class InvalidRewardPointsException extends HealthkartRuntimeException {

    @SuppressWarnings("unused")
    private double rewardPointsValue;

    public InvalidRewardPointsException(Double value) {
        super("Reward points must be less than 10000 entered reward points are: " + value);
        this.rewardPointsValue = value;
    }
}
