package com.hk.admin.pact.service.order;

import com.hk.admin.dto.order.DummyOrder;
import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.dao.courier.PincodeRegionZoneDao;
import com.hk.admin.pact.service.courier.CourierGroupService;
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

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/25/12
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OrderSplitterService {

    public List<DummyOrder> listBestDummyOrdersPractically(Order order);

    public Map.Entry<List<DummyOrder>, Long> listFirstMapEntryPractically(Order order);

    public Map<List<DummyOrder>, Long> listAllDummyOrdersMap(Order order);

    public List<DummyOrder> listBestDummyOrdersIdeally(Order order, Warehouse ggnWarehouse, Warehouse mumWarehouse);

    public TreeMap<List<DummyOrder>, Long> splitBOPractically(Order order);

    public TreeMap<List<DummyOrder>, Long> splitBOIdeally(Order order, Warehouse ggnWarehouse, Warehouse mumWarehouse);

}