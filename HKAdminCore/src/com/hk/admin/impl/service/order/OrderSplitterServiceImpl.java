package com.hk.admin.impl.service.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.admin.pact.dao.courier.PincodeRegionZoneDao;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.util.helper.OrderSplitterHelper;
import com.hk.comparator.MapValueComparator;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.core.Pincode;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.OrderSplitException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderSplitterService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pojo.DummyOrder;

/**
 * Created with IntelliJ IDEA. User: Pratham Date: 5/25/12 Time: 5:48 PM To change this template use File | Settings |
 * File Templates.
 */
@Service
public class OrderSplitterServiceImpl implements OrderSplitterService {

    private static Logger        logger = LoggerFactory.getLogger(OrderSplitterServiceImpl.class);

    @Autowired
    CourierPricingEngineDao      courierPricingEngineDao;

    @Autowired
    private ShippingOrderService shippingOrderService;

    @Autowired
    PincodeRegionZoneDao         pincodeRegionZoneDao;

    @Autowired
    SkuService                   skuService;

    @Autowired
    WarehouseService             warehouseService;

    @Autowired
    InventoryService             inventoryService;

    @Autowired
    PincodeDao                   pincodeDao;

    @Autowired
    ShipmentPricingEngine        shipmentPricingEngine;

    @Autowired
    CourierGroupService          courierGroupService;

    @Autowired
    OrderSplitterHelper          orderSplitterHelper;

    @Autowired
    private OrderLoggingService  orderLoggingService;

    @Value("#{hkEnvProps['" + Keys.Env.codMinAmount + "']}")
    private Double               codMinAmount;

    public List<DummyOrder> listBestDummyOrdersPractically(Order order, Set<CartLineItem> CartlineItems) {
        TreeMap<List<DummyOrder>, Long> sortedCourierCostingTreeMap = splitBOPractically(order, CartlineItems);
        return sortedCourierCostingTreeMap.lastKey();
    }

    public List<DummyOrder> listBestDummyOrdersPractically(Order order) {
        TreeMap<List<DummyOrder>, Long> sortedCourierCostingTreeMap = splitBOPractically(order);
        return sortedCourierCostingTreeMap.lastKey();
    }

    public NavigableMap<List<DummyOrder>, Long> listSortedDummyOrderMapPractically(Order order) {
        TreeMap<List<DummyOrder>, Long> sortedCourierCostingTreeMap = splitBOPractically(order);
        return sortedCourierCostingTreeMap != null ? sortedCourierCostingTreeMap.descendingMap() : null;
    }

    public Map.Entry<List<DummyOrder>, Long> listFirstMapEntryPractically(Order order) {
        TreeMap<List<DummyOrder>, Long> sortedCourierCostingTreeMap = splitBOPractically(order);
        return sortedCourierCostingTreeMap.firstEntry();
    }

    public Map<List<DummyOrder>, Long> listAllDummyOrdersMap(Order order) {
        TreeMap<List<DummyOrder>, Long> sortedCourierCostingTreeMap = splitBOPractically(order);
        return sortedCourierCostingTreeMap.descendingMap();
    }

    public List<DummyOrder> listBestDummyOrdersIdeally(Order order, Warehouse ggnWarehouse, Warehouse mumWarehouse) {
        TreeMap<List<DummyOrder>, Long> sortedCourierCostingTreeMap = splitBOIdeally(order, ggnWarehouse, mumWarehouse);
        if (sortedCourierCostingTreeMap == null || sortedCourierCostingTreeMap.isEmpty()) {
            logger.info("sorted Courier Map null/Empty for BO order " + order.getId());
            return null;
        }
        return sortedCourierCostingTreeMap.lastKey();
    }

