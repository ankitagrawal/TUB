package com.hk.api.edge.integration.impl.service.user;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.hk.api.edge.integration.response.user.UserTempResponse;
import com.hk.pact.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.api.edge.integration.pact.service.user.HybridUserService;
import com.hk.api.edge.integration.response.user.UserResponseFromHKR;
import com.hk.constants.core.EnumRole;
import com.hk.domain.user.Permission;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;

@Service
public class HybridUserServiceImpl implements HybridUserService {

  @Autowired
  private UserService userService;
  @Autowired
  private OrderService orderService;

  public UserTempResponse isTempUser(Long userId) {
    if (userId == null) {
      throw new InvalidParameterException("INVALID USER ID");
    }
    UserTempResponse userTempResponse = new UserTempResponse();
    User user = getUserService().getUserById(userId);
    if (user == null) {
      throw new InvalidParameterException("User Doesn't Exist");
    }
    if (user.getRoles().contains(EnumRole.TEMP_USER.toRole())) {
      userTempResponse.setException(false);
      userTempResponse.setTempUser(true);
      return userTempResponse;
    }
    userTempResponse.setException(false);
    userTempResponse.setTempUser(false);
    return userTempResponse;
  }

  public UserResponseFromHKR getUserResponseById(Long userId) {
    if (userId == null) {
      throw new InvalidParameterException("INVALID USER ID");
    }
    User user = getUserService().getUserById(userId);
    return getUserApiResponseByUser(user);
  }

  public UserResponseFromHKR getUserResponseByLogin(String login) {
    if (login == null || login.equals("")) {
      throw new InvalidParameterException("INVALID LOGIN");
    }
    User user = getUserService().findByLogin(login);
    return getUserApiResponseByUser(user);
  }

  private UserResponseFromHKR getUserApiResponseByUser(User user) {
    UserResponseFromHKR userApiResponse = new UserResponseFromHKR();
    if (user != null) {
      userApiResponse.setException(false);
      userApiResponse.setId(user.getId());
      userApiResponse.setNm(user.getName());
      userApiResponse.setUserHash(user.getUserHash());
      Integer orderCount = 0;
      try{
        orderCount = getOrderService().getOrderCountByUser(user.getId());
      }catch (Exception e){
        System.out.println(e.getStackTrace());
      }
      userApiResponse.setOrderCount(orderCount);
      if(user.getBirthDate()!=null) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateString = df.format(user.getBirthDate());
        userApiResponse.setBirthDt(dateString);
      }
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

  public OrderService getOrderService() {
    return orderService;
  }
}
