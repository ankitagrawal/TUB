package com.hk.manager;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;

import java.util.Date;
import java.util.List;

import com.hk.constants.coupon.EnumCouponType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.discount.EnumRewardPointTxnType;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointTxn;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.domain.user.UserAccountInfo;
import com.hk.pact.dao.reward.RewardPointDao;
import com.hk.pact.dao.reward.RewardPointTxnDao;
import com.hk.pact.dao.user.UserAccountInfoDao;
import com.hk.pact.service.discount.CouponService;

/**
 * User: rahul Time: 22 Apr, 2010 3:46:20 PM <p/> A User can earn Reward Points in 2 ways 1.) Adding reward points
 * directly to the user's account through admin. 2.) Reward Points by referring others users through a special coupon
 * code containing a referrer user id and when referred user will order something then reward points will be added to
 * the user's account. <p/> <p/> Reward points added by 2nd method are subject to admin approval. So whenever reward
 * points are added to the user's account, they are in pending state. Admin has to approve these points manually. On
 * approval a reward point txn will be created of the type add. Reward point has an expiry date associated with it.
 * After that date user can not redeem them i.e reward point will be expired. <p/> At any given time total redeemable
 * points will be the balance of all the active i.e not expired reward points txns Example: Value Expiry-Date
 * Reward-Point-Type +50 6-May-2010 Add +50 7-May-2010 Add -25 6-May-2010 Redeem <p/> so on 5-May-2010 total redeemable
 * points will be +75 and on 7-May-2010 total redeemable points will be 50 and on 8-May-2010 total redeemable points
 * will be 0 <p/> Reward points can be directly used to pay for the order i.e if the order total is Rs 500 and user has
 * 100 reward points then if he uses his reward points and the total billable amount will be Rs 400 To keep track of
 * reward points used in an order we have a field in order named "rewardPointsUsed". And also an invoiceLine will be
 * created of the type RewardPoint whenever an order uses reward points to checkout. <p/> <p/>
 * ----------------------------------------------------------------------- Redeem Reward Points Logic Whenever user
 * redeem his reward points a reward point txn of the type redeem will be created. To redeem the points we will find all
 * the active txns group by reward point and sorted by expiry date asc. i.e reward points having expiry date earlier
 * will be redeemed first. <p/> ----------------------------------------------------------------------- We can not
 * redeem more than added value from a reward point Example: If reward points with value +50 and +100 are added, then it
 * does not mean that we can reddem more than 50 points from the reward point whose value is 50. In short, an individual
 * reward point balance can not be less than zero <p/> <p/>
 * ----------------------------------------------------------------------- <p/> Cancelling the order where reward points
 * were used: Since we are keeping track of the redeemed reward points i.e in which order they are redeemed so if an
 * order is cancelled where part/total amount was paid by reward points then on cancellation of the order we will refund
 * the redeemed reward points. <p/> ----------------------------------------------------------------------- <p/>
 * Cancellation of the referred order: Referred order is the order due to which reward points will be added to the
 * user'd account. So on cancellation of such an order. Reward points credited will also be cancelled. In normal cases
 * when user has not used those reward points which were added due to this cancelled order then a reward point txn of
 * the type cancel with the same value as the added will be created. But if user has somehow used these reward points
 * then this used points/amount will be taken by cancelling other rewward points of that user. In case, to be cancelled
 * points are more than the total reddemable points then these overusedRewardPoints will be stored in user account info
 * and will be charged whenever new reward points will be added to the user's account. So at any given time balance of
 * the reward point in reward point txn table can not be less than zero. And if any overused reward points are there,
 * they will be stored seperately in user account info and taken into account whenever a new reward point will be added
 * to the user's account <p/> Example: Value Expiry-Date Reward-Point-Type Reward-Point-Id +50 6-May-2010 Add 1 +50
 * 7-May-2010 Add 2 -50 6-May-2010 Redeem 1 -26 7-May-2010 Redeem 2 <p/> Now suppose the reward point with id 1 is
 * cancelled so we have to take these reward points back but user has already used them. Now to take these points back
 * we will cancel other reward points. Since 24 points in reward-point-id 2 is un-used so will deduct those 24 points
 * from reward point 2 as cancelled points and rest amount i.e 26 will be stored in user account info <p/> Value
 * Expiry-Date Reward-Point-Type Reward-Point-Id +50 6-May-2010 Add 1 +50 7-May-2010 Add 2 -50 6-May-2010 Redeem 1 -26
 * 7-May-2010 Redeem 2 -24 7-May-2010 Cancel 2 <p/> And overusedRewardPoint = 26, redeemablePoints = 0 <p/> Now suppose
 * that in future user gets some more reward points then we can deduct the overused reward points from these newly added
 * reward points <p/> Value Expiry-Date Reward-Point-Type Reward-Point-Id +50 6-May-2010 Add 1 +50 7-May-2010 Add 2 -50
 * 6-May-2010 Redeem 1 -26 7-May-2010 Redeem 2 -24 7-May-2010 Cancel 2 +50 15-May-2010 Add 3 -26 15-May-2010 Cancel 3
 * <p/> And overusedRewardPoints = 0, redeemablePoints = 24
 */

