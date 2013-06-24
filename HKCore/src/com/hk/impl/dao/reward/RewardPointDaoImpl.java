package com.hk.impl.dao.reward;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.discount.RewardPointConstants;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.offer.rewardPoint.RewardPointStatus;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.InvalidRewardPointsException;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.reward.RewardPointDao;

@SuppressWarnings("unchecked")
@Repository
public class RewardPointDaoImpl extends BaseDaoImpl implements RewardPointDao {
    private static Logger      logger            = LoggerFactory.getLogger(RewardPointDao.class);

    @Transactional
    public RewardPoint save(RewardPoint rewardPoint) {
        if (rewardPoint != null) {
            if (rewardPoint.getCreateDate() == null) {
                rewardPoint.setCreateDate(BaseUtils.getCurrentTimestamp());
            }
        }
        return (RewardPoint) super.save(rewardPoint);
    }

    public RewardPoint creditPointsToReferrer(User referredBy, User referredUser, Order referredOrder) {
        RewardPoint rewardPoint = findByReferredUser(referredUser);
        if (rewardPoint == null) {
            return addRewardPoints(referredBy, referredUser, referredOrder, RewardPointConstants.referrerRewardPoints, null, EnumRewardPointStatus.PENDING, get(
                    RewardPointMode.class, EnumRewardPointMode.REFERRAL.getId()));
        }
        logger.info("reward point already credited due to referred user " + referredUser.getLogin() + " to user " + rewardPoint.getUser().getLogin());
        return null;
    }

    @Transactional
    public RewardPoint addRewardPoints(User referredBy, User referredUser, Order referredOrder, Double value, String comment, EnumRewardPointStatus rewardPointStatus,
            RewardPointMode rewardPointMode) throws InvalidRewardPointsException {

        if (value >= RewardPointConstants.MAX_REWARD_POINTS) {
            throw new InvalidRewardPointsException(value);
        }
        RewardPoint rewardPoint = new RewardPoint();
        rewardPoint.setReferredUser(referredUser);
        rewardPoint.setUser(referredBy);
        rewardPoint.setRewardPointStatus(get(RewardPointStatus.class, rewardPointStatus.getId()));
        rewardPoint.setRewardPointMode(rewardPointMode);
        rewardPoint.setValue(value);
        rewardPoint.setReferredOrder(referredOrder);
        rewardPoint.setComment(comment);
        return save(rewardPoint);
    }

    public List<RewardPoint> findByStatus(List<Long> statusIds) {
        Criteria criteria = getSession().createCriteria(RewardPoint.class);
        criteria.add(Restrictions.in("rewardPointStatus.id", statusIds));
        return criteria.list();
    }

    public Page searchRewardPoints(List<RewardPointMode> modes, List<RewardPointStatus> status, int pageNo, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(RewardPoint.class);
        criteria.add(Restrictions.in("rewardPointStatus", status));
        if (modes != null && !modes.isEmpty()) {
            criteria.add(Restrictions.in("rewardPointMode", modes));
        }

        return list(criteria, pageNo, perPage);
    }

    public List<RewardPoint> findByReferredOrder(Order order) {
        Criteria criteria = getSession().createCriteria(RewardPoint.class);
        criteria.add(Restrictions.eq("referredOrder", order));
        return criteria.list();
    }

    public RewardPoint findByReferredOrderAndRewardMode(Order order, RewardPointMode rewardPointMode) {
        Criteria criteria = getSession().createCriteria(RewardPoint.class);
        criteria.add(Restrictions.eq("referredOrder", order));
        criteria.add(Restrictions.eq("rewardPointMode", rewardPointMode));
        return (RewardPoint) criteria.uniqueResult();
    }

    public RewardPoint cancelRewardPoint(RewardPoint rewardPoint) {
        rewardPoint.setRewardPointStatus(get(RewardPointStatus.class, EnumRewardPointStatus.CANCELLED.getId()));
        return save(rewardPoint);
    }

    public RewardPoint findByReferredUser(User referredUser) {
        Criteria criteria = getSession().createCriteria(RewardPoint.class);
        criteria.add(Restrictions.eq("referredUser", referredUser));
        return (RewardPoint) criteria.uniqueResult();
    }

    public List<RewardPoint> findByUser(User user) {
        Criteria criteria = getSession().createCriteria(RewardPoint.class);
        criteria.add(Restrictions.eq("user", user));
        return criteria.list();
    }
}
