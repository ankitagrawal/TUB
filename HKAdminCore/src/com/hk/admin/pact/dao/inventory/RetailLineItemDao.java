package com.hk.admin.pact.dao.inventory;

import com.hk.domain.RetailLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.BaseDao;

public interface RetailLineItemDao extends BaseDao {

    public RetailLineItem getRetailLineItem(LineItem lineItem);
}
