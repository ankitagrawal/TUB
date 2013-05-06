package com.hk.web.action.admin.inventory;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.dao.inventory.PurchaseInvoiceDao;
import com.hk.admin.pact.service.accounting.PaymentHistoryService;
import com.hk.admin.pact.service.accounting.ProcurementService;
import com.hk.admin.pact.service.accounting.PurchaseInvoiceService;
import com.hk.admin.pact.service.rtv.ExtraInventoryLineItemService;
import com.hk.admin.pact.service.rtv.ExtraInventoryService;
import com.hk.admin.pact.service.rtv.RtvNoteService;
import com.hk.constants.core.EnumPermission;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumPILineItemType;
import com.hk.constants.inventory.EnumPurchaseInvoiceStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceLineItem;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Rahul Date: Feb 15, 2012 Time: 8:26:07 AM To change this template use File | Settings |
 * File Templates.
 */

@Secure(hasAnyPermissions = {PermissionConstants.PURCHASE_INVOICE_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class PurchaseInvoiceAction extends BasePaginatedAction {
	@Autowired
	PurchaseInvoiceDao purchaseInvoiceDao;
	@Autowired
	ProductVariantDao productVariantDao;
	@Autowired
	GoodsReceivedNoteDao goodsReceivedNoteDao;
	@Autowired
	SkuService skuService;
	@Autowired
	UserService userService;
	@Autowired
	private ProductVariantService productVariantService;
	@Autowired
	private ProcurementService procurementService;
	@Autowired
	ExtraInventoryService extraInventoryService;
	@Autowired
	RtvNoteService rtvNoteService;
	@Autowired
	private PurchaseInvoiceService purchaseInvoiceService;
	@Autowired
	ExtraInventoryLineItemService extraInventoryLineItemService;
	@Autowired
	private PaymentHistoryService paymentHistoryService;

	private static Logger logger = Logger.getLogger(PurchaseInvoiceAction.class);

	private Integer defaultPerPage = 20;
	Page purchaseInvoicePage;

	private List<PurchaseInvoice> purchaseInvoiceList = new ArrayList<PurchaseInvoice>();
	private List<RtvNote> rtvList = new ArrayList<RtvNote>();
	private List<Long> rtvId = new ArrayList<Long>();
	private List<RtvNote> toImportRtvList = new ArrayList<RtvNote>();
	private PurchaseInvoice purchaseInvoice;
	private Boolean piHasRtv;
	private List<PurchaseInvoiceLineItem> purchaseInvoiceLineItems;
	private List<PurchaseInvoiceLineItem> purchaseInvoiceShortLineItems;
	private List<ExtraInventoryLineItem> extraInventoryLineItems = new ArrayList<ExtraInventoryLineItem>();;
	private Date startDate;
	private Date endDate;
	private String tinNumber;
	private String invoiceNumber;
	private String supplierName;
	private ProductVariant productVariant;
	private PurchaseInvoiceStatus purchaseInvoiceStatus;
	private User approvedBy;
	private User createdBy;
	private String productVariantId;
	private Boolean reconciled;
	private Warehouse warehouse;
	private Boolean saveEnabled = true;
	private Date grnDate;
	private Double shortTotalPayable;
	private Double rtvTotalPayable;
	private Long extraInventoryId;
	private Double piFinalPayable;

	@DefaultHandler
	public Resolution pre() {

		if (productVariant != null) {
			purchaseInvoiceList = getPurchaseInvoiceService().listPurchaseInvoiceWithProductVariant(productVariant);
		} else {
			purchaseInvoicePage = getPurchaseInvoiceService().searchPurchaseInvoice(purchaseInvoice, purchaseInvoiceStatus, createdBy, invoiceNumber, tinNumber, supplierName, getPageNo(),
					getPerPage(), reconciled, warehouse, startDate, endDate);
			purchaseInvoiceList = purchaseInvoicePage.getList();
		}
		// purchaseInvoiceList = purchaseInvoiceDao.listAll();

		/*
						 * purchaseInvoicePage = purchaseInvoiceDao.searchPurchaseInvoice(purchaseInvoice, purchaseInvoiceStatus,
						 * createdBy, invoiceNumber, tinNumber, supplierName, getPageNo(), getPerPage()); purchaseInvoiceList =
						 * purchaseInvoicePage.getList();
						 */

		return new ForwardResolution("/pages/admin/purchaseInvoiceList.jsp");
	}

	public Resolution view() {
		if (purchaseInvoice != null) {
			List<GoodsReceivedNote> grnList = purchaseInvoice.getGoodsReceivedNotes();
			List<Date> grnDateList = new ArrayList<Date>();
			
			boolean isLoggedInUserHasFinancePermission = userService.getLoggedInUser().hasPermission(EnumPermission.FINANCE_MANAGEMENT);

			if (isLoggedInUserHasFinancePermission) {
				saveEnabled = true;
			} else if (purchaseInvoice.getReconciled() == null) {
				saveEnabled = true;
			} else if (purchaseInvoice.getReconciled()) {
				saveEnabled = false;
			}
			for (GoodsReceivedNote grn : grnList) {
				grnDateList.add(grn.getGrnDate());
			}
			grnDate = Collections.min(grnDateList);
			if (purchaseInvoice != null) {
				// logger.debug("purchaseInvoice@view: " + purchaseInvoice.getId());
				// grnDto = grnManager.generateGRNDto(grn);
			}
			purchaseInvoiceShortLineItems = new ArrayList<PurchaseInvoiceLineItem>();
			purchaseInvoiceLineItems = new ArrayList<PurchaseInvoiceLineItem>();
			for(PurchaseInvoiceLineItem purchaseInvoiceLineItem : purchaseInvoice.getPurchaseInvoiceLineItems()){
				if(purchaseInvoiceLineItem.getPiLineItemType()!=null&&purchaseInvoiceLineItem.getPiLineItemType().getId().equals(EnumPILineItemType.Short.getId())){
					purchaseInvoiceShortLineItems.add(purchaseInvoiceLineItem);
				}
				else
					purchaseInvoiceLineItems.add(purchaseInvoiceLineItem);
			}
			
			//fetch all existing
			piHasRtv = Boolean.FALSE;
			if (purchaseInvoice.getRtvNotes() != null && purchaseInvoice.getRtvNotes().size() > 0) {
				piHasRtv = Boolean.TRUE;
				for (RtvNote rtvNote : purchaseInvoice.getRtvNotes()) {
					ExtraInventory extraInventory = rtvNote.getExtraInventory();
					List<ExtraInventoryLineItem> eiLineItems = extraInventoryLineItemService
							.getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
					if (eiLineItems != null && eiLineItems.size()!=0) {
						for (ExtraInventoryLineItem eiLineItem : eiLineItems) {
							if (eiLineItem.isRtvCreated()!=null && eiLineItem.isRtvCreated().equals(Boolean.TRUE)) {
								extraInventoryLineItems.add(eiLineItem);
							}
						}
					}
				}
			}
			
			//fetch all rtv list existing for the pi
			Set<RtvNote> rtvSet = new HashSet<RtvNote>();
			for (GoodsReceivedNote grn : purchaseInvoice.getGoodsReceivedNotes()) {
				PurchaseOrder po = grn.getPurchaseOrder();
				ExtraInventory ei = extraInventoryService.getExtraInventoryByPoId(po.getId());
				if(ei!=null){
				RtvNote rtv = rtvNoteService.getRtvNoteByExtraInventory(ei.getId());
				if (rtv != null) {
					rtvSet.add(rtv);
					}
				}
			}
			rtvList.addAll(rtvSet);
			
			//show the ones which are not imported(intersection of the two)
			for (RtvNote note : rtvList) {
	            if(!purchaseInvoice.getRtvNotes().contains(note)) {
	                toImportRtvList.add(note);
	            }
	        }
			
			return new ForwardResolution("/pages/admin/purchaseInvoice.jsp");
		} else {
			addRedirectAlertMessage(new SimpleMessage("Incorrect purchase Invoice Id."));
			return new ForwardResolution("/pages/admin/purchaseInvoice.jsp");
		}
	}
	
	public Resolution importRtv(){
		
		//fetch all rtv notes
		
		Set<RtvNote> rtvSet = new HashSet<RtvNote>();
		for(GoodsReceivedNote grn : purchaseInvoice.getGoodsReceivedNotes()){
			PurchaseOrder po = grn.getPurchaseOrder();
			ExtraInventory ei = extraInventoryService.getExtraInventoryByPoId(po.getId());
			RtvNote rtv = rtvNoteService.getRtvNoteByExtraInventory(ei.getId());
			if(rtv!=null){
				rtvSet.add(rtv);
			}
		}
		rtvList.addAll(rtvSet);
		
		//choose the ones which has been selected
		List<RtvNote> piHasRtvList = new ArrayList<RtvNote>();
		if(rtvId!=null && !rtvId.isEmpty()){
			for(Long id : rtvId){
				for(RtvNote rtv:rtvList){
					if(rtv.getId().equals(id)){
						piHasRtvList.add(rtv);
					}
				}
			}
		}
		
		//fetch the extraInventoryLineitem corresponding to all the RTV Notes 
		if(purchaseInvoice.getRtvNotes()!=null && purchaseInvoice.getRtvNotes().size()>0){
			piHasRtvList.addAll(purchaseInvoice.getRtvNotes());
		}
		Double piRtvAmount  =0.0;
		for (RtvNote rtvNote : piHasRtvList) {
			ExtraInventory extraInventory = rtvNote.getExtraInventory();
			List<ExtraInventoryLineItem> eiLineItems = extraInventoryLineItemService
					.getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
			if (eiLineItems != null) {
				for (ExtraInventoryLineItem eiLineItem : eiLineItems) {
					if (eiLineItem.isRtvCreated()!=null && eiLineItem.isRtvCreated().equals(Boolean.TRUE)) {
						extraInventoryLineItems.add(eiLineItem);
						piRtvAmount+=eiLineItem.getPayableAmount();
					}
				}
			}
		}
		purchaseInvoice.setRtvNotes(piHasRtvList);
		purchaseInvoice.setPiRtvAmount(piRtvAmount);
		getPurchaseInvoiceService().save(purchaseInvoice);
		
		return new RedirectResolution(PurchaseInvoiceAction.class).addParameter("view").addParameter("purchaseInvoice", purchaseInvoice.getId());
	}

	public Resolution saveRtv(){
		
		
		for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItems)
		{
			ExtraInventory extraInventory = extraInventoryService.getExtraInventoryById(extraInventoryId);
			extraInventoryLineItem.setExtraInventory(extraInventory);
			extraInventoryLineItemService.save(extraInventoryLineItem);
		}
		Double piRtvAmount = purchaseInvoice.getFinalPayableAmount()+rtvTotalPayable;
		purchaseInvoice.setPiRtvAmount(piRtvAmount);
		getPurchaseInvoiceService().save(purchaseInvoice);
		return new RedirectResolution(PurchaseInvoiceAction.class).addParameter("view").addParameter("purchaseInvoice", purchaseInvoice.getId());
	}
	public Resolution paymentDetails() {

		return new ForwardResolution("/pages/admin/purchaseInvoicePaymentDetails.jsp");
	}

	public Resolution updateStatusAndPaymentDetails() {
		Resolution sourceResolution = performPISanityChecks("paymentDetails", purchaseInvoice);
		if (sourceResolution != null) {
			return sourceResolution;
		}
		getPurchaseInvoiceService().save(purchaseInvoice);
		addRedirectAlertMessage(new SimpleMessage("Changes saved."));
		return new RedirectResolution(PurchaseInvoiceAction.class);
	}

	public Resolution save() {
		if (purchaseInvoice != null && purchaseInvoice.getId() != null) {
			logger.debug("purchaseInvoiceLineItems@Save: " + purchaseInvoiceLineItems.size());

			if (StringUtils.isBlank(purchaseInvoice.getInvoiceNumber()) || purchaseInvoice.getInvoiceDate() == null) {
				addRedirectAlertMessage(new SimpleMessage("Invoice date and number are mandatory."));
				return new RedirectResolution(PurchaseInvoiceAction.class).addParameter("view").addParameter("purchaseInvoice", purchaseInvoice.getId());
			}

			Resolution sourceResolution = performPISanityChecks("view", purchaseInvoice);
			if (sourceResolution != null) {
				return sourceResolution;
			}
			for (PurchaseInvoiceLineItem purchaseInvoiceLineItem : purchaseInvoiceLineItems) {
				if (purchaseInvoiceLineItem.getQty() != null && purchaseInvoiceLineItem.getQty() == 0 && purchaseInvoiceLineItem.getId() != null) {
					purchaseInvoiceDao.delete(purchaseInvoiceLineItem);
				} else if (purchaseInvoiceLineItem.getQty() > 0) {
					purchaseInvoiceLineItem.setPurchaseInvoice(purchaseInvoice);
					Sku sku = purchaseInvoiceLineItem.getSku();
					//Sku sku = skuService.getSKU(purchaseInvoiceLineItem.getProductVariant(), purchaseInvoice.getWarehouse());
					if (sku == null) {
						sku = skuService.getSKU(purchaseInvoiceLineItem.getProductVariant(), purchaseInvoice.getWarehouse());
						purchaseInvoiceLineItem.setSku(sku);
					}
					skuService.saveSku(sku);
					purchaseInvoiceLineItem.setPiLineItemType(EnumPILineItemType.Normal.asPiLineItemType());
					purchaseInvoiceLineItem = (PurchaseInvoiceLineItem) purchaseInvoiceDao.save(purchaseInvoiceLineItem);
				}
				productVariant = purchaseInvoiceLineItem.getSku().getProductVariant();
				productVariant = productVariantDao.save(productVariant);
			}
			if (purchaseInvoice.getReconciled() != null) {
				if (purchaseInvoice.getReconciled() && purchaseInvoice.getReconcilationDate() == null) {
					purchaseInvoice.setReconcilationDate(new Date());
				}
			}

			if(purchaseInvoice.getPiRtvAmount()!=null){
				Double piRtvDiff = purchaseInvoice.getFinalPayableAmount()-piFinalPayable;
				Double piRtv = purchaseInvoice.getPiRtvAmount();
				piRtv+=piRtvDiff;
				purchaseInvoice.setPiRtvAmount(piRtv);
			}
			
			getPurchaseInvoiceService().save(purchaseInvoice);
		}
		addRedirectAlertMessage(new SimpleMessage("Changes saved."));
		return new RedirectResolution(PurchaseInvoiceAction.class).addParameter("view").addParameter("purchaseInvoice", purchaseInvoice.getId());
	}

	public Resolution delete() {
		Boolean deleteStatus = false;
		if (purchaseInvoice != null && purchaseInvoice.getId() != null) {
			deleteStatus = procurementService.deletePurchaseInvoice(purchaseInvoice);
		}
		if (deleteStatus == false) {
			addRedirectAlertMessage(new SimpleMessage("Unable to delete purchase invoice, please contact admin."));
		} else {
			addRedirectAlertMessage(new SimpleMessage("Purchase Invoice Deleted."));
		}
		return new RedirectResolution(PurchaseInvoiceAction.class);
	}
	
	public Resolution saveShortLineItems() {
		if (purchaseInvoice != null && purchaseInvoice.getId() != null) {
			logger.debug("purchaseInvoiceLineItems@Save: " + purchaseInvoiceShortLineItems.size());

			if (StringUtils.isBlank(purchaseInvoice.getInvoiceNumber()) || purchaseInvoice.getInvoiceDate() == null) {
				addRedirectAlertMessage(new SimpleMessage("Invoice date and number are mandatory."));
				return new RedirectResolution(PurchaseInvoiceAction.class).addParameter("view").addParameter("purchaseInvoice", purchaseInvoice.getId());
			}

			Resolution sourceResolution = performPISanityChecks("view", purchaseInvoice);
			if (sourceResolution != null) {
				return sourceResolution;
			}
			for (PurchaseInvoiceLineItem purchaseInvoiceLineItem : purchaseInvoiceShortLineItems) {
				if (purchaseInvoiceLineItem.getQty() != null && purchaseInvoiceLineItem.getQty() == 0 && purchaseInvoiceLineItem.getId() != null) {
					purchaseInvoiceDao.delete(purchaseInvoiceLineItem);
				} else if (purchaseInvoiceLineItem.getQty() > 0) {
					purchaseInvoiceLineItem.setPurchaseInvoice(purchaseInvoice);
					Sku sku = skuService.getSKU(purchaseInvoiceLineItem.getProductVariant(), purchaseInvoice.getWarehouse());
					purchaseInvoiceLineItem.setSku(sku);
					skuService.saveSku(sku);
					purchaseInvoiceLineItem.setPiLineItemType(EnumPILineItemType.Short.asPiLineItemType());
					purchaseInvoiceLineItem = (PurchaseInvoiceLineItem) purchaseInvoiceDao.save(purchaseInvoiceLineItem);
				}
			}
			purchaseInvoice.setShortAmount(shortTotalPayable);

			getPurchaseInvoiceService().save(purchaseInvoice);
		}
		addRedirectAlertMessage(new SimpleMessage("Changes saved."));
		return new RedirectResolution(PurchaseInvoiceAction.class).addParameter("view").addParameter("purchaseInvoice", purchaseInvoice.getId());
	}
	
	public Resolution close(){
		return new RedirectResolution(PurchaseInvoiceAction.class);
	}
	
	
	@SuppressWarnings("unchecked")
	public Resolution getPVDetails() {
		Map dataMap = new HashMap();
		Warehouse warehouse = userService.getWarehouseForLoggedInUser();
		ProductVariant pv = productVariantService.getVariantById(productVariantId);
		if (pv != null) {
			dataMap.put("variant", pv);
			dataMap.put("product", pv.getProduct().getName());
			dataMap.put("options", pv.getOptionsCommaSeparated());
			if (warehouse != null) {
				Sku sku = skuService.getSKU(pv, warehouse);
				dataMap.put("taxId", sku.getTax());
			}
			HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Product Variant", dataMap);
			noCache();
			return new JsonResolution(healthkartResponse);
		}
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Product VariantID", dataMap);
		noCache();
		return new JsonResolution(healthkartResponse);
	}

	/**
	 * Returns Null when all the sanity checks pass otherwise returns the resolution with error message.
	 *
	 * @param redirectResolution
	 * @param purchaseInvoice
	 * @return
	 */
	private Resolution performPISanityChecks(String redirectResolution, PurchaseInvoice purchaseInvoice) {
		if (purchaseInvoice.getPaymentDate() != null &&
				!(purchaseInvoice.getPurchaseInvoiceStatus().getId().equals(EnumPurchaseInvoiceStatus.PurchaseInvoiceSettled.getId()))) {
			addRedirectAlertMessage(new SimpleMessage("Please mark PI status " + EnumPurchaseInvoiceStatus.PurchaseInvoiceSettled.getName() + " as payment date is mentioned."));
			return new RedirectResolution(PurchaseInvoiceAction.class).addParameter(redirectResolution).addParameter("purchaseInvoice", purchaseInvoice.getId());
		}

		if (purchaseInvoice.getPaymentDate() == null &&
				(purchaseInvoice.getPurchaseInvoiceStatus().getId().equals(EnumPurchaseInvoiceStatus.PurchaseInvoiceSettled.getId()))) {
			addRedirectAlertMessage(new SimpleMessage("Payment date cannot be null when status is " + EnumPurchaseInvoiceStatus.PurchaseInvoiceSettled.getName()));
			return new RedirectResolution(PurchaseInvoiceAction.class).addParameter(redirectResolution).addParameter("purchaseInvoice", purchaseInvoice.getId());
		}

		if (purchaseInvoice.getReconciled() != null && purchaseInvoice.getReconciled() &&
				purchaseInvoice.getPurchaseInvoiceStatus().getId().equals(EnumPurchaseInvoiceStatus.PurchaseInvoiceGenerated.getId())) {
			addRedirectAlertMessage(new SimpleMessage("Since PI is reconciled, please change the status from " + EnumPurchaseInvoiceStatus.PurchaseInvoiceGenerated.getName() + " to some other status"));
			return new RedirectResolution(PurchaseInvoiceAction.class).addParameter(redirectResolution).addParameter("purchaseInvoice", purchaseInvoice.getId());
		}

		Double outstandingAmount = paymentHistoryService.getOutstandingAmountForPurchaseInvoice(purchaseInvoice);
		if (outstandingAmount.doubleValue() > 0.00
				&& purchaseInvoice.getPurchaseInvoiceStatus().getId().equals(EnumPurchaseInvoiceStatus.PurchaseInvoiceSettled.getId())) {
			addRedirectAlertMessage(new SimpleMessage("There is an outstanding amount of " + outstandingAmount + ". Please clear the same in PI's payment history before closing it."));
			return new RedirectResolution(PurchaseInvoiceAction.class).addParameter(redirectResolution).addParameter("purchaseInvoice", purchaseInvoice.getId());
		}

		/* Needs to go live once back log is resolved.

		if(purchaseInvoice.getPaymentDate() != null){
			if(purchaseInvoice.getPaymentDate().compareTo(DateUtils.getDateMinusDays(8)) < 0){
				addRedirectAlertMessage(new SimpleMessage("Payment date cannot be less than 7 days ago"));
				return new RedirectResolution(PurchaseInvoiceAction.class).addParameter(redirectResolution).addParameter("purchaseInvoice", purchaseInvoice.getId());
			}

			if(purchaseInvoice.getPaymentDate().compareTo(new Date()) > 0){
				addRedirectAlertMessage(new SimpleMessage("You cannot add a future date"));
				return new RedirectResolution(PurchaseInvoiceAction.class).addParameter(redirectResolution).addParameter("purchaseInvoice", purchaseInvoice.getId());
			}
		}*/


		return null;
	}

	public int getPerPageDefault() {
		return defaultPerPage; // To change body of implemented methods use File | Settings | File Templates.
	}

	public int getPageCount() {
		return purchaseInvoicePage == null ? 0 : purchaseInvoicePage.getTotalPages();
	}

	public int getResultCount() {
		return purchaseInvoicePage == null ? 0 : purchaseInvoicePage.getTotalResults();
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("productVariant");
		params.add("tinNumber");
		params.add("supplierName");
		params.add("createdBy");
		params.add("invoiceNumber");
		params.add("purchaseInvoiceStatus");
		params.add("createdBy");
		params.add("purchaseInvoice");
		params.add("warehouse");
		params.add("reconciled");
		params.add("startDate");
		params.add("endDate");
		return params;
	}

	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public PurchaseInvoice getPurchaseInvoice() {
		return purchaseInvoice;
	}

	public void setPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		this.purchaseInvoice = purchaseInvoice;
	}

	public List<PurchaseInvoice> getPurchaseInvoiceList() {
		return purchaseInvoiceList;
	}

	public void setPurchaseInvoiceList(List<PurchaseInvoice> purchaseInvoiceList) {
		this.purchaseInvoiceList = purchaseInvoiceList;
	}

	public PurchaseInvoiceStatus getPurchaseInvoiceStatus() {
		return purchaseInvoiceStatus;
	}

	public void setPurchaseInvoiceStatus(PurchaseInvoiceStatus purchaseInvoiceStatus) {
		this.purchaseInvoiceStatus = purchaseInvoiceStatus;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public List<PurchaseInvoiceLineItem> getPurchaseInvoiceLineItems() {
		return purchaseInvoiceLineItems;
	}

	public void setPurchaseInvoiceLineItems(List<PurchaseInvoiceLineItem> purchaseInvoiceLineItems) {
		this.purchaseInvoiceLineItems = purchaseInvoiceLineItems;
	}

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public Boolean getReconciled() {
		return this.reconciled;
	}

	public void setReconciled(Boolean reconciled) {
		this.reconciled = reconciled;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Boolean isSaveEnabled() {
		return saveEnabled;
	}

	public Boolean getSaveEnabled() {
		return saveEnabled;
	}

	public void setSaveEnabled(Boolean saveEnabled) {
		this.saveEnabled = saveEnabled;
	}

	public Date getGrnDate() {
		return grnDate;
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

	public PurchaseInvoiceService getPurchaseInvoiceService() {
		return purchaseInvoiceService;
	}
	
	public List<PurchaseInvoiceLineItem> getPurchaseInvoiceShortLineItems() {
		return purchaseInvoiceShortLineItems;
	}

	public void setPurchaseInvoiceShortLineItems(List<PurchaseInvoiceLineItem> purchaseInvoiceShortLineItems) {
		this.purchaseInvoiceShortLineItems = purchaseInvoiceShortLineItems;
	}
	
	public List<ExtraInventoryLineItem> getExtraInventoryLineItems() {
		return extraInventoryLineItems;
	}

	public void setExtraInventoryLineItems(List<ExtraInventoryLineItem> extraInventoryLineItems) {
		this.extraInventoryLineItems = extraInventoryLineItems;
	}
	
	public List<RtvNote> getRtvList() {
		return rtvList;
	}

	public void setRtvList(List<RtvNote> rtvList) {
		this.rtvList = rtvList;
	}
	
	public List<Long> getRtvId() {
		return rtvId;
	}

	public void setRtvId(List<Long> rtvId) {
		this.rtvId = rtvId;
	}

	public Boolean getPiHasRtv() {
		return piHasRtv;
	}

	public void setPiHasRtv(Boolean piHasRtv) {
		this.piHasRtv = piHasRtv;
	}

	public List<RtvNote> getToImportRtvList() {
		return toImportRtvList;
	}

	public void setToImportRvList(List<RtvNote> toImportRtvList) {
		this.toImportRtvList = toImportRtvList;
	}
	
	public Double getShortTotalPayable() {
		return shortTotalPayable;
	}

	public void setShortTotalPayable(Double shortTotalPayable) {
		this.shortTotalPayable = shortTotalPayable;
	}

	public Double getRtvTotalPayable() {
		return rtvTotalPayable;
	}

	public void setRtvTotalPayable(Double rtvTotalPayable) {
		this.rtvTotalPayable = rtvTotalPayable;
	}

	public Long getExtraInventoryId() {
		return extraInventoryId;
	}

	public void setExtraInventoryId(Long extraInventoryId) {
		this.extraInventoryId = extraInventoryId;
	}

	public Double getPiFinalPayable() {
		return piFinalPayable;
	}

	public void setPiFinalPayable(Double piFinalPayable) {
		this.piFinalPayable = piFinalPayable;
	}

	@Validate(converter = CustomDateTypeConvertor.class)
	public void setGrnDate(Date grnDate) {
		this.grnDate = grnDate;
	}
}
