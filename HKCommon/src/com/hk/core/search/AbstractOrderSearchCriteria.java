package com.hk.core.search;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.akube.framework.dao.SearchDao;
import com.hk.service.ServiceLocatorFactory;

/**
 * @author vaibhav.adlakha
 */
public abstract class AbstractOrderSearchCriteria {

  private Long orderId;
  private String gatewayOrderId;
  private boolean orderAsc = false;
  private boolean sortByUpdateDate = true;

  private SearchDao searchDao;
  protected DetachedCriteria baseCriteria;


  public AbstractOrderSearchCriteria setOrderId(Long orderId) {
    this.orderId = orderId;
    return this;
  }

  public AbstractOrderSearchCriteria setGatewayOrderId(String gatewayOrderId) {
    this.gatewayOrderId = gatewayOrderId;
    return this;
  }

  public AbstractOrderSearchCriteria setOrderAsc(boolean orderAsc) {
    this.orderAsc = orderAsc;
    return this;
  }

  public AbstractOrderSearchCriteria setSortByUpdateDate(boolean sortByUpdateDate) {
    this.sortByUpdateDate = sortByUpdateDate;
    return this;
  }

  protected abstract DetachedCriteria getBaseCriteria();

  /**
   * override this method in child search criterias and enusre to call super.buildSearchCriteriaFromBaseCriteria()
   * before adding more restrictions.
   *
   * @return
   */
  protected DetachedCriteria buildSearchCriteriaFromBaseCriteria() {
    return buildBaseCriteria();
  }


  private DetachedCriteria buildBaseCriteria() {
    this.baseCriteria = getBaseCriteria();
    if (orderId != null) {
      baseCriteria.add(Restrictions.eq("id", orderId));
    }


    if (StringUtils.isNotBlank(gatewayOrderId)) {
      baseCriteria.add(Restrictions.eq("gatewayOrderId", gatewayOrderId));
    }

    if (sortByUpdateDate) {
      if (!orderAsc) {
        baseCriteria.addOrder(org.hibernate.criterion.Order.desc("updateDate"));
      } else {
        baseCriteria.addOrder(org.hibernate.criterion.Order.asc("updateDate"));
      }
    }

    //TODO: fix later after rewrite
//    baseCriteria.setMaxResults(2);


    return baseCriteria;
  }

  public DetachedCriteria getSearchCriteria() {
    return buildSearchCriteriaFromBaseCriteria();
  }


 /* protected Session getSessionProvider() {
    return getSearchDao().getSessionProvider();
  }*/

  public SearchDao getSearchDao() {
    if (searchDao == null) {
      searchDao = (SearchDao) ServiceLocatorFactory.getService(SearchDao.class);
    }
    return searchDao;
  }
}
