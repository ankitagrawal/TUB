package com.hk.web.action.core.loyaltypg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.loyaltypg.Badge;
import com.hk.domain.loyaltypg.LoyaltyProduct;
import com.hk.loyaltypg.dto.CategoryLoyaltyDto;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.store.ProductAdapter;
import com.hk.store.SearchCriteria;


@Component
@UrlBinding("/loyaltypg")
@Secure(hasAnyRoles = {RoleConstants.HK_USER}, authActionBean=SignInAction.class)
public class LoyaltyCatalogAction extends AbstractLoyaltyAction {

	private int defaultPerPage = 10;
	private Page productPage;
	private List<LoyaltyProduct> productList;
	private List<Badge> badgeList;
	private List<CategoryLoyaltyDto> categories;
	private List<Category> testList ;
	private String categoryName;
	private double minPoints;
	private double maxPoints;
	
	
	@Autowired
	LoyaltyProgramService loyaltyProgramService;

	@DefaultHandler
	public Resolution pre() {
		SearchCriteria criteria = new SearchCriteria();
		int startRow = (this.getPageNo()-1)*this.getPerPage();
		int maxRow = this.getPageNo()*this.getPerPage() - startRow;

		criteria.setStartRow(startRow);
		criteria.setMaxRows(maxRow);
		
		int count = this.getProcessor().countProducts(this.getPrincipal().getId(), criteria);
		List<ProductAdapter> list = this.getProcessor().searchProducts(this.getPrincipal().getId(), criteria);
		this.productList = new ArrayList<LoyaltyProduct>();
		for (ProductAdapter productAdapter : list) {
			this.productList.add(productAdapter.getLoyaltyProduct());
		}
	
		this.setCategories(this.loyaltyProgramService.getLoyaltyCatalog());
		this.testList = this.loyaltyProgramService.getCategoryForLoyaltyProducts();
		
		this.productPage = new Page(this.productList, this.getPerPage(), this.getPerPageDefault(), count);
		return new ForwardResolution("/pages/loyalty/catalog.jsp");
	}

	public Resolution aboutLoyaltyProgram() {
		this.badgeList = this.loyaltyProgramService.getAllBadges();
		return new ForwardResolution("/pages/loyalty/aboutLoyaltyProgram.jsp");
	}
	
	/**
	 * @param categoryName
	 * @return
	 */
	public Resolution listProductsByCategory() {

		SearchCriteria criteria = new SearchCriteria();
		this.setCategories(this.getCategories());		
		//this.setCategories(this.loyaltyProgramService.getLoyaltyCatalog());
		int startRow = (this.getPageNo()-1)*this.getPerPage();
		int maxRow = this.getPageNo()*this.getPerPage() - startRow;

		criteria.setStartRow(startRow);
		criteria.setMaxRows(maxRow);

		int count = this.getProcessor().countProducts(this.getPrincipal().getId(), criteria);
		/*List<ProductAdapter> list = this.getProcessor().searchProducts(this.getPrincipal().getId(), criteria);
		this.productList = new ArrayList<LoyaltyProduct>();
		for (ProductAdapter productAdapter : list) {
			this.productList.add(productAdapter.getLoyaltyProduct());
		}
*/
		this.productList = this.loyaltyProgramService.getProductsByCategoryName(this.categoryName);
		
		this.productPage = new Page(this.productList, this.getPerPage(), this.getPerPageDefault(), count);

		return new ForwardResolution("/pages/loyalty/catalog.jsp");
	}
	
	public Resolution listProductsByPoints () {
		
		SearchCriteria criteria = new SearchCriteria();
		this.setCategories(this.getCategories());
		int startRow = (this.getPageNo()-1)*this.getPerPage();
		int maxRow = this.getPageNo()*this.getPerPage() - startRow;

		criteria.setStartRow(startRow);
		criteria.setMaxRows(maxRow);

		int count = this.getProcessor().countProducts(this.getPrincipal().getId(), criteria);
		/*List<ProductAdapter> list = this.getProcessor().searchProducts(this.getPrincipal().getId(), criteria);
		this.productList = new ArrayList<LoyaltyProduct>();
		for (ProductAdapter productAdapter : list) {
			this.productList.add(productAdapter.getLoyaltyProduct());
		}
*/
		this.productList = this.loyaltyProgramService.getProductsByPoints(this.minPoints, this.maxPoints);
		
		this.productPage = new Page(this.productList, this.getPerPage(), this.getPerPageDefault(), count);

		return new ForwardResolution("/pages/loyalty/catalog.jsp");
	}

	@Override
	public int getPerPageDefault() {
		return this.defaultPerPage;
	}

	@Override
	public int getPageCount() {
		return this.productPage == null ? 0 : this.productPage.getTotalPages();
	}

	@Override
	public int getResultCount() {
		return this.productPage == null ? 0 : this.productPage.getTotalResults();
	}

	@Override
	public Set<String> getParamSet() {
		return new HashSet<String>();
	}

	public List<LoyaltyProduct> getProductList() {
		return this.productList;
	}

	public List<Badge> getBadgeList() {
		return this.badgeList;
	}

	public void setBadgeList(List<Badge> badgeList) {
		this.badgeList = badgeList;
	}

	public List<CategoryLoyaltyDto> getCategories() {
		return this.categories;
	}

	public void setCategories(List<CategoryLoyaltyDto> categories) {
		this.categories = categories;
	}

	/**
	 * @return the minPoints
	 */
	public double getMinPoints() {
		return this.minPoints;
	}

	/**
	 * @param minPoints the minPoints to set
	 */
	public void setMinPoints(double minPoints) {
		this.minPoints = minPoints;
	}

	/**
	 * @return the maxPoints
	 */
	public double getMaxPoints() {
		return this.maxPoints;
	}

	/**
	 * @param maxPoints the maxPoints to set
	 */
	public void setMaxPoints(double maxPoints) {
		this.maxPoints = maxPoints;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return this.categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return the testList
	 */
	public List<Category> getTestList() {
		return this.testList;
	}

	/**
	 * @param testList the testList to set
	 */
	public void setTestList(List<Category> testList) {
		this.testList = testList;
	}
}
