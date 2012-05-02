package com.hk.dao.offer;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.offer.Offer;

@Repository
@SuppressWarnings("unchecked")
public class OfferDao extends BaseDaoImpl {

    
    public Page listAllValid(int pageNo, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Offer.class);
        criteria.add(Restrictions.ge("endDate", new Date()));
        criteria.addOrder(Order.desc("id"));
        return list(criteria, pageNo, perPage);
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
