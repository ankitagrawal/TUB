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

    private Long               orderId;
    private Long               storeId;
    private String             gatewayOrderId;

    protected boolean          sortByPaymentDate = true;
    protected boolean          sortByScore       = true;
    protected boolean          sortByLastEscDate       = true;
    protected boolean          sortByDispatchDate       = true;

    private boolean            orderAsc          = false;
    private boolean            sortByUpdateDate  = true;

    private SearchDao          searchDao;
    protected DetachedCriteria baseCriteria;

    /*
     * private int maxResults = -1; private static final int DEFAULT_MAX_RESULTS = 100;
     */

    public AbstractOrderSearchCriteria setStoreId(Long storeId) {
        this.storeId = storeId;
        return this;
    }

    public AbstractOrderSearchCriteria setOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public AbstractOrderSearchCriteria setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
        return this;
    }

    public AbstractOrderSearchCriteria setSortByPaymentDate(boolean sortByPaymentDate) {
        this.sortByPaymentDate = sortByPaymentDate;
        return this;
    }

    public AbstractOrderSearchCriteria setSortByScore(boolean sortByScore) {
        this.sortByScore = sortByScore;
        return this;
    }

    public AbstractOrderSearchCriteria setSortByLastEscDate(boolean sortByLastEscDate) {
        this.sortByLastEscDate = sortByLastEscDate;
        return this;
    }
    public AbstractOrderSearchCriteria setSortByDispatchDate(boolean sortByDispatchDate) {
        this.sortByDispatchDate = sortByDispatchDate;
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

    /*
     * public AbstractOrderSearchCriteria setMaxResults(int maxResults) { this.maxResults = maxResults; return this; }
     */

    protected abstract DetachedCriteria getBaseCriteria();

    /**
     * override this method in child search criterias and ensure to call super.buildSearchCriteriaFromBaseCriteria()
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

        if (storeId != null) {
            baseCriteria.add(Restrictions.eq("store.id", storeId));
        }

        /*
         * if (sortByUpdateDate) { if (!orderAsc) {
         * baseCriteria.addOrder(org.hibernate.criterion.Order.desc("updateDate")); } else {
         * baseCriteria.addOrder(org.hibernate.criterion.Order.asc("updateDate")); } }
         */

        if (sortByUpdateDate && orderAsc) {
            baseCriteria.addOrder(org.hibernate.criterion.Order.asc("updateDate"));
        }

        // TODO: fix later after rewrite
        // baseCriteria.setMaxResults(2);

        /*
         * if (maxResults == -1) { baseCriteria.setMaxResults(DEFAULT_MAX_RESULTS); } else {
         * baseCriteria.setMaxResults(maxResults); }
         */

        return baseCriteria;
    }

    public DetachedCriteria getSearchCriteria() {
        return buildSearchCriteriaFromBaseCriteria();
    }

    /*
     * protected Session getSessionProvider() { return getSearchDao().getSessionProvider(); }
     */

    public SearchDao getSearchDao() {
        if (searchDao == null) {
            searchDao = (SearchDao) ServiceLocatorFactory.getService(SearchDao.class);
        }
        return searchDao;
    }
}
