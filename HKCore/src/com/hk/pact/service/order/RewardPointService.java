package com.hk.pact.service.order;

import java.util.List;

import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.offer.rewardPoint.RewardPointStatus;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.InvalidRewardPointsException;

public interface RewardPointService {
    
    public RewardPointMode getRewardPointMode(EnumRewardPointMode enumRewardPointMode);
    
    public RewardPointStatus getRewardPointStatus(EnumRewardPointStatus enumRewardPointStatus);
    
    public RewardPoint addRewardPoints(User referredBy, User referredUser, Order referredOrder, Double value, String comment, EnumRewardPointStatus rewardPointStatus,
            RewardPointMode rewardPointMode) throws InvalidRewardPointsException ;

    // logic for cashback offer
    public void awardRewardPoints(Order order);
    
    public RewardPoint findByReferredOrderAndRewardMode(Order order, RewardPointMode rewardPointMode);

    public void approvePendingRewardPointsForOrder(Order order);
    
    public List<RewardPoint> findByReferredOrder(Order order) ;
}
