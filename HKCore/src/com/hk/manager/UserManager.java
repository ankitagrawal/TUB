package com.hk.manager;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.HttpRequestAndSessionConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.domain.TempToken;
import com.hk.domain.analytics.TrafficTracking;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.matcher.CartLineItemMatcher;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.Address;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.exception.HealthkartSignupException;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.core.TempTokenDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.UserSessionService;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.service.ServiceLocatorFactory;
import com.hk.util.TokenUtils;
import com.hk.web.filter.WebContext;
import com.shiro.PrincipalImpl;

@Component
public class UserManager {

    // private static final int MAX_UNVERIFIED_DAYS = 15;
    private static final int    ACTIVATION_LINK_EXPIRY_DAYS = 100;

    private static Logger       logger                      = LoggerFactory.getLogger(UserManager.class);

    @Autowired
    private UserService         userService;
    @Autowired
    private UserSessionService  userSessionService;

    @Autowired
    private RoleService         roleService;
    @Autowired
    private EmailManager        emailManager;
    @Autowired
    private LinkManager         linkManager;

    @Autowired
    private TempTokenDao        tempTokenDao;
    @Autowired
    private CartLineItemDao     cartLineItemDao;
    @Autowired
    private OrderDao            orderDao;
    @Autowired
    private OfferInstanceDao    offerInstanceDao;
    @Autowired
    private AddressService      addressDao;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private BaseDao             baseDao;

    // Please do not add @Autowired has been taken care of in getter .
    private OrderManager        orderManager;

    public UserLoginDto login(String email, String password, boolean rememberMe) throws HealthkartLoginException {
        /**
         * Check whether any user is logged in or not. if yes then if the user is TEMP_USER then save a reference to
         * this user we're doing this so that any data that the temp user has created, like images, cart etc are
         * automatically transferred to the correct logged in user. after the login is successful, this data
         * merge/transfer will be done
         */
        User tempUser = null;
        if (getPrincipal() != null) {
            User currentUser = getUserService().getUserById(getPrincipal().getId());
            Role tempUserRole = getRoleService().getRoleByName(RoleConstants.TEMP_USER);
            if (currentUser != null && currentUser.getRoles().contains(tempUserRole)) {
                tempUser = currentUser;
            }
        }

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(email, password);
        usernamePasswordToken.setRememberMe(rememberMe);

        try {
            SecurityUtils.getSubject().login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            // Note: if the login fails, existing subject is still retained
            throw new HealthkartLoginException(e);
        }

        getUserSessionService().onLoginUser();
        /*
         * Here we are checking whether user has verified his email account or not. If user has not yet verified even
         * after MAX_UNVERIFIED_DAYS limit exceed then we will change the user's role to ITV_DEACTIVATED
         */
        User user = getUserService().getUserById(getPrincipal().getId());

        if (user != null) {
            // change last login date before proceeding
            user.setLastLoginDate(BaseUtils.getCurrentTimestamp());

            // save user to save the last login date and roles change etc if any.
            getUserService().save(user);

            // Set UserId in Traffic Tracking
            TrafficTracking trafficTracking = (TrafficTracking) WebContext.getRequest().getSession().getAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING);
            if (trafficTracking != null) {
                trafficTracking.setUserId(user.getId());
                getBaseDao().save(trafficTracking);
            }
        }
        /**
         * Now to transfer any data created by a temp user.
         */
        if (tempUser != null) {
            // Merge order/address/offer instances of Guest user
            this.mergeUsers(tempUser, user);
        }

