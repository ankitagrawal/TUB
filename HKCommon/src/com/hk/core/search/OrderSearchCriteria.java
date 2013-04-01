package com.hk.core.search;

import com.hk.domain.analytics.Reason;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrderLifeCycleActivity;
import com.hk.domain.order.ShippingOrderStatus;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author vaibhav.adlakha
 */
public class OrderSearchCriteria extends AbstractOrderSearchCriteria {

    /**
     * order general fields
     */
    private String                    login;
    private String                    phone;
    private String                    name;
    private String                    email;
    private Set<Category>             categories;

    private List<OrderStatus>         orderStatusList;

    /**
     * payment fields
     */
    private List<PaymentMode>         paymentModes;
    private List<PaymentStatus>       paymentStatuses;
    private Date                      paymentStartDate;
    private Date                      paymentEndDate;

    private Boolean                  dropShip;
    private Boolean                  containsJit;

    /**
     * shipping order fields
     */
    private List<ShippingOrderStatus> shippingOrderStatusList;
    private Set<Category>               shippingOrderCategories;
    private List<ShippingOrderLifeCycleActivity> SOLifecycleActivityList;           //addded by someone saying: MAIN HOO DON !!!! please use camel case
    private Set<Reason> reasonList;

    public OrderSearchCriteria setLogin(String login) {
        this.login = login;
        return this;
    }

    public OrderSearchCriteria setOrderStatusList(List<OrderStatus> orderStatusList) {
        this.orderStatusList = orderStatusList;
        return this;
    }

    public OrderSearchCriteria setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public OrderSearchCriteria setName(String name) {
        this.name = name;
        return this;
    }

    public OrderSearchCriteria setPaymentStatuses(List<PaymentStatus> paymentStatuses) {
        this.paymentStatuses = paymentStatuses;
        return this;
    }

    public OrderSearchCriteria setShippingOrderStatusList(List<ShippingOrderStatus> shippingOrderStatusList) {
        this.shippingOrderStatusList = shippingOrderStatusList;
        return this;
    }

    public void setSOLifecycleActivityList(List<ShippingOrderLifeCycleActivity> SOLifecycleActivityList) {
        this.SOLifecycleActivityList = SOLifecycleActivityList;
    }

    public OrderSearchCriteria setReasonList(Set<Reason> reasonList) {
        this.reasonList = reasonList;
        return this;
    }

    public OrderSearchCriteria setEmail(String email) {
        this.email = email;
        return this;
    }

    public OrderSearchCriteria setShippingOrderCategories(Set<Category> shippingOrderCategories) {
        this.shippingOrderCategories = shippingOrderCategories;
        return this;
    }

    public OrderSearchCriteria setPaymentModes(List<PaymentMode> paymentModes) {
        this.paymentModes = paymentModes;
        return this;
    }

    public OrderSearchCriteria setPaymentStartDate(Date paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
        return this;
    }

    public OrderSearchCriteria setPaymentEndDate(Date paymentEndDate) {
        this.paymentEndDate = paymentEndDate;
        return this;
    }

