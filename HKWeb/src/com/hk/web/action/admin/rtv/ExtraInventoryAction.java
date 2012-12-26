package com.hk.web.action.admin.rtv;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.service.inventory.GrnLineItemService;
import com.hk.admin.pact.service.inventory.PoLineItemService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.rtv.ExtraInventoryLineItemService;
import com.hk.admin.pact.service.rtv.RtvNoteLineItemService;
import com.hk.admin.pact.service.rtv.RtvNoteService;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.pact.service.core.WarehouseService;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.constants.inventory.EnumGrnStatus;
import com.hk.domain.user.User;
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
  @Autowired
  PoLineItemService poLineItemService;
  @Autowired
  GoodsReceivedNoteDao goodsReceivedNoteDao;
  @Autowired
  GrnLineItemService grnLineItemService;

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
  private String reconciledStatus;
  private AdminEmailManager adminEmailManager;

  @DefaultHandler
  public Resolution pre(){
    extraInventory = getExtraInventoryService().getExtraInventoryByPoId(purchaseOrderId);
    if(extraInventory!=null){
      extraInventoryLineItems= getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
    }
    if(extraInventory != null){
      rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventory.getId());
      if(rtvNote!=null){
        if(rtvNote.getRtvNoteStatus().getId().equals(EnumRtvNoteStatus.Reconciled.getId()) || rtvNote.isReconciled()){
          reconciledStatus = "reconciled";
        }
      }
    }
    return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
  }

  public Resolution save(){

    extraInventory = getExtraInventoryService().getExtraInventoryByPoId(purchaseOrderId);
    purchaseOrder = getPurchaseOrderService().getPurchaseOrderById(purchaseOrderId);
    List<Long> skus = new ArrayList<Long>();
    for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItems){
      if(skus.size() == 0){
        skus.add(extraInventoryLineItem.getSku().getId());
      }
      else if(skus.contains(extraInventoryLineItem.getSku().getId())){
        extraInventoryLineItems = getExtraInventoryLineItemService().getExtraInventoryLineItemsByExtraInventoryId(extraInventory.getId());
        noCache();
        addRedirectAlertMessage(new SimpleMessage("Same Sku is present more than once !!!! "));
        return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
      }
      else{
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
    noCache();
    addRedirectAlertMessage(new SimpleMessage("Changes Saved Successfully !!!! "));
    return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
  }

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
    return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId",purchaseOrderId);
  }

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
    rtvNoteLineItems = getRtvNoteLineItemService().getRtvNoteLineItemsByRtvNote(rtvNote);
    noCache();
    addRedirectAlertMessage(new SimpleMessage("Changes Saved successfully !!!!"));
    return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId",purchaseOrderId);
  }


  public Resolution editRtv(){
    rtvNote = getRtvNoteService().getRtvNoteByExtraInventory(extraInventoryId);
    rtvNoteLineItems = getRtvNoteLineItemService().getRtvNoteLineItemsByRtvNote(rtvNote);
    extraInventory = rtvNote.getExtraInventory();
    return new ForwardResolution("/pages/admin/createRtvNote.jsp").addParameter("purchaseOrderId",purchaseOrderId);
  }
  @Secure(hasAnyPermissions = {PermissionConstants.GRN_CREATION}, authActionBean = AdminPermissionAction.class)
  public Resolution createGRN(){
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
          addRedirectAlertMessage(new SimpleMessage("One of the selected Line Item sku is null, please Enter Sku and then press create GRN !!!"));
          return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
        }
//        skus.add(extraInventoryLineItem.getSku().getId());
      }
    }
    purchaseOrder = getPurchaseOrderService().getPurchaseOrderById(purchaseOrderId);
    //checking if one of the selected sku has already been created under this PO
