package com.hk.core.search;

import com.hk.dto.user.UserDTO;
import com.hk.constants.core.Keys;
import com.hk.domain.user.User;
import com.hk.util.HKCollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

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

    // Map values are as follows {<String:joinEntity>,<String:alias>,<Boolean:joinCreatedAlready>}
    private Map<Short, Object[]> joinColumns = new HashMap<Short, Object[]>();

    private void createMap() {
        joinColumns.put(USER_STORE, new Object[]{"user.store", "store", Boolean.FALSE});
        joinColumns.put(USER_REPORT, new Object[]{"user.report", "report", Boolean.FALSE});
        joinColumns.put(USER_ROLES, new Object[]{"user.roles", "roles", Boolean.FALSE});
        joinColumns.put(USER_ORDER, new Object[]{"user.orders", "orders", Boolean.FALSE});
        joinColumns.put(ORDER_CARTLINEITEM, new Object[]{"orders.cartLineItems", "cli", Boolean.FALSE});
        joinColumns.put(CARTLINEITEM_PRODUCTVARIANT, new Object[]{"cli.productVariant", "pv", Boolean.FALSE});
        joinColumns.put(PRODUCTVARIANT_PRODUCT, new Object[]{"pv.product", "prod", Boolean.FALSE});
        joinColumns.put(PRODUCT_PRODUCTVARIANT, new Object[]{"prod.productVariants", "pv2", Boolean.FALSE});
        joinColumns.put(USER_ADDRESS, new Object[]{"user.addresses", "addr", Boolean.FALSE});
        joinColumns.put(ADDRESS_PIN, new Object[]{"addr.pincode", "pin", Boolean.FALSE});
        joinColumns.put(PIN_ZONE, new Object[]{"pin.zone", "zone", Boolean.FALSE});
        joinColumns.put(PRODUCT_CATEGORIES, new Object[]{"prod.categories", "cats", Boolean.FALSE});
    }

    public DetachedCriteria getSearchCriteria() {
        DetachedCriteria criteria = getCriteriaFromBaseCriteria();
        resetMap();
        return criteria;
    }


    private DetachedCriteria getCriteriaFromBaseCriteria() {

        DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class, "user");

        if (HKCollectionUtils.isNotBlank(emails)) {
            userCriteria.add(Restrictions.in("user.email", emails));
        } else {
            userCriteria.add(Restrictions.isNotNull("user.email"));
        }

        if (HKCollectionUtils.isNotBlank(storeIds)) {
            userCriteria = createJoin(userCriteria, USER_STORE);
            userCriteria.add(Restrictions.in("store.id", storeIds));
        }

        if (userOrderCount != null) {
            userCriteria = createJoin(userCriteria, USER_REPORT);
            SimpleExpression se = createUserOrderRestriction(userOrderCount, equality);
            userCriteria.add(se);
        }

        if (verified != null) {
            userCriteria = createJoin(userCriteria, USER_ROLES);
            String roleName = (verified) ? "HK_USER" : "HKUNVERIFIED";
            userCriteria.add(Restrictions.eq("roles.name", roleName));
        }

        boolean prodNotBlank = HKCollectionUtils.isNotBlank(productIds);
        boolean prodVarNotBlank = HKCollectionUtils.isNotBlank(productVariantIds);
        boolean catNotBlank = HKCollectionUtils.isNotBlank(categories);

        if (prodNotBlank || prodVarNotBlank || catNotBlank) {
            userCriteria = createJoin(userCriteria, USER_ORDER);
            userCriteria = createJoin(userCriteria, ORDER_CARTLINEITEM);
            userCriteria = createJoin(userCriteria, CARTLINEITEM_PRODUCTVARIANT);
            userCriteria = createJoin(userCriteria, PRODUCTVARIANT_PRODUCT);

            if (prodNotBlank) {
                userCriteria.add(Restrictions.in("prod.id", productIds));
            } else if (prodVarNotBlank) {
                userCriteria = createJoin(userCriteria, PRODUCT_PRODUCTVARIANT);
                userCriteria.add(Restrictions.in("pv2.id", productVariantIds));
            } else if (catNotBlank) {
                userCriteria = createJoin(userCriteria, PRODUCT_CATEGORIES);

                userCriteria.add(Restrictions.in("cats.name", categories));
            }
        }

        if (HKCollectionUtils.isNotBlank(cities)) {
            userCriteria = createJoin(userCriteria, USER_ADDRESS);
            userCriteria.add(Restrictions.in("addr.city", cities));
        }

        if (HKCollectionUtils.isNotBlank(states)) {
            userCriteria = createJoin(userCriteria, USER_ADDRESS);
            userCriteria.add(Restrictions.in("addr.state", states));
        }

        if (HKCollectionUtils.isNotBlank(zones)) {
            userCriteria = createJoin(userCriteria, USER_ADDRESS);
            userCriteria = createJoin(userCriteria, ADDRESS_PIN);
            userCriteria = createJoin(userCriteria, PIN_ZONE);
            userCriteria.add(Restrictions.in("zone.id", zones));
        }

        ProjectionList projList = Projections.projectionList();
        if ("true".equalsIgnoreCase(notOnProduction)) {
            projList.add(Projections.distinct(Projections.property("user.login")), "login");
            projList.add(Projections.property("user.email"), "email");
            projList.add(Projections.property("user.name"), "name");
            projList.add(Projections.property("user.subscribedMask"), "subscribedMask");
            projList.add(Projections.property("user.unsubscribeToken"), "unsubscribeToken");
        } else {
            projList.add(Projections.distinct(Projections.property("user.email")), "email");
            projList.add(Projections.property("user.login"), "login");
            projList.add(Projections.property("user.name"), "name");
            projList.add(Projections.property("user.subscribedMask"), "subscribedMask");
            projList.add(Projections.property("user.unsubscribeToken"), "unsubscribeToken");
        }
        userCriteria.setProjection(projList);
        userCriteria.setResultTransformer(Transformers.aliasToBean(UserDTO.class));

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

    private DetachedCriteria createJoin(DetachedCriteria criteria, Short joinColumnId) {
        Object[] vals = joinColumns.get(joinColumnId);
        if (vals == null || vals.length < 3) {
            return criteria;
        }
        String joinColumn = (String) vals[0];
        String alias = (String) vals[1];
        Boolean criteriaBool = (Boolean) vals[2];
        if (!criteriaBool) {
            criteria.createAlias(joinColumn, alias);
            criteriaBool = Boolean.TRUE;
            vals[2] = criteriaBool;
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

    public UsersSearchCriteria() {
        createMap();
    }

    private void resetMap() {
        Collection<Object[]> vals = joinColumns.values();
        Iterator<Object[]> it = vals.iterator();
        while (it.hasNext()) {
            Object[] ob = it.next();
            ob[2] = Boolean.FALSE;
        }
    }
}
