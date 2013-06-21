package com.hk.admin.pact.service.inventory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hk.admin.dto.inventory.CreateInventoryFileDto;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.VariantConfig;
import com.hk.domain.core.InvTxnType;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.StockTransfer;
import com.hk.domain.inventory.StockTransferLineItem;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;

public interface AdminInventoryService {

    public List<SkuGroup> getInStockSkuGroups(String upc);

    public Long countOfCheckedInUnitsForGrnLineItem(GrnLineItem grnLineItem);

    public Long countOfCheckedOutUnitsOfLineItem(LineItem lineItem);

    public Boolean areAllUnitsOfOrderCheckedOut(ShippingOrder shippingOrder);

    /**
     * SkuGroup/SkuItem/PVI Inventory Checkin / Checkout Functions
     */
    public SkuGroup createSkuGroup(String batch, Date mfgDate, Date expiryDate, Double costPrice, Double mrp, GoodsReceivedNote goodsReceivedNote, ReconciliationVoucher reconciliationVoucher, StockTransfer stockTransfer, Sku sku);

    public SkuGroup createSkuGroupWithoutBarcode(String batch, Date mfgDate, Date expiryDate, Double costPrice, Double mrp, GoodsReceivedNote goodsReceivedNote, ReconciliationVoucher reconciliationVoucher, StockTransfer stockTransfer, Sku sku) ;

    public void createSkuItemsAndCheckinInventory(SkuGroup skuGroup, Long qty, LineItem lineItem, GrnLineItem grnLineItem, RvLineItem rvLineItem,
            StockTransferLineItem stockTransferLineItem, InvTxnType invTxnType, User txnBy);

    public void inventoryCheckinCheckout(Sku sku, SkuItem skuItem, LineItem lineItem, ShippingOrder shippingOrder, GrnLineItem grnLineItem, RvLineItem rvLineItem,
            StockTransferLineItem stockTransferLineItem, InvTxnType invTxnType, Long qty, User txnBy);

    /**
     * @param sku
     * @return Inventory count of all action awaiting and in process orders
     */
    public Long getBookedInventory(Sku sku);

    public Long getBookedInventory(ProductVariant productVariant);

    public Long getNetInventory(Sku sku);

    public Long getNetInventory(ProductVariant productVariant);

	public Long getNetInventoryAtServiceableWarehouses(ProductVariant productVariant);

    public void adjustInventory(SkuGroup skuGroup, Long qty);

    public void damageInventoryCheckin(SkuItem skuItem, LineItem lineItem);

    public void reCheckInInventory(ShippingOrder shippingOrder);

    public Long countOfCheckedInUnitsForStockTransferLineItem(StockTransferLineItem stockTransferLineItem);

    public List<VariantConfig> getAllVariantConfig();

	public List<SkuItem> getInStockSkuItems(List<SkuGroup> skuGroupList);

	public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup);

	public List<SkuItem> getInStockSkuItems(String barcode, Warehouse warehouse);

	public void inventoryCheckoutForStockTransfer(Sku sku, SkuItem skuItem, StockTransferLineItem stockTransferLineItem, Long qty, User txnBy );

//    public  List<SkuItem> getCheckedInOrOutSkuItems(RvLineItem rvLineItem, StockTransferLineItem stockTransferLineItem, GrnLineItem grnLineItem , Long transferQty) ;

    public  List<SkuItem> getCheckedInOrOutSkuItems(RvLineItem rvLineItem, StockTransferLineItem stockTransferLineItem, GrnLineItem grnLineItem ,LineItem lineItem, Long transferQty) ;
    
   public Map<Long, String> skuItemBarcodeMap(List<SkuItem> checkedInSkuItems);

    public List<CreateInventoryFileDto> getCheckedInSkuGroup(String brand, Warehouse warehouse, Product product, ProductVariant productVariant);

    public void deleteInventory(GrnLineItem grnLineItem);

    public List<SkuGroup> getInStockSkuGroupsForReview(LineItem lineItem);

    public List<SkuGroup> getSkuGroupsInReviewState();
}
