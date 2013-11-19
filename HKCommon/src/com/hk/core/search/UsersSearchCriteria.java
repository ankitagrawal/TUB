package com.hk.core.search;

import com.hk.constants.core.Keys;
import com.hk.domain.user.User;
import com.hk.util.HKCollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/1/13
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsersSearchCriteria {
    // Join Column Ids
    private static final short USER_STORE = 0;
    private static final short USER_REPORT = 1;
    private static final short USER_ROLES = 2;
    private static final short USER_ORDER = 3;
    private static final short ORDER_CARTLINEITEM = 4;
    private static final short CARTLINEITEM_PRODUCTVARIANT = 5;
    private static final short PRODUCTVARIANT_PRODUCT = 6;
    private static final short PRODUCT_PRODUCTVARIANT = 7;
    private static final short USER_ADDRESS = 8;
    private static final short ADDRESS_PIN = 9;
    private static final short PIN_ZONE = 10;
    private static final short PRODUCT_CATEGORIES = 11;

    // Map values are as follows {<joinEntity>,<alias>}
    private static Map<Short, String[]> joinColumns = new HashMap<Short, String[]>();

    static {
        joinColumns.put(USER_STORE, new String[]{"user.store", "store"});
        joinColumns.put(USER_REPORT, new String[]{"user.report", "report"});
        joinColumns.put(USER_ROLES, new String[]{"user.roles", "roles"});
        joinColumns.put(USER_ORDER, new String[]{"user.orders", "orders"});
        joinColumns.put(ORDER_CARTLINEITEM, new String[]{"orders.cartLineItems", "cli"});
        joinColumns.put(CARTLINEITEM_PRODUCTVARIANT, new String[]{"cli.productVariant", "pv"});
        joinColumns.put(PRODUCTVARIANT_PRODUCT, new String[]{"pv.product", "prod"});
        joinColumns.put(PRODUCT_PRODUCTVARIANT, new String[]{"prod.productVariants", "pv2"});
        joinColumns.put(USER_ADDRESS, new String[]{"user.addresses", "addr"});
        joinColumns.put(ADDRESS_PIN, new String[]{"addr.pincode", "pin"});
        joinColumns.put(PIN_ZONE, new String[]{"pin.zone", "zone"});
        joinColumns.put(PRODUCT_CATEGORIES, new String[]{"prod.categories", "cats"});
    }

    @Value("#{hkEnvProps['" + Keys.Env.debug_mode + "']}")
    private static String notOnProduction;

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
    private List<Long> storeIds;
    private boolean fetchMinimumRequiredData = false;

    // variables used for processing DetachedCriteria
    private boolean orderCriteria = false,
            cliCriteria = false,
            pvCriteria = false,
            prodCriteria = false,
            addrCriteria = false,
            pinCriteria = false,
            zoneCriteria = false,
            pv2Criteria = false,
            rolesCriteria = false,
            categoryCriteria = false,
            userReportCriteria = false,
            userStoreCriteria = false;

    public DetachedCriteria getSearchCriteria(boolean minInfo) {
        return getCriteriaFromBaseCriteria(fetchMinimumRequiredData);
    }


    private DetachedCriteria getCriteriaFromBaseCriteria(boolean fetchMinimumRequiredData) {

        DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class, "user");

        if (HKCollectionUtils.isNotBlank(emails)) {
            userCriteria.add(Restrictions.in("user.email", emails));
        } else {
            userCriteria.add(Restrictions.isNotNull("user.email"));
        }

        if (HKCollectionUtils.isNotBlank(storeIds)) {
            userCriteria = createJoin(userCriteria, userStoreCriteria, USER_STORE);
            userCriteria.add(Restrictions.in("store.id", storeIds));
        }

        if (userOrderCount != null) {
            userCriteria = createJoin(userCriteria, userReportCriteria, USER_REPORT);
            SimpleExpression se = createUserOrderRestriction(userOrderCount, equality);
            userCriteria.add(se);
        }

        if (verified != null) {
            userCriteria = createJoin(userCriteria, rolesCriteria, USER_ROLES);
            String roleName = (verified.booleanValue()) ? "HK_USER" : "HKUNVERIFIED";
            userCriteria.add(Restrictions.eq("roles.name", roleName));
        }

        boolean prodNotBlank = HKCollectionUtils.isNotBlank(productIds);
        boolean prodVarNotBlank = HKCollectionUtils.isNotBlank(productVariantIds);
        boolean catNotBlank = HKCollectionUtils.isNotBlank(categories);

        if (prodNotBlank || prodVarNotBlank || catNotBlank) {
            userCriteria = createJoin(userCriteria, orderCriteria, USER_ORDER);
            userCriteria = createJoin(userCriteria, cliCriteria, ORDER_CARTLINEITEM);
            userCriteria = createJoin(userCriteria, pvCriteria, CARTLINEITEM_PRODUCTVARIANT);
            userCriteria = createJoin(userCriteria, prodCriteria, PRODUCTVARIANT_PRODUCT);

            if (prodNotBlank) {
                userCriteria.add(Restrictions.in("prod.id", productIds));
            } else if (prodVarNotBlank) {
                userCriteria = createJoin(userCriteria, pv2Criteria, PRODUCT_PRODUCTVARIANT);
                userCriteria.add(Restrictions.in("pv2.id", productVariantIds));
            } else if (catNotBlank) {
                userCriteria = createJoin(userCriteria, categoryCriteria, PRODUCT_CATEGORIES);

                userCriteria.add(Restrictions.in("cats.name", categories));
            }
        }

        if (HKCollectionUtils.isNotBlank(cities)) {
            userCriteria = createJoin(userCriteria, addrCriteria, USER_ADDRESS);
            userCriteria.add(Restrictions.in("addr.city", cities));
        }

        if (HKCollectionUtils.isNotBlank(states)) {
            userCriteria = createJoin(userCriteria, addrCriteria, USER_ADDRESS);
            userCriteria.add(Restrictions.in("addr.state", states));
        }

        if (HKCollectionUtils.isNotBlank(zones)) {
            userCriteria = createJoin(userCriteria, addrCriteria, USER_ADDRESS);
            userCriteria = createJoin(userCriteria, pinCriteria, ADDRESS_PIN);
            userCriteria = createJoin(userCriteria, zoneCriteria, PIN_ZONE);
            userCriteria.add(Restrictions.in("zone.id", zones));
        }

        if (fetchMinimumRequiredData) {
            ProjectionList projList = Projections.projectionList();
            if ("true".equalsIgnoreCase(notOnProduction)) {
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


    private SimpleExpression createUserOrderRestriction(Integer userOrderCount, String equality) {
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
        return this;
    }

    public UsersSearchCriteria setVerified(Boolean verified) {
        this.verified = verified;
        return this;
    }

    public UsersSearchCriteria setProductIds(List<String> productIds) {
        this.productIds = productIds;
        return this;
    }

    public UsersSearchCriteria setProductVariantIds(List<String> productVariantIds) {
        this.productVariantIds = productVariantIds;
        return this;
    }

    public UsersSearchCriteria setCities(List<String> cities) {
        this.cities = cities;
        return this;
    }

    public UsersSearchCriteria setEmails(List<String> emails) {
        this.emails = emails;
        return this;
    }


    public UsersSearchCriteria setStates(List<String> states) {
        this.states = states;
        return this;
    }

    public UsersSearchCriteria setCategories(List<String> categories) {
        this.categories = categories;
        return this;
    }

    public UsersSearchCriteria setEquality(String equality) {
        this.equality = equality;
        return this;
    }


    public UsersSearchCriteria setStoreIds(List<Long> storeIds) {
        this.storeIds = storeIds;
        return this;
    }

    public UsersSearchCriteria setUserOrderCount(Integer userOrderCount) {
        this.userOrderCount = userOrderCount;
        return this;
    }

    private DetachedCriteria createJoin(DetachedCriteria criteria, boolean criteriaBool, Short joinColumnId) {
        String[] vals = joinColumns.get(joinColumnId);
        if (vals == null || vals.length < 2) {
            return criteria;
        }
        String joinColumn = vals[0];
        String alias = vals[1];

        if (!criteriaBool) {
            criteria.createAlias(joinColumn, alias);
            criteriaBool = true;
        }
        return criteria;
    }

    public static String getNotOnProduction() {
        return notOnProduction;
    }

    /**
     * Overriding value of <code>notOnProduction</code> is for testing web services only
     *
     * @param notOnProduction
     */
    public static void setNotOnProduction(String notOnProduction) {
        UsersSearchCriteria.notOnProduction = notOnProduction;
    }
}
