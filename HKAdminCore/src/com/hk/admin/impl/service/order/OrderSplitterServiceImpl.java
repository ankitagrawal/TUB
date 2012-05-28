package com.hk.admin.impl.service.order;

import com.hk.admin.dto.order.DummyOrder;
import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.dao.courier.PincodeRegionZoneDao;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.order.OrderSplitterService;
import com.hk.admin.util.helper.OrderSplitterHelper;
import com.hk.comparator.MapValueComparator;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.core.Pincode;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/25/12
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class OrderSplitterServiceImpl implements OrderSplitterService {

    private static Logger logger = LoggerFactory.getLogger(OrderSplitterServiceImpl.class);

    @Autowired
    CourierPricingEngineDao courierPricingEngineDao;

    @Autowired
    PincodeRegionZoneDao pincodeRegionZoneDao;

    @Autowired
    CourierServiceInfoDao courierServiceInfoDao;

    @Autowired
    SkuService skuService;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    PincodeDao pincodeDao;

    @Autowired
    ShipmentPricingEngine shipmentPricingEngine;

    @Autowired
    CourierGroupService courierGroupService;

    @Autowired
    OrderSplitterHelper orderSplitterHelper;

    public List<DummyOrder> listBestDummyOrdersPractically(Order order) {
        TreeMap<List<DummyOrder>, Long> sortedCourierCostingTreeMap = splitBOPractically(order);
        return sortedCourierCostingTreeMap.lastKey();
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

    public TreeMap<List<DummyOrder>, Long> splitBOPractically(Order order) {

        // get static things
        Pincode pincode = pincodeDao.getByPincode(order.getAddress().getPin());
        boolean isCod = order.isCOD();
        Payment payment = order.getPayment();

        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        Map<CartLineItem, Set<Warehouse>> cartLineItemWarehouseListMap = new HashMap<CartLineItem, Set<Warehouse>>();

        // iterate over product line items, make a map for the sku available (i.e having inventory as well) for each warehouse
        for (CartLineItem cartLineItem : productCartLineItems) {
            List<Sku> skuList = skuService.getSKUsForProductVariant(cartLineItem.getProductVariant());

            Set<Warehouse> applicableWarehousesForLineItem = null;
            if (!skuList.isEmpty()) {
                applicableWarehousesForLineItem = new HashSet<Warehouse>();
                for (Sku sku : skuList) {
                    if (inventoryService.getAvailableUnbookedInventory(sku) > 0) {
                        applicableWarehousesForLineItem.add(sku.getWarehouse());
                    }
                }
                cartLineItemWarehouseListMap.put(cartLineItem, applicableWarehousesForLineItem);
            }
        }

        Warehouse ggnWarehouse = warehouseService.getDefaultWarehouse();
        Warehouse mumWarehouse = warehouseService.getMumbaiWarehouse();

        List<CartLineItem> awaraCartLineItems = new ArrayList<CartLineItem>();         // these are the dicey lineItems for which whole algo has been written
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

        //now what happens here is ki awara LineItems ko P&C karte hain, and allocate them in ggn/Mumbai, and then (shipping+cost) decide which is the best fit for them
        for (int i = 0; i <= awaraLineItemsSize; i++) {
            List<CartLineItem> ggnCartLineItems = new ArrayList<CartLineItem>();
            ggnCartLineItems.addAll(ggnKiCartLineItems);
            ggnCartLineItems.addAll(awaraCartLineItems.subList(0, i));

            List<CartLineItem> mumCartLineItems = new ArrayList<CartLineItem>();
            mumCartLineItems.addAll(mumKiCartLineItems);
            mumCartLineItems.addAll(awaraCartLineItems.subList(i, awaraLineItemsSize));

            DummyOrder dummyGgnOrder = new DummyOrder(ggnCartLineItems, ggnWarehouse, isCod, pincode, payment);
            DummyOrder dummyMumOrder = new DummyOrder(mumCartLineItems, mumWarehouse, isCod, pincode, payment);
            dummyOrderCostingMap.put(Arrays.asList(dummyGgnOrder, dummyMumOrder), orderSplitterHelper.calculateShippingPlusTax(Arrays.asList(dummyGgnOrder, dummyMumOrder)));
        }

        MapValueComparator mapValueComparator = new MapValueComparator(dummyOrderCostingMap);
        TreeMap<List<DummyOrder>, Long> sortedCourierCostingTreeMap = new TreeMap(mapValueComparator);
        sortedCourierCostingTreeMap.putAll(dummyOrderCostingMap);

        return sortedCourierCostingTreeMap;
    }


    public TreeMap<List<DummyOrder>, Long> splitBOIdeally(Order order, Warehouse ggnWarehouse, Warehouse mumWarehouse) {
        // get static things
        Pincode pincode = pincodeDao.getByPincode(order.getAddress().getPin());
        Payment payment = order.getPayment();
        if (pincode == null) {
            logger.info("illegal pincode entry for pincode " + order.getAddress().getPin() + " for " + order.getId());
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

        //now what happens here is ki productCartLineItems ko P&C karte hain, and allocate them in ggn/Mumbai, and then (shipping+cost) decide which is the best fit for them
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

}