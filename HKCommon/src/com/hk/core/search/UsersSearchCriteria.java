package com.hk.core.search;

import com.hk.domain.user.User;
import com.hk.util.HKCollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
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
    private List<String> categories;
    private List<String> states;
    private List<String> cities;
    private List<Long> zones;
    private List<String> productVariantIds;
    private List<String> productIds;
    private Boolean verified;
    private Integer userOrderCount;
    private String equality = "ge"; // ge (greater than) is the default value for equality
    private boolean atleastOneVariableSet = false;
    private boolean notOnProduction = true;

    private static Logger logger = LoggerFactory.getLogger(UsersSearchCriteria.class);

    private boolean minInfo = false;

    public DetachedCriteria getSearchCriteria(boolean minInfo) {
        return getCriteriaFromBaseCriteria(minInfo);
    }


    private DetachedCriteria getCriteriaFromBaseCriteria(boolean fetchMinimumRequiredData) {

        if (!atleastOneVariableSet) {
            throw new RuntimeException("No parameter set");
        }

        DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class, "user");
        userCriteria.add(Restrictions.isNotNull("user.email"));

        boolean orderCriteria = false,
                cliCriteria = false,
                pvCriteria = false,
                prodCriteria = false,
                addrCriteria = false,
                pinCriteria = false,
                zoneCriteria = false,
                pv2Criteria = false,
                rolesCriteria = false,
                baseOrderCategoryCriteria = false,
                categoryCriteria = false,
                userReportCriteria = false;


        /*
     select u.login,u.email,u.name,ur.number_of_orders_by_user
from user u, user_report ur
where u.email is not null
and u.id=ur.user_id
and ur.number_of_orders_by_user <=10;
        */


        if (userOrderCount != null) {
            if (!userReportCriteria) {
                userCriteria.createAlias("user.report", "report");
                userReportCriteria = true;
            }
            SimpleExpression se = createRestriction(userOrderCount, equality);
            userCriteria.add(se);
        }
        if (verified != null) {
            if (!rolesCriteria) {
                rolesCriteria = true;
                userCriteria.createAlias("user.roles", "roles");
            }
            String roleName = (verified.booleanValue()) ? "HK_USER" : "HKUNVERIFIED";
            userCriteria.add(Restrictions.eq("roles.name", roleName));
        }

        if (categories != null && !categories.isEmpty()) {
            if (!orderCriteria) {
                userCriteria.createAlias("user.orders", "orders");
                orderCriteria = true;
            }
            if (!baseOrderCategoryCriteria) {
                userCriteria.createAlias("orders.categories", "boc");
                baseOrderCategoryCriteria = true;
            }
            if (!categoryCriteria) {
                userCriteria.createAlias("boc.category", "cat");
                categoryCriteria = true;
            }
            userCriteria.add(Restrictions.in("cat.name", categories));
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
        if (fetchMinimumRequiredData) {
            ProjectionList projList = Projections.projectionList();
            if (notOnProduction) {
                projList.add(Projections.distinct(Projections.property("user.login")));
                projList.add(Projections.property("user.email"));
                projList.add(Projections.property("user.name"));
                projList.add(Projections.property("user.subscribedMask"));
                projList.add(Projections.property("user.unsubscribeToken"));
            } else {
                projList.add(Projections.distinct(Projections.property("user.email")));
                projList.add(Projections.property("user.login"));
                projList.add(Projections.property("user.name"));
                projList.add(Projections.property("user.subscribedMask"));
                projList.add(Projections.property("user.unsubscribeToken"));
            }
            userCriteria.setProjection(projList);
        } else {
            userCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        }
        return userCriteria;
    }

    private SimpleExpression createRestriction(Integer userOrderCount, String equality) {
        String condition = "report.numberOfOrdersByUser";
        int count = userOrderCount.intValue();
        SimpleExpression se = null;
        if ("ge".equalsIgnoreCase(equality)) {
            se = Restrictions.ge(condition, count);
        } else if ("gt".equalsIgnoreCase(equality)) {

            se = Restrictions.gt(condition, count);
        } else if ("eq".equalsIgnoreCase(equality)) {

            se = Restrictions.eq(condition, count);
        } else if ("le".equalsIgnoreCase(equality)) {

            se = Restrictions.le(condition, count);
        } else if ("lt".equalsIgnoreCase(equality)) {
            se = Restrictions.lt(condition, count);
        } else {
            throw new RuntimeException("Illegal equality operator");
        }
        return se;
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

    public UsersSearchCriteria setCategories(List<String> categories) {
        this.categories = categories;
        atleastOneVariableSet = true;
        return this;
    }

    public UsersSearchCriteria setEquality(String equality) {
        this.equality = equality;
        atleastOneVariableSet = true;
        return this;
    }

    public UsersSearchCriteria setUserOrderCount(String userOrderCount) {
        try {
            this.userOrderCount = Integer.parseInt(userOrderCount);
            atleastOneVariableSet = this.userOrderCount == null ? atleastOneVariableSet : true;
        } catch (Exception e) {
            this.userOrderCount = null;
        }
        return this;
    }
}



