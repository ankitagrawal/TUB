package com.hk.web.action.admin.queue;

import java.util.*;
import java.util.Map.Entry;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.util.TaxUtil;
import com.hk.constants.core.EnumTax;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.constants.inventory.EnumPurchaseOrderType;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.core.Tax;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.dto.TaxComponent;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.admin.AdminHomeAction;

@Component
public class JitShippingOrderAction extends BaseAction {
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

	private Date startDate;
	private Date endDate;
	private List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();
	private List<LineItem> jitLineItems = new ArrayList<LineItem>();
	private HashMap<Supplier, List<LineItem>> supplierLineItemListMap = new HashMap<Supplier, List<LineItem>>();
	private List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();
	private List<LineItem> jitFilteredLineItems = new ArrayList<LineItem>();

	@DefaultHandler
	public Resolution pre() {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria();
		shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);
		List<ShippingOrder> shippingOrderListToProcess = new ArrayList<ShippingOrder>();
		if (shippingOrderList != null && shippingOrderList.size() > 0) {

			for (ShippingOrder shippingOrder : shippingOrderList) {
				if (shippingOrder.getPurchaseOrders() == null) {
					shippingOrderListToProcess.add(shippingOrder);
				}
			}
			jitLineItems = getJitLineItems(shippingOrderListToProcess);
			jitFilteredLineItems = autoEscalateShippingOrdersOfJitLineItems(jitLineItems);
			supplierLineItemListMap = getSupplierLineItemMap(jitFilteredLineItems);
			HashMap<Supplier, HashMap<ProductVariant, Long>> supplierVariantQtyMap = createSupplierVariantQuantityMap(supplierLineItemListMap);
			purchaseOrders = createPurchaseOrder(supplierLineItemListMap);
			createPoLineItems(purchaseOrders, supplierVariantQtyMap);
			purchaseOrders = deletePOsWithEmptyPOLineItems(purchaseOrders);
			List<PurchaseOrderStatus> purchaseOrderStatus = EnumPurchaseOrderStatus
					.getAllPurchaseOrderStatusForSystemGeneratedPOs();
			for (PurchaseOrder purchaseOrder : purchaseOrders) {
				if (purchaseOrder != null) {
					approveAllPos(purchaseOrder, purchaseOrderStatus);
					for (ShippingOrder so : purchaseOrder.getShippingOrders()) {
						ShippingOrderLifecycle shippingOrderLifecycle = new ShippingOrderLifecycle();
						shippingOrderLifecycle.setOrder(so);
						shippingOrderLifecycle.setShippingOrderLifeCycleActivity(getBaseDao().get(
								ShippingOrderLifeCycleActivity.class,
								EnumShippingOrderLifecycleActivity.SO_LoggedComment.getId()));
						shippingOrderLifecycle.setUser(userService.getAdminUser());
						shippingOrderLifecycle.setComments("PO# " + purchaseOrder.getId()
								+ " Approved for the Shipping Order");
						shippingOrderLifecycle.setActivityDate(new Date());
						shippingOrderLifecycleDao.save(shippingOrderLifecycle);
					}
				}
			}

		}

