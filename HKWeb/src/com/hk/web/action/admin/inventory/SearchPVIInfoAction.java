package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.List;

@Secure(hasAnyPermissions = {PermissionConstants.INVENTORY_CHECKIN}, authActionBean = AdminPermissionAction.class)
@Component
public class SearchPVIInfoAction extends BaseAction {

  private static Logger logger = Logger.getLogger(SearchPVIInfoAction.class);

  @Autowired
  SkuItemDao skuItemDao;


  @Validate(required = true)
  private String barcode;

  private List<ProductVariantInventory> pviList;

  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/searchPVIInfo.jsp");
  }

  public Resolution search() {
    pviList = skuItemDao.getPVIInfo(barcode);
    if (pviList == null) {
      addRedirectAlertMessage(new SimpleMessage("Wrong Barcode"));
    } else if (pviList.isEmpty()) {
      addRedirectAlertMessage(new SimpleMessage("No PVI records"));
    }
    return new ForwardResolution("/pages/admin/searchPVIInfo.jsp");
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public List<ProductVariantInventory> getPviList() {
    return pviList;
  }

  public void setPviList(List<ProductVariantInventory> pviList) {
    this.pviList = pviList;
  }
}