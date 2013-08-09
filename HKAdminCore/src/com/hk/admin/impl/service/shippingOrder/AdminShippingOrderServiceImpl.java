package com.hk.admin.impl.service.shippingOrder;

import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.PurchaseOrderService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.EnumJitShippingOrderMailToCategoryReason;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Shipment;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.order.*;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.shippingOrder.ShippingOrderCategory;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.impl.service.queue.BucketService;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.service.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AdminShippingOrderServiceImpl implements AdminShippingOrderService {

	private static Logger logger = LoggerFactory.getLogger(AdminShippingOrderServiceImpl.class);
	@Autowired
	AwbService awbService;
	@Autowired
	UserService userService;
	@Autowired
	AdminEmailManager adminEmailManager;
	@Autowired
	SkuItemLineItemService skuItemLineItemService;
	@Autowired
	InventoryHealthService inventoryHealthService;
	@Autowired
	BaseDao baseDao;
	@Autowired
	PurchaseOrderService purchaseOrderService;
	@Autowired
	LineItemDao lineItemDao;
	@Autowired
	ShippingOrderDao shippingOrderDao;
	@Autowired
	SkuItemDao skuItemDao;
	@Autowired
	private ShippingOrderService shippingOrderService;
	@Autowired
	private AdminInventoryService adminInventoryService;
	@Autowired
	private BucketService bucketService;
	@Autowired
	private PincodeCourierService pincodeCourierService;
	private String cancellationRemark;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ShippingOrderStatusService shippingOrderStatusService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private WarehouseService warehouseService;
	private AdminOrderService adminOrderService;
	@Autowired
	private ShipmentService shipmentService;
	@Autowired
	private AdminShippingOrderDao adminShippingOrderDao;
	@Autowired
	private LoyaltyProgramService loyaltyProgramService;

	public void cancelShippingOrder(ShippingOrder shippingOrder, String cancellationRemark) {
		// Check if Order is in Action Queue before cancelling it.
		if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
			logger.warn("Cancelling Shipping order gateway id:::" + shippingOrder.getGatewayOrderId());
			shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Cancelled));
			skuItemLineItemService.freeInventoryForSOCancellation(shippingOrder);
			//shippingOrder = getShippingOrderService().save(shippingOrder);
