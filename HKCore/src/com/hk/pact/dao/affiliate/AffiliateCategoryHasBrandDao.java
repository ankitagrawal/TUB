package com.hk.pact.dao.affiliate;

import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.affiliate.AffiliateCategoryHasBrand;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 8/22/12
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AffiliateCategoryHasBrandDao extends BaseDao {

	public AffiliateCategoryHasBrand associateBrandToAffiliateCategory(AffiliateCategory affiliateCategory, String brand);

	public void associateAffiliateCategoryToVariantViaBrand(AffiliateCategory affiliateCategory, String brand);

}
