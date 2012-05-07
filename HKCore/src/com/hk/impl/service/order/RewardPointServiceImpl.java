package com.hk.impl.service.order;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.discount.EnumRewardPointMode;
import com.hk.constants.discount.EnumRewardPointStatus;
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
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.exception.InvalidRewardPointsException;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.reward.RewardPointDao;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.util.OfferTriggerMatcher;

@Service
public class RewardPointServiceImpl implements RewardPointService {

    @Autowired
    private PaymentService         paymentService;
    @Autowired
    private RewardPointDao         rewardPointDao;
    @Autowired
    private ReferrerProgramManager referrerProgramManager;
    @Autowired
    private OfferInstanceDao       offerInstanceDao;
    @Autowired
    private OrderService           orderService;

    public RewardPointMode getRewardPointMode(EnumRewardPointMode enumRewardPointMode) {
        return getRewardPointDao().get(RewardPointMode.class, enumRewardPointMode.getId());
    }

    public RewardPointStatus getRewardPointStatus(EnumRewardPointStatus enumRewardPointStatus) {
        return getRewardPointDao().get(RewardPointStatus.class, enumRewardPointStatus.getId());
    }

    public RewardPoint addRewardPoints(User referredBy, User referredUser, Order referredOrder, Double value, String comment, EnumRewardPointStatus rewardPointStatus,
            RewardPointMode rewardPointMode) throws InvalidRewardPointsException {
        return getRewardPointDao().addRewardPoints(referredBy, referredUser, referredOrder, value, comment, rewardPointStatus, rewardPointMode);
    }

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
                        if (order.getPayment().getPaymentMode().equals(getPaymentService().findPaymentMode(EnumPaymentMode.TECHPROCESS))
                                || order.getPayment().getPaymentMode().equals(getPaymentService().findPaymentMode(EnumPaymentMode.CITRUS))) {
                            RewardPoint techProcessRewardPoint = getRewardPointDao().addRewardPoints(order.getUser(), null, order,
                                    cashbackLimit != null ? applicableDiscount < cashbackLimit ? applicableDiscount : cashbackLimit : applicableDiscount, "Cashback Offer",
                                    EnumRewardPointStatus.APPROVED, getRewardPointMode(EnumRewardPointMode.HK_CASHBACK));
                            referrerProgramManager.approveRewardPoints(Arrays.asList(techProcessRewardPoint), new DateTime().plusMonths(1).toDate());
                        } else {
                            RewardPoint notTechProcessRewardPoint = getRewardPointDao().addRewardPoints(order.getUser(), null, order,
                                    cashbackLimit != null ? applicableDiscount < cashbackLimit ? applicableDiscount : cashbackLimit : applicableDiscount, "Cashback Offer",
                                    EnumRewardPointStatus.PENDING, getRewardPointMode(EnumRewardPointMode.HK_CASHBACK));
                            referrerProgramManager.approveRewardPoints(Arrays.asList(notTechProcessRewardPoint), new DateTime().plusMonths(1).toDate());
                        }
                    }
                }
                order.getOfferInstance().setActive(false);
                getOfferInstanceDao().save(order.getOfferInstance());
            }
        }
    }
    
    public RewardPoint findByReferredOrderAndRewardMode(Order order, RewardPointMode rewardPointMode){
        return getRewardPointDao().findByReferredOrderAndRewardMode(order, rewardPointMode);
    }

    public void approvePendingRewardPointsForOrder(Order order) {
        List<RewardPoint> rewardPointList = getRewardPointDao().findByReferredOrder(order);
        RewardPointStatus rewardPointPendingStatus = getRewardPointStatus(EnumRewardPointStatus.PENDING);
        if (rewardPointList != null) {
            for (RewardPoint rewardPoint : rewardPointList) {
                if (rewardPoint.getRewardPointStatus().equals(rewardPointPendingStatus)) {
                    rewardPoint.setRewardPointStatus(getRewardPointStatus(EnumRewardPointStatus.APPROVED));
                    referrerProgramManager.approveRewardPoints(Arrays.asList(rewardPoint), new DateTime().plusMonths(1).toDate());
                    getOrderService().logOrderActivity(order, EnumOrderLifecycleActivity.RewardPointsApproved);
                }
            }
        }
    }

    public List<RewardPoint> findByReferredOrder(Order order) {
        return getRewardPointDao().findByReferredOrder(order);
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public RewardPointDao getRewardPointDao() {
        return rewardPointDao;
    }

    public void setRewardPointDao(RewardPointDao rewardPointDao) {
        this.rewardPointDao = rewardPointDao;
    }

    public ReferrerProgramManager getReferrerProgramManager() {
        return referrerProgramManager;
    }

    public void setReferrerProgramManager(ReferrerProgramManager referrerProgramManager) {
        this.referrerProgramManager = referrerProgramManager;
    }

    public OfferInstanceDao getOfferInstanceDao() {
        return offerInstanceDao;
    }

    public void setOfferInstanceDao(OfferInstanceDao offerInstanceDao) {
        this.offerInstanceDao = offerInstanceDao;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

}
