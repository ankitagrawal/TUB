package com.hk.impl.service.order;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.constants.discount.EnumRewardPointTxnType;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferAction;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.offer.OfferTrigger;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.offer.rewardPoint.RewardPointStatus;
import com.hk.domain.offer.rewardPoint.RewardPointTxn;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.domain.user.UserAccountInfo;
import com.hk.exception.InvalidRewardPointsException;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.reward.RewardPointDao;
import com.hk.pact.dao.reward.RewardPointTxnDao;
import com.hk.pact.dao.user.UserAccountInfoDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.util.OfferTriggerMatcher;

@Service
public class RewardPointServiceImpl implements RewardPointService {

    @Autowired
    private RewardPointDao      rewardPointDao;
    @Autowired
    private OfferInstanceDao    offerInstanceDao;
    @Autowired
    private OrderLoggingService orderLoggingService;
    @Autowired
    private UserService         userService;
    @Autowired
    private RewardPointTxnDao   rewardPointTxnDao;
    @Autowired
    private UserAccountInfoDao  userAccountInfoDao;
    @Autowired
    private EmailManager        emailManager;

    @Override
    public RewardPointMode getRewardPointMode(EnumRewardPointMode enumRewardPointMode) {
        return getRewardPointDao().get(RewardPointMode.class, enumRewardPointMode.getId());
    }

    @Override
    public Double getEligibleRewardPointsForUser(String login) {
        User user = userService.findByLogin(login);
        Double redeemablePoint = getTotalRedeemablePoints(user);

        return (redeemablePoint - user.getUserAccountInfo().getOverusedRewardPoints());
    }

    @Override
    public Double getTotalRedeemablePoints(User user) {
        Double redeemablePoints = 0D;
        List<RewardPointTxn> rewardPointTxnList = getRewardPointTxnDao().findActiveTxns(user);
        for (RewardPointTxn rewardPointTxn : rewardPointTxnList) {
            redeemablePoints += rewardPointTxn.getValue();
        }
        return redeemablePoints;
    }

    @Override
    public RewardPointStatus getRewardPointStatus(EnumRewardPointStatus enumRewardPointStatus) {
        return getRewardPointDao().get(RewardPointStatus.class, enumRewardPointStatus.getId());
    }

    @Override
    public RewardPoint addRewardPoints(User referredBy, User referredUser, Order referredOrder, Double value, String comment, EnumRewardPointStatus rewardPointStatus,
            RewardPointMode rewardPointMode) throws InvalidRewardPointsException {
        return getRewardPointDao().addRewardPoints(referredBy, referredUser, referredOrder, value, comment, rewardPointStatus, rewardPointMode);
    }

    @Override
    public RewardPointTxn createRewardPointTxnForApprovedRewardPoints(RewardPoint rewardPoint, Date expiryDate){
        if(rewardPoint.getRewardPointStatus().getId().equals(EnumRewardPointStatus.APPROVED.getId())){
          return  rewardPointTxnDao.createRewardPointAddTxn(rewardPoint, expiryDate);
        }
        else return null;
    }

