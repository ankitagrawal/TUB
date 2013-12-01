package com.hk.core.search;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/12/12
 * Time: 3:35 PM
 */
public class SubscriptionSearchCriteria{
    private Long               subscriptionId;


    private boolean            sortByUpdateDate  = true;

    private String                    login;
    private String                    phone;
    private String                    name;
    private String                    email;

    private ProductVariant productVariant;

    private Long                                     baseOrderId;

    private Date startNextShipmentDate;
    private Date endNextShipmentDate;

    private List<SubscriptionStatus> subscriptionStatusList;

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public void setSortByUpdateDate(boolean sortByUpdateDate) {
        this.sortByUpdateDate = sortByUpdateDate;
    }

    public DetachedCriteria getSearchCriteria() {
        return buildSearchCriteria();
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

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public Date getStartNextShipmentDate() {
        return startNextShipmentDate;
    }

    public void setStartNextShipmentDate(Date startNextShipmentDate) {
        this.startNextShipmentDate = startNextShipmentDate;
    }

    public Date getEndNextShipmentDate() {
        return endNextShipmentDate;
    }

    public void setEndNextShipmentDate(Date endNextShipmentDate) {
        this.endNextShipmentDate = endNextShipmentDate;
    }

    protected DetachedCriteria buildSearchCriteria() {
        DetachedCriteria subscriptionCriteria = DetachedCriteria.forClass(Subscription.class);

        if (subscriptionId != null) {
            subscriptionCriteria.add(Restrictions.eq("id", subscriptionId));
        }

        if(sortByUpdateDate){
            subscriptionCriteria.addOrder(Order.desc("updateDate"));
        }

        if (subscriptionStatusList != null && subscriptionStatusList.size() > 0) {
            subscriptionCriteria.add(Restrictions.in("subscriptionStatus", subscriptionStatusList));
        }

        if(productVariant !=null){
            subscriptionCriteria.add(Restrictions.eq("productVariant",productVariant));
        }

        DetachedCriteria userCriteria = subscriptionCriteria.createCriteria("user");

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

        DetachedCriteria baseOrderCriteria = subscriptionCriteria.createCriteria("baseOrder");

        if (baseOrderId != null) {
            baseOrderCriteria.add(Restrictions.eq("id", baseOrderId));
        }

        /**
         * address specific restrictions
         */
        DetachedCriteria addressCriteria = subscriptionCriteria.createCriteria("address");
        if (StringUtils.isNotBlank(phone)) {
            addressCriteria.add(Restrictions.like("phone", "%" + phone + "%"));
        }

        /**
         * restrictions for next shipment dates
         */
        if (startNextShipmentDate != null || endNextShipmentDate != null) {
            subscriptionCriteria.add(Restrictions.between("nextShipmentDate", startNextShipmentDate, endNextShipmentDate));
        }

        return subscriptionCriteria;
    }

}
