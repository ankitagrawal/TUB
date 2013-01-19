package com.hk.web.action.core.loyaltypg;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.springframework.stereotype.Component;

import com.akube.framework.dao.Page;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.store.SearchCriteria;
import com.hk.web.action.core.auth.LoginAction;

@Component
@UrlBinding("/loyaltypg")
public class LoyaltyCatalogAction extends AbstractLoyaltyAction {

	private int defaultPerPage = 24;
	private Page productPage;
	private List<LoyaltyProduct> productList;

	@DefaultHandler
	public Resolution pre() {
		if(getPrincipal() == null) {
			return new RedirectResolution(LoginAction.class);
		}
		SearchCriteria criteria = new SearchCriteria();
		criteria.setStartRow(0);
		criteria.setMaxRows(0);
		productList = getProcessor().searchProducts(getPrincipal().getId(), criteria);
		productPage = new Page(productList, getPerPage(), getPageNo(), (int) productList.size());
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