//            getAdminInventoryService().reCheckInInventory(shippingOrder);
			getAdminInventoryService().reCheckInInventory(shippingOrder, EnumSkuItemStatus.Checked_IN, EnumSkuItemOwner.SELF, EnumInvTxnType.CANCEL_CHECKIN, 1L);
			// TODO : Write a generic ROLLBACK util which will essentially release all attached laibilities i.e.
			// inventory, reward points, shipment, discount
			getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Cancelled, shippingOrder.getReason(), cancellationRemark);

			orderService.updateOrderStatusFromShippingOrders(shippingOrder.getBaseOrder(), EnumShippingOrderStatus.SO_Cancelled, EnumOrderStatus.Cancelled);
			if (shippingOrder.getShipment() != null) {
				Awb awbToRemove = shippingOrder.getShipment().getAwb();
				awbService.preserveAwb(awbToRemove);
				Shipment shipmentToDelete = shippingOrder.getShipment();
				shippingOrder.setShipment(null);
				shipmentService.delete(shipmentToDelete);
				//shippingOrderService.save(shippingOrder);
			}
			shippingOrder = getShippingOrderService().save(shippingOrder);
			if (shippingOrder.getPurchaseOrders() != null && shippingOrder.getPurchaseOrders().size() > 0) {
				adminEmailManager.sendJitShippingCancellationMail(shippingOrder, null, EnumJitShippingOrderMailToCategoryReason.SO_CANCELLED);
			}
			getBucketService().popFromActionQueue(shippingOrder);
		}
		for (LineItem lineItem : shippingOrder.getLineItems()) {
			getInventoryService().checkInventoryHealth(lineItem.getSku().getProductVariant());
		}
	}

	public boolean updateWarehouseForShippingOrder(ShippingOrder shippingOrder, Warehouse warehouse) {
		Set<LineItem> lineItems = shippingOrder.getLineItems();
		boolean shouldUpdate = true;

		List<Sku> toSkuList = new ArrayList<Sku>();
		try {
			for (LineItem lineItem : lineItems) {
				toSkuList.clear();
				toSkuList.add(getSkuService().getSKU(lineItem.getSku().getProductVariant(), warehouse));
				List<SkuItem> skuItemList = getSkuItemDao().getSkuItems(toSkuList, Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()), Arrays.asList(EnumSkuItemOwner.SELF.getId()), lineItem.getMarkedPrice());
				if (skuItemList != null && skuItemList.size() >= lineItem.getQty()) {
					lineItem.setSku(toSkuList.get(0));
				} else {
					return false;
				}
			}
			shouldUpdate = skuItemLineItemService.isWarehouseBeFlippable(shippingOrder, warehouse);
			logger.debug("isWarehouseBeFlippable = " + shouldUpdate);
			if (shouldUpdate) {
				shippingOrder.setWarehouse(warehouse);
				shipmentService.recreateShipment(shippingOrder);
				shippingOrder = getShippingOrderService().save(shippingOrder);
				if (shippingOrder.getShippingOrderStatus().equals(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus()) && shippingOrder.getPurchaseOrders() != null && shippingOrder.getPurchaseOrders().size() > 0) {
					adminEmailManager.sendJitShippingCancellationMail(shippingOrder, null, EnumJitShippingOrderMailToCategoryReason.SO_WAREHOUSE_FLIPPED);
				}
				getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_WarehouseChanged);
				for (LineItem lineItem : shippingOrder.getLineItems()) {
					inventoryHealthService.inventoryHealthCheck(lineItem.getSku().getProductVariant());
				}
			}

		} catch (NoSkuException noSku) {
			shouldUpdate = false;
		}
		return shouldUpdate;
	}

	public ShippingOrder createSOforManualSplit(Set<CartLineItem> cartLineItems, Warehouse warehouse) {

		if (cartLineItems != null && !cartLineItems.isEmpty() && warehouse != null) {
			Order baseOrder = cartLineItems.iterator().next().getOrder();
			ShippingOrder shippingOrder = getShippingOrderService().createSOWithBasicDetails(baseOrder, warehouse);
			shippingOrder.setBaseOrder(baseOrder);

			for (CartLineItem cartLineItem : cartLineItems) {
				ProductVariant productVariant = cartLineItem.getProductVariant();
				Sku sku = getSkuService().getSKU(productVariant, warehouse);
				if (sku != null) {
					LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
					shippingOrder.getLineItems().add(shippingOrderLineItem);
				} else {
					throw new NoSkuException(productVariant, warehouse);
				}
			}

			ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, baseOrder);
			shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
			shippingOrder = getShippingOrderService().save(shippingOrder);
			/**
			 * this additional call to save is done so that we have shipping order id to generate shipping order gateway
			 * id
			 */
			shippingOrder = getShippingOrderService().setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
			shippingOrder = getShippingOrderService().save(shippingOrder);
			Set<ShippingOrderCategory> categories = getOrderService().getCategoriesForShippingOrder(shippingOrder);
			shippingOrder.setShippingOrderCategories(categories);
			shippingOrder.setBasketCategory(getOrderService().getBasketCategory(categories).getName());
			shippingOrder = getShippingOrderService().save(shippingOrder);

			//shipmentService.createShipment(shippingOrder);
			// auto escalate shipping orders if possible
			//getShippingOrderService().autoEscalateShippingOrder(shippingOrder);

			//		orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(baseOrder);

			//Validate SO for SkuItem booking
			shippingOrderService.validateShippingOrder(shippingOrder);

			return shippingOrder;
		}
		return null;
	}

	@Transactional
	public ShippingOrder putShippingOrderOnHold(ShippingOrder shippingOrder) {
		Long qty = 0L;
		if (shippingOrder.getShippingOrderStatus().getId() >= EnumShippingOrderStatus.SO_CheckedOut.getId()) {
			qty = 1L;
		}
		if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_ActionAwaiting.getId())) {
			shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_OnHold));
