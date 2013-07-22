package com.hk.web.action.admin.queue;

import java.util.*;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

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
import com.hk.domain.catalog.Supplier;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderLifecycleDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.admin.AdminHomeAction;

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
	private List<PurchaseOrder> purchaseOrders;

	@DefaultHandler
	public Resolution pre() {
		//Warehouse warehouse = warehouseService.getWarehouseById(10l);
		boolean filterJit = false;
		int count = 0;
		List<ShippingOrder> shippingOrderListToProcess = jitShippingOrderPOCreationService.getShippingOrderListToProcess(filterJit);
		if (shippingOrderListToProcess != null && shippingOrderListToProcess.size() > 0) {
			List<LineItem> lineItemList = jitShippingOrderPOCreationService.getValidLineItems(shippingOrderListToProcess);
			Set<ShippingOrder> validShippingOrders = jitShippingOrderPOCreationService.getValidShippingOrders(lineItemList);
			List<PurchaseOrder> purchaseOrders = jitShippingOrderPOCreationService.processShippingOrderForPOCreation(lineItemList, validShippingOrders);
			addRedirectAlertMessage(new SimpleMessage(jitShippingOrderPOCreationService.getCountOfPOs()+" Purchase Orders created (From Aqua to Bright), approved and sent to supplier for JIT shipping orders. Please visit POList page to check them."));
			return new RedirectResolution(ActionAwaitingQueueAction.class);
		}
		addRedirectAlertMessage(new SimpleMessage("No Po Created Against This Action"));
		return new RedirectResolution(ActionAwaitingQueueAction.class);

	}

	public Resolution jitPoActionForBright() {
		boolean filterJit = true;
		List<ShippingOrder> shippingOrderListToProcess = jitShippingOrderPOCreationService.getShippingOrderListToProcess(filterJit);
		if (shippingOrderListToProcess != null && shippingOrderListToProcess.size() > 0) {
			List<LineItem> jitLineItemList = jitShippingOrderPOCreationService.getJitLineItems(shippingOrderListToProcess);
			Set<ShippingOrder> validShippingOrders = jitShippingOrderPOCreationService.getValidShippingOrders(jitLineItemList);
			purchaseOrders = jitShippingOrderPOCreationService.processShippingOrderForPOCreation(jitLineItemList, validShippingOrders);
			if(purchaseOrders.size()>0){
				addRedirectAlertMessage(new SimpleMessage(purchaseOrders.size()+" Purchase Orders created (From Bright to External), approved and sent to supplier. Please visit POList page to check them."));
			}
			else{
				addRedirectAlertMessage(new SimpleMessage("Purchase Orders created (From Bright to External), approved and sent to supplier. Please visit POList page to check them."));
			}
			return new RedirectResolution(ActionAwaitingQueueAction.class);
		}
		addRedirectAlertMessage(new SimpleMessage("No Po Created Against This Action"));
		return new RedirectResolution(ActionAwaitingQueueAction.class);
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