    @SuppressWarnings("unchecked")
    public TreeMap<List<DummyOrder>, Long> splitBOPractically(Order order, Set<CartLineItem> productCartLineItems) {

        // get static things
        Pincode pincode = order.getAddress().getPincode();
        if (pincode == null) {
            String comments = "Pincode does not exist in our system, Please get in touch with OPS or customer care";
            orderLoggingService.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
            throw new OrderSplitException(comments + ". Aborting splitting of order.", order);
        }
        if (order.getPayment() == null) {
            String comments = "No Payment Associated with order";
            orderLoggingService.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
            throw new OrderSplitException(comments + ". Aborting splitting of order.", order);
        }
        boolean isCod = order.isCOD();
        Payment payment = order.getPayment();

        // Set<CartLineItem> productCartLineItems = new
        // CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        Map<CartLineItem, Set<Warehouse>> cartLineItemWarehouseListMap = new HashMap<CartLineItem, Set<Warehouse>>();

        // iterate over product line items, make a map for the sku available (i.e having inventory as well) for each
        // warehouse
        for (CartLineItem cartLineItem : productCartLineItems) {
            //List<Sku> skuList = skuService.getSKUsForProductVariant(cartLineItem.getProductVariant());
	        List<Sku> skuList = skuService.getSKUsForProductVariantAtServiceableWarehouses(cartLineItem.getProductVariant());
            Set<Warehouse> applicableWarehousesForLineItem = null;
            if (!skuList.isEmpty()) {
                applicableWarehousesForLineItem = new HashSet<Warehouse>();
                for (Sku sku : skuList) {
                    if (inventoryService.getAvailableUnbookedInventory(sku, cartLineItem.getMarkedPrice()) >= 0) {
                        applicableWarehousesForLineItem.add(sku.getWarehouse());
                    }
                }
                cartLineItemWarehouseListMap.put(cartLineItem, applicableWarehousesForLineItem);
            }
        }

        Warehouse ggnWarehouse = warehouseService.getDefaultWarehouse();
        Warehouse mumWarehouse = warehouseService.getMumbaiWarehouse();

        List<CartLineItem> awaraCartLineItems = new ArrayList<CartLineItem>(); // these are the dicey lineItems for
                                                                                // which whole algo has been written
        List<CartLineItem> ggnKiCartLineItems = new ArrayList<CartLineItem>();
        List<CartLineItem> mumKiCartLineItems = new ArrayList<CartLineItem>();

        for (CartLineItem cartLineItem : cartLineItemWarehouseListMap.keySet()) {
            Set<Warehouse> applicableWarehouses = cartLineItemWarehouseListMap.get(cartLineItem);
            Integer applicableWarehousesSize = applicableWarehouses != null ? applicableWarehouses.size() : 0;
            if (applicableWarehousesSize == 0) {
                ggnKiCartLineItems.add(cartLineItem);
            } else if (applicableWarehousesSize == 1) {
                Warehouse applicableWarehouse = applicableWarehouses.iterator().next();
                if (applicableWarehouse.equals(ggnWarehouse)) {
                    ggnKiCartLineItems.add(cartLineItem);
                } else if (applicableWarehouse.equals(mumWarehouse)) {
                    mumKiCartLineItems.add(cartLineItem);
                }
            } else {
                awaraCartLineItems.add(cartLineItem);
            }
        }

        Integer awaraLineItemsSize = awaraCartLineItems.size();
        Map<List<DummyOrder>, Long> dummyOrderCostingMap = new HashMap<List<DummyOrder>, Long>();

        // now what happens here is ki awara LineItems ko P&C karte hain, and allocate them in ggn/Mumbai, and then
        // (shipping+cost) decide which is the best fit for them
        for (int i = 0; i <= awaraLineItemsSize; i++) {
            List<CartLineItem> ggnCartLineItems = new ArrayList<CartLineItem>();
            ggnCartLineItems.addAll(ggnKiCartLineItems);
            ggnCartLineItems.addAll(awaraCartLineItems.subList(0, i));

            List<CartLineItem> mumCartLineItems = new ArrayList<CartLineItem>();
            mumCartLineItems.addAll(mumKiCartLineItems);
            mumCartLineItems.addAll(awaraCartLineItems.subList(i, awaraLineItemsSize));

            DummyOrder dummyGgnOrder = new DummyOrder(ggnCartLineItems, ggnWarehouse, isCod, pincode, payment);
            DummyOrder dummyMumOrder = new DummyOrder(mumCartLineItems, mumWarehouse, isCod, pincode, payment);

            List<DummyOrder> splitDummyOrders = Arrays.asList(dummyGgnOrder, dummyMumOrder);

            if (isCod) {
                if (!validCase(splitDummyOrders))
                    continue;
            }

            dummyOrderCostingMap.put(splitDummyOrders, orderSplitterHelper.calculateShippingPlusTax(splitDummyOrders));

            List<CartLineItem> ggnCartLineItems2 = new ArrayList<CartLineItem>();
            ggnCartLineItems2.addAll(ggnKiCartLineItems);
            ggnCartLineItems2.addAll(awaraCartLineItems.subList(i, awaraLineItemsSize));

            List<CartLineItem> mumCartLineItems2 = new ArrayList<CartLineItem>();
            mumCartLineItems2.addAll(mumKiCartLineItems);
            mumCartLineItems2.addAll(awaraCartLineItems.subList(0, i));
            DummyOrder dummyGgnOrder2 = new DummyOrder(ggnCartLineItems2, ggnWarehouse, isCod, pincode, payment);
            DummyOrder dummyMumOrder2 = new DummyOrder(mumCartLineItems2, mumWarehouse, isCod, pincode, payment);

            List<DummyOrder> splitDummyOrders2 = Arrays.asList(dummyGgnOrder2, dummyMumOrder2);
            if (isCod) {
                if (!validCase(splitDummyOrders2))
                    continue;
            }
            dummyOrderCostingMap.put(splitDummyOrders2, orderSplitterHelper.calculateShippingPlusTax(splitDummyOrders2));
        }

        if (dummyOrderCostingMap.size() == 0) {
            String comments = "System could not split the order, Most probably due to one item being less than min cod limit Rs 50";
            orderLoggingService.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
            throw new OrderSplitException(comments + ". Aborting splitting of order.", order);
        }

        MapValueComparator mapValueComparator = new MapValueComparator(dummyOrderCostingMap);
        TreeMap<List<DummyOrder>, Long> sortedCourierCostingTreeMap = new TreeMap(mapValueComparator);
        sortedCourierCostingTreeMap.putAll(dummyOrderCostingMap);

        return sortedCourierCostingTreeMap;
    }

