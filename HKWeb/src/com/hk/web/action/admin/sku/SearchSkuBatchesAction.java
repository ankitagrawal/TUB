package com.hk.web.action.admin.sku;


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
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.SkuGroup;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.web.action.admin.inventory.ListBatchesAndCheckinInventory;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.INVENTORY_CHECKIN }, authActionBean = AdminPermissionAction.class)
@Component
public class SearchSkuBatchesAction extends BaseAction {

    private static Logger         logger = Logger.getLogger(SearchSkuBatchesAction.class);
    @Autowired
    private ProductVariantService productVariantService;
    @Autowired
    private AdminInventoryService adminInventoryService;

    @Validate(required = true)
    private String                upc;

    private List<SkuGroup>        skuGroups;
    private List<ProductVariant>  productVariants;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/searchSkuBatches.jsp");
    }

    public Resolution showBatches() {
        logger.debug("upc: " + upc);
        if (StringUtils.isNotBlank(upc)) {
            ProductVariant productVariant = getProductVariantService().findVariantFromUPC(upc);
            if (productVariant == null) {
                productVariant = getProductVariantService().getVariantById(upc);// UPC not available must have entered
                // Variant Id
            }
            logger.debug("productVariant: " + productVariant);
            if (productVariant != null) {
                skuGroups = getAdminInventoryService().getInStockSkuGroups(upc);
                logger.debug("skuGroups: " + skuGroups.size());
            } else {
                addRedirectAlertMessage(new SimpleMessage("Invalid UPC or VariantID"));
                return new RedirectResolution(SearchSkuBatchesAction.class);
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("Invalid UPC or VariantID"));
            return new RedirectResolution(SearchSkuBatchesAction.class);
        }
        return new ForwardResolution(ListBatchesAndCheckinInventory.class);
    }

    public Resolution findProductVariantByUpc() {
        if (StringUtils.isNotBlank(upc)) {
            productVariants = getProductVariantService().findVariantsFromUPC(upc);
        }
        return new ForwardResolution("/pages/admin/searchSkuBatches.jsp");
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public List<SkuGroup> getSkuGroups() {
        return skuGroups;
    }

    public void setSkuGroups(List<SkuGroup> skuGroups) {
        this.skuGroups = skuGroups;
    }

    public List<ProductVariant> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<ProductVariant> productVariants) {
        this.productVariants = productVariants;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public AdminInventoryService getAdminInventoryService() {
        return adminInventoryService;
    }

    public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
        this.adminInventoryService = adminInventoryService;
    }

}