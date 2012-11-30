package com.hk.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.commons.lang.StringUtils;

import com.hk.cache.vo.UserVO;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.service.ServiceLocatorFactory;
import com.shiro.PrincipalImpl;

/**
 * @author vaibhav.adlakha
 */
public class UserCache {

    /*private static UserCache    _instance        = new UserCache();
    private UserCache           _transient;

    private Map<String, UserVO> loginToUserCache = new HashMap<String, UserVO>();
    private Map<Long, UserVO>   idToUserCache    = new HashMap<Long, UserVO>();

    @SuppressWarnings("serial")
    private Map<Long, UserVO>   idToUserLRUCache = Collections.synchronizedMap(new LinkedHashMap<Long, UserVO>(1000, 0.75f, true) {
                                                     private static final int MAX_ENTRIES = 100;

                                                     protected boolean removeEldestEntry(Map.Entry<Long, UserVO> eldest) {
                                                         return size() > MAX_ENTRIES;
                                                     }
                                                 });

    private UserService         userService;
    private SecurityManager     securityManager;

    private UserCache() {
    }

    public static UserCache getInstance() {
        return _instance;
    }

    public void addUser(UserVO userVO) {
        if (StringUtils.isNotBlank(userVO.getLogin())) {
            loginToUserCache.put(userVO.getLogin(), userVO);
        }
        if (userVO.getId() != null) {
            idToUserCache.put(userVO.getId(), userVO);
        }
    }

    public UserVO getUserById(Long userId) {
        UserVO userVO = idToUserLRUCache.get(userId);
        if (userVO == null) {
            userVO = idToUserCache.get(userId);
            if (userVO != null) {
                idToUserLRUCache.put(userId, userVO);
            }
        }

        *//**
         * if user is not in cache try and attempt to find from db
         *//*
        if (userVO == null) {
             User user = getUserService().getUserById(userId);
             userVO = new UserVO(user);
        }

        return userVO;
    }

    public UserVO getUserByLogin(String login) {

        *//**
         * if user is not in cache try and attempt to find from db
         *//*
        
         * if(userVO == null){ //User user = getUserService().get }
         

        UserVO userVO = loginToUserCache.get(login);
        if(userVO == null){
            User user = getUserService().findByLogin(login);
            userVO = new UserVO(user);
            
        }
        return userVO;
    }

    public User getAdminUser() {
        UserVO userVO = getUserById(UserService.ADMIN_USER_ID);
        return userVO.getUser();
    }

    public User getLoggedInUser() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            UserVO userVO = getUserById(getPrincipal().getId());
            if (userVO != null) {
                loggedOnUser = userVO.getUser();
            }
        }

        return loggedOnUser;
    }

    private PrincipalImpl getPrincipal() {
        return (PrincipalImpl) getSecurityManager().getSubject().getPrincipal();
    }

    public void freeze() {
        _instance = this;
    }

    public void reset() {
        _transient = new UserCache();
    }

    public UserCache getTransientCache() {
        return _transient;
    }

    public UserService getUserService() {
        if (userService == null) {
            userService = (UserService) ServiceLocatorFactory.getService(UserService.class);
        }
        return userService;
    }

    public SecurityManager getSecurityManager() {
        if (securityManager == null) {
            securityManager = (SecurityManager) ServiceLocatorFactory.getService("SecurityManager");
        }
        return securityManager;
    }
*/
    
}