//            getAdminInventoryService().reCheckInInventory(shippingOrder);
			getAdminInventoryService().reCheckInInventory(shippingOrder, EnumSkuItemStatus.BOOKED, EnumSkuItemOwner.SELF, EnumInvTxnType.CANCEL_CHECKIN, qty);
			shippingOrder = getShippingOrderService().save(shippingOrder);
			getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_PutOnHold);
		}
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder unHoldShippingOrder(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ActionAwaiting));
		shippingOrder = getShippingOrderService().save(shippingOrder);

		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_RemovedOnHold);
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder
	markShippingOrderAsDelivered(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Delivered));
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Delivered);
		Order order = shippingOrder.getBaseOrder();
		getAdminOrderService().markOrderAsDelivered(order);
		loyaltyProgramService.approveKarmaPoints(shippingOrder.getBaseOrder());
//	    smsManager.sendOrderDeliveredSMS(shippingOrder);
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder markShippingOrderAsInstalled(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Installed));
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Installed);
		Order order = shippingOrder.getBaseOrder();
		getAdminOrderService().markOrderAsCompletedWithInstallation(order);
//	    smsManager.sendOrderDeliveredSMS(shippingOrder);
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder markShippingOrderAsRTO(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_RTO));
		shippingOrder.getShipment().setReturnDate(new Date());
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Returned);
		Order order = shippingOrder.getBaseOrder();
		getAdminOrderService().markOrderAsRTO(order);
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder markShippingOrderAsLost(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Lost));
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Lost);
		Order order = shippingOrder.getBaseOrder();
		getAdminOrderService().markOrderAsLost(order);
		return shippingOrder;
	}

	public ShippingOrder initiateRTOForShippingOrder(ShippingOrder shippingOrder, ReplacementOrderReason rtoReason) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.RTO_Initiated));
		getShippingOrderService().save(shippingOrder);
		if (rtoReason != null) {
			getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RTO_Initiated, null, rtoReason.getName());


		} else {
			getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.RTO_Initiated);
		}
		return shippingOrder;
	}

	public List<ShippingOrder> getShippingOrderListByCouriers(Date startDate, Date endDate, List<Long> courierId) {
		return getAdminShippingOrderDao().getShippingOrderListByCouriers(startDate, endDate, courierId);
	}

	@Transactional
	public ShippingOrder markShippingOrderAsShipped(ShippingOrder shippingOrder) {
		Shipment shipment = shippingOrder.getShipment();
		if (shipment != null) {
			shipment.getAwb().setAwbStatus(EnumAwbStatus.Used.getAsAwbStatus());
			shipment.setShipDate(new Date());
			getShipmentService().save(shipment);
		}
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Shipped));
		getPincodeCourierService().setTargetDeliveryDate(shippingOrder);
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Shipped);
		getBucketService().popFromActionQueue(shippingOrder);
		getAdminOrderService().markOrderAsShipped(shippingOrder.getBaseOrder());
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder markShippingOrderAsPrinted(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_MarkedForPrinting));
		getShippingOrderService().save(shippingOrder);

		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ChosenForPrinting);

		return shippingOrder;
	}

	@Transactional
	public ShippingOrder moveShippingOrderToPickingQueue(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_Picking));
		getShippingOrderService().save(shippingOrder);

		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_InPicking);

		return shippingOrder;
	}

	@Transactional
	public ShippingOrder moveShippingOrderBackToActionQueue(ShippingOrder shippingOrder) {
		Long qty = 0L;
		if (shippingOrder.getShippingOrderStatus().getId() >= EnumShippingOrderStatus.SO_CheckedOut.getId()) {
			qty = 1L;
		}
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_OnHold));
//        getAdminInventoryService().reCheckInInventory(shippingOrder);

		getAdminInventoryService().reCheckInInventory(shippingOrder, EnumSkuItemStatus.BOOKED, EnumSkuItemOwner.SELF, EnumInvTxnType.CANCEL_CHECKIN, qty);
		shippingOrder = getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_EscalatedBackToActionQueue, shippingOrder.getReason(), null);

		getBucketService().escalateBackToActionQueue(shippingOrder);
		return shippingOrder;
	}

	@Transactional
	public ShippingOrder moveShippingOrderBackToPackingQueue(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForProcess));