@Component
public class ReferrerProgramManager {

    @Autowired
    private OfferManager       offerManager;
    @Autowired
    private CouponService      couponService;
    @Autowired
    private RewardPointDao     rewardPointDao;
    @Autowired
    private RewardPointTxnDao  rewardPointTxnDao;
    @Autowired
    private UserAccountInfoDao userAccountInfoDao;
    @Autowired
    private EmailManager       emailManager;

    public Coupon getOrCreateRefferrerCoupon(User user) {
        Coupon referrerCoupon = user.getReferrerCoupon();
        if (referrerCoupon == null) {
            Offer offer = offerManager.getOfferForReferralAndAffiliateProgram();
            String code = getCouponCode(user);
            while (getCouponService().findByCode(code) != null) {
                code = getCouponCode(user);
            }
            referrerCoupon = getCouponService().createCoupon(code, null, 10L, null, offer, user, false, EnumCouponType.REFERRAL.asCouponType());
        }
        return referrerCoupon;
    }

    private String getCouponCode(User user) {
        String name = user.getName().replace(" ", "");
        int nameLength = name.length();
        return nameLength > 10 ? name.substring(0, 10) + BaseUtils.getRandomStringTypable(5) : name + BaseUtils.getRandomStringTypable(15 - nameLength);
    }

    /**
     * Expiry date is nullable. In case it is null, RewardPointConstants.MAX_ALLOWED_DAYS_TO_REDEEM_REFERRER_POINTS will
     * be applied automatically.
     * 
     * @param rewardPointList
     * @param expiryDate
     */
    @Transactional
    public void approveRewardPoints(List<RewardPoint> rewardPointList, Date expiryDate) {
        for (RewardPoint rewardPoint : rewardPointList) {
            rewardPoint = getRewardPointDao().save(rewardPoint);

            if (rewardPoint.getRewardPointStatus().getId().equals(EnumRewardPointStatus.APPROVED.getId())) {
                RewardPointTxn addTxn = getRewardPointTxnDao().createRewardPointAddTxn(rewardPoint, expiryDate);
                UserAccountInfo userAccountInfo = getUserAccountInfoDao().getOrCreateUserAccountInfo(rewardPoint.getUser());
                if (userAccountInfo.getOverusedRewardPoints() > 0) {
                    Double cancelRewardPoints = 0D;
                    Double overusedRewardPoints = 0D;
                    if (addTxn.getValue() > userAccountInfo.getOverusedRewardPoints()) {
                        cancelRewardPoints = userAccountInfo.getOverusedRewardPoints();
                    } else {
                        cancelRewardPoints = addTxn.getValue();
                        overusedRewardPoints = userAccountInfo.getOverusedRewardPoints() - addTxn.getValue();
                    }
                    cancelRewardPoints(rewardPoint.getUser(), cancelRewardPoints);
                    userAccountInfo.setOverusedRewardPoints(overusedRewardPoints);
                    getUserAccountInfoDao().save(userAccountInfo);
                } else {
                    // send a reward point credit email to user
                    if (rewardPoint.getRewardPointMode() != null && rewardPoint.getValue() != null && rewardPoint.getValue() > 10.0) {
                        if (rewardPoint.getReferredUser() != null && rewardPoint.getRewardPointMode().getId().equals(EnumRewardPointMode.REFERRAL.getId())) {
                            emailManager.sendReferralRewardPointEmail(rewardPoint, addTxn);
                        } else if (rewardPoint.getRewardPointMode().getId().equals(EnumRewardPointMode.HK_CASHBACK.getId())) {
                            emailManager.sendCashBackRewardPointEmail(rewardPoint, addTxn);
                        } else if (rewardPoint.getRewardPointMode().getId().equals(EnumRewardPointMode.FB_SHARING.getId())) {
                            emailManager.sendFBShareRewardPointEmail(rewardPoint, addTxn);
                        }
                    }
                }
            }

        }
    }

