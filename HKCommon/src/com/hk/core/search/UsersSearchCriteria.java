package com.hk.core.search;

import com.hk.domain.user.User;
import com.hk.util.HKCollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
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

    private boolean emailsonly = false;

    public DetachedCriteria getSearchCriteria(boolean emailsonly) {
        return getCriteriaFromBaseCriteria(emailsonly);
    }


    private DetachedCriteria getCriteriaFromBaseCriteria(boolean emailsonly) {

        if (!atleastOneVariableSet) {
            throw new RuntimeException("No parameter set");
        }

        DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class, "user");
        boolean orderCriteria = false,
                cliCriteria = false,
                pvCriteria = false,
                prodCriteria = false,
                addrCriteria = false,
                pinCriteria = false,
                zoneCriteria = false,
                pv2Criteria = false,
                rolesCriteria = false;

        if (verified != null) {
            if (!rolesCriteria) {
                rolesCriteria = true;
                userCriteria.createAlias("user.roles", "roles");
            }
            String roleName = (verified.booleanValue()) ? "HK_USER" : "HKUNVERIFIED";
            userCriteria.add(Restrictions.eq("roles.name", roleName));
        }

        if ((productIds != null && !productIds.isEmpty()) || (productVariantIds != null && !productVariantIds.isEmpty())) {
            if (!orderCriteria) {
                userCriteria.createAlias("user.orders", "orders");
                orderCriteria = true;
            }
            if (!cliCriteria) {
                userCriteria.createAlias("orders.cartLineItems", "cli");
                cliCriteria = true;
            }
            if (!pvCriteria) {
                userCriteria.createAlias("cli.productVariant", "pv");
                pvCriteria = true;
            }
            if (!prodCriteria) {
                userCriteria.createAlias("pv.product", "prod");
                prodCriteria = true;
            }


            if (productIds != null && !productIds.isEmpty()) {
                userCriteria.add(Restrictions.in("prod.id", productIds));
            } else if (productVariantIds != null && !productVariantIds.isEmpty()) {
                if (!pv2Criteria) {
                    userCriteria.createAlias("prod.productVariants", "pv2");
                    pv2Criteria = true;
                }
                userCriteria.add(Restrictions.in("pv2.id", productVariantIds));
            }
        }

        if (HKCollectionUtils.isNotBlank(cities)) {
            if (!addrCriteria) {
                userCriteria.createAlias("user.addresses", "addr");
                addrCriteria = true;
            }
            userCriteria.add(Restrictions.in("addr.city", cities));
        }

        if (HKCollectionUtils.isNotBlank(states)) {
            if (!addrCriteria) {
                userCriteria.createAlias("user.addresses", "addr");
                addrCriteria = true;
            }
            userCriteria.add(Restrictions.in("addr.state", states));
        }

        if (HKCollectionUtils.isNotBlank(zones)) {
            if (!addrCriteria) {
                userCriteria.createAlias("user.addresses", "addr");
                addrCriteria = true;
            }
            if (!pinCriteria) {
                userCriteria.createAlias("addr.pincode", "pin");
                pinCriteria = true;
            }
            if (!zoneCriteria) {
                userCriteria.createAlias("pin.zone", "zone");
                zoneCriteria = true;
            }
            userCriteria.add(Restrictions.in("zone.id", zones));
        }
        if (emailsonly) {
            userCriteria.setProjection(Projections.distinct(Projections.property("user.login")));
        } else {
            userCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        }
        return userCriteria;
    }

    public UsersSearchCriteria setZones(List<Long> zones) {
        this.zones = zones;
        atleastOneVariableSet = true;
        return this;
    }

    public UsersSearchCriteria setVerified(String verified) {
        try {
            this.verified = "true".equalsIgnoreCase(verified) ? true : "false".equalsIgnoreCase(verified) ? false : null;
            atleastOneVariableSet = this.verified == null ? atleastOneVariableSet : true;
        } catch (Exception e) {
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
