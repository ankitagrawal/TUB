package com.hk.web.action.admin.queue;

import java.util.*;
import java.util.Map.Entry;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.util.TaxUtil;
import com.hk.constants.core.EnumTax;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.core.Tax;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.dto.TaxComponent;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.CustomDateTypeConvertor;

@Component
public class JitShippingOrderAction extends BasePaginatedAction {
	Page shippingOrderPage;
	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	PurchaseOrderDao purchaseOrderDao;
	@Autowired
	BaseDao baseDao;
	
	
	private Integer defaultPerPage = 40;
	private Date startDate;
	private Date endDate;
	private List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();
	private List<LineItem> jitLineItems = new ArrayList<LineItem>();
	private HashMap<Supplier, List<LineItem>> supplierLineItemListMap = new HashMap<Supplier, List<LineItem>>();

	@SuppressWarnings("unchecked")
	@DefaultHandler
	public Resolution pre() {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria();
		shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, getPageNo(),
				getPerPage());
		if (shippingOrderPage != null) {
			shippingOrderList = shippingOrderPage.getList();
			jitLineItems = getJitLineItems(shippingOrderList);
			supplierLineItemListMap = getSupplierLineItemMap(jitLineItems);
			createPurchaseOrder(supplierLineItemListMap);
			
		}

		return new ForwardResolution("/pages/admin/jitShippingOrderQueue.jsp");

	}

	public ShippingOrderSearchCriteria getShippingOrderSearchCriteria() {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
		shippingOrderSearchCriteria.setContainsJitProducts(true);
		if (startDate != null) {
			shippingOrderSearchCriteria.setPaymentStartDate(startDate);
		}
		if (endDate != null) {
			shippingOrderSearchCriteria.setPaymentEndDate(endDate);
		}
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
						if(lineItemlist.size()>0){
							for(LineItem dupItem:lineItemlist){
								if(dupItem.getSku().getProductVariant().equals(lineItem.getSku().getProductVariant())){
									dupItem.setQty(dupItem.getQty()+lineItem.getQty());
									lineItemlist.add(dupItem);
								}
								else
									lineItemlist.add(lineItem);
							}
						}
						else
							lineItemlist.add(lineItem);
					}
				}
				supplierItemMap.put(supplier, lineItemlist);
			}
		}

		return supplierItemMap;
	}

	public List<PoLineItem> createPoLineItems(PurchaseOrder purchaseOrder, List<LineItem> lineItems) {
		List<PoLineItem> poLineItems = new ArrayList<PoLineItem>();
		Double totalTaxable = 0.0D, totalTax = 0.0D, totalSurcharge = 0.0D, totalPayable = 0.0D;
		if(lineItems!=null && lineItems.size()>0){
			for(LineItem lineItem: lineItems){
				Double taxableAmount = 0.0D;
				Double discountPercentage = 0D;
				PoLineItem poLineItem = new PoLineItem();
				Sku sku = lineItem.getSku();
				ProductVariant productVariant = sku.getProductVariant();
				poLineItem.setSku(lineItem.getSku());
				poLineItem.setQty(lineItem.getQty());
				poLineItem.setCostPrice(productVariant.getCostPrice());
				poLineItem.setMrp(productVariant.getMarkedPrice());
				poLineItem.setPurchaseOrder(purchaseOrder);
				
				Tax tax;
				if (lineItem.getSku() != null) {
					if(purchaseOrder.getSupplier().getState().equalsIgnoreCase(purchaseOrder.getWarehouse().getState())){
						tax = lineItem.getSku().getTax();
					}
					else{
						tax = new Tax();
						tax.setId(EnumTax.CST.getId());
						tax.setName(EnumTax.CST.getName());
						tax.setType(EnumTax.CST.getType());
						tax.setValue(EnumTax.CST.getValue());
					}
					taxableAmount = (lineItem.getQty() * (lineItem.getCostPrice() - lineItem.getCostPrice() * discountPercentage / 100));
					totalTaxable += taxableAmount;
					TaxComponent taxComponent = TaxUtil.getSupplierTaxForVariedTaxRatesWithoutSku(purchaseOrder.getSupplier(),purchaseOrder.getWarehouse().getState(), tax, taxableAmount);
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
			purchaseOrder.setPayable(totalPayable);
			purchaseOrder.setTaxableAmount(totalTaxable);
			purchaseOrder.setTaxAmount(totalTax);
			purchaseOrder.setSurchargeAmount(totalSurcharge);
			purchaseOrder.setPoLineItems(poLineItems);
		}
		return poLineItems;
	}

	public void createPurchaseOrder(HashMap<Supplier, List<LineItem>> supplierLineItemHashMap) {

		User user = null;
		if (getPrincipal() != null) {
			user = getUserService().getUserById(getPrincipal().getId());
		}

		if (supplierLineItemHashMap != null && !supplierLineItemHashMap.isEmpty()) {
			Set<Entry<Supplier, List<LineItem>>> entrySet = supplierLineItemHashMap.entrySet();
			for (Entry<Supplier, List<LineItem>> entry : entrySet) {
				Supplier supplier = entry.getKey();
				List<LineItem> lineItems = entry.getValue();
				PurchaseOrder purchaseOrder = new PurchaseOrder();
				purchaseOrder.setCreateDate(new Date());
				purchaseOrder.setCreatedBy(user);
				purchaseOrder.setSupplier(supplier);
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
				purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);
				List<PoLineItem> poLineItems = createPoLineItems(purchaseOrder, lineItems);
				purchaseOrder.setPoLineItems(poLineItems);
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

}
