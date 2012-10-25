package com.hk.pact.dao.shippingOrder;

import com.hk.domain.order.ReplacementOrder;
import com.hk.pact.dao.BaseDao;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tarun
 * Date: Oct 3, 2012
 * Time: 12:44:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReplacementOrderDao extends BaseDao{
	public List<ReplacementOrder> getReplacementOrderFromShippingOrder(Long refShippingOrderId);
}
