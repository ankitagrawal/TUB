package com.hk.web.action.admin.inventory;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.dto.inventory.GRNDto;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.manager.GRNManager;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.GrnLineItemService;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.dao.inventory.GrnLineItemDao;
import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.admin.pact.dao.inventory.PurchaseInvoiceDao;
import com.hk.admin.pact.service.inventory.PoLineItemService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.rtv.ExtraInventoryService;
import com.hk.admin.pact.service.rtv.RtvNoteService;
import com.hk.admin.util.TaxUtil;
import com.hk.constants.core.EnumSurcharge;
import com.hk.constants.core.EnumTax;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.StateList;
import com.hk.constants.inventory.EnumGrnStatus;
import com.hk.constants.inventory.EnumPurchaseInvoiceStatus;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Tax;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.GrnStatus;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceLineItem;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.TaxComponent;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.util.XslGenerator;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class GRNAction extends BasePaginatedAction {

	private static Logger logger = Logger.getLogger(GRNAction.class);
	@Autowired
	private GRNManager grnManager;
	@Autowired
	private GoodsReceivedNoteDao goodsReceivedNoteDao;
	@Autowired
	private GrnLineItemDao grnLineItemDao;
	@Autowired
	private ProductVariantService productVariantService;
	@Autowired
	private UserService userService;
	@Autowired
	private PurchaseInvoiceDao purchaseInvoiceDao;
	@Autowired
	private SupplierDao supplierDao;
	@Autowired
	private SkuService skuService;
	@Autowired
	private PoLineItemService poLineItemService;
	@Autowired
	private PurchaseOrderService purchaseOrderService;
    @Autowired
    GrnLineItemService grnLineItemService;
    @Autowired
    AdminInventoryService adminInventoryService;
    @Autowired
    InventoryService inventoryService;
    @Autowired 
    private ExtraInventoryService extraInventoryService;
    @Autowired
	PoLineItemDao poLineItemDao;
    @Autowired
    RtvNoteService rtvNoteService;

	@Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
	String adminDownloads;

	@Autowired
	XslGenerator xslGenerator;
	@Autowired
	private AdminEmailManager                 adminEmailManager;

	private File xlsFile;
	Page grnPage;
	Long grnStatusValue;
	private List<GoodsReceivedNote> grnList = new ArrayList<GoodsReceivedNote>();
	private List<GoodsReceivedNote> grnListForPurchaseInvoice = new ArrayList<GoodsReceivedNote>();
	private GoodsReceivedNote grn;
	private List<GrnLineItem> grnLineItems = new ArrayList<GrnLineItem>();
	private Date startDate;
	private Date endDate;
	private String tinNumber;
	private String invoiceNumber;
	private String supplierName;
	private Boolean reconciled;
	private Warehouse warehouse;
	public GRNDto grnDto;
	private ProductVariant productVariant;
	private Sku sku;
	public GrnStatus grnStatus;
	public Double surcharge;
	private Map<Sku, Boolean> skuIsNew = new HashMap<Sku, Boolean>();
	private Integer defaultPerPage = 20;
    private GrnLineItem grnLineItem;
    private long saveValue;

	@DefaultHandler
	public Resolution pre() {
		if (productVariant != null) {
			grnList = goodsReceivedNoteDao.listGRNsWithProductVariant(productVariant);
		} else {
			if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
				warehouse = getPrincipalUser().getSelectedWarehouse();
			}
			grnPage = goodsReceivedNoteDao.searchGRN(grn, grnStatus, invoiceNumber, tinNumber, supplierName, reconciled, warehouse, getPageNo(), getPerPage());
			grnList = grnPage.getList();
		}
		return new ForwardResolution("/pages/admin/grnList.jsp");
	}

	public Resolution generateExcelReport() {
		if (productVariant != null) {
			grnList = goodsReceivedNoteDao.listGRNsWithProductVariant(productVariant);
		} else {
			if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
				warehouse = getPrincipalUser().getSelectedWarehouse();
			}
			grnList = goodsReceivedNoteDao.searchGRN(grn, grnStatus, invoiceNumber, tinNumber, supplierName, reconciled, warehouse);
		}
		if (grnList != null) {
			xlsFile = new File(adminDownloads + "/reports/GRNList.xls");
			xlsFile = xslGenerator.generateGRNListExcel(xlsFile, grnList);

			return new HTTPResponseResolution();
		}
		return new RedirectResolution(GRNAction.class);

	}


	public Resolution print() {
		logger.debug("grn: " + grn);
		grnDto = grnManager.generateGRNDto(grn);
		return new ForwardResolution("/pages/admin/grnPrintView.jsp");
	}

	public Resolution view() {
		if (grn != null) {
			logger.debug("grn@view: " + grn.getId());
			grnDto = grnManager.generateGRNDto(grn);
			for (GrnLineItem grnlineitem : grn.getGrnLineItems()) {
				List<GrnLineItem> grnLineItemsList = grnLineItemDao.getAllGrnLineItemBySku(grnlineitem.getSku());
				if (grnLineItemsList != null && grnLineItemsList.size() == 1) {
					skuIsNew.put(grnlineitem.getSku(), true);
				}
			}

		}
		return new ForwardResolution("/pages/admin/grn.jsp");
	}

	public Resolution save() {
		if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
			warehouse = getPrincipalUser().getSelectedWarehouse();
		}
		if (grn != null && grn.getId() != null) {
			logger.debug("grnLineItems@Save: " + grnLineItems.size());

			if (StringUtils.isBlank(grn.getInvoiceNumber()) || StringUtils.equals(grn.getInvoiceNumber(), "-") || grn.getInvoiceDate() == null) {
				addRedirectAlertMessage(new SimpleMessage("Invoice date and number are mandatory."));
				return new RedirectResolution(GRNAction.class).addParameter("view").addParameter("grn", grn.getId());
			}

			double overallDiscount = 0;
			if (grn.getPurchaseOrder().getDiscount() != null && grn.getPurchaseOrder().getPayable() != null && grn.getPurchaseOrder().getPayable() > 0 && grn.getPayable() != null) {
				overallDiscount = (grn.getPurchaseOrder().getDiscount() / grn.getPurchaseOrder().getPayable()) * grn.getPayable();
			}
			grn.setDiscount(overallDiscount);

			double discountRatio = 0;
			if (grn.getPayable() != null && grn.getPayable() > 0 && grn.getDiscount() != null) {
				discountRatio = grn.getDiscount() / grn.getPayable();
			}

			for (GrnLineItem grnLineItem : grnLineItems) {
				//setting sku when adding new grn line item
				if(grnLineItem.getMrp().longValue() < grnLineItem.getCostPrice().longValue()){
					addRedirectAlertMessage(new SimpleMessage("MRP cannot be less than cost price for item "+grnLineItem.getSku().getProductVariant().getId()));
					return new RedirectResolution(GRNAction.class, "view").addParameter("grn", grn.getId());
				}

				if(grnLineItem.getCheckedInQty() != null && grnLineItem.getCheckedInQty() > 0 && grnLineItem.getQty() < grnLineItem.getCheckedInQty()){
					addRedirectAlertMessage(new SimpleMessage("GRN qty cannot be less than checked in quantity for item "+grnLineItem.getSku().getProductVariant().getId()));
					return new RedirectResolution(GRNAction.class, "view").addParameter("grn", grn.getId());
				}

				if (grnLineItem.getSku() == null && grnLineItem.getProductVariant() != null) {
					grnLineItem.setSku(skuService.getSKU(grnLineItem.getProductVariant(), warehouse));
				}
				
				if(saveValue == 2){
					if (grnLineItem.getQty() != null && grnLineItem.getQty() == 0 && grnLineItem.getId() != null &&
							(grnLineItem.getCheckedInQty() == null || grnLineItem.getCheckedInQty() == 0) ) {
						grnLineItemDao.delete(grnLineItem);
				}
				
				} else{
					if (grnLineItem.getPayableAmount() != null) {
						if(grnLineItem.getQty()>0){
						grnLineItem.setProcurementPrice((grnLineItem.getPayableAmount() / grnLineItem.getQty()) - (grnLineItem.getPayableAmount() / grnLineItem.getQty() * discountRatio));
						}
						else
							grnLineItem.setProcurementPrice(0.0);
					}

					if (grnLineItem.getId() != null) {
						GrnLineItem grnLineItemInDB = grnLineItemDao.get(GrnLineItem.class, grnLineItem.getId());
						grnLineItem.setCheckedInQty(grnLineItemInDB.getCheckedInQty());
					} else {
						grnLineItem.setCheckedInQty(grnLineItem.getCheckedInQty());
					}
					grnLineItem.setGoodsReceivedNote(grn);
					grnLineItemDao.save(grnLineItem);
				}
				sku = grnLineItem.getSku();
				skuService.saveSku(sku);
				// productVariant = productVariantDao.save(productVariant);

				// SkuTax
				/*
								 * Sku sku = skuService.getSKU(productVariant, grn.getWarehouse()); if(sku != null){ SkuTax skuTax =
								 * skuService.getOrCreateSkuTax(sku, productVariant.getTax()); }
								 */
			}

			grnDto = grnManager.generateGRNDto(grn);
			grn.setPayable(grnDto.getTotalPayable());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(grn.getGrnDate());
			calendar.add(Calendar.DATE, grn.getPurchaseOrder().getSupplier().getCreditDays());
			grn.setEstPaymentDate(calendar.getTime());

			grn.setFinalPayableAmount(grn.getPayable() - overallDiscount);
			goodsReceivedNoteDao.save(grn);
			grn.getPurchaseOrder().setPurchaseOrderStatus(EnumPurchaseOrderStatus.Received.getPurchaseOrderStatus());
			getGrnManager().getPurchaseOrderDao().save(grn.getPurchaseOrder());
			if(grn.getGrnStatus().getId().equals(EnumGrnStatus.Closed.getId())){
				getPurchaseOrderService().updatePOFillRate(grn.getPurchaseOrder());
					for(PoLineItem poLineItem: grn.getPurchaseOrder().getPoLineItems()){
							if(poLineItemDao.getPoLineItemCountBySku(poLineItem.getSku()) <= 1) {
								poLineItem.setFirstTimePurchased(true);
							}
					}
				if(grn.getPurchaseOrder().isExtraInventoryCreated()){
                	PurchaseOrder po = grn.getPurchaseOrder();
                	Long id = getExtraInventoryService().getExtraInventoryByPoId(po.getId()).getId();
                	po.setExtraInventoryId(id);
                }
				getAdminEmailManager().sendGRNEmail(grn);
			}

		}
		addRedirectAlertMessage(new SimpleMessage("Changes saved."));
		return new RedirectResolution(GRNAction.class);
	}

	public class HTTPResponseResolution implements Resolution {
		public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
			OutputStream out = null;
			InputStream in = new BufferedInputStream(new FileInputStream(xlsFile));
			res.setContentLength((int) xlsFile.length());
			res.setHeader("Content-Disposition", "attachment; filename=\"" + xlsFile.getName() + "\";");
			out = res.getOutputStream();

			// Copy the contents of the file to the output stream
			byte[] buf = new byte[4096];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
		}

	}

	public Resolution downloadGRN() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			xlsFile = new File(adminDownloads + "/reports/Purchase_Order-GRN-" + sdf.format(new Date()) + ".xls");
			xlsFile = grnManager.generateGRNXls(xlsFile.getPath(), grnStatusValue, startDate, endDate, warehouse, reconciled);
			addRedirectAlertMessage(new SimpleMessage("Purchase Order successfully generated"));
		} catch (Exception e) {
			e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
			addRedirectAlertMessage(new SimpleMessage("GoodsReceivedNote generation failed"));
		}
		return new HTTPResponseResolution();

	}

	@Secure(hasAnyPermissions = {PermissionConstants.PURCHASE_INVOICE_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
	public Resolution generatePurchaseInvoiceCheck() {

		Supplier supplier = null;
		if (grnListForPurchaseInvoice != null && grnListForPurchaseInvoice.isEmpty()) {
			return new ForwardResolution("/pages/admin/purchaseInvoiceForGrnAlreadyExist.jsp");
		}
   
		if (grnListForPurchaseInvoice != null && !grnListForPurchaseInvoice.isEmpty() && grnListForPurchaseInvoice.get(0) != null
				&& grnListForPurchaseInvoice.get(0).getPurchaseOrder() != null && grnListForPurchaseInvoice.get(0).getPurchaseOrder().getSupplier() != null) {
			supplier = grnListForPurchaseInvoice.get(0).getPurchaseOrder().getSupplier();
		}
		for (GoodsReceivedNote grn : grnListForPurchaseInvoice) {
			if (grn.getPurchaseInvoices() != null && !grn.getPurchaseInvoices().isEmpty()) {
				addRedirectAlertMessage(new SimpleMessage("Purchase Invoice for GRN: " + grn.getId() + " already exists with id: " + grn.getPurchaseInvoices().get(0).getId()));
				return new ForwardResolution("/pages/admin/purchaseInvoiceForGrnAlreadyExist.jsp");
			}
			if (grn.getPurchaseOrder() != null && grn.getPurchaseOrder().getSupplier() != null) {
				Supplier supplierFromList = grn.getPurchaseOrder().getSupplier();
				if (!supplier.equals(supplierFromList)) {
					addRedirectAlertMessage(new SimpleMessage("GRN of different suppliers cannot be merged"));
					return new ForwardResolution("/pages/admin/purchaseInvoiceForGrnAlreadyExist.jsp");
				}
			}
		}

		return new ForwardResolution(GRNAction.class, "generatePurchaseInvoice");
	}

	public Resolution generatePurchaseInvoice() {
		User loggedOnUser = null;
		Supplier supplier = null;
		Date invoiceDate = new Date();
		String invoiceNumber = "";
		Warehouse warehouseForPI = null;
		if (grnListForPurchaseInvoice != null && !grnListForPurchaseInvoice.isEmpty() && grnListForPurchaseInvoice.get(0) != null
				&& grnListForPurchaseInvoice.get(0).getPurchaseOrder() != null && grnListForPurchaseInvoice.get(0).getPurchaseOrder().getSupplier() != null) {

			supplier = grnListForPurchaseInvoice.get(0).getPurchaseOrder().getSupplier();

			if (grnListForPurchaseInvoice.get(0).getInvoiceDate() != null) {
				invoiceDate = grnListForPurchaseInvoice.get(0).getInvoiceDate();
			}

			if (grnListForPurchaseInvoice.get(0).getInvoiceNumber() != null) {
				invoiceNumber = grnListForPurchaseInvoice.get(0).getInvoiceNumber();
			}

			if (grnListForPurchaseInvoice.get(0).getWarehouse() != null) {
				warehouseForPI = grnListForPurchaseInvoice.get(0).getWarehouse();
			}

		}
		if (getPrincipal() != null) {
			loggedOnUser = getUserService().getUserById(getPrincipal().getId());
		}
		PurchaseInvoice purchaseInvoice = new PurchaseInvoice();
		purchaseInvoice.setCreateDate(new Date());
		purchaseInvoice.setCreatedBy(loggedOnUser);
		int first_index = grnListForPurchaseInvoice.size()-1;
		if (grnListForPurchaseInvoice.get(first_index) != null && grnListForPurchaseInvoice.get(first_index).getEstPaymentDate() != null) {
			purchaseInvoice.setEstPaymentDate(grnListForPurchaseInvoice.get(first_index).getEstPaymentDate());
		}
		purchaseInvoice.setPurchaseInvoiceStatus(getPurchaseInvoiceDao().get(PurchaseInvoiceStatus.class, EnumPurchaseInvoiceStatus.PurchaseInvoiceGenerated.getId()));
		if (supplier != null) {
			purchaseInvoice.setSupplier(supplier);
		}
		if (invoiceNumber != null) {
			purchaseInvoice.setInvoiceNumber(invoiceNumber);
		}
		purchaseInvoice.setInvoiceDate(invoiceDate);
		purchaseInvoice.setWarehouse(warehouseForPI);
		purchaseInvoice = (PurchaseInvoice) getPurchaseInvoiceDao().save(purchaseInvoice);
		Double totalTaxable = 0.0D, totalTax = 0.0D, totalSurcharge = 0.0D, totalPayable = 0.0D;
		Double overallDiscount = 0.0D;
		for (GoodsReceivedNote grn : grnListForPurchaseInvoice) {
			for (GrnLineItem grnLineItem : grn.getGrnLineItems()) {
				Double taxableAmount = 0.0D;
				Double discountPercentage = 0D;
				PurchaseInvoiceLineItem purchaseInvoiceLineItem = new PurchaseInvoiceLineItem();
				purchaseInvoiceLineItem.setPurchaseInvoice(purchaseInvoice);
				purchaseInvoiceLineItem.setGrnLineItem(grnLineItem);
				if (grnLineItem.getCostPrice() != null) {
					purchaseInvoiceLineItem.setCostPrice(grnLineItem.getCostPrice());
				}
				if (grnLineItem.getMrp() != null) {
					purchaseInvoiceLineItem.setMrp(grnLineItem.getMrp());
				}
				if (grnLineItem.getQty() != null) {
					purchaseInvoiceLineItem.setQty(grnLineItem.getQty());
				}
				if (grnLineItem.getDiscountPercent() != null) {
					discountPercentage = grnLineItem.getDiscountPercent();
					purchaseInvoiceLineItem.setDiscountPercent(discountPercentage);
				}
				sku = grnLineItem.getSku();
				
				if (supplier != null && supplier.getState() != null && !supplier.getState().equals(sku.getWarehouse().getState())) {
					purchaseInvoiceLineItem.setTax(getBaseDao().get(Tax.class, EnumTax.CST.getId()));

				}else if (sku != null) {
					purchaseInvoiceLineItem.setSku(sku);
					purchaseInvoiceLineItem.setTax(sku.getTax());
				}

				if (grnLineItem.getQty() != null && grnLineItem.getCostPrice() != null) {
					taxableAmount = (grnLineItem.getQty() * (grnLineItem.getCostPrice() - grnLineItem.getCostPrice() * discountPercentage / 100));
					totalTaxable += taxableAmount;
					purchaseInvoiceLineItem.setTaxableAmount(taxableAmount);
				}
				if (sku != null) {
					purchaseInvoiceLineItem.setSku(sku);
				}
				TaxComponent taxComponent = TaxUtil.getSupplierTaxForPV(grn.getPurchaseOrder().getSupplier(), sku, taxableAmount);
				totalTax += taxComponent.getTax();
				totalSurcharge += taxComponent.getSurcharge();
				totalPayable += taxComponent.getPayable();
				purchaseInvoiceLineItem.setTaxAmount(taxComponent.getTax());
				purchaseInvoiceLineItem.setSurchargeAmount(taxComponent.getSurcharge());
				purchaseInvoiceLineItem.setPayableAmount(taxComponent.getPayable());
				getPurchaseInvoiceDao().save(purchaseInvoiceLineItem);

			}
			if (grn.getDiscount() != null) {
				overallDiscount += grn.getDiscount();
			}
			grn.setReconciled(true);
			goodsReceivedNoteDao.save(grn);
		}
		
		purchaseInvoice.setDiscount(overallDiscount);
		purchaseInvoice.setGoodsReceivedNotes(grnListForPurchaseInvoice);
		purchaseInvoice.setTaxableAmount(totalTaxable);
		purchaseInvoice.setTaxAmount(totalTax);
		purchaseInvoice.setSurchargeAmount(totalSurcharge);
		purchaseInvoice.setPayableAmount(totalPayable);
		purchaseInvoice.setFinalPayableAmount(totalPayable - overallDiscount);
		purchaseInvoice.setShortAmount(0.0);
		purchaseInvoice.setRtvAmount(0.0);
		purchaseInvoice.setPiRtvShortTotal(totalPayable - overallDiscount);
		purchaseInvoiceDao.save(purchaseInvoice);

		addRedirectAlertMessage(new SimpleMessage("Purchase Invoice generated from GRN(s). Please adjust it according to invoice"));
		return new RedirectResolution(PurchaseInvoiceAction.class).addParameter("view").addParameter("purchaseInvoice", purchaseInvoice.getId());
	}
   //commenting Delete Link on Grn Line item
//    public Resolution deleteGrnLineItem() {
//        if (grnLineItem != null) {
//            grn = grnLineItem.getGoodsReceivedNote();
//            if (grnLineItem.getCheckedInQty() != null) {
//                boolean status = grnLineItemService.isAllSkuItemInCheckedInStatus(grnLineItem);
//                if (status) {
//                    //Delete Inventory
//                    ProductVariant productVariant = grnLineItem.getSku().getProductVariant();
//                    adminInventoryService.deleteInventory(grnLineItem);
//                    //Check inventory health
//                    inventoryService.checkInventoryHealth(productVariant);
//
//
//                }
//            }
//        }
//        return new RedirectResolution(GRNAction.class).addParameter("view").addParameter("grn", grn.getId());
//
//    }

	public List<GoodsReceivedNote> getGrnList() {
		return grnList;
	}

	public void setGrnList(List<GoodsReceivedNote> grnList) {
		this.grnList = grnList;
	}

	public GoodsReceivedNote getGrn() {
		return grn;
	}

	public void setGrn(GoodsReceivedNote grn) {
		this.grn = grn;
	}

	public Date getStartDate() {
		return startDate;
	}

	@Validate(converter = CustomDateTypeConvertor.class)
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@Validate(converter = CustomDateTypeConvertor.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public GRNDto getGrnDto() {
		return grnDto;
	}

	public void setGrnDto(GRNDto grnDto) {
		this.grnDto = grnDto;
	}

	public List<GrnLineItem> getGrnLineItems() {
		return grnLineItems;
	}

	public void setGrnLineItems(List<GrnLineItem> grnLineItems) {
		this.grnLineItems = grnLineItems;
	}

	public int getPerPageDefault() {
		return defaultPerPage;
	}

	public int getPageCount() {
		return grnPage == null ? 0 : grnPage.getTotalPages();
	}

	public int getResultCount() {
		return grnPage == null ? 0 : grnPage.getTotalResults();
	}

	public GrnStatus getGrnStatus() {
		return grnStatus;
	}

	public void setGrnStatus(GrnStatus grnStatus) {
		this.grnStatus = grnStatus;
	}

	public Double getSurcharge() {
		if (grn.getPurchaseOrder().getSupplier().getState().equals(StateList.HARYANA)) {
			return 0.05;
		} else {
			return 0.0;
		}
	}

	public void setSurcharge(Double surcharge) {
		this.surcharge = surcharge;
	}

	public List<GoodsReceivedNote> getGrnListForPurchaseInvoice() {
		return grnListForPurchaseInvoice;
	}

	public void setGrnListForPurchaseInvoice(List<GoodsReceivedNote> grnListForPurchaseInvoice) {
		this.grnListForPurchaseInvoice = grnListForPurchaseInvoice;
	}

	public Boolean isReconciled() {
		return reconciled;
	}

	public Boolean getReconciled() {
		return reconciled;
	}

	public void setReconciled(Boolean reconciled) {
		this.reconciled = reconciled;
	}

	public Long getGrnStatusValue() {
		return grnStatusValue;
	}

	public void setGrnStatusValue(Long grnStatusValue) {
		this.grnStatusValue = grnStatusValue;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public GRNManager getGrnManager() {
		return grnManager;
	}

	public void setGrnManager(GRNManager grnManager) {
		this.grnManager = grnManager;
	}

	public GoodsReceivedNoteDao getGoodsReceivedNoteDao() {
		return goodsReceivedNoteDao;
	}

	public void setGoodsReceivedNoteDao(GoodsReceivedNoteDao goodsReceivedNoteDao) {
		this.goodsReceivedNoteDao = goodsReceivedNoteDao;
	}

	public GrnLineItemDao getGrnLineItemDao() {
		return grnLineItemDao;
	}

	public void setGrnLineItemDao(GrnLineItemDao grnLineItemDao) {
		this.grnLineItemDao = grnLineItemDao;
	}

	public ProductVariantService getProductVariantService() {
		return productVariantService;
	}

	public void setProductVariantService(ProductVariantService productVariantService) {
		this.productVariantService = productVariantService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public PurchaseInvoiceDao getPurchaseInvoiceDao() {
		return purchaseInvoiceDao;
	}

	public void setPurchaseInvoiceDao(PurchaseInvoiceDao purchaseInvoiceDao) {
		this.purchaseInvoiceDao = purchaseInvoiceDao;
	}

	public SupplierDao getSupplierDao() {
		return supplierDao;
	}

	public void setSupplierDao(SupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	public SkuService getSkuService() {
		return skuService;
	}

	public void setSkuService(SkuService skuService) {
		this.skuService = skuService;
	}

	public Map<Sku, Boolean> getSkuIsNew() {
		return skuIsNew;
	}

	public void setSkuIsNew(Map<Sku, Boolean> skuIsNew) {
		this.skuIsNew = skuIsNew;
	}

	public PoLineItemService getPoLineItemService() {
		return poLineItemService;
	}

	public void setPoLineItemService(PoLineItemService poLineItemService) {
		this.poLineItemService = poLineItemService;
	}

	public PurchaseOrderService getPurchaseOrderService() {
		return purchaseOrderService;
	}

	public void setPurchaseOrderService(PurchaseOrderService purchaseOrderService) {
		this.purchaseOrderService = purchaseOrderService;
	}

	public ExtraInventoryService getExtraInventoryService() {
		return extraInventoryService;
	}

	public void setExtraInventoryService(ExtraInventoryService extraInventoryService) {
		this.extraInventoryService = extraInventoryService;
	}

public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("productVariant");
		params.add("invoiceNumber");
		params.add("tinNumber");
		params.add("supplierName");
		params.add("grn");
		params.add("grnStatus");
		params.add("reconciled");
		params.add("warehouse");
		return params;
	}

    public GrnLineItem getGrnLineItem() {
        return grnLineItem;
    }

    public void setGrnLineItem(GrnLineItem grnLineItem) {
        this.grnLineItem = grnLineItem;
    }

	public AdminEmailManager getAdminEmailManager() {
		return adminEmailManager;
	}

	public void setAdminEmailManager(AdminEmailManager adminEmailManager) {
		this.adminEmailManager = adminEmailManager;
	}

	public long getSaveValue() {
		return saveValue;
	}

	public void setSaveValue(long saveValue) {
		this.saveValue = saveValue;
	}
    
}