package com.hk.admin.util.helper;

import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.service.courier.CourierCostCalculator;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.sku.Sku;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pojo.DummyOrder;
import com.hk.pojo.DummySO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/25/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class OrderSplitterHelper {

    private static Logger logger = LoggerFactory.getLogger(OrderSplitterHelper.class);

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    ShipmentPricingEngine shipmentPricingEngine;

    @Autowired
    CourierCostCalculator courierCostCalculator;

    @Autowired
    SkuService skuService;

    public Long calculateShippingPlusTax(List<DummyOrder> dummyOrders) {
        Double totalTax = calculateTaxIncurred(dummyOrders);
        List<DummySO> dummySOs = getSOsForShippingCost(dummyOrders);
        Double shipmentCost = 0D;
        for (DummySO dummySO : dummySOs) {
            shipmentCost += dummySO.getShipmentCost();
        }
        return (shipmentCost.longValue() + totalTax.longValue());     //check better way
    }

    private Double calculateTaxIncurred(List<DummyOrder> dummyOrdersList) {
        Double taxIncurred = 0D;
        for (DummyOrder dummyOrder : dummyOrdersList) {
            for (CartLineItem cartLineItem : dummyOrder.getCartLineItemList()) {
                ProductVariant productVariant = cartLineItem.getProductVariant();
                Warehouse warehouse = dummyOrder.getWarehouse();
                Sku sku = null;
                try {
                    sku = skuService.getSKU(productVariant, warehouse);
                } catch (NoSkuException e) {
                	logger.debug("no sku exists for variant" + productVariant.getId());
                } catch (Exception e) {
                    logger.debug("no sku exists for variant" + productVariant.getId());
                }
                if (sku != null) {
	                Double netHkPriceForVariant = cartLineItem.getHkPrice() * cartLineItem.getQty() - cartLineItem.getDiscountOnHkPrice();
                    taxIncurred += netHkPriceForVariant * sku.getTax().getValue();
                }
            }
            dummyOrder.setTaxIncurred(taxIncurred);
        }
        return taxIncurred;
    }

    private List<DummySO> getSOsForShippingCost(List<DummyOrder> dummyOrderList) {
        List<DummySO> dummySOs = new ArrayList<DummySO>();
        for (DummyOrder dummyOrder : dummyOrderList) {
            if (dummyOrder.getCartLineItemList().size() > 0) {
                DummySO dummySO = new DummySO(dummyOrder);
                dummyOrder.setDummySO(dummySO);
                Map.Entry<Courier, Long> cheapestEntry = calculateCheapestShipmentPlusReconciliationCost(dummySO);
                if (cheapestEntry != null) {
                    dummySO.setCourier(cheapestEntry.getKey());
                    dummySO.setShipmentCost(cheapestEntry.getValue());
                    dummySOs.add(dummySO);
                }
            }
        }
        return dummySOs;
    }

    public Map.Entry<Courier, Long> calculateCheapestShipmentPlusReconciliationCost(DummySO dummySO) {
        return getAllShipmentPlusReconciliationCost(dummySO).lastEntry();
    }

    public TreeMap<Courier, Long> getAllShipmentPlusReconciliationCost(DummySO dummySO) {
        DummyOrder dummyOrder = dummySO.getDummyOrder();
        return courierCostCalculator.getCourierCostingMap(dummyOrder.getPincode().getPincode(), dummyOrder.isCod(),
            dummyOrder.getWarehouse(), dummyOrder.getAmount(), dummyOrder.getWeight(), dummyOrder.isGround(),null);
    }


   /* public Double calculateReconciliationCost(CourierPricingEngine courierPricingEngine, DummyOrder dummyOrder) {
        return shipmentPricingEngine.calculateReconciliationCost(courierPricingEngine, dummyOrder.getPayment(), dummyOrder.getAmount());
    }*/


}
