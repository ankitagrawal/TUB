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
import com.hk.domain.sku.Sku;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.inventory.SkuGroupService;
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
	SkuGroupService skuGroupService;

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
	private String errorMessage;

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


	public Resolution subtractReconciliation() {
		if (reconciliationVoucher != null) {
			rvLineItems=reconciliationVoucher.getRvLineItems();
			logger.debug("reconciliationVoucher@Pre: " + reconciliationVoucher.getId());
		}

		return new ForwardResolution("/pages/admin/createReconciliationVoucher.jsp");
	}


	public Resolution addReconciliation() {
		if (reconciliationVoucher != null) {
			rvLineItems=reconciliationVoucher.getRvLineItems();
			logger.debug("reconciliationVoucher@Pre: " + reconciliationVoucher.getId());
		}

		return new ForwardResolution("/pages/admin/reconciliationVoucher.jsp");
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
		Map<RvLineItem,String>  rvNotSavedMap = new HashMap<RvLineItem,String>();
		List<RvLineItem> rvLineItemsCorrectSize = new ArrayList<RvLineItem>();
		List<RvLineItem> rvLineItemsFinal = new ArrayList<RvLineItem>();
		if (rvLineItems != null && (!(rvLineItems.isEmpty()))) {
			for (RvLineItem rvLineItem : rvLineItems) {
				if (rvLineItem != null) {
					rvLineItemsCorrectSize.add(rvLineItem);
				}
			}
			for (RvLineItem rvLineItem : rvLineItemsCorrectSize) {
				if (rvLineItem.getQty() == null) {
					rvNotSavedMap.put(rvLineItem, "Quantity is Not Entered");
					continue;
				}
				if (rvLineItem.getProductVariant() == null) {
					rvNotSavedMap.put(rvLineItem, "Invalid Product Variant");
					continue;
				}
				if(rvLineItem.getSku() == null){
				Sku sku = skuService.getSKU(rvLineItem.getProductVariant(), reconciliationVoucher.getWarehouse());
				if (sku == null) {
					rvNotSavedMap.put(rvLineItem, "Sku does not exist. Contact Category Manager");
					continue;
				}
				rvLineItem.setSku(sku);
				}
			if (rvLineItemsFinal.contains(rvLineItem)) {
				int index = rvLineItemsFinal.indexOf(rvLineItem);
				RvLineItem rvLineItemO = rvLineItemsFinal.get(index);
				rvLineItemO.setQty(rvLineItemO.getQty() + rvLineItem.getQty());
			}
			else{
				rvLineItemsFinal.add(rvLineItem);
			}
		}
		reconciliationVoucherService.save(rvLineItemsFinal, reconciliationVoucher);
		if (rvNotSavedMap.size() > 0) {
			addRedirectAlertMessage(new SimpleMessage("Below RV Line items are not saved"));
			for (RvLineItem rvLineItem : rvNotSavedMap.keySet()) {
				String errorMessage =  rvNotSavedMap.get(rvLineItem);
				addRedirectAlertMessage(new SimpleMessage("Product Variant =  " + rvLineItem.getProductVariant() + " REASON::::  "  +errorMessage));
			}
		}
		}
		return new RedirectResolution("/pages/admin/editReconciliationVoucher.jsp").addParameter("reconciliationVoucher", reconciliationVoucher.getId());
	}


	@JsonHandler
	public Resolution saveAndReconcileRv() {
		Map dataMap = new HashMap();
		HealthkartResponse healthkartResponse ;
		if (rvLineItems != null && (!rvLineItems.isEmpty())) {
			for(RvLineItem rvLineItemTemp:rvLineItems){
				if(rvLineItemTemp != null){
					rvLineItem = rvLineItemTemp;
					break;
				}
			}

			try {
				if (rvLineItem.getProductVariant() != null) {
					if(rvLineItem.getQty() < rvLineItem.getReconciliedQty()){
					healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Qty cannot be Less  than reconciliation Qty");
					return new JsonResolution(healthkartResponse);
					}
					ProductVariant productVariant = rvLineItem.getProductVariant();
					List<SkuItem> skuItemList = getInStockSkuItemsForEnteredBatch(rvLineItem.getBatchNumber().trim(), productVariant.getId());
					if (skuItemList != null && skuItemList.size() > 0) {
						int targetReconciliedQty = rvLineItem.getQty().intValue() - rvLineItem.getReconciliedQty().intValue();
						if ( skuItemList.size() >= targetReconciliedQty) {
							Sku sku = skuService.getSKU(productVariant, getUserService().getWarehouseForLoggedInUser());
							rvLineItem.setSku(sku);
							RvLineItem rvLineItemSaved = reconciliationVoucherService.reconcile(rvLineItem, reconciliationVoucher,skuItemList);
							if(rvLineItemSaved == null){
							healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "EXCEPTION:: Reconciliation Failed", dataMap);
							}
							else{
							dataMap.put("rvLineItem", rvLineItemSaved);
							healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "successful", dataMap);
							}

						} else {
							String msg = "Operation Failed::::Batch contains qty  " + skuItemList.size() + " < " + rvLineItem.getQty() + "Qty";
							healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, msg );
						}
					} else {
						healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, errorMessage);
					}
				} else {
					healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Product Variant");
				}

			} catch (Exception e) {
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage());
			}

		} else {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "EXCEPTION ::::RV Line Item Null");
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



	@JsonHandler
	public Resolution getBatchDetails() {
		HealthkartResponse healthkartResponse ;
		try {
			List<SkuItem> skuItemList = getInStockSkuItemsForEnteredBatch(batchNumber.trim(),productVariantId);
			if (skuItemList != null && skuItemList.size() > 0) {
				if(skuItemList.size() < askedQty){
				String msg ="Batch Contains  " + skuItemList.size() + " quantity only  , Enter Qty values less than :: " +skuItemList.size();
				 healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, msg);
				}
				else{
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "");
				}
			} else {
				healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, errorMessage);
			}
			return new JsonResolution(healthkartResponse);

		} catch (Exception e) {
			healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage());
			return new JsonResolution(healthkartResponse);
		}

	}

	private  List<SkuItem> getInStockSkuItemsForEnteredBatch(String batchNumber,String productVariantId) {
		List<SkuItem> skuItemList = null;
		errorMessage = "";
		ProductVariant productVariant = productVariantService.getVariantById(productVariantId);
		if (productVariant != null) {
			Sku sku = skuService.getSKU(productVariant, getUserService().getWarehouseForLoggedInUser());
			if (sku != null) {
				skuItemList = skuGroupService.getInStockSkuItemByBarcode(batchNumber, sku);
				if (skuItemList != null && skuItemList.size() > 0) {
					return skuItemList;
				}
				else {
					 skuItemList = skuGroupService.getInStockSkuItemByBatch(batchNumber,sku);
					if (skuItemList != null && skuItemList.size() > 0) {
						return skuItemList;
					} else {
						errorMessage = "Invalid Batch OR Batch is Empty";
					}
				}
			} else {
				errorMessage = "Sku of Product varinat  does not exist";
			}
		} else {
			errorMessage = "Invalid Product varinat Id";
		}
		return skuItemList;
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