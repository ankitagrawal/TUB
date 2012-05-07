package com.hk.admin.impl.dao.inventory;

import org.springframework.stereotype.Repository;

import com.hk.domain.RetailLineItem;
import com.hk.domain.shippingOrder.LineItem;
// Generated Jan 9, 2012 11:17:59 PM by Hibernate Tools 3.2.4.CR1
import com.hk.impl.dao.BaseDaoImpl;



@Repository
public class RetailLineItemDao extends BaseDaoImpl {

  /*public RetailLineItemDao() {
    super(RetailLineItem.class);
  }*/

  public RetailLineItem getRetailLineItem(LineItem lineItem) {
    String query = " select rli from RetailLineItem rli where rli.lineItem = :lineItem ";
    return (RetailLineItem) getSession().createQuery(query)
        .setEntity("lineItem", lineItem)
        .uniqueResult();
  }
  //To change body of overridden methods use File | Settings | File Templates.
}



