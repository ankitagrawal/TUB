package com.hk.api.edge.integration.pact.service.user;

import com.hk.api.edge.integration.response.user.UserResponseFromHKR;
import com.hk.api.edge.integration.response.user.UserTempResponse;

public interface HybridUserService {

  public UserTempResponse isTempUser(Long userId);

  public UserResponseFromHKR getUserResponseById(Long userId);

  public UserResponseFromHKR getUserResponseByLogin(String loginName);
}