//    if(purchaseOrder.getGoodsReceivedNotes()!=null && purchaseOrder.getGoodsReceivedNotes().size()!=0){
//      for(GoodsReceivedNote goodsReceivedNote : purchaseOrder.getGoodsReceivedNotes()){
//        if(goodsReceivedNote.getGrnLineItems()!=null && goodsReceivedNote.getGrnLineItems().size()!=0){
//          for(GrnLineItem grnLineItem : goodsReceivedNote.getGrnLineItems()){
//            if(skus.contains(grnLineItem.getSku().getId())){
//              noCache();
//              addRedirectAlertMessage(new SimpleMessage("Grn of one of the selected Line Item is already created under this PO !!!!"));
//              return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
//            }
//          }
//        }
//      }
//    }
    noCache();
    return new ForwardResolution(ExtraInventoryAction.class, "generateGRN").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId).addParameter("extraInventoryLineItemsSelected",extraInventoryLineItemsSelected);
  }

  @Secure(hasAnyPermissions = {PermissionConstants.GRN_CREATION}, authActionBean = AdminPermissionAction.class)
  public Resolution generateGRN(){
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
    List<GoodsReceivedNote> gRNs = getGoodsReceivedNoteDao().getGRNByPO(purchaseOrder1);
    if(gRNs==null || gRNs.size()==0){
      //Creating New PurchaseOrder and set Extra Inventory in it
      PurchaseOrder newPurchaseOrder = new PurchaseOrder();
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
      newPurchaseOrder.setApprovedBy(purchaseOrder.getApprovedBy());
      newPurchaseOrder.setPurchaseOrderStatus(getBaseDao().get(PurchaseOrderStatus.class, EnumPurchaseOrderStatus.Approved.getId()));
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

      //Generating Goods Received Note

      GoodsReceivedNote goodsReceivedNote = new GoodsReceivedNote();
      goodsReceivedNote.setGrnDate(new Date());
      goodsReceivedNote.setPurchaseOrder(newPurchaseOrder);
      goodsReceivedNote.setReconciled(false);
      goodsReceivedNote.setReceivedBy(user);
      goodsReceivedNote.setWarehouse(newPurchaseOrder.getWarehouse());
      goodsReceivedNote.setDiscount(0.0D);
      goodsReceivedNote.setTaxAmount(newPurchaseOrder.getTaxAmount());
      goodsReceivedNote.setTaxableAmount(newPurchaseOrder.getTaxableAmount());
      goodsReceivedNote.setCreateDate(new Date());
      goodsReceivedNote.setSurchargeAmount(newPurchaseOrder.getSurchargeAmount());
      goodsReceivedNote.setEstPaymentDate(newPurchaseOrder.getEstPaymentDate());
      goodsReceivedNote.setGrnStatus(EnumGrnStatus.GoodsReceived.asGrnStatus());
      goodsReceivedNote.setInvoiceNumber("-");
      goodsReceivedNote.setInvoiceDate(new Date());
      goodsReceivedNote = getGoodsReceivedNoteDao().save(goodsReceivedNote);

      //Creating new POLine Items and set Extra inventory Line Items Id in it
      //Parallel creating grn line items also
      for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected){
        if(extraInventoryLineItem!=null){
          extraInventoryLineItem = getExtraInventoryLineItemService().getExtraInventoryLineItemById(extraInventoryLineItem.getId());
          extraInventoryLineItem.setGrnCreated(true);
          extraInventoryLineItem = getExtraInventoryLineItemService().save(extraInventoryLineItem);
          PoLineItem poLineItem = new PoLineItem();
          GrnLineItem grnLineItem = new GrnLineItem();
          poLineItem.setExtraInventoryLineItem(extraInventoryLineItem);
          poLineItem.setCostPrice(extraInventoryLineItem.getCostPrice());
          grnLineItem.setCostPrice(extraInventoryLineItem.getCostPrice());
          poLineItem.setMrp(extraInventoryLineItem.getMrp());
          grnLineItem.setMrp(extraInventoryLineItem.getMrp());
          poLineItem.setQty(extraInventoryLineItem.getReceivedQty());
          grnLineItem.setQty(extraInventoryLineItem.getReceivedQty());
          poLineItem.setReceivedQty(extraInventoryLineItem.getReceivedQty());
          grnLineItem.setCheckedInQty(extraInventoryLineItem.getReceivedQty());
          poLineItem.setSku(extraInventoryLineItem.getSku());
          grnLineItem.setSku(extraInventoryLineItem.getSku());
          poLineItem.setPurchaseOrder(newPurchaseOrder);
          grnLineItem.setGoodsReceivedNote(goodsReceivedNote);
          poLineItem.setDiscountPercent(0.0D);
          grnLineItem.setDiscountPercent(0.0D);
          poLineItem.setTaxAmount(newPurchaseOrder.getTaxAmount());
          grnLineItem.setTaxAmount(goodsReceivedNote.getTaxAmount());
          poLineItem.setTaxableAmount(newPurchaseOrder.getTaxableAmount());
          grnLineItem.setTaxableAmount(goodsReceivedNote.getTaxableAmount());
          poLineItem.setSurchargeAmount(newPurchaseOrder.getSurchargeAmount());
          grnLineItem.setSurchargeAmount(goodsReceivedNote.getSurchargeAmount());
          poLineItem = getPoLineItemService().save(poLineItem);
          grnLineItem = getGrnLineItemService().save(grnLineItem);
        }
      }
      //getAdminEmailManager().sendGRNEmail(goodsReceivedNote);
    }
    else{
      GoodsReceivedNote goodsReceivedNote = gRNs.get(0);
      for(ExtraInventoryLineItem extraInventoryLineItem : extraInventoryLineItemsSelected){
        if(extraInventoryLineItem!=null){
          PoLineItem poLineItem = new PoLineItem();
          GrnLineItem grnLineItem = new GrnLineItem();
          poLineItem.setExtraInventoryLineItem(extraInventoryLineItem);
          poLineItem.setCostPrice(extraInventoryLineItem.getCostPrice());
          grnLineItem.setCostPrice(extraInventoryLineItem.getCostPrice());
          poLineItem.setMrp(extraInventoryLineItem.getMrp());
          grnLineItem.setMrp(extraInventoryLineItem.getMrp());
          poLineItem.setQty(extraInventoryLineItem.getReceivedQty());
          grnLineItem.setQty(extraInventoryLineItem.getReceivedQty());
          poLineItem.setReceivedQty(extraInventoryLineItem.getReceivedQty());
          grnLineItem.setCheckedInQty(extraInventoryLineItem.getReceivedQty());
          poLineItem.setSku(extraInventoryLineItem.getSku());
          grnLineItem.setSku(extraInventoryLineItem.getSku());
          poLineItem.setPurchaseOrder(purchaseOrder1);
          grnLineItem.setGoodsReceivedNote(goodsReceivedNote);
          poLineItem.setDiscountPercent(0.0D);
          grnLineItem.setDiscountPercent(0.0D);
          poLineItem.setTaxAmount(purchaseOrder1.getTaxAmount());
          grnLineItem.setTaxAmount(goodsReceivedNote.getTaxAmount());
          poLineItem.setTaxableAmount(purchaseOrder1.getTaxableAmount());
          grnLineItem.setTaxableAmount(goodsReceivedNote.getTaxableAmount());
          poLineItem = getPoLineItemService().save(poLineItem);
          grnLineItem = getGrnLineItemService().save(grnLineItem);
        }
      }
    }

    noCache();
    addRedirectAlertMessage(new SimpleMessage("Grn Has been created !!!"));
    return new ForwardResolution("/pages/admin/extraInventoryItems.jsp").addParameter("purchaseOrderId",purchaseOrderId).addParameter("wareHouseId",wareHouseId);
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

  public String getReconciledStatus() {
    return reconciledStatus;
  }

  public void setReconciledStatus(String reconciledStatus) {
    this.reconciledStatus = reconciledStatus;
  }

  public PoLineItemService getPoLineItemService() {
    return poLineItemService;
  }

  public GoodsReceivedNoteDao getGoodsReceivedNoteDao() {
    return goodsReceivedNoteDao;
  }

  public GrnLineItemService getGrnLineItemService() {
    return grnLineItemService;
  }

  public AdminEmailManager getAdminEmailManager() {
    return adminEmailManager;
  }
}
