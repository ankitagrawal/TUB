package com.hk.impl.dao.pos;

import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.pos.POSDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class POSDaoImpl extends BaseDaoImpl implements POSDao {

  @Autowired
  private BaseDao baseDao;

  @Override
  public List<Order> findSaleForTimeFrame(Long storeId, Date startDate, Date endDate, List<OrderStatus> orderStatusList) {

    DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
    criteria.add(Restrictions.eq("store.id", storeId));
    if (startDate != null)
      criteria.add(Restrictions.gt("createDate", startDate));
    if (endDate != null)
      criteria.add(Restrictions.lt("createDate", endDate));
    if (orderStatusList != null)
      criteria.add(Restrictions.in("orderStatus", orderStatusList));
    criteria.createAlias("shippingOrders", "SO");
    criteria.createAlias("SO.shippingOrderStatus", "SOStatus");
    criteria.createAlias("payment", "pmt");
    criteria.createAlias("pmt.paymentMode", "paymentMode");
    return findByCriteria(criteria);
  }

  public List<ReverseOrder> findReturnItemForTimeFrame(Long warehouseId, Date startDate, Date endDate) {
    String hqlQuery = "select ro from ReverseOrder ro join ro.shippingOrder so join so.warehouse w join w.store st where st.id = :warehouseId and ro.createDate between :startDate and :endDate";
    return getSession().createQuery(hqlQuery).setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("warehouseId", warehouseId).list();

/*    DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
    criteria.add(Restrictions.eq("store.id", storeId));
    criteria.createAlias("shippingOrders", "SO");
    criteria.createAlias("SO.shippingOrderStatus", "SOStatus");
    if (startDate != null)
      criteria.add(Restrictions.gt("createDate", startDate));
    if (endDate != null)
      criteria.add(Restrictions.lt("createDate", endDate));
    if (SOStatusList != null)
      criteria.add(Restrictions.in("SO.shippingOrderStatus", SOStatusList));

   // criteria.setResultTransformer(Transformers.aliasToBean(POSReturnItemDto.class));
    return this.findByCriteria(criteria);*/

    // return t;
  }

}
