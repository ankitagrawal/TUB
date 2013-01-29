package com.hk.web.action.core.loyaltypg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.HttpCache;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.store.ProductAdapter;
import com.hk.store.SearchCriteria;
import com.hk.web.action.core.auth.LoginAction;

@Component
@UrlBinding("/loyaltypg")
@Secure(hasAnyRoles = {RoleConstants.HK_UNVERIFIED, RoleConstants.HK_USER}, authUrl = "/core/auth/Login.action?source=" + LoginAction.SOURCE_CHECKOUT, disallowRememberMe = true)
@HttpCache(allow = false)
public class LoyaltyCatalogAction extends AbstractLoyaltyAction {

	private int defaultPerPage = 10;
	private Page productPage;
	private List<LoyaltyProduct> productList;

	@DefaultHandler
	public Resolution pre() {
		if(getPrincipal() == null) {
			return new RedirectResolution(LoginAction.class);
		}
		SearchCriteria criteria = new SearchCriteria();

		int startRow = (getPageNo()-1)*getPerPage();
		int maxRow = getPageNo()*getPerPage() - startRow;

		criteria.setStartRow(startRow);
		criteria.setMaxRows(maxRow);

		List<ProductAdapter> list = getProcessor().searchProducts(getPrincipal().getId(), criteria);
		productList = new ArrayList<LoyaltyProduct>();
		for (ProductAdapter productAdapter : list) {
			productList.add(productAdapter.getLoyaltyProduct());
		}
		productPage = new Page(productList, getPerPage(), getPerPageDefault(), 4);
		return new ForwardResolution("/pages/loyalty/catalog.jsp");
	}
	
	@Override
	public int getPerPageDefault() {
		return defaultPerPage;
	}

	@Override
	public int getPageCount() {
		return productPage == null ? 0 : productPage.getTotalPages();
	}

	@Override
	public int getResultCount() {
		return productPage == null ? 0 : productPage.getTotalResults();
	}

	@Override
	public Set<String> getParamSet() {
		return new HashSet<String>();
	}
	
	public List<LoyaltyProduct> getProductList() {
		return productList;
	}
}