		addRedirectAlertMessage(new SimpleMessage(purchaseOrders.size()
				+ " Purchase Orders created, approved and sent to supplier for JIT shipping orders"));
		return new RedirectResolution(AdminHomeAction.class);

	}

	public ShippingOrderSearchCriteria getShippingOrderSearchCriteria() {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
		shippingOrderSearchCriteria.setContainsJitProducts(true);
		List<ShippingOrderStatus> soStatusList = new ArrayList<ShippingOrderStatus>();
		soStatusList.add(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus());
		shippingOrderSearchCriteria.setShippingOrderStatusList(soStatusList);
		return shippingOrderSearchCriteria;
	}

	public List<LineItem> getJitLineItems(List<ShippingOrder> shippingOrders) {
		List<LineItem> lineItemIsJitList = new ArrayList<LineItem>();
		if (shippingOrderList != null && shippingOrderList.size() > 0) {
			for (ShippingOrder order : shippingOrderList) {
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

	public List<LineItem> autoEscalateShippingOrdersOfJitLineItems(List<LineItem> jitLineItems) {
		if (jitLineItems != null && jitLineItems.size() > 0) {
			for (LineItem item : jitLineItems) {
				ProductVariant productVariant = item.getSku().getProductVariant();
				Long inventory = productVariantService.findNetInventory(productVariant);
				if (inventory != null && inventory.compareTo(item.getQty()) > 0) {
					shippingOrderService.automateManualEscalation(item.getShippingOrder());
				} else
					jitFilteredLineItems.add(item);
			}
		}
		return jitFilteredLineItems;
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

		return supplierItemMap;
	}

	public HashMap<Supplier, HashMap<ProductVariant, Long>> createSupplierVariantQuantityMap(
			HashMap<Supplier, List<LineItem>> supplierLineItemHashMap) {
		HashMap<Supplier, HashMap<ProductVariant, Long>> supplierVariantQuantityMap = new HashMap<Supplier, HashMap<ProductVariant, Long>>();

		Set<Entry<Supplier, List<LineItem>>> entrySet = supplierLineItemHashMap.entrySet();
		for (Entry<Supplier, List<LineItem>> entry : entrySet) {
			Supplier supplier = entry.getKey();
			List<LineItem> lineItems = entry.getValue();
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
			supplierVariantQuantityMap.put(supplier, productQtyHashMap);
		}
		return supplierVariantQuantityMap;
	}

	public List<PurchaseOrder> createPurchaseOrder(HashMap<Supplier, List<LineItem>> supplierVariantQuantityMap) {

		List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();
		if (supplierVariantQuantityMap != null && !supplierVariantQuantityMap.isEmpty()) {
			Set<Entry<Supplier, List<LineItem>>> entrySet = supplierVariantQuantityMap.entrySet();

			for (Entry<Supplier, List<LineItem>> entry : entrySet) {
				Supplier supplier = entry.getKey();
				List<LineItem> lineItems = entry.getValue();
				if (lineItems != null && lineItems.size() > 0) {
					PurchaseOrder purchaseOrder = new PurchaseOrder();
					purchaseOrder.setCreateDate(new Date());
					purchaseOrder.setCreatedBy(userService.getAdminUser());
					purchaseOrder.setSupplier(supplier);
					purchaseOrder.setPurchaseOrderType(EnumPurchaseOrderType.JIT.asEnumPurchaseOrderType());
					purchaseOrder.setPurchaseOrderStatus(getBaseDao().get(PurchaseOrderStatus.class,
							EnumPurchaseOrderStatus.Generated.getId()));
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getLeadTime());
					purchaseOrder.setEstDelDate(calendar.getTime());
					if (purchaseOrder.getSupplier().getCreditDays() != null
							&& purchaseOrder.getSupplier().getCreditDays() >= 0) {
						calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getCreditDays());
						purchaseOrder.setEstPaymentDate(calendar.getTime());
					} else {
						purchaseOrder.setEstPaymentDate(new Date());
					}
					// purchaseOrder.setWarehouse(user.getSelectedWarehouse());

					Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();
					for (LineItem lineItem : lineItems) {
						ShippingOrder so = lineItem.getShippingOrder();
						shippingOrders.add(so);
					}
					List<ShippingOrder> soList = new ArrayList<ShippingOrder>(shippingOrders);
					purchaseOrder.setShippingOrders(soList);
					purchaseOrder.setWarehouse(soList.get(0).getWarehouse());
					purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);
					purchaseOrders.add(purchaseOrder);
				}

			}
		}
		return purchaseOrders;
	}

	public void createPoLineItems(List<PurchaseOrder> purchaseOrders,
			HashMap<Supplier, HashMap<ProductVariant, Long>> supplierVariantQtyMap) {

		if (supplierVariantQtyMap != null && supplierVariantQtyMap.size() > 0) {
			for (PurchaseOrder purchaseOrder : purchaseOrders) {
				List<PoLineItem> poLineItems = new ArrayList<PoLineItem>();
				Double totalTaxable = 0.0D, totalTax = 0.0D, totalSurcharge = 0.0D, totalPayable = 0.0D;

				Set<Entry<Supplier, HashMap<ProductVariant, Long>>> entrySet = supplierVariantQtyMap.entrySet();

				for (Entry<Supplier, HashMap<ProductVariant, Long>> supplierProVarMap : entrySet) {
					Supplier supplier = supplierProVarMap.getKey();
					if (purchaseOrder.getSupplier().equals(supplier)) {
						Set<Entry<ProductVariant, Long>> prodQty = supplierProVarMap.getValue().entrySet();
						for (Entry<ProductVariant, Long> entry : prodQty) {
							ProductVariant productVariant = entry.getKey();
							Long quantity = entry.getValue();

							Long inventory = productVariantService.findNetInventory(productVariant);
							if (inventory != null && quantity.compareTo(inventory) > 0 && (quantity - inventory) > 0) {
								Long poQty = quantity - inventory;
								// TODO --
								Double taxableAmount = 0.0D;
								Double discountPercentage = 0D;
								PoLineItem poLineItem = new PoLineItem();
								Sku sku = skuService.getSKU(productVariant, purchaseOrder.getWarehouse());
								poLineItem.setSku(sku);
								poLineItem.setQty(poQty);
								poLineItem.setCostPrice(productVariant.getCostPrice());
								poLineItem.setMrp(productVariant.getMarkedPrice());
								poLineItem.setPurchaseOrder(purchaseOrder);

								Tax tax;
								if (sku != null) {
									if (purchaseOrder.getSupplier().getState()
											.equalsIgnoreCase(purchaseOrder.getWarehouse().getState())) {
										tax = sku.getTax();
									} else {
										tax = new Tax();
										tax.setId(EnumTax.CST.getId());
										tax.setName(EnumTax.CST.getName());
										tax.setType(EnumTax.CST.getType());
										tax.setValue(EnumTax.CST.getValue());
									}
									taxableAmount = quantity
											* ((productVariant.getCostPrice() - productVariant.getCostPrice()
													* discountPercentage / 100));
									totalTaxable += taxableAmount;
									TaxComponent taxComponent = TaxUtil.getSupplierTaxForVariedTaxRatesWithoutSku(
											purchaseOrder.getSupplier(), purchaseOrder.getWarehouse().getState(), tax,
											taxableAmount);
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
						break;
					}
				}
				purchaseOrder.setPayable(totalPayable);
				purchaseOrder.setTaxableAmount(totalTaxable);
				purchaseOrder.setTaxAmount(totalTax);
				purchaseOrder.setSurchargeAmount(totalSurcharge);
				purchaseOrder.setPoLineItems(poLineItems);

				purchaseOrder.setPoLineItems(poLineItems);
				purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);
			}
		}

	}

	private List<PurchaseOrder> deletePOsWithEmptyPOLineItems(List<PurchaseOrder> purchaseOrders) {
		List<PurchaseOrder> nonEmptyPOList = new ArrayList<PurchaseOrder>();
		if (purchaseOrders != null && purchaseOrders.size() > 0) {
			Iterator<PurchaseOrder> iterator = purchaseOrders.iterator();
			while (iterator.hasNext()) {
				PurchaseOrder po = iterator.next();
				if (po.getNoOfSku() == 0) {
					getBaseDao().delete(po);
					iterator.remove();
				} else
					nonEmptyPOList.add(po);
			}
		}
		return nonEmptyPOList;
	}

	public void approveAllPos(PurchaseOrder purchaseOrder, List<PurchaseOrderStatus> purchaseOrderStatus) {

		for (PurchaseOrderStatus status : purchaseOrderStatus) {
			EnumPurchaseOrderStatus poStatus = EnumPurchaseOrderStatus.getById(status.getId());
			purchaseOrder.setUpdateDate(new Date());
			purchaseOrder.setPurchaseOrderStatus(poStatus.asEnumPurchaseOrderStatus());
			purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);
			if (poStatus.getId().equals(EnumPurchaseOrderStatus.Approved.getId())) {
				adminEmailManager.sendPOApprovedEmail(purchaseOrder);
				if (purchaseOrder.getPoLineItems() != null && purchaseOrder.getPoLineItems().size() > 0) {
					if (purchaseOrder.getPoLineItems().get(0).getExtraInventoryLineItem() == null) {
						if (purchaseOrder.getSupplier().getEmail_id() != null) {
							adminEmailManager.sendPOMailToSupplier(purchaseOrder, purchaseOrder.getSupplier()
									.getEmail_id());
						}
					}
				}
				purchaseOrder
						.setPurchaseOrderStatus(EnumPurchaseOrderStatus.SentToSupplier.asEnumPurchaseOrderStatus());
				purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);

			}
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	@Validate(converter = CustomDateTypeConvertor.class)
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@Validate(converter = CustomDateTypeConvertor.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<ShippingOrder> getShippingOrderList() {
		return shippingOrderList;
	}

	public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
		this.shippingOrderList = shippingOrderList;
	}

	public List<LineItem> getJitLineItems() {
		return jitLineItems;
	}

	public void setJitLineItems(List<LineItem> jitLineItems) {
		this.jitLineItems = jitLineItems;
	}

	public HashMap<Supplier, List<LineItem>> getSupplierLineItemListMap() {
		return supplierLineItemListMap;
	}

	public void setSupplierLineItemListMap(HashMap<Supplier, List<LineItem>> supplierLineItemListMap) {
		this.supplierLineItemListMap = supplierLineItemListMap;
	}

	public List<PurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	public List<LineItem> getJitFilteredLineItems() {
		return jitFilteredLineItems;
	}

	public void setJitFilteredLineItems(List<LineItem> jitFilteredLineItems) {
		this.jitFilteredLineItems = jitFilteredLineItems;
	}

}
