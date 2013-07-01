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

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.util.TaxUtil;
import com.hk.constants.core.EnumTax;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.constants.inventory.EnumPurchaseOrderType;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.core.Tax;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.dto.TaxComponent;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.admin.AdminHomeAction;

@Component
public class JitShippingOrderAction extends BasePaginatedAction {
	Page shippingOrderPage;
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

	private Integer defaultPerPage = 40;
	private Date startDate;
	private Date endDate;
	private List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();
	private List<LineItem> jitLineItems = new ArrayList<LineItem>();
	private HashMap<Supplier, List<LineItem>> supplierLineItemListMap = new HashMap<Supplier, List<LineItem>>();
	private List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();

	@SuppressWarnings("unchecked")
	@DefaultHandler
	public Resolution pre() {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria();
		shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, getPageNo(),
				getPerPage());
		List<ShippingOrder> shippingOrderListToProcess = new ArrayList<ShippingOrder>();
		if (shippingOrderPage != null) {
			shippingOrderList = shippingOrderPage.getList();

			for (ShippingOrder shippingOrder : shippingOrderList) {
				if (shippingOrder.getPurchaseOrders() == null) {
					shippingOrderListToProcess.add(shippingOrder);
				}
			}
			jitLineItems = getJitLineItems(shippingOrderListToProcess);
			supplierLineItemListMap = getSupplierLineItemMap(jitLineItems);
			HashMap<Supplier, HashMap<ProductVariant, Long>> supplierVariantQtyMap = createSupplierVariantQuantityMap(supplierLineItemListMap);
			// createPurchaseOrder(supplierVariantQtyMap);
			purchaseOrders = createPurchaseOrder(supplierLineItemListMap);
			createPoLineItems(purchaseOrders, supplierVariantQtyMap);
			List<PurchaseOrderStatus> purchaseOrderStatus = EnumPurchaseOrderStatus
					.getAllPurchaseOrderStatusForSystemGeneratedPOs();
			for (PurchaseOrder purchaseOrder : purchaseOrders) {
				approveAllPos(purchaseOrder, purchaseOrderStatus);
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

							Double taxableAmount = 0.0D;
							Double discountPercentage = 0D;
							PoLineItem poLineItem = new PoLineItem();
							Sku sku = skuService.getSKU(productVariant, purchaseOrder.getWarehouse());
							poLineItem.setSku(sku);
							poLineItem.setQty(quantity);
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
		User user = null;
		if (getPrincipal() != null) {
			user = getUserService().getUserById(getPrincipal().getId());
		}

		if (supplierVariantQuantityMap != null && !supplierVariantQuantityMap.isEmpty()) {
			Set<Entry<Supplier, List<LineItem>>> entrySet = supplierVariantQuantityMap.entrySet();

			for (Entry<Supplier, List<LineItem>> entry : entrySet) {
				Supplier supplier = entry.getKey();
				List<LineItem> lineItems = entry.getValue();
				PurchaseOrder purchaseOrder = new PurchaseOrder();
				purchaseOrder.setCreateDate(new Date());
				purchaseOrder.setCreatedBy(user);
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
				purchaseOrder.setWarehouse(user.getSelectedWarehouse());

				Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();
				for (LineItem lineItem : lineItems) {
					ShippingOrder so = lineItem.getShippingOrder();
					shippingOrders.add(so);
				}
				List<ShippingOrder> soList = new ArrayList<ShippingOrder>(shippingOrders);
				purchaseOrder.setShippingOrders(soList);
				purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);
				purchaseOrders.add(purchaseOrder);
			}
		}
		return purchaseOrders;
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

	public Page getShippingOrderPage() {
		return shippingOrderPage;
	}

	public void setShippingOrderPage(Page shippingOrderPage) {
		this.shippingOrderPage = shippingOrderPage;
	}

	public int getPerPageDefault() {
		return defaultPerPage;
	}

	public int getPageCount() {
		return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalPages();
	}

	public int getResultCount() {
		return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalResults();
	}

	public Integer getDefaultPerPage() {
		return defaultPerPage;
	}

	public void setDefaultPerPage(Integer defaultPerPage) {
		this.defaultPerPage = defaultPerPage;
	}

	@Override
	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("startDate");
		params.add("endDate");
		return null;
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

}
