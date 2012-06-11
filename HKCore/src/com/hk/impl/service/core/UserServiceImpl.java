package com.hk.impl.service.core;

import java.util.List;
import java.math.BigInteger;

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
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
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

  public List<EmailRecepient> getMailingListByCategory(EmailCampaign emailCampaign, Category category, int maxResult) {
        return getUserDao().getMailingListByCategory(emailCampaign, category, maxResult);
    }

  public Long getMailingListCountByCategory(EmailCampaign emailCampaign, Category category) {
        return getUserDao().getMailingListCountByCategory(emailCampaign, category);
    }

    public Page getAllMailingList(int pageNo, int perPage) {
        return getUserDao().getAllMailingList(pageNo, perPage);
    }

  public List<EmailRecepient> getAllMailingList(EmailCampaign emailCampaign, String[]roles, String[] userIds, int maxResult) {
        return getUserDao().getUserMailingList(emailCampaign, roles, userIds, maxResult);
    }

  public BigInteger getAllMailingListCount(EmailCampaign emailCampaign, String [] roles) {
        return getUserDao().getAllMailingListCount(emailCampaign, roles);
    }

  public List<EmailRecepient> getMailingListByEmailIds(EmailCampaign emailCampaign, List<String> emailList, int maxResult) {
    return getUserDao().getMailingListByEmailIds(emailCampaign,  emailList, maxResult);
  }

    public Page getAllUnverifiedMailingList(int pageNo, int perPage) {
        return getUserDao().getAllUnverifiedMailingList(pageNo, perPage);
    }
  
    public void updateIsProductBought(Order order) {
        getUserCartDao().updateIsProductBought(order);
    }
  public List<User> findAllUsersNotInEmailRecepient(int maxResult) {
    return getUserDao().findAllUsersNotInEmailRecepient(maxResult);
  }

    public User save(User user) {
        if (user != null) {
            if (user.getCreateDate() == null)
                user.setCreateDate(BaseUtils.getCurrentTimestamp());
            if (user.getLastLoginDate() == null)
                user.setLastLoginDate(BaseUtils.getCurrentTimestamp());
            if (StringUtils.isBlank(user.getUserHash()))
                user.setUserHash(TokenUtils.generateUserHash());
            user.setUpdateDate(BaseUtils.getCurrentTimestamp());
            if (user.getStore() == null) {
                user.setStore(getStoreService().getDefaultStore());
            }
        }
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

    @Override
    public User findByLoginAndStoreId(String login, Long storeId) {
        return getUserDao().findByLoginAndStoreId(login, storeId);
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
