package com.hk.web.action.admin.queue;

import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.shippingOrder.JitShippingOrderPOCreationService;
import com.hk.admin.util.TaxUtil;
import com.hk.constants.EnumJitShippingOrderMailToCategoryReason;
import com.hk.constants.core.EnumTax;
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
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.web.filter.WebContext;

@Component
public class JitShippingOrderAction extends BaseAction {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(JitShippingOrderAction.class);

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
	JitShippingOrderPOCreationService jitShippingOrderPOCreationService;
	@Autowired
	WarehouseService warehouseService;

	private List<LineItem> jitLineItems = new ArrayList<LineItem>();
	private HashMap<Supplier, List<LineItem>> supplierLineItemListMap = new HashMap<Supplier, List<LineItem>>();
	private List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();
	private List<LineItem> jitFilteredLineItems = new ArrayList<LineItem>();
	private Warehouse warehouse;

	@DefaultHandler
	public Resolution pre() {
		if(getPrincipalUser() != null && getPrincipalUser().getSelectedWarehouse() != null){
			warehouse = getPrincipalUser().getSelectedWarehouse();
		}
		//Warehouse warehouse = warehouseService.getWarehouseById(10l);
		boolean filterJit = false;
		List<ShippingOrder> shippingOrderListToProcess = jitShippingOrderPOCreationService.getShippingOrderListToProcess(filterJit);
		if (shippingOrderListToProcess != null && shippingOrderListToProcess.size() > 0) {
			List<LineItem> lineItemList = jitShippingOrderPOCreationService.getLineItems(shippingOrderListToProcess);
			purchaseOrders = jitShippingOrderPOCreationService.processShippingOrderForPOCreation(lineItemList, shippingOrderListToProcess);
			addRedirectAlertMessage(new SimpleMessage(purchaseOrders.size() + " Purchase Orders created (From Aqua to Bright), approved and sent to supplier for JIT shipping orders"));
			return new RedirectResolution(AdminHomeAction.class);
		}
		addRedirectAlertMessage(new SimpleMessage("No Po Created Against This Action"));
		return new RedirectResolution(AdminHomeAction.class);

	}

	public Resolution jitPoActionForBright() {
		boolean filterJit = true;
		List<ShippingOrder> shippingOrderListToProcess = jitShippingOrderPOCreationService.getShippingOrderListToProcess(filterJit);
		if (shippingOrderListToProcess != null && shippingOrderListToProcess.size() > 0) {
			List<LineItem> jitLineItemList = jitShippingOrderPOCreationService.getJitLineItems(shippingOrderListToProcess);
			purchaseOrders = jitShippingOrderPOCreationService.processShippingOrderForPOCreation(jitLineItemList, shippingOrderListToProcess);
			addRedirectAlertMessage(new SimpleMessage(purchaseOrders.size() + " Purchase Orders created (from Bright To External), approved and sent to supplier for JIT shipping orders"));
			return new RedirectResolution(AdminHomeAction.class);
		}
		addRedirectAlertMessage(new SimpleMessage("No Po Created Against This Action"));
		return new RedirectResolution(AdminHomeAction.class);
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
