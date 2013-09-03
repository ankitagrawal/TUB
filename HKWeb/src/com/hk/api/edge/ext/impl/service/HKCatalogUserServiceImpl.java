package com.hk.api.edge.ext.impl.service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.api.edge.ext.pact.service.HKCatalogUserService;
import com.hk.api.edge.ext.response.user.UserApiResponse;
import com.hk.constants.core.EnumRole;
import com.hk.domain.user.Permission;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;

@Service
public class HKCatalogUserServiceImpl implements HKCatalogUserService {

  @Autowired
  private UserService userService;

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
      userApiResponse.setNm(user.getName());
      userApiResponse.setBirthDt(user.getBirthDate());
      userApiResponse.setEmail(user.getEmail());
      userApiResponse.setGender(user.getGender());
      userApiResponse.setLogin(user.getLogin());
      userApiResponse.setPwd(user.getPasswordChecksum());
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
      List<String> messages = new ArrayList<String>();
      messages.add("User doesn't Exist");
      userApiResponse.setException(false);
      userApiResponse.setMsgs(messages);
    }
    return userApiResponse;
  }

  public UserService getUserService() {
    return userService;
  }
}
