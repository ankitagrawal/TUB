package com.hk.impl.service.core;

import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;

import net.sourceforge.stripes.util.CryptoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.affiliate.EnumAffiliateTxnType;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.coupon.EnumCouponType;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.domain.affiliate.AffiliateTxn;
import com.hk.domain.affiliate.AffiliateTxnType;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.pact.dao.affiliate.AffiliateCategoryDao;
import com.hk.pact.dao.affiliate.AffiliateCategoryHasBrandDao;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.affiliate.AffiliateTxnDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.reward.RewardPointDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AffilateService;
import com.hk.web.filter.WebContext;

@Service
public class AffilateServiceImpl implements AffilateService {
    private static final String  TEXT_HTML = "text/html";

    @Autowired
    private UserService          userService;
    @Autowired
    private AffiliateDao         affiliateDao;
    @Autowired
    private OfferInstanceDao     offerInstanceDao;
    @Autowired
    private RewardPointDao       rewardPointDao;
    @Autowired
    private AffiliateTxnDao      affiliateTxnDao;
    @Autowired
    private AffiliateCategoryDao affiliateCategoryCommissionDao;
	@Autowired
	private AffiliateCategoryHasBrandDao affiliateCategoryHasBrandDao;

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
                else if (offerInstance.getCoupon().getCouponType() != null && offerInstance.getCoupon().getCouponType().getId().equals(EnumCouponType.AFFILIATE.getId()) && user.getAffiliateTo() != null
                        && affiliateDao.getAffilateByUser(user.getAffiliateTo()) != null) {
                    addAmountInAccountforFirstTransaction(user.getAffiliateTo(), order);
                    order.setReferredOrder(true);
                }
            }
        }
        // else check if user is already affiliated, then pay him latter commission
        else if (user.getAffiliateTo() != null && affiliateDao.getAffilateByUser(user.getAffiliateTo()) != null && !hasGotAffiliateCommission) {
            addAmountInAccountforLatterTransaction(user.getAffiliateTo(), order);
            order.setReferredOrder(true);
        }
    }

    private void addAmountInAccountforFirstTransaction(User affiliateUser, Order order) {
        Affiliate affiliate = getAffilateByUser(affiliateUser);
        Double affiliateSumTotal = 0D;
        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        for (CartLineItem cartLineItem : productCartLineItems) {
            AffiliateCategory affiliateCategory = cartLineItem.getProductVariant().getAffiliateCategory();
            if (affiliateCategory != null) {
                AffiliateCategoryCommission affiliateCategoryCommission = getAffiliateCategoryCommissionDao().getCommissionAffiliateWise(affiliate, affiliateCategory);
                if (affiliateCategoryCommission != null) {
                    affiliateSumTotal += cartLineItem.getProductVariant().getHkPrice(null) * cartLineItem.getQty() * affiliateCategoryCommission.getCommissionFirstTime() * 0.01;
                }
            }
        }
	    AffiliateTxnType affiliateTxnType = null;
	    if(EnumPaymentMode.getPrePaidPaymentModes().contains(order.getPayment().getPaymentMode().getId())){
		    affiliateTxnType = getAffiliateTxnType(EnumAffiliateTxnType.ADD.getId());
	    }else{
		    affiliateTxnType = getAffiliateTxnType(EnumAffiliateTxnType.PENDING.getId());
	    }
        getAffiliateTxnDao().saveTxn(affiliate, affiliateSumTotal, affiliateTxnType, order);
    }

    private void addAmountInAccountforLatterTransaction(User affiliateUser, Order order) {
        Affiliate affiliate = getAffilateByUser(affiliateUser);
        Double affiliateSumTotal = 0D;
        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        for (CartLineItem cartLineItem : productCartLineItems) {
            AffiliateCategory affiliateCategory = cartLineItem.getProductVariant().getAffiliateCategory();
            if (affiliateCategory != null) {
                AffiliateCategoryCommission affiliateCategoryCommission = getAffiliateCategoryCommissionDao().getCommissionAffiliateWise(affiliate, affiliateCategory);
                if (affiliateCategoryCommission != null) {
                    affiliateSumTotal += cartLineItem.getProductVariant().getHkPrice() * cartLineItem.getQty() * affiliateCategoryCommission.getCommissionLatterTime() * 0.01;
                }
            }
        }
	    AffiliateTxnType affiliateTxnType = null;
	    if(EnumPaymentMode.getPrePaidPaymentModes().contains(order.getPayment().getPaymentMode().getId())){
		    affiliateTxnType = getAffiliateTxnType(EnumAffiliateTxnType.ADD.getId());
	    }else{
		    affiliateTxnType = getAffiliateTxnType(EnumAffiliateTxnType.PENDING.getId());
	    }
	    getAffiliateTxnDao().saveTxn(affiliate, affiliateSumTotal, affiliateTxnType, order);
    }

	public void associateBrandToAffiliateCategory(AffiliateCategory affiliateCategory, String brand) {
		affiliateCategoryHasBrandDao.associateBrandToAffiliateCategory(affiliateCategory, brand);
		affiliateCategoryHasBrandDao.associateAffiliateCategoryToVariantViaBrand(affiliateCategory, brand);
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
                    addAmountInAccountforFirstTransaction(affiliate.getUser(), order);
                } else if (user.getAffiliateTo().equals(affiliate.getUser())) {
                    addAmountInAccountforLatterTransaction(affiliate.getUser(), order);
                } else {
                    addAmountInAccountforFirstTransaction(affiliate.getUser(), order);
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

	public Long getMaxCouponsLeft(Affiliate affiliate){
		return affiliateDao.getMaxCouponsLeft(affiliate);
	}

	public void approvePendingAffiliateTxn(Order order) {
		User affiliateTo = order.getUser().getAffiliateTo();
		if (affiliateTo != null) {
			Affiliate affiliate = getAffilateByUser(affiliateTo);
			if (affiliate != null) {
				affiliateTxnDao.approvePendingAffiliateTxn(affiliate, order);
			}
		}
	}

	@Override
	public Page searchAffiliates(List<Long> affiliateStatusIds, String name, String email, String websiteName, String code, Long affiliateMode, Long affiliateType, Role role, int perPage, int pageNo) {
		return affiliateDao.searchAffiliates(affiliateStatusIds,name,email,websiteName,code,affiliateMode,affiliateType, role, perPage, pageNo);
	}

	@Override
	public void markAffiliateTxnAsDue(Affiliate affiliate) {
		affiliateTxnDao.markAffiliateTxnAsDue(affiliate);
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

    public AffiliateCategoryDao getAffiliateCategoryCommissionDao() {
        return affiliateCategoryCommissionDao;
    }

    public void setAffiliateCategoryCommissionDao(AffiliateCategoryDao affiliateCategoryCommissionDao) {
        this.affiliateCategoryCommissionDao = affiliateCategoryCommissionDao;
    }

}
