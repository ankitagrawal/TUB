package com.hk.admin.impl.dao.marketing;
// Generated Dec 23, 2011 1:27:53 PM by Hibernate Tools 3.2.4.CR1


import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.marketing.AmazonFeedDao;
import com.hk.domain.amazon.AmazonFeed;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.impl.dao.BaseDaoImpl;


@Repository
public class AmazonFeedDaoImpl extends BaseDaoImpl implements AmazonFeedDao{

   /* public AmazonFeedDao() {
        super(AmazonFeed.class);
    }*/

  public AmazonFeed findByPV(ProductVariant productVariant){
    Criteria criteria = getSession().createCriteria(AmazonFeed.class);
    criteria.add(Restrictions.eq("productVariant", productVariant));
    return (AmazonFeed) criteria.uniqueResult();
  }

}

