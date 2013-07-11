package com.hk.admin.impl.service.shippingOrder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.shippingOrder.JitShippingOrderPOCreationService;
import com.hk.admin.util.TaxUtil;
import com.hk.constants.core.EnumTax;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.constants.inventory.EnumPurchaseOrderType;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.core.Tax;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.TaxComponent;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;


@Service
public class JitShippingOrderPOCreationServiceImpl implements JitShippingOrderPOCreationService{
	
	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	PurchaseOrderDao purchaseOrderDao;
	@Autowired
	BaseDao baseDao;
	@Autowired
	SkuService skuService;
	@Autowired
	AdminEmailManager adminEmailManager;
	@Autowired
	ProductVariantService productVariantService;
	@Autowired
	ShippingOrderLifecycleDao shippingOrderLifecycleDao;
	@Autowired
	private UserService userService;
	@Autowired
	PurchaseOrderService purchaseOrderService;
	@Autowired
	AdminInventoryService adminInventoryService;
	
	private static Logger logger = LoggerFactory.getLogger(JitShippingOrderPOCreationServiceImpl.class);

	public List<LineItem> getJitLineItems(List<ShippingOrder> shippingOrders) {
		logger.debug("Inside method 1 - getJitLineItems, no of shipping orders received - " + shippingOrders.size());
		List<LineItem> lineItemIsJitList = new ArrayList<LineItem>();
		if (shippingOrders != null && shippingOrders.size() > 0) {
			for (ShippingOrder order : shippingOrders) {
				Set<LineItem> items = order.getLineItems();
				for (LineItem lineItem : items) {
					if (lineItem.getSku().getProductVariant().getProduct().getJit()) {
						lineItemIsJitList.add(lineItem);
					}
				}
			}
		}
		return lineItemIsJitList;
	}

	public HashMap<Supplier, List<LineItem>> getSupplierLineItemMap(List<LineItem> lineItems) {
		HashMap<Supplier, List<LineItem>> supplierItemMap = new HashMap<Supplier, List<LineItem>>();
		Set<Supplier> suppliersSet = new HashSet<Supplier>();
		if (lineItems != null && lineItems.size() > 0) {
			for (LineItem lineItem : lineItems) {
				Supplier supplier = lineItem.getSku().getProductVariant().getProduct().getSupplier();
				suppliersSet.add(supplier);
			}
		}

		if (lineItems != null && lineItems.size() > 0) {
			for (Supplier supplier : suppliersSet) {
				List<LineItem> lineItemlist = new ArrayList<LineItem>();
				for (LineItem lineItem : lineItems) {
					if (lineItem.getSku().getProductVariant().getProduct().getSupplier().equals(supplier)) {
						lineItemlist.add(lineItem);
					}
				}
				supplierItemMap.put(supplier, lineItemlist);
			}
		}
		logger.debug("Inside method 3 - getSupplierLineItemMap, no of lineItems received - " + lineItems.size());
		return supplierItemMap;
	}

	public HashMap<PurchaseOrder, HashMap<ProductVariant, Long>> createPurchaseOrderVariantQuantityMap(
			HashMap<PurchaseOrder, List<LineItem>> supplierLineItemHashMap) {
		HashMap<PurchaseOrder, HashMap<ProductVariant, Long>> purchaseOrderVariantQuantityMap = new HashMap<PurchaseOrder, HashMap<ProductVariant, Long>>();
		int count = 0;
		Set<Entry<PurchaseOrder, List<LineItem>>> entrySet = supplierLineItemHashMap.entrySet();
		for (Entry<PurchaseOrder, List<LineItem>> entry : entrySet) {
			PurchaseOrder purchaseOrder = entry.getKey();
			List<LineItem> lineItems = entry.getValue();
			count += lineItems.size();
			HashMap<ProductVariant, Long> productQtyHashMap = new HashMap<ProductVariant, Long>();
			if (lineItems != null && lineItems.size() > 0) {
				for (LineItem item : lineItems) {
					ProductVariant productVariant = item.getSku().getProductVariant();
					Long quantity = item.getQty();
					if (productQtyHashMap.containsKey(productVariant)) {
						Long qty = productQtyHashMap.get(productVariant);
						qty += quantity;
						productQtyHashMap.put(productVariant, qty);
					} else {
						productQtyHashMap.put(productVariant, quantity);
					}
				}
			}
			purchaseOrderVariantQuantityMap.put(purchaseOrder, productQtyHashMap);
		}
		logger.debug("Inside method 5 - createPurchaseOrderVariantQuantityMap, no of lineItems received - " + count);
		return purchaseOrderVariantQuantityMap;
	}

