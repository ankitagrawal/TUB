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
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.dao.inventory.PoLineItemDao;
import com.hk.admin.impl.dao.inventory.PurchaseOrderDao;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.PermissionConstants;
import com.hk.dao.catalog.product.ProductVariantDao;
import com.hk.dao.sku.SkuGroupDao;
import com.hk.dao.sku.SkuItemDao;
import com.hk.domain.sku.SkuGroup;
import com.hk.service.InventoryService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.INVENTORY_CHECKIN}, authActionBean = AdminPermissionAction.class)
@Component
public class InventoryBulkCheckinAction extends BaseAction {

  private static Logger logger = Logger.getLogger(InventoryBulkCheckinAction.class);

   ProductVariantDao productVariantDao;
   SkuGroupDao skuGroupDao;
   SkuItemDao skuItemDao;
   PoLineItemDao poLineItemDao;
  
  InventoryService inventoryService;
   PurchaseOrderDao purchaseOrderDao;
   XslParser xslParser;

   //@Named(Keys.Env.adminUploads) 
   String adminUploadsPath;

  @Validate(required = true)
  FileBean fileBean;

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
    //excelFile.delete();
    return new RedirectResolution(InventoryBulkCheckinAction.class);
  }

}