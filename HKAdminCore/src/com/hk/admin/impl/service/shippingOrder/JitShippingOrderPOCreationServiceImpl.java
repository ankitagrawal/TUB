package com.hk.admin.impl.service.shippingOrder;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.shippingOrder.JitShippingOrderPOCreationService;
import com.hk.admin.util.TaxUtil;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.core.EnumTax;
import com.hk.constants.core.Keys;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.constants.inventory.EnumPurchaseOrderType;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.core.Tax;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.TaxComponent;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;

@Service
public class JitShippingOrderPOCreationServiceImpl implements JitShippingOrderPOCreationService {

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
	@Autowired
	WarehouseService warehouseService;
	@Autowired
	SupplierDao supplierDao;
	private Warehouse warehouse;
	
	
	private List<PurchaseOrder> purchaseOrders;
	private static Logger logger = LoggerFactory.getLogger(JitShippingOrderPOCreationServiceImpl.class);
	private Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();
	List<PurchaseOrderStatus> purchaseOrderStatus = EnumPurchaseOrderStatus.getAllPurchaseOrderStatusForSystemGeneratedPOs();
	@Value("#{hkEnvProps['" + Keys.Env.aquaBrightSeparatedFor + "']}")
    private String aquaBrightSeparatedFor;
	
	List<String> warehouseIdList = new ArrayList<String>();
	