    public TreeMap<List<DummyOrder>, Long> splitBOPractically(Order order) {

        // get static things
        Pincode pincode = order.getAddress().getPincode();
        if (pincode == null) {
            String comments = "Pincode does not exist in our system, Please get in touch with OPS or customer care";
            orderLoggingService.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
            throw new OrderSplitException(comments + ". Aborting splitting of order.", order);
        }
        if (order.getPayment() == null) {
            String comments = "No Payment Associated with order";
            orderLoggingService.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
            throw new OrderSplitException(comments + ". Aborting splitting of order.", order);
        }
        boolean isCod = order.isCOD();
        Payment payment = order.getPayment();

        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        Map<CartLineItem, Set<Warehouse>> cartLineItemWarehouseListMap = new HashMap<CartLineItem, Set<Warehouse>>();

        // iterate over product line items, make a map for the sku available (i.e having inventory as well) for each
        // warehouse
        for (CartLineItem cartLineItem : productCartLineItems) {
            //List<Sku> skuList = skuService.getSKUsForProductVariant(cartLineItem.getProductVariant());
	        List<Sku> skuList = skuService.getSKUsForProductVariantAtServiceableWarehouses(cartLineItem.getProductVariant());
            Set<Warehouse> applicableWarehousesForLineItem = null;
            if (!skuList.isEmpty()) {
                applicableWarehousesForLineItem = new HashSet<Warehouse>();
                for (Sku sku : skuList) {
                    if (inventoryService.getAvailableUnbookedInventory(sku, cartLineItem.getMarkedPrice()) > 0) {
                        applicableWarehousesForLineItem.add(sku.getWarehouse());
                    }
                }
                cartLineItemWarehouseListMap.put(cartLineItem, applicableWarehousesForLineItem);
            }
        }

        Warehouse ggnWarehouse = warehouseService.getDefaultWarehouse();
        Warehouse mumWarehouse = warehouseService.getMumbaiWarehouse();

        List<CartLineItem> awaraCartLineItems = new ArrayList<CartLineItem>(); // these are the dicey lineItems for
                                                                                // which whole algo has been written
        List<CartLineItem> ggnKiCartLineItems = new ArrayList<CartLineItem>();
        List<CartLineItem> mumKiCartLineItems = new ArrayList<CartLineItem>();

        for (CartLineItem cartLineItem : cartLineItemWarehouseListMap.keySet()) {
            Set<Warehouse> applicableWarehouses = cartLineItemWarehouseListMap.get(cartLineItem);
            Integer applicableWarehousesSize = applicableWarehouses != null ? applicableWarehouses.size() : 0;
            if (applicableWarehousesSize == 0) {
                ggnKiCartLineItems.add(cartLineItem);
            } else if (applicableWarehousesSize == 1) {
                Warehouse applicableWarehouse = applicableWarehouses.iterator().next();
                if (applicableWarehouse.equals(ggnWarehouse)) {
                    ggnKiCartLineItems.add(cartLineItem);
                } else if (applicableWarehouse.equals(mumWarehouse)) {
                    mumKiCartLineItems.add(cartLineItem);
                }
            } else {
                awaraCartLineItems.add(cartLineItem);
            }
        }

        Integer awaraLineItemsSize = awaraCartLineItems.size();
        Map<List<DummyOrder>, Long> dummyOrderCostingMap = new HashMap<List<DummyOrder>, Long>();

        // now what happens here is ki awara LineItems ko P&C karte hain, and allocate them in ggn/Mumbai, and then
        // (shipping+cost) decide which is the best fit for them
        for (int i = 0; i <= awaraLineItemsSize; i++) {
            List<CartLineItem> ggnCartLineItems = new ArrayList<CartLineItem>();
            ggnCartLineItems.addAll(ggnKiCartLineItems);
            ggnCartLineItems.addAll(awaraCartLineItems.subList(0, i));

            List<CartLineItem> mumCartLineItems = new ArrayList<CartLineItem>();
            mumCartLineItems.addAll(mumKiCartLineItems);
            mumCartLineItems.addAll(awaraCartLineItems.subList(i, awaraLineItemsSize));

            DummyOrder dummyGgnOrder = new DummyOrder(ggnCartLineItems, ggnWarehouse, isCod, pincode, payment);
            DummyOrder dummyMumOrder = new DummyOrder(mumCartLineItems, mumWarehouse, isCod, pincode, payment);

            List<DummyOrder> splitDummyOrders = Arrays.asList(dummyGgnOrder, dummyMumOrder);

            if (isCod) {
                if (!validCase(splitDummyOrders))
                    continue;
            }

            dummyOrderCostingMap.put(splitDummyOrders, orderSplitterHelper.calculateShippingPlusTax(splitDummyOrders));

            List<CartLineItem> ggnCartLineItems2 = new ArrayList<CartLineItem>();
            ggnCartLineItems2.addAll(ggnKiCartLineItems);
            ggnCartLineItems2.addAll(awaraCartLineItems.subList(i, awaraLineItemsSize));

            List<CartLineItem> mumCartLineItems2 = new ArrayList<CartLineItem>();
            mumCartLineItems2.addAll(mumKiCartLineItems);
            mumCartLineItems2.addAll(awaraCartLineItems.subList(0, i));
            DummyOrder dummyGgnOrder2 = new DummyOrder(ggnCartLineItems2, ggnWarehouse, isCod, pincode, payment);
            DummyOrder dummyMumOrder2 = new DummyOrder(mumCartLineItems2, mumWarehouse, isCod, pincode, payment);

            List<DummyOrder> splitDummyOrders2 = Arrays.asList(dummyGgnOrder2, dummyMumOrder2);
            if (isCod) {
                if (!validCase(splitDummyOrders2))
                    continue;
            }
            dummyOrderCostingMap.put(splitDummyOrders2, orderSplitterHelper.calculateShippingPlusTax(splitDummyOrders2));
        }

        if (dummyOrderCostingMap.size() == 0) {
            String comments = "System could not split the order, Please report to tech ";
            orderLoggingService.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
            throw new OrderSplitException(comments + ". Aborting splitting of order.", order);
        }

        MapValueComparator mapValueComparator = new MapValueComparator(dummyOrderCostingMap);
        TreeMap<List<DummyOrder>, Long> sortedCourierCostingTreeMap = new TreeMap(mapValueComparator);
        sortedCourierCostingTreeMap.putAll(dummyOrderCostingMap);

        return sortedCourierCostingTreeMap;
    }

