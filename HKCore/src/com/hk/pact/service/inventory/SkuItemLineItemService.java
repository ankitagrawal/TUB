package com.hk.pact.service.inventory;

import com.hk.domain.api.HKAPIForeignBookingResponseInfo;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.StockTransfer;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.domain.warehouse.Warehouse;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 7/24/13
 * Time: 12:14 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SkuItemLineItemService {

  public List<SkuItemLineItem> getSkuItemLineItem(LineItem lineItem, Long skuItemStatusId);

  public SkuItemLineItem getById(Long skuItemLineItemId);

  public Boolean createNewSkuItemLineItem(LineItem lineItem);

  public boolean isWarehouseBeFlippable(ShippingOrder shippingOrder, Warehouse targetWarehouse);

  public SkuItemLineItem save(SkuItemLineItem skuItemLineItem);

  public List<SkuItemLineItem> getSkuItemLineItemForLineItem(LineItem lineItem);

  public Boolean freeInventoryForSOCancellation(ShippingOrder shippingORder);

    public Boolean freeInventoryForRTOCheckIn(ShippingOrder shippingORder);

    public SkuItemLineItem getBySkuItemId(Long skuItemLineItemId);
    
    public Boolean freeBookingTable(ShippingOrder shippingOrder);

  public boolean sicliAlreadyExists(CartLineItem cartLineItem);

  public ForeignSkuItemCLI getForeignSkuItemCLI(Long id);


  
  //==========Methods For Foreign Table=============
  
  public SkuItem getSkuItem(Long fsicliId);

  public List<ForeignSkuItemCLI> updateSkuItemForABJit(List<HKAPIForeignBookingResponseInfo> infos);

  public void populateSILIForABJit(List<ForeignSkuItemCLI> foreignSkuItemCLIs, LineItem lineItem);

  public List<ForeignSkuItemCLI> getForeignSkuItemCli(CartLineItem cartLineItem);

  public List<SkuItem> updateForeignSICLIForCancelledOrder (List <HKAPIForeignBookingResponseInfo> infos );

   public ForeignSkuItemCLI getFSICI(Long foreignSkuItemId);

  public List<HKAPIForeignBookingResponseInfo> freeBrightInventoryAgainstBOCancellation(CartLineItem cartLineItem);

  public List<HKAPIForeignBookingResponseInfo> freeBrightInventoryAgainstSoValidation(CartLineItem cartLineItem);

  public boolean deleteSicliAndSili (CartLineItem cartLineItem);

  public Boolean freeBookingItem(Long cartLineItemId);

  public boolean freeBookingInventoryAtAqua(CartLineItem cartLineItem);

  public boolean freeBookingInventoryAtBright(CartLineItem cartLineItem);

  public Boolean validateBooking(CartLineItem cartLineItem);

  public List<LineItem> freeBooking(ShippingOrder shippingOrder);

  public void removeRefusedFsicli (List <HKAPIForeignBookingResponseInfo> infos );

  public Boolean createNewSkuItemLineItemForFlipping(CartLineItem cartLineItem);

  public List<SkuItem> freeFsiclis(List<HKAPIForeignBookingResponseInfo> infos , Boolean isSkuItemsToBeDeleted);
}
