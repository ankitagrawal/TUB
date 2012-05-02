package com.hk.service;

import java.util.List;

import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.sku.Sku;

public interface InventoryService {

    /**
     * Check If Inventory is going in RED zone. Check for Low Inventory and Delete from the list if it's healthy Mark
     * variants in stock and out of stock
     */
    public void checkInventoryHealth(ProductVariant productVariant);
    
    public Long getAggregateCutoffInventory(ProductVariant productVariant);
    
    public Long getAggregateCutoffInventory(List<Sku> skuList) ;
    
    public InvTxnType getInventoryTxnType(EnumInvTxnType enumInvTxnType);

    /**
     * @param sku
     * @return Unbooked Inventory - this is difference of net physical inventory and all booked inventory The return
     *         value can be negative in case we are doing overbooking and product is not timely marked as Out Of Stock
     */
    public Long getAvailableUnbookedInventory(Sku sku);

    public Long getAvailableUnbookedInventory(List<Sku> skuList);

    //public List<Warehouse> getWarehousesForSkuAndQty(List<Sku> skuList, Long qty);

    public Supplier getSupplierForSKU(Sku sku);

    public Long getBookedQtyOfSkuInQueue(List<Sku> sku);

    public Long getBookedQtyOfProductVariantInQueue(ProductVariant productVariant);

}