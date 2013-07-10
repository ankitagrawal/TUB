package com.hk.web.action.admin.queue;

import java.util.*;
import java.util.Map.Entry;

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
import com.hk.domain.warehouse.Warehouse;
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

	private Date startDate;
	private Date endDate;
	private List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();
	private List<LineItem> jitLineItems = new ArrayList<LineItem>();
	private HashMap<Supplier, List<LineItem>> supplierLineItemListMap = new HashMap<Supplier, List<LineItem>>();
	private List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();
	private List<LineItem> jitFilteredLineItems = new ArrayList<LineItem>();
	private HashMap<PurchaseOrder, List<LineItem>> purOrderLineItem = new HashMap<PurchaseOrder, List<LineItem>>();

	@DefaultHandler
	public Resolution pre() {
		ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria();
		shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);
		List<ShippingOrder> shippingOrderListToProcess = new ArrayList<ShippingOrder>();
		if (shippingOrderList != null && shippingOrderList.size() > 0) {

			for (ShippingOrder shippingOrder : shippingOrderList) {
				if (shippingOrder.getPurchaseOrders() == null || (shippingOrder.getPurchaseOrders() != null && shippingOrder.getPurchaseOrders().size() == 0)) {
					shippingOrderListToProcess.add(shippingOrder);
				}
			}
			jitLineItems = jitShippingOrderPOCreationService.getJitLineItems(shippingOrderListToProcess);
			supplierLineItemListMap = jitShippingOrderPOCreationService.getSupplierLineItemMap(jitLineItems);
			HashMap<Supplier, HashMap<Warehouse, List<LineItem>>> supplierWhLineItemsMap = jitShippingOrderPOCreationService
					.createSupplierWhLineitemsMap(supplierLineItemListMap);
			purOrderLineItem = jitShippingOrderPOCreationService.createPurchaseOrder(supplierWhLineItemsMap);
			HashMap<PurchaseOrder, HashMap<ProductVariant, Long>> purchaseOrderProductVariantMap = jitShippingOrderPOCreationService
					.createPurchaseOrderVariantQuantityMap(purOrderLineItem);
			jitShippingOrderPOCreationService.createPoLineItems(purchaseOrderProductVariantMap);

			List<PurchaseOrder> purchaseOrderList = new ArrayList<PurchaseOrder>();
			Set<Entry<PurchaseOrder, HashMap<ProductVariant, Long>>> entrySet = purchaseOrderProductVariantMap.entrySet();
			for (Entry<PurchaseOrder, HashMap<ProductVariant, Long>> purchaseOrderPVEntry : entrySet) {
				PurchaseOrder purchaseOrder = purchaseOrderPVEntry.getKey();
				purchaseOrderList.add(purchaseOrder);
			}

			purchaseOrders = jitShippingOrderPOCreationService.deletePOsWithEmptyPOLineItems(purchaseOrderList);

			List<PurchaseOrderStatus> purchaseOrderStatus = EnumPurchaseOrderStatus.getAllPurchaseOrderStatusForSystemGeneratedPOs();
			for (PurchaseOrder purchaseOrder : purchaseOrderList) {
				if (purchaseOrder != null) {
					jitShippingOrderPOCreationService.approveAllPos(purchaseOrder, purchaseOrderStatus);
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

		}

		addRedirectAlertMessage(new SimpleMessage(purchaseOrders.size() + " Purchase Orders created, approved and sent to supplier for JIT shipping orders"));
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

	public Resolution checkShippingOrderForCornerCases() {

		List<ShippingOrder> shippingOrders = purchaseOrderService.getCancelledShippingOrderFromSoPo();
		if (shippingOrders != null && shippingOrders.size() > 0) {
			for (ShippingOrder so : shippingOrders) {
				List<PurchaseOrder> pos = so.getPurchaseOrders();
				if (pos != null && pos.size() > 0) {
					adminEmailManager.sendJitShippingCancellationMail(so,null, EnumJitShippingOrderMailToCategoryReason.SO_CANCELLED);
				}
			}
		}
		logger.debug("size of shipping orders - " + shippingOrders.size());
		return new RedirectResolution(AdminHomeAction.class);
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
