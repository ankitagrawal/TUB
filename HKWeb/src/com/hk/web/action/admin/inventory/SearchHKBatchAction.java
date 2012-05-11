package com.hk.web.action.admin.inventory;

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
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.sku.SkuGroup;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.INVENTORY_CHECKIN }, authActionBean = AdminPermissionAction.class)
public class SearchHKBatchAction extends BaseAction {

    private static Logger logger = Logger.getLogger(SearchHKBatchAction.class);

    @Autowired
    AdminInventoryService      adminInventoryService;

    @Validate(required = true)
    private String        hkBarcode;

    private SkuGroup      skuGroup;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/searchHKBatch.jsp");
    }

    public Resolution showBatchInfo() {
        logger.debug("upc: " + hkBarcode);
        if (StringUtils.isNotBlank(hkBarcode)) {
            skuGroup = adminInventoryService.getSkuGroupByHkBarcode(hkBarcode);
            return new ForwardResolution("/pages/admin/searchHKBatch.jsp");
        } else {
            addRedirectAlertMessage(new SimpleMessage("Invalid HK Barcode."));
            return new RedirectResolution(SearchHKBatchAction.class);
        }
    }

    public String getHkBarcode() {
        return hkBarcode;
    }

    public void setHkBarcode(String hkBarcode) {
        this.hkBarcode = hkBarcode;
    }

    public SkuGroup getSkuGroup() {
        return skuGroup;
    }

    public void setSkuGroup(SkuGroup skuGroup) {
        this.skuGroup = skuGroup;
    }
}