package com.hk.web.action.admin.inventory.checkin;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.BinManager;
import com.hk.admin.pact.dao.warehouse.BinDao;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.Bin;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.web.action.admin.inventory.InventoryCheckinAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: May 18, 2012
 * Time: 9:40:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class InventoryBinAllocationAction extends BaseAction {
   private static Logger logger = Logger.getLogger(InventoryCheckinAction.class);
  @Autowired
  SkuGroupService skuGroupService;


   @Autowired
  BinManager binManager;
  @Autowired
  BinDao binDao;
   @Autowired
  UserService userService;
 @Autowired
  SkuItemDao skuItemDao;
    private Bin bin;

  @Validate(required = true, on = "saveBin")
  private String barcode;
  @Validate(required = true, on = "saveBin")
  private String location;
   private SkuGroup skuGroup = null;


  @DefaultHandler
  public Resolution pre() {

    return new ForwardResolution("/pages/admin/inventoryBinAllocation.jsp");
  }

                                             

  public Resolution save() {
    if (StringUtils.isBlank(barcode)) {
      addRedirectAlertMessage(new SimpleMessage("Please enter the barcode"));
       return new ForwardResolution("/pages/admin/inventoryBinAllocation.jsp").addParameter("saved","false");
    }
    if (StringUtils.isBlank(location)) {

      addRedirectAlertMessage(new SimpleMessage("Please enter the Bin Location"));
       return new ForwardResolution("/pages/admin/inventoryBinAllocation.jsp").addParameter("saved","false");
    }
    List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
    skuItemStatusList.add(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
    skuItemStatusList.add(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
    skuItemStatusList.add(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
    this.skuGroup = skuGroupService.getInStockSkuGroup(barcode, userService.getWarehouseForLoggedInUser().getId(), skuItemStatusList);
    if (skuGroup != null) {
   ProductVariant productVariant = skuGroup.getSku().getProductVariant();
      if (productVariant == null) {
        addRedirectAlertMessage(new SimpleMessage("No Product Variant found with barcode : " + barcode));
        logger.error("No Product Variant found with barcode : " + barcode);
        return new ForwardResolution(InventoryBinAllocationAction.class).addParameter("saved","false");
      }
       boolean  status = binManager.assignBinToVariant(location,skuGroup);
      String message="";
      if(status){
         message = " \"SAVED!!  bin assigned !\"";
        addRedirectAlertMessage(new SimpleMessage( message+ " for Barcode  " + barcode + " for product " + productVariant.getProduct().getName()+"at Location "+ location));
       logger.info("Bin has been added for barcode : "+ barcode+"  for Product Variant :  "+ productVariant.getProduct().getName());
        return new RedirectResolution("/pages/admin/inventoryBinAllocation.jsp").addParameter("saved","true");
      }
      else{
        message = " \"Location is not Valid !\"";
        addRedirectAlertMessage(new SimpleMessage( message+ " :::::  Barcode Entered  " + barcode + " ::::Product Name  " + productVariant.getProduct().getName()));
       logger.info("Invalid Location  : "+ barcode+"  for Product Variant :  "+ productVariant.getProduct().getName());
        return new RedirectResolution("/pages/admin/inventoryBinAllocation.jsp").addParameter("saved","false");
      }

          }

    addRedirectAlertMessage(new SimpleMessage("There are no batches for Barcode  " + barcode + " Either Invalid barcode -->  Barcode should start with HK  or No such product  in Inventory,  "));
     logger.error("There are no batches for Barcode  " + barcode + " for product  in Inventory , Barcode should start with HK ");
    return new ForwardResolution("/pages/admin/inventoryBinAllocation.jsp").addParameter("saved","false");

  }


  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public Bin getBin() {
    return bin;
  }

  public void setBin(Bin bin) {
    this.bin = bin;
  }


  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}

