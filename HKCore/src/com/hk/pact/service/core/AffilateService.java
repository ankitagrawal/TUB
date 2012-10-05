package com.hk.pact.service.core;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.affiliate.AffiliateTxnType;
import com.hk.domain.order.Order;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;

public interface AffilateService {

    //TODO: javadoc
    public void saveOfferInstanceAndSaveAffiliateCommission(Order order, PricingDto pricingDto);

    public void cancelTxn(Order order);

    public Affiliate save(Affiliate affiliate);
    
    public Affiliate getAffiliateByUserId(Long userId);
    
    public Affiliate getAffilateByUser(User affiliateUser);
    
    public AffiliateTxnType getAffiliateTxnType(Long txnId);

	public void associateBrandToAffiliateCategory(AffiliateCategory affiliateCategory, String brand);

	public Long getMaxCouponsLeft(Affiliate affiliate);

	public void approvePendingAffiliateTxn(Order order);

	public Page searchAffiliates(List<Long> affiliateStatusIds, String name, String email, String websiteName, String code, Long affiliateMode, Long affiliateType, Role role, int perPage, int pageNo);

	public void markAffiliateTxnAsDue(Affiliate affiliate);
}
