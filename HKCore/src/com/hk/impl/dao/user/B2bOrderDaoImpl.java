package com.hk.impl.dao.user;

import com.hk.domain.order.B2bOrderChecklist;
import com.hk.domain.order.Order;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.B2bOrderDao;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 26 Mar, 2013
 * Time: 12:21:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class B2bOrderDaoImpl extends BaseDaoImpl implements B2bOrderDao {
  @Override
  public B2bOrderChecklist getB2bOrderChecklist(Order order) {
    String queryString = "from B2bOrderChecklist bo where bo.baseOrder=:baseOrder ";
    return (B2bOrderChecklist) findUniqueByNamedParams(queryString, new String[]{"baseOrder"}, new Object[]{order});
  }


}