    @Transactional
    public void redeemRewardPoints(Order order, Double rewardPointsUsed) {
        redeemOrCancelRedeemableRewardPoints(EnumRewardPointTxnType.REDEEM, order.getUser(), rewardPointsUsed, order);
    }

    @Transactional
    public void cancelRewardPoints(User user, Double cancelRewardPoints) {
        redeemOrCancelRedeemableRewardPoints(EnumRewardPointTxnType.REFERRED_ORDER_CANCELLED, user, cancelRewardPoints, null);
    }

    private void redeemOrCancelRedeemableRewardPoints(EnumRewardPointTxnType action, User user, Double rewardPointsUsed, Order order) {
        Double redeemablePoints = getTotalRedeemablePoints(user);
        if (rewardPointsUsed < redeemablePoints) {
            redeemablePoints = rewardPointsUsed;
        }
        List<RewardPointTxn> rewardPointTxnList = getRewardPointTxnDao().findActiveTxns(user);
        Multimap<RewardPoint, RewardPointTxn> txnsGroupByRewardPoint = ArrayListMultimap.create();
        for (RewardPointTxn rewardPointTxn : rewardPointTxnList) {
            txnsGroupByRewardPoint.put(rewardPointTxn.getRewardPoint(), rewardPointTxn);
        }

        TreeMultimap<RewardPoint, RewardPointTxn> txnsGroupByRewardPointAndSorted = TreeMultimap.create(txnsGroupByRewardPoint);

        for (RewardPoint rewardPoint : txnsGroupByRewardPointAndSorted.keySet()) {
            Double unclaimedPoints = sum(txnsGroupByRewardPointAndSorted.get(rewardPoint), on(RewardPointTxn.class).getValue());
            if (unclaimedPoints > 0) {
                Date rewardPointExpiryDate = txnsGroupByRewardPointAndSorted.get(rewardPoint).first().getExpiryDate();
                if (redeemablePoints > unclaimedPoints) {
                    redeemablePoints -= unclaimedPoints;
                    switch (action) {
                        case REDEEM:
                            getRewardPointTxnDao().createRewardPointRedeemTxn(rewardPoint, unclaimedPoints, rewardPointExpiryDate, order);
                            break;
                        case REFERRED_ORDER_CANCELLED:
                            getRewardPointTxnDao().createRewardPointCancelTxn(rewardPoint, unclaimedPoints, rewardPointExpiryDate);
                            break;
                        default:
                            throw new RuntimeException("Invalid case");
                    }
                } else {
                    switch (action) {
                        case REDEEM:
                            getRewardPointTxnDao().createRewardPointRedeemTxn(rewardPoint, redeemablePoints, rewardPointExpiryDate, order);
                            break;
                        case REFERRED_ORDER_CANCELLED:
                            getRewardPointTxnDao().createRewardPointCancelTxn(rewardPoint, redeemablePoints, rewardPointExpiryDate);
                            break;
                        default:
                            throw new RuntimeException("Invalid case");
                    }
                    redeemablePoints = 0D;
                    break;
                }
            } else {
                // do nothing
            }
        }
    }

