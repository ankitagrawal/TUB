package com.hk.impl.dao.pos;

import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.Order;
import com.hk.dto.pos.POSReturnItemDto;
import com.hk.dto.pos.POSSalesDto;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.pos.POSDao;
import org.hibernate.FetchMode;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nitin Kumar
 * Date: 7/18/13
 * Time: 9:44 PM
 * To change this template use File | Settings | File Templates.
 */
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
    criteria.createAlias("payment", "pmt");
    criteria.createAlias("pmt.paymentMode", "paymentMode");
   // criteria.createAlias("cartLineItems", "LineItems");
/*    ProjectionList proList = Projections.projectionList();
    proList.add(Projections.property("amount"), "amount");
    proList.add(Projections.property("cartLineItems"),"cartLineItems");
    proList.add(Projections.property("paymentMode.name"), "paymentMode");
    proList.add(Projections.property("id"), "orderNo");*/

    // proList.add(Projections.sum("cartLineItems.discountOnHkPrice").as("discount"));
 //   criteria.setProjection(proList);
  //  criteria.setResultTransformer(Transformers.aliasToBean(POSReturnItemDto.class));
   // List<POSSalesDto> t = findByCriteria(criteria);
    return findByCriteria(criteria);


  }


  public List<Order> findReturnItemForTimeFrame(Long storeId, Date startDate, Date endDate, List<OrderStatus> orderStatusList) {

    DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
    criteria.add(Restrictions.eq("store.id", storeId));
    if (startDate != null)
      criteria.add(Restrictions.gt("createDate", startDate));
    if (endDate != null)
      criteria.add(Restrictions.lt("createDate", endDate));
    if (orderStatusList != null)
      criteria.add(Restrictions.in("orderStatus", orderStatusList));
    ProjectionList proList = Projections.projectionList();
    proList.add(Projections.alias(Projections.property("amount"), "amount"));
    criteria.setProjection(proList);
    criteria.setResultTransformer(Transformers.aliasToBean(POSReturnItemDto.class));
    return this.findByCriteria(criteria);

   // return t;
  }
}
