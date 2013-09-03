package com.hk.api.hkedge.pact.service;

import com.hk.api.hkedge.response.user.UserApiBaseResponse;
import com.hk.api.hkedge.response.user.UserApiResponse;

public interface HkCatalogUserService {

  public UserApiBaseResponse getLoggedInUser();

  public boolean isTempUser(Long userId);

  public UserApiResponse getUserResponseById(Long userId);

  public UserApiResponse getUserResponseByLogin(String loginName);
}
