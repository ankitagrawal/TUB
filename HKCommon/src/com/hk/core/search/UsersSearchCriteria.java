package com.hk.core.search;

import com.hk.constants.core.Keys;
import com.hk.domain.user.User;
import com.hk.dto.user.UserDTO;
import com.hk.util.HKCollectionUtils;
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
    private static final short USER_USERBADGEINFO = 12;
    private static final short USERBADGEINFO_BADGE = 13;


    // list of variables that might be received
    private List<String> badgeNames;
    private List<String> emails;
    private List<String> categories;
    private List<String> states;
    private List<String> cities;
    private List<String> zones;
    private List<String> productVariantIds;
    private List<String> productIds;
    private String verified;
    private Integer userOrderCount;
    private String equality = "eq"; // eq (equals) is the default value for equality
    private String gender;
    private List<Long> storeIds;

    private Set<Short> alreadyJoint = new HashSet<Short>();
    // Map values are as follows {<String:joinEntity>,<String:alias>}
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
        joinColumns.put(USER_USERBADGEINFO, new String[]{"user.userBadgeInfo", "ubi"});
        joinColumns.put(USERBADGEINFO_BADGE, new String[]{"ubi.badge", "badge"});
    }

    public DetachedCriteria getSearchCriteria() {
        alreadyJoint.clear();
        DetachedCriteria criteria = getCriteriaFromBaseCriteria();
        return criteria;
    }


    private DetachedCriteria getCriteriaFromBaseCriteria() {
        DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class, "user");

        if (gender != null) {
            if ("all".equalsIgnoreCase(gender)) {
                //do nothing - should return users who have male,female and null
            } else {
                userCriteria.add(Restrictions.eq("user.gender", gender));
            }
        }

        if (HKCollectionUtils.isNotBlank(emails)) {
            userCriteria.add(Restrictions.in("user.email", emails));
        } else {
            userCriteria.add(Restrictions.isNotNull("user.email"));
        }

        if (HKCollectionUtils.isNotBlank(badgeNames)) {
            userCriteria = createJoin(userCriteria, USER_USERBADGEINFO);
            userCriteria = createJoin(userCriteria, USERBADGEINFO_BADGE);
            userCriteria.add(Restrictions.in("badge.badgeName", badgeNames));
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
            if ("all".equalsIgnoreCase(verified)) {
                //do nothing - should return users who have HK_User,HKUnverified, and other users as well
            } else {
                userCriteria = createJoin(userCriteria, USER_ROLES);
                userCriteria.add(Restrictions.eq("roles.name", verified));
            }
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
            }
            if (catNotBlank) {
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
            userCriteria.add(Restrictions.in("zone.name", zones));
        }

        ProjectionList projList = Projections.projectionList();
        projList.add(Projections.distinct(Projections.property("user.email")), "email");
        projList.add(Projections.property("user.login"), "login");
        projList.add(Projections.property("user.name"), "name");
        projList.add(Projections.property("user.subscribedMask"), "subscribedMask");
        projList.add(Projections.property("user.unsubscribeToken"), "unsubscribeToken");
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

    public UsersSearchCriteria setZones(List<String> zones) {
        this.zones = zones;
        return this;
    }

    public UsersSearchCriteria setVerified(String verified) {
        String v = (verified != null) ? verified.trim() : null;
        this.verified = v;
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

    public UsersSearchCriteria setBadgeNames(List<String> badgeNames) {
        this.badgeNames = badgeNames;
        return this;
    }

    public UsersSearchCriteria setGender(String gender) {
        String g = (gender != null) ? gender.trim() : null;
        if (!"male".equalsIgnoreCase(g) && !"female".equalsIgnoreCase(g) && !"all".equalsIgnoreCase(g)) {
            throw new RuntimeException("Illegal gender value");
        }
        this.gender = g;
        return this;
    }


    private DetachedCriteria createJoin(DetachedCriteria criteria, Short joinColumnId) {
        Boolean criteriaBool = alreadyJoint.contains(joinColumnId);
        if (!criteriaBool) {
            Object[] vals = joinColumns.get(joinColumnId);
            if (vals == null || vals.length < 2) {
                return criteria;
            }
            String joinColumn = (String) vals[0];
            String alias = (String) vals[1];
            criteria.createAlias(joinColumn, alias);
            alreadyJoint.add(joinColumnId);
        }
        return criteria;
    }

}
