package com.hk.pact.dao.reward;

import java.util.Date;
import java.util.List;

import com.hk.constants.discount.EnumRewardPointTxnType;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointTxn;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface RewardPointTxnDao extends BaseDao {

    public RewardPointTxn save(RewardPointTxn rewardPointTxn);

    public RewardPointTxn createRewardPointAddTxn(RewardPoint rewardPoint, Date expiryDate);

    public List<RewardPointTxn> findActiveTxns(User user);

    public List<RewardPointTxn> findActiveAddTxnsSortedByExpiryDateAsc(User user);

    public RewardPointTxn createRewardPointRedeemTxn(RewardPoint rewardPoint, Double unclaimedPoints, Date rewardPointExpiryDate, Order redeemedOrder);

    public RewardPointTxn createRefundTxn(RewardPointTxn rewardPointTxn, Double percentage);

    public RewardPointTxn createRewardPointCancelTxn(RewardPoint rewardPoint, Double value, Date expiryDate);

    public List<RewardPointTxn> findByTxnTypeAndOrder(EnumRewardPointTxnType enumRewardPointTxnType, Order order);

    public List<RewardPointTxn> findByRewardPoint(RewardPoint rewardPoint);

}
