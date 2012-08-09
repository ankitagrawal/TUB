package com.hk.web.action.admin.inventory;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.inventory.BrandsToAuditDao;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.hk.domain.inventory.BrandsToAudit;
import com.hk.web.action.error.AdminPermissionAction;

import java.util.*;

@Secure (hasAnyRoles = {RoleConstants.WH_MANAGER}, authActionBean = AdminPermissionAction.class)
public class BrandsToAuditAction extends BasePaginatedAction {

	private static Logger logger = Logger.getLogger(BrandsToAuditAction.class);
	private String brand;
	private Warehouse warehouse;
	private String auditorLogin;
	private Date startDate;
	private Date endDate;
	private List<BrandsToAudit> brandsToAuditList = new ArrayList<BrandsToAudit>();
	private Page brandsAuditPage;
	private BrandsToAudit brandsToAudit;

	@Autowired
	private BrandsToAuditDao brandsToAuditDao;

	private Integer defaultPerPage = 20;

	@DefaultHandler
	public Resolution pre() {
		User loggedOnUser = getPrincipalUser();
		if (warehouse == null) warehouse = loggedOnUser.getSelectedWarehouse();
		User auditor = null;
		if(StringUtils.isNotBlank(auditorLogin)){
			auditor = getUserService().findByLogin(auditorLogin);
		}
		brandsAuditPage = getBrandsToAuditDao().searchAuditList(brand, warehouse, auditor, startDate, endDate, getPageNo(), getPerPage());
		if(brandsAuditPage != null){
			brandsToAuditList = brandsAuditPage.getList();
		}
		return new ForwardResolution("/pages/admin/brandAuditList.jsp");
	}

	public Resolution view() {
		if (brandsToAudit == null) {
			brandsToAudit = new BrandsToAudit();
			brandsToAudit.setAuditStatus(0L);
			brandsToAudit.setAuditDate(new Date());
		}
		return new ForwardResolution("/pages/admin/brandToAudit.jsp");
	}

	public Resolution save() {
		logger.debug("brand: " + brandsToAudit.getBrand());
		try {
			if (brandsToAudit != null && brandsToAudit.getAuditor() == null && getPrincipalUser() != null) {
				User auditor = getPrincipalUser();
				warehouse = auditor.getSelectedWarehouse();
				brandsToAudit.setAuditor(auditor);
				brandsToAudit.setWarehouse(warehouse);
			}
			getBrandsToAuditDao().save(brandsToAudit);

			addRedirectAlertMessage(new SimpleMessage("Saved."));
			return new RedirectResolution(BrandsToAuditAction.class);
		} catch (Exception e) {
			addRedirectAlertMessage(new SimpleMessage("Some Error."));
			return new RedirectResolution(BrandsToAuditAction.class).addParameter("view").addParameter("brandsToAudit", brandsToAudit.getId());
		}

	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getAuditorLogin() {
		return auditorLogin;
	}

	public void setAuditorLogin(String auditorLogin) {
		this.auditorLogin = auditorLogin;
	}

	public Date getStartDate() {
		return startDate;
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

	public List<BrandsToAudit> getBrandsToAuditList() {
		return brandsToAuditList;
	}

	public void setBrandsToAuditList(List<BrandsToAudit> brandsToAuditList) {
		this.brandsToAuditList = brandsToAuditList;
	}

	public BrandsToAudit getBrandsToAudit() {
		return brandsToAudit;
	}

	public void setBrandsToAudit(BrandsToAudit brandsToAudit) {
		this.brandsToAudit = brandsToAudit;
	}

	public BrandsToAuditDao getBrandsToAuditDao() {
		return brandsToAuditDao;
	}

	public void setBrandsToAuditDao(BrandsToAuditDao brandsToAuditDao) {
		this.brandsToAuditDao = brandsToAuditDao;
	}

	public int getPerPageDefault() {
		return defaultPerPage;
	}

	public int getPageCount() {
		return brandsAuditPage == null ? 0 : brandsAuditPage.getTotalPages();
	}

	public int getResultCount() {
		return brandsAuditPage == null ? 0 : brandsAuditPage.getTotalResults();
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("brand");
		params.add("warehouse");
		params.add("auditorLogin");
		params.add("startDate");
		params.add("endDate");
		return params;
	}
}