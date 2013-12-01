package com.hk.pact.service.order;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pojo.DummyOrder;


/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/25/12
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OrderSplitterService {

   public List<DummyOrder> listBestDummyOrdersPractically(Order order);

    public List<DummyOrder> listBestDummyOrdersPractically(Order order, Set<CartLineItem> cartlineitems);

    public NavigableMap<List<DummyOrder>, Long> listSortedDummyOrderMapPractically(Order order);

    public Map.Entry<List<DummyOrder>, Long> listFirstMapEntryPractically(Order order);

    public Map<List<DummyOrder>, Long> listAllDummyOrdersMap(Order order);

    public List<DummyOrder> listBestDummyOrdersIdeally(Order order, Warehouse ggnWarehouse, Warehouse mumWarehouse);

    public TreeMap<List<DummyOrder>, Long> splitBOPractically(Order order);

    public TreeMap<List<DummyOrder>, Long> splitBOIdeally(Order order, Warehouse ggnWarehouse, Warehouse mumWarehouse);

    public Map<Warehouse,Set<CartLineItem>> splitBOExcludingShippingTaxConsideration(Order order);

  
    
}