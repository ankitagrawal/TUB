package com.hk.admin.pact.service.shippingOrder;

import java.util.HashMap;
import java.util.List;

import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.Warehouse;

public interface JitShippingOrderPOCreationService {
	
	public List<LineItem> getJitLineItems(List<ShippingOrder> shippingOrders);
	
	public HashMap<Supplier, List<LineItem>> getSupplierLineItemMap(List<LineItem> lineItems);
	
	public HashMap<PurchaseOrder, HashMap<ProductVariant, Long>> createPurchaseOrderVariantQuantityMap(
			HashMap<PurchaseOrder, List<LineItem>> supplierLineItemHashMap);
	
	public HashMap<Supplier, HashMap<Warehouse, List<LineItem>>> createSupplierWhLineitemsMap(HashMap<Supplier, List<LineItem>> supplierVariantQuantityMap);
	
	public HashMap<PurchaseOrder, List<LineItem>> createPurchaseOrder(HashMap<Supplier, HashMap<Warehouse, List<LineItem>>> supplierWhLineitemsMap);
	
	public void createPoLineItems(HashMap<PurchaseOrder, HashMap<ProductVariant, Long>> purchaseOrderProductVariantMap);
	
	public List<PurchaseOrder> deletePOsWithEmptyPOLineItems(List<PurchaseOrder> purchaseOrders);
	
	public void approveAllPos(PurchaseOrder purchaseOrder, List<PurchaseOrderStatus> purchaseOrderStatus);
	
	public void deleteExtraEntryFromSOPO(List<PurchaseOrder> purchaseOrders);

}
