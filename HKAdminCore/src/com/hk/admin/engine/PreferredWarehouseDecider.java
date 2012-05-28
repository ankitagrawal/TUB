package com.hk.admin.engine;

import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.admin.pact.dao.courier.PincodeRegionZoneDao;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.comparator.MapValueComparator;
import com.hk.constants.core.EnumTax;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.PincodeRegionZone;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 5/26/12
 * Time: 1:53 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS})
public class PreferredWarehouseDecider {

    @Autowired
    CourierPricingEngineDao courierPricingEngineDao;

    @Autowired
    PincodeRegionZoneDao pincodeRegionZoneDao;

    @Autowired
    CourierService courierService;

    @Autowired
    SkuService skuService;

    @Autowired
    ShippingOrderService shippingOrderService;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    PincodeDao pincodeDao;

    @Autowired
    CourierGroupService courierGroupService;

    @Autowired
    ShipmentPricingEngine shipmentPricingEngine;

    @Autowired
    CourierCostCalculator courierCostCalculator;

    private static Logger logger = LoggerFactory.getLogger(PreferredWarehouseDecider.class);

    //todo rewrite generic
    public Warehouse getPreferredWareHouse(LineItem lineItem) {

        // get static things
        ShippingOrder shippingOrder = lineItem.getShippingOrder();
        Order order = shippingOrder.getBaseOrder();
        Pincode pincode = pincodeDao.getByPincode(order.getAddress().getPin());
        boolean isCod = order.isCOD();

        ProductVariant productVariant = lineItem.getSku().getProductVariant();
        Double weight = lineItem.getSku().getProductVariant().getWeight() * lineItem.getQty();
        Double amount = (lineItem.getHkPrice() * lineItem.getQty()) - lineItem.getDiscountOnHkPrice() - lineItem.getOrderLevelDiscount() - lineItem.getRewardPoints();
        Map<Warehouse, Double> warehouseCheapestCourierCostingMap = new HashMap<Warehouse, Double>();

        for (Warehouse warehouse : warehouseService.getAllWarehouses()) {
            Sku sku = skuService.getSKU(productVariant, warehouse);
            Double taxIncurred = productVariant.getCostPrice() * sku.getTax().getValue();
            List<Courier> applicableCouriers = courierService.getAvailableCouriers(pincode.getPincode(), isCod);
            Double totalCost = 0D;
            Map<Courier, Double> courierCostingMap = new HashMap<Courier, Double>();

            List<PincodeRegionZone> sortedApplicableZoneList = pincodeRegionZoneDao.getSortedRegionList(applicableCouriers, pincode, warehouse);
            for (PincodeRegionZone pincodeRegionZone : sortedApplicableZoneList) {
                Set<Courier> couriers = courierGroupService.getCommonCouriers(pincodeRegionZone.getCourierGroup(), applicableCouriers);
                for (Courier courier : couriers) {
                    CourierPricingEngine courierPricingInfo = courierPricingEngineDao.getCourierPricingInfo(courier, pincodeRegionZone.getRegionType(), warehouse);
                    totalCost = taxIncurred + shipmentPricingEngine.calculateShipmentCost(courierPricingInfo, weight) + shipmentPricingEngine.calculateReconciliationCost(courierPricingInfo, amount, isCod);
                    courierCostingMap.put(courier, totalCost);
                }
            }

            MapValueComparator mapValueComparator = new MapValueComparator(courierCostingMap);
            TreeMap<Courier, Double> sortedCourierCostingTreeMap = new TreeMap(mapValueComparator);
            sortedCourierCostingTreeMap.putAll(courierCostingMap);

            warehouseCheapestCourierCostingMap.put(warehouse, sortedCourierCostingTreeMap.lastEntry().getValue());
        }

        MapValueComparator mapValueComparator = new MapValueComparator(warehouseCheapestCourierCostingMap);
        TreeMap<Warehouse, Double> sortedWarehouseCheapestCourierCostingMap = new TreeMap(mapValueComparator);
        sortedWarehouseCheapestCourierCostingMap.putAll(warehouseCheapestCourierCostingMap);

        return sortedWarehouseCheapestCourierCostingMap.lastEntry().getKey();
    }

    public Map<Warehouse, Map<Courier, Long>> getPreferredWareHouse(List<ProductVariant> productVariants, boolean isCod, String pincode) {
        Pincode pincodeObj = pincodeDao.getByPincode(pincode);
        Map<Warehouse, Map<Courier, Long>> warehouseCheapestCourierCostingMap = new HashMap<Warehouse, Map<Courier, Long>>();

        for (Warehouse warehouse : warehouseService.getServiceableWarehouses()) {
            Double weight = 0D;
            Double amount = 0D;
            Double taxIncurred = 0D;
            for (ProductVariant productVariant : productVariants) {
                amount += productVariant.getHkPrice();
                Double wt = productVariant.getWeight();
                if (wt == null || wt == 0D) {
                    weight += 125D;
                } else {
                    weight += wt;
                }
                Sku sku;
                try {
                    sku = skuService.getSKU(productVariant, warehouse);
                } catch (NoSkuException e) {
                    sku = skuService.getSKU(productVariant, warehouseService.getWarehoueForFlipping(warehouse));
                } catch (Exception e) {
                    sku = null;
                }
                if (sku != null) {
                    taxIncurred += productVariant.getCostPrice() * sku.getTax().getValue();
                }
            }

            Map<Courier, Long> courierCostingMap = courierCostCalculator.getCourierCostingMap(pincode, isCod, warehouse, amount, weight);

            //TODO add taxIncurred in all values of map

            warehouseCheapestCourierCostingMap.put(warehouse, courierCostingMap);
        }
        return warehouseCheapestCourierCostingMap;
    }
}