	public HashMap<Supplier, HashMap<Warehouse, List<LineItem>>> createSupplierWhLineitemsMap(HashMap<Supplier, List<LineItem>> supplierVariantQuantityMap) {
		int count = 0;
		HashMap<Supplier, HashMap<Warehouse, List<LineItem>>> supplierWhLineitemsMap = new HashMap<Supplier, HashMap<Warehouse, List<LineItem>>>();
		if (supplierVariantQuantityMap != null && !supplierVariantQuantityMap.isEmpty()) {
			Set<Entry<Supplier, List<LineItem>>> entrySet = supplierVariantQuantityMap.entrySet();
			for (Entry<Supplier, List<LineItem>> entry : entrySet) {
				Supplier supplier = entry.getKey();
				List<LineItem> lineItems = entry.getValue();
				count += lineItems.size();
				HashMap<Warehouse, List<LineItem>> whLiMap = new HashMap<Warehouse, List<LineItem>>();
				if (lineItems != null && lineItems.size() > 0) {
					for (LineItem item : lineItems) {
						Warehouse wh = item.getSku().getWarehouse();
						if (whLiMap.containsKey(wh)) {
							List<LineItem> items = new ArrayList<LineItem>(whLiMap.get(wh));
							items.add(item);
							whLiMap.put(wh, items);
						} else {
							List<LineItem> items = new ArrayList<LineItem>();
							items.add(item);
							whLiMap.put(wh, items);
						}
					}
					supplierWhLineitemsMap.put(supplier, whLiMap);
				}
			}
		}
		logger.debug("Inside method 4 - createSupplierWhLineitemsMap, no of lineItems received - " + count);
		return supplierWhLineitemsMap;
	}

