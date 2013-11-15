package com.hk.core.search;

import com.hk.domain.user.User;
import com.hk.util.HKCollectionUtils;
import org.apache.commons.lang.StringUtils;
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
    private List<String> emails;
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
    private List<Long> storeIds;
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
        if (HKCollectionUtils.isNotBlank(emails)) {
            userCriteria.add(Restrictions.in("user.email", emails));
        } else {
            userCriteria.add(Restrictions.isNotNull("user.email"));
        }

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
                userReportCriteria = false,
                userStoreCriteria = false;

        if (HKCollectionUtils.isNotBlank(storeIds)) {
            if (!userStoreCriteria) {
                userCriteria.createAlias("user.store", "store");
                userStoreCriteria = true;
            }
            userCriteria.add(Restrictions.in("store.id", storeIds));
        }

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
        atleastOneVariableSet = HKCollectionUtils.isNotBlank(this.zones) ? true : atleastOneVariableSet;
        return this;
    }

    public UsersSearchCriteria setVerified(Boolean verified) {
        if (verified != null) {
            this.verified = verified;
            atleastOneVariableSet = true;
        }
        return this;
    }

    public UsersSearchCriteria setProductIds(List<String> productIds) {
        this.productIds = productIds;
        atleastOneVariableSet = HKCollectionUtils.isNotBlank(this.productIds) ? true : atleastOneVariableSet;
        return this;
    }

    public UsersSearchCriteria setProductVariantIds(List<String> productVariantIds) {
        this.productVariantIds = productVariantIds;
        atleastOneVariableSet = HKCollectionUtils.isNotBlank(this.productVariantIds) ? true : atleastOneVariableSet;
        return this;
    }

    public UsersSearchCriteria setCities(List<String> cities) {
        this.cities = cities;
        atleastOneVariableSet = HKCollectionUtils.isNotBlank(this.cities) ? true : atleastOneVariableSet;
        return this;
    }

    public UsersSearchCriteria setEmails(List<String> emails) {
        this.emails = emails;
        atleastOneVariableSet = HKCollectionUtils.isNotBlank(this.emails) ? true : atleastOneVariableSet;
        return this;
    }


    public UsersSearchCriteria setStates(List<String> states) {
        this.states = states;
        atleastOneVariableSet = HKCollectionUtils.isNotBlank(this.states) ? true : atleastOneVariableSet;
        return this;
    }

    public UsersSearchCriteria setCategories(List<String> categories) {
        this.categories = categories;
        atleastOneVariableSet = HKCollectionUtils.isNotBlank(this.categories) ? true : atleastOneVariableSet;
        return this;
    }

    public UsersSearchCriteria setEquality(String equality) {
        this.equality = equality;
        atleastOneVariableSet = StringUtils.isNotBlank(equality) ? true : atleastOneVariableSet;
        return this;
    }


    public UsersSearchCriteria setStoreIds(List<Long> storeIds) {
        this.storeIds = storeIds;
        atleastOneVariableSet = HKCollectionUtils.isNotBlank(this.storeIds) ? true : atleastOneVariableSet;
        return this;
    }

    public UsersSearchCriteria setUserOrderCount(Integer userOrderCount) {
        this.userOrderCount = userOrderCount;
        atleastOneVariableSet = this.userOrderCount != null ? true : atleastOneVariableSet;
        return this;
    }
}
