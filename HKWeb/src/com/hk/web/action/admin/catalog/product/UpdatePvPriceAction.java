package com.hk.web.action.admin.catalog.product;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.user.User;
import com.hk.pact.dao.catalog.product.UpdatePvPriceDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

@Secure (hasAnyPermissions = {PermissionConstants.UPDATE_PRODUCT_CATALOG}, authActionBean = AdminPermissionAction.class)
@Component
public class UpdatePvPriceAction extends BasePaginatedAction {

	@Autowired
	UpdatePvPriceDao updatePvPriceDao;
	@Autowired
	ProductVariantService productVariantService;

	Page updatePvPricePage;
	List<UpdatePvPrice> updatePvPriceList = new ArrayList<UpdatePvPrice>();
	UpdatePvPrice updatePvPrice;
	Category primaryCategory;
	boolean updated;

	@DefaultHandler
	public Resolution pre() {
		updatePvPricePage = updatePvPriceDao.getPVForPriceUpdate(primaryCategory, updated, getPageNo(), getPerPage());
		if (updatePvPricePage != null) {
			updatePvPriceList = updatePvPricePage.getList();
		}
		return new ForwardResolution("/pages/updatePvPriceList.jsp");
	}

	public Resolution update() {
		ProductVariant pv = updatePvPrice.getProductVariant();
		pv.setCostPrice(updatePvPrice.getNewCostPrice());
		pv.setMarkedPrice(updatePvPrice.getNewMrp());
		pv.setHkPrice(updatePvPrice.getNewHkprice());
		productVariantService.save(pv);

		User loggedOnUser = getUserService().getLoggedInUser();
		updatePvPrice.setUpdated(true);
		updatePvPrice.setUpdateDate(new Date());
		updatePvPrice.setUpdatedBy(loggedOnUser);
		getBaseDao().save(updatePvPrice);

		addRedirectAlertMessage(new SimpleMessage("Price updated successfully."));
		return new RedirectResolution(UpdatePvPriceAction.class);
	}

	public List<UpdatePvPrice> getUpdatePvPriceList() {
		return updatePvPriceList;
	}

	public void setUpdatePvPrice(UpdatePvPrice updatePvPrice) {
		this.updatePvPrice = updatePvPrice;
	}

	public Category getPrimaryCategory() {
		return primaryCategory;
	}

	public void setPrimaryCategory(Category primaryCategory) {
		this.primaryCategory = primaryCategory;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("primaryCategory");
		params.add("updated");
		return params;
	}

	public int getPerPageDefault() {
		return 30;
	}

	public int getPageCount() {
		return updatePvPricePage == null ? 0 : updatePvPricePage.getTotalPages();
	}

	public int getResultCount() {
		return updatePvPricePage == null ? 0 : updatePvPricePage.getTotalResults();
	}
}