package com.hk.web.action.admin.rtv;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.rtv.ExtraInventoryLineItemService;
import com.hk.admin.pact.service.rtv.RtvNoteLineItemService;
import com.hk.admin.pact.service.rtv.RtvNoteService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.hk.constants.rtv.EnumRtvNoteStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.admin.pact.service.rtv.ExtraInventoryService;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItem;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.constants.core.PermissionConstants;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.domain.sku.Sku;
import com.hk.domain.inventory.rtv.RtvNote;
import com.hk.domain.inventory.rtv.RtvNoteLineItem;
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
  @Autowired
  PurchaseOrderService purchaseOrderService;
  @Autowired
  RtvNoteService rtvNoteService;
  @Autowired
  RtvNoteLineItemService rtvNoteLineItemService;

  private List<ExtraInventoryLineItem> extraInventoryLineItems = new ArrayList<ExtraInventoryLineItem>();
  private List<ExtraInventoryLineItem> extraInventoryLineItemsSelected = new ArrayList<ExtraInventoryLineItem>();
  private List<RtvNoteLineItem> rtvNoteLineItems = new ArrayList<RtvNoteLineItem>();
  private Integer defaultPerPage = 20;
  Page purchaseOrderPage;
  private Long purchaseOrderId;
  private ExtraInventory extraInventory;
  private PurchaseOrder purchaseOrder;
  private Long wareHouseId;
  private String comments;
  private String productVariantId;
  private Long extraInventoryId;
  private User user;
  private RtvNote rtvNote;
  private Long rtvNoteId;
  private EnumRtvNoteStatus rtvStatus;
  private Boolean isDebitToSupplier;
  private Boolean isReconciled;

  @DefaultHandler
  public Resolution pre(){
    extraInventory = getExtraInventoryService().getExtraInventoryByPoId(purchaseOrderId);
    if(extraInventory!=null){
      extraInventoryLineItems= getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
    }
    return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
  }

  public Resolution save(){
    // creating Extra Inventory
    if(extraInventoryId==null){
      ExtraInventory extraInventory1 = new ExtraInventory();
      purchaseOrder = getPurchaseOrderService().getPurchaseOrderById(purchaseOrderId);
      if(purchaseOrder!=null){
        extraInventory1.setPurchaseOrder(purchaseOrder);
      }
      extraInventory1.setComments(comments);
      extraInventory1.setCreateDate(new Date());
      extraInventory1.setUpdateDate(new Date());
      if (getPrincipal() != null) {
        user = getUserService().getUserById(getPrincipal().getId());
      }
      extraInventory1.setCreatedBy(user);
      extraInventory = getExtraInventoryService().save(extraInventory1);
    }
    else{
      extraInventory = getExtraInventoryService().getExtraInventoryById(extraInventoryId);
      extraInventory.setUpdateDate(new Date());
      extraInventory.setComments(comments);
      extraInventory = getExtraInventoryService().save(extraInventory);
    }
     purchaseOrder.setExtraInventory(extraInventory);
     purchaseOrder = getPurchaseOrderService().save(purchaseOrder);
    //creating Extra Inventory Line Items
    for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItems){
      if(extraInventoryLineItem.getId()==null){
        extraInventoryLineItem.setExtraInventory(extraInventory);
        extraInventoryLineItem.setCreateDate(new Date());
        extraInventoryLineItem.setUpdateDate(new Date());
        getExtraInventoryLineItemService().save(extraInventoryLineItem);
      }
      else{
        extraInventoryLineItem.setUpdateDate(new Date());
        extraInventoryLineItem.setExtraInventory(extraInventory);
        getExtraInventoryLineItemService().save(extraInventoryLineItem);
      }
    }
    extraInventoryLineItems = getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
    addRedirectAlertMessage(new SimpleMessage("Changes Saved Successfully !!!! "));
    return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
  }

  public Resolution createRtv(){
     extraInventory = getExtraInventoryService().getExtraInventoryById(extraInventoryId);
    List<ExtraInventoryLineItem> extraLineItems = new ArrayList<ExtraInventoryLineItem>();
    for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected){
      extraInventoryLineItem = getExtraInventoryLineItemService().getExtraInventoryLineItemById(extraInventoryLineItem.getId());
          extraInventoryLineItem.setRtvCreated(true);
          extraInventoryLineItem = getExtraInventoryLineItemService().save(extraInventoryLineItem);
          extraLineItems.add(extraInventoryLineItem);
     }

    extraInventoryLineItemsSelected = extraLineItems;

    //creating Rtv Note
     rtvNote = new RtvNote();
     rtvNote.setExtraInventory(extraInventory);
     rtvNote.setRtvNoteStatus(EnumRtvNoteStatus.Created.asRtvNoteStatus());
     rtvNote.setCreateDate(new Date());
     rtvNote.setUpdateDate(new Date());
    if (getPrincipal() != null) {
        user = getUserService().getUserById(getPrincipal().getId());
      }
     rtvNote.setCreatedBy(user);
     rtvNote = getRtvNoteService().save(rtvNote);

      //creating Rtv Note Line Items
      for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected){
            RtvNoteLineItem rtvNoteLineItem = new RtvNoteLineItem();
            rtvNoteLineItem.setExtraInventoryLineItem(extraInventoryLineItem);
            rtvNoteLineItem.setRtvNote(rtvNote);
            rtvNoteLineItem = getRtvNoteLineItemService().save(rtvNoteLineItem);
            rtvNoteLineItems.add(rtvNoteLineItem);
      }
    addRedirectAlertMessage(new SimpleMessage("Rtv Created !!!!"));
    return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId",purchaseOrderId);
  }

  public Resolution editRtvNote(){
   rtvNote = getRtvNoteService().getRtvNoteById(rtvNoteId);
    extraInventory = rtvNote.getExtraInventory();
    if(rtvNote !=null){
      rtvNote.setRemarks(comments);
      rtvNote.setRtvNoteStatus(rtvStatus.asRtvNoteStatus());
      rtvNote.setDebitToSupplier(isDebitToSupplier);
      rtvNote.setReconciled(isReconciled);
      rtvNote = getRtvNoteService().save(rtvNote);
    }
    rtvNoteLineItems = getRtvNoteLineItemService().getRtvNoteLineItemsByRtvNote(rtvNote);
    addRedirectAlertMessage(new SimpleMessage("Changes Saved successfully !!!!"));
    return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId",purchaseOrderId);
  }
  public Resolution editRtv(){
    rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventoryId);
    rtvNoteLineItems = getRtvNoteLineItemService().getRtvNoteLineItemsByRtvNote(rtvNote);
    extraInventory = rtvNote.getExtraInventory();
    return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId",purchaseOrderId);
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

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public PurchaseOrderService getPurchaseOrderService() {
    return purchaseOrderService;
  }

  public Long getExtraInventoryId() {
    return extraInventoryId;
  }

  public void setExtraInventoryId(Long extraInventoryId) {
    this.extraInventoryId = extraInventoryId;
  }

  public RtvNote getRtvNote() {
    return rtvNote;
  }

  public void setRtvNote(RtvNote rtvNote) {
    this.rtvNote = rtvNote;
  }

  public RtvNoteService getRtvNoteService() {
    return rtvNoteService;
  }

  public RtvNoteLineItemService getRtvNoteLineItemService() {
    return rtvNoteLineItemService;
  }

  public List<RtvNoteLineItem> getRtvNoteLineItems() {
    return rtvNoteLineItems;
  }

  public void setRtvNoteLineItems(List<RtvNoteLineItem> rtvNoteLineItems) {
    this.rtvNoteLineItems = rtvNoteLineItems;
  }

  public Long getRtvNoteId() {
    return rtvNoteId;
  }

  public void setRtvNoteId(Long rtvNoteId) {
    this.rtvNoteId = rtvNoteId;
  }

  public EnumRtvNoteStatus getRtvStatus() {
    return rtvStatus;
  }

  public void setRtvStatus(EnumRtvNoteStatus rtvStatus) {
    this.rtvStatus = rtvStatus;
  }

  public Boolean isReconciled() {
    return isReconciled;
  }

  public void setReconciled(Boolean reconciled) {
    isReconciled = reconciled;
  }

  public Boolean isDebitToSupplier() {
    return isDebitToSupplier;
  }

  public void setDebitToSupplier(Boolean debitToSupplier) {
    isDebitToSupplier = debitToSupplier;
  }
}
