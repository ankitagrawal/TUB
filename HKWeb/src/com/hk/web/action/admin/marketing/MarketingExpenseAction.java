package com.hk.web.action.admin.marketing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.marketing.MarketingExpenseDao;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.marketing.MarketingExpense;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Mar 5, 2012
 * Time: 4:26:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.MARKETING_EXPENSE_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class MarketingExpenseAction extends BasePaginatedAction {

	private static Logger logger = Logger.getLogger(MarketingExpenseAction.class);
	@Autowired
	MarketingExpenseDao marketingExpenseDao;
	@Autowired
	CategoryDao categoryDao;
	
	
	private Date startDate;
	private Date endDate;
	private Category category;
	private Integer defaultPerPage = 20;
	private Long marketingExpenseId;
	private MarketingExpense marketingExpense;
	private MarketingExpense uniqueMarketingExpense;
    private Long adNetworksId;

	Page marketingExpensePage;

	private List<MarketingExpense> marketingExpenseList = new ArrayList<MarketingExpense>();

	@DefaultHandler
	public Resolution pre() {

		marketingExpensePage = marketingExpenseDao.searchMarketingExpense(category, adNetworksId, startDate, endDate, getPageNo(), getPerPage());
		marketingExpenseList = marketingExpensePage.getList();
		return new ForwardResolution("/pages/admin/marketingExpenseList.jsp");
	}

	public Resolution save() {
		Category category = categoryDao.getCategoryByName(Category.getNameFromDisplayName(marketingExpense.getCategory().getName()));
		if (category.getName() == null) {
			addRedirectAlertMessage(new SimpleMessage("Please verify the category name."));
			return new RedirectResolution(MarketingExpenseAction.class);
		}
			uniqueMarketingExpense = marketingExpenseDao.checkUniqueMarketingExpense(marketingExpense.getCategory(), marketingExpense.getDate(), marketingExpense.getAdNetworks());
			if (uniqueMarketingExpense != null && marketingExpense.getId() == null) {
				addRedirectAlertMessage(new SimpleMessage("The entries corresponding to this category, Ad Network and this date have already been entered. Please check in the list and edit it if needed."));
				return new RedirectResolution(MarketingExpenseAction.class);
			}
			if (marketingExpense != null || marketingExpense.getId() == null) {
				marketingExpenseDao.save(marketingExpense);
			}
			addRedirectAlertMessage(new SimpleMessage("Changes saved."));
			return new RedirectResolution(MarketingExpenseAction.class);
		}

	public Resolution addMarketingExpense() {
		return new ForwardResolution("/pages/admin/marketingExpense.jsp");
	}

	public Resolution view() {
		if (marketingExpenseId != null) {
			marketingExpense = marketingExpenseDao.get(MarketingExpense.class, marketingExpenseId);
			logger.debug("marketingExpense@view: " + marketingExpense.getId());
		}
		return new ForwardResolution("/pages/admin/marketingExpense.jsp");
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("category");
		return params;
	}

	public int getPerPageDefault() {
		return defaultPerPage;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public int getPageCount() {
		return marketingExpensePage == null ? 0 : marketingExpensePage.getTotalPages();
	}

	public int getResultCount() {
		return marketingExpensePage == null ? 0 : marketingExpensePage.getTotalResults();
	}

	public Date getStartDate() {
		return startDate;
	}

    public Long getAdNetworksId() {
        return adNetworksId;
    }

    public void setAdNetworksId(Long adNetworksId) {
        this.adNetworksId = adNetworksId;
    }

    public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<MarketingExpense> getMarketingExpenseList() {
		return marketingExpenseList;
	}

	public void setMarketingExpenseList(List<MarketingExpense> marketingExpenseList) {
		this.marketingExpenseList = marketingExpenseList;
	}

	public MarketingExpense getMarketingExpense() {
		return marketingExpense;
	}

	public void setMarketingExpense(MarketingExpense marketingExpense) {
		this.marketingExpense = marketingExpense;
	}

	public void setMarketingExpenseId(Long marketingExpenseId) {
		this.marketingExpenseId = marketingExpenseId;
	}

}