    public Double getTotalRedeemablePoints(User user) {
        Double redeemablePoints = 0D;
        List<RewardPointTxn> rewardPointTxnList = getRewardPointTxnDao().findActiveTxns(user);
        for (RewardPointTxn rewardPointTxn : rewardPointTxnList) {
            redeemablePoints += rewardPointTxn.getValue();
        }
        return redeemablePoints;
    }

    public void refundRedeemedPoints(Order order) {
        List<RewardPointTxn> rewardPointTxnList = getRewardPointTxnDao().findByTxnTypeAndOrder(EnumRewardPointTxnType.REDEEM, order);
        for (RewardPointTxn rewardPointTxn : rewardPointTxnList) {
            getRewardPointTxnDao().createRefundTxn(rewardPointTxn);
        }
    }

    @Transactional
    public void cancelReferredOrderRewardPoint(RewardPoint rewardPoint) {

        // if reward point status is not approved then there will not be any reward point txn
        // so no need to add any cancel txn

        if (rewardPoint.getRewardPointStatus().getId().equals(EnumRewardPointStatus.APPROVED.getId())) {
            List<RewardPointTxn> rewardPointTxnList = getRewardPointTxnDao().findByRewardPoint(rewardPoint);

            Double addedRewardPoints = 0D;
            Double rewardPointBalance = 0D;
            for (RewardPointTxn rewardPointTxn : rewardPointTxnList) {
                rewardPointBalance += rewardPointTxn.getValue();
                if (rewardPointTxn.isType(EnumRewardPointTxnType.ADD)) {
                    addedRewardPoints = rewardPointTxn.getValue();
                }
            }

            if (addedRewardPoints > 0 && addedRewardPoints.equals(rewardPointBalance)) {
                // case when none of the reward points are used from the reward points added by the cancelled referred
                // order.
                getRewardPointTxnDao().createRewardPointCancelTxn(rewardPoint, addedRewardPoints, rewardPointTxnList.get(0).getExpiryDate());
            } else {

                if (rewardPointBalance > 0) {
                    getRewardPointTxnDao().createRewardPointCancelTxn(rewardPoint, rewardPointBalance, rewardPointTxnList.get(0).getExpiryDate());
                }

                Double overusedRewardPoints = addedRewardPoints - rewardPointBalance;
                Double redeemablePoints = getTotalRedeemablePoints(rewardPoint.getUser());
                if (redeemablePoints >= overusedRewardPoints) {
                    // cacel other reward points in lieu of reward points used of cancelled referred order
                    cancelRewardPoints(rewardPoint.getUser(), overusedRewardPoints);
                } else {
                    if (redeemablePoints > 0) {
                        // cacel other reward points in lieu of reward points used of cancelled referred order
                        cancelRewardPoints(rewardPoint.getUser(), redeemablePoints);
                        overusedRewardPoints -= redeemablePoints;
                    }
                    // store this overused_reward_point info to the user account info
                    UserAccountInfo userAccountInfo = getUserAccountInfoDao().getOrCreateUserAccountInfo(rewardPoint.getUser());
                    userAccountInfo.setOverusedRewardPoints(userAccountInfo.getOverusedRewardPoints() + overusedRewardPoints);
                    getUserAccountInfoDao().save(userAccountInfo);
                }
            }
        }

        getRewardPointDao().cancelRewardPoint(rewardPoint);

    }

    public CouponService getCouponService() {
        return couponService;
    }

    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }

    public RewardPointDao getRewardPointDao() {
        return rewardPointDao;
    }

    public void setRewardPointDao(RewardPointDao rewardPointDao) {
        this.rewardPointDao = rewardPointDao;
    }

    public RewardPointTxnDao getRewardPointTxnDao() {
        return rewardPointTxnDao;
    }

    public void setRewardPointTxnDao(RewardPointTxnDao rewardPointTxnDao) {
        this.rewardPointTxnDao = rewardPointTxnDao;
    }

    public UserAccountInfoDao getUserAccountInfoDao() {
        return userAccountInfoDao;
    }

    public void setUserAccountInfoDao(UserAccountInfoDao userAccountInfoDao) {
        this.userAccountInfoDao = userAccountInfoDao;
    }

}
