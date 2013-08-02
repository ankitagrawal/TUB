package com.hk.impl.dao.reward;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.DateUtils;
import com.hk.constants.discount.EnumRewardPointTxnType;
import com.hk.constants.discount.RewardPointConstants;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointTxn;
import com.hk.domain.offer.rewardPoint.RewardPointTxnType;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.reward.RewardPointTxnDao;

@SuppressWarnings("unchecked")
@Repository
public class RewardPointTxnDaoImpl extends BaseDaoImpl implements RewardPointTxnDao {

    @Transactional
    public RewardPointTxn save(RewardPointTxn rewardPointTxn) {
        if (rewardPointTxn != null) {
            if (rewardPointTxn.getTxnDate() == null) {
                rewardPointTxn.setTxnDate(BaseUtils.getCurrentTimestamp());
            }
        }
        return (RewardPointTxn) super.save(rewardPointTxn);
    }

    public RewardPointTxn createRewardPointAddTxn(RewardPoint rewardPoint, Date expiryDate) {
        if (expiryDate == null) {
            expiryDate = new DateTime().plusDays(RewardPointConstants.MAX_ALLOWED_DAYS_TO_REDEEM_REFERRER_POINTS).toDate();
        }
        return save(createRewardPointTxn(rewardPoint, EnumRewardPointTxnType.ADD, rewardPoint.getValue(), expiryDate, null));
    }

    public List<RewardPointTxn> findActiveTxns(User user) {
        Criteria criteria = getSession().createCriteria(RewardPointTxn.class);
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.ge("expiryDate", new DateTime().toDate()));
        return criteria.list();
    }

    public List<RewardPointTxn> findActiveAddTxnsSortedByExpiryDateAsc(User user) {
        Criteria criteria = getSession().createCriteria(RewardPointTxn.class);
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.ge("expiryDate", new DateTime().toDate()));
        criteria.add(Restrictions.eq("rewardPointTxnType.id", EnumRewardPointTxnType.ADD.getId()));
        criteria.addOrder(org.hibernate.criterion.Order.asc("expiryDate"));
        return criteria.list();
    }

    public RewardPointTxn createRewardPointRedeemTxn(RewardPoint rewardPoint, Double unclaimedPoints, Date rewardPointExpiryDate, Order redeemedOrder) {
        if (unclaimedPoints > 0) {
            unclaimedPoints = -unclaimedPoints;
        }
        return save(createRewardPointTxn(rewardPoint, EnumRewardPointTxnType.REDEEM, unclaimedPoints, rewardPointExpiryDate, redeemedOrder));
    }

    public RewardPointTxn createRefundTxn(RewardPointTxn rewardPointTxn,Double percentage) {
        return save(createRewardPointTxn(rewardPointTxn.getRewardPoint(), EnumRewardPointTxnType.REFUND, Math.abs(rewardPointTxn.getValue())*percentage, rewardPointTxn.getExpiryDate(),
                rewardPointTxn.getRedeemedOrder()));
    }

    public RewardPointTxn createRewardPointCancelTxn(RewardPoint rewardPoint, Double value, Date expiryDate) {
        if (value > 0) {
            value = -value;
        }
        return save(createRewardPointTxn(rewardPoint, EnumRewardPointTxnType.REFERRED_ORDER_CANCELLED, value, expiryDate, null));
    }

    private RewardPointTxn createRewardPointTxn(RewardPoint rewardPoint, EnumRewardPointTxnType txnType, Double value, Date expiryDate, Order redeemedOrder) {
        RewardPointTxn rewardPointTxn = new RewardPointTxn();
        rewardPointTxn.setUser(rewardPoint.getUser());
        rewardPointTxn.setRewardPoint(rewardPoint);
        rewardPointTxn.setExpiryDate(DateUtils.getEndOfDay(expiryDate));
        rewardPointTxn.setValue(value);
        rewardPointTxn.setRedeemedOrder(redeemedOrder);
        rewardPointTxn.setRewardPointTxnType((RewardPointTxnType) get(RewardPointTxnType.class, txnType.getId()));
        return rewardPointTxn;
    }

    public List<RewardPointTxn> findByTxnTypeAndOrder(EnumRewardPointTxnType enumRewardPointTxnType, Order order) {
        Criteria criteria = getSession().createCriteria(RewardPointTxn.class);
        criteria.add(Restrictions.eq("redeemedOrder", order));
        criteria.add(Restrictions.eq("rewardPointTxnType.id", enumRewardPointTxnType.getId()));
        return criteria.list();
    }

    public List<RewardPointTxn> findByRewardPoint(RewardPoint rewardPoint) {
        Criteria criteria = getSession().createCriteria(RewardPointTxn.class);
        criteria.add(Restrictions.eq("rewardPoint", rewardPoint));
        return criteria.list();
    }

}
