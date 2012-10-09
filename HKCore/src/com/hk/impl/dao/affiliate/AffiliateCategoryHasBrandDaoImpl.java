package com.hk.impl.dao.affiliate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.affiliate.AffiliateCategoryHasBrand;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.affiliate.AffiliateCategoryHasBrandDao;
import com.hk.pact.service.catalog.ProductVariantService;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 8/22/12
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class AffiliateCategoryHasBrandDaoImpl extends BaseDaoImpl implements AffiliateCategoryHasBrandDao  {

	@Autowired
	ProductVariantService productVariantService;


	public AffiliateCategoryHasBrand associateBrandToAffiliateCategory(AffiliateCategory affiliateCategory, String brand){
		AffiliateCategoryHasBrand affiliateCategoryHasBrand = new AffiliateCategoryHasBrand();
		affiliateCategoryHasBrand.setAffiliateCategory(affiliateCategory);
		affiliateCategoryHasBrand.setBrand(brand);
		return (AffiliateCategoryHasBrand) save(affiliateCategoryHasBrand);
	}

	public void associateAffiliateCategoryToVariantViaBrand(AffiliateCategory affiliateCategory, String brand){
		List<ProductVariant> productVariants = productVariantService.getAllNonDeletedProductVariants(null,brand,false);
		for (ProductVariant productVariant : productVariants) {
			productVariant.setAffiliateCategory(affiliateCategory);
			save(productVariant);
		}
	}



}
