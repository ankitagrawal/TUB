package com.hk.impl.dao.user;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.DateUtils;
import com.hk.cache.RoleCache;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.dto.user.UserFilterDto;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.user.UserDao;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Author: Kani Date: Sep 1, 2008
 */
@SuppressWarnings("unchecked")
@Repository
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

    @Autowired
    private RoleDao                 roleDao;

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    public User getUserById(Long userId) {
        return get(User.class, userId);
    }

    public User save(User user) {
        // set defaults
        /*
         * if (user != null) { if (user.getCreateDate() == null) user.setCreateDate(BaseUtils.getCurrentTimestamp()); if
         * (user.getLastLoginDate() == null) user.setLastLoginDate(BaseUtils.getCurrentTimestamp()); if
         * (StringUtils.isBlank(user.getUserHash())) user.setUserHash(TokenUtils.generateUserHash());
         * user.setUpdateDate(BaseUtils.getCurrentTimestamp()); }
         */

        return (User) super.save(user);
    }

    public List<User> findByEmail(String email) {
        return getSession().getNamedQuery("user.findByEmail").setString("email", email).list();
    }

    public User findByLogin(String login) {

        return (User) getSession().getNamedQuery("user.findByLogin").setString("login", login).uniqueResult();
    }

    //todo: marut - ideally store id also has to be passed here
    public User findByUnsubscribeToken(String unsubscribeToken) {
        Criteria criteria = getSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("unsubscribeToken", unsubscribeToken));
        return (User) criteria.uniqueResult();
    }

    public User findByLoginAndStoreId(String login, Long storeId) {
        if (storeId != null && !storeId.equals(1L)) {
            login += "||" + storeId;
        }

        return (User) getSession().createQuery("from User u where u.login = :login and u.store.id = :storeId").setParameter("login", login).setParameter("storeId", storeId).uniqueResult();
    }

    public User findByEmailAndPassword(String email, String password) {
        return (User) getSession().getNamedQuery("user.findByEmailAndPassword").setString("email", email).setString("passwordEncrypted", BaseUtils.passwordEncrypt(password)).uniqueResult();
    }

    public User findByUserHandle(String userHandle) {
        Criteria criteria = getSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("userHandle", userHandle));
        return (User) criteria.uniqueResult();
    }

    public User findByUserHash(String userHash) {
        return (User) getSession().createQuery("from User u where u.userHash = :userHash").setString("userHash", userHash).uniqueResult();
    }

    public List<User> findByRole(Role role) {
        Criteria criteria = getSession().createCriteria(User.class);
        Criteria roleCriteria = criteria.createCriteria("roles");
        roleCriteria.add(Restrictions.eq("name", role.getName()));
        criteria.addOrder(org.hibernate.criterion.Order.asc("name"));
        return criteria.list();
    }

    public Page findByRole(String name, String email, Role role, int pageNo, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        if (StringUtils.isNotBlank(email)) {
            criteria.add(Restrictions.eq("email", email));
        }
        if (StringUtils.isNotBlank(name)) {
            criteria.add(Restrictions.like("name", "%" + name + "%"));
        }
        DetachedCriteria roleCriteria = criteria.createCriteria("roles");
        roleCriteria.add(Restrictions.eq("name", role.getName()));
        criteria.addOrder(org.hibernate.criterion.Order.desc("id"));
        return list(criteria, pageNo, perPage);
    }

    public Page listUsersByRole(Role role, int page, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        DetachedCriteria roleCriteria = criteria.createCriteria("roles");
        roleCriteria.add(Restrictions.eq("name", role.getName()));
        criteria.addOrder(org.hibernate.criterion.Order.asc("email"));
        return list(criteria, page, perPage);
    }

    public Page search(UserFilterDto userFilterDto, int pageNo, int perPage) {
        userFilterDto.setCreateDateFrom(DateUtils.getStartOfDay(userFilterDto.getCreateDateFrom()));
        userFilterDto.setCreateDateTo(DateUtils.getEndOfDay(userFilterDto.getCreateDateTo()));
        userFilterDto.setLastLoginDateFrom(DateUtils.getStartOfDay(userFilterDto.getLastLoginDateFrom()));
        userFilterDto.setLastLoginDateTo(DateUtils.getEndOfDay(userFilterDto.getLastLoginDateTo()));

        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
        if (userFilterDto.getId() != null) {
            criteria.add(Restrictions.eq("id", userFilterDto.getId()));
        }
        if (StringUtils.isNotBlank(userFilterDto.getLogin())) {
            criteria.add(Restrictions.eq("login", userFilterDto.getLogin()));
        }
        if (StringUtils.isNotBlank(userFilterDto.getEmail())) {
            criteria.add(Restrictions.like("email", "%" + userFilterDto.getEmail() + "%"));
        }
        if (StringUtils.isNotBlank(userFilterDto.getName())) {
            criteria.add(Restrictions.like("name", "%" + userFilterDto.getName() + "%"));
        }
        if (StringUtils.isNotBlank(userFilterDto.getUserHash())) {
            criteria.add(Restrictions.eq("userHash", userFilterDto.getUserHash()));
        }
        if (userFilterDto.getCreateDateFrom() != null) {
            criteria.add(Restrictions.ge("createDate", userFilterDto.getCreateDateFrom()));
        }
        if (userFilterDto.getCreateDateTo() != null) {
            criteria.add(Restrictions.le("createDate", userFilterDto.getCreateDateTo()));
        }
        if (userFilterDto.getLastLoginDateFrom() != null) {
            criteria.add(Restrictions.ge("lastLoginDate", userFilterDto.getLastLoginDateFrom()));
        }
        if (userFilterDto.getLastLoginDateTo() != null) {
            criteria.add(Restrictions.le("lastLoginDate", userFilterDto.getLastLoginDateTo()));
        }

        return list(criteria, pageNo, perPage);
    }

    public List<User> referredUserList(User user) {
        return getSession().createQuery("select u from User u where u.referredBy =:user").setParameter("user", user).list();
    }

    public User findByOfferInstance(OfferInstance offerInstance) {
        Criteria criteria = getSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("offerInstance", offerInstance));
        return (User) criteria.uniqueResult();
    }

    public Page getAllMailingList(int pageNo, int perPage) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(User.class);
        DetachedCriteria rolesCriteria = criteria1.createCriteria("roles");
        rolesCriteria.add(Restrictions.in("name", Arrays.asList(RoleConstants.HK_USER)));
        return list(criteria1, pageNo, perPage);
    }

    public Page getAllUnverifiedMailingList(int pageNo, int perPage) {
        DetachedCriteria criteria1 = DetachedCriteria.forClass(User.class);
        DetachedCriteria rolesCriteria = criteria1.createCriteria("roles");
        rolesCriteria.add(Restrictions.in("name", Arrays.asList(RoleConstants.HK_UNVERIFIED)));
        return list(criteria1, pageNo, perPage);
    }

    public List<User> getAllMissingUsersLastOrderId(Integer missingSinceDays) {
        Calendar missingSinceDate = Calendar.getInstance();
        missingSinceDate.setTime(new Date());
        missingSinceDate.add(Calendar.DAY_OF_YEAR, -missingSinceDays);

        String usersOrderedDuringThePeriodQuery = "select distinct bo.user from Order bo where date(bo.payment.paymentDate) > :missingSinceDate and bo.orderStatus.id not in (10,99)";
        List<User> usersOrderedDuringThePeriodList = getSession().createQuery(usersOrderedDuringThePeriodQuery).setParameter("missingSinceDate", missingSinceDate.getTime()).list();

        if (usersOrderedDuringThePeriodList != null && !usersOrderedDuringThePeriodList.isEmpty()) {
            logger.debug("user who ordered in the past specified days " + usersOrderedDuringThePeriodList.size());
            String query = "select distinct bo.user from Order bo where bo.orderStatus.id not in (10,99)";
            List<User> totalUsersWhoOrdered = getSession().createQuery(query).list();
            if (totalUsersWhoOrdered != null && !totalUsersWhoOrdered.isEmpty()) {
                logger.debug("all users who ordered  " + totalUsersWhoOrdered.size());
                totalUsersWhoOrdered.removeAll(usersOrderedDuringThePeriodList);
                return totalUsersWhoOrdered;
            }
        }

        return null;
    }

    public Page getMailingList(Category category, int pageNo, int perPage) {
        DetachedCriteria criteria = DetachedCriteria.forClass(User.class);

        String query = "select distinct li.shippingOrder.baseOrder.user.id from LineItem li left join li.sku.productVariant.product.categories c"
                + " left join li.shippingOrder.baseOrder.user.roles r " + "where c in (:categoryList) " + "and r in (:roleList)";

        List<Role> applicableRoles = new ArrayList<Role>();
        applicableRoles.add(RoleCache.getInstance().getRoleByName(EnumRole.HK_USER).getRole());
        applicableRoles.add(RoleCache.getInstance().getRoleByName(EnumRole.HK_UNVERIFIED).getRole());

        /*List<Long> userIds = getSession().createQuery(query).setParameterList("categoryList", Arrays.asList(category)).setParameterList("roleList",
                Arrays.asList(getRoleDao().getRoleByName(EnumRole.HK_USER), getRoleDao().getRoleByName(EnumRole.HK_UNVERIFIED))).list();*/
        
        List<Long> userIds = getSession().createQuery(query).setParameterList("categoryList", Arrays.asList(category)).setParameterList("roleList",
                applicableRoles).list();

        criteria.add(Restrictions.in("id", userIds));
        return list(criteria, pageNo, perPage);
    }

    public void updateUserSubscription(String login,int subscriptionMask) {
         User user = findByLogin(login);
         user.setSubscribedMask(subscriptionMask);
         save(user);
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

}
