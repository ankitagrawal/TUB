package com.hk.pact.service.inventory;

import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.order.CartLineItem;

import java.util.List;
import java.util.Set;

public interface InventoryService {

  /**
   * Check If Inventory is going in RED zone. Check for Low Inventory and Delete from the list if it's healthy Mark
   * variants in stock and out of stock
   */
  public void checkInventoryHealth(ProductVariant productVariant);

  public Long getAggregateCutoffInventory(ProductVariant productVariant);

  public InvTxnType getInventoryTxnType(EnumInvTxnType enumInvTxnType);

  /**
   * @param sku
   * @param mrp - Nullable
   * @return Unbooked Inventory - this is qty of 'CHECKED_IN' units for a SKU at a particular MRP
   * In case mrp is NULL - it will return all the 'CHECKED_IN' units for the SKU
   */
  public Long getAvailableUnbookedInventory(Sku sku, Double mrp);

  public Long getAllowedStepUpInventory(ProductVariant productVariant);

  public Supplier getSupplierForSKU(Sku sku);

  public boolean allInventoryCheckedIn(GoodsReceivedNote grn);

  //Migrated from Inventory Manage Service
  public List<SkuItemCLI> saveSkuItemCLI(Set<SkuItem> skuItemsToBeBooked, CartLineItem cartLineItem);

    public Long getAvailableUnBookedInventory(ProductVariant productVariant);

    public Long getAvailableUnbookedInventory(List<Sku> skuList, boolean addBrightInventory);

    public Long getLatestcheckedInBatchInventoryCount(ProductVariant productVariant);

    public List<CartLineItem> getClisForOrderInProcessingState(ProductVariant productVariant, Long skuId, Double mrp);

    public boolean sicliAlreadyExists(CartLineItem cartLineItem);

}