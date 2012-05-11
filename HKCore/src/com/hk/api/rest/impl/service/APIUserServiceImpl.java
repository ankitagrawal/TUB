package com.hk.api.rest.impl.service;

import com.hk.api.rest.pact.service.APIUserService;
import com.hk.api.rest.models.user.APIUser;
import com.hk.pact.service.UserService;
import com.hk.pact.service.store.StoreService;
import com.hk.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 1, 2012
 * Time: 1:25:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class APIUserServiceImpl implements APIUserService {

  @Autowired
  UserService userService;
  @Autowired
  StoreService storeService;


  public User getHKUser(APIUser apiUser) {
    if (userExists(apiUser)) {
      return getUser(apiUser);
    } else {
      return createNewHKUser(apiUser);
    }
  }

  private User createNewHKUser(APIUser apiUser) {
    User user = new User();
    user.setEmail(apiUser.getEmail());
    user.setPasswordChecksum(apiUser.getPassword());
    user.setLogin(apiUser.getEmail());
    user.setUserHash(apiUser.getEmail());
    user.setStore(storeService.getStoreById(apiUser.getStoreId()));
    return userService.save(user);
  }

  private boolean userExists(APIUser apiUser) {
    if (apiUser != null) {
      if (userService.findByLogin(apiUser.getEmail()) != null) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  private User getUser(APIUser apiUser) {
    return userService.findByLogin(apiUser.getEmail());
  }
}
