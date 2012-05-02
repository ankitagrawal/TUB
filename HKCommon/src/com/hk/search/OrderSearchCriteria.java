package com.hk.search;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrderStatus;

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

    /**
     * shipping order fields
     */
    private List<ShippingOrderStatus> shippingOrderStatusList;
    private Set<String>               shippingOrderCategories;

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

    public OrderSearchCriteria setEmail(String email) {
        this.email = email;
        return this;
    }

    public OrderSearchCriteria setShippingOrderCategories(Set<String> shippingOrderCategories) {
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
            paymentCriteria.addOrder(org.hibernate.criterion.Order.asc("paymentDate"));
        } else {
            // criteria.addOrder(org.hibernate.criterion.Order.asc("id"));
            paymentCriteria.addOrder(org.hibernate.criterion.Order.asc("paymentDate"));
        }
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

        if (shippingOrderCategories != null && !shippingOrderCategories.isEmpty()) {
            if (shippingOrderCriteria == null) {
                shippingOrderCriteria = criteria.createCriteria("shippingOrders");
            }

            shippingOrderCriteria.add(Restrictions.in("basketCategory", shippingOrderCategories));
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

        // criteria.addOrder(org.hibernate.criterion.Order.desc("updateDate"));

        return criteria;
    }
}
