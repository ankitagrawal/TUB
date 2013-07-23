package com.hk.admin.impl.dao.order;

import com.hk.admin.pact.dao.order.AdminOrderDao;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 9/18/12
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class AdminOrderDaoImpl extends BaseDaoImpl implements AdminOrderDao {
	public List<Order> getOrdersByGatewayOrderList(List<String> gatewayOrderIdList) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
		criteria.add(Restrictions.in("gatewayOrderId", gatewayOrderIdList));
		return findByCriteria(criteria);
	}

  @Override
  public List<Order> findSaleForTimeFrame(Long storeId, Date startDate, Date endDate, List<OrderStatus> orderStatusList) {

    DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
    criteria.add(Restrictions.eq("store.id", storeId));
    if (startDate != null)
      criteria.add(Restrictions.ge("createDate", startDate));
    if (endDate != null)
      criteria.add(Restrictions.le("createDate", endDate));
    if (orderStatusList != null)
      criteria.add(Restrictions.in("orderStatus", orderStatusList));
    //   criteria.createAlias("shippingOrders", "SO");
    //   criteria.createAlias("SO.shippingOrderStatus", "SOStatus");
    //criteria.createAlias("payment", "pmt");
    //criteria.createAlias("pmt.paymentMode", "paymentMode");
    return findByCriteria(criteria);
  }

}