    private boolean validCase(List<DummyOrder> splitDummyOrders) {
        boolean validCase = true;
        Double codMinAmountForSplitting = 50D; // so that orders get split
        for (DummyOrder splitDummyOrder : splitDummyOrders) {
            double amount = splitDummyOrder.getAmount();
            if (splitDummyOrder.getCartLineItemList().size() > 0 && amount > 0D && amount < codMinAmountForSplitting) {
                validCase = false;
            }
        }
        return validCase;
    }

    @SuppressWarnings("unchecked")
    public TreeMap<List<DummyOrder>, Long> splitBOIdeally(Order order, Warehouse ggnWarehouse, Warehouse mumWarehouse) {
        // get static things
        Pincode pincode = order.getAddress().getPincode();
        Payment payment = order.getPayment();
        if (pincode == null) {
            return null;
        }

        boolean cod = false;
        try {
            cod = order.isCOD();
        } catch (Exception e) {
            logger.info("Could not determine isCod for BO order " + order.getId());
        }

        List<CartLineItem> productCartLineItems = new ArrayList<CartLineItem>();
        productCartLineItems.addAll(new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter());

        if (productCartLineItems.size() > 5) {
            logger.info("Terminating " + order.getId());
            return null;
        }

        Map<List<DummyOrder>, Long> dummyOrderCostingMap = new HashMap<List<DummyOrder>, Long>();
        Integer productCartLineItemsSize = productCartLineItems.size();

        // now what happens here is ki productCartLineItems ko P&C karte hain, and allocate them in ggn/Mumbai, and then
        // (shipping+cost) decide which is the best fit for them
        for (int i = 0; i <= productCartLineItemsSize; i++) {
            DummyOrder dummyGgnOrder = new DummyOrder(productCartLineItems.subList(0, i), ggnWarehouse, cod, pincode, payment);
            DummyOrder dummyMumOrder = new DummyOrder(productCartLineItems.subList(i, productCartLineItemsSize), mumWarehouse, cod, pincode, payment);
            dummyOrderCostingMap.put(Arrays.asList(dummyGgnOrder, dummyMumOrder), orderSplitterHelper.calculateShippingPlusTax(Arrays.asList(dummyGgnOrder, dummyMumOrder)));

            DummyOrder dummyGgnOrder2 = new DummyOrder(productCartLineItems.subList(i, productCartLineItemsSize), ggnWarehouse, cod, pincode, payment);
            DummyOrder dummyMumOrder2 = new DummyOrder(productCartLineItems.subList(0, i), mumWarehouse, cod, pincode, payment);
            dummyOrderCostingMap.put(Arrays.asList(dummyGgnOrder2, dummyMumOrder2), orderSplitterHelper.calculateShippingPlusTax(Arrays.asList(dummyGgnOrder2, dummyMumOrder2)));
        }

        MapValueComparator mapValueComparator = new MapValueComparator(dummyOrderCostingMap);
        TreeMap<List<DummyOrder>, Long> sortedCourierCostingTreeMap = new TreeMap(mapValueComparator);
        sortedCourierCostingTreeMap.putAll(dummyOrderCostingMap);

        return sortedCourierCostingTreeMap;
    }

