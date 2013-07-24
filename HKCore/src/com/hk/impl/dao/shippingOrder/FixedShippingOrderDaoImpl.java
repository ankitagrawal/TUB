package com.hk.impl.dao.shippingOrder;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang.StringUtils;

import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.shippingOrder.FixedShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.shippingOrder.ShippingOrderStatusDao;
import com.hk.pact.dao.shippingOrder.FixedShippingOrderDao;
import com.akube.framework.dao.Page;

@Repository
public class FixedShippingOrderDaoImpl extends BaseDaoImpl implements FixedShippingOrderDao {

  @Override
  public Page getFixedShippingOrder(Warehouse warehouse, String status, int pageNo, int perPage) {
    DetachedCriteria criteria = DetachedCriteria.forClass(FixedShippingOrder.class);
    if (warehouse != null) {
      DetachedCriteria whCriteria = criteria.createCriteria("shippingOrder");
      whCriteria.add(Restrictions.eq("warehouse", warehouse));
    }
    if (StringUtils.isNotBlank(status)) {
      criteria.add(Restrictions.eq("status", status));
    }
    criteria.addOrder(org.hibernate.criterion.Order.desc("createDate"));
    return list(criteria, pageNo, perPage);
  }
}