	public HashMap<PurchaseOrder, List<LineItem>> createPurchaseOrder(HashMap<Supplier, HashMap<Warehouse, List<LineItem>>> supplierWhLineitemsMap) {

		int count = 0;
		HashMap<PurchaseOrder, List<LineItem>> purchaseOrderLineItemMap = new HashMap<PurchaseOrder, List<LineItem>>();
		if (supplierWhLineitemsMap != null && !supplierWhLineitemsMap.isEmpty()) {
			Set<Entry<Supplier, HashMap<Warehouse, List<LineItem>>>> entrySet = supplierWhLineitemsMap.entrySet();

			for (Entry<Supplier, HashMap<Warehouse, List<LineItem>>> entry : entrySet) {
				Supplier supplier = entry.getKey();
				HashMap<Warehouse, List<LineItem>> whLineItemsMap = entry.getValue();
				if (whLineItemsMap != null && !whLineItemsMap.isEmpty()) {
					Set<Entry<Warehouse, List<LineItem>>> whLiEntry = whLineItemsMap.entrySet();
					for (Entry<Warehouse, List<LineItem>> otherEntry : whLiEntry) {
						List<LineItem> lineItemsFromwhLineItemsMap = otherEntry.getValue();
						count += lineItemsFromwhLineItemsMap.size();
						Warehouse warehouse = otherEntry.getKey();
						if (lineItemsFromwhLineItemsMap != null && lineItemsFromwhLineItemsMap.size() > 0) {
							PurchaseOrder purchaseOrder = new PurchaseOrder();
							purchaseOrder.setCreateDate(new Date());
							purchaseOrder.setCreatedBy(userService.getAdminUser());
							purchaseOrder.setSupplier(supplier);
							purchaseOrder.setPurchaseOrderType(EnumPurchaseOrderType.JIT.asEnumPurchaseOrderType());
							purchaseOrder.setPurchaseOrderStatus(getBaseDao().get(PurchaseOrderStatus.class, EnumPurchaseOrderStatus.Generated.getId()));
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(new Date());
							calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getLeadTime());
							purchaseOrder.setEstDelDate(calendar.getTime());
							if (purchaseOrder.getSupplier().getCreditDays() != null && purchaseOrder.getSupplier().getCreditDays() >= 0) {
								calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getCreditDays());
								purchaseOrder.setEstPaymentDate(calendar.getTime());
							} else {
								purchaseOrder.setEstPaymentDate(new Date());
							}
							Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();
							for (LineItem lineItem : lineItemsFromwhLineItemsMap) {
								ShippingOrder so = lineItem.getShippingOrder();
								shippingOrders.add(so);
							}
							List<ShippingOrder> soList = new ArrayList<ShippingOrder>(shippingOrders);
							purchaseOrder.setShippingOrders(soList);
							purchaseOrder.setWarehouse(warehouse);
							purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);
							purchaseOrderLineItemMap.put(purchaseOrder, lineItemsFromwhLineItemsMap);
							// purchaseOrders.add(purchaseOrder);
						}
					}
				}
			}
		}
		logger.debug("Inside method 6 - createPurchaseOrder, no of lineItems received - " + count);
		return purchaseOrderLineItemMap;
	}

	public void createPoLineItems(HashMap<PurchaseOrder, HashMap<ProductVariant, Long>> purchaseOrderProductVariantMap) {

		if (purchaseOrderProductVariantMap != null && purchaseOrderProductVariantMap.size() > 0) {
			boolean containsDropShip = false;
			Set<Entry<PurchaseOrder, HashMap<ProductVariant, Long>>> entrySet = purchaseOrderProductVariantMap.entrySet();

			for (Entry<PurchaseOrder, HashMap<ProductVariant, Long>> purchaseOrderPVEntry : entrySet) {

				PurchaseOrder purchaseOrder = purchaseOrderPVEntry.getKey();
				List<PoLineItem> poLineItems = new ArrayList<PoLineItem>();
				Double totalTaxable = 0.0D, totalTax = 0.0D, totalSurcharge = 0.0D, totalPayable = 0.0D;
				Set<Entry<ProductVariant, Long>> prodQty = purchaseOrderPVEntry.getValue().entrySet();
				for (Entry<ProductVariant, Long> entry : prodQty) {
					ProductVariant productVariant = entry.getKey();
					if(productVariant.getProduct().isDropShipping()){
						containsDropShip = true;
					}
					Long quantity = entry.getValue();
					Sku sku = skuService.getSKU(productVariant, purchaseOrder.getWarehouse());
					Long inventory = adminInventoryService.getNetInventory(sku);
					Long bookedInventory = adminInventoryService.getBookedInventory(sku);
					if (bookedInventory == null) {
						bookedInventory = 0L;
					}
					if (inventory == null) {
						inventory = 0L;
					}
					Long unbookedInventory = inventory - bookedInventory;
					logger.debug("Inventory check for Variant - " + productVariant.getId() + "qty - " + inventory + "asked Qty - " + quantity);
					if (unbookedInventory<0) {
						Long poQty = 0L;
						// TODO --
						Double taxableAmount = 0.0D;
						Double discountPercentage = 0D;
						PoLineItem poLineItem = new PoLineItem();
						poLineItem.setSku(sku);
						if(Math.abs(unbookedInventory)<quantity){
							poQty = Math.abs(unbookedInventory);
						}
						else{
							poQty = quantity;
						}
						poLineItem.setQty(poQty);
						poLineItem.setCostPrice(productVariant.getCostPrice());
						poLineItem.setMrp(productVariant.getMarkedPrice());
						poLineItem.setPurchaseOrder(purchaseOrder);

						Tax tax;
						if (sku != null) {
							if (purchaseOrder.getSupplier().getState().equalsIgnoreCase(purchaseOrder.getWarehouse().getState())) {
								tax = sku.getTax();
							} else {
								tax = new Tax();
								tax.setId(EnumTax.CST.getId());
								tax.setName(EnumTax.CST.getName());
								tax.setType(EnumTax.CST.getType());
								tax.setValue(EnumTax.CST.getValue());
							}
							taxableAmount = poQty * ((productVariant.getCostPrice() - productVariant.getCostPrice() * discountPercentage / 100));
							totalTaxable += taxableAmount;
							TaxComponent taxComponent = TaxUtil.getSupplierTaxForVariedTaxRatesWithoutSku(purchaseOrder.getSupplier(), purchaseOrder
									.getWarehouse().getState(), tax, taxableAmount);
							totalTax += taxComponent.getTax();
							totalSurcharge += taxComponent.getSurcharge();
							totalPayable += taxComponent.getPayable();
							poLineItem.setTaxableAmount(taxableAmount);
							poLineItem.setTaxAmount(taxComponent.getTax());
							poLineItem.setSurchargeAmount(taxComponent.getSurcharge());
							poLineItem.setPayableAmount(taxComponent.getPayable());
							poLineItem = (PoLineItem) getBaseDao().save(poLineItem);
							poLineItems.add(poLineItem);
						}
					}
				}
				purchaseOrder.setPayable(totalPayable);
				purchaseOrder.setTaxableAmount(totalTaxable);
				purchaseOrder.setTaxAmount(totalTax);
				purchaseOrder.setSurchargeAmount(totalSurcharge);
				purchaseOrder.setFinalPayableAmount(totalPayable);
				purchaseOrder.setPoLineItems(poLineItems);
				if(containsDropShip){
					purchaseOrder.setPurchaseOrderType(EnumPurchaseOrderType.DROP_SHIP.asEnumPurchaseOrderType());
				}
				purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);
			}
		}

	}

	public List<PurchaseOrder> deletePOsWithEmptyPOLineItems(List<PurchaseOrder> purchaseOrders) {
		List<PurchaseOrder> list = new ArrayList<PurchaseOrder>();
		if (purchaseOrders != null && purchaseOrders.size() > 0) {
			Iterator<PurchaseOrder> iterator = purchaseOrders.iterator();
			while (iterator.hasNext()) {
				PurchaseOrder po = iterator.next();
				if (po.getPoLineItems() != null && po.getPoLineItems().size() > 0) {
					list.add(po);
				} else {
					iterator.remove();
					getBaseDao().delete(po);
				}
			}
		}
		return list;
	}

	public void approveAllPos(PurchaseOrder purchaseOrder, List<PurchaseOrderStatus> purchaseOrderStatus) {

		for (PurchaseOrderStatus status : purchaseOrderStatus) {
			EnumPurchaseOrderStatus poStatus = EnumPurchaseOrderStatus.getById(status.getId());
			purchaseOrder.setUpdateDate(new Date());
			purchaseOrder.setApprovedBy(userService.getAdminUser());
			purchaseOrder.setPurchaseOrderStatus(poStatus.asEnumPurchaseOrderStatus());
			purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);
			if (poStatus.getId().equals(EnumPurchaseOrderStatus.Approved.getId())) {
				adminEmailManager.sendPOApprovedEmail(purchaseOrder);
				if (purchaseOrder.getPoLineItems() != null && purchaseOrder.getPoLineItems().size() > 0) {
					if (purchaseOrder.getPoLineItems().get(0).getExtraInventoryLineItem() == null) {
						if (purchaseOrder.getSupplier().getEmail_id() != null) {
							adminEmailManager.sendPOMailToSupplier(purchaseOrder, purchaseOrder.getSupplier().getEmail_id());
						}
					}
				}
				purchaseOrder.setPurchaseOrderStatus(EnumPurchaseOrderStatus.SentToSupplier.asEnumPurchaseOrderStatus());
				purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);

			}
		}
	}
	
	@Override
	public void deleteExtraEntryFromSOPO(List<PurchaseOrder> purchaseOrders) {
		
		if(purchaseOrders!=null && purchaseOrders.size()>0){
			for(PurchaseOrder po : purchaseOrders){
				List<ShippingOrder> removeSoList = new ArrayList<ShippingOrder>();
				boolean deleted = true;
				List<ProductVariant> pvList = purchaseOrderService.getAllProductVariantFromPO(po);
				List<ShippingOrder> shippingOrders = po.getShippingOrders();
				if(pvList!=null && pvList.size()>0 && shippingOrders!=null && shippingOrders.size()>0){
					for(ShippingOrder so : shippingOrders){
						for(ProductVariant pv : pvList){
						if(shippingOrderService.shippingOrderContainsProductVariant(so, pv)){
							deleted = false;
							break;
						}
						}
						if(deleted){
							removeSoList.add(so);
						}
					}
				}
				if(removeSoList!=null && removeSoList.size()>0){
				List<ShippingOrder> soFromPoList = po.getShippingOrders();
				soFromPoList.removeAll(removeSoList);
				po.setShippingOrders(soFromPoList);
	        	baseDao.save(po);
				}
			}
		}
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

}
