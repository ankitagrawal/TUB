package com.hk.pact.dao.user;

import com.hk.domain.order.B2bOrderChecklist;
import com.hk.domain.order.Order;
import com.hk.pact.dao.BaseDao;

public interface B2bOrderDao extends BaseDao {

  public B2bOrderChecklist getB2bOrderChecklist(Order order);

}