//        getAdminInventoryService().reCheckInInventory(shippingOrder);

		getAdminInventoryService().reCheckInInventory(shippingOrder, EnumSkuItemStatus.BOOKED, EnumSkuItemOwner.SELF, EnumInvTxnType.CANCEL_CHECKIN, 0L);
		getShippingOrderService().save(shippingOrder);

		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_BackToPackingQueue);

		return shippingOrder;
	}

	@Transactional
	public ShippingOrder moveShippingOrderBackToDropShippingQueue(ShippingOrder shippingOrder) {
		shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForDropShipping));
//           getAdminInventoryService().reCheckInInventory(shippingOrder);
		getShippingOrderService().save(shippingOrder);
		getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_BackToDropShippingQueue);
		return shippingOrder;
	}

	public ReplacementOrderReason getRTOReasonForShippingOrder(ShippingOrder shippingOrder) {
		String rtoReason = null;
		ReplacementOrderReason replacementOrderReason = null;
		for (ShippingOrderLifecycle shippingOrderLifecycle : shippingOrder.getShippingOrderLifecycles()) {
			if (shippingOrderLifecycle.getShippingOrderLifeCycleActivity().getId().equals(EnumShippingOrderLifecycleActivity.RTO_Initiated.getId())) {
				if (shippingOrderLifecycle.getComments() != null) {
					replacementOrderReason = getReplacementOrderReasonByName(shippingOrderLifecycle.getComments());
				}
			}
		}
		return replacementOrderReason;
	}

	public ReplacementOrderReason getReplacementOrderReasonByName(String replacementOrderReasonString) {
		List<ReplacementOrderReason> replacementOrderReasonList = getAdminShippingOrderDao().getAll(ReplacementOrderReason.class);
		for (ReplacementOrderReason replacementOrderReason : replacementOrderReasonList) {
			if (replacementOrderReasonString.contains(replacementOrderReason.getName())) {
				return replacementOrderReason;
			}
		}
		return null;
	}

	public void adjustPurchaseOrderForSplittedShippingOrder(ShippingOrder shippingOrder, ShippingOrder newShippingOrder) {
		List<PurchaseOrder> poList = shippingOrder.getPurchaseOrders();
		Set<PurchaseOrder> newShippingOrderPoSet = new HashSet<PurchaseOrder>();
		Set<PurchaseOrder> parentShippingOrderPoSet = new HashSet<PurchaseOrder>();
		List<ProductVariant> variantListFromSO = new ArrayList<ProductVariant>();
		for (LineItem item : shippingOrder.getLineItems()) {
			variantListFromSO.add(item.getSku().getProductVariant());
		}

		if (poList != null && poList.size() > 0) {
			for (PurchaseOrder order : poList) {
				boolean flag = false;
				List<ProductVariant> productVariants = purchaseOrderService.getAllProductVariantFromPO(order);
				if (productVariants != null && productVariants.size() > 0) {
					for (ProductVariant pv : productVariants) {
						if (variantListFromSO.contains(pv)) {
							flag = true;
						}
						boolean soHasPv = shippingOrderService.shippingOrderContainsProductVariant(newShippingOrder, pv, pv.getMarkedPrice());
						if (soHasPv) {
							newShippingOrderPoSet.add(order);
						}
					}
				}
				if (flag == true) {
					parentShippingOrderPoSet.add(order);
				}
			}
		}


		//shippingOrder = shippingOrderService.save(shippingOrder);
		newShippingOrder.setPurchaseOrders(new ArrayList<PurchaseOrder>(newShippingOrderPoSet));
		shippingOrder.setPurchaseOrders(new ArrayList<PurchaseOrder>(parentShippingOrderPoSet));

		newShippingOrder = shippingOrderService.save(newShippingOrder);
		shippingOrder = shippingOrderService.save(shippingOrder);

		for (PurchaseOrder po : newShippingOrderPoSet) {

			List<ShippingOrder> soList = po.getShippingOrders();
			soList.add(newShippingOrder);
			if (!parentShippingOrderPoSet.contains(po)) {
				soList.remove(shippingOrder);
			}
			po.setShippingOrders(soList);
			baseDao.save(po);
		}
		adminEmailManager.sendJitShippingCancellationMail(shippingOrder, newShippingOrder, EnumJitShippingOrderMailToCategoryReason.SO_SPLITTED);

	}

	public ShippingOrderService getShippingOrderService() {
		return shippingOrderService;
	}

	public void setShippingOrderService(ShippingOrderService shippingOrderService) {
		this.shippingOrderService = shippingOrderService;
	}

	public ShippingOrderStatusService getShippingOrderStatusService() {
		return shippingOrderStatusService;
	}

	public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
		this.shippingOrderStatusService = shippingOrderStatusService;
	}

	public SkuService getSkuService() {
		return skuService;
	}

	public void setSkuService(SkuService skuService) {
		this.skuService = skuService;
	}

	public WarehouseService getWarehouseService() {
		return warehouseService;
	}

	public void setWarehouseService(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	public AdminOrderService getAdminOrderService() {
		if (adminOrderService == null) {
			adminOrderService = ServiceLocatorFactory.getService(AdminOrderService.class);
		}
		return adminOrderService;
	}

	public void setAdminOrderService(AdminOrderService adminOrderService) {
		this.adminOrderService = adminOrderService;
	}

	public ShipmentService getShipmentService() {
		return shipmentService;
	}

	public void setShipmentService(ShipmentService shipmentService) {
		this.shipmentService = shipmentService;
	}

	public AdminInventoryService getAdminInventoryService() {
		return adminInventoryService;
	}

	public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
		this.adminInventoryService = adminInventoryService;
	}

	public InventoryService getInventoryService() {
		return inventoryService;
	}

	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	public AdminShippingOrderDao getAdminShippingOrderDao() {
		return adminShippingOrderDao;
	}

	public void setAdminShippingOrderDao(AdminShippingOrderDao adminShippingOrderDao) {
		this.adminShippingOrderDao = adminShippingOrderDao;
	}

	public OrderService getOrderService() {
		if (orderService == null) {
			this.orderService = ServiceLocatorFactory.getService(OrderService.class);
		}
		return orderService;
	}

	public UserService getUserService() {
		return userService;
	}

	public BucketService getBucketService() {
		return bucketService;
	}

	public void setBucketService(BucketService bucketService) {
		this.bucketService = bucketService;
	}

	public PincodeCourierService getPincodeCourierService() {
		return pincodeCourierService;
	}

	public void setPincodeCourierService(PincodeCourierService pincodeCourierService) {
		this.pincodeCourierService = pincodeCourierService;
	}

	public String getCancellationRemark() {
		return cancellationRemark;
	}

	public void setCancellationRemark(String cancellationRemark) {
		this.cancellationRemark = cancellationRemark;
	}

	public SkuItemDao getSkuItemDao() {
		return skuItemDao;
	}

}
