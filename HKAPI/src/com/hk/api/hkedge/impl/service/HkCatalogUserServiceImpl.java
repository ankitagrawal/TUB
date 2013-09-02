package com.hk.api.hkedge.impl.service;

import com.hk.api.hkedge.pact.service.HkCatalogUserService;
import com.hk.api.hkedge.response.user.UserApiBaseResponse;
import com.hk.api.hkedge.response.user.UserApiResponse;
import com.hk.constants.core.EnumRole;
import com.hk.domain.user.Permission;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

@Service
public class HkCatalogUserServiceImpl implements HkCatalogUserService {

  @Autowired
  private UserService userService;

  public UserApiBaseResponse getLoggedInUser() {
    UserApiBaseResponse userApiBaseResponse = new UserApiBaseResponse();
    User user = null;
    try {
      user = getUserService().getLoggedInUser();
    } catch (Exception e) {
      return null;
    }
    if (user != null) {
      userApiBaseResponse.setId(user.getId());
      userApiBaseResponse.setName(user.getName());
      userApiBaseResponse.setException(false);
      Set<String> roles = new HashSet<String>();
      for (Role role : user.getRoles()) {
        roles.add(role.getName());
      }
      userApiBaseResponse.setRoles(roles);
      userApiBaseResponse.setMessage("User Logged In");
      return userApiBaseResponse;
    } else {
      userApiBaseResponse.setMessage("No Logged-in User");
      userApiBaseResponse.setId(null);
      userApiBaseResponse.setException(false);
    }
    return userApiBaseResponse;
  }

  public boolean isTempUser(Long userId) {
    if (userId == null) {
      throw new InvalidParameterException("INVALID USER ID");
    }
    User user = getUserService().getUserById(userId);
    if (user == null) {
      throw new InvalidParameterException("User Doesn't Exist");
    }
    if (user.getRoles().contains(EnumRole.TEMP_USER.toRole())) {
      return true;
    }
    return false;
  }

  public UserApiResponse getUserResponseById(Long userId) {
    if (userId == null) {
      throw new InvalidParameterException("INVALID USER ID");
    }
    User user = getUserService().getUserById(userId);
    return getUserApiResponseByUser(user);
  }

  public UserApiResponse getUserResponseByLogin(String login) {
    if (login == null || login.equals("")) {
      throw new InvalidParameterException("INVALID LOGIN");
    }
    User user = getUserService().findByLogin(login);
    return getUserApiResponseByUser(user);
  }

  private UserApiResponse getUserApiResponseByUser(User user) {
    UserApiResponse userApiResponse = new UserApiResponse();
    if (user != null) {
      userApiResponse.setException(false);
      userApiResponse.setId(user.getId());
      userApiResponse.setName(user.getName());
      userApiResponse.setBirthDate(user.getBirthDate());
      userApiResponse.setEmail(user.getEmail());
      userApiResponse.setGender(user.getGender());
      userApiResponse.setLogin(user.getLogin());
      userApiResponse.setPasswordChecksum(user.getPasswordChecksum());
      Set<String> roles = new HashSet<String>();
      Set<String> permissions = new HashSet<String>();
      if (user.getRoles() != null && user.getRoles().size() > 0) {
        for (Role role : user.getRoles()) {
          roles.add(role.getName());
          if (role.getPermissions() != null && role.getPermissions().size() > 0) {
            for (Permission permission : role.getPermissions()) {
              permissions.add(permission.getName());
            }
          }
        }
      }
      userApiResponse.setRoles(roles);
      userApiResponse.setPermissions(permissions);
    } else {
      userApiResponse.setException(false);
      userApiResponse.setId(null);
      userApiResponse.setName(null);
      userApiResponse.setRoles(null);
      userApiResponse.setMessage("User doesn't Exist");
    }
    return userApiResponse;
  }

  public UserService getUserService() {
    return userService;
  }
}
