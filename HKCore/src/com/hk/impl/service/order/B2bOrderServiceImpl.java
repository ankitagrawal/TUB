package com.hk.impl.service.order;

import com.hk.domain.order.B2bOrderChecklist;
import com.hk.domain.order.Order;
import com.hk.pact.dao.user.B2bOrderDao;
import com.hk.pact.service.order.B2BOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class B2bOrderServiceImpl implements B2BOrderService {

  @Autowired
  B2bOrderDao b2bOrderDao;

  @Override
  public boolean checkCForm(Order order) {
    if (order != null && order.isB2bOrder() == true) {
      if (getB2bOrderDao().getB2bOrderChecklist(order) != null)
        return true;
    }
    return false;
  }

  @Override
  public void saveB2BOrder(B2bOrderChecklist checkList) {
    getB2bOrderDao().save(checkList);
  }


  public B2bOrderDao getB2bOrderDao() {
    return b2bOrderDao;
  }

  public void setB2bOrderDao(B2bOrderDao b2bOrderDao) {
    this.b2bOrderDao = b2bOrderDao;
  }
}