package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.pact.service.inventory.PoLineItemService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 11/19/12
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

  @Autowired
  PurchaseOrderDao purchaseOrderDao;
  @Autowired
  PoLineItemDao poLineItemDao;
  @Autowired
  PoLineItemService poLineItemService;
  @Autowired
  UserService userService;
  @Autowired
  SupplierDao supplierDao;
  @Autowired
  SkuService skuService;
  @Autowired
  BaseDao baseDao;

  public void updatePOFillRate(PurchaseOrder purchaseOrder) {
    long totalAskedQty = 0;
    long totalReceivedQty = 0;
    double fillRate = 0.0;

    for (GoodsReceivedNote grn : purchaseOrder.getGoodsReceivedNotes()) {
      for (GrnLineItem grnLineItem : grn.getGrnLineItems()) {
        getPoLineItemService().updatePoLineItemFillRate(grn, grnLineItem, grnLineItem.getCheckedInQty());
      }
    }

    for (PoLineItem poLineItem : purchaseOrder.getPoLineItems()) {
      totalAskedQty += poLineItem.getQty();
      if (poLineItem.getReceivedQty() != null) {
        totalReceivedQty += poLineItem.getReceivedQty();
      }
    }

    if (totalAskedQty > 0) {
      fillRate = (totalReceivedQty * 100.0) / totalAskedQty;
    }
    purchaseOrder.setFillRate(fillRate);
    getPurchaseOrderDao().saveOrUpdate(purchaseOrder);

  }

  public PurchaseOrder save(PurchaseOrder purchaseOrder) {
    return (PurchaseOrder) getPurchaseOrderDao().save(purchaseOrder);
  }

  public PurchaseOrder getPurchaseOrderById(Long purchaseOrderId) {
    return (PurchaseOrder) getPurchaseOrderDao().findUniqueByNamedQueryAndNamedParam("getPurchaseOrderById", new String[]{"purchaseOrderId"}, new Object[]{purchaseOrderId});
  }

  public PurchaseOrder getPurchaseOrderByExtraInventory(ExtraInventory extraInventory) {
    return (PurchaseOrder) getPurchaseOrderDao().findUniqueByNamedQueryAndNamedParam("getPurchaseOrderByExtraInventory", new String[]{"extraInventory"}, new Object[]{extraInventory});
  }

  @SuppressWarnings("unchecked")
  public List<PurchaseOrder> getAllPurchaseOrderByExtraInventory() {
    return (List<PurchaseOrder>) getPurchaseOrderDao().findByNamedQuery("getAllPurchaseOrderByExtraInventory");
  }

  public List<ProductVariant> getAllProductVariantFromPO(PurchaseOrder po) {
    return (List<ProductVariant>) getPurchaseOrderDao().getAllProductVariantFromPO(po);
  }

  @Override
  public List<ShippingOrder> getCancelledShippingOrderFromSoPo() {
    return (List<ShippingOrder>) getPurchaseOrderDao().getCancelledShippingOrderFromSoPo();

  }

  @Override
  public List<PurchaseOrder> createPOsForBright(Map<Warehouse, Set<ProductVariant>> whPVMap) {
    List<PurchaseOrder> poList = new ArrayList<PurchaseOrder>();

    for (Map.Entry<Warehouse, Set<ProductVariant>> warehouseSetEntry : whPVMap.entrySet()) {
      PurchaseOrder purchaseOrder = new PurchaseOrder();
      User loggedOnUser = getUserService().getLoggedInUser();
      purchaseOrder.setApprovedBy(loggedOnUser);
      purchaseOrder.setApprovalDate(new Date());
      purchaseOrder.setCreateDate(new Date());
      purchaseOrder.setCreatedBy(loggedOnUser);
      purchaseOrder.setPoPlaceDate(new Date());
      purchaseOrder.setPurchaseOrderStatus(getBaseDao().get(PurchaseOrderStatus.class, EnumPurchaseOrderStatus.SentToSupplier.getId()));
      Warehouse warehouse = warehouseSetEntry.getKey();
      Supplier supplier = getBrightSupplierForAquaWH(warehouse);
      purchaseOrder.setSupplier(supplier);
      purchaseOrder.setWarehouse(warehouse);
      purchaseOrder = (PurchaseOrder) getPurchaseOrderDao().save(purchaseOrder);

      for (ProductVariant productVariant : warehouseSetEntry.getValue()) {
        PoLineItem poLineItem = new PoLineItem();
        poLineItem.setPurchaseOrder(purchaseOrder);
        poLineItem.setQty(productVariant.getQty());
        Sku sku = getSkuService().getSKU(productVariant, purchaseOrder.getWarehouse());
        poLineItem.setSku(sku);
        poLineItem.setCostPrice(productVariant.getCostPrice());
        poLineItem.setMrp(productVariant.getMarkedPrice());
        getPoLineItemDao().save(poLineItem);
      }

      poList.add(purchaseOrder);
    }

    return poList;
  }

  public Supplier getBrightSupplierForAquaWH(Warehouse warehouse) {
    if (warehouse.getId().equals(WarehouseService.GGN_AQUA_WH_ID)) {
      return getSupplierDao().findByTIN("06101832036");
    } else if (warehouse.getId().equals(WarehouseService.MUM_AQUA_WH_ID)) {
      return getSupplierDao().findByTIN("27210893736V");
    } else if (warehouse.getId().equals(WarehouseService.DEL_KAPASHERA_AQUA_WH_ID)) {
      return getSupplierDao().findByTIN("07320452122");
    }
    return null;
  }

  public PurchaseOrderDao getPurchaseOrderDao() {
    return purchaseOrderDao;
  }

  public void setPurchaseOrderDao(PurchaseOrderDao purchaseOrderDao) {
    this.purchaseOrderDao = purchaseOrderDao;
  }

  public PoLineItemService getPoLineItemService() {
    return poLineItemService;
  }

  public void setPoLineItemService(PoLineItemService poLineItemService) {
    this.poLineItemService = poLineItemService;
  }

  public UserService getUserService() {
    return userService;
  }

  public SupplierDao getSupplierDao() {
    return supplierDao;
  }

  public SkuService getSkuService() {
    return skuService;
  }

  public PoLineItemDao getPoLineItemDao() {
    return poLineItemDao;
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }
}
