package com.hk.pact.dao.affiliate;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface AffiliateDao extends BaseDao {

    public Affiliate getAffilateByUser(User user);

    public Affiliate getAffiliateByCode(String code);

    public Affiliate getAffiliateByUserId(Long userId);

		public Affiliate getAffiliateById(Long affiliateId);

    @SuppressWarnings("unchecked")
    public List<AffiliateCategoryCommission> getAffiliatePlan(Affiliate affiliate);

    @SuppressWarnings("unchecked")
    public Page getAffiliateVerifiedUsers(int page, int perPage);

	public Long getMaxCouponsLeft(Affiliate affiliate);

	Page searchAffiliates(List<Long> affiliateStatusIds, String name, String email, String websiteName, String code, Long affiliateMode, Long affiliateType, Role role, int perPage, int pageNo);
}
