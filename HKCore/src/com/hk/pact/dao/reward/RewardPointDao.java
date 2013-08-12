package com.hk.pact.dao.reward;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.offer.rewardPoint.RewardPointStatus;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.InvalidRewardPointsException;
import com.hk.pact.dao.BaseDao;

public interface RewardPointDao extends BaseDao {
    public static final double MAX_REWARD_POINTS = 10000;

    public RewardPoint save(RewardPoint rewardPoint);

    public RewardPoint creditPointsToReferrer(User referredBy, User referredUser, Order referredOrder);

    public RewardPoint addRewardPoints(User referredBy, User referredUser, Order referredOrder, Double value, String comment, EnumRewardPointStatus rewardPointStatus,
            RewardPointMode rewardPointMode) throws InvalidRewardPointsException;

    public List<RewardPoint> findByStatus(List<Long> statusIds);

    public Page searchRewardPoints(List<RewardPointMode> modes, List<RewardPointStatus> status, int pageNo, int perPage);

    public List<RewardPoint> findByReferredOrder(Order order);

    public RewardPoint findByReferredOrderAndRewardMode(Order order, RewardPointMode rewardPointMode);

    public List<RewardPoint> findRewardPoints(Order order,List<RewardPointMode> rewardPointMode);

    public RewardPoint cancelRewardPoint(RewardPoint rewardPoint);

    public RewardPoint findByReferredUser(User referredUser);

    public List<RewardPoint> findByUser(User user);
}
