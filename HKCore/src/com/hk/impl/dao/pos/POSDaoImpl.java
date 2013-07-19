package com.hk.impl.dao.pos;

import com.hk.dto.pos.POSSalesDto;
import com.hk.dto.pos.POSReturnItemDto;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.pos.POSDao;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import com.hk.pact.dao.BaseDao;

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
  public List<POSSalesDto> findSaleForTimeFrame(Long storeId, Date startDate, Date endDate) {

/*    DetachedCriteria criteria = DetachedCriteria.forClass(Payment.class);
    criteria.add(Restrictions.eq("order.id", 1L));
    //criteria.add(Restrictions.eq("order.store.id", storeId));


    @SuppressWarnings("unchecked")
    List<Payment> payments = this.baseDao.findByCriteria(criteria);
    return payments;*/

    String hqlQuery = "select p.amount as amount ,p.paymentMode.name as paymentMode from Payment p where p.order.createDate > :startDate and p.order.createDate < :endDate and p.order.store.id= :storeId";
    return getSession().createQuery(hqlQuery).setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("storeId", storeId).setResultTransformer(Transformers.aliasToBean(POSSalesDto.class)).list();

  }


/*  @Override
  public List<POSSalesDto> findSaleForTimeFrame(Long storeId,Date startDate, Date endDate) {

    String hqlQuery = "select p.amount as amount ,p.paymentMode.name as paymentMode, p.order.cartLineItems  from Payment p where p.order.createDate > :startDate and p.order.createDate < :endDate and p.order.store.id= :storeId";
    return getSession().createQuery(hqlQuery).setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("storeId", storeId).setResultTransformer(Transformers.aliasToBean(POSSalesDto.class)).list();

  }*/

  public List<POSReturnItemDto> findReturnItemForTimeFrame(Long storeId, Date startDate, Date endDate){

    String hqlQuery = "select order from order order where order.orderStatus.id=45 or order.orderStatus.id=50 and order.createDate > :startDate and order.createDate < :endDate and order.store.id= :storeId ";
    return getSession().createQuery(hqlQuery).setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("storeId", storeId).setResultTransformer(Transformers.aliasToBean(POSSalesDto.class)).list();

  }
}
