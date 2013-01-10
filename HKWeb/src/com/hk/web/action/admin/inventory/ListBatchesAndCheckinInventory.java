package com.hk.web.action.admin.inventory;

import java.util.Date;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.SkuGroup;
import com.hk.pact.dao.inventory.LowInventoryDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.INVENTORY_CHECKIN }, authActionBean = AdminPermissionAction.class)
@Component
public class ListBatchesAndCheckinInventory extends BaseAction {

    private static Logger           logger = Logger.getLogger(ListBatchesAndCheckinInventory.class);

    @Autowired
    private ProductVariantService   productVariantService;
    @Autowired
    SkuGroupService                skuGroupService;
    @Autowired
    SkuItemDao                      skuItemDao;
    @Autowired
    PoLineItemDao                   poLineItemDao;
    @Autowired
    PurchaseOrderDao                purchaseOrderDao;
    @Autowired
    LowInventoryDao                 lowInventoryDao;
    @Autowired
    AdminInventoryService           adminInventoryService;
    @Autowired
    AdminProductVariantInventoryDao productVariantInventoryDao;

    @Validate(required = true)
    private String                  upc;
    @Validate(required = true, minvalue = 1.0)
    private Long                    qty;
    @Validate(required = true)
    private String                  batch;
    private Date                    mfgDate;
    private Date                    expiryDate;
    private String                  invoiceNumber;
    private Date                    invoiceDate;

    private List<SkuGroup>          availableSkuGroups;
    private List<SkuGroup>          allSkuGroups;
    private ProductVariant          productVariant;
    private SkuGroup                skuGroup;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        logger.debug("upc: " + upc);
        if (StringUtils.isNotBlank(upc)) {
            productVariant = getProductVariantService().findVariantFromUPC(upc);
            if (productVariant == null) {
                productVariant = getProductVariantService().getVariantById(upc);// UPC not available must have entered
                // Variant Id
            }
            logger.debug("productVariant: " + productVariant);
            if (productVariant != null) {
                availableSkuGroups = adminInventoryService.getInStockSkuGroups(upc);
                logger.debug("availableSkuGroups: " + availableSkuGroups.size());
                allSkuGroups = skuGroupService.getAllCheckedInBatches(productVariant);
                logger.debug("allSkuGroups: " + allSkuGroups.size());
            } else {
                addRedirectAlertMessage(new SimpleMessage("Invalid UPC or VariantID"));
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("Invalid UPC or VariantID"));
        }
        logger.debug("availableSkuGroups: " + availableSkuGroups);
        return new ForwardResolution("/pages/admin/listBatchesAndCheckinInventory.jsp");
    }

    @DontValidate
    public Resolution adjust() {
        if (skuGroup != null && qty != null && qty > 0) {
            adminInventoryService.adjustInventory(skuGroup, qty);
            addRedirectAlertMessage(new SimpleMessage("Inventory adjusted by " + qty));
            return new RedirectResolution(ListBatchesAndCheckinInventory.class, "pre").addParameter("upc", upc);
        }
        addRedirectAlertMessage(new SimpleMessage("Inventory adjustment Qty is mandatory"));
        return new RedirectResolution(ListBatchesAndCheckinInventory.class, "pre").addParameter("upc", upc);
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Date getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(Date mfgDate) {
        this.mfgDate = mfgDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public List<SkuGroup> getAvailableSkuGroups() {
        return availableSkuGroups;
    }

    public void setAvailableSkuGroups(List<SkuGroup> availableSkuGroups) {
        this.availableSkuGroups = availableSkuGroups;
    }

    public List<SkuGroup> getAllSkuGroups() {
        return allSkuGroups;
    }

    public void setAllSkuGroups(List<SkuGroup> allSkuGroups) {
        this.allSkuGroups = allSkuGroups;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public SkuGroup getSkuGroup() {
        return skuGroup;
    }

    public void setSkuGroup(SkuGroup skuGroup) {
        this.skuGroup = skuGroup;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

}