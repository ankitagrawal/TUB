package com.hk.impl.dao.shippingOrder;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.domain.order.ReplacementOrder;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.shippingOrder.ReplacementOrderDao;

/**
 * Created by IntelliJ IDEA.
 * User: Tarun
 * Date: Oct 3, 2012
 * Time: 12:46:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
@SuppressWarnings("unchecked")
public class ReplacementOrderDaoImpl extends BaseDaoImpl implements ReplacementOrderDao{
	@Override
	public List<ReplacementOrder> getReplacementOrderFromShippingOrder(Long refShippingOrderId) {
		String query = "from ReplacementOrder ro where ro.refShippingOrder.id = :refShippingOrderId";
		return (List<ReplacementOrder>) findByNamedParams(query, new String[]{"refShippingOrderId"},new Object[]{refShippingOrderId});
	}
}