        return new UserLoginDto(user);
    }

    private PrincipalImpl getPrincipal() {
        return (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
    }

    @SuppressWarnings("deprecation")
    public Warehouse getAssignedWarehouse() {
        if (getPrincipal() != null) {
            User currentUser = getUserService().getUserById(getPrincipal().getId());
            if (currentUser != null && !currentUser.getWarehouses().isEmpty()) {
                return currentUser.getWarehouses().iterator().next();
            }
        }
        return null;
    }

    public User signup(String email, String name, String password, User referredBy) throws HealthkartSignupException {
        return signup(email, name, password, referredBy, null);
    }

    public User signup(String email, String name, String password, User referredBy, String roleName) throws HealthkartSignupException {
        if (getUserService().findByLogin(email) != null) {
            throw new HealthkartSignupException("User already exists by this email");
        }

        User user;
        User tempUser = null;
        /**
         * Check whether any user is logged in or not. if yes then if the user is TEMP_USER then remove the role
         * TEMP_USER and proceed. else create a new User object and proceeed. we're doing this so that any data that the
         * temp user has created, like images, cart etc are automatically transferred to the new account.
         */
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            Role tempUserRole = getRoleService().getRoleByName(RoleConstants.TEMP_USER);
            if (user != null && user.getRoles().contains(tempUserRole)) {
                tempUser = user;
                user.getRoles().remove(tempUserRole);
            } else {
                user = new User();
            }

            // Set UserId in Traffic Tracking
            TrafficTracking trafficTracking = (TrafficTracking) WebContext.getRequest().getSession().getAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING);
            if (trafficTracking != null) {
                trafficTracking.setUserId(user.getId());
                getBaseDao().save(trafficTracking);
            }

        } else {
            user = new User();
        }

        user.setName(name);
        user.setLogin(email);
        user.setEmail(email);
        user.setPasswordChecksum(BaseUtils.passwordEncrypt(password));
        user.setUnsubscribeToken(TokenUtils.getTokenToUnsubscribeWommEmail(email));

        // to prevent overwriting referredBy we are checking for null
        // if this user is already referred by any other user then we will not override
        if (user.getReferredBy() == null) {
            user.setReferredBy(referredBy);
        }

        if (StringUtils.isBlank(roleName)) {
            Role role = getRoleService().getRoleByName(RoleConstants.HK_UNVERIFIED);
            roleName = role.getName();
            user.getRoles().add(role);
        } else {
            user.getRoles().add(getRoleService().getRoleByName(roleName));
        }
        user = getUserService().save(user);

        /**
         * Now to transfer any data created by a temp user.
         */
        if (tempUser != null) {
            // Merge order/address/offer instances of Guest user
            this.mergeUsers(tempUser, user);
        }

        // logging in the just created user
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(email, password);
        usernamePasswordToken.setRememberMe(true);

        SecurityUtils.getSubject().login(usernamePasswordToken);
        getUserSessionService().onLoginUser();

        user.setPassword(password); // is required by the email template.

        // generate user activation link
        if (RoleConstants.HK_UNVERIFIED.equals(roleName)) {
            String userActivationLink = getUserActivationLink(user);
            getEmailManager().sendWelcomeEmail(user, userActivationLink);
        }

        return user;
    }

    public String getUserActivationLink(User user) {
        TempToken tempToken = getTempTokenDao().createNew(user, ACTIVATION_LINK_EXPIRY_DAYS);
        return getLinkManager().getUserActivationLink(tempToken);
    }

    public User createAndLoginAsGuestUser(String email, String name) {
        User user = createGuestUser(email, name);

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getLogin(), user.getLogin());
        usernamePasswordToken.setRememberMe(true);

        SecurityUtils.getSubject().login(usernamePasswordToken);
        return user;
    }

    public User createGuestUser(String email, String name) {
        User user = new User();
        user.setName(StringUtils.isBlank(name) ? "Guest" : name);
        String randomLogin = TokenUtils.generateGuestLogin();
        user.setLogin(randomLogin);
        user.setEmail(email);
        user.setUnsubscribeToken(TokenUtils.getTokenToUnsubscribeWommEmail(email));
        user.setPasswordChecksum(BaseUtils.passwordEncrypt(randomLogin));
        user.getRoles().add(getRoleService().getRoleByName(RoleConstants.TEMP_USER));
        user = getUserService().save(user);

        // ADD In Cookie -there is no need to put this in a cookie
        /*
         * Cookie cookie = new Cookie(HealthkartConstants.Cookie.tempHealthKartUser, user.getUserHash());
         * cookie.setPath("/"); cookie.setMaxAge(30 * 24 * 60 * 60); HttpServletResponse httpResponse =
         * WebContext.getResponse(); httpResponse.addCookie(cookie); logger.debug("Added Cookie for New Temp
         * User="+user.getUserHash());
         */
        return user;
    }

    @Transactional
    public void mergeUsers(User srcUser, User dstUser) {
        Order guestOrder = getOrderManager().getOrCreateOrder(srcUser);
        Order loggedOnUserOrder = getOrderManager().getOrCreateOrder(dstUser);
        Set<CartLineItem> guestLineItems = guestOrder.getCartLineItems();
        for (CartLineItem guestLineItem : guestLineItems) {
            // The variant is not added in user account already
            CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher();

            if (cartLineItemMatcher.addProductVariant(guestLineItem.getProductVariant()).addCartLineItemTypeId(guestLineItem.getLineItemType().getId()).match(
                    loggedOnUserOrder.getCartLineItems()) == null) {
                if (guestLineItem.getQty() > 0) {
                    guestLineItem.setOrder(loggedOnUserOrder);
                    getCartLineItemDao().save(guestLineItem);
                    if (guestLineItem.getLineItemType().getId().longValue() == EnumCartLineItemType.Subscription.getId().longValue()) {
                        Subscription subscription = subscriptionService.getSubscriptionFromCartLineItem(guestLineItem);
                        subscription.setBaseOrder(loggedOnUserOrder);
                        subscription.setUser(dstUser);
                        subscriptionService.save(subscription);
                    }
                }
            }
        }
        getOrderDao().save(loggedOnUserOrder);

        // Offer Instances
        List<OfferInstance> guestOfferInsatnces = srcUser.getOfferInstances();
        for (OfferInstance guestOfferInsatnce : guestOfferInsatnces) {
            guestOfferInsatnce.setUser(dstUser);
            getOfferInstanceDao().save(guestOfferInsatnce);
        }

        // Address
        List<Address> guestAddresses = srcUser.getAddresses();
        for (Address guestAddress : guestAddresses) {
            guestAddress.setUser(dstUser);
            getAddressDao().save(guestAddress);
        }
    }

    public Long getProcessedOrdersCount(User user) {
        Long count = 0L;
        List<Long> validOrderStatusIds = Arrays.asList(EnumOrderStatus.Delivered.getId());
        for (Order order : user.getOrders()) {
            if (validOrderStatusIds.contains(order.getOrderStatus().getId())) {
                count++;
            }
        }
        return count;
    }

    public Double getApplicableOfferPriceForUser(ProductVariant pv) {
        if (pv != null) {
            if (getPrincipal() != null) {
                User sessionUser = getUserService().getUserById(getPrincipal().getId());
                if (sessionUser != null) {
                    return pv.getHkPrice(sessionUser.getRoleStrings());
                }
            }
            return pv.getHkPrice(null);
        }
        return 0D;
    }

    public Double getApplicableOfferPriceForUser(ProductVariant pv, Order order) {
        if (order != null) {
            User user = order.getUser();
            if (user != null) {
                return pv.getHkPrice(user.getRoleStrings());
            }
        }
        return pv.getHkPrice(null);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public OrderManager getOrderManager() {
        if (orderManager == null) {
            orderManager = (OrderManager) ServiceLocatorFactory.getService("OrderManager");
        }
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public LinkManager getLinkManager() {
        return linkManager;
    }

    public void setLinkManager(LinkManager linkManager) {
        this.linkManager = linkManager;
    }

    public TempTokenDao getTempTokenDao() {
        return tempTokenDao;
    }

    public void setTempTokenDao(TempTokenDao tempTokenDao) {
        this.tempTokenDao = tempTokenDao;
    }

    public CartLineItemDao getCartLineItemDao() {
        return cartLineItemDao;
    }

    public void setCartLineItemDao(CartLineItemDao cartLineItemDao) {
        this.cartLineItemDao = cartLineItemDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public OfferInstanceDao getOfferInstanceDao() {
        return offerInstanceDao;
    }

    public void setOfferInstanceDao(OfferInstanceDao offerInstanceDao) {
        this.offerInstanceDao = offerInstanceDao;
    }

    public AddressService getAddressDao() {
        return addressDao;
    }

    public void setAddressDao(AddressService addressDao) {
        this.addressDao = addressDao;
    }

    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public UserSessionService getUserSessionService() {
        return userSessionService;
    }

}
