package com.hk.impl.dao.affiliate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.domain.affiliate.DefaultCategoryCommission;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.affiliate.AffiliateCategoryDao;

@Repository
public class AffiliateCategoryDaoImpl extends BaseDaoImpl implements AffiliateCategoryDao {

    public AffiliateCategory getAffiliateCategoryByName(String name) {
        return get(AffiliateCategory.class, name);
    }

    public List<AffiliateCategory> getAffiliateCategoryCommissionList() {
        return getAll(AffiliateCategory.class);
    }

    public void insertCategoryCommissionsAffiliateWise(Affiliate affiliate) {
        AffiliateCategoryCommission affiliateCategoryCommission;
        List<DefaultCategoryCommission> allCommisions = getAll(DefaultCategoryCommission.class);
        for (DefaultCategoryCommission categoryCommission : allCommisions) {
            affiliateCategoryCommission = new AffiliateCategoryCommission();
            affiliateCategoryCommission.setAffiliate(affiliate);
            affiliateCategoryCommission.setAffiliateCategory(categoryCommission.getAffiliateCategory());
            affiliateCategoryCommission.setCommissionFirstTime(categoryCommission.getCommissionFirstTime());
            affiliateCategoryCommission.setCommissionLatterTime(categoryCommission.getCommissionLatterTime());
            save(affiliateCategoryCommission);
        }
    }

    /*
     * public void modifyCategoryCommissionForExistingAffiliates(DefaultCategoryCommission defaultCategoryCommission) { }
     */

    @SuppressWarnings("unchecked")
    public void deleteCategoryCommissionForExistingAffiliates(AffiliateCategory affiliateCategory) {
        String queryString = "from AffiliateCategoryCommission acc where acc.affiliateCategory = ?";
        List<AffiliateCategoryCommission> toBeRemoved = findByQuery(queryString, new Object[] { affiliateCategory });

        deleteAll(toBeRemoved);
        /*
         * for (AffiliateCategoryCommission categoryCommission : toBeRemoved) { delete(categoryCommission); }
         */
    }

    public AffiliateCategoryCommission getCommissionAffiliateWise(Affiliate affiliate, AffiliateCategory affiliateCategory) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("affiliate", affiliate);
        params.put("affiliateCategory", affiliateCategory);
        String queryString = "from AffiliateCategoryCommission acc where acc.affiliate = ? and acc.affiliateCategory = ?";

        return (AffiliateCategoryCommission) findUnique(queryString, new Object[] { affiliate, affiliateCategory });

    }
}
