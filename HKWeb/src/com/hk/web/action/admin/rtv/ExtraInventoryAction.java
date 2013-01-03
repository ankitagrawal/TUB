package com.hk.web.action.admin.rtv;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.inventory.PoLineItemService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.rtv.ExtraInventoryLineItemService;
import com.hk.admin.pact.service.rtv.RtvNoteLineItemService;
import com.hk.admin.pact.service.rtv.RtvNoteService;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.pact.service.core.WarehouseService;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.user.User;
import com.hk.pact.dao.MasterDataDao;
import com.hk.constants.rtv.EnumRtvNoteStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.accounting.PoLineItem;
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
import com.hk.domain.core.Tax;
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
  @Autowired
  PoLineItemService poLineItemService;
  @Autowired
  MasterDataDao masterDataDao;

  private List<ExtraInventoryLineItem> extraInventoryLineItems = new ArrayList<ExtraInventoryLineItem>();
  private List<ExtraInventoryLineItem> extraInventoryLineItemsSelected = new ArrayList<ExtraInventoryLineItem>();
  private List<RtvNoteLineItem> rtvNoteLineItems = new ArrayList<RtvNoteLineItem>();
  private List<Tax> taxList = new ArrayList<Tax>();
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
  private String reconciledStatus;
  private Long newPurchaseOrderId;


  @DefaultHandler
  public Resolution pre(){
    extraInventory = getExtraInventoryService().getExtraInventoryByPoId(purchaseOrderId);
    if(extraInventory!=null){
      extraInventoryLineItems= getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
    }
    purchaseOrder = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
    if(purchaseOrder!=null){
      newPurchaseOrderId = purchaseOrder.getId();
    }
    if(extraInventory != null){
      rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
      if(rtvNote!=null){
        if(rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.Reconciled.getId()) || rtvNote.isReconciled()){
          reconciledStatus = "reconciled";
        }
      }
    }
    taxList = getMasterDataDao().getTaxList();
    return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
  }

  @Secure(hasAnyPermissions = {PermissionConstants.GRN_CREATION}, authActionBean = AdminPermissionAction.class)
  public Resolution save(){

    extraInventory = getExtraInventoryService().getExtraInventoryByPoId(purchaseOrderId);
    purchaseOrder = getPurchaseOrderService().getPurchaseOrderById(purchaseOrderId);
    List<Long> skus = new ArrayList<Long>();
    for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItems){
      if(skus.size() == 0 && extraInventoryLineItem.getSku()!=null){
        skus.add(extraInventoryLineItem.getSku().getId());
      }
      else if(extraInventoryLineItem.getSku()!=null && skus.contains(extraInventoryLineItem.getSku().getId())){
        if(extraInventory!=null){
          extraInventoryLineItems = getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
            rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
            if(rtvNote!=null){
              if(rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.Reconciled.getId()) || rtvNote.isReconciled()){
                reconciledStatus = "reconciled";
              }
            }
          purchaseOrder = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
          if(purchaseOrder!=null){
            newPurchaseOrderId = purchaseOrder.getId();
          }
        }
        noCache();
        addRedirectAlertMessage(new SimpleMessage("Same Sku is present more than once !!!! "));
        return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
      }
      else if(extraInventoryLineItem.getSku()!=null){
        skus.add(extraInventoryLineItem.getSku().getId());
      }
    }
    // creating Extra Inventory
    if(extraInventory == null){
      ExtraInventory extraInventory1 = new ExtraInventory();
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
      extraInventory.setUpdateDate(new Date());
      extraInventory.setComments(comments);
      extraInventory = getExtraInventoryService().save(extraInventory);
    }
    //creating Extra Inventory Line Items
    for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItems){
      if(extraInventoryLineItem.getId()==null){
        extraInventoryLineItem.setExtraInventory(extraInventory);
        extraInventoryLineItem.setCreateDate(new Date());
        extraInventoryLineItem.setUpdateDate(new Date());
        extraInventoryLineItem.setRtvCreated(false);
        extraInventoryLineItem.setGrnCreated(false);
        getExtraInventoryLineItemService().save(extraInventoryLineItem);
      }
      else{
        extraInventoryLineItem.setUpdateDate(new Date());
        extraInventoryLineItem.setExtraInventory(extraInventory);
        getExtraInventoryLineItemService().save(extraInventoryLineItem);
      }
    }
    extraInventoryLineItems = getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
    if(extraInventory != null){
      rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
      if(rtvNote!=null){
        if(rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.Reconciled.getId()) || rtvNote.isReconciled()){
          reconciledStatus = "reconciled";
        }
      }
    }
    purchaseOrder = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
    if(purchaseOrder!=null){
      newPurchaseOrderId = purchaseOrder.getId();
    }
    noCache();
    addRedirectAlertMessage(new SimpleMessage("Changes Saved Successfully !!!! "));
    return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
  }

@Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
  public Resolution createRtv(){
    extraInventory = getExtraInventoryService().getExtraInventoryById(extraInventoryId);
    List<ExtraInventoryLineItem> extraLineItems = new ArrayList<ExtraInventoryLineItem>();
    for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected){
      if(extraInventoryLineItem!=null){
        extraInventoryLineItem = getExtraInventoryLineItemService().getExtraInventoryLineItemById(extraInventoryLineItem.getId());
        extraInventoryLineItem.setRtvCreated(true);
        extraInventoryLineItem = getExtraInventoryLineItemService().save(extraInventoryLineItem);
        extraLineItems.add(extraInventoryLineItem);
      }
    }

    extraInventoryLineItemsSelected = extraLineItems;
    List<RtvNoteLineItem> rtvNoteLineItems1 = new ArrayList<RtvNoteLineItem>();
    //creating Rtv Note
    rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
    if(rtvNote == null){
      rtvNote = new RtvNote();
      rtvNote.setExtraInventory(extraInventory);
      rtvNote.setRtvNoteStatus(EnumRtvNoteStatus.Created.asRtvNoteStatus());
      rtvNote.setReconciled(false);
      rtvNote.setDebitToSupplier(false);
      rtvNote.setCreateDate(new Date());
      rtvNote.setUpdateDate(new Date());
      if (getPrincipal() != null) {
        user = getUserService().getUserById(getPrincipal().getId());
      }
      rtvNote.setCreatedBy(user);
      rtvNote = getRtvNoteService().save(rtvNote);
    }
    else{
      rtvNoteLineItems1 =  getRtvNoteLineItemService().getRtvNoteLineItemsByRtvNote(rtvNote);
    }
    //creating Rtv Note Line Items
    for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected){
      if(extraInventoryLineItem!=null){
        RtvNoteLineItem rtvNoteLineItem  = getRtvNoteLineItemService().getRtvNoteLineItemByExtraInventoryLineItem(extraInventoryLineItem.getId());
        if(rtvNoteLineItem == null){
          rtvNoteLineItem = new RtvNoteLineItem();
          rtvNoteLineItem.setExtraInventoryLineItem(extraInventoryLineItem);
          rtvNoteLineItem.setRtvNote(rtvNote);
          rtvNoteLineItem = getRtvNoteLineItemService().save(rtvNoteLineItem);
          rtvNoteLineItems.add(rtvNoteLineItem);
        }
      }
    }
    if(rtvNoteLineItems1!=null && rtvNoteLineItems1.size()!=0){
      rtvNoteLineItems.addAll(rtvNoteLineItems1);
    }
    noCache();
    addRedirectAlertMessage(new SimpleMessage("Rtv Created !!!!"));
    return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
  }

  @Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
  public Resolution editRtvNote(){
    rtvNote = getRtvNoteService().getRtvNoteById(rtvNoteId);
    extraInventory = rtvNote.getExtraInventory();
    if(rtvNote !=null){
      rtvNote.setRemarks(comments);
      if(rtvStatus!=null && isReconciled!=null){
        if(rtvStatus.getName().equalsIgnoreCase("reconciled") || isReconciled){
          rtvNote.setReconciled(true);
          rtvNote.setRtvNoteStatus(EnumRtvNoteStatus.Reconciled.asRtvNoteStatus());
        }
        else{
          rtvNote.setReconciled(isReconciled);
          rtvNote.setRtvNoteStatus(rtvStatus.asRtvNoteStatus());
        }
      }
      rtvNote.setDebitToSupplier(isDebitToSupplier);
      rtvNote = getRtvNoteService().save(rtvNote);
    }
    noCache();
    addRedirectAlertMessage(new SimpleMessage("Changes Saved successfully !!!!"));
    return new RedirectResolution(RTVAction.class,"pre");
  }

  @Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
  public Resolution editRtv(){
    rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventoryId);
    if(rtvNote == null){
      extraInventory = getExtraInventoryService().getExtraInventoryById(extraInventoryId);
      if(extraInventory!=null){
        extraInventoryLineItems = getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
        if(extraInventory != null){
          rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
          if(rtvNote!=null){
            if(rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.Reconciled.getId()) || rtvNote.isReconciled()){
              reconciledStatus = "reconciled";
            }
          }
        }
        purchaseOrder = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
        if(purchaseOrder!=null){
          newPurchaseOrderId = purchaseOrder.getId();
        }
      }
      addRedirectAlertMessage(new SimpleMessage("No RTV Exist !!!! "));
      return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
    }
    rtvNoteLineItems = getRtvNoteLineItemService().getRtvNoteLineItemsByRtvNote(rtvNote);
    extraInventory = rtvNote.getExtraInventory();
    return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
  }

  @Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
  public Resolution createPO(){
    extraInventory = getExtraInventoryService().getExtraInventoryById(extraInventoryId);
    extraInventoryLineItems = getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
    if(extraInventory != null){
      rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
      if(rtvNote!=null){
        if(rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.Reconciled.getId()) || rtvNote.isReconciled()){
          reconciledStatus = "reconciled";
        }
      }
    }
