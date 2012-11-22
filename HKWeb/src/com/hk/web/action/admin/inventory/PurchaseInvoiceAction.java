package com.hk.web.action.admin.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hk.admin.pact.service.accounting.PurchaseInvoiceService;
import com.hk.constants.inventory.EnumPurchaseInvoiceStatus;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.dao.inventory.PurchaseInvoiceDao;
import com.hk.admin.pact.service.accounting.ProcurementService;
import com.hk.constants.core.EnumPermission;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.domain.inventory.po.PurchaseInvoiceLineItem;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
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

/**
 * Created by IntelliJ IDEA. User: Rahul Date: Feb 15, 2012 Time: 8:26:07 AM To change this template use File | Settings |
 * File Templates.
 */

@Secure(hasAnyPermissions = { PermissionConstants.PURCHASE_INVOICE_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
@Component
public class PurchaseInvoiceAction extends BasePaginatedAction {
    @Autowired
    PurchaseInvoiceDao                    purchaseInvoiceDao;
    @Autowired
    ProductVariantDao                     productVariantDao;
    @Autowired
    GoodsReceivedNoteDao                  goodsReceivedNoteDao;
    @Autowired
    SkuService                            skuService;
    @Autowired
    UserService                           userService;
    @Autowired
    private ProductVariantService         productVariantService;
    @Autowired
    private ProcurementService            procurementService;

	@Autowired
	private PurchaseInvoiceService        purchaseInvoiceService;
  
    private static Logger                 logger                    = Logger.getLogger(PurchaseInvoiceAction.class);

    private Integer                       defaultPerPage            = 20;
    Page                                  purchaseInvoicePage;

    private List<PurchaseInvoice>         purchaseInvoiceList       = new ArrayList<PurchaseInvoice>();
    private PurchaseInvoice               purchaseInvoice;
    private List<PurchaseInvoiceLineItem> purchaseInvoiceLineItems  = new ArrayList<PurchaseInvoiceLineItem>();
    private Date                          startDate;
    private Date                          endDate;
    private String                        tinNumber;
    private String                        invoiceNumber;
    private String                        supplierName;
    private ProductVariant                productVariant;
    private PurchaseInvoiceStatus         purchaseInvoiceStatus;
    private User                          approvedBy;
    private User                          createdBy;
    private String                        productVariantId;
    private Boolean                       reconciled;
    private Warehouse                     warehouse;
    private Boolean                       saveEnabled                = true;
    private Date                          grnDate;

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
        List<GoodsReceivedNote> grnList=purchaseInvoice.getGoodsReceivedNotes();
        List<Date> grnDateList=new ArrayList<Date>();
        boolean isLoggedInUserHasFinancePermission=userService.getLoggedInUser().hasPermission(EnumPermission.FINANCE_MANAGEMENT);

        if(isLoggedInUserHasFinancePermission){
            saveEnabled=true;
        } else if(purchaseInvoice.getReconciled()== null)
        {
            saveEnabled=true;
        } else if(purchaseInvoice.getReconciled()){
            saveEnabled=false;
        }
        for(GoodsReceivedNote grn:grnList){
            grnDateList.add(grn.getGrnDate());
        }
        grnDate= Collections.min(grnDateList);
        if (purchaseInvoice != null) {
            // logger.debug("purchaseInvoice@view: " + purchaseInvoice.getId());
            // grnDto = grnManager.generateGRNDto(grn);
        }
        return new ForwardResolution("/pages/admin/purchaseInvoice.jsp");
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
				    if (sku == null) {
					    sku = skuService.getSKU(purchaseInvoiceLineItem.getProductVariant(), purchaseInvoice.getWarehouse());
					    purchaseInvoiceLineItem.setSku(sku);
				    }
				    skuService.saveSku(sku);
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

		    getPurchaseInvoiceService().save(purchaseInvoice);
	    }
	    addRedirectAlertMessage(new SimpleMessage("Changes saved."));
	    return new RedirectResolution(PurchaseInvoiceAction.class);
    }

    public Resolution delete() {
      Boolean deleteStatus = false;
      if (purchaseInvoice != null && purchaseInvoice.getId() != null) {
        deleteStatus = procurementService.deletePurchaseInvoice(purchaseInvoice);
      }
      if(deleteStatus == false){
        addRedirectAlertMessage(new SimpleMessage("Unable to delete purchase invoice, please contact admin."));
      }else{
        addRedirectAlertMessage(new SimpleMessage("Purchase Invoice Deleted."));
      }
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
	 * @param redirectResolution
	 * @param purchaseInvoice
	 * @return
	 */
	private Resolution performPISanityChecks(String redirectResolution, PurchaseInvoice purchaseInvoice){
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

		if(purchaseInvoice.getReconciled() != null && purchaseInvoice.getReconciled() &&
				purchaseInvoice.getPurchaseInvoiceStatus().getId().equals(EnumPurchaseInvoiceStatus.PurchaseInvoiceGenerated.getId())){
			addRedirectAlertMessage(new SimpleMessage("Since PI is reconciled, please change the status from " + EnumPurchaseInvoiceStatus.PurchaseInvoiceGenerated.getName()+" to some other status"));
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

	@Validate(converter = CustomDateTypeConvertor.class)
    public void setGrnDate(Date grnDate) {
        this.grnDate = grnDate;
    }
}
