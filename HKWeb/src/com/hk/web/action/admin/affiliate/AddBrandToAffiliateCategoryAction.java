package com.hk.web.action.admin.affiliate;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.pact.service.core.AffilateService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 8/22/12
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.MANAGE_AFFILIATES}, authActionBean = AdminPermissionAction.class)
@Component
public class AddBrandToAffiliateCategoryAction extends BaseAction {

	@Autowired
	AffilateService affilateService;

	AffiliateCategory affiliateCategory;
	String brand;

	public Resolution addBrandsToExistingAffiliateCategory() {
		affilateService.associateBrandToAffiliateCategory(affiliateCategory, brand);
		addRedirectAlertMessage(new SimpleMessage("Brand Added, and Affiliate Category Set at Variant Level"));
		return new RedirectResolution(AddBrandToAffiliateCategoryAction.class);
	}


}
