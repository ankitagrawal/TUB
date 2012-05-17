package com.hk.web.action.admin.inventory;

import java.io.File;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.sku.SkuGroup;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.INVENTORY_CHECKIN }, authActionBean = AdminPermissionAction.class)
@Component
public class InventoryBulkCheckinAction extends BaseAction {

    private static Logger logger = Logger.getLogger(InventoryBulkCheckinAction.class);
    @Autowired
    ProductVariantDao     productVariantDao;
    @Autowired
    SkuGroupDao           skuGroupDao;
    @Autowired
    SkuItemDao            skuItemDao;
    @Autowired
    PoLineItemDao         poLineItemDao;
    @Autowired
    InventoryService      inventoryService;
    @Autowired
    PurchaseOrderDao      purchaseOrderDao;
    @Autowired
    XslParser             xslParser;

    // @Named(Keys.Env.adminUploads)
    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String                adminUploadsPath;

    @Validate(required = true)
    FileBean              fileBean;

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/inventoryBulkCheckin.jsp");
    }

    public Resolution parse() throws Exception {
        String excelFilePath = adminUploadsPath + "/catalogFiles/" + System.currentTimeMillis() + ".xls";
        File excelFile = new File(excelFilePath);
        excelFile.getParentFile().mkdirs();
        fileBean.save(excelFile);

        try {
            Set<SkuGroup> skuGroupSet = xslParser.readAndBulkCheckinInventory(null, excelFile);
            addRedirectAlertMessage(new SimpleMessage(skuGroupSet.size() + " SkuGroups Updated Successfully."));
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
            return new RedirectResolution(InventoryBulkCheckinAction.class);
        }
        // excelFile.delete();
        return new RedirectResolution(InventoryBulkCheckinAction.class);
    }

}