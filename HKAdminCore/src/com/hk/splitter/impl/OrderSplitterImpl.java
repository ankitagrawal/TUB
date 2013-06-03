package com.hk.splitter.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.admin.util.helper.OrderSplitterHelper;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.warehouse.EnumWarehouseType;
import com.hk.core.fliter.OrderSplitterFilter;
import com.hk.domain.core.Pincode;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.shippingOrder.ShippingOrderCategory;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.OrderSplitException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.pact.dao.sku.SkuDao;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.splitter.OrderSplitter;
import com.hk.pojo.DummyOrder;
import com.hk.service.ServiceLocatorFactory;
import com.hk.splitter.LineItemBucket;
import com.hk.splitter.LineItemClassification;
import com.hk.splitter.LineItemClassification.UniqueWhCombination;
import com.hk.splitter.LineItemContainer;
import com.hk.splitter.LineItemContainer.Classification;
import com.hk.splitter.WarehouseBucket;

@Service
public class OrderSplitterImpl implements OrderSplitter {

	private static Logger logger = LoggerFactory.getLogger(OrderSplitterImpl.class);

	@Autowired private OrderLoggingService orderLoggingService;
	@Autowired private SkuDao skuDao;
	@Autowired private WarehouseService warehouseService;
	@Autowired private InventoryService inventoryService;
	@Autowired private OrderSplitterHelper orderSplitterHelper;
	@Autowired private ShippingOrderService shippingOrderService;
	@Autowired private SkuService skuService;

	@Override
	@Transactional
	public Set<ShippingOrder> split(long orderId) {
		Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();

		Order order = getOrderService().find(orderId);
		if (order.isB2bOrder()) {
			logger.debug("order with gatewayId:" + order.getGatewayOrderId() + " is B2B order. Will not split.");
			return Collections.emptySet();
		}

		validate(order);

		Warehouse defaultWareHouse = warehouseService.getDefaultWarehouse();
		Collection<Warehouse> whs = warehouseService.getAllActiveWarehouses();
		List<Warehouse> filteredWarehoues = new ArrayList<Warehouse>();
		for (Warehouse warehouse : whs) {
			if (warehouse.isHonoringB2COrders() && (warehouse.getWarehouseType().equals(EnumWarehouseType.Online_B2C.getId()) 
					|| warehouse.getWarehouseType().equals(EnumWarehouseType.Online_B2B.getId()))) {
				filteredWarehoues.add(warehouse);
			}
		}
		
		if(filteredWarehoues.size() == 0) {
			return Collections.emptySet();
		}
		
		long startTime = System.currentTimeMillis();

		LineItemContainer container = new LineItemContainer();
		for (CartLineItem cartLineItem : order.getCartLineItems()) {
			if (cartLineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId())) {
				boolean isAdded = false;
				List<Sku> skuList = skuDao.getSkus(cartLineItem.getProductVariant(), filteredWarehoues);
				if (skuList != null && !skuList.isEmpty()) {
					for (Sku sku : skuList) {
						if(inventoryService.getAvailableUnbookedInventory(sku) >= 0) {
							container.addLineItem(sku.getWarehouse(), cartLineItem);
							isAdded = true;
						}
					}
				}
				if(!isAdded) {
					container.addLineItem(defaultWareHouse, cartLineItem);
				}
			}
		}

