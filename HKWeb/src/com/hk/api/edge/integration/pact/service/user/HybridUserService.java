package com.hk.api.edge.integration.pact.service.user;

import com.hk.api.edge.integration.response.user.UserResponseFromHKR;

public interface HybridUserService {

  public boolean isTempUser(Long userId);

  public UserResponseFromHKR getUserResponseById(Long userId);

  public UserResponseFromHKR getUserResponseByLogin(String loginName);
}
