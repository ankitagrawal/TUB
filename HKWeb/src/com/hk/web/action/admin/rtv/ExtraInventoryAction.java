package com.hk.web.action.admin.rtv;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.rtv.ExtraInventoryLineItemService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.domain.warehouse.Warehouse;
import com.hk.admin.pact.service.rtv.ExtraInventoryService;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.constants.core.PermissionConstants;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.domain.sku.Sku;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 19, 2012
 * Time: 1:50:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
@Component
public class ExtraInventoryAction extends BasePaginatedAction{

  @Autowired
  ExtraInventoryService extraInventoryService;
  @Autowired
  ExtraInventoryLineItemService extraInventoryLineItemService;
  @Autowired
	private ProductVariantService productVariantService;
  @Autowired
  SkuService skuService;
  @Autowired
  WarehouseService wareHouseService;

  private List<ExtraInventoryLineItem> extraInventoryLineItems = new ArrayList<ExtraInventoryLineItem>();
  private List<ExtraInventoryLineItem> extraInventoryLineItemsSelected = new ArrayList<ExtraInventoryLineItem>();
  private Integer defaultPerPage = 20;
  Page purchaseOrderPage;
  private Long purchaseOrderId;
  private ExtraInventory extraInventory;
  private Long wareHouseId;
  private String productVariantId;

  @DefaultHandler
  public Resolution pre(){
    extraInventory = getExtraInventoryService().getExtraInventoryByPoId(purchaseOrderId);
    if(extraInventory!=null){
    extraInventoryLineItems= getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
    }
     return new RedirectResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
  }

  public Resolution save(){

    addRedirectAlertMessage(new SimpleMessage("rukja abhi kuch save nahi kar raha hu abhi to bas second phase testing chal rahi hai"));
   return new RedirectResolution("/pages/admin/extraInventoryItems.jsp");
  }

  public Resolution getSku(){
    HealthkartResponse healthkartResponse = null;
     Warehouse wareHouse = null;
     Sku sku = null;
    Map dataMap = new HashMap();
    ProductVariant pv = getProductVariantService().getVariantById(productVariantId);
            if(wareHouseId!=null){
            wareHouse = getWareHouseService().getWarehouseById(wareHouseId);
            }
       else {
              healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "There Came an Error, please try again later");
           noCache();
            }
                if(pv!=null){
                  sku = getSkuService().getSKU(pv,wareHouse);
                  if(sku!=null){
                    dataMap.put("sku",sku);
                    healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Product Variant",dataMap);
                  }
                  else{
                    healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Variant Id");
                  }
                }
    else{
          healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Variant Id");
                  noCache();
                }
					return new JsonResolution(healthkartResponse);
  }
  	public int getPageCount() {
		return purchaseOrderPage == null ? 0 : purchaseOrderPage.getTotalPages();
	}

	public int getResultCount() {
		return purchaseOrderPage == null ? 0 : purchaseOrderPage.getTotalResults();
	}

  public int getPerPageDefault() {
		return defaultPerPage;
	}

  public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		
		return params;
	}

  public ExtraInventoryService getExtraInventoryService() {
    return extraInventoryService;
  }

  public ExtraInventoryLineItemService getExtraInventoryLineItemService() {
    return extraInventoryLineItemService;
  }

  public Long getPurchaseOrderId() {
    return purchaseOrderId;
  }

  public void setPurchaseOrderId(Long purchaseOrderId) {
    this.purchaseOrderId = purchaseOrderId;
  }

  public List<ExtraInventoryLineItem> getExtraInventoryLineItems() {
    return extraInventoryLineItems;
  }

  public void setExtraInventoryLineItems(List<ExtraInventoryLineItem> extraInventoryLineItems) {
    this.extraInventoryLineItems = extraInventoryLineItems;
  }

  public List<ExtraInventoryLineItem> getExtraInventoryLineItemsSelected() {
    return extraInventoryLineItemsSelected;
  }

  public void setExtraInventoryLineItemsSelected(List<ExtraInventoryLineItem> extraInventoryLineItemsSelected) {
    this.extraInventoryLineItemsSelected = extraInventoryLineItemsSelected;
  }

  public ExtraInventory getExtraInventory() {
    return extraInventory;
  }

  public void setExtraInventory(ExtraInventory extraInventory) {
    this.extraInventory = extraInventory;
  }

  public Long getWareHouseId() {
    return wareHouseId;
  }

  public void setWareHouseId(Long wareHouseId) {
    this.wareHouseId = wareHouseId;
  }

  public String getProductVariantId() {
    return productVariantId;
  }

  public void setProductVariantId(String productVariantId) {
    this.productVariantId = productVariantId;
  }

  public ProductVariantService getProductVariantService() {
    return productVariantService;
  }

  public SkuService getSkuService() {
    return skuService;
  }

  public WarehouseService getWareHouseService() {
    return wareHouseService;
  }
}
