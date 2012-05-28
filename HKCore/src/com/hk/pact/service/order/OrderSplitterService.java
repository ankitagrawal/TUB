package com.hk.pact.service.order;

import com.hk.pojo.DummyOrder;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;

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