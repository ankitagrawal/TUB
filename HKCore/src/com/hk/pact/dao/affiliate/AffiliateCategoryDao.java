package com.hk.pact.dao.affiliate;

import java.util.List;

import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.pact.dao.BaseDao;

public interface AffiliateCategoryDao extends BaseDao {

    public AffiliateCategory getAffiliateCategoryByName(String name);
    
    public List<AffiliateCategory> getAffiliateCategoryCommissionList() ;

    public void insertCategoryCommissionsAffiliateWise(Affiliate affiliate) ;

    @SuppressWarnings("unchecked")
    public void deleteCategoryCommissionForExistingAffiliates(AffiliateCategory affiliateCategory) ;

    public AffiliateCategoryCommission getCommissionAffiliateWise(Affiliate affiliate, AffiliateCategory affiliateCategory) ;

}
