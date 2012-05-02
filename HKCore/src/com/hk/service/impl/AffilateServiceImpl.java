package com.hk.service.impl;

import javax.servlet.http.Cookie;

import net.sourceforge.stripes.util.CryptoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.EnumAffiliateTxnType;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.discount.OfferConstants;
import com.hk.dao.affiliate.AffiliateDao;
import com.hk.dao.affiliate.AffiliateTxnDao;
import com.hk.dao.offer.OfferInstanceDao;
import com.hk.dao.reward.RewardPointDao;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateTxn;
import com.hk.domain.affiliate.AffiliateTxnType;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.AffiliateManager;
import com.hk.service.AffilateService;
import com.hk.service.UserService;
import com.hk.web.filter.WebContext;

@Service
public class AffilateServiceImpl implements AffilateService {
    private static final String TEXT_HTML = "text/html";

    
    @Autowired
    private UserService         userService;
    @Autowired
    private AffiliateDao        affiliateDao;
    @Autowired
    private AffiliateManager    affiliateManager;
    @Autowired
    private OfferInstanceDao    offerInstanceDao;
    @Autowired
    private RewardPointDao      rewardPointDao;
    @Autowired
    private AffiliateTxnDao     affiliateTxnDao;

    @Transactional
    public void saveOfferInstanceAndSaveAffiliateCommission(Order order, PricingDto pricingDto) {

        User user = order.getUser();
        OfferInstance offerInstance = order.getOfferInstance();

        // give affiliate commission if order came from an affilate
        boolean hasGotAffiliateCommission = applyAffiliateCommission(order);

        if (offerInstance != null) {
            // after applying offer explicitly save the offerInstance because PricingEngine will not do that
            offerInstance = getOfferInstanceDao().save(offerInstance);
            // check if this order is using referral coupon
            if (offerInstance.getCoupon() != null && offerInstance.getCoupon().getReferrerUser() != null) {
                // credit points to the referredBy, if user availing discount as well
                if (user.getReferredBy() != null && pricingDto.getTotalDiscount() > 0) {
                    rewardPointDao.creditPointsToReferrer(user.getReferredBy(), user, order);
                    order.setReferredOrder(true);
                }
                // check if the coupon is an affiliate coupon && user is affiliatedTo is set, Hence give first
                // transacting commission
                else if (offerInstance.getOffer().getOfferIdentifier().equals(OfferConstants.affiliateCommissionOffer) && user.getAffiliateTo() != null
                        && affiliateDao.getAffilateByUser(user.getAffiliateTo()) != null) {
                    affiliateManager.addAmountInAccountforFirstTransaction(user.getAffiliateTo(), order);
                    order.setReferredOrder(true);
                }
            }
        }
        // else check if user is already affiliated, then pay him latter commission
        else if (user.getAffiliateTo() != null && affiliateDao.getAffilateByUser(user.getAffiliateTo()) != null && !hasGotAffiliateCommission) {
            affiliateManager.addAmountInAccountforLatterTransaction(user.getAffiliateTo(), order);
            order.setReferredOrder(true);
        }
    }

    @Transactional
    private boolean applyAffiliateCommission(Order order) {
        // find if the user is referred by online affiliate or not, if yes pay him as per first commission, if first
        // time user and set his affiliate_to corresponding affiliate,
        // if affiliate_to already set, pay latter commision assuming that affiliate_to is same to affiliate cookie
        // but if different, pay the new affiliate first time commision for a transacting user, and none to the old
        // affiliate

        boolean hasGotAffiliateCommission = false;
        User user = order.getUser();
        Cookie affiliateCookie = BaseUtils.getCookie(WebContext.getRequest(), HealthkartConstants.Cookie.affiliate_id);
        if (affiliateCookie != null) {
            String affiliateID = CryptoUtil.decrypt(affiliateCookie.getValue());
            Affiliate affiliate = affiliateDao.getAffiliateByCode(affiliateID);
            if (affiliate != null) {
                if (user.getAffiliateTo() == null) {
                    user.setAffiliateTo(affiliate.getUser());
                    getUserService().save(user);
                    getAffiliateManager().addAmountInAccountforFirstTransaction(affiliate.getUser(), order);
                } else if (user.getAffiliateTo().equals(affiliate.getUser())) {
                    getAffiliateManager().addAmountInAccountforLatterTransaction(affiliate.getUser(), order);
                } else {
                    getAffiliateManager().addAmountInAccountforFirstTransaction(affiliate.getUser(), order);
                }
                order.setReferredOrder(true);
                hasGotAffiliateCommission = true;
                Cookie expireCookie = new Cookie(HealthkartConstants.Cookie.affiliate_id, CryptoUtil.encrypt(affiliateID));
                expireCookie.setPath("/");
                expireCookie.setMaxAge(0);
                WebContext.getResponse().addCookie(expireCookie);
                WebContext.getResponse().setContentType(TEXT_HTML);
            }
        }

        return hasGotAffiliateCommission;
    }
    
    @Override
    public AffiliateTxnType getAffiliateTxnType(Long txnId) {
        return getAffiliateDao().get(AffiliateTxnType.class, txnId);
    }

    public void cancelTxn(Order order) {
        AffiliateTxn affiliateTxn = getAffiliateTxnDao().getTxnByOrder(order);
        if (affiliateTxn != null) {
            affiliateTxn.setAffiliateTxnType(getAffiliateDao().get(AffiliateTxnType.class, EnumAffiliateTxnType.ORDER_CANCELLED.getId()));
            getAffiliateTxnDao().save(affiliateTxn);
        }
    }
    
    @Override
    public Affiliate getAffilateByUser(User affiliateUser) {
        return getAffiliateDao().getAffilateByUser(affiliateUser);
    }

    @Override
    public Affiliate getAffiliateByUserId(Long userId) {
        return getAffiliateDao().getAffiliateByUserId(userId);
    }

    @Override
    public Affiliate save(Affiliate affiliate) {
        return (Affiliate) getAffiliateDao().save(affiliate);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public AffiliateDao getAffiliateDao() {
        return affiliateDao;
    }

    public void setAffiliateDao(AffiliateDao affiliateDao) {
        this.affiliateDao = affiliateDao;
    }

    public AffiliateManager getAffiliateManager() {
        return affiliateManager;
    }

    public void setAffiliateManager(AffiliateManager affiliateManager) {
        this.affiliateManager = affiliateManager;
    }

    public OfferInstanceDao getOfferInstanceDao() {
        return offerInstanceDao;
    }

    public void setOfferInstanceDao(OfferInstanceDao offerInstanceDao) {
        this.offerInstanceDao = offerInstanceDao;
    }

    public RewardPointDao getRewardPointDao() {
        return rewardPointDao;
    }

    public void setRewardPointDao(RewardPointDao rewardPointDao) {
        this.rewardPointDao = rewardPointDao;
    }

    public AffiliateTxnDao getAffiliateTxnDao() {
        return affiliateTxnDao;
    }

    public void setAffiliateTxnDao(AffiliateTxnDao affiliateTxnDao) {
        this.affiliateTxnDao = affiliateTxnDao;
    }

    

}