		Collection<LineItemClassification> lineItemClassifications = container.getAllClassifications();
		for (LineItemClassification lic : lineItemClassifications) {
			if (lic.getClassification() == Classification.SERVICE) {
				Collection<LineItemBucket> lineItemBuckets = lic.getLineItemBuckets();
				for (LineItemBucket lineItemBucket : lineItemBuckets) {
					shippingOrders.add(getOrderService().createSOForService(lineItemBucket.getLineItem()));
				}
			} else {
				Collection<UniqueWhCombination> whCombinations = lic.generatePerfactCombinations();
				List<DummyOrder> bestShips = null;
				long bestCost = Long.MAX_VALUE;
				for (UniqueWhCombination uniqueWhCombination : whCombinations) {
					List<DummyOrder> dummyOrders = createDummyOrders(order, uniqueWhCombination);
					long cost = calculateCost(order, dummyOrders);
					if (cost < bestCost) {
						bestCost = cost;
						bestShips = dummyOrders;
					}
				}
				
				if(bestCost == Long.MAX_VALUE) {
					throw new OrderSplitException("Order is not good for split.", order); 
				}
				
				if(bestShips != null) {
					shippingOrders.addAll(createDaywiseShippingOrders(order, bestShips));
				}
			}
		}
		logger.debug("Total time to split order[" + order.getId() + "] = " + (System.currentTimeMillis() - startTime));
		return shippingOrders;
	}

	private Collection<ShippingOrder> createDaywiseShippingOrders(Order order, List<DummyOrder> dummyOrders) {
		Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();
		for (DummyOrder dummyOrder : dummyOrders) {
			Warehouse warehouse = dummyOrder.getWarehouse();
			Map<String, List<CartLineItem>> bucketedCartLineItemMap = OrderSplitterFilter .bucketCartLineItems(dummyOrder.getCartLineItemList());
			for (Map.Entry<String, List<CartLineItem>> bucketedCartLineItemMapEntry : bucketedCartLineItemMap.entrySet()) {
				ShippingOrder shippingOrder = shippingOrderService.createSOWithBasicDetails(order, warehouse);
				boolean isDropShipped = false;
				boolean containsJitProducts = false;
				for (CartLineItem cartLineItem : bucketedCartLineItemMapEntry.getValue()) {
					isDropShipped = cartLineItem.getProductVariant().getProduct().isDropShipping();
					containsJitProducts = cartLineItem.getProductVariant().getProduct().isJit();
					Sku sku = skuService.getSKU(cartLineItem.getProductVariant(), warehouse);
					LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
					shippingOrder.getLineItems().add(shippingOrderLineItem);
				}
				shippingOrder.setDropShipping(isDropShipped);
				shippingOrder.setContainsJitProducts(containsJitProducts);
				ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, order);
				shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
				shippingOrder = shippingOrderService.save(shippingOrder);
				shippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
				shippingOrder = shippingOrderService.save(shippingOrder);
				Set<ShippingOrderCategory> categories = getOrderService().getCategoriesForShippingOrder(shippingOrder);
				shippingOrder.setShippingOrderCategories(categories);
				shippingOrder.setBasketCategory(getOrderService().getBasketCategory(categories).getName());
				shippingOrder = shippingOrderService.save(shippingOrder);
				shippingOrders.add(shippingOrder);
			}
		}
		return shippingOrders;
	}

	private void validate(Order order) {
		logger.info("IN THE SPLITTER WITH ORDER-ID: " + order.getId() + " ORDER STATUS: " + order.getOrderStatus().getName());
		if (!EnumOrderStatus.Placed.getId().equals(order.getOrderStatus().getId())) {
			logger.debug("order with gatewayId:" + order.getGatewayOrderId()
					+ " is not in placed status. abort system split and do a manual split");
		}
		validatePincode(order);
		validatePayment(order);
	}

	private void validatePincode(Order order) {
		Pincode pincode = order.getAddress().getPincode();
		if (pincode == null) {
			String comments = "Pincode does not exist in our system, Please get in touch with OPS or customer care";
			orderLoggingService.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit,
					comments);
			throw new OrderSplitException(comments + ". Aborting splitting of order.", order);
		}
	}

	private void validatePayment(Order order) {
		if (order.getPayment() == null) {
			String comments = "No Payment Associated with order";
			orderLoggingService.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit,
					comments);
			throw new OrderSplitException(comments + ". Aborting splitting of order.", order);
		}
	}

	private List<DummyOrder> createDummyOrders(Order order, UniqueWhCombination combination) {
		Collection<WarehouseBucket> whBuckets = combination.getBuckets();
		List<DummyOrder> dummyOrders = new ArrayList<DummyOrder>();

		for (WarehouseBucket warehouseBucket : whBuckets) {
			Collection<LineItemBucket> liBuckets = warehouseBucket.getAllLineItemBuckets();
			List<CartLineItem> lineItems = new ArrayList<CartLineItem>();
			for (LineItemBucket lineItemBucket : liBuckets) {
				lineItems.add(lineItemBucket.getLineItem());
			}
			DummyOrder dummyOrder = new DummyOrder(lineItems, warehouseBucket.getWarehouse(), order.isCOD(), order
					.getAddress().getPincode(), order.getPayment());
			dummyOrders.add(dummyOrder);
		}

		return dummyOrders;

	}

	private long calculateCost(Order order, List<DummyOrder> dummyOrders) {
		if (order.isCOD()) {
			Double codMinAmountForSplitting = 50D; // so that orders get split
			for (DummyOrder splitDummyOrder : dummyOrders) {
				double amount = splitDummyOrder.getAmount();
				if (splitDummyOrder.getCartLineItemList().size() > 0 && amount > 0D && amount < codMinAmountForSplitting) {
					return Long.MAX_VALUE;
				}
			}
		}
		return orderSplitterHelper.calculateShippingPlusTax(dummyOrders);
	}
	
	public OrderService getOrderService() {
		return ServiceLocatorFactory.getService(OrderService.class);
	}
}
