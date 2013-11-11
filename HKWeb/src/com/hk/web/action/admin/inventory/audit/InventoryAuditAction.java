package com.hk.web.action.admin.inventory.audit;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.inventory.SkuItemAuditDao;
import com.hk.admin.pact.dao.warehouse.BinDao;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.inventory.Bin;
import com.hk.domain.inventory.SkuItemAudit;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.UserService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Secure(hasAnyPermissions = {PermissionConstants.INVENTORY_CHECKIN}, authActionBean = AdminPermissionAction.class)
public class InventoryAuditAction extends BasePaginatedAction {
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

  private Page skuItemAuditPage;
  private List<SkuItemAudit> skuItemAuditList;
  private Integer defaultPerPage = 20;

  private SkuItemAuditDao skuItemAuditDao;
  private String brand;
  private String variantId;
  private String auditedBy;
  private Date startDate;
  private Date endDate;

  @DefaultHandler
  public Resolution pre() {
    if (getUserService().getWarehouseForLoggedInUser() != null) {
      warehouse = userService.getWarehouseForLoggedInUser();
      return new ForwardResolution("/pages/admin/inventory/inventoryAudit.jsp");
    } else {
      return new RedirectResolution(AdminHomeAction.class);
    }
  }

  public Resolution auditList() {
    if (warehouse == null && getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null) {
      warehouse = getPrincipalUser().getSelectedWarehouse();
    }
    skuItemAuditPage = getSkuItemAuditDao().search(brand, variantId, barcode, auditedBy, startDate, endDate, warehouse,
        getPageNo(), getPerPage());
    skuItemAuditList = skuItemAuditPage.getList();
    return new ForwardResolution("/pages/admin/inventory/auditList.jsp");
  }

  public Resolution save() {
    logger.debug("Location: " + firstLocation + "; Barcode=" + barcode + "; Warehouse=" + warehouse);
    if (warehouse != null && firstLocation != null && finalLocation != null && barcode != null && firstLocation.equals(finalLocation)) {
      SkuItem skuItem = skuItemDao.getSkuItemByBarcode(barcode);
      Bin bin = binDao.findByBarCodeAndWarehouse(firstLocation, warehouse);
      if (skuItem != null && bin != null && skuItem.getSkuGroup().getSku().getWarehouse().getId().equals(warehouse.getId())) {
        SkuItemAudit sia = getSkuItemAuditDao().getSkuItemAudit(skuItem);
        if (sia != null) {
          addRedirectAlertMessage(new SimpleMessage("<strong style='color:red'>Duplicate SKU Item Barcode</strong>"));
          return new RedirectResolution(InventoryAuditAction.class).addParameter("firstLocation", firstLocation);
        }
        sia = new SkuItemAudit();
        sia.setSkuItem(skuItem);
        sia.setUser(getUserService().getLoggedInUser());
        sia.setAuditDate(new Date());
        try {
          getBaseDao().save(sia);
          addRedirectAlertMessage(new SimpleMessage("<strong style='color:green'>Successfully recorded Audit Information.</strong>"));
          try {
            skuItem.setBin(bin);
            skuItem = (SkuItem) getBaseDao().save(skuItem);
            addRedirectAlertMessage(new SimpleMessage("<strong style='color:green'>Successfully saved the Bin Allocation.</strong>"));
          } catch (Exception e) {
            logger.error("Got an exception while saving BIN mapping: " + e);
            addRedirectAlertMessage(new SimpleMessage("<strong style='color:red'>Could not update location</strong>"));
          }
        } catch (Exception e) {
          logger.error("Got an exception while saving audit records: " + e);
          addRedirectAlertMessage(new SimpleMessage("<strong style='color:red'>Duplicate SKU Item Barcode</strong>"));
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
      addRedirectAlertMessage(new SimpleMessage("<strong style='color:red'>Input seems to be wrong. Please check the values.</strong>"));
      addRedirectAlertMessage(new SimpleMessage("Input -> " + "Location: " + firstLocation + "; Barcode=" + barcode + "; Warehouse=" + warehouse));
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

  public int getPerPageDefault() {
    return defaultPerPage;
  }

  public int getPageCount() {
    return skuItemAuditPage == null ? 0 : skuItemAuditPage.getTotalPages();
  }

  public int getResultCount() {
    return skuItemAuditPage == null ? 0 : skuItemAuditPage.getTotalResults();
  }

  @Override
  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
    params.add("brand");
    params.add("variantId");
    params.add("barcode");
    params.add("auditedBy");
    params.add("startDate");
    params.add("endDate");
    params.add("warehouse");
    return params;
  }

  public List<SkuItemAudit> getSkuItemAuditList() {
    return skuItemAuditList;
  }

  public void setSkuItemAuditList(List<SkuItemAudit> skuItemAuditList) {
    this.skuItemAuditList = skuItemAuditList;
  }

  public SkuItemAuditDao getSkuItemAuditDao() {
    if (skuItemAuditDao == null) {
      skuItemAuditDao = ServiceLocatorFactory.getService(SkuItemAuditDao.class);
    }
    return skuItemAuditDao;
  }

  public void setSkuItemAuditDao(SkuItemAuditDao skuItemAuditDao) {
    this.skuItemAuditDao = skuItemAuditDao;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getVariantId() {
    return variantId;
  }

  public void setVariantId(String variantId) {
    this.variantId = variantId;
  }

  public String getAuditedBy() {
    return auditedBy;
  }

  public void setAuditedBy(String auditedBy) {
    this.auditedBy = auditedBy;
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
}