    @Override
    // logic for cashback offer
    public void awardRewardPoints(Order order) {
        OfferInstance offerInstance = order.getOfferInstance();
        if (offerInstance != null) {
            Offer offer = order.getOfferInstance().getOffer();
            if (offer != null) {
                OfferAction offerAction = offer.getOfferAction();
                OfferTrigger offerTrigger = offer.getOfferTrigger();
                Double applicableOrderAmount = 0.0D;
                Double cashbackLimit = offerAction.getRewardPointCashbackLimit();
                Double cashbackPercent = offerAction.getRewardPointDiscountPercent();
                ProductGroup productGroup = order.getOfferInstance().getOffer().getOfferAction().getProductGroup();
                if (offerTrigger != null) {
                    Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
                    OfferTriggerMatcher triggerMatcher = offerTrigger.easyMatch(productCartLineItems);
                    if (triggerMatcher.hasEasyMatch(offer.isExcludeTriggerProducts())) {
                        for (CartLineItem lineItem : productCartLineItems) {
                            if (productGroup == null || productGroup.contains(lineItem.getProductVariant())) {
                                applicableOrderAmount += lineItem.getHkPrice() * lineItem.getQty();
                            }
                        }
                    }
                }
                Double applicableDiscount = applicableOrderAmount * (cashbackPercent != null ? cashbackPercent : 0.0);
                if (findByReferredOrderAndRewardMode(order, getRewardPointMode(EnumRewardPointMode.HK_CASHBACK)) == null) {
                    if (Boolean.TRUE.equals(offerInstance.getOffer().getOfferAction().isCashback()) && applicableDiscount > 0.00) {
                        if (EnumPaymentMode.getPrePaidPaymentModes().contains(order.getPayment().getPaymentMode().getId())) {
                            RewardPoint techProcessRewardPoint = getRewardPointDao().addRewardPoints(order.getUser(), null, order,
                                    cashbackLimit != null ? applicableDiscount < cashbackLimit ? applicableDiscount : cashbackLimit : applicableDiscount, "Cashback Offer",
                                    EnumRewardPointStatus.APPROVED, getRewardPointMode(EnumRewardPointMode.HK_CASHBACK));
	                        if (offerAction.getRewardPointExpiryDate() != null) {
		                        approveRewardPoints(Arrays.asList(techProcessRewardPoint), offerAction.getRewardPointExpiryDate());
	                        } else if (offerAction.getRewardPointRedeemWithinDays() != null) {
		                        approveRewardPoints(Arrays.asList(techProcessRewardPoint), new DateTime().plusDays(offerAction.getRewardPointRedeemWithinDays()).toDate());
	                        } else {
		                        approveRewardPoints(Arrays.asList(techProcessRewardPoint), new DateTime().plusMonths(1).toDate());
	                        }
                        } else {
                            RewardPoint notTechProcessRewardPoint = getRewardPointDao().addRewardPoints(order.getUser(), null, order,
                                    cashbackLimit != null ? applicableDiscount < cashbackLimit ? applicableDiscount : cashbackLimit : applicableDiscount, "Cashback Offer",
                                    EnumRewardPointStatus.PENDING, getRewardPointMode(EnumRewardPointMode.HK_CASHBACK));
	                        if (offerAction.getRewardPointExpiryDate() != null) {
		                        approveRewardPoints(Arrays.asList(notTechProcessRewardPoint), offerAction.getRewardPointExpiryDate());
	                        } else if (offerAction.getRewardPointRedeemWithinDays() != null) {
		                        approveRewardPoints(Arrays.asList(notTechProcessRewardPoint), new DateTime().plusDays(offerAction.getRewardPointRedeemWithinDays()).toDate());
	                        } else {
		                        approveRewardPoints(Arrays.asList(notTechProcessRewardPoint), new DateTime().plusMonths(1).toDate());
	                        }
                        }
                    }
                }
                order.getOfferInstance().setActive(false);
                getOfferInstanceDao().save(order.getOfferInstance());
            }
        }
    }

    @Override
    public RewardPoint findByReferredOrderAndRewardMode(Order order, RewardPointMode rewardPointMode) {
        return getRewardPointDao().findByReferredOrderAndRewardMode(order, rewardPointMode);
    }

    @Override
    public void approvePendingRewardPointsForOrder(Order order) {
        List<RewardPoint> rewardPointList = getRewardPointDao().findByReferredOrder(order);
        RewardPointStatus rewardPointPendingStatus = getRewardPointStatus(EnumRewardPointStatus.PENDING);
        if (rewardPointList != null) {
            for (RewardPoint rewardPoint : rewardPointList) {
                if (rewardPoint.getRewardPointStatus().equals(rewardPointPendingStatus)) {
                    rewardPoint.setRewardPointStatus(getRewardPointStatus(EnumRewardPointStatus.APPROVED));
                    approveRewardPoints(Arrays.asList(rewardPoint), new DateTime().plusMonths(1).toDate());
                    getOrderLoggingService().logOrderActivity(order, EnumOrderLifecycleActivity.RewardPointsApproved);
                }
            }
        }
    }

    /**
     * Expiry date is nullable. In case it is null, RewardPointConstants.MAX_ALLOWED_DAYS_TO_REDEEM_REFERRER_POINTS will
     * be applied automatically.
     * 
     * @param rewardPointList
     * @param expiryDate
     */
    @Override
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

    @Override
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

    
    @Transactional
    private void cancelRewardPoints(User user, Double cancelRewardPoints) {
        redeemOrCancelRedeemableRewardPoints(EnumRewardPointTxnType.REFERRED_ORDER_CANCELLED, user, cancelRewardPoints, null);
    }

    
    @Override
    @Transactional
    public void redeemRewardPoints(Order order, Double rewardPointsUsed) {
        redeemOrCancelRedeemableRewardPoints(EnumRewardPointTxnType.REDEEM, order.getUser(), rewardPointsUsed, order);
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

    public List<RewardPoint> findByReferredOrder(Order order) {
        return getRewardPointDao().findByReferredOrder(order);
    }

    public RewardPointDao getRewardPointDao() {
        return rewardPointDao;
    }

    public void setRewardPointDao(RewardPointDao rewardPointDao) {
        this.rewardPointDao = rewardPointDao;
    }

    public OfferInstanceDao getOfferInstanceDao() {
        return offerInstanceDao;
    }

    public void setOfferInstanceDao(OfferInstanceDao offerInstanceDao) {
        this.offerInstanceDao = offerInstanceDao;
    }

    public OrderLoggingService getOrderLoggingService() {
        return orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }

    public RewardPointTxnDao getRewardPointTxnDao() {
        return rewardPointTxnDao;
    }

    public UserAccountInfoDao getUserAccountInfoDao() {
        return userAccountInfoDao;
    }

}
