package com.hk.service.impl;

import java.util.List;

import org.apache.shiro.mgt.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.hk.dao.user.UserCartDao;
import com.hk.dao.user.UserDaoImpl;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.order.Order;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.service.UserService;
import com.shiro.PrincipalImpl;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDaoImpl         userDao;
    @Autowired
    private UserCartDao     userCartDao;
    //@Autowired
    private SecurityManager securityManager;

    public User getUserById(Long userId) {
        return userDao.getUserById(userId);
    }

    public User findByUserHash(String userHash) {
        return getUserDao().findByUserHash(userHash);
    }

    public List<User> findByEmail(String email) {
        return getUserDao().findByEmail(email);
    }

    public User findByLogin(String email) {
        return userDao.findByLogin(email);
    }

    public User getAdminUser() {
        return getUserDao().getUserById(ADMIN_USER_ID);
    }

    public User getLoggedInUser() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserDao().getUserById(getPrincipal().getId());
        }

        // TODO: #warehouse this can be null, fix this
        return loggedOnUser;
    }

    public Warehouse getWarehouseForLoggedInUser() {
        User loggedOnUser = getLoggedInUser();
        if (loggedOnUser != null) {
            return loggedOnUser.getSelectedWarehouse();
        }
        return null;
    }

    private PrincipalImpl getPrincipal() {
        return (PrincipalImpl) getSecurityManager().getSubject().getPrincipal();
    }

    public Page getMailingList(Category category, int pageNo, int perPage) {
        return getUserDao().getMailingList(category, pageNo, perPage);
    }

    public Page getAllMailingList(int pageNo, int perPage) {
        return getUserDao().getAllMailingList(pageNo, perPage);
    }

    public Page getAllUnverifiedMailingList(int pageNo, int perPage) {
        return getUserDao().getAllUnverifiedMailingList(pageNo, perPage);
    }

    public void updateIsProductBought(Order order) {
        getUserCartDao().updateIsProductBought(order);
    }

    @Transactional(readOnly = false)
    public User save(User user) {
        return getUserDao().save(user);
    }

    @Override
    public List<User> findByRole(Role role) {
        return getUserDao().findByRole(role);
    }

    @Override
    public Page findByRole(Role role, int pageNo, int perPage) {
        return getUserDao().findByRole(role, pageNo, perPage);
    }

    public UserDaoImpl getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    public UserCartDao getUserCartDao() {
        return userCartDao;
    }

    public void setUserCartDao(UserCartDao userCartDao) {
        this.userCartDao = userCartDao;
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

}
