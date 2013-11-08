package com.hk.web.action.admin.inventory.audit;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.warehouse.BinDao;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.inventory.Bin;
import com.hk.domain.inventory.SkuItemAudit;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.UserService;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.Date;

@Secure(hasAnyPermissions = {PermissionConstants.INVENTORY_CHECKIN}, authActionBean = AdminPermissionAction.class)
public class InventoryAuditAction extends BaseAction {
  private static Logger logger = Logger.getLogger(InventoryAuditAction.class);
  @Autowired
  BinDao binDao;
  @Autowired
  UserService userService;
  @Autowired
  SkuItemDao skuItemDao;
  @Autowired
  BaseDao baseDao;

  @Validate(required = true, on = "saveBin")
  private String barcode;
  @Validate(required = true, on = "saveBin")
  private String firstLocation;
  @Validate(required = true, on = "saveBin")
  private String finalLocation;
  private Warehouse warehouse;

  @DefaultHandler
  public Resolution pre() {
    if (getUserService().getWarehouseForLoggedInUser() != null) {
      warehouse = userService.getWarehouseForLoggedInUser();
      return new ForwardResolution("/pages/admin/inventory/inventoryAudit.jsp");
    } else {
      return new RedirectResolution(AdminHomeAction.class);
    }
  }

  public Resolution save() {
    logger.debug(firstLocation + ":" + barcode + ":" + finalLocation + ":" + warehouse);
    if (firstLocation != null && finalLocation != null && barcode != null && firstLocation.equals(finalLocation)) {
      SkuItem skuItem = skuItemDao.getSkuItemByBarcode(barcode);
      Bin bin = binDao.findByBarCodeAndWarehouse(firstLocation, warehouse);
      if (skuItem != null && bin != null && skuItem.getSkuGroup().getSku().getWarehouse().getId().equals(warehouse.getId())) {
        skuItem.setBin(bin);
        skuItem = (SkuItem) getBaseDao().save(skuItem);
        SkuItemAudit sia = new SkuItemAudit();
        sia.setSkuItem(skuItem);
        sia.setUser(getUserService().getLoggedInUser());
        sia.setAuditDate(new Date());
        try {
          getBaseDao().save(sia);
          addRedirectAlertMessage(new SimpleMessage("<strong style='color:green'>Successfully saved the Bin Allocation.</strong>"));
        } catch (Exception e) {
          //addRedirectAlertMessage(new SimpleMessage("<strong style='color:red'>Got an exception - " + e.getCause() + "</strong>"));
          addRedirectAlertMessage(new SimpleMessage("<strong style='color:red'>Duplicate SKU Item Barcode (but location updated)</strong>"));
        }
      } else if (skuItem != null && bin != null && !skuItem.getSkuGroup().getSku().getWarehouse().getId().equals(warehouse.getId())) {
        addRedirectAlertMessage(new SimpleMessage("<strong style='color:red'>Incorrect Warehouse for SkuItem and Location</strong>"));
      } else if (bin != null && skuItem == null) {
        addRedirectAlertMessage(new SimpleMessage("<strong style='color:red'>Invalid SkuItem Barcode</strong>"));
      } else if (skuItem != null && bin == null) {
        addRedirectAlertMessage(new SimpleMessage("<strong style='color:red'>Invalid Location Barcode</strong>"));
      } else {
        addRedirectAlertMessage(new SimpleMessage("<strong style='color:red'>Invalid Location and SKU Item Barcode</strong>"));
      }
    } else {
      addRedirectAlertMessage(new SimpleMessage("<strong style='color:red'>Input seems to be wrong. Please Try Again.</strong>"));
    }
    return new RedirectResolution(InventoryAuditAction.class).addParameter("firstLocation", firstLocation);
  }


  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public String getFirstLocation() {
    return firstLocation;
  }

  public void setFirstLocation(String firstLocation) {
    this.firstLocation = firstLocation;
  }

  public String getFinalLocation() {
    return finalLocation;
  }

  public void setFinalLocation(String finalLocation) {
    this.finalLocation = finalLocation;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }


}