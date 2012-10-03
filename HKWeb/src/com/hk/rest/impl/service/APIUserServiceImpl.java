package com.hk.rest.impl.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import com.hk.pact.service.store.StoreService;
import com.hk.rest.models.user.APIUser;
import com.hk.rest.pact.service.APIUserService;


/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 1, 2012
 * Time: 1:25:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class APIUserServiceImpl implements APIUserService {

    @Autowired
    UserService userService;
    @Autowired
    StoreService storeService;


    public User getHKUser(APIUser apiUser) {
        if (userExists(apiUser)) {
            User user=getUser(apiUser);
            if(StringUtils.isBlank(user.getName())){
                user.setName(apiUser.getName());
            }
            user=userService.save(user);
            return user;
        } else {
            return createNewHKUser(apiUser);
        }
    }

    public User getHKUser(User user){
        if (userExists(user)) {
            return getUser(user);
        } else {
            User hkUser = new User();
            hkUser.setName(user.getName());
            hkUser.setEmail(user.getEmail());
            hkUser.setPasswordChecksum(user.getPasswordChecksum());
            hkUser.setStore(user.getStore());
            hkUser.setLogin(user.getEmail()+"||"+user.getStore().getId());
            hkUser=userService.save(hkUser);
            return hkUser;
        }
    }

    private User createNewHKUser(APIUser apiUser) {
        User user = new User();
        user.setName(apiUser.getName());
        user.setEmail(apiUser.getEmail());
        user.setPasswordChecksum(apiUser.getPassword());
        user.setLogin(apiUser.getEmail()+"||"+apiUser.getStoreId());
        user.setStore(storeService.getStoreById(apiUser.getStoreId()));
        return userService.save(user);
    }

    private boolean userExists(APIUser apiUser) {
        if (apiUser != null) {
            if (userService.findByLoginAndStoreId(apiUser.getEmail(), apiUser.getStoreId()) != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean userExists(User user) {
        if (user != null) {
            if (userService.findByLoginAndStoreId(user.getEmail(), user.getStore().getId()) != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private User getUser(APIUser apiUser) {
        return userService.findByLoginAndStoreId(apiUser.getEmail(), apiUser.getStoreId());
    }

    private User getUser(User user) {
        return userService.findByLoginAndStoreId(user.getEmail(), user.getStore().getId());
    }
}
