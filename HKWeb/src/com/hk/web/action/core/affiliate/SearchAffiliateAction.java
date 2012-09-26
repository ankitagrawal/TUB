package com.hk.web.action.core.affiliate;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateStatus;
import com.hk.domain.user.Role;
import com.hk.pact.service.core.AffilateService;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 8/23/12
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SearchAffiliateAction extends BaseAction {

	AffiliateStatus affiliateStatus;
	String name;
	String email;
	String websiteName;
	String code;
	Long affiliateMode;
	Long affiliateType;
	Role role;
	int perPage;
	int pageNo;

	Page affiliatePage;

	List<Affiliate>  affiliates;

	@Autowired
	AffilateService affilateService;

	public Resolution pre(){
		return new RedirectResolution("/pages/affiliate/searchAffiliates.jsp");
	}

	public Resolution searchAffiliates() {
		affiliatePage = affilateService.searchAffiliates(Arrays.asList(affiliateStatus.getId()), name, email, websiteName, code, affiliateMode, affiliateType, role, perPage, pageNo);
		affiliates = affiliatePage != null ? affiliatePage.getList() : null;
		return new RedirectResolution("/pages/affiliate/searchAffiliates.jsp");
	}


}
