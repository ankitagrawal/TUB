package com.hk.impl.dao.offer;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.domain.offer.Offer;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.offer.OfferDao;

@Repository
@SuppressWarnings("unchecked")
public class OfferDaoImpl extends BaseDaoImpl implements OfferDao {

    public Page listAllValid(int pageNo, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Offer.class);
        criteria.add(Restrictions.ge("endDate", new Date()));
        criteria.addOrder(Order.desc("id"));
        return list(criteria, pageNo, perPage);
    }

  public Page listAllValidShowPromptly(int pageNo, int perPage) {
      DetachedCriteria criteria = DetachedCriteria.forClass(Offer.class);
      criteria.add(Restrictions.eq("showPromptly", Boolean.TRUE));
      criteria.add(Restrictions.ge("endDate", new Date()));
      criteria.addOrder(Order.desc("id"));
      return list(criteria, pageNo, perPage);
  }

  public List<Offer> listAllValidShowPromptly() {
    DetachedCriteria criteria = DetachedCriteria.forClass(Offer.class);
    criteria.add(Restrictions.eq("showPromptly", Boolean.TRUE));
    criteria.add(Restrictions.ge("endDate", new Date()));
    criteria.addOrder(Order.desc("id"));
    return (List<Offer>) findByCriteria(criteria);
  }


  public Page listAll(int pageNo, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Offer.class);
        criteria.addOrder(Order.desc("id"));
        return list(criteria, pageNo, perPage);
    }

    public Offer findByIdentifier(String offerIdentifier) {
        Criteria criteria = getSession().createCriteria(Offer.class);
        criteria.add(Restrictions.eq("offerIdentifier", offerIdentifier));
        return (Offer) criteria.uniqueResult();
    }

}
