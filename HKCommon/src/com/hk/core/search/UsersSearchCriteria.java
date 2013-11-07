package com.hk.core.search;

import com.hk.domain.user.User;
import com.hk.util.HKCollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/1/13
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsersSearchCriteria {

    // list of variables that might be received
    private List<String> states;
    private List<String> cities;
    private List<Long> zones;
    private List<String> productVariantIds;
    private List<String> productIds;
    private Boolean verified;
    private boolean atleastOneVariableSet = false;

    private static Logger logger = LoggerFactory.getLogger(UsersSearchCriteria.class);

    public DetachedCriteria getSearchCriteria() {
        return getCriteriaFromBaseCriteria();
    }


    private DetachedCriteria getCriteriaFromBaseCriteria() {

        if (!atleastOneVariableSet) {
            throw new RuntimeException("No parameter set");
        }

        DetachedCriteria criteria = DetachedCriteria.forClass(User.class, "user");


        if ((productIds != null && !productIds.isEmpty()) || (productVariantIds != null && !productVariantIds.isEmpty())) {
            criteria.createAlias("user.orders", "orders");
            criteria.createAlias("orders.cartLineItems", "cli");
            criteria.createAlias("cli.productVariant", "pv");
            criteria.createAlias("pv.product", "prod");

            if (productIds != null && !productIds.isEmpty()) {
                criteria.add(Restrictions.in("prod.id", productIds));
            } else if (productVariantIds != null && !productVariantIds.isEmpty()) {
                criteria.createAlias("prod.productVariants", "pv2");
                criteria.add(Restrictions.in("pv2.id", productVariantIds));
            }
        }

        if (HKCollectionUtils.isNotBlank(cities)) {
            criteria.createAlias("user.addresses", "addr");
            criteria.add(Restrictions.in("addr.city", cities));
        }

        if (HKCollectionUtils.isNotBlank(states)) {
            criteria.createAlias("user.addresses", "addr");
            criteria.add(Restrictions.in("addr.state", states));
        }

        if (HKCollectionUtils.isNotBlank(zones)) {
            criteria.createAlias("user.addresses", "addr");
            criteria.createAlias("addr.pincode", "pin");
            criteria.createAlias("pin.zone", "zone");
            criteria.add(Restrictions.in("zone.id", zones));
        }
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    public UsersSearchCriteria setZones(List<Long> zones) {
        this.zones = zones;
        atleastOneVariableSet = true;
        return this;
    }

    public UsersSearchCriteria setVerified(String verified) {
        try {
            atleastOneVariableSet = true;
            this.verified = Boolean.parseBoolean(verified);
        } catch (Exception e) {
            atleastOneVariableSet = false;
            this.verified = null;
        }
        return this;
    }

    public UsersSearchCriteria setProductIds(List<String> ids) {
        this.productIds = ids;
        atleastOneVariableSet = true;
        return this;
    }

    public UsersSearchCriteria setProductVariantIds(List<String> ids) {
        this.productVariantIds = ids;
        atleastOneVariableSet = true;
        return this;
    }

    public UsersSearchCriteria setCities(List<String> cities) {
        this.cities = cities;
        atleastOneVariableSet = true;
        return this;
    }


    public UsersSearchCriteria setStates(List<String> states) {
        this.states = states;
        atleastOneVariableSet = true;
        return this;
    }


}
