package com.hk.admin.pact.service.inventory;

import java.util.Date;
import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;

public interface AdminInventoryService {
    
    public List<SkuGroup> getInStockSkuGroups(String upc) ;
    
    public Long countOfCheckedInUnitsForGrnLineItem(GrnLineItem grnLineItem);
    
    public Long countOfCheckedOutUnitsOfLineItem(LineItem lineItem);
    
    public Boolean areAllUnitsOfOrderCheckedOut(ShippingOrder shippingOrder);
    
    /**
     * SkuGroup/SkuItem/PVI Inventory Checkin / Checkout Functions
     */
    public SkuGroup createSkuGroup(String batch, Date mfgDate, Date expiryDate, GoodsReceivedNote goodsReceivedNote, ReconciliationVoucher reconciliationVoucher, Sku sku);

    public void createSkuItemsAndCheckinInventory(SkuGroup skuGroup, Long qty, LineItem lineItem, GrnLineItem grnLineItem, RvLineItem rvLineItem, InvTxnType invTxnType, User txnBy);

    public void inventoryCheckinCheckout(Sku sku, SkuItem skuItem, LineItem lineItem, ShippingOrder shippingOrder, GrnLineItem grnLineItem, RvLineItem rvLineItem,
            InvTxnType invTxnType, Long qty, User txnBy);
    
    
    /**
     * @param sku
     * @return Inventory count of all action awaiting and in process orders
     */
    public Long getBookedInventory(Sku sku);
    
    public Long getBookedInventory(ProductVariant productVariant);

    public Long getNetInventory(Sku sku);
    
    public Long getNetInventory(ProductVariant productVariant) ;
    
    public void adjustInventory(SkuGroup skuGroup, Long qty);
    
    public void damageInventoryCheckin(SkuItem skuItem, LineItem lineItem);
    
    public void reCheckInInventory(ShippingOrder shippingOrder);
    
}