    public Map<Warehouse, Set<CartLineItem>> splitBOExcludingShippingTaxConsideration(Order order) {
        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        Map<CartLineItem, Set<Warehouse>> cartLineItemWarehouseListMap = new HashMap<CartLineItem, Set<Warehouse>>();

        for (CartLineItem lineItem : productCartLineItems) {
            List<Sku> skuList = new ArrayList<Sku>();
            //skuList = skuService.getSKUsForProductVariant(lineItem.getProductVariant());
	        skuList = skuService.getSKUsForProductVariantAtServiceableWarehouses(lineItem.getProductVariant());
            Set<Warehouse> applicableWarehousesForLineItem = null;
            if (!skuList.isEmpty()) {
                applicableWarehousesForLineItem = new HashSet<Warehouse>();
                for (Sku sku : skuList) {
                    if (inventoryService.getAvailableUnbookedInventory(sku, lineItem.getMarkedPrice()) > 0) {
                        applicableWarehousesForLineItem.add(sku.getWarehouse());
                    }
                }
                /*
                 * Can be uncommented if daddy doesn't agree if (applicableWarehousesForLineItem.isEmpty()) { String
                 * comments = "Did not get the required qty in any of the warehouse as none had the right amount of net
                 * inventory to serve the order, one of the sku being " +
                 * lineItem.getProductVariant().getProduct().getName(); logOrderActivityByAdmin(order,
                 * EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments); throw new OrderSplitException("Didn't
                 * get inventory for sku. Aborting splitting of order.", order); }
                 */
            } else {
                String comments = "No Sku has been created for " + lineItem.getProductVariant().getProduct().getName();
                orderLoggingService.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
                throw new OrderSplitException("Didn't get sku for few variants. Aborting splitting of order.", order);
            }
            cartLineItemWarehouseListMap.put(lineItem, applicableWarehousesForLineItem);
        }

        // phase 1 complete

        Map<Warehouse, Set<CartLineItem>> warehouseLineItemSetMap = new HashMap<Warehouse, Set<CartLineItem>>();

        for (CartLineItem cartLineItem : cartLineItemWarehouseListMap.keySet()) {
            Warehouse warehouse = warehouseService.getWarehouseToBeAssignedByDefinedLogicForSplitting(cartLineItemWarehouseListMap.get(cartLineItem));

            if (warehouseLineItemSetMap.containsKey(warehouse)) {
                Set<CartLineItem> cartLineItems = warehouseLineItemSetMap.get(warehouse);
                if (cartLineItems != null) {
                    cartLineItems.add(cartLineItem);
                }
                warehouseLineItemSetMap.put(warehouse, cartLineItems);
            } else {
                Set<CartLineItem> cartLineItems = new HashSet<CartLineItem>();
                cartLineItems.add(cartLineItem);
                warehouseLineItemSetMap.put(warehouse, cartLineItems);
            }
        }

        // phase 2 complete

        // COD Amount Check
        if (order.getPayment().getPaymentMode().getId().equals(EnumPaymentMode.COD.getId())) {
            for (Map.Entry<Warehouse, Set<CartLineItem>> warehouseSetEntry : warehouseLineItemSetMap.entrySet()) {
                ShippingOrder shippingOrder = shippingOrderService.createSOWithBasicDetails(order, warehouseSetEntry.getKey());
                for (CartLineItem cartLineItem : warehouseSetEntry.getValue()) {
                    Sku sku = skuService.getSKU(cartLineItem.getProductVariant(), warehouseSetEntry.getKey());
                    LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
                    shippingOrder.getLineItems().add(shippingOrderLineItem);
                }
                ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, order);
                if (ShippingOrderHelper.getAmountForSO(shippingOrder) <= codMinAmount) {
                    String comments = "One of the SO amount was computed below " + codMinAmount;
                    orderLoggingService.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
                    throw new OrderSplitException(comments + ". Aborting splitting of order.", order);
                }
            }
        }
        return warehouseLineItemSetMap;
    }



  

}