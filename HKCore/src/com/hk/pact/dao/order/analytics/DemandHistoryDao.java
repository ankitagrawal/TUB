package com.hk.pact.dao.order.analytics;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 7/13/12
 * Time: 12:16 AM
 * To change this template use File | Settings | File Templates.
 */

import java.util.Date;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.analytics.DemandHistory;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;


public interface DemandHistoryDao extends BaseDao {

    public DemandHistory createOrUpdateEntry(CartLineItem cartLineItem, Warehouse warehouse, Order order);

    public DemandHistory findByPVWarehouse(ProductVariant productVariant, Warehouse warehouse, Date currentDate);


}

