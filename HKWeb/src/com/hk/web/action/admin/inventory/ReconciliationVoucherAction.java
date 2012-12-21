package com.hk.web.action.admin.inventory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.inventory.ReconciliationVoucherDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.ReconciliationVoucherService;
import com.hk.admin.util.ReconciliationVoucherParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.web.HealthkartResponse;

@Secure(hasAnyPermissions = {PermissionConstants.RECON_VOUCHER_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class ReconciliationVoucherAction extends BasePaginatedAction {

	private static Logger logger = Logger.getLogger(ReconciliationVoucherAction.class);
	@Autowired
	ReconciliationVoucherDao reconciliationVoucherDao;

	@Autowired
	ProductVariantDao productVariantDao;
	@Autowired
	private ReconciliationVoucherParser rvParser;
	@Autowired
	ReconciliationVoucherService reconciliationVoucherService;

	@Autowired
	UserDao userDao;
	@Autowired
	AdminSkuItemDao adminSkuItemDao;
	@Autowired
	AdminInventoryService adminInventoryService;
	/*@Autowired
		private InventoryService inventoryService;*/
	@Autowired
	AdminProductVariantInventoryDao productVariantInventoryDao;
	@Autowired
	SkuService skuService;
	@Autowired
	private ProductVariantService productVariantService;
	@Autowired
	SkuGroupDao skuGroupDao;
	@Autowired
	SkuItemDao skuItemDao;

	private ReconciliationVoucher reconciliationVoucher;
	private List<ReconciliationVoucher> reconciliationVouchers = new ArrayList<ReconciliationVoucher>();
	private List<RvLineItem> rvLineItems = new ArrayList<RvLineItem>();
	private RvLineItem rvLineItem;
	public String productVariantId;
	Page reconciliationVoucherPage;
	private Integer defaultPerPage = 30;
	private Date createDate;
	private String userLogin;
	private Warehouse warehouse;
	private Integer askedQty;
	private String batchNumber;

	@Validate(required = true, on = "parse")
	private FileBean fileBean;

	@Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
	String adminUploadsPath;

	@SuppressWarnings("unchecked")
	@DefaultHandler
	public Resolution pre() {
		if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
			warehouse = getPrincipalUser().getSelectedWarehouse();
		}
		reconciliationVoucherPage = reconciliationVoucherDao.searchReconciliationVoucher(createDate, userLogin, warehouse, getPageNo(), getPerPage());
		reconciliationVouchers = reconciliationVoucherPage.getList();
		return new ForwardResolution("/pages/admin/reconciliationVoucherList.jsp");
	}


	public Resolution parse() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String excelFilePath = adminUploadsPath + "/rvFiles/" + reconciliationVoucher.getId() + sdf.format(new Date()) + ".xls";
		File excelFile = new File(excelFilePath);
		excelFile.getParentFile().mkdirs();
		fileBean.save(excelFile);

		try {
			//reconciliationVoucher has warehouse and reconciliation date
			rvLineItems = rvParser.readAndCreateRVLineItems(excelFilePath, "Sheet1", reconciliationVoucher);
			save();
		} catch (Exception e) {
			logger.error("Exception while reading excel sheet.", e);
			addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
		}
		return new RedirectResolution(ReconciliationVoucherAction.class);
	}

	public Resolution save() {
		User loggedOnUser = null;
		if (getPrincipal() != null) {
			loggedOnUser = getUserService().getUserById(getPrincipal().getId());
		}
		reconciliationVoucherService.save(loggedOnUser, rvLineItems, reconciliationVoucher);

		addRedirectAlertMessage(new SimpleMessage("Changes saved."));
		return new RedirectResolution(ReconciliationVoucherAction.class);
	}




	
	public Resolution saveAll() {
		List<RvLineItem> rvNotSaved = reconciliationVoucherService.newSave(rvLineItems, reconciliationVoucher);
		if (rvNotSaved.size() > 0) {
			addRedirectAlertMessage(new SimpleMessage("Below RV Line items are not saved"));
			for (RvLineItem rvLineItem : rvNotSaved) {
				addRedirectAlertMessage(new SimpleMessage("Product Variant =  " + rvLineItem.getProductVariant() + "Batch Number =  " + rvLineItem.getBatchNumber()));
			}
		}
		return new RedirectResolution(ReconciliationVoucherAction.class);
	}


	@JsonHandler
	public Resolution saveRv() {
		Map dataMap = new HashMap();
		HealthkartResponse healthkartResponse = null;
		if (rvLineItems != null && (!rvLineItems.isEmpty())) {
			RvLineItem rvLineItem = rvLineItems.get(0);
			List<SkuGroup> skuGroupList = getValidSkuGroups(rvLineItem.getBatchNumber().trim());
			if (skuGroupList == null) {
				//remove the lines after testing datamap
				dataMap.put("rvLineItems", rvLineItems);
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Batch Number", dataMap);
				return new JsonResolution(healthkartResponse);
			}
			List<SkuItem> skuItemList = skuItemDao.getInStockSkuItem(skuGroupList.get(0));
			if (skuItemList.size() > rvLineItem.getQty()) {
				try {
					reconciliationVoucherService.newSave(rvLineItems, reconciliationVoucher);
					rvLineItem.setReconciliedQty(rvLineItem.getQty());
					dataMap.put("rvLineItems", rvLineItems);
					healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "successful", dataMap);
				} catch (Exception e) {
					healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage());
				}
			} else {
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Operation Failed::::Batch contains qty" + skuItemList.size() + " < " + rvLineItem.getQty() + "Qty");
			}
		} else {

		}
		return new JsonResolution(healthkartResponse);
	}


	public Resolution create() {
		User loggedOnUser = null;
		if (getPrincipal() != null) {
			loggedOnUser = getUserService().getUserById(getPrincipal().getId());
		}
		if (reconciliationVoucher == null || reconciliationVoucher.getId() == null) {
			reconciliationVoucher.setCreateDate(new Date());
			reconciliationVoucher.setCreatedBy(loggedOnUser);
		}
		reconciliationVoucher = reconciliationVoucherService.save(reconciliationVoucher);
		return new ForwardResolution("/pages/admin/editReconciliationVoucher.jsp").addParameter("reconciliationVoucher", reconciliationVoucher.getId());
	}

	public Resolution view() {
		if (reconciliationVoucher != null) {
			logger.debug("reconciliationVoucher@Pre: " + reconciliationVoucher.getId());
		}
		return new ForwardResolution("/pages/admin/createReconciliationVoucher.jsp");
	}


	public Resolution getBatchDetails() {
		SkuGroup skuGroup = null;
		String msg = null;
		HealthkartResponse healthkartResponse = null;
		Map dataMap = new HashMap();
		try {
			List<SkuGroup> skuGroupList = getValidSkuGroups(batchNumber.trim());
			if (skuGroupList != null && skuGroupList.size() > 0) {
				dataMap.put("skuGroupList", skuGroupList);
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, " ", dataMap);
			} else {
				msg = "Eneterd Batch is not correct for row";
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, msg);
			}
			return new JsonResolution(healthkartResponse);

		} catch (Exception e) {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage());
			return new JsonResolution(healthkartResponse);
		}

	}

	public List<SkuGroup> getValidSkuGroups(String batchNumber) {
		SkuGroup skuGroup = null;
		List<SkuGroup> skuGroupList = null;
		skuGroup = skuGroupDao.getSkuGroup(batchNumber.trim());
		if (skuGroup != null) {
			skuGroupList = new ArrayList<SkuGroup>();
			skuGroupList.add(skuGroup);
		} else {
			skuGroupList = skuGroupDao.getSkuGroupByBatch(batchNumber.trim());
		}
		return skuGroupList;

	}

	public ReconciliationVoucher getReconciliationVoucher() {
		return reconciliationVoucher;
	}

	public void setReconciliationVoucher(ReconciliationVoucher reconciliationVoucher) {
		this.reconciliationVoucher = reconciliationVoucher;
	}

	public List<ReconciliationVoucher> getReconciliationVouchers() {
		return reconciliationVouchers;
	}

	public void setReconciliationVouchers(List<ReconciliationVoucher> reconciliationVouchers) {
		this.reconciliationVouchers = reconciliationVouchers;
	}

	public List<RvLineItem> getRvLineItems() {
		return rvLineItems;
	}

	public void setRvLineItems(List<RvLineItem> rvLineItems) {
		this.rvLineItems = rvLineItems;
	}

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public int getPerPageDefault() {
		return defaultPerPage;
	}

	public int getPageCount() {
		return reconciliationVoucherPage == null ? 0 : reconciliationVoucherPage.getTotalPages();
	}

	public int getResultCount() {
		return reconciliationVoucherPage == null ? 0 : reconciliationVoucherPage.getTotalResults();
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public FileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}

	public ProductVariantService getProductVariantService() {
		return productVariantService;
	}

	public void setProductVariantService(ProductVariantService productVariantService) {
		this.productVariantService = productVariantService;
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("createDate");
		params.add("userLogin");
		params.add("warehouse");
		return params;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public RvLineItem getRvLineItem() {
		return rvLineItem;
	}

	public void setRvLineItem(RvLineItem rvLineItem) {
		this.rvLineItem = rvLineItem;
	}

	public Integer getAskedQty() {
		return askedQty;
	}

	public void setAskedQty(Integer askedQty) {
		this.askedQty = askedQty;
	}
}