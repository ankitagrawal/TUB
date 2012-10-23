package com.hk.web.action.admin.catalog.product;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.catalog.product.EnumUpdatePVPriceStatus;
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
	ProductVariant productVariant;
	Long status;

	@DefaultHandler
	public Resolution pre() {
		if(status == null) status = EnumUpdatePVPriceStatus.Pending.getId();
		updatePvPricePage = updatePvPriceDao.getPVForPriceUpdate(primaryCategory, productVariant, status, getPageNo(), getPerPage());
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
		updatePvPrice.setStatus(EnumUpdatePVPriceStatus.Updated.getId());
		updatePvPrice.setUpdateDate(new Date());
		updatePvPrice.setUpdatedBy(loggedOnUser);
		getBaseDao().save(updatePvPrice);

		addRedirectAlertMessage(new SimpleMessage("Price updated successfully."));
		return new RedirectResolution(UpdatePvPriceAction.class);
	}

	public Resolution ignore() {

		User loggedOnUser = getUserService().getLoggedInUser();
		updatePvPrice.setStatus(EnumUpdatePVPriceStatus.Ignored.getId());
		updatePvPrice.setUpdateDate(new Date());
		updatePvPrice.setUpdatedBy(loggedOnUser);
		getBaseDao().save(updatePvPrice);

		addRedirectAlertMessage(new SimpleMessage("Price update recommendation ignored successfully."));
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

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("primaryCategory");
		params.add("productVariant");
		params.add("status");
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