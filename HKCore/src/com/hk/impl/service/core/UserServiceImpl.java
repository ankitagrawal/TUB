package com.hk.impl.service.core;

import java.util.List;

import com.hk.constants.user.EnumSubscriptions;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.order.Order;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.user.UserDaoImpl;
import com.hk.pact.dao.user.UserCartDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.store.StoreService;
import com.hk.util.TokenUtils;
import com.shiro.PrincipalImpl;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao         userDao;
    @Autowired
    private UserCartDao     userCartDao;
    @Autowired
    private SecurityManager securityManager;
    @Autowired
    private StoreService    storeService;

    public User getUserById(Long userId) {
        return getUserDao().getUserById(userId);
    }

    public User findByUserHash(String userHash) {
        return getUserDao().findByUserHash(userHash);
    }

    public List<User> findByEmail(String email) {
        return getUserDao().findByEmail(email);
    }

    public User findByLogin(String email) {
        return getUserDao().findByLogin(email);
    }


    public boolean unsubscribeUser(String unsubscribeToken){
        User user = getUserDao().findByUnsubscribeToken(unsubscribeToken);
        if (user != null){
            user.setSubscribedMask(EnumSubscriptions.UNSUBSCRIBED.getValue());
            userDao.save(user);
            return true;
        }
        return false;
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

    public User save(User user) {
        if (user != null) {
            if (user.getCreateDate() == null)
                user.setCreateDate(BaseUtils.getCurrentTimestamp());
            if (user.getLastLoginDate() == null)
                user.setLastLoginDate(BaseUtils.getCurrentTimestamp());
            if (StringUtils.isBlank(user.getUserHash())) {
                user.setUserHash(TokenUtils.generateUserHash());
            }
            // user.setUpdateDate(BaseUtils.getCurrentTimestamp());
            if (user.getStore() == null) {
                user.setStore(getStoreService().getDefaultStore());
            }
        }
        user.setSubscribedMask(30);//Subscribe for all
        return getUserDao().save(user);
    }

    @Override
    public List<User> findByRole(Role role) {
        return getUserDao().findByRole(role);
    }

    @Override
    public Page findByRole(String name, String email, Role role, int pageNo, int perPage) {
        return getUserDao().findByRole(name, email, role, pageNo, perPage);
    }

    @Override
    public User findByLoginAndStoreId(String login, Long storeId) {
        return getUserDao().findByLoginAndStoreId(login, storeId);
    }

    public void subscribeUserForOffers(String login, boolean subscribe) {
        int subscriptionMask = 0;
        if (subscribe){
            //if user has subscribed then all the type of emails will go to him
            subscriptionMask = EnumSubscriptions.NOTIFY_ME.getValue()
                    | EnumSubscriptions.PRODUCT_REPLENISHMENT.getValue() | EnumSubscriptions.PROMOTIONAL_OFFERS.getValue() | EnumSubscriptions.NEWSLETTERS.getValue();
        }else{
            //if user has not subscribed then only this type of email will go to him
            subscriptionMask = EnumSubscriptions.NOTIFY_ME.getValue() | EnumSubscriptions.PRODUCT_REPLENISHMENT.getValue();
        }
        getUserDao().updateUserSubscription(login, subscriptionMask);
    }

    public void subscribeUserForOffers(User user, EnumSubscriptions enumSubscriptions) {
        int subscriptionMask = user.getSubscribedMask() | enumSubscriptions.getValue();
        getUserDao().updateUserSubscription(user.getLogin(), subscriptionMask);
    }

    // TODO: move such methods and methods getOrdersForUserSortedByDate to a user profile service
    /*
     * private Set<ProductVariant> getRecentlyOrderedProductVariantsForUser(User user) { Map<String, ProductVariant>
     * recentlyOrderedProductVariantsMap = new HashMap<String, ProductVariant>(); List<Order> ordersByRecentDate = new
     * ArrayList<Order>(getOrdersForUserSortedByDate(user)); Product product; ProductVariant productVariant; if
     * (!ordersByRecentDate.isEmpty()) { for (Order order : ordersByRecentDate) { CartLineItemFilter cartLineItemFilter =
     * new CartLineItemFilter(new HashSet<CartLineItem>(order.getCartLineItems())); Set<CartLineItem>
     * productCartLineItems = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).filter(); for
     * (CartLineItem cartLineItem : productCartLineItems) { productVariant = cartLineItem.getProductVariant(); product =
     * cartLineItem.getProductVariant().getProduct(); if
     * (!recentlyOrderedProductVariantsMap.containsKey(product.getId())) { if ((productVariant != null) &&
     * (productVariant.getOutOfStock() == Boolean.FALSE) && (productVariant.getDeleted() == Boolean.FALSE)) {
     * recentlyOrderedProductVariantsMap.put(product.getId(), productVariant); if
     * (recentlyOrderedProductVariantsMap.size() == 3) { break; } } } } if (recentlyOrderedProductVariantsMap.size() ==
     * 3) { break; } } } return new HashSet<ProductVariant>(recentlyOrderedProductVariantsMap.values()); }
     */

    public UserDao getUserDao() {
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

    public StoreService getStoreService() {
        return storeService;
    }

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

}
