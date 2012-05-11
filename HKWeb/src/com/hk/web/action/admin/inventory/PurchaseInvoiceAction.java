package com.hk.web.action.admin.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.dao.inventory.PurchaseInvoiceDao;
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
    private List<GoodsReceivedNote>       grnListForPurchaseInvoice = new ArrayList<GoodsReceivedNote>();
    private String                        productVariantId;
    private Boolean                       isReconciled;

    @DefaultHandler
    public Resolution pre() {

        if (productVariant != null) {
            purchaseInvoiceList = purchaseInvoiceDao.listPurchaseInvoiceWithProductVariant(productVariant);
        } else {
            purchaseInvoicePage = purchaseInvoiceDao.searchPurchaseInvoice(purchaseInvoice, purchaseInvoiceStatus, createdBy, invoiceNumber, tinNumber, supplierName, getPageNo(),
                    getPerPage(), isReconciled);
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
            // logger.debug("purchaseInvoice@view: " + purchaseInvoice.getId());
            // grnDto = grnManager.generateGRNDto(grn);
        }
        return new ForwardResolution("/pages/admin/purchaseInvoice.jsp");
    }

    public Resolution save() {
        if (purchaseInvoice != null && purchaseInvoice.getId() != null) {
            logger.debug("purchaseInvoiceLineItems@Save: " + purchaseInvoiceLineItems.size());

            if (StringUtils.isBlank(purchaseInvoice.getInvoiceNumber()) || purchaseInvoice.getInvoiceDate() == null) {
                addRedirectAlertMessage(new SimpleMessage("Invoice date and number are mandatory."));
                return new RedirectResolution(PurchaseInvoiceAction.class).addParameter("view").addParameter("purchaseInvoice", purchaseInvoice.getId());
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
            purchaseInvoiceDao.save(purchaseInvoice);
        }
        addRedirectAlertMessage(new SimpleMessage("Changes saved."));
        return new RedirectResolution(PurchaseInvoiceAction.class);
    }

    public Resolution delete() {
        if (purchaseInvoice != null && purchaseInvoice.getId() != null) {
            List<GoodsReceivedNote> grns = purchaseInvoice.getGoodsReceivedNotes();
            for (GoodsReceivedNote grn : grns) {
                grn.setReconciled(false);
                goodsReceivedNoteDao.save(grn);
            }
            for (PurchaseInvoiceLineItem purchaseInvoiceLineItem : purchaseInvoiceLineItems) {
                purchaseInvoiceDao.delete(purchaseInvoiceLineItem);
            }
            purchaseInvoiceDao.delete(purchaseInvoice);
        }
        addRedirectAlertMessage(new SimpleMessage("Purchase Invoice Deleted."));
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

    public Boolean isReconciled() {
        return isReconciled;
    }

    public void setReconciled(Boolean reconciled) {
        isReconciled = reconciled;
    }
}