	public List<ShippingOrder> getShippingOrderListToProcess(boolean filterJit) {
		List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria(filterJit);
		shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);
		List<ShippingOrder> shippingOrderListToProcess = new ArrayList<ShippingOrder>();
		if (shippingOrderList != null && shippingOrderList.size() > 0) {
			for (ShippingOrder shippingOrder : shippingOrderList) {
				if (shippingOrder.getPurchaseOrders() == null || (shippingOrder.getPurchaseOrders() != null && shippingOrder.getPurchaseOrders().size() == 0)) {
					shippingOrderListToProcess.add(shippingOrder);
				}
			}
		}
		return shippingOrderListToProcess;
	}

	public ShippingOrderSearchCriteria getShippingOrderSearchCriteria(boolean filterJit) {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
		if(filterJit){
		shippingOrderSearchCriteria.setContainsJitProducts(true);
		}
		List<ShippingOrderStatus> soStatusList = new ArrayList<ShippingOrderStatus>();
		soStatusList.add(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus());
		shippingOrderSearchCriteria.setShippingOrderStatusList(soStatusList);
		List<PaymentStatus> paymentStatusList = new ArrayList<PaymentStatus>();
		paymentStatusList.add(EnumPaymentStatus.SUCCESS.asPaymenStatus());
		paymentStatusList.add(EnumPaymentStatus.ON_DELIVERY.asPaymenStatus());
		shippingOrderSearchCriteria.setPaymentStatuses(paymentStatusList);
		return shippingOrderSearchCriteria;
	}
	
	public List<PurchaseOrder> processShippingOrderForPOCreation(List<LineItem> lineItemsList, Set<ShippingOrder> shippingOrders){
		//List<LineItem> jitLineItems = getJitLineItems(shippingOrderToProcess);
		purchaseOrders = new ArrayList<PurchaseOrder>();
		this.shippingOrders = shippingOrders;
		HashMap<Supplier, List<LineItem>> supplierLineItemListMap = getSupplierLineItemMap(lineItemsList);
		HashMap<Supplier, HashMap<Warehouse, List<LineItem>>> supplierWhLineItemsMap = createSupplierWhLineitemsMap(supplierLineItemListMap);
		HashMap<PurchaseOrder, List<LineItem>> purchaseOrderLineItemMap= createPurchaseOrder(supplierWhLineItemsMap);
		HashMap<PurchaseOrder, Set<ProductVariantMrpQtyLineItems>> poProductVariantMrpQtyLineItemsSet=createPurchaseOrderVariantMrpQuantityMap(purchaseOrderLineItemMap);
		Set<Entry<PurchaseOrder, Set<ProductVariantMrpQtyLineItems>>> entrySet = poProductVariantMrpQtyLineItemsSet.entrySet();
		for(Entry<PurchaseOrder, Set<ProductVariantMrpQtyLineItems>> entry : entrySet){
			PurchaseOrder po = entry.getKey();
			if(!purchaseOrders.contains(po)){
				purchaseOrders.add(po);
			}
			if(entry.getValue()!=null && entry.getValue().size()>0){
			createPOLineItemsForPO(po, entry.getValue(), shippingOrders);
			}
		}
		deletePOsWithEmptyPOLineItems(purchaseOrders);
		for (PurchaseOrder purchaseOrder : purchaseOrders) {
			if (purchaseOrder != null) {
				approveAllPos(purchaseOrder, purchaseOrderStatus);
				for (ShippingOrder so : purchaseOrder.getShippingOrders()) {
					ShippingOrderLifecycle shippingOrderLifecycle = new ShippingOrderLifecycle();
					shippingOrderLifecycle.setOrder(so);
					shippingOrderLifecycle.setShippingOrderLifeCycleActivity(getBaseDao().get(ShippingOrderLifeCycleActivity.class,
							EnumShippingOrderLifecycleActivity.SO_LoggedComment.getId()));
					shippingOrderLifecycle.setUser(userService.getAdminUser());
					shippingOrderLifecycle.setComments("PO# " + purchaseOrder.getId() + " Approved for the Shipping Order");
					shippingOrderLifecycle.setActivityDate(new Date());
					shippingOrderLifecycleDao.save(shippingOrderLifecycle);
				}
			}
		}
		return purchaseOrders;
	}

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
	
	public List<LineItem> getValidLineItems(List<ShippingOrder> shippingOrders) {
		List<LineItem> lineItemList = new ArrayList<LineItem>();
		if (shippingOrders != null && shippingOrders.size() > 0) {
			for (ShippingOrder order : shippingOrders) {
				Set<LineItem> items = order.getLineItems();
				for (LineItem lineItem : items) {
					ProductVariant productVariant = lineItem.getSku().getProductVariant();
					Sku sku = skuService.getSKU(productVariant,lineItem.getShippingOrder().getWarehouse());
					Long inventory = adminInventoryService.getNetInventory(sku);
					Long bookedInventory = adminInventoryService.getBookedInventory(sku);
					if (bookedInventory == null) {
						bookedInventory = 0L;
					}
					if (inventory == null) {
						inventory = 0L;
					}
					Long unbookedInventory = inventory - bookedInventory;
					if(unbookedInventory<0){
						lineItemList.add(lineItem);
					}
				}
			}
		}
		return lineItemList;
	}
	
	public Set<ShippingOrder> getValidShippingOrders(List<LineItem> validLineItemList){
		Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();
		if(validLineItemList!=null && validLineItemList.size()>0){
			for(LineItem li : validLineItemList){
				ShippingOrder so = li.getShippingOrder();
				shippingOrders.add(so);
			}
		}
		return shippingOrders;
	}

	public HashMap<Supplier, List<LineItem>> getSupplierLineItemMap(List<LineItem> lineItems) {
		HashMap<Supplier, List<LineItem>> supplierItemMap = new HashMap<Supplier, List<LineItem>>();
		if(aquaBrightSeparatedFor!=null){
		warehouseIdList.addAll(Arrays.asList(aquaBrightSeparatedFor.split(",")));
		}
		logger.debug("Aqua Bright Separated For - "+aquaBrightSeparatedFor+" "+warehouseIdList.size());
		if (lineItems != null && lineItems.size() > 0) {
			for (LineItem lineItem : lineItems) {
				Warehouse wh = lineItem.getSku().getWarehouse();
				logger.debug("Warehouse : " + wh.getId());// WH of SO
				Supplier supplier; 
				if(warehouseIdList!=null && warehouseIdList.size()>0 && warehouseIdList.contains(wh.getId().toString())){
					String whTin = warehouseService.getWarehouseById(wh.getId()).getTin();
					String supplierTin = getWhSupplierTinMap(whTin);
					supplier = supplierDao.findByTIN(supplierTin);
					logger.debug("Warehouse : "+wh.getIdentifier()+" Supplier : "+supplier.getName());
				}
				else{
					supplier = lineItem.getSku().getProductVariant().getProduct().getSupplier();
				}
				if(supplierItemMap.containsKey(supplier)){
					List<LineItem> items = supplierItemMap.get(supplier);
					items.add(lineItem);
					supplierItemMap.put(supplier, items);
				}
				else{
					List<LineItem> newLineItems = new ArrayList<LineItem>();
					newLineItems.add(lineItem);
					supplierItemMap.put(supplier, newLineItems);
				}
			}
		}
		logger.debug("Inside method 3 - getSupplierLineItemMap, no of lineItems received - " + lineItems.size());
		return supplierItemMap;
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

	//Method2. createPurchaseOrder
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
							PurchaseOrder purchaseOrder = createPO(supplier, warehouse);
							purchaseOrderLineItemMap.put(purchaseOrder, lineItemsFromwhLineItemsMap);
						}
					}
				}
			}
		}
		logger.debug("Inside method 6 - createPurchaseOrder, no of lineItems received - " + count);
		return purchaseOrderLineItemMap;
	}

	
	// Copy of Method1. createPurchaseOrderVariantQuantityMap
		public HashMap<PurchaseOrder, Set<ProductVariantMrpQtyLineItems>> createPurchaseOrderVariantMrpQuantityMap(
				HashMap<PurchaseOrder, List<LineItem>> supplierLineItemHashMap) {
			HashMap<PurchaseOrder, Set<ProductVariantMrpQtyLineItems>> purchaseOrderVariantQuantityMap = new HashMap<PurchaseOrder, Set<ProductVariantMrpQtyLineItems>>();
			int count = 0;
			Set<Entry<PurchaseOrder, List<LineItem>>> entrySet = supplierLineItemHashMap.entrySet();
			for (Entry<PurchaseOrder, List<LineItem>> entry : entrySet) {
				PurchaseOrder purchaseOrder = entry.getKey();
				List<LineItem> lineItems = entry.getValue();
				count += lineItems.size();
				Set<ProductVariantMrpQtyLineItems> pvMrpQtyLiSetForThisPO = new HashSet<JitShippingOrderPOCreationServiceImpl.ProductVariantMrpQtyLineItems>();
				Set<ProductVariantMrpQtyLineItems> pvMrpQtyLiSetForOtherPO = new HashSet<JitShippingOrderPOCreationServiceImpl.ProductVariantMrpQtyLineItems>();
				if (lineItems != null && lineItems.size() > 0) {
					for (LineItem item : lineItems) {
						boolean canAddToThisSet = false;
						boolean canAddToOtherSet = false;
						boolean addingToOtherSet = false;
						ProductVariant productVariant = item.getSku().getProductVariant();
						Long quantity = item.getQty();
						Double mrp = item.getMarkedPrice();
						ProductVariantMrpQtyLineItems variantMrpQtyLineItems = new ProductVariantMrpQtyLineItems();
						variantMrpQtyLineItems.setProductVariant(productVariant);
						variantMrpQtyLineItems.setMrp(mrp);
						variantMrpQtyLineItems.setCostPrice(item.getCostPrice());
						variantMrpQtyLineItems.setQty(quantity);
						
						
						for (ProductVariantMrpQtyLineItems pvmq : pvMrpQtyLiSetForThisPO) {
							if (pvmq.getProductVariant().equals(productVariant)) {
								canAddToThisSet = true;
								if (pvmq.getMrp().equals(mrp)) {
									pvmq.setQty(pvmq.getQty() + quantity);
								}
								else
									canAddToOtherSet = true;
							}
						}
						if(!canAddToThisSet){
							pvMrpQtyLiSetForThisPO.add(variantMrpQtyLineItems);
						}
						if(canAddToOtherSet){
							for (ProductVariantMrpQtyLineItems pvmq : pvMrpQtyLiSetForOtherPO) {
								if (pvmq.getProductVariant().equals(productVariant) && pvmq.getMrp().equals(mrp)) {
									addingToOtherSet = true;
									pvmq.setQty(pvmq.getQty() + quantity);
								}
							}
							if(!addingToOtherSet){
								pvMrpQtyLiSetForOtherPO.add(variantMrpQtyLineItems);
							}
						}
					}
					if(pvMrpQtyLiSetForOtherPO!=null && pvMrpQtyLiSetForOtherPO.size()>0){
					createPurchaseOrdersForVariedMrp(purchaseOrder, pvMrpQtyLiSetForOtherPO);
					}
				}
				purchaseOrderVariantQuantityMap.put(purchaseOrder, pvMrpQtyLiSetForThisPO);
			}
			logger.debug("Inside method 5 - createPurchaseOrderVariantQuantityMap, no of lineItems received - " + count);
			return purchaseOrderVariantQuantityMap;
		}

		//create PO method intermediate for the MRP varied PVs
		public void createPurchaseOrdersForVariedMrp(PurchaseOrder purchaseOrder, Set<ProductVariantMrpQtyLineItems> productVariantMrpQtyLineItems) {
			Warehouse wh = purchaseOrder.getWarehouse();
			Supplier sup = purchaseOrder.getSupplier();
			if (productVariantMrpQtyLineItems != null && productVariantMrpQtyLineItems.size() > 0) {
				Set<ProductVariantMrpQtyLineItems> setToProcess = new HashSet<JitShippingOrderPOCreationServiceImpl.ProductVariantMrpQtyLineItems>();
				for (ProductVariantMrpQtyLineItems pvmq : productVariantMrpQtyLineItems) {
					boolean canAdd = true;
					if (setToProcess == null || !(setToProcess.size() > 0)) {
						setToProcess.add(pvmq);
					} else {
						for (ProductVariantMrpQtyLineItems pmvqToAdd : setToProcess) {
							if (pvmq.getProductVariant().equals(pmvqToAdd.getProductVariant())) {
								canAdd= false;
							}
						}
						
						if(canAdd){
							setToProcess.add(pvmq);
						}
					}
				}
				productVariantMrpQtyLineItems.removeAll(setToProcess);
				if (setToProcess != null && setToProcess.size() > 0) {
					PurchaseOrder po = createPO(sup, wh);
					createPOLineItemsForPO(po, setToProcess, shippingOrders);
					approveAllPos(po, purchaseOrderStatus);
				}
				if (productVariantMrpQtyLineItems != null && productVariantMrpQtyLineItems.size() > 0) {
					createPurchaseOrdersForVariedMrp(purchaseOrder, productVariantMrpQtyLineItems);
				}
			}
		}

		//Copy of Method2. - creatPO
		public PurchaseOrder createPO(Supplier supplier, Warehouse warehouse) {
			PurchaseOrder purchaseOrder = new PurchaseOrder();
			purchaseOrder.setCreateDate(new Date());
			purchaseOrder.setCreatedBy(userService.getAdminUser());
			purchaseOrder.setSupplier(supplier);
			purchaseOrder.setWarehouse(warehouse);
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
			purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);
			return purchaseOrder;
		}

		//Copy of Method3. - createPOLineItemsForPO
		public void createPOLineItemsForPO(PurchaseOrder purchaseOrder, Set<ProductVariantMrpQtyLineItems> items, Set<ShippingOrder> shippingOrders) {
			if (items != null && items.size() > 0) {
				Double totalTaxable = 0.0D, totalTax = 0.0D, totalSurcharge = 0.0D, totalPayable = 0.0D;
				boolean containsDropShip = false;
				boolean isRegular = true;
				Set<ShippingOrder> shippingOrdersInPO = new HashSet<ShippingOrder>();
				List<PoLineItem> poLineItems = new ArrayList<PoLineItem>();
				for (ProductVariantMrpQtyLineItems pvmq : items) {
					ProductVariant productVariant = pvmq.getProductVariant();
					Long quantity = pvmq.getQty();
					Double mrp = pvmq.getMrp();

					if (productVariant.getProduct().isDropShipping()) {
						containsDropShip = true;
					}
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
					if (unbookedInventory < 0) {
						for (ShippingOrder so : shippingOrders) {
							boolean sohaspv = shippingOrderService.shippingOrderContainsProductVariant(so, productVariant, mrp);
							if (sohaspv && purchaseOrder.getWarehouse().equals(so.getWarehouse())) {
								shippingOrdersInPO.add(so);
							}
						}
						Long poQty = 0L;
						Double taxableAmount = 0.0D;
						Double discountPercentage = 0D;
						PoLineItem poLineItem = new PoLineItem();
						poLineItem.setSku(sku);
						if (Math.abs(unbookedInventory) < quantity) {
							poQty = Math.abs(unbookedInventory);
						} else {
							poQty = quantity;
						}
						poLineItem.setQty(poQty);
						poLineItem.setCostPrice(pvmq.getCostPrice());
						poLineItem.setMrp(mrp);
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
							TaxComponent taxComponent = TaxUtil.getSupplierTaxForVariedTaxRatesWithoutSku(purchaseOrder.getSupplier(), purchaseOrder.getWarehouse()
									.getState(), tax, taxableAmount);
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
				purchaseOrder.setShippingOrders(new ArrayList<ShippingOrder>(shippingOrdersInPO));
				for(ShippingOrder so: shippingOrdersInPO){
					for(LineItem li : so.getLineItems()){
						if(li.getSku().getProductVariant().getProduct().isDropShipping()||li.getSku().getProductVariant().getProduct().isJit()){
							isRegular = false;
						}
					}
				}
				if(isRegular){
					purchaseOrder.setPurchaseOrderType(EnumPurchaseOrderType.REGULAR.asEnumPurchaseOrderType());
				}
				if (containsDropShip) {
					purchaseOrder.setPurchaseOrderType(EnumPurchaseOrderType.DROP_SHIP.asEnumPurchaseOrderType());
				}
				purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);
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
				if (!purchaseOrder.getPurchaseOrderType().equals(EnumPurchaseOrderType.DROP_SHIP.asEnumPurchaseOrderType())
						&& purchaseOrder.getPoLineItems() != null && purchaseOrder.getPoLineItems().size() > 0) {
					List<ProductVariant> pvList = purchaseOrderService.getAllProductVariantFromPO(purchaseOrder);
					boolean send = true;
					for (ProductVariant pv : pvList) {
						if (pv.getProduct().getPrimaryCategory().getName().equals(CategoryConstants.EYE))
							send = false;
					}
					if (send) {
						if (purchaseOrder.getPoLineItems().get(0).getExtraInventoryLineItem() == null) {
							if (purchaseOrder.getSupplier().getEmail_id() != null) {
								adminEmailManager.sendPOMailToSupplier(purchaseOrder, purchaseOrder.getSupplier().getEmail_id());
							}
						}
					}
				}
				purchaseOrder.setPurchaseOrderStatus(EnumPurchaseOrderStatus.SentToSupplier.asEnumPurchaseOrderStatus());
				purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);

			}
		}
	}

	public String getWhSupplierTinMap(String tinNumber){
		HashMap<String, String> whSupplierHashMap = new HashMap<String, String>();
		whSupplierHashMap.put("06101832327","06101832036");
		whSupplierHashMap.put("27800897340V", "27210893736V");
		whSupplierHashMap.put("07840464349", "07320452122");
		whSupplierHashMap.put("07320452122", "07320452122");
		String tin = whSupplierHashMap.get(tinNumber);
		return tin;
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}
	
	class ProductVariantMrpQtyLineItems {
		ProductVariant productVariant;
		Double mrp;
		Double costPrice;
		Long qty;

		public ProductVariant getProductVariant() {
			return productVariant;
		}

		public void setProductVariant(ProductVariant productVariant) {
			this.productVariant = productVariant;
		}

		public Double getMrp() {
			return mrp;
		}

		public void setMrp(Double mrp) {
			this.mrp = mrp;
		}

		public Double getCostPrice() {
			return costPrice;
		}

		public void setCostPrice(Double costPrice) {
			this.costPrice = costPrice;
		}

		public Long getQty() {
			return qty;
		}

		public void setQty(Long qty) {
			this.qty = qty;
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof ProductVariantMrpQtyLineItems)) {
				return false;
			}
			ProductVariantMrpQtyLineItems pvmrpqty = (ProductVariantMrpQtyLineItems) obj;
			if (this.productVariant != null && pvmrpqty.getProductVariant() != null && this.mrp != null && pvmrpqty.getMrp() != null) {
				return this.productVariant.equals(pvmrpqty.getProductVariant()) && this.mrp.equals(pvmrpqty.getMrp());
			}
			return false;
		}
	}

	public List<PurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Set<ShippingOrder> getShippingOrders() {
		return shippingOrders;
	}

	public void setShippingOrders(Set<ShippingOrder> shippingOrders) {
		this.shippingOrders = shippingOrders;
	}

	public List<String> getWarehouseIdList() {
		return warehouseIdList;
	}

	public void setWarehouseIdList(List<String> warehouseIdList) {
		this.warehouseIdList = warehouseIdList;
	}

}
