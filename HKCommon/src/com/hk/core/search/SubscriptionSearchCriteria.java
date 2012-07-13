package com.hk.core.search;

import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionStatus;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
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
public class SubscriptionSearchCriteria extends AbstractSubscriptionSearchCriteria{

    private String                    login;
    private String                    phone;
    private String                    name;
    private String                    email;

    private Long                                     baseOrderId;

    private Date                      paymentStartDate;
    private Date                      paymentEndDate;

    private List<SubscriptionStatus> subscriptionStatusList;

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

    public SubscriptionSearchCriteria setPaymentStartDate(Date paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
        return this;
    }

    public SubscriptionSearchCriteria setPaymentEndDate(Date paymentEndDate) {
        this.paymentEndDate = paymentEndDate;
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
        DetachedCriteria criteria = super.buildSearchCriteriaFromBaseCriteria();

        if (subscriptionStatusList != null && subscriptionStatusList.size() > 0) {
            criteria.add(Restrictions.in("subscriptionStatus", subscriptionStatusList));
        }

        DetachedCriteria userCriteria = criteria.createCriteria("user");

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

        DetachedCriteria orderCriteria = criteria.createCriteria("order");

        if (baseOrderId != null) {
             orderCriteria.add(Restrictions.eq("id", baseOrderId));
        }

        /**
         * address specific restrictions
         */
        DetachedCriteria addressCriteria = criteria.createCriteria("address");
        if (StringUtils.isNotBlank(phone)) {
            addressCriteria.add(Restrictions.like("phone", "%" + phone + "%"));
        }

        /**
         * payment specific restrictions
         */
     /*   DetachedCriteria paymentCriteria = criteria.createCriteria("payment", CriteriaSpecification.LEFT_JOIN);
        if (paymentStartDate != null || paymentEndDate != null) {
            paymentCriteria.add(Restrictions.between("paymentDate", paymentStartDate, paymentEndDate));
        }


        if (sortByPaymentDate) {
            paymentCriteria.addOrder(OrderBySqlFormula.sqlFormula("date(payment_date) asc"));

        }*/

        return criteria;
    }

}
