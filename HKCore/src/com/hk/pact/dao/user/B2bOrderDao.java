package com.hk.pact.dao.user;

import com.hk.domain.order.B2BOrderChecklist;
import com.hk.domain.order.Order;
import com.hk.pact.dao.BaseDao;

public interface B2bOrderDao extends BaseDao {

  public B2BOrderChecklist getB2bOrderChecklist(Order order);
  
  public B2BOrderChecklist saveB2BOrderChecklist(B2BOrderChecklist b2bOrderChecklist);

}