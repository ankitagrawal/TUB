package com.hk.pact.service.core;

import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateTxnType;
import com.hk.domain.order.Order;
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
}
