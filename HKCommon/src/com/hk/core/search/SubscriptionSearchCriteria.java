package com.hk.core.search;

import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionStatus;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/12/12
 * Time: 3:35 PM
 */
public class SubscriptionSearchCriteria{
    private Long               subscriptionId;


    private boolean            sortByUpdateDate  = true;

    protected DetachedCriteria baseCriteria;

    private String                    login;
    private String                    phone;
    private String                    name;
    private String                    email;

    private Long                                     baseOrderId;

    private List<SubscriptionStatus> subscriptionStatusList;

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public void setSortByUpdateDate(boolean sortByUpdateDate) {
        this.sortByUpdateDate = sortByUpdateDate;
    }

    public DetachedCriteria getSearchCriteria() {
        return buildSearchCriteriaFromBaseCriteria();
    }

    public SubscriptionSearchCriteria setLogin(String login) {
        this.login = login;
        return this;
    }

    public SubscriptionSearchCriteria setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public SubscriptionSearchCriteria setName(String name) {
        this.name = name;
        return this;
    }

    public SubscriptionSearchCriteria setEmail(String email) {
        this.email = email;
        return this;
    }

    public SubscriptionSearchCriteria setBaseOrderId(Long baseOrderId) {
        this.baseOrderId = baseOrderId;
        return this;
    }

    public SubscriptionSearchCriteria setSubscriptionStatusList(List<SubscriptionStatus> subscriptionStatusList) {
        this.subscriptionStatusList = subscriptionStatusList;
        return this;
    }

    protected DetachedCriteria getBaseCriteria() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Subscription.class);
        return criteria;
    }

    protected DetachedCriteria buildSearchCriteriaFromBaseCriteria() {
        this.baseCriteria = getBaseCriteria();

        if (subscriptionId != null) {
            baseCriteria.add(Restrictions.eq("id", subscriptionId));
        }

        if(sortByUpdateDate){
            baseCriteria.addOrder(Order.desc("updateDate"));
        }

        if (subscriptionStatusList != null && subscriptionStatusList.size() > 0) {
            baseCriteria.add(Restrictions.in("subscriptionStatus", subscriptionStatusList));
        }

        DetachedCriteria userCriteria = baseCriteria.createCriteria("user");

        /**
         * user specific restrictions
         */

        if (StringUtils.isNotBlank(email)) {
            userCriteria.add(Restrictions.like("email", "%" + email + "%"));
        }
        if (StringUtils.isNotBlank(login)) {
            userCriteria.add(Restrictions.like("login", "%" + login + "%"));
        }
        if (StringUtils.isNotBlank(name)) {
            userCriteria.add(Restrictions.like("name", "%" + name + "%"));
        }

        DetachedCriteria baseOrderCriteria = baseCriteria.createCriteria("baseOrder");

        if (baseOrderId != null) {
            baseOrderCriteria.add(Restrictions.eq("id", baseOrderId));
        }

        /**
         * address specific restrictions
         */
        DetachedCriteria addressCriteria = baseCriteria.createCriteria("address");
        if (StringUtils.isNotBlank(phone)) {
            addressCriteria.add(Restrictions.like("phone", "%" + phone + "%"));
        }

        return baseCriteria;
    }

}
