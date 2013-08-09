package com.hk.pact.service.inventory;

import java.util.List;

import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.inventory.GoodsReceivedNote;

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

  // todo --deleted
    @Deprecated
    public Long getAvailableUnbookedInventoryForPrescriptionEyeglasses(List<Sku> skuList);
    // todo --deleted
    @Deprecated
    public Long getAvailableUnbookedInventory(List<Sku> skuList);

    public Long getAvailableUnbookedInventory(ProductVariant productVariant);

    //public List<Warehouse> getWarehousesForSkuAndQty(List<Sku> skuList, Long qty);

    public Supplier getSupplierForSKU(Sku sku);
    // todo deleted
    public Long getBookedQtyOfSkuInQueue(List<Sku> sku);
     // todo deleted
    @Deprecated
    public Long getUnbookedInventoryInProcessingQueue(List<Sku> skuList);

   // todo -- need to be replace
    long getUnbookedInventoryInProcessingQueue(LineItem lineItem);
    // todo -- need to be replace
    public long getUnbookedInventoryForActionQueue(LineItem lineItem);
     // todo deleted
    public Long getBookedQtyOfSkuInProcessingQueue(List<Sku> skuList);
       // todo deleted
    public Long getBookedQtyOfProductVariantInQueue(ProductVariant productVariant);

    public boolean allInventoryCheckedIn(GoodsReceivedNote grn);

}