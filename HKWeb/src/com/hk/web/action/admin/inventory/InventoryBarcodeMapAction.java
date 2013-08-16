package com.hk.web.action.admin.inventory;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.dto.inventory.CreditNoteDto;
import com.hk.admin.pact.dao.inventory.CreditNoteDao;
import com.hk.admin.pact.dao.inventory.InventoryBarcodeMapDao;
import com.hk.admin.util.inventory.InventoryBarcodeXslManager;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.Keys;
import com.hk.constants.inventory.EnumInventoryBarcodeMapType;
import com.hk.domain.inventory.creditNote.CreditNote;
import com.hk.domain.inventory.creditNote.CreditNoteLineItem;
import com.hk.domain.inventory.creditNote.CreditNoteStatus;
import com.hk.domain.inventory.crossDomain.InventoryBarcodeMap;
import com.hk.domain.inventory.crossDomain.InventoryBarcodeMapItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.inventory.SkuService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.File;

@Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class InventoryBarcodeMapAction extends BasePaginatedAction {

  private static Logger logger = Logger.getLogger(InventoryBarcodeMapAction.class);

  @Autowired
  private InventoryBarcodeMapDao inventoryBarcodeMapDao;

  @Autowired
  private XslParser xslParser;


  @Validate(required = true, on = "parse")
  private FileBean fileBean;

  @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
  String adminUploadsPath;


  Page invBarcodeMapPage;
  private List<InventoryBarcodeMap> invBarcodeMapList = new ArrayList<InventoryBarcodeMap>();

  private PurchaseOrder purchaseOrder;
  private String login;
  private String status;

  private Integer defaultPerPage = 20;

  @DefaultHandler
  public Resolution pre() {
    invBarcodeMapPage = inventoryBarcodeMapDao.searchInventoryBarcodeMap(status, login, getPageNo(), getPerPage());
    invBarcodeMapList = invBarcodeMapPage.getList();
    return new ForwardResolution("/pages/admin/inventoryBarcodeMapList.jsp");
  }


  public Resolution parse() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String excelFilePath = adminUploadsPath + "/poBarcodes/" + sdf.format(new Date()) + "/POID-" + purchaseOrder + "-" + sdf.format(new Date()) + ".xls";
    File excelFile = new File(excelFilePath);
    excelFile.getParentFile().mkdirs();
    fileBean.save(excelFile);

    try {
      InventoryBarcodeMap inventoryBarcodeMap = inventoryBarcodeMapDao.getInventoryBarcodeMap(purchaseOrder);
      if(inventoryBarcodeMap == null){
        inventoryBarcodeMap = new InventoryBarcodeMap();
        inventoryBarcodeMap.setUser(getUserService().getLoggedInUser());
        inventoryBarcodeMap.setPurchaseOrder(purchaseOrder);
        inventoryBarcodeMap.setStatus(EnumInventoryBarcodeMapType.Active.getName());
        inventoryBarcodeMap.setCreateDate(new Date());
        inventoryBarcodeMap = (InventoryBarcodeMap)getBaseDao().save(inventoryBarcodeMap);
      }else{
        inventoryBarcodeMap.setStatus(EnumInventoryBarcodeMapType.Active.getName());
        inventoryBarcodeMap = (InventoryBarcodeMap)getBaseDao().save(inventoryBarcodeMap);
      }
      Set<InventoryBarcodeMapItem> barcodeMapItems = xslParser.readAndCreateInvBarcodeMapItems(excelFile, inventoryBarcodeMap);
      addRedirectAlertMessage(new SimpleMessage(barcodeMapItems.size() + " Barcodes Mapped Successfully."));
    } catch (Exception e) {
      logger.error("Exception while reading excel sheet.", e);
      addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
    }
    return new RedirectResolution(InventoryBarcodeMapAction.class);
  }

  public Resolution delete() {
    InventoryBarcodeMap inventoryBarcodeMap = inventoryBarcodeMapDao.getInventoryBarcodeMap(purchaseOrder);
    inventoryBarcodeMapDao.deleteRecords(inventoryBarcodeMap);
    addRedirectAlertMessage(new SimpleMessage("Records Deleted for Barcode Map of PO#"+purchaseOrder.getId()));
    return new ForwardResolution("/pages/admin/inventoryBarcodeMapList.jsp");
  }

  public List<InventoryBarcodeMap> getInvBarcodeMapList() {
    return invBarcodeMapList;
  }

  public void setInvBarcodeMapList(List<InventoryBarcodeMap> invBarcodeMapList) {
    this.invBarcodeMapList = invBarcodeMapList;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public FileBean getFileBean() {
    return fileBean;
  }

  public void setFileBean(FileBean fileBean) {
    this.fileBean = fileBean;
  }

  public PurchaseOrder getPurchaseOrder() {
    return purchaseOrder;
  }

  public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
    this.purchaseOrder = purchaseOrder;
  }

  public int getPerPageDefault() {
    return defaultPerPage;
  }

  public int getPageCount() {
    return invBarcodeMapPage == null ? 0 : invBarcodeMapPage.getTotalPages();
  }

  public int getResultCount() {
    return invBarcodeMapPage == null ? 0 : invBarcodeMapPage.getTotalResults();
  }

  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
    params.add("tinNumber");
    params.add("customerLogin");
    params.add("creditNoteStatus");
    params.add("warehouse");
    return params;
  }


}