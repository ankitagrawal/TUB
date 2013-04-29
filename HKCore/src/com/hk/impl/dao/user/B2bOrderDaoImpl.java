package com.hk.impl.dao.user;

import com.hk.domain.order.B2BOrderChecklist;
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
  public B2BOrderChecklist getB2bOrderChecklist(Order order) {
    String queryString = "from B2BOrderChecklist bo where bo.baseOrder=:order ";
    return (B2BOrderChecklist) findUniqueByNamedParams(queryString, new String[]{"order"}, new Object[]{order});
  }

@Override
public B2BOrderChecklist saveB2BOrderChecklist(B2BOrderChecklist b2bOrderChecklist) {
	
	B2BOrderChecklist checkList = getB2bOrderChecklist(b2bOrderChecklist.getBaseOrder());
	if(checkList!=null){
		checkList.setCForm(b2bOrderChecklist.isCForm());
		return (B2BOrderChecklist) save(checkList);
	}
	return (B2BOrderChecklist) save(b2bOrderChecklist);
}


}
