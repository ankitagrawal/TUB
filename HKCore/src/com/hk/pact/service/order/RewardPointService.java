package com.hk.pact.service.order;

import java.util.Date;
import java.util.List;

import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.offer.rewardPoint.RewardPointStatus;
import com.hk.domain.offer.rewardPoint.RewardPointTxn;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.InvalidRewardPointsException;

public interface RewardPointService {

    public RewardPointMode getRewardPointMode(EnumRewardPointMode enumRewardPointMode);

    public RewardPointStatus getRewardPointStatus(EnumRewardPointStatus enumRewardPointStatus);

    public RewardPoint addRewardPoints(User referredBy, User referredUser, Order referredOrder, Double value, String comment, EnumRewardPointStatus rewardPointStatus,
            RewardPointMode rewardPointMode) throws InvalidRewardPointsException;

    public RewardPointTxn createRewardPointTxnForApprovedRewardPoints(RewardPoint rewardPoint, Date expiryDate);

    // logic for cashback offer
    public void awardRewardPoints(Order order);

    public RewardPoint findByReferredOrderAndRewardMode(Order order, RewardPointMode rewardPointMode);

    public List<RewardPoint> findRewardPoints(Order order, List<RewardPointMode> rewardPointMode);

    public void approvePendingRewardPointsForOrder(Order order);

    public List<RewardPoint> findByReferredOrder(Order order);

    /**
     * these are the points that user is eligible to redeem
     * @param login
     * @return
     */
    public Double getEligibleRewardPointsForUser(String login);

    public Double getTotalRedeemablePoints(User user);

    public void approveRewardPoints(List<RewardPoint> rewardPointList, Date expiryDate);
    
    public void cancelReferredOrderRewardPoint(RewardPoint rewardPoint);
    
    public void redeemRewardPoints(Order order, Double rewardPointsUsed);

    public void cancelRewardPoints(User user,  Double cancelRewardPoints);

}