    public OrderSearchCriteria setCategories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    protected DetachedCriteria getBaseCriteria() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
        return criteria;
    }

    protected DetachedCriteria buildSearchCriteriaFromBaseCriteria() {
        DetachedCriteria criteria = super.buildSearchCriteriaFromBaseCriteria();



        if (orderStatusList != null && orderStatusList.size() > 0) {
            criteria.add(Restrictions.in("orderStatus", orderStatusList));
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
        DetachedCriteria paymentCriteria = criteria.createCriteria("payment", CriteriaSpecification.LEFT_JOIN);
        if (paymentStartDate != null || paymentEndDate != null) {
            paymentCriteria.add(Restrictions.between("paymentDate", paymentStartDate, paymentEndDate));
        }
        /*
         * else { // criteria.addOrder(org.hibernate.criterion.Order.asc("id"));
         * paymentCriteria.addOrder(org.hibernate.criterion.Order.asc("paymentDate")); }
         */
        if (paymentModes != null && paymentModes.size() > 0) {
            /*
             * if (paymentCriteria == null) { paymentCriteria = criteria.createCriteria("payment"); }
             */
            paymentCriteria.add(Restrictions.in("paymentMode", paymentModes));
        }
        if (paymentStatuses != null && paymentStatuses.size() > 0) {
            /*
             * if (paymentCriteria == null) { paymentCriteria = criteria.createCriteria("payment"); }
             */
            paymentCriteria.add(Restrictions.in("paymentStatus", paymentStatuses));
        }

        /**
         * shipping order status restrictions
         */
        DetachedCriteria shippingOrderCriteria = null;
        if (shippingOrderStatusList != null && !shippingOrderStatusList.isEmpty()) {
            if (shippingOrderCriteria == null) {
                shippingOrderCriteria = criteria.createCriteria("shippingOrders", CriteriaSpecification.LEFT_JOIN);
            }

        shippingOrderCriteria.add(Restrictions.or(Restrictions.in("shippingOrderStatus", shippingOrderStatusList), Restrictions.isNull("shippingOrderStatus")));
        }

        if (SOLifecycleActivityList != null && !SOLifecycleActivityList.isEmpty()) {
            DetachedCriteria shippingLifeCycleCriteria = null;
            if (shippingOrderCriteria == null){
                shippingOrderCriteria = criteria.createCriteria("shippingOrders");
            }
                shippingLifeCycleCriteria =  shippingOrderCriteria.createCriteria("shippingOrderLifecycles", CriteriaSpecification.INNER_JOIN);
                shippingLifeCycleCriteria.add(Restrictions.in("shippingOrderLifeCycleActivity", SOLifecycleActivityList));

            DetachedCriteria lifecycleCriteria = null;
            if (reasonList != null && !reasonList.isEmpty()) {
                if (lifecycleCriteria == null) {
                    lifecycleCriteria = shippingLifeCycleCriteria.createCriteria("lifecycleReasons");
                }
                lifecycleCriteria.add(Restrictions.in("reason", reasonList));
            }
        }

        if (shippingOrderCategories != null && !shippingOrderCategories.isEmpty()) {
            if (shippingOrderCriteria == null) {
                shippingOrderCriteria = criteria.createCriteria("shippingOrders");
            }

            DetachedCriteria shippingOrderCategoryCriteria = null;
            if (shippingOrderCategoryCriteria == null) {
                shippingOrderCategoryCriteria = shippingOrderCriteria.createCriteria("categories");
            }
            shippingOrderCategoryCriteria.add(Restrictions.in("category", shippingOrderCategories));
//            shippingOrderCriteria.add(Restrictions.in("basketCategory", shippingOrderCategories));
        }

        /**
         * order category restriction
         */
        DetachedCriteria orderCategoryCriteria = null;
        if (categories != null && !categories.isEmpty()) {
            if (orderCategoryCriteria == null) {
                orderCategoryCriteria = criteria.createCriteria("categories");
            }

            orderCategoryCriteria.add(Restrictions.in("category", categories));
        }

        // criteria.addOrder(org.hibernate.criterion.Order.desc("score"));
        // criteria.addOrder(org.hibernate.criterion.Order.desc("updateDate"));

        if (sortByPaymentDate) {
            paymentCriteria.addOrder(OrderBySqlFormula.sqlFormula("payment_date asc"));

        }
        if(sortByDispatchDate){
            criteria.addOrder(org.hibernate.criterion.Order.asc("targetDispatchDate"));
        }
        if (sortByScore) {
            criteria.addOrder(org.hibernate.criterion.Order.desc("score"));
        }

        if(dropShip != null  )  {
             shippingOrderCriteria.add(Restrictions.eq("isDropShipping",dropShip));
         }
        if(containsJit != null  )  {
            shippingOrderCriteria.add(Restrictions.eq("containsJitProducts",containsJit));
        }        return criteria;
    }

    public Boolean isDropShip() {
        return dropShip;
    }

    public void setDropShip(Boolean dropShip) {
        this.dropShip = dropShip;
    }

    public Boolean containsJit() {
        return containsJit;
    }

    public void setContainsJit(Boolean containsJit) {
        this.containsJit = containsJit;
    }
}