//    List<Long> skus = new ArrayList<Long>();
    //checking if one of sku is null
    for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected){
      if(extraInventoryLineItem!=null){
        extraInventoryLineItem = getExtraInventoryLineItemService().getExtraInventoryLineItemById(extraInventoryLineItem.getId());
        if(extraInventoryLineItem.getSku()==null){
          noCache();
          addRedirectAlertMessage(new SimpleMessage("One of the selected Line Item sku is null, please Enter Sku and then press create PO !!!"));
          return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
        }
//        skus.add(extraInventoryLineItem.getSku().getId());
      }
    }
    generatePO();
    return new ForwardResolution(ExtraInventoryAction.class, "pre").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
  }

  @Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
  public Resolution generatePO(){

    purchaseOrder = getPurchaseOrderService().getPurchaseOrderById(purchaseOrderId);
    extraInventory = getExtraInventoryService().getExtraInventoryById(extraInventoryId);
    extraInventoryLineItems = getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
    if(extraInventory != null){
      rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
      if(rtvNote!=null){
        if(rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.Reconciled.getId()) || rtvNote.isReconciled()){
          reconciledStatus = "reconciled";
        }
      }
    }
    PurchaseOrder purchaseOrder1 = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
    PurchaseOrder newPurchaseOrder = new PurchaseOrder();

    if(purchaseOrder1==null){
      //Creating New PurchaseOrder and set Extra Inventory in it

      newPurchaseOrder.setExtraInventory(extraInventory);
      newPurchaseOrder.setSupplier(purchaseOrder.getSupplier());
      newPurchaseOrder.setCreateDate(new Date());
      newPurchaseOrder.setUpdateDate(new Date());
      newPurchaseOrder.setAdvPayment(0.0D);
      newPurchaseOrder.setApprovalDate(new Date());
      newPurchaseOrder.setDiscount(0.0D);
      newPurchaseOrder.setPoPlaceDate(new Date());
      newPurchaseOrder.setWarehouse(purchaseOrder.getWarehouse());
      newPurchaseOrder.setSurchargeAmount(purchaseOrder.getSurchargeAmount());
      newPurchaseOrder.setTaxAmount(purchaseOrder.getTaxAmount());
      newPurchaseOrder.setTaxableAmount(purchaseOrder.getTaxableAmount());
      if (getPrincipal() != null) {
        user = getUserService().getUserById(getPrincipal().getId());
      }
      newPurchaseOrder.setCreatedBy(user);
      newPurchaseOrder.setPurchaseOrderStatus(getBaseDao().get(PurchaseOrderStatus.class, EnumPurchaseOrderStatus.Generated.getId()));
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getLeadTime());
      newPurchaseOrder.setEstDelDate(calendar.getTime());
      if (purchaseOrder.getSupplier().getCreditDays() != null && purchaseOrder.getSupplier().getCreditDays() >= 0) {
        calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getCreditDays());
        newPurchaseOrder.setEstPaymentDate(calendar.getTime());
      } else {
        newPurchaseOrder.setEstPaymentDate(new Date());
      }
      newPurchaseOrder = getPurchaseOrderService().save(newPurchaseOrder);
    }
    else{
      newPurchaseOrder = purchaseOrder1;
      if(!newPurchaseOrder.getPurchaseOrderStatus().getId().equals(EnumPurchaseOrderStatus.Generated.getId())){
        purchaseOrder = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
        if(purchaseOrder!=null){
          newPurchaseOrderId = purchaseOrder.getId();
        }
        noCache();
        addRedirectAlertMessage(new SimpleMessage("PO is not in Generated State"));
        return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
      }
    }

    //Creating new POLine Items and set Extra inventory Line Items Id in it

    for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected){

      if(extraInventoryLineItem!=null){
        extraInventoryLineItem = getExtraInventoryLineItemService().getExtraInventoryLineItemById(extraInventoryLineItem.getId());
        extraInventoryLineItem.setGrnCreated(true);
        extraInventoryLineItem = getExtraInventoryLineItemService().save(extraInventoryLineItem);
        PoLineItem poLineItem = new PoLineItem();
        poLineItem.setExtraInventoryLineItem(extraInventoryLineItem);
        poLineItem.setCostPrice(extraInventoryLineItem.getCostPrice());
        poLineItem.setMrp(extraInventoryLineItem.getMrp());
        poLineItem.setQty(extraInventoryLineItem.getReceivedQty());
        poLineItem.setReceivedQty(extraInventoryLineItem.getReceivedQty());
        poLineItem.setSku(extraInventoryLineItem.getSku());
        poLineItem.setPurchaseOrder(newPurchaseOrder);
        poLineItem.setDiscountPercent(0.0D);
        poLineItem.setTaxAmount(extraInventoryLineItem.getSku().getTax().getValue());
        poLineItem.setTaxableAmount(0.0D);
        poLineItem.setSurchargeAmount(0.0D);
        poLineItem = getPoLineItemService().save(poLineItem);
      }
    }
    purchaseOrder = getPurchaseOrderService().getPurchaseOrderByExtraInventory(extraInventory);
    if(purchaseOrder!=null){
      newPurchaseOrderId = purchaseOrder.getId();
    }
    noCache();
    addRedirectAlertMessage(new SimpleMessage("PO and PoLine Item has been created !!! with New PO ID - " + newPurchaseOrder.getId() ));
    return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
  }

  @Secure(hasAnyPermissions = {PermissionConstants.PO_MANAGEMENT}, authActionBean = AdminPermissionAction.class)
  public Resolution editRtvNoteLineItems(){
    rtvNote = getRtvNoteService().getRtvNoteById(rtvNoteId);
    extraInventory = rtvNote.getExtraInventory();
    rtvNoteLineItems = getRtvNoteLineItemService().getRtvNoteLineItemsByRtvNote(rtvNote);
    return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId",purchaseOrderId);
  }

  @SuppressWarnings("unchecked")
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
      sku = getSkuService().findSKU(pv,wareHouse);
      if(sku!=null){
        dataMap.put("sku",sku);
        dataMap.put("productName",sku.getProductVariant().getProduct().getName());
        healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Product Variant",dataMap);
      }
      else{
        healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Sku Not present for this Variant Id");
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

  public String getReconciledStatus() {
    return reconciledStatus;
  }

  public void setReconciledStatus(String reconciledStatus) {
    this.reconciledStatus = reconciledStatus;
  }

  public PoLineItemService getPoLineItemService() {
    return poLineItemService;
  }

  public Long getNewPurchaseOrderId() {
    return newPurchaseOrderId;
  }

  public void setNewPurchaseOrderId(Long newPurchaseOrderId) {
    this.newPurchaseOrderId = newPurchaseOrderId;
  }

  public List<Tax> getTaxList() {
    return taxList;
  }

  public void setTaxList(List<Tax> taxList) {
    this.taxList = taxList;
  }

  public MasterDataDao getMasterDataDao() {
    return masterDataDao;
  }
}
