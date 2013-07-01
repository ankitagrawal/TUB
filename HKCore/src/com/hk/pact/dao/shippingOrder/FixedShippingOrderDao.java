package com.hk.pact.dao.shippingOrder;

import com.akube.framework.dao.Page;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface FixedShippingOrderDao extends BaseDao {

    public Page getFixedShippingOrder(Warehouse warehouse, String status, int pageNo, int perPage);

}