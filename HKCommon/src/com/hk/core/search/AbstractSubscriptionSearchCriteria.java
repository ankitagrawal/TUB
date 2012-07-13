package com.hk.core.search;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/12/12
 * Time: 5:59 PM
 */
public abstract class AbstractSubscriptionSearchCriteria {

    private Long               subscriptionId;

    protected boolean          sortByPaymentDate = true;
    protected boolean          sortByScore       = true;

    private boolean            orderAsc          = false;
    private boolean            sortByUpdateDate  = true;

    protected DetachedCriteria baseCriteria;

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

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

    public void setSortByPaymentDate(boolean sortByPaymentDate) {
        this.sortByPaymentDate = sortByPaymentDate;
    }

    public void setSortByScore(boolean sortByScore) {
        this.sortByScore = sortByScore;
    }

    public void setOrderAsc(boolean orderAsc) {
        this.orderAsc = orderAsc;
    }

    public void setSortByUpdateDate(boolean sortByUpdateDate) {
        this.sortByUpdateDate = sortByUpdateDate;
    }

    private DetachedCriteria buildBaseCriteria() {

        this.baseCriteria = getBaseCriteria();

        if (subscriptionId != null) {
            baseCriteria.add(Restrictions.eq("subscriptionId", subscriptionId));
        }

        /*
         * if (sortByUpdateDate) { if (!orderAsc) {
         * baseCriteria.addOrder(org.hibernate.criterion.Order.desc("updateDate")); } else {
         * baseCriteria.addOrder(org.hibernate.criterion.Order.asc("updateDate")); } }
         */

    /*    if (sortByUpdateDate && orderAsc) {
            baseCriteria.addOrder(org.hibernate.criterion.Order.asc("updateDate"));
        }
*/
        return baseCriteria;
    }

    public DetachedCriteria getSearchCriteria() {
        return buildSearchCriteriaFromBaseCriteria();
    }

}
